<div class="container-fluid vh-100 d-none justify-content-center align-items-center" id="mainUserProfilePageMainDIV" style="display:none;">
    <div class="row w-100 justify-content-center">
        <div class="col-12 col-md-10 col-lg-8 col-xl-6">
            <div class="card shadow-sm w-100">
                <div class="card-body p-3 p-md-4">
                    <h3 class="card-title text-center mb-4">Profile Settings</h3>

                    <!-- Profile Image Section -->
                    <div class="text-center mb-4">
                        <div class="position-relative d-inline-block">
                            <img id="userProfileImage" src="${pageContext.request.contextPath}/resources/images/default-user.png"
                                 class="rounded-circle shadow-sm"
                                 alt="Profile Picture"
                                 style="width: 120px; height: 120px; object-fit: cover;">
                            <div class="position-absolute bottom-0 end-0">
                                <label for="profileImageInput" class="btn btn-sm btn-primary rounded-circle p-2" onclick="triggerImageUpload();">
                                    <i id="profileCameraIcon" class="fas fa-camera"></i>
                                </label>
                                <input type="file" id="profileImageInput" hidden accept="image/*">
                            </div>
                        </div>
                    </div>

                    <!-- Profile Form -->
                    <div class="row g-3">
                        <!-- Full Name -->
                        <div class="col-12">
                            <label for="userName" class="form-label">Full Name</label>
                            <input type="text" class="form-control form-control-lg" id="userName" required>
                        </div>

                        <!-- Email -->
                        <div class="col-12">
                            <label for="userEmail" class="form-label">Email</label>
                            <input type="email" class="form-control form-control-lg" id="userEmail" required>
                        </div>

                        <!-- Phone -->
                        <div class="col-12 col-md-6">
                            <label for="userPhone" class="form-label">Phone Number</label>
                            <input type="tel" class="form-control form-control-lg" id="userPhone">
                        </div>

                        <!-- Language -->
                        <div class="col-12 col-md-6">
                            <label for="mainLanguage" class="form-label">Main Language</label>
                            <select class="form-select form-select-lg" id="mainLanguage">
                                <option value="english">English</option>
                                <option value="spanish">Spanish</option>
                                <option value="french">French</option>
                                <option value="german">German</option>
                                <option value="chinese">Chinese</option>
                                <option value="japanese">Japanese</option>
                            </select>
                        </div>

                        <!-- Gender -->
                        <div class="col-12 col-sm-6">
                            <label for="gender" class="form-label">Gender</label>
                            <select class="form-select form-select-lg" id="gender">
                                <option value="">Select Gender</option>
                                <option value="male">Male</option>
                                <option value="female">Female</option>
                                <option value="other">Other</option>
                            </select>
                        </div>

                        <!-- Age -->
                        <div class="col-12 col-sm-6">
                            <label for="age" class="form-label">Age</label>
                            <input type="number" class="form-control form-control-lg" id="age" min="1" max="120">
                        </div>

                        <!-- Submit Button -->
                        <div class="col-12 mt-4">
                            <button type="submit" onclick="saveUserProfileInfo();" class="btn btn-primary btn-lg w-100">
                                Save Changes
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="resources/script/userProfile/userProfile.js"></script>

<style>
    #mainUserProfilePageMainDIV {
        min-height: 100vh;
        padding-top: 2rem;
        padding-bottom: 2rem;
    }

    .form-label {
        font-weight: 500;
        margin-bottom: 0.5rem;
    }

    .form-control, .form-select {
        padding: 0.75rem 1rem;
        font-size: 1rem;
        border-radius: 0.5rem;
    }

    .form-control:focus, .form-select:focus {
        box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.15);
    }

    .btn-lg {
        padding: 0.75rem 1.5rem;
        font-size: 1rem;
        border-radius: 0.5rem;
    }

    @media (max-width: 576px) {
        .card-body {
            padding: 1.25rem;
        }

        .form-control-lg, .form-select-lg, .btn-lg {
            font-size: 1rem;
            padding: 0.5rem 0.75rem;
        }
    }

    @media (max-width: 768px) {
        #userProfileImage {
            width: 100px;
            height: 100px;
        }
    }

    /* Custom shadow effect */
    .card {
        border: none;
        border-radius: 1rem;
        box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.08);
    }

    /* Smooth transitions */
    .form-control, .form-select, .btn {
        transition: all 0.2s ease-in-out;
    }

    /* Hover effects */
    .btn-primary:hover {
        transform: translateY(-1px);
        box-shadow: 0 0.5rem 1rem rgba(13, 110, 253, 0.15);
    }

    /* Style for the camera icon button */
    .btn-sm.btn-primary.rounded-circle {
        width: 32px;
        height: 32px;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: background-color 0.2s ease-in-out;
    }

    .fa-camera {
        font-size: 14px;
    }

    /* Optional: Add a subtle overlay effect when hovering over the profile image */
    .position-relative:hover #userProfileImage {
        filter: brightness(0.95);
    }

</style>