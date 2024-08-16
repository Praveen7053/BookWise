<div class="container py-4 uploadBookContainer" id="mainUploadNewBooksPageMainDIV" style="display:none;">
    <div class="row justify-content-center">
        <div class="col-12 col-lg-10">
            <div class="card shadow-sm border-0 rounded">
                <div class="card-header bg-primary text-white">
                    <h3 class="mb-0">Upload New Book</h3>
                </div>
                <div class="card-body">
                    <form>
                        <!-- Book Title and Author Name -->
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <input type="text" class="form-control" id="bookTitle" placeholder="Enter book title" required>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <input type="text" class="form-control" id="authorName" placeholder="Enter author name" required>
                                </div>
                            </div>
                        </div>

                        <!-- ISBN, Price, and Category (Side by Side) -->
                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <input type="text" class="form-control" id="isbnNumber" placeholder="Enter ISBN number" required>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <input type="number" class="form-control" id="bookPrice" placeholder="Enter book price" required>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <select class="form-select" id="bookCategory" required>
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
                                    <input type="number" class="form-control" id="numberOfPages" placeholder="Enter number of pages" required>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <input type="date" class="form-control" id="publicationDate" required>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <select class="form-select" id="bookLanguage" required>
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
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <label for="bookCover" class="form-label">Book Cover Image</label>
                                    <input type="file" class="form-control" id="bookCover" accept="image/*" required>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <label for="bookPdf" class="form-label">Upload Book PDF</label>
                                    <input type="file" class="form-control" id="bookPdf" accept="application/pdf" required>
                                </div>
                            </div>
                        </div>


                        <!-- Book Description -->
                        <div class="form-group mb-3">
                            <textarea class="form-control" id="bookDescription" rows="4" placeholder="Enter book description" required></textarea>
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
