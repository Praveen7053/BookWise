

function openDflipViewer(pdfUrl, bookTitle) {
    // Extract the book encounter ID from the URL
    var bookEncounterId = pdfUrl.split('/').pop();
    
    // Start reading session tracking
    window.currentReadingSession = {
        bookEncounterId: parseInt(bookEncounterId),
        startTime: Date.now(),
        lastPage: 1,
        previouslySavedPage: 1  // Track the highest page saved to database
    };
    
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
            disableStream: true,
            // Add automatic page tracking
            onFlip: function(flipbook) {
                if (window.currentReadingSession && flipbook && flipbook.target && flipbook.target._activePage !== undefined) {
                    const currentPage = flipbook.target._activePage;
                    window.currentReadingSession.lastPage = currentPage;
                }
            },
            onReady: function(flipbook) {
                // Initialize page tracking when viewer is ready
                if (window.currentReadingSession && flipbook) {
                    try {
                        // Try different methods to get initial page
                        let currentPage = null;
                        
                        // Method 1: Try getPageNumber()
                        if (flipbook.getPageNumber && typeof flipbook.getPageNumber === 'function') {
                            currentPage = flipbook.getPageNumber();
                        }
                        
                        // Method 2: Try page property
                        if (!currentPage && flipbook.page !== undefined) {
                            currentPage = flipbook.page;
                        }
                        
                        // Method 3: Try currentPage property
                        if (!currentPage && flipbook.currentPage !== undefined) {
                            currentPage = flipbook.currentPage;
                        }
                        
                        // Method 4: Try to get from PDF viewer
                        if (!currentPage && flipbook.pdfViewer && flipbook.pdfViewer.currentPageNumber) {
                            currentPage = flipbook.pdfViewer.currentPageNumber;
                        }
                        
                        if (currentPage && currentPage > 0) {
                            window.currentReadingSession.lastPage = currentPage;
                        }
                    } catch (e) {
                        // Silent error handling
                    }
                }
            }
        };
        
        try {
            const flipbookInstance = $originalContainer.flipBook(pdfUrl, options);
            
            // Store the instance for proper cleanup
            $originalContainer.data('flipbook', flipbookInstance);
            window.currentDFlipInstance = flipbookInstance;
            
            // Add close button with better styling
            addCloseButton($originalContainer, 'dflip-container');
            
            // Manual update button is now hidden since auto-tracking works perfectly
            // if (window.currentReadingSession) {
            //     addProgressUpdateButton($originalContainer, 'dflip-container');
            // }
            
            // Set up periodic page checking (every 2 seconds)
            if (window.currentReadingSession) {
                window.pageCheckInterval = setInterval(function() {
                    checkCurrentPage();
                }, 2000); // Check every 2 seconds
            }
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
        disableStream: true,
        // Add automatic page tracking
        onFlip: function(flipbook) {
            if (window.currentReadingSession && flipbook && flipbook.target && flipbook.target._activePage !== undefined) {
                const currentPage = flipbook.target._activePage;
                window.currentReadingSession.lastPage = currentPage;
            }
        },
        onReady: function(flipbook) {
            // Initialize page tracking when viewer is ready
            if (window.currentReadingSession && flipbook) {
                try {
                    // Try different methods to get initial page
                    let currentPage = null;
                    
                    // Method 1: Try getPageNumber()
                    if (flipbook.getPageNumber && typeof flipbook.getPageNumber === 'function') {
                        currentPage = flipbook.getPageNumber();
                    }
                    
                    // Method 2: Try page property
                    if (!currentPage && flipbook.page !== undefined) {
                        currentPage = flipbook.page;
                    }
                    
                    // Method 3: Try currentPage property
                    if (!currentPage && flipbook.currentPage !== undefined) {
                        currentPage = flipbook.currentPage;
                    }
                    
                    // Method 4: Try to get from PDF viewer
                    if (!currentPage && flipbook.pdfViewer && flipbook.pdfViewer.currentPageNumber) {
                        currentPage = flipbook.pdfViewer.currentPageNumber;
                    }
                    
                    if (currentPage && currentPage > 0) {
                        window.currentReadingSession.lastPage = currentPage;
                    }
                } catch (e) {
                    // Silent error handling
                }
            }
        }
    };
    
    try {
        const flipbookInstance = $lightboxContainer.flipBook(pdfUrl, options);
        
        // Store the instance for proper cleanup
        $lightboxContainer.data('flipbook', flipbookInstance);
        window.currentDFlipInstance = flipbookInstance;
        
                    // Add close button with better styling
            addCloseButton($lightboxContainer, 'df-lightbox-container');
            
            // Manual update button is now hidden since auto-tracking works perfectly
            // if (window.currentReadingSession) {
            //     addProgressUpdateButton($lightboxContainer, 'df-lightbox-container');
            // }
            
            // Set up periodic page checking (every 2 seconds)
            if (window.currentReadingSession) {
                window.pageCheckInterval = setInterval(function() {
                    checkCurrentPage();
                }, 2000); // Check every 2 seconds
            }
    } catch (error) {
        alert('Error opening PDF viewer. Please try again.');
    }
}

// Function to update reading progress from DFlip viewer
function updateReadingProgressFromViewer() {
    if (!window.currentReadingSession) {
        return;
    }
    
    const sessionDuration = Math.round((Date.now() - window.currentReadingSession.startTime) / 60000); // Convert to minutes
    
    // Always get the latest page from flipbook.target._activePage if available
    if (window.currentDFlipInstance && window.currentDFlipInstance.target && window.currentDFlipInstance.target._activePage !== undefined) {
        window.currentReadingSession.lastPage = window.currentDFlipInstance.target._activePage;
    }
    
    let currentPage = window.currentReadingSession.lastPage || 1;
    
    // Always update progress - backend will handle backward progress prevention
    const contextPath = $('meta[name="context-path"]').attr('content');
    const url = `${contextPath}/api/bookshelf/update-progress`;
    
    const data = {
        bookEncounterId: window.currentReadingSession.bookEncounterId,
        currentPage: currentPage,
        readingTimeMinutes: sessionDuration,
        isCompleted: false
    };
    
    postData(url, JSON.stringify(data), 'json', function(response) {
        if (response && response.success) {
            // Update the previously saved page to current page
            window.currentReadingSession.previouslySavedPage = currentPage;
            // Refresh bookshelf if it's open
            if (typeof loadBookshelfData === 'function') {
                loadBookshelfData();
            }
        }
    }, function(error) {
        // Silent error handling
    });
}

// Function to check current page periodically
function checkCurrentPage() {
    if (!window.currentReadingSession || !window.currentDFlipInstance || !window.currentDFlipInstance.target) {
        return;
    }
    
    try {
        const currentPage = window.currentDFlipInstance.target._activePage;
        if (currentPage && currentPage > 0 && currentPage !== window.currentReadingSession.lastPage) {
            window.currentReadingSession.lastPage = currentPage;
        }
    } catch (e) {
        // Silent error handling
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
    
    // Set up periodic progress updates (every 5 minutes)
    if (window.currentReadingSession) {
        window.progressUpdateInterval = setInterval(function() {
            if (window.currentReadingSession) {
                updateReadingProgressFromViewer();
                // Restart the session timer
                window.currentReadingSession.startTime = Date.now();
            }
        }, 5 * 60 * 1000); // 5 minutes
    }
}

function addProgressUpdateButton(container, containerId) {
    // Remove any existing progress button
    container.find('.df-progress-btn').remove();
    
    // Add progress update button
    const progressButton = $(`
        <div class="df-progress-btn" style="
            position: absolute;
            top: 20px;
            right: 70px;
            width: 40px;
            height: 40px;
            background: rgba(0,128,0,0.7);
            color: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 16px;
            cursor: pointer;
            z-index: 10000;
            transition: all 0.3s ease;
            border: 2px solid rgba(255,255,255,0.3);
        " title="Update Reading Progress">
            📖
        </div>
    `);
    
    // Add hover effects
    progressButton.hover(
        function() {
            $(this).css({
                'background': 'rgba(0,128,0,0.9)',
                'border-color': 'rgba(255,255,255,0.8)',
                'transform': 'scale(1.1)'
            });
        },
        function() {
            $(this).css({
                'background': 'rgba(0,128,0,0.7)',
                'border-color': 'rgba(255,255,255,0.3)',
                'transform': 'scale(1)'
            });
        }
    );
    
    // Add click handler
    progressButton.click(function() {
        // Show prompt for current page only
        const currentPage = prompt('Enter your current page number:', window.currentReadingSession.lastPage || 1);
        if (currentPage && !isNaN(currentPage)) {
            const pageNum = parseInt(currentPage);
            window.currentReadingSession.lastPage = pageNum;
            updateReadingProgressFromViewer();
            // Show a brief success message
            const message = $('<div style="position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: rgba(0,128,0,0.9); color: white; padding: 10px 20px; border-radius: 5px; z-index: 10001;">Progress Updated!</div>');
            $('body').append(message);
            setTimeout(() => message.remove(), 2000);
        }
    });
    
    container.append(progressButton);
}

function closeDFlipViewer(containerId) {
    // Clear progress update interval
    if (window.progressUpdateInterval) {
        clearInterval(window.progressUpdateInterval);
        window.progressUpdateInterval = null;
    }
    
    // Clear page check interval
    if (window.pageCheckInterval) {
        clearInterval(window.pageCheckInterval);
        window.pageCheckInterval = null;
    }
    
    // Always get the latest page from flipbook.target._activePage before closing
    if (window.currentReadingSession && window.currentDFlipInstance && window.currentDFlipInstance.target && window.currentDFlipInstance.target._activePage !== undefined) {
        const finalPage = window.currentDFlipInstance.target._activePage;
        window.currentReadingSession.lastPage = finalPage;
    }
    
    // Update reading progress before closing
    if (window.currentReadingSession) {
        updateReadingProgressFromViewer();
    }
    
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