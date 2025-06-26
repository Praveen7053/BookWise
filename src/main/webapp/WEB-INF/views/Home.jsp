<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Home - BookWise</title>
    <%@include file="./base.jsp" %>
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
    <style>
        html, body {
            height: 100%;
            margin: 0;
            overflow: hidden; /* Prevent full-page scroll */
        }

        .full-height-container {
            height: 100vh;
            display: flex;
        }

        .sidebar {
            width: 250px;
            background-color: #343a40;
            color: white;
            display: flex;
            flex-direction: column;
        }

        .sidebar h4 {
            padding: 20px;
        }

        .sidebar .nav {
            flex-grow: 1;
            overflow-y: auto;
            padding-left: 20px;
        }

        .sidebar-footer {
            padding: 15px 20px;
            border-top: 1px solid rgba(255, 255, 255, 0.1);
        }

        .sidebar-profile-img {
            width: 35px;
            height: 35px;
            object-fit: cover;
        }

        .book-card {
            transition: transform 0.2s;
        }

        .book-card:hover {
            transform: scale(1.02);
        }

        .card-img-top {
            height: 220px;
            object-fit: cover;
        }

        .sticky-top {
            top: 0;
            z-index: 10; /* Ensures it stays above other content */
        }

        .main-header {
            position: sticky;
            top: 0;
            z-index: 100;
            background-color: white;
            border-bottom: 1px solid #dee2e6;
        }

        .main-content {
            flex-grow: 1;
            overflow-y: auto;
            background-color: #f8f9fa;
            overflow-x: hidden; /* Add this */
            display: flex;
            flex-direction: column;
        }

        .booksRow {
            padding: 20px; /* adds spacing below sticky header */
        }


    </style>
</head>
<body>
<div class="full-height-container">
    <!-- Sidebar -->
    <div class="sidebar">
        <h4><i class="fas fa-book-reader me-2"></i>BookWise</h4>

        <ul class="nav flex-column">
            <li class="nav-item">
                <a onclick="changeModulePage('readerUserHome');" class="nav-link text-white active" href="#"><i class="fas fa-home me-2"></i>Home</a>
            </li>
            <!-- Add more nav items if needed -->
        </ul>

        <!-- User Dropdown -->
        <div class="dropdown sidebar-footer">
            <a class="nav-link text-white dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                <img src="resources/images/default-user.png" class="rounded-circle sidebar-profile-img me-2" alt="User">
                <span>Hi, <c:out value="${userName}" /></span>
            </a>
            <ul class="dropdown-menu dropdown-menu-dark" aria-labelledby="navbarDropdown">
                <li>
                    <a class="dropdown-item" id="userProfileReader" onclick="selectMenuTabs('userProfileReader'); changeModulePage('userProfileReader');">
                        <i class="fas fa-user me-2"></i>Profile
                    </a>
                </li>

                <li><hr class="dropdown-divider"></li>

                <li>
                    <form action="${pageContext.request.contextPath}/logout" method="post" class="dropdown-item p-0 m-0 border-0 bg-transparent">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <button type="submit" class="btn btn-link text-white w-100 text-start">
                            <i class="fas fa-sign-out-alt me-2"></i>Logout
                        </button>
                    </form>
                </li>
            </ul>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content" id="readerHomeMainDiv">
        <div class="main-header sticky-top bg-white px-4 py-3 shadow-sm">
            <div class="d-flex justify-content-between align-items-center">
                <h2 class="mb-0">Available Books</h2>
                <input type="text" id="searchBookByName" onkeyup="loadAllBooks();" class="form-control w-50 rounded-pill" placeholder="Search books...">
            </div>
        </div>

        <div class="booksRow">
            <!-- Book Cards (same structure, reusable) -->
            <div id="allUserBooks"></div>
        </div>
    </div>
    <jsp:include page="userProfile/userProfile.jsp" />
</div>

<script src="resources/script/home/home.js"></script>
</body>
</html>
