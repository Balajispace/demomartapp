<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Admin Dashboard - BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 24px;">
    <h1 style="font-family: 'Outfit', sans-serif; font-size: 2.2rem; font-weight: 800;">
        <i class="fa-solid fa-gauge"></i> System Control Panel
    </h1>
    <p style="color: var(--gray-500);">Overview of platform statistics, metrics, users, and catalog</p>
</div>

<!-- Stats Metric Cards Grid -->
<div class="stats-grid">
    <div class="stat-card">
        <div class="stat-icon" style="background: var(--primary-light); color: var(--primary);">
            <i class="fa-solid fa-users"></i>
        </div>
        <div>
            <div class="stat-value">${totalUsers}</div>
            <div class="stat-label">Total Users</div>
            <div style="font-size: 0.78rem; color: var(--gray-500); margin-top: 4px;">
                ${totalBuyers} Buyers | ${totalSellers} Sellers
            </div>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-icon" style="background: #e0e7ff; color: #4338ca;">
            <i class="fa-solid fa-boxes-stacked"></i>
        </div>
        <div>
            <div class="stat-value">${totalProducts}</div>
            <div class="stat-label">Total Products</div>
            <div style="font-size: 0.78rem; color: var(--gray-500); margin-top: 4px;">
                ${pendingProducts} Pending Approval
            </div>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-icon" style="background: #fef3c7; color: #b45309;">
            <i class="fa-solid fa-receipt"></i>
        </div>
        <div>
            <div class="stat-value">${totalOrders}</div>
            <div class="stat-label">Total Orders</div>
            <div style="font-size: 0.78rem; color: var(--gray-500); margin-top: 4px;">
                Completed Transactions
            </div>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-icon" style="background: var(--success-light); color: var(--success);">
            <i class="fa-solid fa-indian-rupee-sign"></i>
        </div>
        <div>
            <div class="stat-value">₹<fmt:formatNumber value="${totalRevenue}" pattern="#,##0.00"/></div>
            <div class="stat-label">Total Platform Revenue</div>
        </div>
    </div>
</div>

<!-- Quick Shortcuts -->
<div style="display: flex; gap: 16px; margin-bottom: 30px;">
    <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-primary">
        <i class="fa-solid fa-user-gear"></i> Manage Users (${totalUsers})
    </a>
    <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline">
        <i class="fa-solid fa-box-open"></i> Manage Products (${totalProducts})
    </a>
    <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-outline">
        <i class="fa-solid fa-receipt"></i> View Orders (${totalOrders})
    </a>
</div>

<!-- Recent Products Overview -->
<div class="table-card">
    <div style="padding: 20px; border-bottom: 1px solid var(--gray-200); display: flex; justify-content: space-between; align-items: center;">
        <h3 style="font-family: 'Outfit', sans-serif; font-size: 1.2rem; font-weight: 700;">Recent Product Listings</h3>
        <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline btn-sm">View All Products</a>
    </div>
    <table class="table">
        <thead>
            <tr>
                <th>Product</th>
                <th>Category</th>
                <th>Price</th>
                <th>Seller</th>
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
                    <td>${not empty p.sellerName ? p.sellerName : 'System Catalog'}</td>
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
                        <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline btn-sm">Manage</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
