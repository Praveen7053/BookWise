<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Home - BookWise</title>
    <%@include file="./base.jsp" %>
    <%@ include file="dflip-viewer/dflip-viewer.jsp" %>
    <!-- Common Reader Styles -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/readerCommon.css"/>
    <style>
        /* Page-specific styles */
        .book-card {
            transition: transform 0.2s, box-shadow 0.2s;
            border-radius: 16px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.07);
            background: #fff;
        }
        .book-card:hover {
            transform: scale(1.03);
            box-shadow: 0 4px 16px rgba(0,0,0,0.12);
        }
        .card-img-top {
            height: 220px;
            object-fit: cover;
            border-radius: 16px 16px 0 0;
        }
        .booksRow {
            padding: 20px; /* adds spacing below sticky header */
        }
        @media (max-width: 767.98px) {
            .booksRow {
                padding: 10px;
            }
            .book-card {
                margin-bottom: 16px;
            }
            .card-img-top {
                height: 160px;
            }
        }
        /* Book card grid improvements */
        .row {
            margin-left: 0;
            margin-right: 0;
        }
        .col-md-4, .col-lg-3 {
            padding-left: 8px;
            padding-right: 8px;
        }
        /* Touch-friendly buttons */
        .btn {
            min-height: 40px;
            font-size: 1rem;
        }
        /* Improved contrast for text */
        .card-title.text-primary {
            color: #1a237e !important;
        }
        .card-footer.bg-light {
            background: #f1f3f4 !important;
        }
        /* Search bar styles */
        .home-search-container {
            padding: 15px 20px;
            background-color: #fff;
            border-bottom: 1px solid #dee2e6;
        }
    </style>
</head>
<body>
<div class="full-height-container">
    <!-- Common Sidebar -->
    <jsp:include page="fragments/readerSidebar.jsp" />

    <!-- Main Content -->
    <div class="main-content">
        <!-- Common Header - Always visible -->
        <jsp:include page="fragments/readerHeader.jsp" />

        <!-- Home Page Content -->
        <div id="readerHomeMainDiv" style="display: flex; flex-direction: column;">
            <!-- Page-specific search bar -->
            <div class="home-search-container">
                <input type="text"
                       id="searchBookByName"
                       onkeyup="loadAllBooks();"
                       class="form-control rounded-pill"
                       placeholder="Search books...">
            </div>

            <div class="booksRow">
                <!-- Book Cards (same structure, reusable) -->
                <div id="allUserBooks"></div>
            </div>
        </div>

        <!-- Other Pages (included but hidden by default) -->
        <jsp:include page="userProfile/userProfile.jsp" />
        <jsp:include page="book-details/bookDetails.jsp" />
        <jsp:include page="bookshelf/myBookshelf.jsp" />
    </div>
    
    <!-- Include bookshelf.js only once for all bookshelf functionality -->
    <script src="${pageContext.request.contextPath}/resources/script/bookShelf/bookshelf.js"></script>
</div>

<!-- Common Reader JavaScript -->
<script src="${pageContext.request.contextPath}/resources/script/readerCommon.js"></script>
<script src="resources/script/home/home.js"></script>
</body>
</html>
