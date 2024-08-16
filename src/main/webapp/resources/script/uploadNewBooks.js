
$(document).ready(function() {
    var contextPath = $('meta[name="context-path"]').attr('content');
    var csrfToken = $('meta[name="csrf-token"]').attr('content');

    // Define URL for the API endpoint
    var url = contextPath + '/api/book/actions/saveUpdateNewBooks';

    // Handle form submission
    $('#uploadBookForm').submit(function(event) {
        event.preventDefault();

        // Create JSON object
        var jsonData = {
            bookTitle: $('#bookTitle').val(),
            authorName: $('#authorName').val(),
            isbnNumber: $('#isbnNumber').val(),
            bookPrice: $('#bookPrice').val(),
            bookCategory: $('#bookCategory').val(),
            numberOfPages: $('#numberOfPages').val(),
            publicationDate: $('#publicationDate').val(),
            bookLanguage: $('#bookLanguage').val(),
            bookDescription: $('#bookDescription').val()
        };

        // Show progress bar
        showProgressBar("progressBarDiv", "bodyDiv");

        // Send POST request
        postData(url, JSON.stringify(jsonData), 'json', function(response) {
           if (response.success) {
               showSuccessAlert(response.message);
           } else {
               closeProgressBar("progressBarDiv", "bodyDiv");
               showErrorAlert(response.message);
           }
        });
    });
});



