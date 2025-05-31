<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Seller Page</title>
    <%@include file="./base.jsp" %>

</head>
<body>
    <input type="hidden" id="userNameHidden" value="<sec:authentication property='principal.username' />"/>
    <input type="hidden" id="userEmailHidden" value="<sec:authentication property='principal.userEmail' />"/>
    <input type="hidden" id="loggedInUserId" value="<sec:authentication property='principal.userId' />"/>

    <div class="container-fluid">
        <div class="row flex-nowrap">

            <!-- Include the Sidebar -->
            <jsp:include page="fragments/sellerSidebar.jsp" />

            <!-- Content Area -->
            <div class="col py-3" style="flex: 1; height: 100vh;">
                <jsp:include page="sellerView/sellerHomeRightSection.jsp" />
                <jsp:include page="sellerView/uploadNewBooks.jsp" />
                <jsp:include page="sellerView/sellerListBooks.jsp" />
                <jsp:include page="userProfile/userProfile.jsp" />
            </div>
        </div>
    </div>

</body>
</html>

<script>

$(document).ready(function() {
    searchUserBook();
});

</script>