<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Forgot Password | BookWise</title>
    <%@include file="./base.jsp" %>
    <style>
        body { background: #f8f9fa; }
        .card {
            max-width: 420px;
            margin: 80px auto;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
            text-align: center;
        }
    </style>
</head>
<body>

<div class="card" id="bodyDiv">

    <!-- 🔹 FORM DIV -->
    <div id="forgotFormDiv">
        <h4 class="mb-3">Forgot Password</h4>
        <p class="text-muted mb-4">
            Enter your registered email. We’ll send you a password reset link.
        </p>

        <form id="forgot-form">
            <input type="email"
                   id="forgotEmail"
                   class="form-control mb-3"
                   placeholder="Enter your email"
                   required />

            <button type="submit" class="btn btn-primary w-100">
                Send Reset Link
            </button>
        </form>
    </div>

    <!-- 🔹 SUCCESS DIV -->
    <div id="forgotSuccessDiv" style="display:none;">
        <img src="${pageContext.request.contextPath}/resources/images/email-sent.png"
             alt="Email Sent"
             style="width:120px;margin-bottom:15px;" />

        <h5 class="text-success">Reset link sent!</h5>

        <p class="text-muted">
            Please check your email to reset your password.
            <br/>
            (Also check spam folder)
        </p>

        <p class="text-muted mt-2">
            Redirecting to login in <span id="redirectCounter">10</span> seconds…
        </p>

        <a href="${pageContext.request.contextPath}/login"
           class="btn btn-success mt-3">
            Go to Login
        </a>
    </div>

</div>

<script>
$(document).ready(function () {

    $('#forgot-form').submit(function (e) {
        e.preventDefault();

        clearAlerts();

        var email = $('#forgotEmail').val().trim().toLowerCase();

        if (email === '') {
            showErrorAlert("Email is required.");
            return;
        }

        var contextPath = $('meta[name="context-path"]').attr('content');
        var url = contextPath + '/api/actions/forgot-password/send';

        var data = JSON.stringify({ email: email });

        showProgressBar("progressBarDiv", "bodyDiv");

        postData(url, data, 'json', function (response) {
            closeProgressBar("progressBarDiv", "bodyDiv");

            if (response.success) {
                $('#forgotFormDiv').hide();
                $('#forgotSuccessDiv').fadeIn();

                // ⏱ 10-second redirect countdown
                var seconds = 10;
                var counter = setInterval(function () {
                    seconds--;
                    $('#redirectCounter').text(seconds);

                    if (seconds <= 0) {
                        clearInterval(counter);
                        window.location.href = contextPath + '/login';
                    }
                }, 1000);

            } else {
                showErrorAlert(response.message);
            }
        });
    });

});
</script>

</body>
</html>
