<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>

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
    <c:choose>
        <c:when test="${sessionScope.user.role == 'ADMIN'}">
            <jsp:include page="sidebar.jsp"><jsp:param name="activePage" value="billing"/></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="cashierSidebar.jsp"/>
        </c:otherwise>
    </c:choose>

    <main class="main-content">
        <div class="container-fluid">
            <div class="header-bar">
                <h1 class="h2 mb-0">Create New Bill</h1>
                <p class="text-muted">Generate a new invoice for a customer.</p>
            </div>

            <div id="messageArea" class="mb-3"></div>

            <form id="billingForm">
                <div class="row">
                    <div class="col-lg-8">
                        <div class="card data-card mb-4">
                            <div class="card-header">Step 1: Select Customer</div>
                            <div class="card-body">
                                <select class="form-select" id="customerSelector" required>
                                    <option value="">-- Choose a Customer --</option>
                                    <c:forEach var="customer" items="${customerList}">
                                        <option value="${customer.customerId}">${customer.fullName} (${customer.customerId})</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="card data-card mb-4">
                            <div class="card-header">Step 2: Add Items to Bill</div>
                            <div class="card-body row align-items-end g-3">
                                <div class="col-md-5">
                                    <label for="itemSelector" class="form-label">Item</label>
                                    <select id="itemSelector" class="form-select">
                                        <option value="">-- Choose an Item --</option>
                                        <c:forEach var="item" items="${itemList}">
                                            <option value="${item.itemId}" data-price="${item.unitPrice}" data-name="${item.itemName}">${item.itemName} (${item.itemId})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <label for="itemQuantity" class="form-label">Quantity</label>
                                    <input type="number" id="itemQuantity" class="form-control" min="1" value="1">
                                </div>
                                 <div class="col-md-2">
                                    <label for="discountPct" class="form-label">Discount %</label>
                                    <input type="number" id="discountPct" class="form-control" min="0" max="100" value="0">
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
                                       <tr><td colspan="3" class="text-center text-muted p-3">No items added yet.</td></tr>
                                    </tbody>
                                    <tfoot>
                                        <tr class="total-row">
                                            <td colspan="2" class="text-end fs-5"><strong>Total:</strong></td>
                                            <td class="fs-5 text-end pe-3"><strong><span id="totalAmountDisplay">Rs. 0.00</span></strong></td>
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
                                <tbody id="bill-items-table"></tbody>
                            </table>
                        </div>
                    </div>
                    <div class="card-footer text-end">
                        <button type="button" id="saveBillBtn" class="btn btn-success btn-lg gradient-button">Complete and Save Bill</button>
                    </div>
                </div>
            </form>
        </div>
    </main>
</div>

<script>
document.addEventListener('DOMContentLoaded', function () {
    // --- ELEMENT SELECTORS ---
    const addItemBtn = document.getElementById('addItemBtn');
    const itemSelector = document.getElementById('itemSelector');
    const itemQuantityInput = document.getElementById('itemQuantity');
    const billItemsTableBody = document.getElementById('bill-items-table');
    const billSummaryTableBody = document.getElementById('bill-summary-table');
    const totalAmountDisplay = document.getElementById('totalAmountDisplay');
    const saveBillBtn = document.getElementById('saveBillBtn');
    const customerSelector = document.getElementById('customerSelector');
    const discountInput = document.getElementById('discountPct');
    const messageArea = document.getElementById('messageArea');

    // --- EVENT LISTENERS ---
    addItemBtn.addEventListener('click', handleAddItem);
    billItemsTableBody.addEventListener('click', handleRemoveItem);
    saveBillBtn.addEventListener('click', handleSaveBill);

    // --- FUNCTIONS ---
    function handleAddItem() {
        const selectedOption = itemSelector.options[itemSelector.selectedIndex];
        if (!selectedOption || !selectedOption.value) {
            showMessage('Please select an item.', 'warning');
            return;
        }

        const itemId = selectedOption.value;
        const itemName = selectedOption.getAttribute('data-name');
        const itemPrice = parseFloat(selectedOption.getAttribute('data-price'));
        const quantity = parseInt(itemQuantityInput.value, 10);

        if (isNaN(quantity) || quantity < 1) {
            showMessage('Please enter a valid quantity.', 'warning');
            return;
        }

        const existingRow = billItemsTableBody.querySelector(`tr[data-item-id='\${itemId}']`);
        if (existingRow) {
            const currentQty = parseInt(existingRow.dataset.quantity, 10);
            const newQty = currentQty + quantity;
            existingRow.dataset.quantity = newQty;
            existingRow.querySelector('.item-quantity').textContent = newQty;
        } else {
            const newRow = document.createElement('tr');
            newRow.dataset.itemId = itemId;
            newRow.dataset.quantity = quantity;
            newRow.dataset.price = itemPrice;
            newRow.dataset.name = itemName;
            newRow.innerHTML = `
                <td>\${itemName}</td>
                <td class="item-quantity">\${quantity}</td>
                <td>Rs. \${itemPrice.toFixed(2)}</td>
                <td class="subtotal">Rs. \${(itemPrice * quantity).toFixed(2)}</td>
                <td class="text-end"><button type="button" class="btn btn-sm btn-danger remove-item-btn">Remove</button></td>
            `;
            billItemsTableBody.appendChild(newRow);
        }
        updateBillSummary();
        itemSelector.value = '';
        itemQuantityInput.value = 1;
    }

    function handleRemoveItem(e) {
        if (e.target.classList.contains('remove-item-btn')) {
            e.target.closest('tr').remove();
            updateBillSummary();
        }
    }

    function updateBillSummary() {
        const rows = billItemsTableBody.querySelectorAll('tr');
        let total = 0;
        billSummaryTableBody.innerHTML = '';

        if (rows.length === 0) {
            billSummaryTableBody.innerHTML = '<tr><td colspan="3" class="text-center text-muted p-3">No items added yet.</td></tr>';
        } else {
            rows.forEach(row => {
                const price = parseFloat(row.dataset.price);
                const quantity = parseInt(row.dataset.quantity, 10);
                const subtotal = price * quantity;
                total += subtotal;
                row.querySelector('.subtotal').textContent = `Rs. \${subtotal.toFixed(2)}`;

                const summaryRow = `
                    <tr>
                        <td>\${row.dataset.name}</td>
                        <td class="text-center">x\${quantity}</td>
                        <td class="text-end pe-3">Rs. \${subtotal.toFixed(2)}</td>
                    </tr>
                `;
                billSummaryTableBody.innerHTML += summaryRow;
            });
        }
        totalAmountDisplay.textContent = `Rs. \${total.toFixed(2)}`;
    }

    function handleSaveBill() {
        const customerId = customerSelector.value;
        const discountPct = parseFloat(discountInput.value) || 0;
        const itemRows = billItemsTableBody.querySelectorAll('tr');

        if (!customerId) {
            showMessage('Please select a customer.', 'danger');
            return;
        }
        if (itemRows.length === 0) {
            showMessage('Please add at least one item to the bill.', 'danger');
            return;
        }

        const billLines = Array.from(itemRows).map(row => ({
            itemId: row.dataset.itemId,
            qty: parseInt(row.dataset.quantity, 10)
        }));

        const billApiRequest = { customerId, discountPct, lines: billLines };

        saveBillBtn.disabled = true;
        saveBillBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Saving...';

        fetch('createBill', {
            method: 'POST',
            headers: {'Content-Type': 'application/json', 'Accept': 'application/json'},
            body: JSON.stringify(billApiRequest)
        })
        .then(response => response.json().then(data => ({ ok: response.ok, data })))
        .then(({ ok, data }) => {
            if (!ok) {
                throw new Error(data.error || 'An unknown error occurred.');
            }
            showMessage(`Bill created successfully! New Bill ID: \${data.billId}`, 'success');
            setTimeout(() => window.location.href = 'dashboard.jsp', 2000);
        })
        .catch(error => {
            showMessage(`Error: \${error.message}`, 'danger');
        })
        .finally(() => {
            saveBillBtn.disabled = false;
            saveBillBtn.innerHTML = 'Complete and Save Bill';
        });
    }
    
    function showMessage(message, type = 'info') {
        messageArea.innerHTML = `
            <div class="alert alert-\${type} alert-dismissible fade show" role="alert">
                \${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        `;
    }
});
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>