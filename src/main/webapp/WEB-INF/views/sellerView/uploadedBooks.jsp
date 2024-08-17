<%@ page contentType="text/html;charset=UTF-8" %>

<div class="container py-4" id="mainUploadListViewPageMainDIV" style="display:none;">
    <h2 class="mb-4">Uploaded Books</h2>

    <!-- Wrapper for Book List with Scrollable Content -->
    <div class="book-list-wrapper">
        <!-- Book List Header -->
        <div class="book-list-header">
            <div class="col-number">#</div>
            <div>Title</div>
            <div>Author</div>
            <div>ISBN</div>
            <div>Price</div>
            <div>Category</div>
            <div class="col-actions">Actions</div>
        </div>
        <!-- Book List Items with Fixed Height and Scrollable Content -->
        <div class="book-list-content" id="bookUploadedListMainDIV">
            <!-- Repeat for other items -->
            <!-- Use the same structure for additional items -->
        </div>
    </div>

    <!-- Pagination Controls -->
    <jsp:include page="/WEB-INF/views/fragments/pagination.jsp" />
</div>

<script src="resources/script/uploadedBooks.js"></script>
