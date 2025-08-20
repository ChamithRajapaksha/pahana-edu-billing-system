<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>View Bill #${bill.billId}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/dashboard_modern.css">
    <style>
        body { background-color: #1a1a2e; font-family: 'Poppins', sans-serif; }
        .invoice-card {
            background-color: #2c2c54;
            color: #e0e0e0;
            border: none;
        }
        .invoice-header {
            background: linear-gradient(45deg, #4776E6 0%, #8E54E9 100%);
            color: white;
        }
        .invoice-card hr { border-color: rgba(255, 255, 255, 0.2); }
        .table { color: #e0e0e0; }
        .table-striped>tbody>tr:nth-of-type(odd)>* {
             background-color: rgba(0,0,0,0.1);
             color: #e0e0e0;
        }

        @media print {
            .no-print { display: none; }
            body { background-color: #ffffff; color: #000; }
            .invoice-card { box-shadow: none !important; color: #000; background-color: #ffffff;}
            .invoice-header { background: #f8f9fa !important; color: #000 !important; -webkit-print-color-adjust: exact; }
            .table, .table-striped>tbody>tr:nth-of-type(odd)>* { color: #000 !important; background-color: #ffffff !important; -webkit-print-color-adjust: exact;}
            .table thead { background-color: #e9ecef !important; -webkit-print-color-adjust: exact;}
            tfoot { background-color: #e9ecef !important; -webkit-print-color-adjust: exact;}
        }
    </style>
</head>
<body>
    <div class="container my-5" style="max-width: 800px;">
        <div class="card shadow-sm invoice-card">
            <div class="card-header invoice-header p-4 d-flex justify-content-between align-items-center">
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
            <button class="btn btn-lg gradient-button" onclick="window.print()">
                <i class="bi bi-printer-fill me-2"></i>Print Bill
            </button>
            <a href="adminDashboard" class="btn btn-secondary btn-lg">Back to Dashboard</a>
        </div>
    </div>
</body>
</html>