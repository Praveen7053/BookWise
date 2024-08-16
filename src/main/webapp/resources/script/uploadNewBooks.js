
$(document).ready(function() {
    var contextPath = $('meta[name="context-path"]').attr('content');
    var csrfToken = $('meta[name="csrf-token"]').attr('content');

    // Define URL for the API endpoint
    var url = contextPath + '/api/book/actions/saveUpdateNewBooks';

    // Handle form submission
    $('#uploadBookForm').submit(function(event) {
        event.preventDefault();

        showProgressBar("progressBarDiv", "bodyDiv");
        postData(url, '', '', function(response) {
           if (response.success) {
               showSuccessAlert(response.message);
           } else {
               closeProgressBar("progressBarDiv", "bodyDiv");
               showErrorAlert(response.message);
           }
        });
    });
});



