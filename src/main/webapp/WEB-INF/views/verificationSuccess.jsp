<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Email Verified | BookWise</title>
    <%@include file="./base.jsp" %>
    <style>
        body {
            background: #f8f9fa;
        }
        .verify-card {
            max-width: 500px;
            margin: 80px auto;
            padding: 30px;
            border-radius: 12px;
            background: #ffffff;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
            text-align: center;
        }
        .verify-card h2 {
            color: #28a745;
            margin-bottom: 15px;
        }
        .verify-card p {
            color: #555;
            margin-bottom: 25px;
            font-size: 15px;
        }
        .verify-card a {
            padding: 10px 25px;
            font-size: 16px;
        }
    </style>
</head>
<body>

<div class="verify-card">
    <h2>✅ ${title}</h2>

    <p>
        ${message}
    </p>

    <a href="${pageContext.request.contextPath}/login"
       class="btn btn-success">
        Go to Login
    </a>
</div>

</body>
</html>
