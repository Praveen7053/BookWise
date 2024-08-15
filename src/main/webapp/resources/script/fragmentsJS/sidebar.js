
function changeModulePage(pageName){
    defaultModulePageDisplay();
    if(pageName === "homePage"){
        document.getElementById("mainHomePageMainDIV").style.display = "block";
    }else if(pageName === "uploadBooks"){
        document.getElementById("mainUploadNewBooksPageMainDIV").style.display = "block";
    }else if(pageName === "booksList"){
        document.getElementById("mainUploadListViewPageMainDIV").style.display = "block";
    }
}

function defaultModulePageDisplay(){
    document.getElementById("mainHomePageMainDIV").style.display = "none";
    document.getElementById("mainUploadNewBooksPageMainDIV").style.display = "none";
    document.getElementById("mainUploadListViewPageMainDIV").style.display = "none";
}

document.getElementById("toggleSidebarBtn").addEventListener("click", function() {
    const sidebar = document.getElementById("sidebar");
    const icon = document.getElementById("toggleIcon");
    const sidebarNav = document.getElementById("sidebarNav");
    const sidebarDropdown = document.getElementById("sidebarDropdown");
    const sidebarLogo = document.getElementById("sidebarLogo");
    const sidebarTitle = document.getElementById("sidebarTitle");

    if (sidebar.style.width === "280px") {
        // Minimize sidebar
        sidebar.style.width = "60px";
        sidebarNav.style.display = "none";
        sidebarDropdown.style.display = "none";
        sidebarTitle.style.display = "none";
        sidebarLogo.innerHTML = '<i class="fas fa-home"></i>'; // Display Home or any other icon when minimized
        icon.classList.remove("fa-angle-left");
        icon.classList.add("fa-angle-right");
    } else {
        // Expand sidebar
        sidebar.style.width = "280px";
        sidebarNav.style.display = "block";
        sidebarDropdown.style.display = "block";
        sidebarTitle.style.display = "block";
        sidebarLogo.innerHTML = '<i class="fas fa-book me-2"></i><span id="sidebarTitle" class="fs-4">Book Wise</span>';
        icon.classList.remove("fa-angle-right");
        icon.classList.add("fa-angle-left");
    }
});

