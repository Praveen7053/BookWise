// WebContent/resources/js/common.js

// Function to display success alert messages
function showSuccessAlert(message) {
    const alertPlaceholder = document.getElementById('common-alert-placeholder-div');
    const wrapper = document.createElement('div');
    wrapper.innerHTML = [
        `<div class="alert alert-success alert-dismissible fade show" role="alert">`,
        `   <strong>Success!</strong> ${message}`,
        '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
        '</div>'
    ].join('');
    alertPlaceholder.append(wrapper);

    // Automatically close the alert after 2 seconds
    setTimeout(() => {
        wrapper.remove();
    }, 2000);
}

// Function to display error alert messages
function showErrorAlert(message) {
    const alertPlaceholder = document.getElementById('common-alert-placeholder-div');
    const wrapper = document.createElement('div');
    wrapper.innerHTML = [
        `<div class="alert alert-danger alert-dismissible fade show" role="alert">`,
        `   <strong>Error!</strong> ${message}`,
        '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
        '</div>'
    ].join('');
    alertPlaceholder.append(wrapper);

    // Automatically close the alert after 2 seconds
    setTimeout(() => {
        wrapper.remove();
    }, 2000);
}

// Function to clear alerts
function clearAlerts() {
    const alertPlaceholder = document.getElementById('common-alert-placeholder-div');
    alertPlaceholder.innerHTML = '';
}