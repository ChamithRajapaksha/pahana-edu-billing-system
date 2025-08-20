<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PahanaEdu Bookstore - Login</title>
    
    <%-- Bootstrap CSS --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <%-- Bootstrap Icons for the header --%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <%-- Link to the modern dashboard stylesheet --%>
    <link rel="stylesheet" href="css/dashboard_modern.css">

    <style>
        /* Custom styles to center the login card */
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 2rem;
        }
        .login-card-wrapper {
            max-width: 900px;
            width: 100%;
        }
        .login-card-wrapper .data-card {
            overflow: hidden;
        }
        .img-side {
            padding: 0;
        }
        .img-fluid {
            height: 100%;
            width: 100%;
            object-fit: cover;
        }
        
        @media (max-width: 991.98px) {
            .img-side {
                display: none;
            }
        }
    </style>
</head>
<body>

<div class="login-card-wrapper">
    <div class="card data-card shadow-lg">
        <div class="row g-0">

            <div class="col-lg-6">
                <div class="p-4 p-md-5">
                    
                    <%-- FIX: Styles updated for better visibility and "pop" --%>
                    <div class="text-center mb-4">
                        <i class="bi bi-book-half" style="font-size: 3.5rem; color: #FFFFFF; text-shadow: 0 2px 8px rgba(0,0,0,0.4);"></i>
                        <h1 class="h2 mt-2" style="font-weight: 700; letter-spacing: 1px; color: #FFFFFF; text-shadow: 0 2px 8px rgba(0,0,0,0.4);">Pahana Edu</h1>
                        <p style="font-size: 1.1rem; color: rgba(255, 255, 255, 0.9);">Please sign in to continue</p>
                    </div>

                    <%-- Display error message if it exists --%>
                    <% if (request.getAttribute("errorMessage") != null) { %>
                        <div class="alert alert-danger" role="alert">
                            <%= request.getAttribute("errorMessage") %>
                        </div>
                    <% } %>

                    <form action="login" method="post">
                        <div class="mb-3">
                            <label for="username" class="form-label">User Name</label>
                            <input type="text" class="form-control" id="username" name="username" placeholder="Enter your email address" required>
                        </div>
                        
                        <div class="mb-4">
                            <label for="password" class="form-label">Password</label>
                            <input type="password" class="form-control" id="password" name="password" placeholder="Enter your password" required>
                        </div>
                        
                        <button type="submit" class="btn btn-lg gradient-button w-100">Login</button>
                    </form>
                </div>
            </div>

            <div class="col-lg-6 img-side">
                <img src="img/a.jpg" alt="Bookstore Image" class="img-fluid" onerror="this.onerror=null;this.src='https://placehold.co/600x800/16222A/FFFFFF?text=Pahana+Edu';">
            </div>

        </div>
    </div>
</div>

</body>
</html>