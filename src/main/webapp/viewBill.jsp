<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>


<!DOCTYPE html>
<html>
<head>
    <title>View Bill #${bill.billId}</title>
    <link href="[https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css](https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css)" rel="stylesheet">
    <style>
        @media print {
            .no-print { display: none; }
        }
    </style>
</head>
<body class="bg-light">
    <div class="container mt-5" style="max-width: 800px;">
        <div class="card shadow-sm">
            <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
                <h2 class="mb-0">Invoice #${bill.billId}</h2>
                <h4 class="mb-0">Pahana Edu</h4>
            </div>
            <div class="card-body p-4">
                <p><strong>Customer:</strong> <c:out value="${bill.customerName}"/></p>
                <p><strong>Date:</strong> <fmt:formatDate value="${bill.billDate}" pattern="yyyy-MM-dd"/></p>
                <hr>
                <table class="table table-striped">
                    <thead>
                        <tr><th>Item</th><th>Quantity</th><th class="text-end">Unit Price</th><th class="text-end">Subtotal</th></tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${bill.items}">
                            <tr>
                                <td><c:out value="${item.itemName}"/></td>
                                <td><c:out value="${item.quantity}"/></td>
                                <td class="text-end"><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="LKR "/></td>
                                <td class="text-end"><fmt:formatNumber value="${item.unitPrice * item.quantity}" type="currency" currencySymbol="LKR "/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr class="fw-bold">
                            <td colspan="3" class="text-end fs-5">Total Amount</td>
                            <td class="text-end fs-5"><fmt:formatNumber value="${bill.totalAmount}" type="currency" currencySymbol="LKR "/></td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
        <div class="text-center mt-4 no-print">
            <button class="btn btn-primary btn-lg" onclick="window.print()">
                <i class="bi bi-printer-fill me-2"></i>Print Bill
            </button>
            <a href="adminDashboard" class="btn btn-secondary btn-lg">Back to Dashboard</a>
        </div>
    </div>
</body>
</html>
