<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- Security Check: Redirect to login if user is not logged in --%>
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
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/dashboard_modern.css">
</head>
<body>

<div class="page-wrapper">
    <%-- 
      Dynamically include the correct sidebar based on the user's role.
    --%>
    <c:choose>
        <c:when test="${sessionScope.user.role == 'ADMIN'}">
            <jsp:include page="sidebar.jsp">
                <jsp:param name="activePage" value="billing"/>
            </jsp:include>
        </c:when>
        <c:when test="${sessionScope.user.role == 'CASHIER'}">
            <jsp:include page="cashierSidebar.jsp"/>
        </c:when>
    </c:choose>

    <main class="main-content">
        <div class="container-fluid">
            <div class="header-bar">
                <h1 class="h2 mb-0">Create New Bill</h1>
                <p class="text-muted">Follow the steps below to generate a new invoice.</p>
            </div>
            
            <form action="createBill" method="post" id="billingForm">
                <div class="row">
                    <div class="col-lg-8">
                        <div class="card data-card mb-4">
                            <div class="card-header">Step 1: Select Customer</div>
                            <div class="card-body">
                                <select class="form-select" name="customerId" id="customerSelector" required>
                                    <option value="">-- Choose a Customer --</option>
                                    <c:forEach var="customer" items="${customerList}">
                                        <option value="${customer.customerId}">${customer.firstName} ${customer.lastName} (<c:out value="${customer.email}" />)</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="card data-card mb-4">
                            <div class="card-header">Step 2: Add Items to Bill</div>
                            <div class="card-body row align-items-end g-3">
                                <div class="col-md-6">
                                    <label for="itemSelector" class="form-label">Item</label>
                                    <select id="itemSelector" class="form-select">
                                        <option value="">-- Choose an Item --</option>
                                        <c:forEach var="item" items="${itemList}">
                                            <option value="${item.itemId}" data-price="${item.unitPrice}" data-name="${item.itemName}"><c:out value="${item.itemName}"/></option>
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
                    </div>

                    <div class="col-lg-4">
                        <div class="card data-card sticky-top" style="top: 2rem;">
                            <div class="card-header">Bill Summary</div>
                            <div class="card-body p-0">
                                <table class="table mb-0">
                                    <tbody id="bill-summary-table">
                                    </tbody>
                                    <tfoot>
                                        <tr class="total-row">
                                            <td colspan="2" class="text-end fs-5"><strong>Total:</strong></td>
                                            <td class="fs-5 text-end pe-3"><strong><span id="totalAmountDisplay">LKR 0.00</span></strong></td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="card data-card mt-4">
                    <div class="card-header">Step 3: Review Bill Items</div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-hover mb-0">
                                <thead>
                                    <tr>
                                        <th>Item Name</th>
                                        <th>Quantity</th>
                                        <th>Unit Price</th>
                                        <th>Subtotal</th>
                                        <th class="text-end">Action</th>
                                    </tr>
                                </thead>
                                <tbody id="bill-items-table">
                                </tbody>
                            </table>
                        </div>
                        <input type="hidden" name="totalAmount" id="totalAmountInput" value="0.00">
                    </div>
                    <div class="card-footer text-end">
                        <button type="submit" class="btn btn-success btn-lg gradient-button">Complete and Save Bill</button>
                    </div>
                </div>
            </form>
        </div>
    </main>
</div>

<%-- REWRITTEN & STABILIZED JAVASCRIPT --%>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const addItemBtn = document.getElementById('addItemBtn');
        const itemSelector = document.getElementById('itemSelector');
        const itemQuantityInput = document.getElementById('itemQuantity');
        const billItemsTableBody = document.getElementById('bill-items-table');
        const billSummaryTableBody = document.getElementById('bill-summary-table');
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
            const priceString = selectedOption.getAttribute('data-price') || '0';
            const cleanedPriceString = priceString.replace(/[^0-9.]/g, ''); 
            const itemPrice = parseFloat(cleanedPriceString);
            const quantityToAdd = parseInt(itemQuantityInput.value, 10);

            if (!itemName || isNaN(itemPrice) || itemPrice <= 0 || isNaN(quantityToAdd) || quantityToAdd < 1) {
                alert('Could not add item. Ensure it has a valid price and quantity.');
                return;
            }

            const existingRow = billItemsTableBody.querySelector(`tr[data-item-id='${itemId}']`);
            if (existingRow) {
                const quantityInput = existingRow.querySelector("input[name='quantities']");
                const currentQuantity = parseInt(quantityInput.value, 10);
                const newQuantity = currentQuantity + quantityToAdd;
                
                quantityInput.value = newQuantity;
                existingRow.cells[1].firstChild.nodeValue = newQuantity;
                
                const newSubtotal = itemPrice * newQuantity;
                existingRow.cells[3].textContent = `LKR ${newSubtotal.toFixed(2)}`;
            } else {
                const subtotal = itemPrice * quantityToAdd;
                const row = billItemsTableBody.insertRow();
                row.setAttribute('data-item-id', itemId);

                row.insertCell(0).innerHTML = `${itemName} <input type="hidden" name="itemIds" value="${itemId}"><input type="hidden" name="unitPrices" value="${itemPrice.toFixed(2)}">`;
                row.insertCell(1).innerHTML = `${quantityToAdd}<input type="hidden" name="quantities" value="${quantityToAdd}">`;
                row.insertCell(2).textContent = `LKR ${itemPrice.toFixed(2)}`;
                row.insertCell(3).textContent = `LKR ${subtotal.toFixed(2)}`;
                row.insertCell(4).innerHTML = `<button type="button" class="btn btn-sm btn-danger remove-item-btn">Remove</button>`;
                row.cells[4].classList.add('text-end');
            }
            
            updateBillSummaryAndTotal();
            itemSelector.selectedIndex = 0;
            itemQuantityInput.value = 1;
        });

        billItemsTableBody.addEventListener('click', function (e) {
            if (e.target && e.target.classList.contains('remove-item-btn')) {
                e.target.closest('tr').remove();
                updateBillSummaryAndTotal();
            }
        });

        function updateBillSummaryAndTotal() {
            let total = 0;
            const rows = billItemsTableBody.querySelectorAll('tr');
            billSummaryTableBody.innerHTML = '';

            if (rows.length === 0) {
                billSummaryTableBody.innerHTML = '<tr><td colspan="3" class="text-center text-muted p-3">No items added yet.</td></tr>';
            } else {
                rows.forEach(row => {
                    const itemName = row.cells[0].innerText.trim();
                    const quantity = row.cells[1].innerText.trim();
                    const subtotalText = row.cells[3].textContent.replace('LKR ', '').replace(/,/g, '');
                    const subtotal = parseFloat(subtotalText);

                    if (!isNaN(subtotal)) {
                        total += subtotal;
                        const summaryRow = billSummaryTableBody.insertRow();
                        summaryRow.insertCell(0).textContent = itemName;
                        summaryRow.insertCell(1).textContent = `x${quantity}`;
                        summaryRow.insertCell(2).textContent = `LKR ${subtotal.toFixed(2)}`;
                        summaryRow.cells[1].classList.add('text-center');
                        summaryRow.cells[2].classList.add('text-end', 'pe-3');
                    }
                });
            }

            totalAmountDisplay.textContent = `LKR ${total.toFixed(2)}`;
            totalAmountInput.value = total.toFixed(2);
        }

        billingForm.addEventListener('submit', function(event) {
            if (!customerSelector.value) {
                alert('Please select a customer before creating the bill.');
                event.preventDefault();
            } else if (billItemsTableBody.querySelectorAll('tr').length === 0) {
                alert('Please add at least one item to the bill.');
                event.preventDefault();
            }
        });

        updateBillSummaryAndTotal(); // Initial call to set placeholder text
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>