<!-- WebContent/base.jsp -->
<%@page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">

<title><c:out value="${title}">Login Page</c:out></title>

<!-- Third-party CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

<!-- DFlip PDF Viewer CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/lib/dflip/css/dflip.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/lib/dflip/css/themify-icons.min.css">

<!-- Your Custom CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/commonStyle.css"/>

<!-- Common HTML Elements -->
<div id="common-alert-placeholder-div" style="position: fixed; top: 20px; right: 20px; z-index: 9999;"></div>
<meta name="context-path" content="${pageContext.request.contextPath}" />
<meta name="csrf-token" content="${_csrf.token}" />

<!-- Progress Bar Div -->
<div id="progressBarDiv" style="display: none;">
    <div class="progress-bar-overlay">
        <div class="progress-bar-spinner"></div>
    </div>
</div>

<!-- Custom Confirmation Popup -->
<div id="jConfirmModal" class="jConfirm-modal">
    <div class="jConfirm-modal-content">
        <div class="jConfirm-modal-body" id="jConfirmMessage"><!-- Message will be inserted here --></div>
        <div class="jConfirm-modal-footer">
            <button type="button" class="jConfirm-btn jConfirm-cancel">No</button>
            <button type="button" class="jConfirm-btn jConfirm-confirm">Yes</button>
        </div>
    </div>
</div>

<!-- Hidden User Info -->
<input type="hidden" id="userNameHidden" value="${userName}"/>
<input type="hidden" id="userEmailHidden" value="${userEmail}"/>
<input type="hidden" id="loggedInUserId" value="${userId}" />

<!-- Core JS Libraries -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>

<!-- DFlip JS libraries (Dependencies first) -->
<script src="${pageContext.request.contextPath}/resources/lib/dflip/js/libs/three.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/lib/dflip/js/libs/mockup.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/lib/dflip/js/libs/pdf.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/lib/dflip/js/libs/compatibility.js"></script>
<script src="${pageContext.request.contextPath}/resources/lib/dflip/js/dflip.min.js"></script>

<!-- Set global variable for dflip.js library location -->
<script>
    // This tells dflip.js where to find its assets (like sounds, icons, etc.)
    var DF_FLIP_LOCATION = "${pageContext.request.contextPath}/resources/lib/dflip/";
</script>

<!-- Your Custom Common JS -->
<script src="${pageContext.request.contextPath}/resources/script/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/commonComponents.js"></script>
<script src="${pageContext.request.contextPath}/resources/script/fragmentsJS/ajaxUtility.js"></script>

<!-- DFlip Viewer Logic -->
<script src="${pageContext.request.contextPath}/resources/script/dflip-viewer/dflip-viewer.js"></script>