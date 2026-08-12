<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Checkout - BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 24px;">
    <h1 style="font-family: 'Outfit', sans-serif; font-size: 2.2rem; font-weight: 800;">Checkout</h1>
    <p style="color: var(--gray-500);">Complete your order details below</p>
</div>

<form action="${pageContext.request.contextPath}/checkout" method="post">
    <div style="display: grid; grid-template-columns: 3fr 2fr; gap: 30px;">
        <div>
            <div class="auth-card" style="padding: 30px; margin-bottom: 24px;">
                <h3 style="font-family: 'Outfit', sans-serif; font-size: 1.3rem; font-weight: 800; margin-bottom: 20px; border-bottom: 1px solid var(--gray-200); padding-bottom: 10px;">
                    <i class="fa-solid fa-truck"></i> Shipping Information
                </h3>

                <div class="form-group">
                    <label class="form-label">Full Name</label>
                    <input type="text" class="form-control" value="${sessionScope.user.name}" required readonly style="background: var(--gray-100);">
                </div>

                <div class="form-group">
                    <label class="form-label">Email Address</label>
                    <input type="email" class="form-control" value="${sessionScope.user.email}" required readonly style="background: var(--gray-100);">
                </div>

                <div class="form-group">
                    <label class="form-label">Shipping Address</label>
                    <textarea class="form-control" rows="3" placeholder="Enter full delivery street address..." required>123 High Street, Electronic City, Bengaluru, KA - 560100</textarea>
                </div>
            </div>

            <div class="auth-card" style="padding: 30px;">
                <h3 style="font-family: 'Outfit', sans-serif; font-size: 1.3rem; font-weight: 800; margin-bottom: 20px; border-bottom: 1px solid var(--gray-200); padding-bottom: 10px;">
                    <i class="fa-solid fa-credit-card"></i> Payment Method
                </h3>

                <div style="display: flex; flex-direction: column; gap: 12px;">
                    <label style="display: flex; align-items: center; gap: 12px; padding: 14px; border: 1.5px solid var(--primary); border-radius: var(--radius-md); background: var(--primary-light); cursor: pointer; font-weight: 700;">
                        <input type="radio" name="paymentMethod" value="COD" checked>
                        <i class="fa-solid fa-money-bill-wave" style="color: var(--primary); font-size: 1.2rem;"></i>
                        Cash on Delivery (COD) / Pay on Arrival
                    </label>

                    <label style="display: flex; align-items: center; gap: 12px; padding: 14px; border: 1px solid var(--gray-300); border-radius: var(--radius-md); cursor: pointer; font-weight: 600;">
                        <input type="radio" name="paymentMethod" value="UPI">
                        <i class="fa-solid fa-qrcode" style="color: var(--gray-600); font-size: 1.2rem;"></i>
                        UPI / QR Code Scan
                    </label>
                </div>
            </div>
        </div>

        <div>
            <div class="auth-card" style="padding: 30px;">
                <h3 style="font-family: 'Outfit', sans-serif; font-size: 1.3rem; font-weight: 800; margin-bottom: 20px; border-bottom: 1px solid var(--gray-200); padding-bottom: 10px;">Items Review</h3>

                <div style="display: flex; flex-direction: column; gap: 14px; margin-bottom: 20px; max-height: 280px; overflow-y: auto;">
                    <c:forEach items="${cartItems}" var="item">
                        <div style="display: flex; justify-content: space-between; align-items: center; font-size: 0.95rem;">
                            <div>
                                <div style="font-weight: 700;">${item.product.name}</div>
                                <div style="font-size: 0.8rem; color: var(--gray-500);">Qty: ${item.quantity} &times; ₹<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00"/></div>
                            </div>
                            <div style="font-weight: 700; color: var(--gray-900);">
                                ₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div style="border-top: 1px dashed var(--gray-300); padding-top: 16px; margin-bottom: 24px;">
                    <div style="display: flex; justify-content: space-between; font-size: 1.4rem; font-weight: 800; color: var(--gray-900);">
                        <span>Total Amount</span>
                        <span style="color: var(--primary);">₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; padding: 14px; font-size: 1.1rem;">
                    <i class="fa-solid fa-lock"></i> Place Order Now
                </button>
            </div>
        </div>
    </div>
</form>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
