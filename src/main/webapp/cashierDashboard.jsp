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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>
    <%-- Include the new, simplified sidebar for cashiers --%>
    <jsp:include page="cashierSidebar.jsp"/>

    <main class="main-content">
        <div class="container-fluid">
            <h1 class="h3 mb-4">Cashier Dashboard</h1>
            
            <div class="alert alert-info">
                Welcome back, <strong><c:out value="${sessionScope.user.fullName}" /></strong>!
            </div>
            
            <div class="card shadow">
                <div class="card-body text-center p-5">
                    <h5 class="card-title">Ready to start a new sale?</h5>
                    <p class="card-text">Click the button below to start creating a new bill for a customer.</p>
                    <a href="createBill" class="btn btn-primary btn-lg mt-3">
                        <i class="bi bi-cart-plus-fill me-2"></i>Create New Bill
                    </a>
                </div>
            </div>
        </div>
    </main>
</body>
</html>
