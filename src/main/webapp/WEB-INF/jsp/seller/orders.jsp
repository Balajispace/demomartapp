<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Customer Orders - Seller BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 24px; display: flex; justify-content: space-between; align-items: center;">
    <div>
        <h1 style="font-family: 'Outfit', sans-serif; font-size: 2.2rem; font-weight: 800;">
            <i class="fa-solid fa-truck-ramp-box"></i> Sales & Orders
        </h1>
        <p style="color: var(--gray-500);">Customer purchases for products listed by your seller account</p>
    </div>
    <a href="${pageContext.request.contextPath}/seller/dashboard" class="btn btn-outline btn-sm">
        <i class="fa-solid fa-arrow-left"></i> Dashboard
    </a>
</div>

<c:choose>
    <c:when test="${empty salesItems}">
        <div class="table-card" style="padding: 60px; text-align: center;">
            <i class="fa-solid fa-receipt" style="font-size: 3.5rem; color: var(--gray-300); margin-bottom: 16px;"></i>
            <h3 style="font-size: 1.4rem; font-weight: 700; margin-bottom: 8px;">No Sales Yet</h3>
            <p style="color: var(--gray-500);">When customers order your products, purchase items will appear here.</p>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-card">
            <table class="table">
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Unit Price</th>
                        <th>Total Amount</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${salesItems}" var="item">
                        <tr>
                            <td><strong>#${item.orderId}</strong></td>
                            <td style="display: flex; align-items: center; gap: 12px;">
                                <img src="${item.productImage}" style="width: 40px; height: 40px; border-radius: var(--radius-sm); object-fit: cover;" onerror="this.src='https://images.unsplash.com/photo-1560343090-f0409e92791a?w=100'">
                                <span>${item.productName}</span>
                            </td>
                            <td>${item.quantity}</td>
                            <td>₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></td>
                            <td style="font-weight: 700; color: var(--primary);">
                                ₹<fmt:formatNumber value="${item.unitPrice * item.quantity}" pattern="#,##0.00"/>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
