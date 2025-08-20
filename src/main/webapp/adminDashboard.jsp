<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- Security Check: Redirect to login if user is not logged in or not an ADMIN --%>
<%
    if (session.getAttribute("user") == null || !((com.pahana.edu.model.User)session.getAttribute("user")).getRole().equals("ADMIN")) {
        response.sendRedirect("login.jsp");
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Pahana Edu</title>
    
    <%-- Bootstrap CSS --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <%-- Bootstrap Icons --%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <%-- Google Fonts for a modern, clean look --%>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <%-- Link to the new, improved stylesheet --%>
    <link rel="stylesheet" href="css/dashboard_modern.css"> 
</head>
<body>

    <div class="page-wrapper">
        <%-- The sidebar is included here. The 'activePage' parameter highlights the correct link. [cite: 2] --%>
        <jsp:include page="sidebar.jsp">
            <jsp:param name="activePage" value="dashboard"/>
        </jsp:include>

        <main class="main-content">
            <div class="container-fluid">
                <div class="header-bar">
                    <h1 class="h2 mb-0">Welcome, <c:out value="${sessionScope.user.fullName}" />!</h1>
                    <p class="text-muted">Here's a snapshot of your system's activity today.</p>
                </div>

                <div class="row">
                    <%-- Total Users Card [cite: 13] --%>
                    <div class="col-lg-3 col-md-6 mb-4">
                        <div class="card stat-card gradient-1 shadow-sm">
                            <div class="card-body">
                                <div class="d-flex align-items-center">
                                    <div class="icon-bg">
                                        <i class="bi bi-people-fill"></i>
                                    </div>
                                    <div class="ms-3">
                                        <p class="mb-0 text-white-75">Total Users</p>
                                        <h4 class="my-0 font-weight-bold text-white">${userCount}</h4>
                                    </div>
                                </div>
                                <a href="manageUsers" class="stretched-link text-white-50 small-text">View Details <i class="bi bi-arrow-right-circle"></i></a>
                            </div>
                        </div>
                    </div>

                    <%-- Total Customers Card [cite: 17] --%>
                    <div class="col-lg-3 col-md-6 mb-4">
                        <div class="card stat-card gradient-2 shadow-sm">
                            <div class="card-body">
                                <div class="d-flex align-items-center">
                                    <div class="icon-bg">
                                        <i class="bi bi-person-lines-fill"></i>
                                    </div>
                                    <div class="ms-3">
                                        <p class="mb-0 text-white-75">Total Customers</p>
                                        <h4 class="my-0 font-weight-bold text-white">${customerCount}</h4>
                                    </div>
                                </div>
                                <a href="manageCustomers" class="stretched-link text-white-50 small-text">View Details <i class="bi bi-arrow-right-circle"></i></a>
                            </div>
                        </div>
                    </div>

                    <%-- Items in Stock Card [cite: 20] --%>
                    <div class="col-lg-3 col-md-6 mb-4">
                        <div class="card stat-card gradient-3 shadow-sm">
                            <div class="card-body">
                                <div class="d-flex align-items-center">
                                    <div class="icon-bg">
                                        <i class="bi bi-box-seam-fill"></i>
                                    </div>
                                    <div class="ms-3">
                                        <p class="mb-0 text-white-75">Items in Stock</p>
                                        <h4 class="my-0 font-weight-bold text-white">${itemsInStock}</h4>
                                    </div>
                                </div>
                                <a href="manageItems" class="stretched-link text-white-50 small-text">View Details <i class="bi bi-arrow-right-circle"></i></a>
                            </div>
                        </div>
                    </div>

                    <%-- Today's Sales Card [cite: 23] --%>
                    <div class="col-lg-3 col-md-6 mb-4">
                        <div class="card stat-card gradient-4 shadow-sm">
                            <div class="card-body">
                                <div class="d-flex align-items-center">
                                    <div class="icon-bg">
                                        <i class="bi bi-receipt"></i>
                                    </div>
                                    <div class="ms-3">
                                        <p class="mb-0 text-white-75">Today's Sales</p>
                                        <h4 class="my-0 font-weight-bold text-white"><fmt:formatNumber value="${todaySales}" type="currency" currencySymbol="LKR "/></h4>
                                    </div>
                                </div>
                                <a href="reports" class="stretched-link text-white-50 small-text">View Details <i class="bi bi-arrow-right-circle"></i></a>
                            </div>
                        </div>
                    </div>
                </div>
                
                <hr class="my-4">
                
                <div class="text-center">
                     <a href="reports" class="btn btn-lg btn-outline-light">View Full System Reports</a>
                </div>
            </div>
        </main>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>