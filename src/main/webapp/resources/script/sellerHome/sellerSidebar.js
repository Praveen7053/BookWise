
$(document).ready(function() {
    try {
        const userName = document.getElementById('userNameHidden').value;
        const userEmail = document.getElementById('userEmailHidden').value;

        // Update the username in the sidebar dropdown if it exists
        const userNameSpan = document.querySelector('#userProfileDropdownToggle span');
        if (userNameSpan) {
            userNameSpan.textContent = userName;
        }

        loadSidebarProfileImage();
    } catch (error) {
        console.error('Error accessing user information:', error);
    }
});

function changeModulePage(pageName){
    defaultModulePageDisplay();
    if(pageName === "sellerHomePage"){
        document.getElementById("mainSellerHomeRightSectionPageMainDIV").style.display = "block";
        fetchSellerBooks();
    }else if(pageName === "uploadBooks"){
        $('#mainUploadNewBooksPageMainDIV').removeClass('d-none').addClass('d-flex');
    }else if(pageName === "booksList"){
        document.getElementById("mainUploadListViewPageMainDIV").style.display = "block";
    }else if(pageName === "userProfile"){
        $('#mainUserProfilePageMainDIV').removeClass('d-none').addClass('d-flex');
    }
}

function defaultModulePageDisplay(){
    document.getElementById("mainSellerHomeRightSectionPageMainDIV").style.display = "none";
    $('#mainUploadNewBooksPageMainDIV').removeClass('d-flex').addClass('d-none');
    document.getElementById("mainUploadListViewPageMainDIV").style.display = "none";
    $('#mainUserProfilePageMainDIV').removeClass('d-flex').addClass('d-none');
}

document.getElementById('userProfileDropdownToggle').addEventListener('click', function () {
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
    if(tabId == 'userProfileTab') {
        loadUserProfile();
    }
}

function loadSidebarProfileImage() {
    const loggedInUserId = $('#loggedInUserId').val();
    const contextPath = $('meta[name="context-path"]').attr('content');
    const url = contextPath + '/api/user/profile/getUserProfileInfo';
    const jsonData = { loggedInUserId: loggedInUserId };

    postData(url, JSON.stringify(jsonData), 'json', function(response) {
        if (response.success && response.imageContent && response.imageMimeType) {
            const profileImage = document.getElementById('sidebarProfileImage');
            profileImage.src = 'data:' + response.imageMimeType + ';base64,' + response.imageContent;

            // Add error handler for fallback to default image
            profileImage.onerror = function() {
                this.src = contextPath + '/resources/images/default-user.png';
            };
        }
    });
}

document.addEventListener('DOMContentLoaded', function() {
    const dropdownToggle = document.getElementById('userProfileDropdownToggle');
    const dropdownMenu = document.getElementById('dropdownMenu');

    document.addEventListener('click', function(event) {
        if (!dropdownToggle.contains(event.target)) {
            dropdownMenu.style.display = 'none';
        }
    });

    dropdownToggle.addEventListener('click', function(event) {
        event.stopPropagation();
        dropdownMenu.style.display = dropdownMenu.style.display === 'none' ? 'block' : 'none';
    });
});