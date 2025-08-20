<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
    <title>Manage Customers - Pahana Edu</title>
    
    <%-- Bootstrap CSS --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <%-- Bootstrap Icons --%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <%-- Google Fonts --%>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <%-- Link to the MODERN stylesheet --%>
    <link rel="stylesheet" href="css/dashboard_modern.css">
</head>
<body>

<div class="page-wrapper">
    <%-- Sidebar is included here --%>
    <jsp:include page="sidebar.jsp">
        <jsp:param name="activePage" value="customers"/>
    </jsp:include>

    <main class="main-content">
        <div class="container-fluid">
            <div class="header-bar">
                <h1 class="h2 mb-0">Manage Customers</h1>
                <p class="text-muted">Add, edit, or view customer details.</p>
            </div>

            <button class="btn btn-primary gradient-button mb-4" data-bs-toggle="modal" data-bs-target="#addCustomerModal">
                <i class="bi bi-person-plus-fill me-2"></i>Add New Customer
            </button>

            <div class="card data-card shadow-sm">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Telephone</th>
                                    <th>NIC</th>
                                    <th class="text-end">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="customer" items="${customerList}">
                                    <tr>
                                        <td><c:out value="${customer.customerId}" /></td>
                                        <td><c:out value="${customer.firstName} ${customer.lastName}" /></td>
                                        <td><c:out value="${customer.email}" /></td>
                                        <td><c:out value="${customer.telephone}" /></td>
                                        <td><c:out value="${customer.nicNumber}" /></td>
                                        <td class="text-end">
                                            <button class="btn btn-sm btn-outline-light edit-btn"
                                                    data-bs-toggle="modal"
                                                    data-bs-target="#editCustomerModal"
                                                    data-customer-id="${customer.customerId}"
                                                    data-customer-firstname="${customer.firstName}"
                                                    data-customer-lastname="${customer.lastName}"
                                                    data-customer-nic="${customer.nicNumber}"
                                                    data-customer-email="${customer.email}"
                                                    data-customer-address="${customer.address}"
                                                    data-customer-telephone="${customer.telephone}">
                                                <i class="bi bi-pencil-fill"></i>
                                            </button>
                                            <button class="btn btn-sm btn-outline-danger delete-btn"
                                                    data-bs-toggle="modal"
                                                    data-bs-target="#deleteCustomerModal"
                                                    data-customer-id="${customer.customerId}"
                                                    data-customer-name="${customer.firstName} ${customer.lastName}">
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

    <!-- Add Customer Modal -->
    <div class="modal fade" id="addCustomerModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <form action="manageCustomers" method="post">
                    <input type="hidden" name="action" value="add">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Customer</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-6 mb-3"><label class="form-label">First Name</label><input type="text" class="form-control" name="firstName" required></div>
                            <div class="col-md-6 mb-3"><label class="form-label">Last Name</label><input type="text" class="form-control" name="lastName" required></div>
                        </div>
                        <div class="mb-3"><label class="form-label">Email Address</label><input type="email" class="form-control" name="email"></div>
                        <div class="mb-3"><label class="form-label">Telephone</label><input type="text" class="form-control" name="telephone"></div>
                        <div class="mb-3"><label class="form-label">NIC Number</label><input type="text" class="form-control" name="nicNumber"></div>
                        <div class="mb-3"><label class="form-label">Address</label><textarea class="form-control" name="address" rows="3"></textarea></div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Save Customer</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Edit Customer Modal -->
    <div class="modal fade" id="editCustomerModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <form action="manageCustomers" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="customerId" id="editCustomerId">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Customer</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                         <div class="row">
                            <div class="col-md-6 mb-3"><label class="form-label">First Name</label><input type="text" class="form-control" name="firstName" id="editFirstName" required></div>
                            <div class="col-md-6 mb-3"><label class="form-label">Last Name</label><input type="text" class="form-control" name="lastName" id="editLastName" required></div>
                        </div>
                        <div class="mb-3"><label class="form-label">Email Address</label><input type="email" class="form-control" name="email" id="editEmail"></div>
                        <div class="mb-3"><label class="form-label">Telephone</label><input type="text" class="form-control" name="telephone" id="editTelephone"></div>
                        <div class="mb-3"><label class="form-label">NIC Number</label><input type="text" class="form-control" name="nicNumber" id="editNicNumber"></div>
                        <div class="mb-3"><label class="form-label">Address</label><textarea class="form-control" name="address" id="editAddress" rows="3"></textarea></div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Save Changes</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Delete Customer Modal -->
    <div class="modal fade" id="deleteCustomerModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirm Deletion</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    Are you sure you want to delete this customer: <strong id="customerNameToDelete"></strong>? This action cannot be undone.
                </div>
                <div class="modal-footer">
                    <form action="manageCustomers" method="post" class="d-flex w-100">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="customerId" id="customerIdToDelete">
                        <button type="button" class="btn btn-secondary me-auto" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-danger">Delete Customer</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // JavaScript to populate the Edit Modal
        const editCustomerModal = document.getElementById('editCustomerModal');
        editCustomerModal.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget;
            // Populate the form fields with data from the data-* attributes
            editCustomerModal.querySelector('#editCustomerId').value = button.getAttribute('data-customer-id');
            editCustomerModal.querySelector('#editFirstName').value = button.getAttribute('data-customer-firstname');
            editCustomerModal.querySelector('#editLastName').value = button.getAttribute('data-customer-lastname');
            editCustomerModal.querySelector('#editEmail').value = button.getAttribute('data-customer-email');
            editCustomerModal.querySelector('#editTelephone').value = button.getAttribute('data-customer-telephone');
            editCustomerModal.querySelector('#editNicNumber').value = button.getAttribute('data-customer-nic');
            editCustomerModal.querySelector('#editAddress').value = button.getAttribute('data-customer-address');
        });

        // JavaScript to populate the Delete Modal
        const deleteCustomerModal = document.getElementById('deleteCustomerModal');
        deleteCustomerModal.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget;
            // Set the customer name and ID in the confirmation modal
            deleteCustomerModal.querySelector('#customerNameToDelete').textContent = button.getAttribute('data-customer-name');
            deleteCustomerModal.querySelector('#customerIdToDelete').value = button.getAttribute('data-customer-id');
        });
    </script>
</body>
</html>