
function selectMenuTabs(tabId) {
    document.querySelectorAll('.nav-link').forEach(function (element) {
        element.classList.remove('active');
    });
    document.getElementById(tabId).classList.add('active');
    if(tabId == 'userProfileTab' || tabId == 'userProfileReader') {
        loadUserProfile();
    }
}

function changeModulePage(pageName){
    if (typeof defaultModulePageDisplay === 'function') {
        defaultModulePageDisplay();
    }

    if(pageName === "sellerHomePage"){
        document.getElementById("mainSellerHomeRightSectionPageMainDIV").style.display = "block";
        fetchSellerBooks();
    }else if(pageName === "uploadBooks"){
        $('#mainUploadNewBooksPageMainDIV').removeClass('d-none').addClass('d-flex');
    }else if(pageName === "booksList"){
        document.getElementById("mainUploadListViewPageMainDIV").style.display = "block";
    }else if(pageName === "userProfile" || pageName === "userProfileReader"){
        $('#mainUserProfilePageMainDIV').removeClass('d-none').addClass('d-flex');
    }else if(pageName === "readerUserHome") {
        document.getElementById("readerHomeMainDiv").style.display = "block";
        loadAllBooks();
    }
}

function defaultModulePageDisplay() {
    const rightSection = document.getElementById("mainSellerHomeRightSectionPageMainDIV");
    if (rightSection) rightSection.style.display = "none";

    const uploadNewBooksDiv = $('#mainUploadNewBooksPageMainDIV');
    if (uploadNewBooksDiv.length) {
        uploadNewBooksDiv.removeClass('d-flex').addClass('d-none');
    }

    const uploadListView = document.getElementById("mainUploadListViewPageMainDIV");
    if (uploadListView) uploadListView.style.display = "none";

    const userProfileDiv = $('#mainUserProfilePageMainDIV');
    if (userProfileDiv.length) {
        userProfileDiv.removeClass('d-flex').addClass('d-none');
    }

    const readerHome = document.getElementById("readerHomeMainDiv");
    if (readerHome) readerHome.style.display = "none";

    const bookDetailsSec = document.getElementById("bookDetailsSection");
    if (bookDetailsSec) bookDetailsSec.style.display = "none";
}