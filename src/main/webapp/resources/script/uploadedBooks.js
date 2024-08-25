
function getUploadedBooks(){
    event.preventDefault();
    var contextPath = $('meta[name="context-path"]').attr('content');
    var csrfToken = $('meta[name="csrf-token"]').attr('content');
    var url = contextPath + '/api/book/actions/getSellerUploadedBooks';

    // Send GET request
    showProgressBar("progressBarDiv", "bodyDiv");
    getData(url, 'json', function(response) {
        if (response.success) {
            populateUploadedSellerBooks(response);
            closeProgressBar("progressBarDiv", "bodyDiv");
        } else {
            closeProgressBar("progressBarDiv", "bodyDiv");
            showErrorAlert(response.message);
        }
    });
}

function populateUploadedSellerBooks(response) {
    if (typeof response === 'object' && response.success) {
        const books = response.books;

        // Check if books is an array
        if (Array.isArray(books)) {
            const container = document.getElementById('bookUploadedListMainDIV');
            container.innerHTML = '';

            let bookItem = "";
            books.forEach(book => {
                // Use encodeURIComponent to escape values
                const bookTitle = encodeURIComponent(book.bookTitle || '');
                const bookAuthor = encodeURIComponent(book.bookAuthor || '');
                const bookIsbnNumber = encodeURIComponent(book.bookIsbnNumber || '');
                const bookPrice = encodeURIComponent(book.bookPrice || '');
                const bookCategory = encodeURIComponent(book.bookCategory || '');
                const bookPageNumber = encodeURIComponent(book.bookPageNumber || '');
                const pdfPath = book.pdfPath ? encodeURIComponent(book.pdfPath) : '';
                const frontPageImagePath = book.frontPageImagePath ? encodeURIComponent(book.frontPageImagePath) : '';
                const publicationDate = book.publicationDate ? encodeURIComponent(book.publicationDate) : '';
                const bookLanguage = book.bookLanguage ? encodeURIComponent(book.bookLanguage) : '';
                const bookDescription = book.bookDescription ? encodeURIComponent(book.bookDescription) : '';

                bookItem += '<div class="book-list-item">' +
                    '<div class="col-number">' + (book.bookEncounterId || '') + '</div>' +
                    '<div>' + decodeURIComponent(bookTitle) + '</div>' +
                    '<div>' + decodeURIComponent(bookAuthor) + '</div>' +
                    '<div>' + decodeURIComponent(bookIsbnNumber) + '</div>' +
                    '<div>' + decodeURIComponent(bookPrice) + '</div>' +
                    '<div>' + decodeURIComponent(bookCategory) + '</div>' +
                    '<div class="col-actions">' +
                        '<a class="btn btn-info btn-sm" onclick="previewBookPdf(\'' +encodeURIComponent(book.pdfPath)+ '\');"><i class="fa fa-eye"></i></a>' +
                        '<a onclick="editUploadedBooks(' +
                            book.bookEncounterId + ',\'' +
                            bookTitle + '\',\'' +
                            bookAuthor + '\',\'' +
                            bookIsbnNumber + '\',\'' +
                            bookPrice + '\',\'' +
                            bookCategory + '\',\'' +
                            pdfPath + '\',\'' +
                            frontPageImagePath + '\',\'' +
                            bookPageNumber + '\',\'' +
                            publicationDate + '\',\'' +
                            bookLanguage + '\',\'' +
                            bookDescription +
                        '\');" class="btn btn-warning btn-sm">' +
                            '<i class="fa fa-pencil"></i>' +
                        '</a>' +
                        '<a onclick="deleteUploadedBooks(' + book.bookEncounterId + ');" class="btn btn-danger btn-sm">' +
                            '<i class="fa fa-trash"></i>' +
                        '</a>' +
                    '</div>' +
                '</div>';
            });

            container.innerHTML = bookItem;
        } else {
            console.error('Expected books to be an array but got:', books);
        }
    } else {
        console.error('Invalid response format or success flag is false:', response);
    }
}

function deleteUploadedBooks(bookId) {
    jConfirm('Are you sure you want to delete this book?', function(response) {
        var contextPath = $('meta[name="context-path"]').attr('content');
        var url = contextPath + '/api/book/actions/deleteUploadedBooks';
        var jsonData = { bookId: bookId };

        // Assuming deleteData is your AJAX function
        deleteData(url, JSON.stringify(jsonData), 'json', function(response) {
            if (response.success) {
                showSuccessAlert(response.message);
                getUploadedBooks();
            } else {
                showErrorAlert(response.message);
            }
        });
    });
}

function editUploadedBooks(bookEncounterId, bookTitle, bookAuthor, bookIsbnNumber, bookPrice, bookCategory, pdfPath, frontPageImagePath, bookPageNumber,publicationDate,bookLanguage,bookDescription) {

    // Set values to form fields
    selectMenuTabs('uploadTab');
    changeModulePage('uploadBooks');

    document.getElementById('bookEncounterIdHidden').value = bookEncounterId;
    document.getElementById('bookTitle').value = decodeURIComponent(bookTitle);
    document.getElementById('authorName').value = decodeURIComponent(bookAuthor);
    document.getElementById('isbnNumber').value = decodeURIComponent(bookIsbnNumber);
    document.getElementById('bookPrice').value = decodeURIComponent(bookPrice);
    document.getElementById('bookCategory').value = decodeURIComponent(bookCategory);
    document.getElementById('numberOfPages').value = decodeURIComponent(bookPageNumber);
    document.getElementById('publicationDate').value = formatDate(publicationDate);
    document.getElementById('bookLanguage').value = decodeURIComponent(bookLanguage);
    document.getElementById('bookDescription').value = decodeURIComponent(bookDescription);

    editImageNBookPDf(pdfPath, frontPageImagePath);
}

function editImageNBookPDf(pdfPath, frontPageImagePath) {
    const fileRequest = {
        imagePath: decodeURIComponent(frontPageImagePath),
        pdfPath: decodeURIComponent(pdfPath)
    };

    var contextPath = $('meta[name="context-path"]').attr('content');
    var url = contextPath + "/api/files/getBookImageNPdf";

    showProgressBar("progressBarDiv", "bodyDiv");
    postData(url, JSON.stringify(fileRequest), 'json', function(response) {
        // Handle image response
        if (response.imageMimeType && response.imageMimeType.startsWith('image/')) {
            const currentCoverImage = document.getElementById('currentCoverImage');
            currentCoverImage.src = 'data:' + response.imageMimeType + ';base64,' + response.imageContent;
            currentCoverImage.style.display = 'block';
        }

        // Handle PDF response
        if (response.pdfMimeType === 'application/pdf') {
            renderPdfFromBase64(response.pdfContent);
        }

        closeProgressBar("progressBarDiv", "bodyDiv");
    });
}


function renderPdfFromBase64(base64String) {
    const pdfData = atob(base64String);
    const pdfCanvas = document.getElementById('pdfCanvas');
    const pdfContext = pdfCanvas.getContext('2d');

    pdfjsLib.getDocument({ data: pdfData }).promise.then(pdf => {
        pdf.getPage(1).then(page => {
            const viewport = page.getViewport({ scale: 1 });
            pdfCanvas.width = viewport.width;
            pdfCanvas.height = viewport.height;

            const renderContext = {
                canvasContext: pdfContext,
                viewport: viewport
            };
            page.render(renderContext);
        });
    }).catch(error => {
        console.error('Error rendering PDF: ', error);
        clearPdfCanvas();
    });
}

function clearPdfCanvas() {
    const pdfCanvas = document.getElementById('pdfCanvas');
    const pdfContext = pdfCanvas.getContext('2d');
    pdfContext.clearRect(0, 0, pdfCanvas.width, pdfCanvas.height);
}

function previewBookPdf(pdfPath) {
    var decodePdfPath = decodeURIComponent(pdfPath);

    var contextPath = $('meta[name="context-path"]').attr('content');
    var url = contextPath + '/api/files/preview';

    var data = {
        pdfPath: decodePdfPath,
        imagePath: null
    };

    postData(url, JSON.stringify(data), 'json', function(response) {
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
            showPdfInModal(fileUrl);
        } else {
            showErrorAlert("Failed to load PDF");
        }
    });
}

function showPdfInModal(fileUrl) {
    var modalHtml = `
        <div id="pdfModal" class="modal" style="display: block; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.8); z-index: 1000;">
            <div class="modal-content" style="position: relative; margin: auto; padding: 20px; width: 80%; height: 80%; background: white;">
                <span class="close" style="position: absolute; top: 10px; right: 25px; font-size: 35px; font-weight: bold; cursor: pointer;">&times;</span>
                <iframe src="${fileUrl}" style="width: 100%; height: 100%;" frameborder="0"></iframe>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', modalHtml);

    document.querySelector('#pdfModal .close').addEventListener('click', function() {
        document.getElementById('pdfModal').remove();
        URL.revokeObjectURL(fileUrl); // Clean up the object URL
    });
}
