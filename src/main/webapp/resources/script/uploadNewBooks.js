
$(document).ready(function() {
    var contextPath = $('meta[name="context-path"]').attr('content');
    var csrfToken = $('meta[name="csrf-token"]').attr('content');

    // Define URL for the API endpoint
    var url = contextPath + '/api/book/actions/saveUpdateNewBooks';

    $('#uploadBookForm').submit(function(event) {
        event.preventDefault();

        var fileInputCover = $('#bookCover')[0].files[0];
        var fileInputPdf = $('#bookPdf')[0].files[0];

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

            // Send POST request
            postData(url, JSON.stringify(jsonData), 'json', function(response) {
                if (response.success) {
                    showSuccessAlert(response.message);
                } else {
                    closeProgressBar("progressBarDiv", "bodyDiv");
                    showErrorAlert(response.message);
                }
            });
        });
    });
});

