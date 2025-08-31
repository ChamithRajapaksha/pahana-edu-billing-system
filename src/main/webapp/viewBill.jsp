<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Bill ${bill.billId} - Pahana Edu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link href="css/dashboard_modern.css" rel="stylesheet">
    <style>
        .invoice-container { max-width: 800px; margin: auto; }
        @media print {
            body * { visibility: hidden; }
            .invoice-container, .invoice-container * { visibility: visible; }
            .invoice-container { position: absolute; left: 0; top: 0; width: 100%; }
            .no-print { display: none !important; }
        }
    </style>
</head>
<body>

<div class="page-wrapper">
    <c:choose>
        <c:when test="${sessionScope.user.role == 'ADMIN'}">
            <jsp:include page="sidebar.jsp" />
        </c:when>
        <c:otherwise>
            <jsp:include page="cashierSidebar.jsp" />
        </c:otherwise>
    </c:choose>

    <main class="main-content">
        <div class="container-fluid">
  
            <div class="d-flex justify-content-between align-items-center mb-4 no-print">
                <h1 class="h2">Bill Details</h1>
                <div class="d-flex gap-2">
                    <button onclick="window.print();" class="btn btn-primary gradient-button">
                        <i class="bi bi-printer-fill"></i> Print Bill
                    </button>
                    <form action="sendReceipt" method="post" class="mb-0">
                        <input type="hidden" name="billId" value="${bill.billId}">
                        <button type="submit" class="btn btn-info">
                            <i class="bi bi-envelope-fill"></i> Send Email
                        </button>
                    </form>
                    <a href="createBill" class="btn btn-secondary">Create New Bill</a>
                </div>
            </div>

            <c:if test="${not empty sessionScope.flashMessage}">
                <div class="alert alert-${sessionScope.flashMessageType} alert-dismissible fade show no-print" role="alert">
                    <c:out value="${sessionScope.flashMessage}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <% session.removeAttribute("flashMessage"); %>
                <% session.removeAttribute("flashMessageType"); %>
            </c:if>

            <c:choose>
                <c:when test="${not empty bill}">
                    <div class="card data-card invoice-container">
                        <div class="card-header text-center">
                            <h2>Invoice / Bill</h2>
                            <p class="text-muted">Pahana Edu</p>
                        </div>
                        <div class="card-body p-4">
                            <div class="row mb-4 invoice-details">
                                <div class="col-6">
                                    <strong>Billed To:</strong>
                                    <span class="invoice-value">${bill.customerName}</span>
                                </div>
                                <div class="col-sm-6 text-sm-end">
                                    <strong>Bill #:</strong>
                                    <span class="invoice-value invoice-id">${bill.billId}</span>
                                    <strong>Date:</strong>
                                    <span class="invoice-value"><fmt:formatDate value="${bill.billDate}" pattern="yyyy-MM-dd" /></span>
                                </div>
                            </div>

                            <table class="table table-bordered">
                                <thead class="table-light">
                                    <tr>
                                        <th>Item Name</th>
                                        <th class="text-center">Quantity</th>
                                        <th class="text-end">Unit Price</th>
                                        <th class="text-end">Subtotal</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${bill.items}">
                                        <tr>
                                            <td>${item.itemName}</td>
                                            <td class="text-center">${item.quantity}</td>
                                            <td class="text-end">Rs. <fmt:formatNumber value="${item.unitPrice}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                            <td class="text-end">Rs. <fmt:formatNumber value="${item.unitPrice * item.quantity}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                                <tfoot>
                                    <tr class="fs-5">
                                        <td colspan="3" class="text-end"><strong>Total:</strong></td>
                                        <td class="text-end"><strong>Rs. <fmt:formatNumber value="${bill.totalAmount}" type="number" minFractionDigits="2" maxFractionDigits="2"/></strong></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-danger">Bill not found or an error occurred.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>