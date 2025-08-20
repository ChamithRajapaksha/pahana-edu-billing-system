<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- Security Check --%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create New Bill - Pahana Edu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>
    <jsp:include page="sidebar.jsp">
        <jsp:param name="activePage" value="billing"/>
    </jsp:include>

    <main class="main-content">
        <div class="container-fluid">
            <h1 class="h3 mb-4">Create New Bill</h1>
            
            <form action="createBill" method="post" id="billingForm">
                <!-- Customer Selection -->
                <div class="card shadow mb-4">
                    <div class="card-header fw-bold">Step 1: Select Customer</div>
                    <div class="card-body">
                        <select class="form-select" name="customerId" id="customerSelector" required>
                            <option value="">-- Choose a Customer --</option>
                            <c:forEach var="customer" items="${customerList}">
                                <option value="${customer.customerId}">${customer.firstName} ${customer.lastName} (<c:out value="${customer.email}" />)</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <!-- Item Selection -->
                <div class="card shadow mb-4">
                    <div class="card-header fw-bold">Step 2: Add Items to Bill</div>
                    <div class="card-body row align-items-end g-3">
                        <div class="col-md-6">
                            <label for="itemSelector" class="form-label">Item</label>
                            <select id="itemSelector" class="form-select">
                                <option value="">-- Choose an Item --</option>
                                <c:forEach var="item" items="${itemList}">
                                    <option value="<c:out value="${item.itemId}"/>" data-price="<c:out value="${item.unitPrice}"/>" data-name="<c:out value="${item.itemName}"/>"><c:out value="${item.itemName}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label for="itemQuantity" class="form-label">Quantity</label>
                            <input type="number" id="itemQuantity" class="form-control" min="1" value="1">
                        </div>
                        <div class="col-md-3">
                            <button type="button" id="addItemBtn" class="btn btn-primary w-100">Add Item</button>
                        </div>
                    </div>
                </div>

                <!-- Bill Review -->
                <div class="card shadow">
                    <div class="card-header fw-bold">Step 3: Review Bill</div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-striped mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th>Item Name</th>
                                        <th>Quantity</th>
                                        <th>Unit Price (LKR)</th>
                                        <th>Subtotal (LKR)</th>
                                        <th class="text-end">Action</th>
                                    </tr>
                                </thead>
                                <tbody id="bill-items-table">
                                    <%-- Items will be added here by JavaScript --%>
                                </tbody>
                                <tfoot>
                                    <tr class="table-dark">
                                        <td colspan="3" class="text-end fs-5"><strong>Total Amount:</strong></td>
                                        <td class="fs-5"><strong>LKR <span id="totalAmountDisplay">0.00</span></strong></td>
                                        <td></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                        <input type="hidden" name="totalAmount" id="totalAmountInput" value="0.00">
                    </div>
                    <div class="card-footer text-end">
                        <button type="submit" class="btn btn-success btn-lg">Complete and Save Bill</button>
                    </div>
                </div>
            </form>
        </div>
    </main>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const addItemBtn = document.getElementById('addItemBtn');
        const itemSelector = document.getElementById('itemSelector');
        const itemQuantity = document.getElementById('itemQuantity');
        const billItemsTable = document.getElementById('bill-items-table');
        const totalAmountDisplay = document.getElementById('totalAmountDisplay');
        const totalAmountInput = document.getElementById('totalAmountInput');
        const billingForm = document.getElementById('billingForm');
        const customerSelector = document.getElementById('customerSelector');

        addItemBtn.addEventListener('click', function () {
            const selectedOption = itemSelector.options[itemSelector.selectedIndex];
            
            if (!selectedOption || !selectedOption.value) {
                alert('Please select an item.');
                return;
            }

            const itemId = selectedOption.value;
            const itemName = selectedOption.getAttribute('data-name');
            const itemPrice = parseFloat(selectedOption.getAttribute('data-price'));
            const quantity = parseInt(itemQuantity.value, 10);

            if (!itemName || isNaN(itemPrice) || itemPrice <= 0 || isNaN(quantity) || quantity < 1) {
                alert('The selected item has invalid data or quantity.');
                return;
            }

            const subtotal = itemPrice * quantity;

            // --- THE FIX: Building the row using robust DOM methods ---
            const row = billItemsTable.insertRow();
            
            row.insertCell(0).innerHTML = `${itemName} <input type="hidden" name="itemIds" value="${itemId}"> <input type="hidden" name="unitPrices" value="${itemPrice.toFixed(2)}">`;
            row.insertCell(1).innerHTML = `${quantity} <input type="hidden" name="quantities" value="${quantity}">`;
            row.insertCell(2).textContent = itemPrice.toFixed(2);
            row.insertCell(3).textContent = subtotal.toFixed(2);
            
            const actionCell = row.insertCell(4);
            actionCell.classList.add('text-end');
            actionCell.innerHTML = `<button type="button" class="btn btn-sm btn-danger remove-item-btn">Remove</button>`;
            
            updateTotal();
            
            itemSelector.selectedIndex = 0;
            itemQuantity.value = 1;
        });

        billItemsTable.addEventListener('click', function (e) {
            if (e.target && e.target.classList.contains('remove-item-btn')) {
                e.target.closest('tr').remove();
                updateTotal();
            }
        });

        function updateTotal() {
            let total = 0;
            const rows = billItemsTable.querySelectorAll('tr');
            rows.forEach(row => {
                const subtotalText = row.cells[3].textContent;
                total += parseFloat(subtotalText);
            });
            totalAmountDisplay.textContent = total.toFixed(2);
            totalAmountInput.value = total.toFixed(2);
        }

        billingForm.addEventListener('submit', function(event) {
            if (!customerSelector.value) {
                alert('Please select a customer before creating the bill.');
                event.preventDefault();
                return;
            }

            const itemCount = billItemsTable.querySelectorAll('tr').length;
            if (itemCount === 0) {
                alert('Please add at least one item to the bill.');
                event.preventDefault();
                return;
            }
        });
    });
</script>
</body>
</html>

