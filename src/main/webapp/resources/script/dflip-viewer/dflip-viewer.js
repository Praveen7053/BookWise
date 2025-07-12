

function openDflipViewer(pdfUrl, bookTitle) {
    console.log('openDflipViewer called:', { pdfUrl, bookTitle });
    
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

function initializeDFlipViewer(pdfUrl, bookTitle) {
    // Try using the original dflip-container first
    const $originalContainer = $('#dflip-container');
    if ($originalContainer.length > 0) {
        console.log('Using original dflip-container...');
        $originalContainer.show().css({
            'position': 'fixed',
            'top': '0',
            'left': '0',
            'width': '100%',
            'height': '100%',
            'z-index': '9999',
            'background-color': 'rgba(0,0,0,0.8)'
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
        
        console.log('DFlip options:', options);
        
        try {
            console.log('Calling flipBook on original container...');
            $originalContainer.flipBook(pdfUrl, options);
            console.log('flipBook called successfully');
            
            // Add close button
            $originalContainer.append('<div style="position:absolute; top:20px; right:20px; color:white; font-size:24px; cursor:pointer; z-index:10000;" onclick="$(\'#dflip-container\').hide();">×</div>');
        } catch (error) {
            console.error('Error initializing DFlip viewer:', error);
            alert('Error opening PDF viewer. Please try again.');
        }
        return;
    }
    
    // Fallback to custom container approach
    console.log('Creating custom lightbox container...');
    if ($('#df-lightbox-container').length === 0) {
        $('body').append('<div id="df-lightbox-container" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; z-index:9999; background:rgba(0,0,0,0.8);"></div>');
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

    console.log('DFlip options:', options);
    
    try {
        console.log('Calling flipBook...');
        $lightboxContainer.flipBook(pdfUrl, options);
        console.log('flipBook called successfully');
        
        // Add close button
        $lightboxContainer.append('<div style="position:absolute; top:20px; right:20px; color:white; font-size:24px; cursor:pointer; z-index:10000;" onclick="$(\'#df-lightbox-container\').hide().empty();">×</div>');
    } catch (error) {
        console.error('Error initializing DFlip viewer:', error);
        alert('Error opening PDF viewer. Please try again.');
    }
}