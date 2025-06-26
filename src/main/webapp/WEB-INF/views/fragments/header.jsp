<!-- fragments/header.jsp -->
<nav class="navbar navbar-expand-lg fixed-top navbar-light bg-white shadow-sm py-2">
  <div class="container-fluid">
    <a class="navbar-brand fw-bold text-primary" href="#">
      <i class="fas fa-book-reader me-2"></i>Book Wise
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarScroll" aria-controls="navbarScroll" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarScroll">
      <ul class="navbar-nav me-auto my-2 my-lg-0">
        <li class="nav-item">
          <a class="nav-link active" aria-current="page" href="#">Home</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">Browse</a>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarScrollingDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            More
          </a>
          <ul class="dropdown-menu" aria-labelledby="navbarScrollingDropdown">
            <li><a class="dropdown-item" href="#">Action</a></li>
            <li><a class="dropdown-item" href="#">Another action</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="#">Something else here</a></li>
          </ul>
        </li>
      </ul>

      <div class="d-flex align-items-center">
        <span class="me-3 text-muted">Welcome, <c:out value="${userName}" /></span>
        <form action="${pageContext.request.contextPath}/logout" method="post" class="d-inline">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          <button class="btn btn-outline-danger btn-sm" type="submit">Logout</button>
        </form>
      </div>
    </div>
  </div>
</nav>

<style>
  body {
    padding-top: 60px; /* to offset the fixed-top navbar */
  }
</style>
