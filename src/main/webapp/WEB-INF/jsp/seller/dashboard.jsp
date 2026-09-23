<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Seller Dashboard - BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 24px; display: flex; justify-content: space-between; align-items: center;">
    <div>
        <h1 style="font-family: 'Outfit', sans-serif; font-size: 2.2rem; font-weight: 800;">
            <i class="fa-solid fa-chart-line"></i> Seller Dashboard
        </h1>
        <p style="color: var(--gray-500);">Manage product listings, track stock, and monitor sales performance</p>
    </div>
    <a href="${pageContext.request.contextPath}/seller/products/add" class="btn btn-primary">
        <i class="fa-solid fa-plus-circle"></i> Add New Product
    </a>
</div>

<!-- Seller Metric Cards Grid -->
<div class="stats-grid">
    <div class="stat-card">
        <div class="stat-icon" style="background: var(--primary-light); color: var(--primary);">
            <i class="fa-solid fa-boxes-stacked"></i>
        </div>
        <div>
            <div class="stat-value">${totalProducts}</div>
            <div class="stat-label">Listed Products</div>
            <div style="font-size: 0.78rem; color: var(--gray-500); margin-top: 4px;">
                ${activeProducts} Approved | ${pendingProducts} Pending
            </div>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-icon" style="background: #e0e7ff; color: #4338ca;">
            <i class="fa-solid fa-truck-ramp-box"></i>
        </div>
        <div>
            <div class="stat-value">${totalItemsSold}</div>
            <div class="stat-label">Units Sold</div>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-icon" style="background: var(--success-light); color: var(--success);">
            <i class="fa-solid fa-indian-rupee-sign"></i>
        </div>
        <div>
            <div class="stat-value">₹<fmt:formatNumber value="${totalEarnings}" pattern="#,##0.00"/></div>
            <div class="stat-label">Total Earnings</div>
        </div>
    </div>
</div>

<!-- Quick Action Shortcuts -->
<div style="display: flex; gap: 16px; margin-bottom: 30px;">
    <a href="${pageContext.request.contextPath}/seller/products" class="btn btn-outline">
        <i class="fa-solid fa-list-check"></i> Manage My Products (${totalProducts})
    </a>
    <a href="${pageContext.request.contextPath}/seller/orders" class="btn btn-outline">
        <i class="fa-solid fa-receipt"></i> View Customer Orders
    </a>
</div>

<!-- Recent Products Overview -->
<div class="table-card">
    <div style="padding: 20px; border-bottom: 1px solid var(--gray-200); display: flex; justify-content: space-between; align-items: center;">
        <h3 style="font-family: 'Outfit', sans-serif; font-size: 1.2rem; font-weight: 700;">My Recent Products</h3>
        <a href="${pageContext.request.contextPath}/seller/products" class="btn btn-outline btn-sm">View All My Products</a>
    </div>
    <c:choose>
        <c:when test="${empty recentProducts}">
            <div style="padding: 40px; text-align: center; color: var(--gray-500);">
                No products listed yet. <a href="${pageContext.request.contextPath}/seller/products/add" style="color: var(--primary); font-weight: 700;">Add your first product now</a>.
            </div>
        </c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${recentProducts}" var="p">
                        <tr>
                            <td style="display: flex; align-items: center; gap: 12px;">
                                <img src="${p.imageUrl}" style="width: 40px; height: 40px; border-radius: var(--radius-sm); object-fit: cover;" onerror="this.src='https://images.unsplash.com/photo-1560343090-f0409e92791a?w=100'">
                                <strong>${p.name}</strong>
                            </td>
                            <td>${p.category}</td>
                            <td>₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                            <td>${p.stock}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.status == 'APPROVED'}">
                                        <span class="status-pill status-completed">APPROVED</span>
                                    </c:when>
                                    <c:when test="${p.status == 'PENDING'}">
                                        <span class="status-pill status-pending">PENDING</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-pill" style="background: #fef2f2; color: #dc2626;">REJECTED</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/seller/products/edit?id=${p.id}" class="btn btn-outline btn-sm"><i class="fa-solid fa-pen"></i> Edit</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
