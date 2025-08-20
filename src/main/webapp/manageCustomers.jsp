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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>

    <jsp:include page="sidebar.jsp">
        <jsp:param name="activePage" value="customers"/>
    </jsp:include>

    <main class="main-content">
        <div class="container-fluid">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1 class="h3">Manage Customers</h1>
                <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addCustomerModal">
                    <i class="bi bi-person-plus-fill me-2"></i>Add New Customer
                </button>
            </div>

            <div class="card shadow">
                <div class="card-body">
                    <table class="table table-hover align-middle">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Telephone</th>
                                <th>NIC</th>
                                <th>Actions</th>
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
                                    <td>
                                        <button class="btn btn-sm btn-outline-primary edit-btn"
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
    </main>

    <!-- Add Customer Modal -->
    <div class="modal fade" id="addCustomerModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="manageCustomers" method="post">
                    <input type="hidden" name="action" value="add">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Customer</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Form fields for new customer -->
                        <div class="mb-3"><input type="text" class="form-control" name="firstName" placeholder="First Name" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="lastName" placeholder="Last Name" required></div>
                        <div class="mb-3"><input type="email" class="form-control" name="email" placeholder="Email Address"></div>
                        <div class="mb-3"><input type="text" class="form-control" name="telephone" placeholder="Telephone"></div>
                        <div class="mb-3"><input type="text" class="form-control" name="nicNumber" placeholder="NIC Number"></div>
                        <div class="mb-3"><textarea class="form-control" name="address" placeholder="Address"></textarea></div>
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
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="manageCustomers" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="customerId" id="editCustomerId">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Customer</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Form fields for editing a customer -->
                        <div class="mb-3"><input type="text" class="form-control" name="firstName" id="editFirstName" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="lastName" id="editLastName" required></div>
                        <div class="mb-3"><input type="email" class="form-control" name="email" id="editEmail"></div>
                        <div class="mb-3"><input type="text" class="form-control" name="telephone" id="editTelephone"></div>
                        <div class="mb-3"><input type="text" class="form-control" name="nicNumber" id="editNicNumber"></div>
                        <div class="mb-3"><textarea class="form-control" name="address" id="editAddress"></textarea></div>
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
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirm Deletion</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    Are you sure you want to delete this customer: <strong id="customerNameToDelete"></strong>?
                </div>
                <div class="modal-footer">
                    <form action="manageCustomers" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="customerId" id="customerIdToDelete">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
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
</html>l>