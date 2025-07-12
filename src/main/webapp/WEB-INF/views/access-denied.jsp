<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Access Denied</title>
    <%@include file="./base.jsp" %>
</head>
<body class="d-flex align-items-center justify-content-center vh-100 bg-light">
    <div class="text-center">
        <h1 class="display-1 fw-bold">403</h1>
        <p class="fs-3"> <span class="text-danger">Access Denied</span></p>
        <p class="lead">
            You do not have permission to view this page.
        </p>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Go to Home</a>
    </div>
</body>
</html>