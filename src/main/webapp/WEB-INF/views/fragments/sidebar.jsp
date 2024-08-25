<!-- sidebar.jsp -->
<div class="col-auto col-md-3 col-xl-2 px-sm-2 px-0 bg-dark">
    <div class="d-flex flex-column align-items-center align-items-sm-start px-3 pt-2 text-white min-vh-100">
        <a class="d-flex align-items-center pb-3 mb-md-0 me-md-auto text-white text-decoration-none">
            <i class="fas fa-bars me-3 fs-3"></i> <!-- Increased icon size for Menu -->
            <span class="fs-5 d-none d-sm-inline">Menu</span>
        </a>
        <ul class="nav nav-pills flex-column mb-sm-auto mb-0 align-items-center align-items-sm-start w-100" id="menu">
            <li class="nav-item w-100">
                <a id="homeTab" onclick="selectMenuTabs('homeTab'); changeModulePage('homePage');" class="nav-link align-middle text-white w-100 active">
                    <i class="fas fa-home fs-5"></i> <!-- Smaller icon for Home -->
                    <span class="ms-1 d-none d-sm-inline">Home</span>
                </a>
            </li>
            <li class="nav-item w-100">
                <a id="uploadTab" onclick="selectMenuTabs('uploadTab'); changeModulePage('uploadBooks'); clearUploadedNewBookFields();" class="nav-link align-middle text-white w-100">
                    <i class="fas fa-upload fs-5"></i> <!-- Smaller icon for Upload Book -->
                    <span class="ms-1 d-none d-sm-inline">Upload Book</span>
                </a>
            </li>
            <li class="nav-item w-100">
                <a id="listTab" onclick="selectMenuTabs('listTab'); changeModulePage('booksList'); getUploadedBooks();" class="nav-link align-middle text-white w-100">
                    <i class="fas fa-list fs-5"></i> <!-- Smaller icon for Book List -->
                    <span class="ms-1 d-none d-sm-inline">Book List</span>
                </a>
            </li>
        </ul>
        <hr>
        <!-- Dropdown -->
        <div class="dropdown pb-4" style="position: relative;">
            <a id="userProfileDropdownToggle" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" aria-expanded="false">
                <img src="https://github.com/mdo.png" alt="Profile Picture" width="30" height="30" class="rounded-circle">
                <span class="d-none d-sm-inline mx-1">loser</span>
            </a>
            <ul id="dropdownMenu" class="dropdown-menu text-small shadow">
                <li><a class="dropdown-item" id="userProfileTab" onclick="selectMenuTabs('userProfileTab'); changeModulePage('userProfile');">Profile</a></li>
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
