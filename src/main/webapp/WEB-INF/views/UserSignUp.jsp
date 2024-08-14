<!-- WebContent/SignUp.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign Up Page</title>
    <%@include file="./base.jsp" %>
</head>
<body>
    <div class="" style="margin-top:40px;">
        <!-- Alert placeholder -->

        <!-- Sign up form -->
        <section class="signup">
            <div class="container">
                <div class="signup-content">
                    <div class="signup-form">
                        <h3 class="form-title">Book Wise Sign Up</h3>
                        <form id="register-form">
                            <div class="form-group">
                                <label for="name"><i class="zmdi zmdi-account material-icons-name"></i></label>
                                <input type="text" name="name" id="name" placeholder="Your Name"/>
                            </div>
                            <div class="form-group">
                                <label for="phone"><i class="zmdi zmdi-email"></i></label>
                                <input type="text" name="phone" id="phone" placeholder="Your phone number"/>
                            </div>
                            <div class="form-group">
                                <label for="email"><i class="zmdi zmdi-email"></i></label>
                                <input type="email" name="email" id="email" placeholder="Your Email"/>
                            </div>
                            <div class="form-group">
                                <label for="pass"><i class="zmdi zmdi-lock"></i></label>
                                <input type="password" name="pass" id="pass" placeholder="Password"/>
                            </div>
                            <div class="form-group">
                                <label for="re-pass"><i class="zmdi zmdi-lock-outline"></i></label>
                                <input type="password" name="re_pass" id="re_pass" placeholder="Repeat your password"/>
                            </div>
                            <div class="form-group form-button">
                                <input type="submit" id="signup" class="form-submit" value="Register"/>
                            </div>
                        </form>
                    </div>
                    <div class="signup-image">
                        <figure><img src="resources/images/signup-image.jpg" alt="sign up image"></figure>
                        <a href="${pageContext.request.contextPath}/" class="signup-image-link">I am already member</a>
                    </div>
                </div>
            </div>
        </section>
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
