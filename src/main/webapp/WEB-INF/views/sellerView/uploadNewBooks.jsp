<div class="container py-4 uploadBookContainer mx-auto" id="mainUploadNewBooksPageMainDIV" style="display:none;">
    <div class="row justify-content-center">
        <div class="col-12 col-lg-10">
            <div class="card shadow-sm border-0 rounded">
                <div class="card-header bg-primary text-white">
                    <h3 class="mb-0">Upload New Book</h3>
                </div>
                <div class="card-body">
                    <form id="uploadBookForm" >
                        <meta name="csrf-token" content="${_csrf.token}"/>
                        <input type="hidden" id="bookEncounterIdHidden" value="0">
                        <!-- Book Title and Author Name -->
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <input type="text" class="form-control" id="bookTitle" placeholder="Enter book title">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <input type="text" class="form-control" id="authorName" placeholder="Enter author name">
                                </div>
                            </div>
                        </div>

                        <!-- ISBN, Price, and Category (Side by Side) -->
                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <input type="text" class="form-control" id="isbnNumber" placeholder="Enter ISBN number">
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <input type="text" class="form-control" id="bookPrice" placeholder="Enter book price">
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <select class="form-select" id="bookCategory">
                                        <option value="" disabled selected>Select category</option>
                                        <option value="fiction">Fiction</option>
                                        <option value="nonfiction">Non-Fiction</option>
                                        <option value="fantasy">Fantasy</option>
                                        <option value="science">Science</option>
                                        <option value="biography">Biography</option>
                                        <option value="children">Children's</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <!-- Number of Pages, Publication Date, and Language (Side by Side) -->
                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <input type="text" class="form-control" id="numberOfPages" placeholder="Enter number of pages">
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <input type="date" class="form-control" id="publicationDate">
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <select class="form-select" id="bookLanguage">
                                        <option value="" disabled selected>Select language</option>
                                        <option value="english">English</option>
                                        <option value="spanish">Spanish</option>
                                        <option value="french">French</option>
                                        <option value="german">German</option>
                                        <option value="chinese">Chinese</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <!-- Book Cover and Book PDF (Side by Side) -->
                        <div class="row">
                            <!-- Book Cover Image -->
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <label for="bookCover" class="form-label">Book Cover Image</label>
                                    <input type="file" class="form-control" id="bookCover" accept="image/*">
                                    <img id="currentCoverImage" src="resources/images/defaultImage.png" alt="Current Cover Image" style="width: 100px; height: auto; display: block;">
                                </div>
                            </div>

                            <!-- Book PDF -->
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <label for="bookPdf" class="form-label">Upload Book PDF</label>
                                    <input type="file" class="form-control" id="bookPdf" accept="application/pdf">
                                    <a id="pdfLink" href="#" target="_blank" style="display: none;">View PDF</a>
                                    <canvas id="pdfCanvas" style="width: 100px; height: auto; display: block;"></canvas>
                                </div>
                            </div>
                        </div>

                        <!-- Image Modal -->
                        <div class="modal fade" id="imageModal" tabindex="-1" aria-labelledby="imageModalLabel" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content">
                                    <div class="modal-body">
                                        <img id="fullScreenImage" src="" alt="Full Screen Image" style="width: 100%; height: auto;">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- PDF Modal -->
                        <div class="modal fade" id="pdfModal" tabindex="-1" aria-labelledby="pdfModalLabel" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered modal-lg">
                                <div class="modal-content">
                                    <div class="modal-body">
                                        <canvas id="fullScreenPdfCanvas" style="width: 100%; height: auto;"></canvas>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Book Description -->
                        <div class="form-group mb-3">
                            <textarea class="form-control" id="bookDescription" rows="4" placeholder="Enter book description" ></textarea>
                        </div>

                        <!-- Upload Button -->
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary w-100">Upload Book</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="resources/script/uploadNewBooks.js"></script>