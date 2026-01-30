<!-- Common Reader Sidebar Fragment -->
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
            <a class="dropdown-item nav-link" id="userProfileReader" onclick="selectMenuTabs('userProfileReader'); changeModulePage('userProfileReader');">
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
