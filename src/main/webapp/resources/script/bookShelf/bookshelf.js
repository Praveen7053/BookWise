/**
 * Bookshelf JavaScript functionality
 * Handles both bookshelf page UI and book details shelf interactions
 */

// Bookshelf module - wrapped in IIFE to avoid global conflicts
(function() {
    'use strict';
    
    // Bookshelf variables
    let currentBookshelfData = [];
    let filteredBookshelfData = [];
    let currentPage = 0;
    const booksPerPage = 12;

    // ========== BOOKSHELF PAGE FUNCTIONS ==========
    
    // Initialize bookshelf page
    window.showMyBookshelf = function() {
        // Hide book details section first
        const bookDetailsSection = document.getElementById('bookDetailsSection');
        if (bookDetailsSection) {
            bookDetailsSection.style.display = 'none';
        }
        
        // Show bookshelf page
        document.getElementById('myBookshelfPage').style.display = 'block';
        loadBookshelfData();
        setupEventListeners();
    };

    window.hideMyBookshelf = function() {
        document.getElementById('myBookshelfPage').style.display = 'none';
        // Show the main content or navigate back
        if (typeof showHomePage === 'function') {
            showHomePage();
        }
    };

    function setupEventListeners() {
        // Search functionality
        document.getElementById('bookshelfSearch').addEventListener('input', function(e) {
            filterBooks(e.target.value);
        });

        // Sort functionality
        document.getElementById('bookshelfSort').addEventListener('change', function(e) {
            sortBooks(e.target.value);
        });

        // Load more books
        document.getElementById('loadMoreBooks').addEventListener('click', function() {
            loadMoreBooks();
        });
    }

    function loadBookshelfData() {
        const contextPath = $('meta[name="context-path"]').attr('content');
        const url = `${contextPath}/api/bookshelf/my-shelf`;
        
        getData(url, 'json', function(response) {
            if (response && Array.isArray(response)) {
                currentBookshelfData = response;
                filteredBookshelfData = [...currentBookshelfData];
                updateBookshelfStats();
                renderBookshelfGrid();
            } else {
                showErrorAlert('Failed to load bookshelf data');
            }
        }, function() {
            showErrorAlert('Failed to load bookshelf data');
        });
    }

    function updateBookshelfStats() {
        const totalBooks = currentBookshelfData.length;
        const recentlyAdded = currentBookshelfData.filter(book => {
            const addedDate = new Date(book.addedDate);
            const weekAgo = new Date();
            weekAgo.setDate(weekAgo.getDate() - 7);
            return addedDate > weekAgo;
        }).length;

        // Calculate favorite genre
        const genreCount = {};
        currentBookshelfData.forEach(book => {
            if (book.bookCategory) {
                genreCount[book.bookCategory] = (genreCount[book.bookCategory] || 0) + 1;
            }
        });
        const favoriteGenre = Object.keys(genreCount).length > 0 
            ? Object.keys(genreCount).reduce((a, b) => genreCount[a] > genreCount[b] ? a : b)
            : '-';

        // Update stats display
        document.getElementById('totalBooksCount').textContent = totalBooks;
        document.getElementById('recentlyAddedCount').textContent = recentlyAdded;
        document.getElementById('favoriteGenre').textContent = favoriteGenre;
        document.getElementById('readingProgress').textContent = '0%'; // Placeholder for future feature
    }

    function renderBookshelfGrid() {
        const gridContainer = document.getElementById('bookshelfGrid');
        
        if (filteredBookshelfData.length === 0) {
            gridContainer.innerHTML = `
                <div class="col-12 text-center py-5">
                    <div class="text-muted">
                        <i class="fas fa-book-open fa-3x mb-3"></i>
                        <h5>No books found</h5>
                        <p>Try adjusting your search or filters</p>
                    </div>
                </div>
            `;
            return;
        }

        const startIndex = currentPage * booksPerPage;
        const endIndex = startIndex + booksPerPage;
        const booksToShow = filteredBookshelfData.slice(startIndex, endIndex);

        const booksHTML = booksToShow.map(book => createBookCard(book)).join('');
        
        if (currentPage === 0) {
            gridContainer.innerHTML = booksHTML;
        } else {
            gridContainer.innerHTML += booksHTML;
        }

        // Show/hide load more button
        const loadMoreSection = document.getElementById('loadMoreSection');
        if (endIndex < filteredBookshelfData.length) {
            loadMoreSection.style.display = 'block';
        } else {
            loadMoreSection.style.display = 'none';
        }
    }

    function createBookCard(book) {
        // Use Base64 image if available, otherwise fallback to file path or default image
        let coverImage = 'resources/images/no_cover_available.png';
        if (book.coverImageContent && book.coverImageMimeType) {
            coverImage = `data:${book.coverImageMimeType};base64,${book.coverImageContent}`;
        } else if (book.frontPageImagePath) {
            coverImage = book.frontPageImagePath;
        }
        const addedDate = new Date(book.addedDate).toLocaleDateString();
        
        return `
            <div class="col-lg-3 col-md-4 col-sm-6 mb-4">
                <div class="card h-100 shadow-sm book-card" data-book-id="${book.bookEncounterId}">
                    <div class="position-relative">
                        <img src="${coverImage}" class="card-img-top" alt="${book.bookTitle}" 
                             style="height: 200px; object-fit: cover;">
                        <div class="position-absolute top-0 end-0 p-2">
                            <button class="btn btn-sm btn-danger" onclick="removeFromBookshelf(${book.bookEncounterId}, this)">
                                <i class="fas fa-trash"></i>
                            </button>
                        </div>
                    </div>
                    <div class="card-body d-flex flex-column">
                        <h6 class="card-title text-truncate" title="${book.bookTitle}">${book.bookTitle}</h6>
                        <p class="card-text text-muted small mb-2">by ${book.bookAuthor || 'Unknown Author'}</p>
                        <p class="card-text small text-muted">Added: ${addedDate}</p>
                        ${book.notes ? `<p class="card-text small"><em>"${book.notes}"</em></p>` : ''}
                        <div class="mt-auto">
                            <button class="btn btn-primary btn-sm w-100 mb-2" onclick="readBookFromShelf(${book.bookEncounterId})">
                                <i class="fas fa-book-open me-1"></i> Read Now
                            </button>
                            <button class="btn btn-outline-secondary btn-sm w-100" onclick="showBookDetails(${book.bookEncounterId})">
                                <i class="fas fa-info-circle me-1"></i> Details
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }

    function filterBooks(searchTerm) {
        if (!searchTerm.trim()) {
            filteredBookshelfData = [...currentBookshelfData];
        } else {
            const term = searchTerm.toLowerCase();
            filteredBookshelfData = currentBookshelfData.filter(book => 
                book.bookTitle.toLowerCase().includes(term) ||
                (book.bookAuthor && book.bookAuthor.toLowerCase().includes(term)) ||
                (book.bookCategory && book.bookCategory.toLowerCase().includes(term)) ||
                (book.notes && book.notes.toLowerCase().includes(term))
            );
        }
        currentPage = 0;
        renderBookshelfGrid();
    }

    function sortBooks(sortBy) {
        filteredBookshelfData.sort((a, b) => {
            switch (sortBy) {
                case 'title':
                    return (a.bookTitle || '').localeCompare(b.bookTitle || '');
                case 'author':
                    return (a.bookAuthor || '').localeCompare(b.bookAuthor || '');
                case 'genre':
                    return (a.bookCategory || '').localeCompare(b.bookCategory || '');
                case 'recent':
                default:
                    return new Date(b.addedDate) - new Date(a.addedDate);
            }
        });
        currentPage = 0;
        renderBookshelfGrid();
    }

    function loadMoreBooks() {
        currentPage++;
        renderBookshelfGrid();
    }

    window.removeFromBookshelf = function(bookEncounterId, buttonElement) {
        if (!confirm('Are you sure you want to remove this book from your shelf?')) {
            return;
        }

        // Disable button during request
        buttonElement.disabled = true;
        buttonElement.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';

        const contextPath = $('meta[name="context-path"]').attr('content');
        const url = `${contextPath}/api/bookshelf/remove/${bookEncounterId}`;
        
        deleteData(url, null, 'json', function(response) {
            if (response && response.success) {
                // Remove from local data
                currentBookshelfData = currentBookshelfData.filter(book => book.bookEncounterId !== bookEncounterId);
                filteredBookshelfData = filteredBookshelfData.filter(book => book.bookEncounterId !== bookEncounterId);
                
                // Update UI
                updateBookshelfStats();
                renderBookshelfGrid();
                showSuccessAlert('Book removed from your shelf!');
            } else {
                showErrorAlert(response.message || 'Failed to remove book from shelf');
                buttonElement.disabled = false;
                buttonElement.innerHTML = '<i class="fas fa-trash"></i>';
            }
        }, function() {
            showErrorAlert('Failed to remove book from shelf');
            buttonElement.disabled = false;
            buttonElement.innerHTML = '<i class="fas fa-trash"></i>';
        });
    };

    window.readBookFromShelf = function(bookEncounterId) {
        const contextPath = $('meta[name="context-path"]').attr('content');
        const pdfUrl = contextPath + '/api/bookWiseDflipView/book/download/' + bookEncounterId;
        
        // Get book title for the viewer
        const book = currentBookshelfData.find(b => b.bookEncounterId === bookEncounterId);
        const bookTitle = book ? book.bookTitle : 'Book';
        
        if (typeof openDflipViewer === 'function') {
            openDflipViewer(pdfUrl, bookTitle);
        } else {
            showErrorAlert('PDF viewer is not available. Please refresh the page.');
        }
    };

    window.showBookDetails = function(bookEncounterId) {
        const book = currentBookshelfData.find(b => b.bookEncounterId === bookEncounterId);
        if (!book) return;

        // Populate modal
        document.getElementById('modalBookTitle').textContent = book.bookTitle;
        
        // Use Base64 image if available, otherwise fallback to file path or default image
        let coverImage = 'resources/images/no_cover_available.png';
        if (book.coverImageContent && book.coverImageMimeType) {
            coverImage = `data:${book.coverImageMimeType};base64,${book.coverImageContent}`;
        } else if (book.frontPageImagePath) {
            coverImage = book.frontPageImagePath;
        }
        
        const modalContent = `
            <div class="row">
                <div class="col-md-4">
                    <img src="${coverImage}" 
                         class="img-fluid rounded" alt="${book.bookTitle}">
                </div>
                <div class="col-md-8">
                    <h6>Author</h6>
                    <p>${book.bookAuthor || 'Unknown Author'}</p>
                    
                    <h6>Description</h6>
                    <p>${book.bookDescription || 'No description available.'}</p>
                    
                    <h6>Category</h6>
                    <p>${book.bookCategory || 'Uncategorized'}</p>
                    
                    <h6>Added to Shelf</h6>
                    <p>${new Date(book.addedDate).toLocaleDateString()}</p>
                    
                    ${book.notes ? `
                        <h6>Your Notes</h6>
                        <p><em>"${book.notes}"</em></p>
                    ` : ''}
                </div>
            </div>
        `;
        
        document.getElementById('modalBookContent').innerHTML = modalContent;
        
        // Set up modal buttons
        document.getElementById('modalReadNowBtn').onclick = () => {
            readBookFromShelf(bookEncounterId);
            $('#bookshelfBookModal').modal('hide');
        };
        
        document.getElementById('modalRemoveFromShelfBtn').onclick = () => {
            removeFromBookshelf(bookEncounterId, document.getElementById('modalRemoveFromShelfBtn'));
            $('#bookshelfBookModal').modal('hide');
        };
        
        // Show modal
        $('#bookshelfBookModal').modal('show');
    };

    // ========== BOOK DETAILS SHELF FUNCTIONS ==========
    // These functions are used by the book details page

    // Check if a book is in the user's shelf
window.checkBookshelfStatus = function(bookEncounterId) {
    const contextPath = $('meta[name="context-path"]').attr('content');
    const url = `${contextPath}/api/bookshelf/check/${bookEncounterId}`;
    
    console.log('Checking bookshelf status for book:', bookEncounterId);
    console.log('API URL:', url);
    
    getData(url, 'json', function(response) {
        console.log('Bookshelf status response:', response);
        if (response && response.success !== undefined) {
            // Use the correct field name from the backend
            const isInShelf = response.isInShelf;
            updateShelfButton(isInShelf);
        } else {
            console.error('Invalid response format:', response);
        }
    }, function(error) {
        console.error('Bookshelf status check failed:', error);
    });
};

    // Update the shelf button appearance and functionality based on shelf status
window.updateShelfButton = function(isInShelf) {
    const button = document.querySelector('#add-to-shelf-btn');
    if (!button) {
        console.error('Shelf button not found!');
        return;
    }
    
    console.log('Updating shelf button. isInShelf:', isInShelf);
    
    if (isInShelf) {
        button.innerHTML = '<i class="fas fa-bookmark me-1"></i> Remove from shelf';
        button.className = 'btn btn-danger w-100 mb-2';
        button.onclick = removeFromShelf;
        console.log('Button updated to: Remove from shelf');
    } else {
        button.innerHTML = '<i class="fas fa-bookmark me-1"></i> Add to shelf';
        button.className = 'btn btn-outline-secondary w-100 mb-2';
        button.onclick = addToShelf;
        console.log('Button updated to: Add to shelf');
    }
};

    // Add a book to the user's shelf
    window.addToShelf = function() {
        const bookEncounterId = document.getElementById("bookDetails_bookEncounterIdHidden").value;
        const contextPath = $('meta[name="context-path"]').attr('content');
        const url = `${contextPath}/api/bookshelf/add`;
        
        const data = {
            bookEncounterId: parseInt(bookEncounterId)
        };
        
        postData(url, JSON.stringify(data), 'json', function(response) {
            if (response && response.success) {
                updateShelfButton(true);
                showSuccessAlert("Book added to your shelf!");
            } else {
                showErrorAlert(response.message || "Failed to add book to shelf");
            }
        });
    };

    // Remove a book from the user's shelf
    window.removeFromShelf = function() {
        const bookEncounterId = document.getElementById("bookDetails_bookEncounterIdHidden").value;
        const contextPath = $('meta[name="context-path"]').attr('content');
        const url = `${contextPath}/api/bookshelf/remove/${bookEncounterId}`;
        
        deleteData(url, null, 'json', function(response) {
            if (response && response.success) {
                updateShelfButton(false);
                showSuccessAlert("Book removed from your shelf!");
            } else {
                showErrorAlert(response.message || "Failed to remove book from shelf");
            }
        });
    };

})(); 