
function handleUploadBook() {
    var fileInputCover = $('#bookCover')[0].files[0];
    var fileInputPdf = $('#bookPdf')[0].files[0];

    var bookTitle = $('#bookTitle');
    var authorName = $('#authorName');
    var bookCategory = $('#bookCategory');
    var numberOfPages = $('#numberOfPages');

    // Reset previous error styles
    bookTitle.removeClass('input-error');
    authorName.removeClass('input-error');
    bookCategory.removeClass('input-error');
    numberOfPages.removeClass('input-error');
    $('#bookCover').removeClass('input-error');
    $('#bookPdf').removeClass('input-error');

    // Perform validation before reading files
    var errorMessages = [];

    if (!bookTitle.val()) {
        errorMessages.push("Book title is required.");
        bookTitle.addClass('input-error');
    }

    if (!authorName.val()) {
        errorMessages.push("Author name is required.");
        authorName.addClass('input-error');
    }

    if (!bookCategory.val()) {
        errorMessages.push("Book category is required.");
        bookCategory.addClass('input-error');
    }

    if (!numberOfPages.val()) {
        errorMessages.push("Number of pages is required.");
        numberOfPages.addClass('input-error');
    }

    if (!fileInputCover) {
        errorMessages.push("Book cover must be selected.");
        $('#bookCover').addClass('input-error');
    }

    if (!fileInputPdf) {
        errorMessages.push("Book PDF must be selected.");
        $('#bookPdf').addClass('input-error');
    }

    if (errorMessages.length > 0) {
        showErrorAlert(errorMessages.join("<br>"));
        return;
    }

    var readerCover = fileInputCover ? new FileReader() : null;
    var readerPdf = fileInputPdf ? new FileReader() : null;

    var coverPromise = new Promise((resolve) => {
        if (readerCover) {
            readerCover.onloadend = function() {
                resolve(readerCover.result);
            };
            readerCover.readAsDataURL(fileInputCover);
        } else {
            resolve(null); // No cover file, resolve with null
        }
    });

    var pdfPromise = new Promise((resolve) => {
        if (readerPdf) {
            readerPdf.onloadend = function() {
                resolve(readerPdf.result);
            };
            readerPdf.readAsDataURL(fileInputPdf);
        } else {
            resolve(null); // No PDF file, resolve with null
        }
    });

    Promise.all([coverPromise, pdfPromise]).then(([coverData, pdfData]) => {
        var jsonData = {
            bookEncounterId: $('#bookEncounterIdHidden').val(),
            bookTitle: $('#bookTitle').val(),
            authorName: $('#authorName').val(),
            isbnNumber: $('#isbnNumber').val(),
            bookPrice: $('#bookPrice').val(),
            bookCategory: $('#bookCategory').val(),
            numberOfPages: $('#numberOfPages').val(),
            publicationDate: $('#publicationDate').val(),
            bookLanguage: $('#bookLanguage').val(),
            bookDescription: $('#bookDescription').val(),
            bookCover: coverData,
            bookPdf: pdfData
        };

        var contextPath = $('meta[name="context-path"]').attr('content');
        var url = contextPath + '/api/book/actions/saveUpdateNewBooks';

        // Send POST request
        postData(url, JSON.stringify(jsonData), 'json', function(response) {
            if (response.success) {
                showSuccessAlert(response.message);
                clearUploadedNewBookFields();
            } else {
                closeProgressBar("progressBarDiv", "bodyDiv");
                showErrorAlert(response.message);
            }
        });
    });
}


// Preview Book Cover Image
document.getElementById('bookCover').addEventListener('change', function(event) {
    const image = document.getElementById('currentCoverImage');
    const file = event.target.files[0];

    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            image.src = e.target.result;
            image.style.width = '100px';
            image.style.height = 'auto';
        };
        reader.readAsDataURL(file);
    }
});

// Preview First Page of Book PDF
document.getElementById('bookPdf').addEventListener('change', function(event) {
    const file = event.target.files[0];

    if (file && file.type === 'application/pdf') {
        const fileReader = new FileReader();

        fileReader.onload = function() {
            const pdfData = new Uint8Array(this.result);
            const loadingTask = pdfjsLib.getDocument({ data: pdfData });

            loadingTask.promise.then(function(pdf) {
                // Fetch the first page
                pdf.getPage(1).then(function(page) {
                    const scale = 1;
                    const viewport = page.getViewport({ scale: scale });

                    // Prepare canvas using PDF page dimensions
                    const canvas = document.getElementById('pdfCanvas');
                    const context = canvas.getContext('2d');
                    canvas.height = viewport.height;
                    canvas.width = viewport.width;

                    // Render PDF page into canvas context
                    const renderContext = {
                        canvasContext: context,
                        viewport: viewport
                    };
                    page.render(renderContext);
                });
            });
        };
        fileReader.readAsArrayBuffer(file);
    }
});

function clearUploadedNewBookFields() {
    // Clear form fields
    document.getElementById('bookEncounterIdHidden').value = '0';
    document.getElementById('bookTitle').value = '';
    document.getElementById('authorName').value = '';
    document.getElementById('isbnNumber').value = '';
    document.getElementById('bookPrice').value = '';
    document.getElementById('bookCategory').value = '';
    document.getElementById('numberOfPages').value = '';
    document.getElementById('publicationDate').value = '';
    document.getElementById('bookLanguage').value = '';
    document.getElementById('bookDescription').value = '';

    // Clear book cover image field and reset the preview
    document.getElementById('bookCover').value = '';
    document.getElementById('currentCoverImage').src = 'resources/images/defaultImage.png';

    // Clear book PDF field and reset the preview
    document.getElementById('bookCoverFileName').value = '';
    document.getElementById('bookPdfFileName').value = '';
    clearPdfEditedCanvas();
}

// Helper function to clear the PDF canvas
function clearPdfEditedCanvas() {
    const pdfCanvas = document.getElementById('pdfCanvas');
    const pdfContext = pdfCanvas.getContext('2d');
    pdfContext.clearRect(0, 0, pdfCanvas.width, pdfCanvas.height);
    //pdfCanvas.style.display = 'none';
}

$(document).ready(function() {
    // Handle full-screen image view
    $('#currentCoverImage').on('click', function() {
        const fullScreenImage = $('#fullScreenImage');
        fullScreenImage.attr('src', $(this).attr('src'));
        $('#imageModal').modal('show');
    });

    // Handle full-screen PDF view
    $('#pdfCanvas').on('click', function() {
        const canvas = $('#fullScreenPdfCanvas')[0];
        const context = canvas.getContext('2d');

        // Copy the canvas content from the preview to the full-screen canvas
        canvas.width = this.width;
        canvas.height = this.height;
        context.drawImage(this, 0, 0, this.width, this.height);

        $('#pdfModal').modal('show');
    });
});


