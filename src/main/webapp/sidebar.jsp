<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%--
    Reusable Sidebar Component for the Admin Dashboard.
    It uses a parameter 'activePage' to highlight the current page link.
--%>

<div class="sidebar d-flex flex-column p-3">
    <%-- Header with link to the main dashboard --%>
    <a href="adminDashboard" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
        <i class="bi bi-book-half fs-4 me-2"></i>
        <span class="sidebar-header">Pahana Edu</span>
    </a>
    <hr>

    <%-- Main Navigation Links --%>
    <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item">
            <a href="adminDashboard" class="nav-link ${param.activePage == 'dashboard' ? 'active' : ''}" aria-current="page">
                <i class="bi bi-speedometer2"></i>Dashboard
            </a>
        </li>
        <li>
            <a href="createBill" class="nav-link ${param.activePage == 'billing' ? 'active' : ''}">
                <i class="bi bi-cart-plus-fill"></i>Create Bill
            </a>
        </li>
        <li>
            <a href="manageCustomers" class="nav-link ${param.activePage == 'customers' ? 'active' : ''}">
                <i class="bi bi-person-lines-fill"></i>Manage Customers
            </a>
        </li>
        <li>
            <a href="manageItems" class="nav-link ${param.activePage == 'items' ? 'active' : ''}">
                <i class="bi bi-box-seam-fill"></i>Manage Items
            </a>
        </li>
        <li>
            <a href="manageUsers" class="nav-link ${param.activePage == 'users' ? 'active' : ''}">
                <i class="bi bi-people-fill"></i>Manage Users
            </a>
        </li>
         <li>
            <a href="reports" class="nav-link ${param.activePage == 'reports' ? 'active' : ''}">
                <i class="bi bi-receipt"></i>View Reports
            </a>
        </li>
    </ul>
    <hr>

    <%-- User Profile and Logout Dropdown --%>
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
