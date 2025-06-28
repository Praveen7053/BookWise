
$(document).ready(function() {
    loadAllBooks();
    loadSidebarProfileImage();
});

function loadAllBooks() {
    var contextPath = $('meta[name="context-path"]').attr('content');

    const requestData = {
        title: document.getElementById("searchBookByName").value,
        bookId: '',
        timeFilter: '',
        sortBy: '',
        page: 0,
        size: 40,
        uploadedByName: ''
    };

    const url = contextPath + '/api/book/userHome/actions/getAllBooksUser';
    showProgressBar("progressBarDiv", "bodyDiv");
    postData(url, JSON.stringify(requestData), 'json', (response) => {
        closeProgressBar("progressBarDiv", "bodyDiv");
        handleHomeBooksResponse(response);
//        if (response.totalPages > 0) {
//            setupPagination(response.currentPage, response.totalPages);
//        }
    });
}

function handleHomeBooksResponse(response) {
    const booksGrid = document.getElementById('allUserBooks');
    booksGrid.innerHTML = '';

    if (!response.books || response.books.length === 0) {
        booksGrid.innerHTML = `
            <div class="col-12 text-center py-5">
                <h3 class="text-muted">No books found</h3>
            </div>
        `;
        return;
    }

    response.books.forEach((book, index) => {
        if (index % 4 === 0) {
            currentRow = document.createElement('div');
            currentRow.className = 'row mb-4';
            booksGrid.appendChild(currentRow);
        }

        const bookCard = `
            <div class="col-md-4 col-lg-3 mb-4">
                <div class="card book-card shadow-sm rounded-lg">
                    <img src="data:${book.coverImageMimeType};base64,${book.coverImageContent}"
                         class="book-cover card-img-top"
                         alt="${book.bookTitle}">

                    <div class="card-body">
                        <h5 class="card-title text-primary">${book.bookTitle}</h5>
                        <p class="card-text small text-muted">By ${book.bookAuthor}</p>
                    </div>
                    <div class="card-footer text-end bg-light">
                        <a href="#" class="btn btn-sm btn-primary rounded-pill">Read <i class="fas fa-arrow-right ms-1"></i></a>
                    </div>
                </div>
            </div>
        `;

        currentRow.innerHTML += bookCard;
    });
}