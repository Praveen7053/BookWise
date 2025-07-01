
function loadBookDetailsByID(bookEncounterId) {
    console.log('bookEncounterId :: '+bookEncounterId);

    document.getElementById("bookDetailsSection").style.display = "block";
    document.getElementById("readerHomeMainDiv").style.display = "none";
}

function hideBookDetails(){
    document.getElementById("bookDetailsSection").style.display = "none";
    document.getElementById("readerHomeMainDiv").style.display = "block";
    loadAllBooks();
}