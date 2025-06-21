<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="home-content" id="mainSellerHomeRightSectionPageMainDIV">
    <div class="container-fluid px-4 d-flex flex-column" style="height: 100vh; overflow: hidden;">
        <!-- Header Section -->
        <div class="row mb-4 flex-shrink-0">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center">
                    <h2 class="text-primary">My Books</h2>
                    <button class="btn btn-primary" onclick="changeModulePage('uploadBooks')">
                        <i class="fas fa-plus"></i> Add New Book
                    </button>
                </div>
            </div>
        </div>

        <!-- Filters Section (Always Visible) -->
        <div class="row mb-4 flex-shrink-0">
            <div class="col-12">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <div class="row g-3">
                            <div class="col-md-3">
                                <input type="text" class="form-control" onchange="searchUserBook();" id="searchTitle"
                                       placeholder="Search by title...">
                            </div>
                            <div class="col-md-2">
                                <input type="text" class="form-control" onchange="searchUserBook();" id="searchBookId"
                                       placeholder="Book ID">
                            </div>
                            <div class="col-md-3">
                                <select class="form-select" onchange="searchUserBook();" id="timeFilter">
                                    <option value="all">All Time</option>
                                    <option value="today">Today</option>
                                    <option value="week">This Week</option>
                                    <option value="month">This Month</option>
                                    <option value="year">This Year</option>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <select class="form-select" onchange="searchUserBook();" id="sortBy">
                                    <option value="newest">Newest First</option>
                                    <option value="oldest">Oldest First</option>
                                    <option value="title">Title A-Z</option>
                                    <option value="price">Price</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Scrollable Books Grid Area -->
        <div class="row flex-grow-1 overflow-auto" style="min-height: 0;">
            <div class="col-12">
                <div class="row" id="booksGrid" style="min-height: 400px;">
                    <!-- Books will be dynamically loaded here -->
                </div>
            </div>
        </div>

        <!-- Pagination (Always Visible at Bottom) -->
        <div class="row mt-3 flex-shrink-0">
            <div class="col-12">
                <nav id="paginationMainNav" aria-label="Page navigation">
                    <ul id="paginationList">
                        <li id="prevPageBtn">
                            <a id="prevPageLink" href="javascript:void(0);" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                        <li id="firstEllipsis" style="display: none;">
                            <span>...</span>
                        </li>
                        <div id="pageNumbersContainer">
                            <!-- Page numbers will be inserted here -->
                        </div>
                        <li id="lastEllipsis" style="display: none;">
                            <span>...</span>
                        </li>
                        <li id="nextPageBtn">
                            <a id="nextPageLink" href="javascript:void(0);" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</div>

<script src="resources/script/sellerHome/sellerHomeRightSection.js"></script>

<style>
/* Book card styles */
.book-card {
    transition: transform 0.2s, box-shadow 0.2s;
    height: 100%;
}

.book-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
}

.book-cover {
    height: 250px;
    object-fit: cover;
    border-radius: 4px 4px 0 0;
}

.book-status {
    position: absolute;
    top: 10px;
    right: 10px;
}

.book-actions {
    visibility: hidden;
    opacity: 0;
    transition: visibility 0s, opacity 0.2s;
    position: absolute;
    bottom: 10px;
    left: 0;
    right: 0;
    text-align: center;
}

.book-card:hover .book-actions {
    visibility: visible;
    opacity: 1;
}

/* Pagination Styles */
#paginationList {
    display: flex;
    justify-content: center;
    align-items: center;
    list-style: none;
    padding: 0;
    margin: 1rem 0; /* Slim vertical space */
    flex-wrap: wrap;
    font-size: 0.875rem;
}

#paginationList li {
    margin: 0 2px;
}

#paginationList li a,
#paginationList li span {
    padding: 0.25rem 0.5rem; /* Slimmer buttons */
    color: #007bff;
    background-color: #fff;
    border: 1px solid #dee2e6;
    text-decoration: none;
    border-radius: 4px;
    display: block;
    font-size: 0.9rem;
}

#paginationList li a span[aria-hidden="true"] {
    font-size: 1.1rem;
    line-height: 1;
}

#paginationList li.active a {
    background-color: #007bff;
    border-color: #007bff;
    color: white;
}

#paginationList li.disabled a {
    color: #6c757d;
    pointer-events: none;
    background-color: #fff;
    border-color: #dee2e6;
    opacity: 0.65;
}

#paginationList li:not(.disabled) a:hover {
    background-color: #e9ecef;
    border-color: #dee2e6;
    color: #0056b3;
}

#pageNumbersContainer {
    display: flex;
    margin: 0 5px;
}

#firstEllipsis,
#lastEllipsis {
    padding: 0.25rem 0.5rem;
    color: #6c757d;
}

/* Focus Styling */
.form-control:focus,
.form-select:focus {
    border-color: #80bdff;
    box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

.book-actions-top {
    position: absolute;
    top: 10px;
    left: 10px;
    display: flex;
    gap: 5px;
    opacity: 0;
    transition: opacity 0.2s ease-in-out;
    z-index: 2;
}

.book-card:hover .book-actions-top {
    opacity: 1;
}

</style>

