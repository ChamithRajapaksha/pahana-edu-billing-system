<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- 
    Security Check: 
    Redirects to the login page if the user is not logged in, 
    or if the logged-in user is not a CASHIER.
--%>
<%
    if (session.getAttribute("user") == null || !((com.pahana.edu.model.User)session.getAttribute("user")).getRole().equals("CASHIER")) {
        response.sendRedirect("login.jsp");
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cashier Dashboard - Pahana Edu</title>
    
    <%-- Bootstrap CSS --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <%-- Bootstrap Icons --%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <%-- Google Fonts --%>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <%-- Link to the modern stylesheet --%>
    <link rel="stylesheet" href="css/dashboard_modern.css">
</head>
<body>

<div class="page-wrapper">
    <%-- Include the updated cashier sidebar --%>
    <jsp:include page="cashierSidebar.jsp"/>

    <main class="main-content">
        <div class="container-fluid">
            
            <div class="header-bar">
                <h1 class="h2 mb-0">Cashier Dashboard</h1>
                <p class="text-muted">Welcome back, <strong><c:out value="${sessionScope.user.fullName}" /></strong>!</p>
            </div>
            
            <div class="card data-card shadow-lg">
                <div class="card-body text-center p-5">
                    <i class="bi bi-receipt-cutoff" style="font-size: 4rem; color: #8E54E9;"></i>
                    <h4 class="card-title mt-4">Ready to start a new sale?</h4>
                    <p class="text-muted mb-4">Click the button below to start creating a new bill for a customer.</p>
                    <a href="createBill" class="btn btn-primary btn-lg gradient-button">
                        <i class="bi bi-cart-plus-fill me-2"></i>Create New Bill
                    </a>
                </div>
            </div>
            
        </div>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>