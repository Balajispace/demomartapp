<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp4/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp4/jstl/fmt" %>
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
            <li><a href="${pageContext.request.contextPath}/products" class="nav-link"><i class="fa-solid fa-store"></i> All Products</a></li>
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <li><a href="${pageContext.request.contextPath}/cart" class="cart-btn"><i class="fa-solid fa-cart-shopping"></i> Cart</a></li>
                    <li><a href="${pageContext.request.contextPath}/orders" class="nav-link"><i class="fa-solid fa-box"></i> My Orders</a></li>
                    <li>
                        <span style="font-weight: 700; color: var(--primary); font-size: 0.9rem;">
                            <i class="fa-solid fa-user-circle"></i> ${sessionScope.user.name}
                        </span>
                    </li>
                    <li><a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm"><i class="fa-solid fa-right-from-bracket"></i> Logout</a></li>
                </c:when>
                <c:otherwise>
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
