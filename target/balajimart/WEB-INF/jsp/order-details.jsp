<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp4/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp4/jstl/fmt" %>

<c:set var="pageTitle" value="Order #${order.id} Receipt - BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 20px;">
    <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline btn-sm"><i class="fa-solid fa-arrow-left"></i> Back to Orders</a>
</div>

<div class="auth-card" style="max-width: 800px; margin: 0 auto 30px auto; padding: 40px;">
    <div style="display: flex; justify-content: space-between; align-items: flex-start; border-bottom: 1px solid var(--gray-200); padding-bottom: 20px; margin-bottom: 24px;">
        <div>
            <h1 style="font-family: 'Outfit', sans-serif; font-size: 2rem; font-weight: 800;">Order #${order.id}</h1>
            <p style="color: var(--gray-500); font-size: 0.9rem;">Placed on <fmt:formatDate value="${order.createdAt}" pattern="MMMM dd, yyyy 'at' hh:mm a"/></p>
        </div>
        <div>
            <span class="status-pill ${order.status == 'COMPLETED' ? 'status-completed' : 'status-pending'}">${order.status}</span>
        </div>
    </div>

    <h3 style="font-family: 'Outfit', sans-serif; font-size: 1.2rem; font-weight: 700; margin-bottom: 16px;">Items Ordered</h3>

    <div style="display: flex; flex-direction: column; gap: 16px; margin-bottom: 30px;">
        <c:forEach items="${order.items}" var="item">
            <div style="display: flex; align-items: center; justify-content: space-between; padding: 14px; background: var(--gray-50); border-radius: var(--radius-md);">
                <div style="display: flex; align-items: center; gap: 16px;">
                    <img src="${item.productImage}" alt="${item.productName}" style="width: 50px; height: 50px; border-radius: var(--radius-sm); object-fit: cover;" onerror="this.src='https://images.unsplash.com/photo-1560343090-f0409e92791a?w=600'">
                    <div>
                        <div style="font-weight: 700;">${item.productName}</div>
                        <div style="font-size: 0.85rem; color: var(--gray-500);">Qty: ${item.quantity} &times; ₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></div>
                    </div>
                </div>
                <div style="font-size: 1.1rem; font-weight: 800; color: var(--gray-900);">
                    ₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                </div>
            </div>
        </c:forEach>
    </div>

    <div style="border-top: 1px dashed var(--gray-300); padding-top: 20px; display: flex; justify-content: space-between; align-items: center;">
        <span style="font-size: 1.2rem; font-weight: 700;">Grand Total</span>
        <span style="font-size: 2rem; font-weight: 800; color: var(--primary);">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
