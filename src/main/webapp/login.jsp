<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PahanaEdu Bookstore - Login</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <style>
        body {
            /* Applying a white to gray gradient background */
            background: linear-gradient(to right, #FFFFFF, #E0E0E0);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .login-card {
            border: none;
            border-radius: 1rem;
            /* Added deep box-shadow for depth */
            box-shadow: 0 1rem 3rem rgba(0, 0, 0, 0.15);
            overflow: hidden; /* Ensures the image corners are rounded with the card */
        }

        .login-card .form-side {
            padding: 3rem;
            background-color: #ffffff;
        }

        .login-card .img-side {
            padding: 0;
        }

        .login-card .img-fluid {
            height: 100%;
            width: 100%;
            object-fit: cover; /* Prevents the image from stretching */
        }
        
        .btn-custom-login {
            /* Button with a gradient */
            background: linear-gradient(to right, #113F67, #58A0C8);
            border: none;
            color: white;
            font-weight: bold;
            padding: 0.75rem;
            border-radius: 0.5rem;
            transition: all 0.3s ease-in-out;
        }

        .btn-custom-login:hover {
            /* A hover effect that reverses the gradient and adds a shadow */
            background: linear-gradient(to right, #58A0C8, #113F67);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
        }
        
        .form-control:focus {
            box-shadow: 0 0 0 0.25rem rgba(17, 63, 103, 0.25);
            border-color: #58A0C8;
        }

        /* Responsive adjustments */
        @media (max-width: 991.98px) {
            .login-card .img-side {
                display: none; /* Hide the image on tablets and mobile devices */
            }
            .login-card {
                max-width: 450px; /* Constrain form width on smaller screens */
                margin: 2rem;
            }
        }
    </style>
</head>
<body>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-lg-10 col-xl-9">
            <div class="card login-card">
                <div class="row g-0">
                    
                    <div class="col-lg-6">
                        <div class="form-side">
                            <h2 class="fw-bold mb-2 text-uppercase">Welcome PahanaEdu Bookstore</h2>
                            <p class="text-black-50 mb-4">Please enter your credentials to log in.</p>

                            <%-- Display error message if it exists in the request --%>
                            <% if (request.getAttribute("errorMessage") != null) { %>
                                <div class="alert alert-danger" role="alert">
                                    <%= request.getAttribute("errorMessage") %>
                                </div>
                            <% } %>

                            <form action="login" method="post">
                                <div class="mb-3">
                                    <label for="username" class="form-label">Email</label>
                                    <input type="text" class="form-control form-control-lg" id="username" name="username" placeholder="Enter your email address" required>
                                </div>
                                <div class="mb-4">
                                    <label for="password" class="form-label">Password</label>
                                    <input type="password" class="form-control form-control-lg" id="password" name="password" placeholder="Enter your password" required>
                                </div>
                                <button type="submit" class="btn btn-custom-login w-100">Login</button>
                            </form>
                        </div>
                    </div>

                    <div class="col-lg-6 img-side">
                        <%-- The image path is set as requested --%>
                        <img src="img/b.jpg" alt="Login image" class="img-fluid" onerror="this.onerror=null;this.src='https://placehold.co/600x800/113F67/FFFFFF?text=Bookstore';">
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
