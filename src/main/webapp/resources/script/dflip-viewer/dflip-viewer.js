

function openDflipViewer(pdfUrl, bookTitle) {
    // Extract the book encounter ID from the URL
    var bookEncounterId = pdfUrl.split('/').pop();
    
    // Use the new preview endpoint that takes bookEncounterId directly
    var contextPath = $('meta[name="context-path"]').attr('content');
    var url = contextPath + '/api/bookWiseDflipView/book/preview/' + bookEncounterId;
    
    // Use the same pattern as previewBookPdf
    postData(url, '{}', 'json', function(response) {
        if (response && response.fileContent) {
            // Decode the Base64 encoded PDF content
            var byteCharacters = atob(response.fileContent);
            var byteArrays = [];

            for (var offset = 0; offset < byteCharacters.length; offset += 512) {
                var slice = byteCharacters.slice(offset, offset + 512);
                var byteNumbers = new Array(slice.length);
                for (var i = 0; i < slice.length; i++) {
                    byteNumbers[i] = slice.charCodeAt(i);
                }
                var byteArray = new Uint8Array(byteNumbers);
                byteArrays.push(byteArray);
            }

            var blob = new Blob(byteArrays, { type: 'application/pdf' });
            var fileUrl = URL.createObjectURL(blob);
            
            // Use DFlip instead of modal
            initializeDFlipViewer(fileUrl, bookTitle);
        } else {
            alert("Failed to load PDF");
        }
    });
}

function resetDFlipContainers() {
    // Reset the original container
    const $originalContainer = $('#dflip-container');
    if ($originalContainer.length > 0) {
        $originalContainer.empty().show();
    }
    
    // Reset the lightbox container
    const $lightboxContainer = $('#df-lightbox-container');
    if ($lightboxContainer.length > 0) {
        $lightboxContainer.empty().show();
    }
    
    // Clear any stored instances
    window.currentDFlipInstance = null;
}

function initializeDFlipViewer(pdfUrl, bookTitle) {
    // Reset containers first to ensure clean state
    resetDFlipContainers();
    
    // Try using the original dflip-container first
    const $originalContainer = $('#dflip-container');
    if ($originalContainer.length > 0) {
        $originalContainer.show().css({
            'position': 'fixed',
            'top': '0',
            'left': '0',
            'width': '100%',
            'height': '100%',
            'z-index': '9999',
            'background-color': 'rgba(0,0,0,0.9)'
        });
        
        const options = {
            source: pdfUrl,
            bookTitle: bookTitle || "BookWise Reader",
            webgl: false,
            height: '100%',
            dFlipLocation: DF_FLIP_LOCATION,
            backgroundColor: "#f8f9fa",
            controlsPosition: 'bottom',
            enableDownload: true,
            isLightBox: false,
            forceFit: true,
            transparent: false,
            disableFontFace: true,
            disableAutoFetch: true,
            disableStream: true
        };
        
        try {
            const flipbookInstance = $originalContainer.flipBook(pdfUrl, options);
            
            // Store the instance for proper cleanup
            $originalContainer.data('flipbook', flipbookInstance);
            window.currentDFlipInstance = flipbookInstance;
            
            // Add close button with better styling
            addCloseButton($originalContainer, 'dflip-container');
        } catch (error) {
            alert('Error opening PDF viewer. Please try again.');
        }
        return;
    }
    
    // Fallback to custom container approach
    if ($('#df-lightbox-container').length === 0) {
        $('body').append('<div id="df-lightbox-container" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; z-index:9999; background:rgba(0,0,0,0.9);"></div>');
    }
    
    const $lightboxContainer = $('#df-lightbox-container');
    $lightboxContainer.show();

    const options = {
        source: pdfUrl,
        bookTitle: bookTitle || "BookWise Reader",
        webgl: false,
        height: '100%',
        dFlipLocation: DF_FLIP_LOCATION,
        backgroundColor: "#f8f9fa",
        controlsPosition: 'bottom',
        enableDownload: true,
        isLightBox: false,
        forceFit: true,
        transparent: false,
        disableFontFace: true,
        disableAutoFetch: true,
        disableStream: true
    };
    
    try {
        const flipbookInstance = $lightboxContainer.flipBook(pdfUrl, options);
        
        // Store the instance for proper cleanup
        $lightboxContainer.data('flipbook', flipbookInstance);
        window.currentDFlipInstance = flipbookInstance;
        
        // Add close button with better styling
        addCloseButton($lightboxContainer, 'df-lightbox-container');
    } catch (error) {
        alert('Error opening PDF viewer. Please try again.');
    }
}

function addCloseButton(container, containerId) {
    // Remove any existing close button
    container.find('.df-close-btn').remove();
    
    // Add close button with better styling
    const closeButton = $(`
        <div class="df-close-btn" style="
            position: absolute;
            top: 20px;
            right: 20px;
            width: 40px;
            height: 40px;
            background: rgba(0,0,0,0.7);
            color: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            cursor: pointer;
            z-index: 10000;
            transition: all 0.3s ease;
            border: 2px solid rgba(255,255,255,0.3);
        " title="Close PDF Viewer (ESC)">
            ×
        </div>
    `);
    
    // Add hover effects
    closeButton.hover(
        function() {
            $(this).css({
                'background': 'rgba(255,0,0,0.8)',
                'border-color': 'rgba(255,255,255,0.8)',
                'transform': 'scale(1.1)'
            });
        },
        function() {
            $(this).css({
                'background': 'rgba(0,0,0,0.7)',
                'border-color': 'rgba(255,255,255,0.3)',
                'transform': 'scale(1)'
            });
        }
    );
    
    // Add click handler
    closeButton.click(function() {
        closeDFlipViewer(containerId);
    });
    
    container.append(closeButton);
    
    // Add keyboard support for ESC key
    $(document).off('keydown.dflip').on('keydown.dflip', function(e) {
        if (e.key === 'Escape') {
            closeDFlipViewer(containerId);
        }
    });
    
    // Add click outside to close (optional)
    container.off('click.outside').on('click.outside', function(e) {
        if (e.target === this) {
            closeDFlipViewer(containerId);
        }
    });
}

function closeDFlipViewer(containerId) {
    // Remove keyboard event listener
    $(document).off('keydown.dflip');
    
    // Close the container and properly destroy DFlip instance
    if (containerId === 'dflip-container') {
        const $container = $('#dflip-container');
        
        // Try to destroy the DFlip instance if it exists
        try {
            // Use stored instance first
            if (window.currentDFlipInstance && window.currentDFlipInstance.destroy) {
                window.currentDFlipInstance.destroy();
                window.currentDFlipInstance = null;
            }
            // Fallback to container data
            else if ($container.data('flipbook') && $container.data('flipbook').destroy) {
                $container.data('flipbook').destroy();
                $container.removeData('flipbook');
            }
            // Try global dFlip
            else if (window.dFlip && window.dFlip.destroy) {
                window.dFlip.destroy();
            }
        } catch (e) {
            // Silent cleanup - no need to log errors in production
        }
        
        // Clear the container content but keep the container itself
        $container.empty().hide();
        
    } else if (containerId === 'df-lightbox-container') {
        const $container = $('#df-lightbox-container');
        
        // Try to destroy the DFlip instance if it exists
        try {
            // Use stored instance first
            if (window.currentDFlipInstance && window.currentDFlipInstance.destroy) {
                window.currentDFlipInstance.destroy();
                window.currentDFlipInstance = null;
            }
            // Fallback to container data
            else if ($container.data('flipbook') && $container.data('flipbook').destroy) {
                $container.data('flipbook').destroy();
                $container.removeData('flipbook');
            }
            // Try global dFlip
            else if (window.dFlip && window.dFlip.destroy) {
                window.dFlip.destroy();
            }
        } catch (e) {
            // Silent cleanup - no need to log errors in production
        }
        
        // Clear the container content but keep the container itself
        $container.empty().hide();
    }
    
    // Remove any remaining DFlip-related elements that might be orphaned
    // But be more selective - don't remove the main containers
    $('.df-book-wrapper:not(.df-container *)').remove();
    $('.df-book:not(.df-container *)').remove();
    
    // Remove any orphaned iframes that might be from previous sessions
    $('iframe[src*="blob:"]:not(.df-container *)').remove();
    $('embed[src*="blob:"]:not(.df-container *)').remove();
    $('object[data*="blob:"]:not(.df-container *)').remove();
    
    // Revoke any blob URLs to free memory
    if (window.URL && window.URL.revokeObjectURL) {
        // Note: In a production app, you'd track specific blob URLs
    }
}