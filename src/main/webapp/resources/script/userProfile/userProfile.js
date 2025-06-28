
function loadUserProfile() {
    // Get user data from hidden fields or API
    const userName = $('#userNameHidden').val();
    const userEmail = $('#userEmailHidden').val();
    const loggedInUserId = $('#loggedInUserId').val();

    console.log('loggedInUserId :: '+loggedInUserId);

    var contextPath = $('meta[name="context-path"]').attr('content');
    var url = contextPath + '/api/user/profile/getUserProfileInfo';
    var jsonData = { loggedInUserId: loggedInUserId };

    showProgressBar("progressBarDiv", "bodyDiv");
    postData(url, JSON.stringify(jsonData), 'json', function(response) {
        closeProgressBar("progressBarDiv", "bodyDiv");
        if (response.success) {
            const userData = JSON.parse(response.data);

            // Populate all form fields
            document.getElementById("userName").value = userData.userName || '';
            document.getElementById("displayUserName").innerHTML = userData.userName || '';
            document.getElementById("userEmail").value = userData.userEmail || '';
            document.getElementById("displayUserEmail").innerHTML = userData.userEmail || '';
            document.getElementById("userPhone").value = userData.userPhoneNumber || '';
            document.getElementById("age").value = userData.age || '';
            document.getElementById("gender").value = userData.gender || '';
            document.getElementById("mainLanguage").value = userData.mainLanguage || '';
            document.getElementById("Street").value = userData.street || '';
            document.getElementById("ciTy").value = userData.city || '';
            document.getElementById("sTate").value = userData.state || '';
            document.getElementById("zIp").value = userData.zipCode || '';
            document.getElementById("user_description").value = userData.description || '';
            document.getElementById("userDescriptionRead").innerHTML = userData.description || '';

            // Handle profile image
            const profileImage = document.getElementById('userProfileImage');
            if (response.imageContent && response.imageMimeType) {
                profileImage.src = 'data:' + response.imageMimeType + ';base64,' + response.imageContent;
            } else {
                profileImage.src = contextPath + '/resources/images/default-user.png';
            }

            // Add error handler for the default image
            profileImage.onerror = function() {
                this.src = contextPath + '/resources/images/default-user.png';
            };
        } else {
            // Handle error
            showErrorAlert(response.message);
        }
    });

}

function triggerImageUpload() {
    // Trigger the hidden file input
    //document.getElementById('profileImageInput').click();

    // Add the change listener for when a file is selected
    const fileInput = document.getElementById('profileImageInput');
    fileInput.onchange = function(e) {
        const file = e.target.files[0];
        if (file) {
            // Validate file type
            if (!file.type.startsWith('image/')) {
                showErrorAlert("Please select an image file");
                return;
            }

            // Validate file size (max 5MB)
            if (file.size > 5 * 1024 * 1024) {
                showErrorAlert("File size should be less than 5MB");
                return;
            }

            // Show preview
            const reader = new FileReader();
            reader.onload = function(event) {
                document.getElementById('userProfileImage').src = event.target.result;
            };
            reader.readAsDataURL(file);

            // Handle the upload
            //handleProfilePictureUpload(file);
        }
    };
}

// Optional: Add hover effect to the camera icon
document.querySelector('#profileCameraIcon').parentElement.addEventListener('mouseenter', function() {
    this.style.backgroundColor = '#0056b3';  // Darker shade on hover
});

document.querySelector('#profileCameraIcon').parentElement.addEventListener('mouseleave', function() {
    this.style.backgroundColor = '#0d6efd';  // Return to original color
});

function saveUserProfileInfo() {
    // Get form values
    const profileData = {
        userName: document.getElementById("userName").value.trim(),
        userEmail: document.getElementById("userEmail").value.trim(),
        userPhoneNumber: document.getElementById("userPhone").value.trim(),
        age: document.getElementById("age").value.trim(),
        gender: document.getElementById("gender").value,
        mainLanguage: document.getElementById("mainLanguage").value,
        street: document.getElementById("Street").value,
        city: document.getElementById("ciTy").value,
        state: document.getElementById("sTate").value,
        zip: document.getElementById("zIp").value,
        user_description: document.getElementById("user_description").value
    };

    // Validation
    if (!profileData.userName) {
        showErrorAlert("Please enter your name");
        return;
    }

    if (!profileData.userEmail) {
        showErrorAlert("Please enter your email");
        return;
    }

    // Email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(profileData.userEmail)) {
        showErrorAlert("Please enter a valid email address");
        return;
    }

    // Phone validation (optional field)
    if (profileData.userPhoneNumber) {
        const phoneRegex = /^\+?[\d\s-]{8,}$/;
        if (!phoneRegex.test(profileData.userPhoneNumber)) {
            showErrorAlert("Please enter a valid phone number");
            return;
        }
    }

    // Age validation (optional field)
    if (profileData.age) {
        const age = parseInt(profileData.age);
        if (isNaN(age) || age < 0 || age > 150) {
            showErrorAlert("Please enter a valid age");
            return;
        }
    }

    // Convert profile image to base64 if it exists
    const profileImageInput = document.getElementById('profileImageInput');
    if (profileImageInput && profileImageInput.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            profileData.profileImage = e.target.result;
            sendProfileData(profileData);
        };
        reader.readAsDataURL(profileImageInput.files[0]);
    } else {
        sendProfileData(profileData);
    }
}

function sendProfileData(profileData) {
    const contextPath = $('meta[name="context-path"]').attr('content');
    const url = contextPath + '/api/user/profile/saveUserProfileInfo';

    showProgressBar("progressBarDiv", "bodyDiv");

    postData(url, JSON.stringify(profileData), 'json', function(response) {
        closeProgressBar("progressBarDiv", "bodyDiv");
        if (response.success) {
            showSuccessAlert("Profile updated successfully!");
            if (response.data) {
                // Update profile image if new one was uploaded
                const profileImg = document.getElementById('userProfileImage');
                if (profileImg && response.data.profileImagePath) {
                    profileImg.src = contextPath + response.data.profileImagePath;
                }
            }

            if (typeof loadSidebarProfileImage === 'function') {
                loadSidebarProfileImage();
            }
            loadUserProfile();
        } else {
            showErrorAlert(response.message || "Failed to update profile");
        }
    });
}

function loadSidebarProfileImage() {
    const loggedInUserId = $('#loggedInUserId').val();
    const contextPath = $('meta[name="context-path"]').attr('content');
    const url = contextPath + '/api/user/profile/getUserProfileInfo';
    const jsonData = { loggedInUserId: loggedInUserId };

    postData(url, JSON.stringify(jsonData), 'json', function(response) {
        if (response.success) {
            if(response.imageContent && response.imageMimeType){
                const profileImage = document.getElementById('sidebarProfileImage');
                const readerProfileImage = document.getElementById('sidebarReaderProfileImage');
                if(profileImage){
                    profileImage.src = 'data:' + response.imageMimeType + ';base64,' + response.imageContent;
                    // Add error handler for fallback to default image
                    profileImage.onerror = function() {
                        this.src = contextPath + '/resources/images/default-user.png';
                    };
                }

                if(readerProfileImage){
                    readerProfileImage.src = 'data:' + response.imageMimeType + ';base64,' + response.imageContent;
                   // Add error handler for fallback to default image
                    readerProfileImage.onerror = function() {
                        this.src = contextPath + '/resources/images/default-user.png';
                    };
                }
            }

            if(response.data){
                const userData = JSON.parse(response.data);
                if(document.getElementById("sideBarLoginUserName")){
                    document.getElementById("sideBarLoginUserName").innerHTML = userData.userName || '';
                }
                if(document.getElementById("sideBarLoginReaderUserName")){
                    document.getElementById("sideBarLoginReaderUserName").innerHTML = userData.userName || '';
                }

            }
        }
    });
}