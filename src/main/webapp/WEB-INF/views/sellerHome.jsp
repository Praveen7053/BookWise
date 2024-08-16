<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Seller Page</title>
    <%@include file="./base.jsp" %>

</head>
<body>
    <div class="container-fluid">
        <div class="row flex-nowrap">

            <!-- Include the Sidebar -->
            <jsp:include page="fragments/sidebar.jsp" />

            <!-- Content Area -->
            <div class="col py-3">
                <jsp:include page="Home.jsp" />
                <jsp:include page="sellerView/uploadNewBooks.jsp" />
                <jsp:include page="sellerView/uploadedBooks.jsp" />
                <jsp:include page="userProfile/userProfile.jsp" />
            </div>

        </div>
    </div>
    <script src="resources\script\sellerHome.js"></script>
</body>
</html>
