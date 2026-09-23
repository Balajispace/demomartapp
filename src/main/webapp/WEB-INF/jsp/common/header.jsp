<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle != null ? pageTitle : 'BALAJIMART - Premium Online Store'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<nav class="navbar">
    <div class="container navbar-inner">
        <a href="${pageContext.request.contextPath}/home" class="brand-logo">
            <i class="fa-solid fa-bag-shopping"></i> BALAJI<span>MART</span>
        </a>

        <form action="${pageContext.request.contextPath}/products" method="get" class="search-form">
            <i class="fa-solid fa-magnifying-glass search-icon"></i>
            <input type="text" name="q" class="search-input" placeholder="Search headphones, books, shirts..." value="${searchQuery}">
        </form>

        <ul class="nav-links">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <c:choose>
                        <%-- ADMIN ROLE NAV --%>
                        <c:when test="${sessionScope.user.role == 'ADMIN'}">
                            <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link"><i class="fa-solid fa-gauge"></i> Admin Dashboard</a></li>
                            <li><a href="${pageContext.request.contextPath}/admin/users" class="nav-link"><i class="fa-solid fa-users"></i> Users</a></li>
                            <li><a href="${pageContext.request.contextPath}/admin/products" class="nav-link"><i class="fa-solid fa-boxes-stacked"></i> Products</a></li>
                            <li><a href="${pageContext.request.contextPath}/admin/orders" class="nav-link"><i class="fa-solid fa-receipt"></i> Orders</a></li>
                            <li>
                                <span class="role-badge role-badge-admin">
                                    <i class="fa-solid fa-shield-halved"></i> ADMIN
                                </span>
                            </li>
                        </c:when>

                        <%-- SELLER ROLE NAV --%>
                        <c:when test="${sessionScope.user.role == 'SELLER'}">
                            <li><a href="${pageContext.request.contextPath}/seller/dashboard" class="nav-link"><i class="fa-solid fa-chart-line"></i> Seller Dashboard</a></li>
                            <li><a href="${pageContext.request.contextPath}/seller/products" class="nav-link"><i class="fa-solid fa-store"></i> My Products</a></li>
                            <li><a href="${pageContext.request.contextPath}/seller/products/add" class="nav-link"><i class="fa-solid fa-plus-circle"></i> Add Product</a></li>
                            <li><a href="${pageContext.request.contextPath}/seller/orders" class="nav-link"><i class="fa-solid fa-truck-ramp-box"></i> Sales Orders</a></li>
                            <li>
                                <span class="role-badge role-badge-seller">
                                    <i class="fa-solid fa-shop"></i> SELLER
                                </span>
                            </li>
                        </c:when>

                        <%-- BUYER ROLE NAV (DEFAULT) --%>
                        <c:otherwise>
                            <li><a href="${pageContext.request.contextPath}/products" class="nav-link"><i class="fa-solid fa-store"></i> Products</a></li>
                            <li><a href="${pageContext.request.contextPath}/wishlist" class="nav-link"><i class="fa-solid fa-heart" style="color: #ef4444;"></i> Wishlist</a></li>
                            <li><a href="${pageContext.request.contextPath}/cart" class="cart-btn"><i class="fa-solid fa-cart-shopping"></i> Cart</a></li>
                            <li><a href="${pageContext.request.contextPath}/orders" class="nav-link"><i class="fa-solid fa-box"></i> My Orders</a></li>
                            <li>
                                <span class="role-badge role-badge-buyer">
                                    <i class="fa-solid fa-user"></i> ${sessionScope.user.name}
                                </span>
                            </li>
                        </c:otherwise>
                    </c:choose>
                    <li><a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm"><i class="fa-solid fa-right-from-bracket"></i> Logout</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${pageContext.request.contextPath}/products" class="nav-link"><i class="fa-solid fa-store"></i> All Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-sm">Sign In</a></li>
                    <li><a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm">Register</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>

<main class="container" style="flex: 1; padding-top: 20px; padding-bottom: 40px;">

<!-- Global Flash Messages -->
<c:if test="${not empty sessionScope.successMessage}">
    <div class="alert alert-success">
        <i class="fa-solid fa-circle-check"></i> ${sessionScope.successMessage}
    </div>
    <c:remove var="successMessage" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.errorMessage}">
    <div class="alert alert-danger">
        <i class="fa-solid fa-triangle-exclamation"></i> ${sessionScope.errorMessage}
    </div>
    <c:remove var="errorMessage" scope="session"/>
</c:if>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">
        <i class="fa-solid fa-triangle-exclamation"></i> ${errorMessage}
    </div>
</c:if>
