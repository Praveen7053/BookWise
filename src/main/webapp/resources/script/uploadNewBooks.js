
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

