
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

                console.log('bookPageNumber :: '+bookPageNumber);

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
            console.log(response);
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

}

function previewBookPdf(pdfPath) {
    var decodePdfPath = decodeURIComponent(pdfPath);

    console.log('pdfPath :: ' + pdfPath);
    console.log('decodePdfPath :: ' + decodePdfPath);
    var contextPath = $('meta[name="context-path"]').attr('content');
    var url = contextPath + '/api/files/preview';

    var data = {
        pdfPath: decodePdfPath,
        imagePath: null
    };

    postData(url, JSON.stringify(data), 'json', function(response) {
        if (response && response.fileContent) {
            console.log('response :: ', response);

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
