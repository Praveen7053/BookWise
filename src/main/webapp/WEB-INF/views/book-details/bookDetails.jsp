<!-- ================= BOOK DETAILS SECTION (initially hidden) ================ -->
<div id="bookDetailsSection" class="container-fluid overflow-auto" style="display:none; max-height:100vh;">
    <!-- Back to list -->
    <button onclick="hideBookDetails();" class="btn btn-link mb-3" id="backToGrid">
        <i class="fas fa-arrow-left me-1"></i> Back to list
    </button>

    <!-- All dynamic content goes in here -->
    <div id="bookDetailsContent">
        <!-- ========== BEGIN STATIC BOOK-DETAILS UI ========== -->
        <div class="row gy-4">

          <!-- ▸ Left column – cover + quick meta -->
          <div class="col-lg-4">
            <div class="card shadow-sm">
              <img id="bd-cover"
                   src="https://via.placeholder.com/400x600?text=Cover"
                   alt="Cover image"
                   class="card-img-top">

              <div class="card-body">
                <h4 id="bd-title"  class="mb-1">Book Title Placeholder</h4>
                <p  id="bd-author" class="text-muted mb-2">by Author Name</p>

                <!-- Average rating placeholder -->
                <div id="bd-avg-rating" class="text-warning fs-5 mb-3">
                  <i class="fas fa-star"></i><i class="fas fa-star"></i>
                  <i class="fas fa-star"></i><i class="fas fa-star-half-alt"></i>
                  <i class="far fa-star"></i>
                  <span class="text-muted fs-6">(3.5 / 5)</span>
                </div>

                <!-- Actions -->
                <button id="bd-read-btn" class="btn btn-primary w-100 mb-2">
                  <i class="fas fa-book-open me-1"></i> Read Now
                </button>
                <button class="btn btn-outline-secondary w-100">
                  <i class="fas fa-bookmark me-1"></i> Add to shelf
                </button>
              </div>
            </div>
          </div>

          <!-- ▸ Right column – description / rating / comments -->
          <div class="col-lg-8">

            <!-- Description -->
            <div class="card shadow-sm mb-4">
              <div class="card-body">
                <h5 class="card-title">About this book</h5>
                <p  id="bd-description" class="card-text">
                  Lorem ipsum dolor sit amet, consectetur adipisicing elit…
                </p>

                <ul class="list-unstyled small mb-0">
                  <li><strong>Pages:</strong>&nbsp;<span id="bd-pages">350</span></li>
                  <li><strong>Language:</strong>&nbsp;<span id="bd-language">English</span></li>
                  <li><strong>Uploaded:</strong>&nbsp;<span id="bd-uploaded">2025-06-01</span></li>
                  <li><strong>ISBN:</strong>&nbsp;<span id="bd-isbn">123-4-567-89012-3</span></li>
                </ul>
              </div>
            </div>

            <!-- Your rating -->
            <div class="card shadow-sm mb-4">
              <div class="card-body">
                <h5 class="card-title">Rate this book</h5>
                <div id="bd-user-rating" class="fs-4 text-warning">
                  <i class="far fa-star" data-val="1"></i>
                  <i class="far fa-star" data-val="2"></i>
                  <i class="far fa-star" data-val="3"></i>
                  <i class="far fa-star" data-val="4"></i>
                  <i class="far fa-star" data-val="5"></i>
                </div>
              </div>
            </div>

            <!-- Comments -->
            <div class="card shadow-sm">
              <div class="card-body">
                <h5 class="card-title">Comments</h5>

                <div id="bd-comments"
                     style="max-height:300px; overflow-y:auto;">
                  <!-- comment items injected dynamically -->
                  <div class="mb-3">
                    <strong>UserOne</strong><br>
                    <span class="small">Great read! Loved the pacing.</span>
                  </div>
                </div>

                <div class="input-group mt-3">
                  <input id="bd-new-comment" type="text"
                         class="form-control"
                         placeholder="Add a comment…">
                  <button class="btn btn-primary" id="bd-post-comment">Post</button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- ========== END BOOK-DETAILS UI ========== -->
    </div>
</div>


<script src="${pageContext.request.contextPath}/resources/script/bookDetails/bookDetails.js"></script>
