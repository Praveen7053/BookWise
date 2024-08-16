<!-- WebContent/Login.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Page</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/loginNSignupStyle.css"/>
    <%@include file="./base.jsp" %>
</head>
<body>
    <div class="" style="margin-top:40px;">

        <!-- Sign in Form -->
        <section class="signin">
            <div class="container">
                <div class="signin-content">
                    <div class="signin-image">
                        <figure><img src="resources/images/signin-image.jpg" alt="sign up image"></figure>
                        <a href="${pageContext.request.contextPath}/userSignUpPage" class="signup-image-link">Create an account</a>
                    </div>

                    <div class="signin-form">
                        <h3 class="form-title">Book Wise Log In</h3>
                        <form id="login-form">
                            <div class="form-group">
                                <label for="userLoginId"><i class="zmdi zmdi-account material-icons-name"></i></label>
                                <input type="text" name="userLoginId" id="userLoginId" placeholder="Email / Phone Number"/>
                            </div>
                            <div class="form-group">
                                <label for="your_pass"><i class="zmdi zmdi-lock"></i></label>
                                <input type="password" name="your_pass" id="your_pass" placeholder="Password"/>
                            </div>
                            <div class="form-group">
                                <input type="checkbox" name="remember-me" id="remember-me" class="agree-term"/>
                                <label for="remember-me" class="label-agree-term"><span><span></span></span>Remember me</label>
                            </div>
                            <div class="form-group form-button">
                                <input type="submit" name="signin" id="signin" class="form-submit" value="Log in"/>
                            </div>
                        </form>
                        <div class="social-login">
                            <span class="social-label">Or login with</span>
                            <ul class="socials">
                                <li><a href="#"><i class="display-flex-center zmdi zmdi-facebook"></i></a></li>
                                <li><a href="#"><i class="display-flex-center zmdi zmdi-twitter"></i></a></li>
                                <li><a href="#"><i class="display-flex-center zmdi zmdi-google"></i></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </section>

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
                            console.log('response.redirectUrl :: '+response.redirectUrl);
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
