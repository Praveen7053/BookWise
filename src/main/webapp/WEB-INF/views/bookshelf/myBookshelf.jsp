<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <title>My Bookshelf - BookWise</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources/css/myBookShelf.css"/>
</head>

<!-- ===============================
     MY BOOKSHELF PAGE
================================ -->
<div class="container-fluid myBookShelf_page"
     id="myBookshelfPage"
     style="display:none;">

    <!-- ===============================
         CONTENT
    ================================ -->
    <div class="px-3 px-md-4 py-4">

        <!-- ===== Stats Cards ===== -->
        <div class="row mb-4 myBookShelf_stats">
            <div class="col-md-3 col-sm-6 myBookShelf_statCol">
                <div class="card myBookShelf_statCard bg-primary text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h4 id="totalBooksCount">0</h4>
                                <small>Total Books</small>
                            </div>
                            <i class="fas fa-books"></i>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-3 col-sm-6 myBookShelf_statCol">
                <div class="card myBookShelf_statCard bg-success text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h4 id="recentlyAddedCount">0</h4>
                                <small>Recently Added</small>
                            </div>
                            <i class="fas fa-clock"></i>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-3 col-sm-6 myBookShelf_statCol">
                <div class="card myBookShelf_statCard bg-info text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h4 id="favoriteGenre">-</h4>
                                <small>Top Genre</small>
                            </div>
                            <i class="fas fa-tags"></i>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-3 col-sm-6 myBookShelf_statCol">
                <div class="card myBookShelf_statCard bg-warning text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h4 id="readingProgress">0%</h4>
                                <small>Reading Progress</small>
                            </div>
                            <i class="fas fa-chart-line"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- ===== Search & Sort ===== -->
        <div class="row mb-4 myBookShelf_filters">
            <div class="col-md-8 mb-2 mb-md-0">
                <div class="input-group">
                    <span class="input-group-text">
                        <i class="fas fa-search"></i>
                    </span>
                    <input type="text"
                           class="form-control"
                           id="bookshelfSearch"
                           placeholder="Search your bookshelf...">
                </div>
            </div>

            <div class="col-md-4">
                <select class="form-select" id="bookshelfSort">
                    <option value="recent">Recently Added</option>
                    <option value="title">Title A–Z</option>
                    <option value="author">Author A–Z</option>
                    <option value="genre">Genre</option>
                </select>
            </div>
        </div>

        <!-- ===== Books Grid ===== -->
        <div class="row myBookShelf_grid" id="bookshelfGrid">
            <div class="col-12 text-center py-5">
                <div class="text-muted">
                    <i class="fas fa-book-open fa-3x mb-3"></i>
                    <h5>No books found</h5>
                    <p>Start building your collection from the library</p>
                    <button class="btn btn-primary"
                            onclick="hideMyBookshelf();">
                        <i class="fas fa-plus me-1"></i>
                        Browse Books
                    </button>
                </div>
            </div>
        </div>

        <!-- ===== Load More ===== -->
        <div class="row mt-4"
             id="loadMoreSection"
             style="display:none;">
            <div class="col-12 text-center">
                <button class="btn btn-outline-primary" id="loadMoreBooks">
                    <i class="fas fa-plus me-1"></i>
                    Load More Books
                </button>
            </div>
        </div>

    </div>
</div>

<!-- ===============================
     BOOK DETAILS MODAL
================================ -->
<div class="modal fade" id="bookshelfBookModal" tabindex="-1">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modalBookTitle"></h5>
                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body" id="modalBookContent"></div>
            <div class="modal-footer">
                <button class="btn btn-secondary"
                        data-bs-dismiss="modal">Close</button>
                <button class="btn btn-primary" id="modalReadNowBtn">
                    <i class="fas fa-book-open me-1"></i>
                    Read Now
                </button>
                <button class="btn btn-danger" id="modalRemoveFromShelfBtn">
                    <i class="fas fa-trash me-1"></i>
                    Remove
                </button>
            </div>
        </div>
    </div>
</div>
