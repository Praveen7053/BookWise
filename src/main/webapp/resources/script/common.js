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

function showProgressBar(progressBarId, bodyDivId) {
    var progressBarDiv = document.getElementById(progressBarId);
    var bodyDiv = document.getElementById(bodyDivId);

    if (progressBarDiv) {
        progressBarDiv.style.display = "block";
    }

    if (bodyDiv) {
        bodyDiv.style.pointerEvents = "none"; // Disable interaction with the background
        bodyDiv.style.opacity = "0.5"; // Optional: dim the background
    }
}

function closeProgressBar(progressBarId, bodyDivId) {
    var progressBarDiv = document.getElementById(progressBarId);
    var bodyDiv = document.getElementById(bodyDivId);

    if (progressBarDiv) {
        progressBarDiv.style.display = "none";
    }

    if (bodyDiv) {
        bodyDiv.style.pointerEvents = "auto"; // Re-enable interaction with the background
        bodyDiv.style.opacity = "1"; // Restore background opacity
    }
}

function jConfirm(message, onConfirm, onCancel) {
    var modal = document.getElementById('jConfirmModal');
    var messageElement = document.getElementById('jConfirmMessage');
    var confirmButton = document.querySelector('.jConfirm-confirm');
    var cancelButton = document.querySelector('.jConfirm-cancel');

    // Set the message and display the modal
    messageElement.textContent = message;
    modal.style.display = 'flex';

    // Handle the 'Yes' button click
    confirmButton.onclick = function() {
        modal.style.display = 'none';
        if (typeof onConfirm === 'function') onConfirm();
    };

    // Handle the 'No' button click
    cancelButton.onclick = function() {
        modal.style.display = 'none';
        if (typeof onCancel === 'function') onCancel();
    };

    // Optionally handle clicks outside the modal to close it
    window.onclick = function(event) {
        if (event.target === modal) {
            modal.style.display = 'none';
            if (typeof onCancel === 'function') onCancel();
        }
    };
}

