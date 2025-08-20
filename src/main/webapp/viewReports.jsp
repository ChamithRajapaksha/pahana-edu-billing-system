<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- Security Check: Ensure the user is a logged-in ADMIN --%>
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
    <title>View Reports - Pahana Edu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>
    <jsp:include page="sidebar.jsp">
        <jsp:param name="activePage" value="reports"/>
    </jsp:include>

    <main class="main-content">
        <div class="container-fluid">
            <h1 class="h3 mb-4">System Reports</h1>
            <div class="row">
                <!-- Recent Bills Card -->
                <div class="col-lg-8">
                    <div class="card shadow mb-4">
                        <div class="card-header fw-bold">
                            <i class="bi bi-clock-history me-2"></i>Recent Bills
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-striped table-hover">
                                    <thead>
                                        <tr>
                                            <th>Bill ID</th>
                                            <th>Date</th>
                                            <th>Customer</th>
                                            <th class="text-end">Amount</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="bill" items="${recentBills}">
                                            <tr>
                                                <td>#${bill.billId}</td>
                                                <td><fmt:formatDate value="${bill.billDate}" pattern="yyyy-MM-dd"/></td>
                                                <td><c:out value="${bill.customerName}"/></td>
                                                <td class="text-end"><fmt:formatNumber value="${bill.totalAmount}" type="currency" currencySymbol="LKR "/></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Top Selling Items Card -->
                <div class="col-lg-4">
                    <div class="card shadow mb-4">
                        <div class="card-header fw-bold">
                            <i class="bi bi-graph-up-arrow me-2"></i>Top Selling Items
                        </div>
                        <div class="card-body">
                            <ul class="list-group">
                                <c:forEach var="entry" items="${topSellingItems}">
                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                        <c:out value="${entry.key}"/>
                                        <span class="badge bg-primary rounded-pill"><c:out value="${entry.value}"/> sold</span>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</body>
</html>
