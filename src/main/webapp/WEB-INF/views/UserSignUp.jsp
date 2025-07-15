
<!-- WebContent/SignUp.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign Up Page</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/loginNSignupStyle.css"/>
    <%@include file="./base.jsp" %>
    <style>
        body {
            background: #f8f9fa;
        }
        .signup-card {
            border-radius: 1rem;
            box-shadow: 0 4px 24px rgba(0,0,0,0.08);
            overflow: hidden;
        }
        .signup-image-bg {
            background: #fff;
        }
        .form-title {
            font-weight: 600;
            color: #2c3e50;
        }
        .form-submit {
            width: 100%;
            padding: 12px;
            font-size: 1.1rem;
        }
        @media (max-width: 767.98px) {
            .signup-image-bg { display: none; }
            .signup-card { border-radius: 0.5rem; }
        }
    </style>
</head>
<body>
<div class="container min-vh-100 d-flex align-items-center justify-content-center">
    <div class="row w-100 justify-content-center">
        <div class="col-lg-8 col-md-10">
            <div class="card signup-card border-0">
                <div class="row g-0 align-items-center">
                    <!-- Image (desktop) -->
                    <div class="col-md-6 signup-image-bg d-none d-md-flex align-items-center justify-content-center">
                        <img src="resources/images/signup-image.jpg" class="img-fluid p-4" alt="sign up image" style="max-height: 350px;">
                    </div>
                    <!-- Form -->
                    <div class="col-md-6 col-12 p-4">
                        <!-- Mobile logo (visible only on mobile) -->
                        <div class="w-100 d-block d-md-none text-center mb-3">
                            <img src="resources/images/signup-image.jpg" class="img-fluid" alt="BookWise logo" style="max-width: 120px;">
                        </div>
                        <h3 class="form-title text-center mb-4">Book Wise Sign Up</h3>
                        <form id="register-form">
                            <div class="form-group mb-3">
                                <label for="name" class="form-label"><i class="zmdi zmdi-account material-icons-name"></i></label>
                                <input type="text" name="name" id="name" class="form-control" placeholder="Your Name" required />
                            </div>
                            <div class="form-group mb-3">
                                <label for="phone" class="form-label"><i class="zmdi zmdi-email"></i></label>
                                <input type="text" name="phone" id="phone" class="form-control" placeholder="Your phone number" required />
                            </div>
                            <div class="form-group mb-3">
                                <label for="email" class="form-label"><i class="zmdi zmdi-email"></i></label>
                                <input type="email" name="email" id="email" class="form-control" placeholder="Your Email" required />
                            </div>
                            <div class="form-group mb-3">
                                <label for="pass" class="form-label"><i class="zmdi zmdi-lock"></i></label>
                                <input type="password" name="pass" id="pass" class="form-control" placeholder="Password" required />
                            </div>
                            <div class="form-group mb-3">
                                <label for="re-pass" class="form-label"><i class="zmdi zmdi-lock-outline"></i></label>
                                <input type="password" name="re_pass" id="re_pass" class="form-control" placeholder="Repeat your password" required />
                            </div>
                            <div class="form-group mb-3">
                                <input type="submit" id="signup" class="btn btn-primary form-submit" value="Register"/>
                            </div>
                        </form>
                        <div class="text-center mt-2">
                            <a href="${pageContext.request.contextPath}/" class="small">I am already member</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function() {
        $('#register-form').submit(function(event) {
            event.preventDefault();
            showProgressBar("progressBarDiv", "bodyDiv");
            $.ajax({
                type: 'POST',
                url: '${pageContext.request.contextPath}/userSignupNLogin/registerNewUser',
                data: $('#register-form').serialize(),
                success: function(response) {
                    clearAlerts();
                    closeProgressBar("progressBarDiv", "bodyDiv");
                    if (response.success) {
                        showSuccessAlert(response.message);
                        setTimeout(function() {
                            window.location.href = '${pageContext.request.contextPath}/Login';
                        }, 2000);
                    } else {
                        closeProgressBar("progressBarDiv", "bodyDiv");
                        showErrorAlert(response.message);
                    }
                },
                error: function() {
                    showErrorAlert('An error occurred. Please try again.');
                }
            });
        });
    });
</script>
</body>
</html>
