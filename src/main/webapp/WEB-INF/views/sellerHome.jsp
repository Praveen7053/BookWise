<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Seller Page</title>
    <%@include file="./base.jsp" %>
    <%@include file="/WEB-INF/views/fragments/header.jsp" %>
</head>
<body>
<main role="main" class="container-fluid" style="">
    <div class="row flex-nowrap" style="height: 100%;">
        <!-- Left Column (60%) -->
        <div class="col-md-8 col-lg-7 d-flex flex-column">
            <div class="my-3 p-3 bg-white rounded box-shadow flex-grow-1">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h6 class="border-bottom border-gray pb-2 mb-0" style="margin-top:50px;">Add Book</h6>
                </div>

                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Add New Book</h5>
                        <div class="form-group">
                            <input type="text" class="form-control" id="bookName" placeholder="Enter book name">
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" id="authorName" placeholder="Enter author name">
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" id="language" placeholder="Enter language">
                        </div>
                        <div class="form-group">
                            <input type="file" class="form-control-file" id="pdfFile" accept=".pdf">
                        </div>
                        <button type="submit" class="btn btn-primary" onclick="saveSellerBook();">Submit</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Right Column (30%) -->
        <div class="col-md-4 col-lg-5 d-flex flex-column">
            <div class="my-3 p-3 bg-white rounded box-shadow flex-grow-1 d-flex flex-column" style="height: 150px;">
                <h6 class="border-bottom border-gray pb-2 mb-0" style="margin-top:50px;">Uploaded Books</h6>

                <div id="mainListDivContainer" style="scroll-behavior: auto;">
                    <!-- Add multiple media divs here -->
                    <div class="media text-muted pt-3" style="padding-right:15px;">
                        <div class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">
                            <div class="d-flex justify-content-between align-items-center w-100">
                                <strong class="text-gray-dark">Full Name</strong>
                                <a href="#" class="text-danger"><i class="fas fa-trash-alt"></i></a>
                            </div>
                            <span class="d-block"></span>
                        </div>
                    </div>

                    <div class="media text-muted pt-3" style="padding-right:15px;">
                        <div class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">
                            <div class="d-flex justify-content-between align-items-center w-100">
                                <strong class="text-gray-dark">Full Name</strong>
                                <a href="#" class="text-danger"><i class="fas fa-trash-alt"></i></a>
                            </div>
                            <span class="d-block"></span>
                        </div>
                    </div>

                    <div class="media text-muted pt-3" style="padding-right:15px;">
                        <div class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">
                            <div class="d-flex justify-content-between align-items-center w-100">
                                <strong class="text-gray-dark">Full Name</strong>
                                <a href="#" class="text-danger"><i class="fas fa-trash-alt"></i></a>
                            </div>
                            <span class="d-block"></span>
                        </div>
                    </div>

                    <div class="media text-muted pt-3" style="padding-right:15px;">
                        <div class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">
                            <div class="d-flex justify-content-between align-items-center w-100">
                                <strong class="text-gray-dark">Full Name</strong>
                                <a href="#" class="text-danger"><i class="fas fa-trash-alt"></i></a>
                            </div>
                            <span class="d-block"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <!-- 30% closed -->
    </div>
</main>
</body>
</html>

<script src="resources\script\sellerHome.js"></script>
