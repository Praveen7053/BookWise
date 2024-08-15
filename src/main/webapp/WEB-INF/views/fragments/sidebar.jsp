<!-- sidebar.jsp -->
<div class="col-auto col-md-3 col-xl-2 px-sm-2 px-0 bg-dark">
    <div class="d-flex flex-column align-items-center align-items-sm-start px-3 pt-2 text-white min-vh-100">
        <a class="d-flex align-items-center pb-3 mb-md-0 me-md-auto text-white text-decoration-none">
            <i class="fas fa-bars me-2"></i> <!-- Font Awesome icon for menu -->
            <span class="fs-5 d-none d-sm-inline">Menu</span>
        </a>
        <ul class="nav nav-pills flex-column mb-sm-auto mb-0 align-items-center align-items-sm-start" id="menu">
            <li class="nav-item">
                <a onclick="changeModulePage('homePage');" class="nav-link align-middle px-0">
                    <i class="fas fa-home fs-4"></i> <!-- Font Awesome icon for home -->
                    <span class="ms-1 d-none d-sm-inline">Home</span>
                </a>
            </li>
            <li>
                <a onclick="changeModulePage('uploadBooks');" class="nav-link px-0 align-middle">
                    <i class="fas fa-upload fs-4"></i> <!-- Font Awesome icon for upload -->
                    <span class="ms-1 d-none d-sm-inline">Upload Book</span>
                </a>
            </li>
            <li>
                <a onclick="changeModulePage('booksList');" class="nav-link px-0 align-middle">
                    <i class="fas fa-list fs-4"></i> <!-- Font Awesome icon for book list -->
                    <span class="ms-1 d-none d-sm-inline">Book List</span>
                </a>
            </li>
        </ul>
        <hr>
        <!-- Dropdown -->
    <!-- Dropdown -->
    <div class="dropdown pb-4" style="position: relative;">
        <a id="dropdownToggle" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" aria-expanded="false">
            <img src="https://github.com/mdo.png" alt="Profile Picture" width="30" height="30" class="rounded-circle">
            <span class="d-none d-sm-inline mx-1">loser</span>
        </a>
        <ul id="dropdownMenu" class="dropdown-menu text-small shadow">
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><hr class="dropdown-divider"></li>
            <li>
                <form action="${pageContext.request.contextPath}/logout" method="post" class="d-inline">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button class="dropdown-item" type="submit">Sign out</button>
                </form>
            </li>
        </ul>
    </div>
    </div>
</div>

<script src="resources/script/fragmentsJS/sidebar.js"></script>
