function loadBookDetailsByID(bookEncounterId) {
    clearBookDetails();
    document.getElementById("bookDetails_bookEncounterIdHidden").value = bookEncounterId; // Store the ID for later use (e.g., posting comments)

    var contextPath = $('meta[name="context-path"]').attr('content');
    var csrfToken = $('meta[name="csrf-token"]').attr('content');
    var url = contextPath + '/api/bookDetails/actions/getUserBookEncounter';

    var data = JSON.stringify({
        bookEncounterId: bookEncounterId
    });

    showProgressBar("progressBarDiv", "bodyDiv");
    postData(url, data, 'json', function(response) {
        if (response.success) {
            handleBookDetailsData(response);
            closeProgressBar("progressBarDiv", "bodyDiv");

            document.getElementById("bookDetailsSection").style.display = "block";
            document.getElementById("readerHomeMainDiv").style.display = "none";
            loadCommentsForBook(bookEncounterId);
        } else {
            closeProgressBar("progressBarDiv", "bodyDiv");
            showErrorAlert(response.message);
            document.getElementById("bookDetailsSection").style.display = "none";
            document.getElementById("readerHomeMainDiv").style.display = "block";
        }
    });
}

function handleBookDetailsData(resp) {
    if (resp && resp.bookDetails) {
        const obj = JSON.parse(resp.bookDetails);
        document.getElementById("bd-title").innerHTML = obj.bookTitle || '';
        document.getElementById("bd-author").innerHTML = (obj.bookAuthor ? "By " + obj.bookAuthor : "");
        document.getElementById("bd-description").innerHTML = obj.bookDescription || '';
        document.getElementById("bd-pages").innerHTML = obj.bookPageNumber || '';
        document.getElementById("bd-language").innerHTML = obj.bookLanguage || '';
        document.getElementById("bd-uploaded").innerHTML = (obj.publicationDate ? formatDateMMMDDYYYYHHMMAM(obj.publicationDate) : "");
        document.getElementById("bd-isbn").innerHTML = obj.bookIsbnNumber || '';

        if (obj.coverImageMimeType && obj.coverImageMimeType.startsWith('image/')) {
            const currentCoverImage = document.getElementById('bd-cover');
            currentCoverImage.src = 'data:' + obj.coverImageMimeType + ';base64,' + obj.coverImageContent;
            currentCoverImage.style.display = 'block';
        }
    }
}

function clearBookDetails() {
    document.getElementById("bookDetails_bookEncounterIdHidden").value = "0";
    document.getElementById("bd-title").innerHTML = "";
    document.getElementById('bd-cover').src = 'resources/images/no_cover_available.png';
    document.getElementById("bd-author").innerHTML = "";
    document.getElementById("bd-description").innerHTML = "";
    document.getElementById("bd-pages").innerHTML = "";
    document.getElementById("bd-language").innerHTML = "";
    document.getElementById("bd-uploaded").innerHTML = "";
    document.getElementById("bd-isbn").innerHTML = "";

    // Also clear the comments section and the input field
    document.getElementById('bd-comments').innerHTML = '';
    document.getElementById('bd-new-comment').value = '';
}

function hideBookDetails(){
    document.getElementById("bookDetailsSection").style.display = "none";
    document.getElementById("readerHomeMainDiv").style.display = "block";
    loadAllBooks();
}

// A global variable to keep track of the current book being viewed.
document.addEventListener('DOMContentLoaded', () => {
    const postButton = document.getElementById('bd-post-comment');
    if (postButton) {
        postButton.addEventListener('click', postNewComment);
    }

    const commentInput = document.getElementById('bd-new-comment');
    if (commentInput) {
        commentInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                postNewComment();
            }
        });
    }

    const commentsContainer = document.getElementById('bd-comments');
    if (commentsContainer) {
        commentsContainer.addEventListener('click', function(e) {
            // Check if a delete button was clicked
            const deleteButton = e.target.closest('.btn-delete-comment');
            if (deleteButton) {
                e.preventDefault();
                const commentId = deleteButton.dataset.commentId;
                if (confirm('Are you sure you want to delete this comment?')) {
                    handleDeleteComment(commentId, deleteButton);
                }
            }
        });
    }

});

function postNewComment() {
    var currentBookEncounterId = document.getElementById("bookDetails_bookEncounterIdHidden").value;
    const commentInput = document.getElementById('bd-new-comment');
    const commentText = commentInput.value.trim();
    const postButton = document.getElementById('bd-post-comment');

    if (!commentText) {
        showErrorAlert("Please enter a comment.");
        return;
    }

    if (!currentBookEncounterId) {
        showErrorAlert("Cannot post comment: A book must be selected first.");
        return;
    }

    const contextPath = $('meta[name="context-path"]').attr('content');
    const url = `${contextPath}/api/bookComments/actions/addComment`;
    const data = JSON.stringify({
        bookEncounterId: currentBookEncounterId,
        commentText: commentText
    });

    postButton.disabled = true;
    postData(url, data, 'json', function(newComment) {
        if (newComment) {
            commentInput.value = '';
            loadCommentsForBook(currentBookEncounterId);
        }
        postButton.disabled = false;
    });
}

function loadCommentsForBook(bookEncounterId) {
    const contextPath = $('meta[name="context-path"]').attr('content');
    const url = `${contextPath}/api/bookComments/actions/forBook/${bookEncounterId}`;

    getData(url, 'json', function(comments) {
        renderComments(comments);
    });
}

function renderComments(comments) {
    const commentsContainer = document.getElementById('bd-comments');
    commentsContainer.innerHTML = ''; // Clear any previous comments

    if (!comments || comments.length === 0) {
        commentsContainer.innerHTML = '<div class="text-muted small p-2">No comments yet. Be the first to share your thoughts!</div>';
        return;
    }

    comments.forEach(comment => {
        const commentElement = createCommentElement(comment);
        commentsContainer.appendChild(commentElement);
    });
}


function prependComment(comment) {
    const commentsContainer = document.getElementById('bd-comments');
    const newCommentElement = createCommentElement(comment);

    // If the "No comments yet" placeholder is visible, remove it first.
    const placeholder = commentsContainer.querySelector('.text-muted');
    if (placeholder) {
        commentsContainer.innerHTML = '';
    }

    commentsContainer.insertBefore(newCommentElement, commentsContainer.firstChild);
}

function createCommentElement(comment) {
    const div = document.createElement('div');
    div.className = 'mb-3';
    div.setAttribute('data-comment-element-id', comment.commentId);

    const currentUserId = document.getElementById('loggedInUserId').value;
    const formattedDate = new Date(comment.createdAt).toLocaleString(/* ... */);
    const safeUsername = escapeHtml(comment.userName);
    const safeCommentText = escapeHtml(comment.commentText);

    // Conditionally add a delete button if the user IDs match
    let deleteButtonHtml = '';
    if (currentUserId && parseInt(currentUserId, 10) === comment.userId) {
        deleteButtonHtml = `
            <a href="#" class="text-danger small ms-3 btn-delete-comment" data-comment-id="${comment.commentId}">
                Delete
            </a>
        `;
    }

    div.innerHTML = `
        <div>
            <strong>${safeUsername}</strong>
            <span class="text-muted small ms-2">${formattedDate}</span>
            ${deleteButtonHtml}
        </div>
        <span class="small">${safeCommentText}</span>
    `;
    return div;
}

function handleDeleteComment(commentId, buttonElement) {
    const contextPath = $('meta[name="context-path"]').attr('content');
    const url = `${contextPath}/api/bookComments/actions/delete/${commentId}`;

    buttonElement.style.pointerEvents = 'none';
    buttonElement.textContent = 'Deleting...';

    const successCallback = function() {
        const commentElement = document.querySelector(`[data-comment-element-id="${commentId}"]`);
        if (commentElement) {
            commentElement.style.transition = 'opacity 0.3s ease-out';
            commentElement.style.opacity = '0';
            setTimeout(() => commentElement.remove(), 300);
        }
    };

    // Define what to do if the deletion fails
    const errorCallback = function(jqXHR) {
        showErrorAlert(jqXHR.responseText || 'Failed to delete comment.');
        buttonElement.style.pointerEvents = 'auto';
        buttonElement.textContent = 'Delete';
    };

    deleteData(url, null, 'json', successCallback, errorCallback);
}

function escapeHtml(unsafe) {
    return unsafe
         .replace(/&/g, "&amp;")
         .replace(/</g, "&lt;")
         .replace(/>/g, "&gt;")
         .replace(/"/g, "&quot;")
         .replace(/'/g, "&#039;");
}