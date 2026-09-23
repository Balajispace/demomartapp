<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="My Listed Products - Seller BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 24px; display: flex; justify-content: space-between; align-items: center;">
    <div>
        <h1 style="font-family: 'Outfit', sans-serif; font-size: 2.2rem; font-weight: 800;">
            <i class="fa-solid fa-boxes-packing"></i> My Listed Products
        </h1>
        <p style="color: var(--gray-500);">Manage your products, update prices, inventory stock, and descriptions</p>
    </div>
    <a href="${pageContext.request.contextPath}/seller/products/add" class="btn btn-primary">
        <i class="fa-solid fa-plus-circle"></i> Add New Product
    </a>
</div>

<c:choose>
    <c:when test="${empty products}">
        <div class="table-card" style="padding: 60px; text-align: center;">
            <i class="fa-solid fa-box-open" style="font-size: 3.5rem; color: var(--gray-300); margin-bottom: 16px;"></i>
            <h3 style="font-size: 1.4rem; font-weight: 700; margin-bottom: 8px;">No Products Listed Yet</h3>
            <p style="color: var(--gray-500); margin-bottom: 20px;">Start selling by adding your first product to BALAJIMART.</p>
            <a href="${pageContext.request.contextPath}/seller/products/add" class="btn btn-primary"><i class="fa-solid fa-plus"></i> Add Product Now</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-card">
            <table class="table">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${products}" var="p">
                        <tr>
                            <td style="display: flex; align-items: center; gap: 12px;">
                                <img src="${p.imageUrl}" style="width: 48px; height: 48px; border-radius: var(--radius-sm); object-fit: cover;" onerror="this.src='https://images.unsplash.com/photo-1560343090-f0409e92791a?w=100'">
                                <div>
                                    <strong>${p.name}</strong>
                                    <div style="font-size: 0.8rem; color: var(--gray-500); max-width: 250px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                                        ${p.description}
                                    </div>
                                </div>
                            </td>
                            <td>${p.category}</td>
                            <td style="font-weight: 700;">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.stock > 0}">
                                        <span style="font-weight: 700; color: var(--gray-900);">${p.stock}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: var(--danger); font-weight: 700;">Out of Stock</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.status == 'APPROVED'}">
                                        <span class="status-pill status-completed">APPROVED</span>
                                    </c:when>
                                    <c:when test="${p.status == 'PENDING'}">
                                        <span class="status-pill status-pending">PENDING APPROVAL</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-pill" style="background: #fef2f2; color: #dc2626;">REJECTED</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div style="display: flex; gap: 8px;">
                                    <a href="${pageContext.request.contextPath}/seller/products/edit?id=${p.id}" class="btn btn-outline btn-sm">
                                        <i class="fa-solid fa-pen-to-square"></i> Edit
                                    </a>
                                    <form action="${pageContext.request.contextPath}/seller/products/delete" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this product?');">
                                        <input type="hidden" name="id" value="${p.id}">
                                        <button type="submit" class="btn btn-outline btn-sm" style="color: var(--danger); border-color: #fecaca;">
                                            <i class="fa-solid fa-trash-can"></i> Delete
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
