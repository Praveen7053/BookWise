<!-- WebContent/Login.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Page</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/loginNSignupStyle.css"/>
    <%@include file="./base.jsp" %>
    <style>
        body {
            background: #f8f9fa;
        }
        .login-card {
            border-radius: 1rem;
            box-shadow: 0 4px 24px rgba(0,0,0,0.08);
            overflow: hidden;
        }
        .login-image {
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
        .social-login {
            margin-top: 1.5rem;
        }
        .socials {
            display: flex;
            justify-content: center;
            gap: 1rem;
        }
        .socials li a {
            font-size: 1.5rem;
            color: #6dabe4;
            transition: color 0.2s;
        }
        .socials li a:hover {
            color: #4292dc;
        }
        /* Force show checkbox for remember-me */
        #remember-me.form-check-input {
          display: inline-block !important;
        }
        @media (max-width: 767.98px) {
            .login-image { display: none; }
            .login-card { border-radius: 0.5rem; }
        }
    </style>
</head>
<body>
<div class="container min-vh-100 d-flex align-items-center justify-content-center">
    <div class="row w-100 justify-content-center">
        <div class="col-lg-8 col-md-10">
            <div class="card login-card border-0">
                <div class="row g-0 align-items-center">
                    <!-- Image (hidden on mobile) -->
                    <div class="col-md-6 login-image d-none d-md-flex align-items-center justify-content-center">
                        <img src="resources/images/signin-image.jpg" class="img-fluid p-4" alt="sign in image" style="max-height: 350px;">
                    </div>
                    <!-- Form -->
                    <div class="col-md-6 col-12 p-4">
                        <!-- Mobile logo (visible only on mobile) -->
                        <div class="w-100 d-block d-md-none text-center mb-3">
                            <img src="resources/images/signin-image.jpg" class="img-fluid" alt="BookWise logo" style="max-width: 120px;">
                        </div>
                        <h3 class="form-title text-center mb-4">Book Wise Log In</h3>
                        <form id="login-form">
                            <div class="form-group mb-3">
                                <label for="userLoginId" class="form-label"><i class="zmdi zmdi-account material-icons-name"></i></label>
                                <input type="text" name="userLoginId" id="userLoginId" class="form-control" placeholder="User ID / Email / Phone Number" required />
                            </div>
                            <div class="form-group mb-3">
                                <label for="your_pass" class="form-label"><i class="zmdi zmdi-lock"></i></label>
                                <input type="password" name="your_pass" id="your_pass" class="form-control" placeholder="Password" required />
                            </div>
                            <div class="form-group mb-3">
                                <input type="submit" name="signin" id="signin" class="btn btn-primary form-submit" value="Log in"/>
                            </div>
                        </form>
                        <div class="text-center mt-2">
                            <a href="${pageContext.request.contextPath}/userSignUpPage" class="small">Create an account</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function() {
        $('#login-form').submit(function(event) {
            event.preventDefault();
            showProgressBar("progressBarDiv", "bodyDiv");
            $.ajax({
                type: 'POST',
                url: '${pageContext.request.contextPath}/userSignupNLogin/loginRegisteredUser',
                data: $('#login-form').serialize(),
                success: function(response) {
                    clearAlerts();
                    closeProgressBar("progressBarDiv", "bodyDiv");
                    if (response.success) {
                        showSuccessAlert(response.message);
                        window.location.href = response.redirectUrl;
                    } else {
                        showErrorAlert(response.message);
                    }
                },
                error: function() {
                    closeProgressBar("progressBarDiv", "bodyDiv");
                    showErrorAlert('An error occurred. Please try again.');
                }
            });
        });
    });
</script>
</body>
</html>
