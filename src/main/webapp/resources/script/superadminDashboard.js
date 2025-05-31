function initializeEventListeners() {
    // Tab click handlers
    document.getElementById('userManagementTab').onclick = function() {
        loadUsers();
    };

    // Save roles button click handler
    document.getElementById('saveUserRoles').onclick = function() {
        handleSaveRoles();
    };
}

function loadUsers() {
    var contextPath = $('meta[name="context-path"]').attr('content');

    getData(contextPath + '/api/admin/users', 'json', function(users) {
        const tbody = document.querySelector('#userTable tbody');
        tbody.innerHTML = '';

        users.forEach(user => {
            const roles = user.roles.map(role =>
                `<span class="badge bg-secondary role-badge">${role}</span>`
            ).join(' ');

            const row = `
                <tr>
                    <td>${user.userId}</td>
                    <td>${user.userName}</td>
                    <td>${user.userEmail}</td>
                    <td>${roles}</td>
                    <td>
                        <button class="btn btn-sm btn-primary edit-roles"
                                data-user-id="${user.userId}"
                                data-user-roles="${user.roles}">
                            Edit Roles
                        </button>
                    </td>
                </tr>
            `;
            tbody.insertAdjacentHTML('beforeend', row);
        });

        // Add click handlers for edit buttons after rendering
        document.querySelectorAll('.edit-roles').forEach(button => {
            button.addEventListener('click', function() {
                const userId = this.getAttribute('data-user-id');
                const roles = this.getAttribute('data-user-roles');
                handleEditRolesClick(userId, roles);
            });
        });
    });
}

function handleEditRolesClick(userId, roles) {
    try {
        // Role mapping with corresponding IDs
        const roleMapping = {
            'ROLE_USER': {value: '1', id: 'roleUser'},
            'ROLE_ADMIN': {value: '2', id: 'roleAdmin'},
            'ROLE_SUPERADMIN': {value: '3', id: 'roleSuperAdmin'}
        };

        let userRoles = [];
        if (roles && roles !== 'undefined' && roles !== 'null') {
            userRoles = roles.split(',').map(role => role.trim());
        }

        // Set user ID in hidden field
        document.getElementById('editUserId').value = userId;

        // Reset all checkboxes first
        const allCheckboxes = document.querySelectorAll('#editUserForm input[type="checkbox"]');
        allCheckboxes.forEach(checkbox => {
            checkbox.checked = false;
        });

        // Check the boxes based on user's roles
        userRoles.forEach(role => {
            const mappedRole = roleMapping[role];

            if (mappedRole) {
                // Try finding checkbox by ID first
                const checkbox = document.getElementById(mappedRole.id);
                if (checkbox) {
                    checkbox.checked = true;
                }
            }
        });

        // Show modal
        const modalElement = document.getElementById('editUserModal');
        modalElement.style.display = 'block';
        modalElement.classList.add('show');
        document.body.classList.add('modal-open');

        // Add backdrop
        if (!document.querySelector('.modal-backdrop')) {
            const backdrop = document.createElement('div');
            backdrop.classList.add('modal-backdrop', 'fade', 'show');
            document.body.appendChild(backdrop);
        }

    } catch (error) {
        console.error('Error in handleEditRolesClick:', error);
    }
}

function handleSaveRoles() {
    const userId = document.getElementById('editUserId').value;
    const roleValueMapping = {
        '1': 'ROLE_USER',
        '2': 'ROLE_ADMIN'
    };

    const selectedRoles = Array.from(document.querySelectorAll('#editUserForm input[type="checkbox"]:checked'))
        .map(checkbox => roleValueMapping[checkbox.value])
        .filter(role => role);

    // Get existing roles for this user to preserve SUPERADMIN if it exists
    const existingRoles = document.querySelector(`button[data-user-id="${userId}"]`)
        .getAttribute('data-user-roles')
        .split(',')
        .map(role => role.trim());

    // If user had SUPERADMIN role, preserve it
    if (existingRoles.includes('ROLE_SUPERADMIN')) {
        selectedRoles.push('ROLE_SUPERADMIN');
    }

    const contextPath = $('meta[name="context-path"]').attr('content');
    const url = `${contextPath}/api/admin/user/saveRoles`;  // Modified URL

    // Create the request object in the format your backend expects
    const requestData = {
        userId: userId,
        roles: selectedRoles
    };

    showProgressBar("progressBarDiv", "bodyDiv");

    postData(url, JSON.stringify(requestData), 'json', function(response) {
        closeProgressBar("progressBarDiv", "bodyDiv");
        if (response.success) {
            showSuccessAlert("User roles updated successfully!");
            closeAndRefreshModal();
            loadUsers();
        } else {
            showErrorAlert(response.message || "Failed to update user roles");
        }
    });
}

function loadDashboardStats() {
    var contextPath = $('meta[name="context-path"]').attr('content');

    getData(contextPath + '/api/admin/dashboard/stats', 'json', function(stats) {
        document.getElementById('totalUsers').textContent = stats.totalUsers;
        document.getElementById('activeUsers').textContent = stats.activeUsers;
        document.getElementById('adminCount').textContent = stats.adminCount;
        document.getElementById('regularUsers').textContent = stats.regularUsers;
    });
}

function closeAndRefreshModal() {
    const modalElement = document.getElementById('editUserModal');
    modalElement.style.display = 'none';
    modalElement.classList.remove('show');
    document.body.classList.remove('modal-open');

    // Remove modal backdrop
    const backdrop = document.querySelector('.modal-backdrop');
    if (backdrop) {
        backdrop.remove();
    }

    loadUsers(); // Refresh the users list
}

// Initialize when document is ready
document.addEventListener('DOMContentLoaded', function() {
    initializeEventListeners();
    loadDashboardStats();
    loadUsers();
});

// Remove any existing event handlers when the page loads
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('editUserForm');
    if (form) {
        const checkboxes = form.querySelectorAll('input[type="checkbox"]');
        checkboxes.forEach(checkbox => {
            // Clone and replace the checkbox to remove any existing event listeners
            const newCheckbox = checkbox.cloneNode(true);
            checkbox.parentNode.replaceChild(newCheckbox, checkbox);
        });
    }
});
