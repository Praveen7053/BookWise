
function getUploadedBooks(){
    event.preventDefault();
    var contextPath = $('meta[name="context-path"]').attr('content');
    var csrfToken = $('meta[name="csrf-token"]').attr('content');
    var url = contextPath + '/api/book/actions/getSellerUploadedBooks';

    // Send GET request
    getData(url, 'json', function(response) {
        if (response.success) {
            populateUploadedSellerBooks(response);
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

            books.forEach(book => {
                const bookItem = `
                    <div class="book-list-item">
                        <div class="col-number">${book.bookEncounterId}</div>
                        <div>${book.bookTitle}</div>
                        <div>${book.bookAuthor}</div>
                        <div>${book.bookIsbnNumber}</div>
                        <div>${book.bookPrice}</div>
                        <div>${book.bookCategory}</div>
                        <div class="col-actions">
                            <a href="#" class="btn btn-info btn-sm"><i class="fa fa-eye"></i></a>
                            <a href="#" class="btn btn-warning btn-sm"><i class="fa fa-pencil"></i></a>
                            <a href="#" class="btn btn-danger btn-sm"><i class="fa fa-trash"></i></a>
                        </div>
                    </div>
                `;
                container.insertAdjacentHTML('beforeend', bookItem);
            });
        } else {
            console.error('Expected books to be an array but got:', books);
        }
    } else {
        console.error('Invalid response format or success flag is false:', response);
    }
}