
function loadBookDetailsByID(bookEncounterId) {
    clearBookDetails();
    console.log('bookEncounterId :: '+bookEncounterId);

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
        } else {
            closeProgressBar("progressBarDiv", "bodyDiv");
            showErrorAlert(response.message);
        }
    });

    document.getElementById("bookDetailsSection").style.display = "block";
    document.getElementById("readerHomeMainDiv").style.display = "none";
}

function handleBookDetailsData(resp) {
    if(resp != '') {
        const obj = JSON.parse(resp.bookDetails);
        var bookTitle = obj.bookTitle || '';
        var bookAuthor = (obj.bookAuthor ? "By " + obj.bookAuthor : "");
        var bookDescription = obj.bookDescription || '';
        var bookPageNumber = obj.bookPageNumber || '';
        var bookLanguage = obj.bookLanguage || '';
        var publicationDate = (obj.publicationDate ? formatDateMMMDDYYYYHHMMAM(obj.publicationDate) : "");
        var bookIsbnNumber = obj.bookIsbnNumber || '';

        document.getElementById("bd-title").innerHTML = bookTitle;
        document.getElementById("bd-author").innerHTML = bookAuthor;
        document.getElementById("bd-description").innerHTML = bookDescription;
        document.getElementById("bd-pages").innerHTML = bookPageNumber;
        document.getElementById("bd-language").innerHTML = bookLanguage;
        document.getElementById("bd-uploaded").innerHTML = publicationDate;
        document.getElementById("bd-isbn").innerHTML = bookIsbnNumber;

        if (obj.coverImageMimeType && obj.coverImageMimeType.startsWith('image/')) {
            const currentCoverImage = document.getElementById('bd-cover');
            currentCoverImage.src = 'data:' + obj.coverImageMimeType + ';base64,' + obj.coverImageContent;
            currentCoverImage.style.display = 'block';
        }
    }
}

function clearBookDetails(){
    document.getElementById("bd-title").innerHTML = "";

    if(document.getElementById('bd-cover')){
        document.getElementById('bd-cover').src = 'resources/images/no_cover_available.png';
    }

    document.getElementById("bd-author").innerHTML = "";
    document.getElementById("bd-description").innerHTML = "";
    document.getElementById("bd-pages").innerHTML = "";
    document.getElementById("bd-language").innerHTML = "";
    document.getElementById("bd-uploaded").innerHTML = "";
    document.getElementById("bd-isbn").innerHTML = "";
}

function hideBookDetails(){
    document.getElementById("bookDetailsSection").style.display = "none";
    document.getElementById("readerHomeMainDiv").style.display = "block";
    loadAllBooks();
}