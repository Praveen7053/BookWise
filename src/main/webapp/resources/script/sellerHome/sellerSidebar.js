
$(document).ready(function() {
    try {
        const userName = document.getElementById('userNameHidden').value;
        const userEmail = document.getElementById('userEmailHidden').value;

        // Update the username in the sidebar dropdown if it exists
        const userNameSpan = document.getElementById('sideBarLoginUserName');
        if (userNameSpan) {
            userNameSpan.innerHTML = userName;
        }

        loadSidebarProfileImage();
    } catch (error) {
        console.error('Error accessing user information:', error);
    }
});

document.getElementById('userProfileDropdownToggle').addEventListener('click', function () {
    const menu = document.getElementById('dropdownMenu');
    const isExpanded = this.getAttribute('aria-expanded') === 'true';

    // Toggle aria-expanded
    this.setAttribute('aria-expanded', !isExpanded);

    // Toggle menu visibility
    if (isExpanded) {
        menu.classList.remove('show');
    } else {
        menu.classList.add('show');
    }
});

document.addEventListener('DOMContentLoaded', function() {
    const dropdownToggle = document.getElementById('userProfileDropdownToggle');
    const dropdownMenu = document.getElementById('dropdownMenu');

    document.addEventListener('click', function(event) {
        if (!dropdownToggle.contains(event.target)) {
            dropdownMenu.style.display = 'none';
        }
    });

    dropdownToggle.addEventListener('click', function(event) {
        event.stopPropagation();
        dropdownMenu.style.display = dropdownMenu.style.display === 'none' ? 'block' : 'none';
    });
});