<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Detail</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;
            background-color: #f5f5f5;
            padding: 20px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            margin-bottom: 30px;
        }
        .detail-group {
            margin-bottom: 20px;
            padding-bottom: 20px;
            border-bottom: 1px solid #e0e0e0;
        }
        .detail-group:last-of-type {
            border-bottom: none;
        }
        .detail-label {
            font-weight: 600;
            color: #666;
            margin-bottom: 5px;
            font-size: 14px;
            text-transform: uppercase;
        }
        .detail-value {
            color: #333;
            font-size: 16px;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 4px;
            border: none;
            cursor: pointer;
            font-size: 14px;
            transition: all 0.3s;
            margin-right: 10px;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
        .btn-warning {
            background-color: #ffc107;
            color: #212529;
        }
        .btn-warning:hover {
            background-color: #e0a800;
        }
        .btn-danger {
            background-color: #dc3545;
            color: white;
        }
        .btn-danger:hover {
            background-color: #c82333;
        }
        .actions {
            margin-top: 30px;
            padding-top: 20px;
            border-top: 2px solid #e0e0e0;
        }
        .actions form {
            display: inline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>User Details</h1>

        <div class="detail-group">
            <div class="detail-label">ID</div>
            <div class="detail-value">${user.id}</div>
        </div>

        <div class="detail-group">
            <div class="detail-label">Username</div>
            <div class="detail-value">${user.username}</div>
        </div>

        <div class="detail-group">
            <div class="detail-label">Email</div>
            <div class="detail-value">${user.email}</div>
        </div>

        <div class="detail-group">
            <div class="detail-label">Full Name</div>
            <div class="detail-value">${user.fullName}</div>
        </div>

        <div class="detail-group">
            <div class="detail-label">Created At</div>
            <div class="detail-value">${user.createdAt}</div>
        </div>

        <div class="detail-group">
            <div class="detail-label">Updated At</div>
            <div class="detail-value">${user.updatedAt}</div>
        </div>

        <div class="actions">
            <a href="${pageContext.request.contextPath}/users" class="btn btn-secondary">Back to List</a>
            <a href="${pageContext.request.contextPath}/users/${user.id}/edit" class="btn btn-warning">Edit</a>
            <form action="${pageContext.request.contextPath}/users/${user.id}/delete" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this user?');">
                <button type="submit" class="btn btn-danger">Delete</button>
            </form>
        </div>
    </div>
</body>
</html>