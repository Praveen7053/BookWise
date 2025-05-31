
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="./base.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SuperAdmin Dashboard</title>
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .sidebar {
            height: 100vh;
            background-color: #2c3e50;
            color: white;
            padding-top: 20px;
        }
        .sidebar .nav-link {
            color: white;
            margin: 5px 0;
        }
        .sidebar .nav-link:hover {
            background-color: #34495e;
        }
        .main-content {
            padding: 20px;
        }
        .dashboard-card {
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            transition: transform 0.3s;
        }
        .dashboard-card:hover {
            transform: translateY(-5px);
        }
        .role-badge {
            margin: 2px;
            font-size: 0.8em;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-2 sidebar">
                <h4 class="text-center mb-4">SuperAdmin Panel</h4>
                <div class="nav flex-column">
                    <a href="#" class="nav-link active" data-bs-toggle="tab" data-bs-target="#dashboard">
                        <i class="fas fa-tachometer-alt me-2"></i> Dashboard
                    </a>
                    <a href="#" class="nav-link" data-bs-toggle="tab" id="userManagementTab" data-bs-target="#users">
                        <i class="fas fa-users me-2"></i> User Management
                    </a>
                    <a href="#" class="nav-link" data-bs-toggle="tab" data-bs-target="#roles">
                        <i class="fas fa-user-shield me-2"></i> Role Management
                    </a>
                    <a href="#" class="nav-link" data-bs-toggle="tab" data-bs-target="#logs">
                        <i class="fas fa-history me-2"></i> Activity Logs
                    </a>
                </div>
            </div>

            <!-- Main Content -->
            <div class="col-md-10 main-content">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2>Welcome, ${userName}</h2>
                    <div class="d-flex align-items-center">
                        <span class="me-3">${userEmail}</span>
                        <form action="${pageContext.request.contextPath}/logout" method="post" class="d-inline m-0">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <button type="submit" class="btn btn-danger">
                                <i class="fas fa-sign-out-alt"></i> Logout
                            </button>
                        </form>
                    </div>
                </div>

                <div class="tab-content">
                    <!-- Dashboard Tab -->
                    <div class="tab-pane fade show active" id="dashboard">
                        <div class="row">
                            <div class="col-md-3 mb-4">
                                <div class="card dashboard-card bg-primary text-white">
                                    <div class="card-body">
                                        <h5 class="card-title">Total Users</h5>
                                        <h2 class="card-text" id="totalUsers">0</h2>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3 mb-4">
                                <div class="card dashboard-card bg-success text-white">
                                    <div class="card-body">
                                        <h5 class="card-title">Active Users</h5>
                                        <h2 class="card-text" id="activeUsers">0</h2>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3 mb-4">
                                <div class="card dashboard-card bg-warning text-white">
                                    <div class="card-body">
                                        <h5 class="card-title">Admins</h5>
                                        <h2 class="card-text" id="adminCount">0</h2>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3 mb-4">
                                <div class="card dashboard-card bg-info text-white">
                                    <div class="card-body">
                                        <h5 class="card-title">Regular Users</h5>
                                        <h2 class="card-text" id="regularUsers">0</h2>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- User Management Tab -->
                    <div class="tab-pane fade" id="users">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0">User Management</h5>
                            </div>
                            <div class="card-body">
                                <table class="table table-hover" id="userTable">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Name</th>
                                            <th>Email</th>
                                            <th>Roles</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <!-- Users will be loaded here dynamically -->
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <!-- Role Management Tab -->
                    <div class="tab-pane fade" id="roles">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0">Role Management</h5>
                            </div>
                            <div class="card-body">
                                <!-- Role management content -->
                            </div>
                        </div>
                    </div>

                    <!-- Activity Logs Tab -->
                    <div class="tab-pane fade" id="logs">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0">Activity Logs</h5>
                            </div>
                            <div class="card-body">
                                <!-- Activity logs content -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Add User Modal -->
    <div class="modal fade" id="editUserModal" tabindex="-1" aria-labelledby="editUserModalLabel" aria-modal="true">
        <div class="modal-dialog" role="dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editUserModalLabel">Edit User Roles</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" onclick="closeAndRefreshModal();" aria-label="Close"></button>
                </div>

                <div class="modal-body">
                    <form id="editUserForm">
                        <input type="hidden" id="editUserId">
                        <div class="mb-3">
                            <label class="form-label">User Roles</label>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="1" id="roleUser">
                                <label class="form-check-label" for="roleUser">User</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="2" id="roleAdmin">
                                <label class="form-check-label" for="roleAdmin">Admin</label>
                            </div>
                        </div>
                    </form>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" onclick="closeAndRefreshModal();">Close</button>
                    <button type="button" class="btn btn-primary" id="saveUserRoles">Save changes</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/script/superadminDashboard.js"></script>

</body>
</html>

<style>
    /* Add this to your existing styles */
    .modal.show {
        background-color: rgba(0, 0, 0, 0.5);
        display: block !important;
    }

    body.modal-open {
        overflow: hidden;
    }
</style>
