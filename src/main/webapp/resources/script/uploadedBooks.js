
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
                        '<a class="btn btn-info btn-sm"><i class="fa fa-eye"></i></a>' +
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

    document.getElementById('publicationDate').value = decodeURIComponent(publicationDate);
    document.getElementById('bookLanguage').value = decodeURIComponent(bookLanguage);
    document.getElementById('bookDescription').value = decodeURIComponent(bookDescription);

}
