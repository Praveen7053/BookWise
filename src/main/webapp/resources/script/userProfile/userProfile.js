
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
            document.getElementById("userEmail").value = userData.userEmail || '';
            document.getElementById("userPhone").value = userData.userPhoneNumber || '';
            document.getElementById("age").value = userData.age || '';
            document.getElementById("gender").value = userData.gender || '';
            document.getElementById("mainLanguage").value = userData.mainLanguage || '';
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
