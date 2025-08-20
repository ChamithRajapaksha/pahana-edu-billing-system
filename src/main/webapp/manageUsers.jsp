%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
    <title>Manage Users - Pahana Edu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="css/dashboard.css"> 
</head>
<body>

    <jsp:include page="sidebar.jsp">
        <jsp:param name="activePage" value="users"/>
    </jsp:include>

    <main class="main-content">
        <div class="container-fluid">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1 class="h3">Manage Users</h1>
                <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addUserModal">
                    <i class="bi bi-plus-circle-fill me-2"></i>Add New User
                </button>
            </div>

            <div class="card shadow">
                <div class="card-body">
                    <table class="table table-hover align-middle">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Full Name</th>
                                <th>Username</th>
                                <th>Role</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${userList}">
                                <tr>
                                    <td><c:out value="${user.userId}" /></td>
                                    <td><c:out value="${user.fullName}" /></td>
                                    <td><c:out value="${user.username}" /></td>
                                    <td><span class="badge bg-secondary text-uppercase"><c:out value="${user.role}" /></span></td>
                                    <td>
                                        <button class="btn btn-sm btn-outline-primary edit-btn"
                                                data-bs-toggle="modal"
                                                data-bs-target="#editUserModal"
                                                data-user-id="${user.userId}"
                                                data-user-fullname="${user.fullName}"
                                                data-user-username="${user.username}"
                                                data-user-role="${user.role}">
                                            <i class="bi bi-pencil-fill"></i>
                                        </button>
                                        <button class="btn btn-sm btn-outline-danger delete-btn" 
                                                data-bs-toggle="modal" 
                                                data-bs-target="#deleteUserModal"
                                                data-user-id="${user.userId}"
                                                data-user-name="${user.fullName}">
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

    <!-- Add User Modal -->
    <div class="modal fade" id="addUserModal" tabindex="-1" aria-labelledby="addUserModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addUserModalLabel">Add New User</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="manageUsers" method="post">
                    <input type="hidden" name="action" value="add">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="fullName" class="form-label">Full Name</label>
                            <input type="text" class="form-control" id="fullName" name="fullName" required>
                        </div>
                        <div class="mb-3">
                            <label for="username" class="form-label">Username</label>
                            <input type="text" class="form-control" id="username" name="username" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Password</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <div class="mb-3">
                            <label for="role" class="form-label">Role</label>
                            <select class="form-select" id="role" name="role">
                                <option value="CASHIER">Cashier</option>
                                <option value="ADMIN">Admin</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Save User</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Delete Confirmation Modal (existing code) -->
    <div class="modal fade" id="deleteUserModal" ... >
        <!-- ... modal content ... -->
    </div>

    <!-- Edit User Modal -->
    <div class="modal fade" id="editUserModal" tabindex="-1" aria-labelledby="editUserModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editUserModalLabel">Edit User</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="manageUsers" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="userId" id="editUserId">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="editFullName" class="form-label">Full Name</label>
                            <input type="text" class="form-control" id="editFullName" name="fullName" required>
                        </div>
                        <div class="mb-3">
                            <label for="editUsername" class="form-label">Username</label>
                            <input type="text" class="form-control" id="editUsername" name="username" required>
                        </div>
                        <div class="mb-3">
                            <label for="editRole" class="form-label">Role</label>
                            <select class="form-select" id="editRole" name="role">
                                <option value="CASHIER">Cashier</option>
                                <option value="ADMIN">Admin</option>
                            </select>
                        </div>
                        <p class="form-text">Note: Password cannot be changed from this form.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Save Changes</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // JS for Delete Modal (existing code)
        const deleteUserModal = document.getElementById('deleteUserModal');
        deleteUserModal.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget; // Button that triggered the modal
            const userId = button.getAttribute('data-user-id');
            const userName = button.getAttribute('data-user-name');
            
            // Update the modal's content
            const modalBodyStrong = deleteUserModal.querySelector('#userNameToDelete');
            const userIdInput = deleteUserModal.querySelector('#userIdToDelete');
            
            modalBodyStrong.textContent = userName;
            userIdInput.value = userId;
        });

        // JS for Edit Modal
        const editUserModal = document.getElementById('editUserModal');
        editUserModal.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget;
            // Extract info from data-* attributes
            const userId = button.getAttribute('data-user-id');
            const fullName = button.getAttribute('data-user-fullname');
            const username = button.getAttribute('data-user-username');
            const role = button.getAttribute('data-user-role');
            
            // Update the modal's content
            editUserModal.querySelector('#editUserId').value = userId;
            editUserModal.querySelector('#editFullName').value = fullName;
            editUserModal.querySelector('#editUsername').value = username;
            editUserModal.querySelector('#editRole').value = role;
        });
    </script>
</body>
</html>

