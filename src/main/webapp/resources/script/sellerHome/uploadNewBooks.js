
function handleUploadBook() {
    var fileInputCover = $('#bookCover')[0].files[0];
    var fileInputPdf = $('#bookPdf')[0].files[0];
    const isEdit = !!$('#bookEncounterIdHidden').val();

    var bookTitle = $('#bookTitle');
    var authorName = $('#authorName');
    var bookCategory = $('#bookCategory');
    var numberOfPages = $('#numberOfPages');

    // Reset previous error styles
    $('.input-error').removeClass('input-error');

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

    if (!fileInputCover && !isEdit) {
        errorMessages.push("Book cover must be selected.");
        $('#bookCover').addClass('input-error');
    }

    if (!fileInputPdf && !isEdit) {
        errorMessages.push("Book PDF must be selected.");
        $('#bookPdf').addClass('input-error');
    }

    if (errorMessages.length > 0) {
        showErrorAlert(errorMessages.join("<br>"));
        return;
    }

    // ✅ JSON metadata ONLY (NO files)
    var bookData = {
        bookEncounterId: $('#bookEncounterIdHidden').val(),
        bookTitle: bookTitle.val(),
        authorName: authorName.val(),
        isbnNumber: $('#isbnNumber').val(),
        bookPrice: $('#bookPrice').val(),
        bookCategory: bookCategory.val(),
        numberOfPages: numberOfPages.val(),
        publicationDate: $('#publicationDate').val(),
        bookLanguage: $('#bookLanguage').val(),
        bookDescription: $('#bookDescription').val()
    };

    // ✅ FormData for multipart
    var formData = new FormData();
    formData.append("bookData", JSON.stringify(bookData));

    if (fileInputCover) {
        formData.append("bookCover", fileInputCover);
    }

    if (fileInputPdf) {
        formData.append("bookPdf", fileInputPdf);
    }

    var contextPath = $('meta[name="context-path"]').attr('content');
    var url = contextPath + '/api/book/actions/saveUpdateNewBooks';

    showProgressBar("progressBarDiv", "bodyDiv");

    postData(url, formData, 'json', function (response) {
        closeProgressBar("progressBarDiv", "bodyDiv");
        if (response && response.success) {
            showSuccessAlert(response.message);
            clearUploadedNewBookFields();
        } else {
            showErrorAlert(response && response.message ? response.message : "Upload failed.");
        }
    }, function (jqXHR, textStatus, errorThrown) {
        closeProgressBar("progressBarDiv", "bodyDiv");
        var msg = "Upload failed.";
        if (errorThrown === "timeout" || (jqXHR && jqXHR.status === 0)) {
            msg = "Connection was aborted or timed out. Try a smaller file or check your connection.";
        } else if (errorThrown && typeof errorThrown === "string") {
            msg = errorThrown;
        }
        if (typeof showErrorAlert === 'function') {
            showErrorAlert(msg);
        } else {
            alert(msg);
        }
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


