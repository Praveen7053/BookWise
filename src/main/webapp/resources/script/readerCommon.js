/**
 * Common Reader Header and Sidebar Functionality
 * This file handles the common header and sidebar behavior for all reader pages
 */

(function() {
    'use strict';

    // Initialize when DOM is ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initializeReaderCommon);
    } else {
        initializeReaderCommon();
    }

    function initializeReaderCommon() {
        initializeSidebarToggle();
        initializeSidebarUserDropdown();
        initializeResponsiveSidebar();
    }

    /**
     * Initialize sidebar toggle functionality
     */
    function initializeSidebarToggle() {
        const sidebar = document.getElementById('sidebar');
        const toggleBtn = document.getElementById('sidebarToggleBtn');
        
        if (!sidebar || !toggleBtn) {
            console.warn('Sidebar or toggle button not found');
            return;
        }

        function closeSidebar() {
            sidebar.classList.remove('show');
            sidebar.setAttribute('aria-hidden', 'true');
            toggleBtn.setAttribute('aria-expanded', 'false');
        }

        function openSidebar() {
            sidebar.classList.add('show');
            sidebar.setAttribute('aria-hidden', 'false');
            toggleBtn.setAttribute('aria-expanded', 'true');
        }

        // Toggle button click handler
        toggleBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            if (sidebar.classList.contains('show')) {
                closeSidebar();
            } else {
                openSidebar();
            }
        });

        // Close sidebar when clicking outside (on mobile)
        document.addEventListener('click', function(e) {
            if (window.innerWidth <= 991 && sidebar.classList.contains('show')) {
                if (!sidebar.contains(e.target) && e.target !== toggleBtn && !toggleBtn.contains(e.target)) {
                    closeSidebar();
                }
            }
        });

        // Close sidebar when a nav link or dropdown item is clicked (on mobile)
        const navLinks = sidebar.querySelectorAll('.nav-link, .dropdown-item');
        navLinks.forEach(link => {
            link.addEventListener('click', function() {
                if (window.innerWidth <= 991) {
                    closeSidebar();
                }
            });
        });

        // Accessibility: close sidebar on Escape key
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape' && sidebar.classList.contains('show')) {
                closeSidebar();
            }
        });

        // Ensure sidebar is visible on desktop resize
        window.addEventListener('resize', function() {
            if (window.innerWidth > 991) {
                sidebar.classList.remove('hide');
                sidebar.classList.add('show');
                sidebar.setAttribute('aria-hidden', 'false');
                toggleBtn.setAttribute('aria-expanded', 'true');
            } else {
                closeSidebar();
            }
        });

        // Initial state
        if (window.innerWidth > 991) {
            sidebar.classList.add('show');
            sidebar.setAttribute('aria-hidden', 'false');
            toggleBtn.setAttribute('aria-expanded', 'true');
        } else {
            closeSidebar();
        }
    }

    /**
     * Initialize sidebar user dropdown
     */
    function initializeSidebarUserDropdown() {
        const toggle = document.getElementById('sidebarUserDropdownToggle');
        const menu = document.getElementById('sidebarUserDropdownMenu');
        
        if (!toggle || !menu) {
            return;
        }

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

        // Close on Escape
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                menu.style.display = 'none';
            }
        });
    }

    /**
     * Initialize responsive sidebar behavior
     */
    function initializeResponsiveSidebar() {
        // Ensure toggle button is visible on mobile
        const toggleBtn = document.getElementById('sidebarToggleBtn');
        if (toggleBtn) {
            // The CSS will handle visibility, but we ensure it's always accessible
            toggleBtn.style.display = window.innerWidth <= 991 ? 'inline-block' : 'none';
            
            window.addEventListener('resize', function() {
                toggleBtn.style.display = window.innerWidth <= 991 ? 'inline-block' : 'none';
            });
        }
    }

    /**
     * Update header title dynamically
     */
    window.updateHeaderTitle = function(title) {
        const titleElement = document.getElementById('mainHeaderTitle');
        if (titleElement) {
            titleElement.textContent = title;
        }
    };

    /**
     * Update header right section with page-specific content
     */
    window.updateHeaderRight = function(htmlContent) {
        const rightSection = document.getElementById('mainHeaderRight');
        if (rightSection) {
            rightSection.innerHTML = htmlContent || '';
        }
    };

    /**
     * Toggle sidebar (exposed for external use)
     */
    window.toggleSidebar = function() {
        const sidebar = document.getElementById('sidebar');
        const toggleBtn = document.getElementById('sidebarToggleBtn');
        
        if (sidebar && toggleBtn) {
            toggleBtn.click();
        }
    };

})();
