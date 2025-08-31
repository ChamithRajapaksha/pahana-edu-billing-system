<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%-- Security Check: Ensure user is a logged-in ADMIN --%>
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
    <title>Manage Items - Pahana Edu</title>
    
    <%-- Bootstrap CSS --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <%-- Bootstrap Icons --%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <%-- Google Fonts --%>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <%-- Link to your stylesheet --%>
    <link rel="stylesheet" href="css/dashboard_modern.css"> 
</head>
<body>

<div class="page-wrapper">
    <%-- Sidebar is included here --%>
    <jsp:include page="sidebar.jsp">
        <jsp:param name="activePage" value="items"/>
    </jsp:include>

    <main class="main-content">
        <div class="container-fluid">
            <div class="header-bar">
                <h1 class="h2 mb-0">Manage Items</h1>
                <p class="text-muted">Add, update, or remove stock items.</p>
            </div>
            
            <c:if test="${not empty sessionScope.flashMessage}">
                <div class="alert alert-${sessionScope.flashMessageType} alert-dismissible fade show" role="alert">
                    <c:out value="${sessionScope.flashMessage}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <%-- Clear the message from the session after displaying it --%>
                <% session.removeAttribute("flashMessage"); %>
                <% session.removeAttribute("flashMessageType"); %>
            </c:if>

            <button class="btn btn-primary gradient-button mb-4" data-bs-toggle="modal" data-bs-target="#addItemModal">
                <i class="bi bi-plus-circle-fill me-2"></i>Add New Item
            </button>

            <div class="card data-card shadow-sm">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead>
                                <tr>
                                    <th>Item ID</th>
                                    <th>Item Name</th>
                                    <th class="text-end">Unit Price</th>
                                    <th class="text-end">Stock Quantity</th>
                                    <th class="text-end">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${itemList}">
                                    <tr>
                                        <td><c:out value="${item.itemId}" /></td>
                                        <td><c:out value="${item.itemName}" /></td>
                                        <td class="text-end"><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="Rs."/></td>
                                        <td class="text-end"><c:out value="${item.stockQuantity}" /></td>
                                        <td class="text-end">
                                            <button class="btn btn-sm btn-outline-light edit-btn"
                                                    data-bs-toggle="modal"
                                                    data-bs-target="#editItemModal"
                                                    data-item-id="${item.itemId}"
                                                    data-item-name="${item.itemName}"
                                                    data-item-price="${item.unitPrice}"
                                                    data-item-stock="${item.stockQuantity}">
                                                <i class="bi bi-pencil-fill"></i>
                                            </button>
                                            <button class="btn btn-sm btn-outline-danger delete-btn" 
                                                    data-bs-toggle="modal" 
                                                    data-bs-target="#deleteItemModal"
                                                    data-item-id="${item.itemId}"
                                                    data-item-name="${item.itemName}">
                                                <i class="bi bi-trash-fill"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

    <div class="modal fade" id="addItemModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <form action="manageItems" method="post">
                    <input type="hidden" name="action" value="add">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Item</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3"><label class="form-label">Item Name</label><input type="text" class="form-control" name="itemName" required></div>
                        <div class="mb-3"><label class="form-label">Unit Price (Rs.)</label><input type="number" step="0.01" class="form-control" name="unitPrice" required></div>
                        <div class="mb-3"><label class="form-label">Stock Quantity</label><input type="number" class="form-control" name="stockQuantity" required></div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Save Item</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="modal fade" id="editItemModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <form action="manageItems" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="itemId" id="editItemId">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Item</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3"><label class="form-label">Item Name</label><input type="text" class="form-control" id="editItemName" name="itemName" required></div>
                        <div class="mb-3"><label class="form-label">Unit Price (Rs.)</label><input type="number" step="0.01" class="form-control" id="editUnitPrice" name="unitPrice" required></div>
                        <div class="mb-3"><label class="form-label">Stock Quantity</label><input type="number" class="form-control" id="editStockQuantity" name="stockQuantity" required></div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Save Changes</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="modal fade" id="deleteItemModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirm Deletion</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">Are you sure you want to delete this item: <strong id="itemNameToDelete"></strong>?</div>
                <div class="modal-footer">
                    <form action="manageItems" method="post" class="d-flex w-100">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="itemId" id="itemIdToDelete">
                        <button type="button" class="btn btn-secondary me-auto" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-danger">Delete Item</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // JS for populating the Edit Modal
        const editItemModal = document.getElementById('editItemModal');
        editItemModal.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget;
            editItemModal.querySelector('#editItemId').value = button.getAttribute('data-item-id');
            editItemModal.querySelector('#editItemName').value = button.getAttribute('data-item-name');
            editItemModal.querySelector('#editUnitPrice').value = button.getAttribute('data-item-price');
            editItemModal.querySelector('#editStockQuantity').value = button.getAttribute('data-item-stock');
        });

        // JS for populating the Delete Modal
        const deleteItemModal = document.getElementById('deleteItemModal');
        deleteItemModal.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget;
            deleteItemModal.querySelector('#itemNameToDelete').textContent = button.getAttribute('data-item-name');
            deleteItemModal.querySelector('#itemIdToDelete').value = button.getAttribute('data-item-id');
        });
    </script>
</body>
</html>