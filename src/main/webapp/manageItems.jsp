<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    // Security Check
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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="css/dashboard.css"> 
</head>
<body>

    <jsp:include page="sidebar.jsp">
        <jsp:param name="activePage" value="items"/>
    </jsp:include>

    <main class="main-content">
        <div class="container-fluid">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1 class="h3">Manage Items</h1>
                <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addItemModal">
                    <i class="bi bi-plus-circle-fill me-2"></i>Add New Item
                </button>
            </div>

            <div class="card shadow">
                <div class="card-body">
                    <table class="table table-hover align-middle">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Item Name</th>
                                <th>Unit Price (LKR)</th>
                                <th>Stock Quantity</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${itemList}">
                                <tr>
                                    <td><c:out value="${item.itemId}" /></td>
                                    <td><c:out value="${item.itemName}" /></td>
                                    <td><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol=""/></td>
                                    <td><c:out value="${item.stockQuantity}" /></td>
                                    <td>
                                        <button class="btn btn-sm btn-outline-primary edit-btn"
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
    </main>

    <!-- Add Item Modal -->
    <div class="modal fade" id="addItemModal" tabindex="-1" aria-labelledby="addItemModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addItemModalLabel">Add New Item</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="manageItems" method="post">
                    <input type="hidden" name="action" value="add">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="itemName" class="form-label">Item Name</label>
                            <input type="text" class="form-control" id="itemName" name="itemName" required>
                        </div>
                        <div class="mb-3">
                            <label for="unitPrice" class="form-label">Unit Price (LKR)</label>
                            <input type="number" step="0.01" class="form-control" id="unitPrice" name="unitPrice" required>
                        </div>
                        <div class="mb-3">
                            <label for="stockQuantity" class="form-label">Stock Quantity</label>
                            <input type="number" class="form-control" id="stockQuantity" name="stockQuantity" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Save Item</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Edit Item Modal -->
    <div class="modal fade" id="editItemModal" tabindex="-1" aria-labelledby="editItemModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editItemModalLabel">Edit Item</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="manageItems" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="itemId" id="editItemId">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="editItemName" class="form-label">Item Name</label>
                            <input type="text" class="form-control" id="editItemName" name="itemName" required>
                        </div>
                        <div class="mb-3">
                            <label for="editUnitPrice" class="form-label">Unit Price (LKR)</label>
                            <input type="number" step="0.01" class="form-control" id="editUnitPrice" name="unitPrice" required>
                        </div>
                        <div class="mb-3">
                            <label for="editStockQuantity" class="form-label">Stock Quantity</label>
                            <input type="number" class="form-control" id="editStockQuantity" name="stockQuantity" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Save Changes</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Delete Item Modal -->
    <div class="modal fade" id="deleteItemModal" tabindex="-1" aria-labelledby="deleteItemModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="deleteItemModalLabel">Confirm Deletion</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    Are you sure you want to delete this item: <strong id="itemNameToDelete"></strong>?
                </div>
                <div class="modal-footer">
                    <form action="manageItems" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="itemId" id="itemIdToDelete">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-danger">Delete Item</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // JS for Edit Modal
        const editItemModal = document.getElementById('editItemModal');
        editItemModal.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget;
            editItemModal.querySelector('#editItemId').value = button.getAttribute('data-item-id');
            editItemModal.querySelector('#editItemName').value = button.getAttribute('data-item-name');
            editItemModal.querySelector('#editUnitPrice').value = button.getAttribute('data-item-price');
            editItemModal.querySelector('#editStockQuantity').value = button.getAttribute('data-item-stock');
        });

        // JS for Delete Modal
        const deleteItemModal = document.getElementById('deleteItemModal');
        deleteItemModal.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget;
            deleteItemModal.querySelector('#itemNameToDelete').textContent = button.getAttribute('data-item-name');
            deleteItemModal.querySelector('#itemIdToDelete').value = button.getAttribute('data-item-id');
        });
    </script>
</body>
</html>
