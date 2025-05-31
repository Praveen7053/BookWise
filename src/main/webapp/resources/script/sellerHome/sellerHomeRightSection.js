

function searchUserBook() {
    fetchSellerBooks(1); // Reset to first page when searching
}

function fetchSellerBooks(page = 1) {
    var contextPath = $('meta[name="context-path"]').attr('content');

    const requestData = {
        title: document.getElementById("searchTitle").value,
        bookId: document.getElementById("searchBookId").value,
        timeFilter: document.getElementById("timeFilter").value,
        sortBy: document.getElementById("sortBy").value,
        page: page,
        size: 8,
        uploadedByName: ''
    };

    const url = contextPath + '/api/book/actions/getSellerBooks';

    postData(url, JSON.stringify(requestData), 'json', (response) => {
        handleBooksResponse(response);
        if (response.totalPages > 0) {
            setupPagination(response.currentPage, response.totalPages);
        }
    });
}

function setupPagination(currentPage, totalPages) {
    const pageNumbers = document.getElementById('pageNumbersContainer');
    const prevPageBtn = document.getElementById('prevPageBtn');
    const nextPageBtn = document.getElementById('nextPageBtn');
    const firstEllipsis = document.getElementById('firstEllipsis');
    const lastEllipsis = document.getElementById('lastEllipsis');

    // Clear existing page numbers
    pageNumbers.innerHTML = '';

    // Disable/Enable previous and next buttons
    if (currentPage === 1) {
        prevPageBtn.classList.add('disabled');
    } else {
        prevPageBtn.classList.remove('disabled');
    }

    if (currentPage === totalPages) {
        nextPageBtn.classList.add('disabled');
    } else {
        nextPageBtn.classList.remove('disabled');
    }

    // Set up click handlers for prev/next
    document.getElementById('prevPageLink').onclick = () => currentPage > 1 && fetchSellerBooks(currentPage - 1);
    document.getElementById('nextPageLink').onclick = () => currentPage < totalPages && fetchSellerBooks(currentPage + 1);

    let startPage = 1;
    let endPage = totalPages;
    const maxVisiblePages = 5;

    if (totalPages > maxVisiblePages) {
        // Calculate start and end pages
        if (currentPage <= 3) {
            endPage = maxVisiblePages;
        } else if (currentPage >= totalPages - 2) {
            startPage = totalPages - maxVisiblePages + 1;
        } else {
            startPage = currentPage - 2;
            endPage = currentPage + 2;
        }

        // Show/hide ellipses
        firstEllipsis.style.display = startPage <= 2 ? 'none' : 'block';
        lastEllipsis.style.display = endPage >= totalPages - 1 ? 'none' : 'block';
    } else {
        firstEllipsis.style.display = 'none';
        lastEllipsis.style.display = 'none';
    }

    // Generate page numbers
    for (let i = startPage; i <= endPage; i++) {
        const pageItem = document.createElement('li');
        pageItem.id = `page${i}`;
        pageItem.className = i === currentPage ? 'active' : '';
        pageItem.innerHTML = `
            <a href="javascript:void(0);" onclick="fetchSellerBooks(${i})">${i}</a>
        `;
        pageNumbers.appendChild(pageItem);
    }
}

function handleBooksResponse(response) {
    const booksGrid = document.getElementById('booksGrid');
    booksGrid.innerHTML = ''; // Clear existing content

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
            <div class="col-md-3">
                <div class="card book-card position-relative h-100">
                    <img src="data:${book.coverImageMimeType};base64,${book.coverImageContent}"
                         class="book-cover card-img-top"
                         alt="${book.bookTitle}">
                    <div class="book-status">
                        <span class="badge ${book.status === 'ACTIVE' ? 'bg-success' : 'bg-secondary'}">
                            ${book.status}
                        </span>
                    </div>
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title text-truncate">${book.bookTitle}</h5>
                        <p class="card-text mb-1">By ${book.bookAuthor}</p>
                        <p class="card-text mb-1">ISBN: ${book.bookIsbnNumber}</p>
                        <p class="card-text mb-1">Category: ${book.bookCategory}</p>
                        <h6 class="card-text mt-auto mb-2">₹${book.bookPrice}</h6>
                        <div class="text-muted small">
                            <i class="far fa-clock"></i> ${formatDate(book.uploadedTime)}
                        </div>
                    </div>
                    <div class="book-actions">
                        <div class="btn-group" role="group">
                            <button class="btn btn-primary btn-sm" onclick="editBook(${book.bookEncounterId})">
                                <i class="fas fa-edit"></i> Edit
                            </button>
                            <button class="btn btn-danger btn-sm" onclick="deleteBook(${book.bookEncounterId})">
                                <i class="fas fa-trash"></i> Delete
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        currentRow.innerHTML += bookCard;
    });
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}
