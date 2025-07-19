<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Home - BookWise</title>
    <%@include file="./base.jsp" %>
    <%@ include file="dflip-viewer/dflip-viewer.jsp" %>    <style>
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
            transition: transform 0.3s ease;
            z-index: 200;
        }
        .sidebar.hide {
            transform: translateX(-100%);
        }
        .sidebar.show {
            transform: translateX(0);
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
        .sticky-top {
            top: 0;
            z-index: 100; /* Ensures it stays above other content */
        }
        .main-header {
            position: sticky;
            top: 0;
            z-index: 100;
            background-color: white;
            border-bottom: 1px solid #dee2e6;
            display: flex;
            align-items: center;
        }
        .main-content {
            flex-grow: 1;
            overflow-y: auto;
            background-color: #f8f9fa;
            overflow-x: hidden; /* Add this */
            display: flex;
            flex-direction: column;
            transition: margin-left 0.3s;
        }
        .booksRow {
            padding: 20px; /* adds spacing below sticky header */
        }
        /* Hamburger button */
        .sidebar-toggle-btn {
            display: none;
            background: none;
            border: none;
            font-size: 2rem;
            margin-right: 16px;
            color: #343a40;
            cursor: pointer;
        }
        /* Responsive styles */
        @media (max-width: 991.98px) {
            .full-height-container {
                flex-direction: column;
            }
            .sidebar {
                position: fixed;
                left: 0;
                top: 0;
                height: 100vh;
                transform: translateX(-100%);
                box-shadow: 2px 0 8px rgba(0,0,0,0.15);
            }
            .sidebar.show {
                transform: translateX(0);
            }
            .main-content {
                margin-left: 0 !important;
            }
            .sidebar-toggle-btn {
                display: inline-block;
            }
        }
        @media (max-width: 767.98px) {
            .main-header h2 {
                font-size: 1.2rem;
            }
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
    </style>
    <style>
        /* Custom user dropdown for sidebar */
        .user-dropdown {
            position: relative;
        }
        .user-dropdown-toggle {
            cursor: pointer;
            user-select: none;
            width: 100%;
            padding: 8px 0;
        }
        .user-dropdown-menu {
            display: none;
            position: absolute;
            left: 0;
            bottom: 48px;
            width: 100%;
            background: #23272b;
            color: #fff;
            border-radius: 0 0 8px 8px;
            box-shadow: 0 4px 16px rgba(0,0,0,0.18);
            z-index: 9999;
            padding: 0.5rem 0;
        }
        .user-dropdown-menu .dropdown-item {
            color: #fff;
            padding: 10px 24px;
            display: flex;
            align-items: center;
            background: none;
            border: none;
            width: 100%;
            text-align: left;
        }
        .user-dropdown-menu .dropdown-item:hover, .user-dropdown-menu .dropdown-item:focus {
            background: #343a40;
            color: #ffd600;
        }
        .user-dropdown-menu .dropdown-divider {
            height: 1px;
            margin: 0.5rem 0;
            background: rgba(255,255,255,0.1);
            border: none;
        }
        @media (max-width: 991.98px) {
            .user-dropdown-menu {
                left: 0;
                width: 100%;
                min-width: 0;
                border-radius: 0 0 8px 8px;
            }
        }
        /* Custom: Sidebar user dropdown menu responsive fix */
        @media (max-width: 991.98px) {
            .sidebar .dropdown-menu {
                position: static !important;
                float: none;
                width: 100%;
                margin: 0;
                box-shadow: none;
                border-radius: 0 0 8px 8px;
                z-index: 9999;
            }
            .sidebar .dropdown-menu .dropdown-item {
                text-align: left;
                width: 100%;
            }
            .sidebar .dropdown {
                position: relative;
            }
        }
        @media (max-width: 575.98px) {
            .sidebar .dropdown-menu {
                min-width: 0;
                width: 100vw;
                left: 0 !important;
                right: 0 !important;
            }
        }
        /* Always ensure sidebar dropdown is above sidebar and not clipped */
        .sidebar .dropdown-menu {
            z-index: 9999;
        }
        .sidebar .dropdown {
            position: relative;
        }
    </style>
</head>
<body>
<div class="full-height-container">
    <!-- Sidebar -->
    <div class="sidebar" id="sidebar">
        <h4><i class="fas fa-book-reader me-2"></i>BookWise</h4>

        <ul class="nav flex-column">
            <li class="nav-item">
                <a onclick="changeModulePage('readerUserHome');" class="nav-link text-white active" href="#"><i class="fas fa-home me-2"></i>Home</a>
            </li>
            <li class="nav-item">
                <a onclick="changeModulePage('myBookshelf');" class="nav-link text-white" href="#"><i class="fas fa-bookmark me-2"></i>My Bookshelf</a>
            </li>
            <!-- Add more nav items if needed -->
        </ul>

        <!-- User Dropdown -->
        <div class="sidebar-footer user-dropdown" id="sidebarUserDropdown">
            <div class="user-dropdown-toggle d-flex align-items-center" tabindex="0" id="sidebarUserDropdownToggle">
                <img id="sidebarReaderProfileImage" src="resources/images/default-user.png" class="rounded-circle sidebar-profile-img me-2" alt="User">
                <span id="sideBarLoginReaderUserName" class="mx-1"></span>
                <i class="fas fa-chevron-down ms-auto"></i>
            </div>
            <div class="user-dropdown-menu" id="sidebarUserDropdownMenu">
                <a class="dropdown-item" id="userProfileReader" onclick="selectMenuTabs('userProfileReader'); changeModulePage('userProfileReader');">
                    <i class="fas fa-user me-2"></i>Profile
                </a>
                <div class="dropdown-divider"></div>
                <form action="${pageContext.request.contextPath}/logout" method="post" class="dropdown-item p-0 m-0 border-0 bg-transparent">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button type="submit" class="btn btn-link text-white w-100 text-start">
                        <i class="fas fa-sign-out-alt me-2"></i>Logout
                    </button>
                </form>
            </div>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content" id="readerHomeMainDiv">
        <div class="main-header sticky-top bg-white px-4 py-3 shadow-sm">
            <div class="d-flex justify-content-between align-items-center">
                <div class="d-flex align-items-center">
                    <button class="sidebar-toggle-btn" id="sidebarToggleBtn" aria-label="Toggle sidebar" aria-controls="sidebar" aria-expanded="false">
                        <i class="fas fa-bars"></i>
                    </button>
                    <h2 class="mb-0">Available Books</h2>
                </div>
                <input type="text" id="searchBookByName" onkeyup="loadAllBooks();" class="form-control w-50 rounded-pill" placeholder="Search books...">
            </div>
        </div>

        <div class="booksRow">
            <!-- Book Cards (same structure, reusable) -->
            <div id="allUserBooks"></div>
        </div>
    </div>
    <jsp:include page="userProfile/userProfile.jsp" />
    <jsp:include page="book-details/bookDetails.jsp" />
    <jsp:include page="bookshelf/myBookshelf.jsp" />
    
    <!-- Include bookshelf.js only once for all bookshelf functionality -->
    <script src="${pageContext.request.contextPath}/resources/script/bookShelf/bookshelf.js"></script>
</div>

<script src="resources/script/home/home.js"></script>
<script>
// Custom sidebar user dropdown logic
(function() {
    const toggle = document.getElementById('sidebarUserDropdownToggle');
    const menu = document.getElementById('sidebarUserDropdownMenu');
    if (toggle && menu) {
        toggle.addEventListener('click', function(e) {
            e.stopPropagation();
            menu.style.display = (menu.style.display === 'block') ? 'none' : 'block';
        });
        // Keyboard accessibility
        toggle.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                menu.style.display = (menu.style.display === 'block') ? 'none' : 'block';
            }
        });
        // Close dropdown when clicking outside
        document.addEventListener('click', function(e) {
            if (!toggle.contains(e.target) && !menu.contains(e.target)) {
                menu.style.display = 'none';
            }
        });
        // Optional: close on Escape
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                menu.style.display = 'none';
            }
        });
    }
})();
</script>
</body>
</html>
