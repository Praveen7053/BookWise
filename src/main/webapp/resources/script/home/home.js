
$(document).ready(function() {
    loadAllBooks();
    loadSidebarProfileImage();
    // Sidebar toggle logic is now handled by readerCommon.js
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
                        <a href="#" onclick="loadBookDetailsByID(${book.bookEncounterId});" class="btn btn-sm btn-primary rounded-pill">Read <i class="fas fa-arrow-right ms-1"></i></a>
                    </div>
                </div>
            </div>
        `;

        currentRow.innerHTML += bookCard;
    });
}

// Navigation functions
function changeModulePage(moduleName) {
    // Navigation to module page
    
    // Hide all pages first
    hideAllPages();
    
    // Update sidebar active state
    updateSidebarActiveState(moduleName);
    
    // Show the selected page
    switch(moduleName) {
        case 'readerUserHome':
            // Show home page
            showHomePage();
            break;
        case 'myBookshelf':
            // Show bookshelf page
            showMyBookshelf();
            break;
        case 'userProfileReader':
            // Show user profile page
            showUserProfile();
            break;
        default:
            // Default: showing home page
            showHomePage();
    }
}

function hideAllPages() {
    // Hide all pages
    // Hide all main content areas
    const pages = [
        'readerHomeMainDiv',
        'myBookshelfPage',
        'mainUserProfilePageMainDIV'
    ];
    
    pages.forEach(pageId => {
        const element = document.getElementById(pageId);
        if (element) {
            element.style.display = 'none';
        } else {
            console.warn('Page element not found:', pageId);
        }
    });
}

function showHomePage() {
    const homeDiv = document.getElementById('readerHomeMainDiv');
    if (homeDiv) {
        homeDiv.style.display = 'flex';
        homeDiv.style.flexDirection = 'column';
    }
    
    // Restore header for home page
    if (typeof updateHeaderTitle === 'function') {
        updateHeaderTitle('Available Books');
    }
    
    // Clear header right section
    if (typeof updateHeaderRight === 'function') {
        updateHeaderRight('');
    }
    
    // Show home search bar
    const searchInput = document.getElementById("searchBookByName");
    if (searchInput && searchInput.closest('.home-search-container')) {
        searchInput.closest('.home-search-container').style.display = 'block';
    }
}

function showUserProfile() {
    document.getElementById('mainUserProfilePageMainDIV').style.display = 'block';
}

function updateSidebarActiveState(activeModule) {
    // Remove active class from all nav links
    document.querySelectorAll('.sidebar .nav-link').forEach(link => {
        link.classList.remove('active');
    });
    
    // Add active class to the clicked link
    const activeLink = document.querySelector(`[onclick*="${activeModule}"]`);
    if (activeLink) {
        activeLink.classList.add('active');
    }
}