<div class="container-fluid vh-100 d-flex flex-column justify-content-center align-items-center" id="mainUploadNewBooksPageMainDIV" style="display: none;">
    <div class="card shadow border-0 rounded-4 w-100 mx-3" style="max-width: 1280px; height: 95vh; overflow: hidden;">
        <div class="card-header bg-primary text-white rounded-top-4">
            <h3 class="mb-0">Upload New Book</h3>
        </div>
        <div class="card-body p-3 overflow-auto">
            <form id="uploadBookForm" class="h-100">
                <meta name="csrf-token" content="${_csrf.token}" />
                <input type="hidden" id="bookEncounterIdHidden" value="0">

                <div class="row g-3 mb-2">
                    <div class="col-md-6">
                        <input type="text" class="form-control" id="bookTitle" placeholder="Enter book title">
                    </div>
                    <div class="col-md-6">
                        <input type="text" class="form-control" id="authorName" placeholder="Enter author name">
                    </div>
                </div>

                <div class="row g-3 mb-2">
                    <div class="col-md-4">
                        <input type="text" class="form-control" id="isbnNumber" placeholder="Enter ISBN number">
                    </div>
                    <div class="col-md-4">
                        <input type="text" class="form-control" id="bookPrice" placeholder="Enter book price">
                    </div>
                    <div class="col-md-4">
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

                <div class="row g-3 mb-2">
                    <div class="col-md-4">
                        <input type="text" class="form-control" id="numberOfPages" placeholder="Enter number of pages">
                    </div>
                    <div class="col-md-4">
                        <input type="date" class="form-control" id="publicationDate">
                    </div>
                    <div class="col-md-4">
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

                <div class="row g-3 mb-2 align-items-start">
                    <div class="col-md-6">
                        <label for="bookCover" class="form-label">Book Cover Image</label>
                        <input type="file" class="form-control" id="bookCover" accept="image/*">
                        <div class="mt-1">
                            <img id="currentCoverImage" src="resources/images/defaultImage.png"
                                 alt="Cover Preview"
                                 class="img-thumbnail"
                                 style="max-width: 120px; cursor: pointer;">
                            <small class="d-block text-muted" id="bookCoverFileName"></small>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <label for="bookPdf" class="form-label">Upload Book PDF</label>
                        <input type="file" class="form-control" id="bookPdf" accept="application/pdf">
                        <div class="mt-1 border rounded p-1" style="max-height: 120px; overflow: hidden;">
                            <canvas id="pdfCanvas" style="width: 100%; height: auto;"></canvas>
                        </div>
                        <small class="d-block text-muted" id="bookPdfFileName"></small>
                    </div>
                </div>

                <!-- Modals (unchanged) -->
                <div class="modal fade" id="imageModal" tabindex="-1" aria-labelledby="imageModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-body">
                                <img id="fullScreenImage" src="" alt="Full Screen Image" style="width: 100%; height: auto;">
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="pdfModal" tabindex="-1" aria-labelledby="pdfModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered modal-lg">
                        <div class="modal-content">
                            <div class="modal-body">
                                <canvas id="fullScreenPdfCanvas" style="width: 100%; height: auto;"></canvas>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="mb-2">
                    <textarea class="form-control" id="bookDescription" rows="2" placeholder="Enter book description"></textarea>
                </div>

                <div class="d-grid">
                    <button type="submit" class="btn btn-primary btn-lg rounded-3">Upload Book</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="resources/script/sellerHome/uploadNewBooks.js"></script>

<style>
    #currentCoverImage:hover {
        opacity: 0.8;
    }

    #pdfCanvas, #fullScreenPdfCanvas {
        max-width: 100%;
        height: auto;
        display: block;
    }

    /* Optional: hide scrollbars visually but keep scroll when needed */
    .card-body::-webkit-scrollbar {
        width: 0px;
        background: transparent;
    }
</style>
