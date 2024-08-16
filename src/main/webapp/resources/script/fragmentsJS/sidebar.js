
function changeModulePage(pageName){
    defaultModulePageDisplay();
    if(pageName === "homePage"){
        document.getElementById("mainHomePageMainDIV").style.display = "block";
    }else if(pageName === "uploadBooks"){
        document.getElementById("mainUploadNewBooksPageMainDIV").style.display = "block";
    }else if(pageName === "booksList"){
        document.getElementById("mainUploadListViewPageMainDIV").style.display = "block";
    }else if(pageName === "userProfile"){
        document.getElementById("mainUserProfilePageMainDIV").style.display = "block";
    }
}

function defaultModulePageDisplay(){
    document.getElementById("mainHomePageMainDIV").style.display = "none";
    document.getElementById("mainUploadNewBooksPageMainDIV").style.display = "none";
    document.getElementById("mainUploadListViewPageMainDIV").style.display = "none";
    document.getElementById("mainUserProfilePageMainDIV").style.display = "none";
}

document.getElementById('dropdownToggle').addEventListener('click', function () {
    const menu = document.getElementById('dropdownMenu');
    const isExpanded = this.getAttribute('aria-expanded') === 'true';

    // Toggle aria-expanded
    this.setAttribute('aria-expanded', !isExpanded);

    // Toggle menu visibility
    if (isExpanded) {
        menu.classList.remove('show');
    } else {
        menu.classList.add('show');
    }
});

function selectMenuTabs(tabId) {
    document.querySelectorAll('.nav-link').forEach(function (element) {
        element.classList.remove('active');
    });
    document.getElementById(tabId).classList.add('active');
}
