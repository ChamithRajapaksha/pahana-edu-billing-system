<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%--
    Reusable Sidebar Component for the Cashier Dashboard.
    It has fewer links than the admin sidebar.
--%>

<div class="sidebar d-flex flex-column p-3">
    <a href="createBill" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
        <i class="bi bi-book-half fs-4 me-2"></i>
        <span class="sidebar-header">Pahana Edu</span>
    </a>
    <hr>
    <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item">
            <a href="createBill" class="nav-link active">
                <i class="bi bi-cart-plus-fill"></i>Create Bill
            </a>
        </li>
    </ul>
    <hr>
    <div class="dropdown">
        <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
            <i class="bi bi-person-circle fs-4 me-2"></i>
            <strong><c:out value="${sessionScope.user.fullName}" /></strong>
        </a>
        <ul class="dropdown-menu dropdown-menu-dark text-small shadow">
            <li><a class="dropdown-item" href="logout">Sign out</a></li>
        </ul>
    </div>
</div>