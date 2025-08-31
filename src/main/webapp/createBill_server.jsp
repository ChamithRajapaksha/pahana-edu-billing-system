<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Bill - Pahana Edu</title>
    
    <%-- Stylesheets --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/dashboard_modern.css"> 
</head>
<body>

    <div class="page-wrapper">
        <jsp:include page="sidebar.jsp">
            <jsp:param name="activePage" value="billing"/>
        </jsp:include>

        <main class="main-content">
            <div class="container-fluid">
                
                <div class="header-bar">
                    <h1 class="h2 mb-0">Create New Bill</h1>
                    <p class="text-muted">Select a customer and add items to generate a new bill.</p>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>

                <%-- Main Form that will be submitted --%>
                <form method="post" action="<c:url value='/bill'/>" id="bill-form">
                    
                    <%-- Step 1: Customer Selection Card --%>
                    <div class="card data-card mb-4">
                        <div class="card-header">
                            Step 1: Select Customer
                        </div>
                        <div class="card-body">
                            <label for="customerId" class="form-label">Customer</label>
                            <select name="customerId" id="customerId" class="form-select" required>
                                <option value="" disabled selected>-- Choose a customer --</option>
                                <c:forEach var="c" items="${customerList}">
                                  <option value="${c.customerId}">${c.fullName} (${c.customerId})</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <%-- Step 2: Add Items Card --%>
                    <div class="card data-card mb-4">
                         <div class="card-header">
                            Step 2: Add Items to Bill
                        </div>
                        <div class="card-body">
                            <div class="row align-items-end">
                                <div class="col-md-6 mb-3 mb-md-0">
                                    <label for="item-select" class="form-label">Item</label>
                                    <select id="item-select" class="form-select">
                                        <option value="">-- Choose item --</option>
                                        <c:forEach var="it" items="${itemList}">
                                          <option value="${it.itemId}" data-price="${it.unitPrice}" data-name="${it.itemName}">${it.itemName} (LKR ${it.unitPrice})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-3 mb-3 mb-md-0">
                                    <label for="item-quantity" class="form-label">Quantity</label>
                                    <input id="item-quantity" type="number" min="1" value="1" class="form-control"/>
                                </div>
                                <div class="col-md-3">
                                    <button type="button" id="add-item-btn" class="btn btn-primary w-100">Add Item</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <%-- Step 3: Review Bill Items Card --%>
                    <div class="card data-card">
                        <div class="card-header">
                            Step 3: Review Bill Items
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table align-middle">
                                    <thead>
                                        <tr>
                                            <th>Item Name</th>
                                            <th>Quantity</th>
                                            <th>Unit Price</th>
                                            <th>Subtotal</th>
                                            <th class="text-end">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody id="bill-items-body">
                                        <%-- This will be populated by JavaScript --%>
                                    </tbody>
                                </table>
                            </div>
                            
                            <%-- This div will hold the hidden inputs for the backend --%>
                            <div id="hidden-inputs-container"></div>
                            
                            <hr>

                            <div class="row g-3 justify-content-end align-items-center mt-3">
                              <div class="col-md-3">
                                <label for="discountPct" class="form-label">Discount (%)</label>
                                <input type="number" id="discountPct" name="discountPct" min="0" max="100" value="0" class="form-control">
                              </div>
                            </div>

                            <hr class="my-4">
                            <div class="d-flex justify-content-end">
                              <a href="<c:url value='/dashboard'/>" class="btn btn-secondary me-2">Cancel</a>
                              <button type="submit" class="btn btn-primary">Generate Bill</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </main>
    </div>

    <%-- Bootstrap JS --%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <%-- Custom JavaScript for Interactive Billing --%>
    <script>
document.addEventListener('DOMContentLoaded', function () {
  // ---- DOM refs ----
  var itemSelect = document.getElementById('item-select');
  var quantityInput = document.getElementById('item-quantity');
  var addItemBtn = document.getElementById('add-item-btn');
  var billItemsBody = document.getElementById('bill-items-body');
  var hiddenInputsContainer = document.getElementById('hidden-inputs-container');

  // ---- State ----
  var billItems = [];

  // Small helper to avoid HTML injection in item names
  function escapeHtml(str) {
    return String(str)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#039;');
  }

  function renderBill() {
    billItemsBody.innerHTML = '';
    hiddenInputsContainer.innerHTML = '';

    if (billItems.length === 0) {
      billItemsBody.innerHTML =
        '<tr><td colspan="5" class="text-center text-muted">No items added yet.</td></tr>';
      return;
    }

    var tableHtml = '';
    var hiddenInputsHtml = '';

    billItems.forEach(function (item, index) {
      var subtotal = item.price * item.quantity;

      tableHtml +=
        '<tr>' +
          '<td>' + escapeHtml(item.name) + '</td>' +
          '<td>' + item.quantity + '</td>' +
          '<td>LKR ' + item.price.toFixed(2) + '</td>' +
          '<td>LKR ' + subtotal.toFixed(2) + '</td>' +
          '<td class="text-end">' +
            '<button type="button" class="btn btn-outline-danger remove-item-btn" data-index="' + index + '">Remove</button>' +
          '</td>' +
        '</tr>';

      // Hidden inputs for backend (repeat names so you can read arrays via getParameterValues)
      hiddenInputsHtml +=
        '<input type="hidden" name="itemId" value="' + item.id + '">' +
        '<input type="hidden" name="quantity" value="' + item.quantity + '">';
    });

    billItemsBody.innerHTML = tableHtml;
    hiddenInputsContainer.innerHTML = hiddenInputsHtml;
  }

  function handleAddItem() {
    var selectedOption = itemSelect.options[itemSelect.selectedIndex];
    var itemId = selectedOption ? selectedOption.value : '';
    var quantity = parseInt(quantityInput.value, 10);

    if (!itemId || isNaN(quantity) || quantity <= 0) {
      alert('Please select a valid item and quantity.');
      return;
    }

    var itemName = selectedOption.getAttribute('data-name');
    var itemPrice = parseFloat(selectedOption.getAttribute('data-price'));

    var existingItem = billItems.find(function (it) { return it.id === itemId; });
    if (existingItem) {
      existingItem.quantity += quantity;
    } else {
      billItems.push({
        id: itemId,
        name: itemName,
        price: itemPrice,
        quantity: quantity
      });
    }

    // Reset inputs and re-render
    itemSelect.value = '';
    quantityInput.value = '1';
    renderBill();
  }

  function handleRemoveItem(index) {
    billItems.splice(index, 1);
    renderBill();
  }

  // Events
  addItemBtn.addEventListener('click', handleAddItem);

  billItemsBody.addEventListener('click', function (event) {
    var target = event.target;
    if (target.classList.contains('remove-item-btn')) {
      var index = parseInt(target.getAttribute('data-index'), 10);
      if (!isNaN(index)) {
        handleRemoveItem(index);
      }
    }
  });

  // Initial paint
  renderBill();
});
</script>
    
</body>
</html>