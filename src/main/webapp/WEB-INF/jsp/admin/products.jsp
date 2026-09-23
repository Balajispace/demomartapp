<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Product Management - Admin BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 24px; display: flex; justify-content: space-between; align-items: center;">
    <div>
        <h1 style="font-family: 'Outfit', sans-serif; font-size: 2.2rem; font-weight: 800;">
            <i class="fa-solid fa-boxes-packing"></i> Product Management
        </h1>
        <p style="color: var(--gray-500);">Approve, reject, or manage all seller and platform listings</p>
    </div>
    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline btn-sm">
        <i class="fa-solid fa-arrow-left"></i> Dashboard
    </a>
</div>

<div class="table-card">
    <table class="table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Product Name</th>
                <th>Category</th>
                <th>Price</th>
                <th>Stock</th>
                <th>Seller</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${products}" var="p">
                <tr>
                    <td>#${p.id}</td>
                    <td style="display: flex; align-items: center; gap: 12px;">
                        <img src="${p.imageUrl}" style="width: 44px; height: 44px; border-radius: var(--radius-sm); object-fit: cover;" onerror="this.src='https://images.unsplash.com/photo-1560343090-f0409e92791a?w=100'">
                        <div>
                            <strong>${p.name}</strong>
                            <div style="font-size: 0.78rem; color: var(--gray-500); max-width: 250px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                                ${p.description}
                            </div>
                        </div>
                    </td>
                    <td>${p.category}</td>
                    <td>₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                    <td>${p.stock}</td>
                    <td>${not empty p.sellerName ? p.sellerName : 'System Admin'}</td>
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
                        <div style="display: flex; gap: 6px; align-items: center;">
                            <c:if test="${p.status != 'APPROVED'}">
                                <form action="${pageContext.request.contextPath}/admin/products/status" method="post" style="display:inline;">
                                    <input type="hidden" name="productId" value="${p.id}">
                                    <input type="hidden" name="status" value="APPROVED">
                                    <button type="submit" class="btn btn-primary btn-sm"><i class="fa-solid fa-check"></i> Approve</button>
                                </form>
                            </c:if>
                            <c:if test="${p.status != 'REJECTED'}">
                                <form action="${pageContext.request.contextPath}/admin/products/status" method="post" style="display:inline;">
                                    <input type="hidden" name="productId" value="${p.id}">
                                    <input type="hidden" name="status" value="REJECTED">
                                    <button type="submit" class="btn btn-outline btn-sm" style="color: #dc2626; border-color: #fecaca;"><i class="fa-solid fa-ban"></i> Reject</button>
                                </form>
                            </c:if>
                            <form action="${pageContext.request.contextPath}/admin/products/delete" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this product?');">
                                <input type="hidden" name="productId" value="${p.id}">
                                <button type="submit" class="btn btn-outline btn-sm" style="color: var(--gray-500);"><i class="fa-solid fa-trash"></i></button>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
