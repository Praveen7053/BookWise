<%@ page contentType="text/html;charset=UTF-8" %>

<div class="container py-4" id="mainUploadListViewPageMainDIV" style="display:none;">
    <h2 class="mb-4">Uploaded Books</h2>

    <!-- Wrapper for Book List with Scrollable Content -->
    <div class="book-list-wrapper">
        <!-- Book List Header -->
        <div class="book-list-header">
            <div>#</div>
            <div>Title</div>
            <div>Author</div>
            <div>ISBN</div>
            <div>Price</div>
            <div>Category</div>
            <div>Actions</div>
        </div>
        <!-- Book List Items with Fixed Height and Scrollable Content -->
        <div class="book-list-content" id="bookUploadedListMainDIV">
            <div class="book-list-item" id="bookUploadedListInnerDIV_1">
                <div>1</div>
                <div>Example Book</div>
                <div>Author Name</div>
                <div>1234567890</div>
                <div>$19.99</div>
                <div>Fiction</div>
                <div>
                    <a href="#" class="btn btn-info btn-sm">View</a>
                    <a href="#" class="btn btn-warning btn-sm">Edit</a>
                    <a href="#" class="btn btn-danger btn-sm">Delete</a>
                </div>
            </div>

            <div class="book-list-item" id="bookUploadedListInnerDIV_2">
                <div>2</div>
                <div>Another Book</div>
                <div>Another Author</div>
                <div>0987654321</div>
                <div>$29.99</div>
                <div>Non-Fiction</div>
                <div>
                    <a href="#" class="btn btn-info btn-sm">View</a>
                    <a href="#" class="btn btn-warning btn-sm">Edit</a>
                    <a href="#" class="btn btn-danger btn-sm">Delete</a>
                </div>
            </div>

            <div class="book-list-item" id="bookUploadedListInnerDIV_2">
                <div>2</div>
                <div>Another Book</div>
                <div>Another Author</div>
                <div>0987654321</div>
                <div>$29.99</div>
                <div>Non-Fiction</div>
                <div>
                    <a href="#" class="btn btn-info btn-sm">View</a>
                    <a href="#" class="btn btn-warning btn-sm">Edit</a>
                    <a href="#" class="btn btn-danger btn-sm">Delete</a>
                </div>
            </div>

            <!-- More book-list-item divs can be added here dynamically -->
        </div>
    </div>

    <!-- Pagination Controls -->
    <jsp:include page="/WEB-INF/views/fragments/pagination.jsp" />
</div>
