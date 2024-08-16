<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container py-4" id="mainUploadListViewPageMainDIV" style="display:none;">
    <h2 class="mb-4">Uploaded Books</h2>

    <!-- Book List Table -->
    <table class="table table-striped">
        <thead>
            <tr>
                <th>#</th>
                <th>Title</th>
                <th>Author</th>
                <th>ISBN</th>
                <th>Price</th>
                <th>Category</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody id="bookListBody">
            <!-- Book List Rows will be dynamically added here -->
            <!-- Example static row -->
            <tr>
                <td>1</td>
                <td>Example Book</td>
                <td>Author Name</td>
                <td>1234567890</td>
                <td>$19.99</td>
                <td>Fiction</td>
                <td>
                    <a href="#" class="btn btn-info btn-sm">View</a>
                    <a href="#" class="btn btn-warning btn-sm">Edit</a>
                    <a href="#" class="btn btn-danger btn-sm">Delete</a>
                </td>
            </tr>
        </tbody>
    </table>

    <!-- Pagination Controls -->
</div>
