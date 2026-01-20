<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Reset Password | BookWise</title>
    <%@include file="./base.jsp" %>
</head>
<body>

<div class="card text-center" style="max-width:420px;margin:80px auto;padding:30px;">
    <h4 class="mb-3">Reset Password</h4>

    <input type="hidden" id="token" value="${token}" />

    <input type="password" id="password" class="form-control mb-3"
           placeholder="New Password" />

    <input type="password" id="confirmPassword" class="form-control mb-3"
           placeholder="Confirm Password" />

    <button id="resetBtn" class="btn btn-success w-100">
        Update Password
    </button>
</div>

<script>
    $('#resetBtn').click(function () {

        clearAlerts();

        var password = $('#password').val().trim();
        var confirmPassword = $('#confirmPassword').val().trim();
        var token = $('#token').val();

        if (password === '' || confirmPassword === '') {
            showErrorAlert("All fields are required.");
            return;
        }

        if (password.length < 6) {
            showErrorAlert("Password must be at least 6 characters.");
            return;
        }

        if (password !== confirmPassword) {
            showErrorAlert("Passwords do not match.");
            return;
        }

        var contextPath = $('meta[name="context-path"]').attr('content');
        var url = contextPath + '/api/actions/reset-password/save';

        showProgressBar("progressBarDiv", "bodyDiv");

        postFormData(url, {
            token: token,
            password: password,
            confirmPassword: confirmPassword
        }, function (html) {
            closeProgressBar("progressBarDiv", "bodyDiv");

            // JSP page returned → render it
            document.open();
            document.write(html);
            document.close();
        });
    });
</script>

</body>
</html>
