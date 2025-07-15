<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid" id="myBookshelfPage" style="display:none;">
    <div class="row">
        <div class="col-12">
            <!-- Header -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="mb-1">
                        <i class="fas fa-bookmark text-primary me-2"></i>My Bookshelf
                    </h2>
                    <p class="text-muted mb-0">Your personal collection of books</p>
                </div>
                <button onclick="hideMyBookshelf();" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left me-1"></i> Back
                </button>
            </div>

            <!-- Bookshelf Stats -->
            <div class="row mb-4">
                <div class="col-md-3">
                    <div class="card bg-primary text-white">
                        <div class="card-body">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <h4 class="mb-0" id="totalBooksCount">0</h4>
                                    <small>Total Books</small>
                                </div>
                                <div class="align-self-center">
                                    <i class="fas fa-books fa-2x"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-success text-white">
                        <div class="card-body">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <h4 class="mb-0" id="recentlyAddedCount">0</h4>
                                    <small>Recently Added</small>
                                </div>
                                <div class="align-self-center">
                                    <i class="fas fa-clock fa-2x"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-info text-white">
                        <div class="card-body">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <h4 class="mb-0" id="favoriteGenre">-</h4>
                                    <small>Top Genre</small>
                                </div>
                                <div class="align-self-center">
                                    <i class="fas fa-tags fa-2x"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-warning text-white">
                        <div class="card-body">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <h4 class="mb-0" id="readingProgress">0%</h4>
                                    <small>Reading Progress</small>
                                </div>
                                <div class="align-self-center">
                                    <i class="fas fa-chart-line fa-2x"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Search and Filter -->
            <div class="row mb-4">
                <div class="col-md-8">
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="fas fa-search"></i>
                        </span>
                        <input type="text" class="form-control" id="bookshelfSearch" 
                               placeholder="Search your bookshelf...">
                    </div>
                </div>
                <div class="col-md-4">
                    <select class="form-select" id="bookshelfSort">
                        <option value="recent">Recently Added</option>
                        <option value="title">Title A-Z</option>
                        <option value="author">Author A-Z</option>
                        <option value="genre">Genre</option>
                    </select>
                </div>
            </div>

            <!-- Books Grid -->
            <div class="row" id="bookshelfGrid" style="max-height: 60vh; overflow-y: auto; padding-right: 8px;">
                <!-- Books will be dynamically loaded here -->
                <div class="col-12 text-center py-5">
                    <div class="text-muted">
                        <i class="fas fa-book-open fa-3x mb-3"></i>
                        <h5>Your bookshelf is empty</h5>
                        <p>Start building your collection by adding books from the library!</p>
                        <button class="btn btn-primary" onclick="hideMyBookshelf();">
                            <i class="fas fa-plus me-1"></i> Browse Books
                        </button>
                    </div>
                </div>
            </div>

            <!-- Load More Button -->
            <div class="row mt-4" id="loadMoreSection" style="display:none;">
                <div class="col-12 text-center">
                    <button class="btn btn-outline-primary" id="loadMoreBooks">
                        <i class="fas fa-plus me-1"></i> Load More Books
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Book Details Modal -->
<div class="modal fade" id="bookshelfBookModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modalBookTitle">Book Title</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body" id="modalBookContent">
                <!-- Book details will be loaded here -->
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" id="modalReadNowBtn">
                    <i class="fas fa-book-open me-1"></i> Read Now
                </button>
                <button type="button" class="btn btn-danger" id="modalRemoveFromShelfBtn">
                    <i class="fas fa-trash me-1"></i> Remove from Shelf
                </button>
            </div>
        </div>
    </div>
</div>

 