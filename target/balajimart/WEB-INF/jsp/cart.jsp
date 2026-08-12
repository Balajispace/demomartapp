<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp4/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp4/jstl/fmt" %>

<c:set var="pageTitle" value="Shopping Cart - BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 24px;">
    <h1 style="font-family: 'Outfit', sans-serif; font-size: 2.2rem; font-weight: 800;">Shopping Cart</h1>
    <p style="color: var(--gray-500);">Review and adjust your selected items before checkout</p>
</div>

<c:choose>
    <c:when test="${empty cartItems}">
        <div class="table-card" style="padding: 60px; text-align: center;">
            <i class="fa-solid fa-cart-shopping" style="font-size: 3.5rem; color: var(--gray-300); margin-bottom: 16px;"></i>
            <h3 style="font-size: 1.4rem; font-weight: 700; margin-bottom: 8px;">Your Shopping Cart is Empty</h3>
            <p style="color: var(--gray-500); margin-bottom: 24px;">Looks like you haven't added any items to your cart yet.</p>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary"><i class="fa-solid fa-store"></i> Start Shopping</a>
        </div>
    </c:when>
    <c:otherwise>
        <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 30px;">
            <div class="table-card">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Subtotal</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${cartItems}" var="item">
                            <tr>
                                <td>
                                    <div style="display: flex; align-items: center; gap: 16px;">
                                        <img src="${item.product.imageUrl}" alt="${item.product.name}" style="width: 54px; height: 54px; border-radius: var(--radius-sm); object-fit: cover;" onerror="this.src='https://images.unsplash.com/photo-1560343090-f0409e92791a?w=600'">
                                        <div>
                                            <a href="${pageContext.request.contextPath}/product?id=${item.product.id}" style="font-weight: 700; color: var(--gray-900); text-decoration: none;">${item.product.name}</a>
                                            <div style="font-size: 0.8rem; color: var(--gray-500);">${item.product.category}</div>
                                        </div>
                                    </div>
                                </td>
                                <td>₹<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00"/></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/cart/update" method="post" style="display: flex; align-items: center; gap: 8px;">
                                        <input type="hidden" name="cartItemId" value="${item.id}">
                                        <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.product.stock}" class="form-control" style="width: 70px; padding: 6px; text-align: center;">
                                        <button type="submit" class="btn btn-outline btn-sm" title="Update Quantity"><i class="fa-solid fa-rotate"></i></button>
                                    </form>
                                </td>
                                <td style="font-weight: 700; color: var(--primary);">
                                    ₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/cart/remove" method="post">
                                        <input type="hidden" name="cartItemId" value="${item.id}">
                                        <button type="submit" class="btn btn-danger btn-sm" title="Remove"><i class="fa-solid fa-trash-can"></i></button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div>
                <div class="auth-card" style="padding: 30px;">
                    <h3 style="font-family: 'Outfit', sans-serif; font-size: 1.4rem; font-weight: 800; margin-bottom: 20px; border-bottom: 1px solid var(--gray-200); padding-bottom: 12px;">Order Summary</h3>
                    
                    <div style="display: flex; justify-content: space-between; margin-bottom: 12px; color: var(--gray-700);">
                        <span>Subtotal</span>
                        <span>₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
                    </div>

                    <div style="display: flex; justify-content: space-between; margin-bottom: 12px; color: var(--gray-700);">
                        <span>Shipping</span>
                        <span style="color: var(--success); font-weight: 700;">FREE</span>
                    </div>

                    <div style="display: flex; justify-content: space-between; font-size: 1.4rem; font-weight: 800; color: var(--gray-900); margin: 20px 0; border-top: 1px dashed var(--gray-300); padding-top: 16px;">
                        <span>Total</span>
                        <span style="color: var(--primary);">₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
                    </div>

                    <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary" style="width: 100%; padding: 14px; font-size: 1.05rem;">
                        Proceed to Checkout <i class="fa-solid fa-arrow-right"></i>
                    </a>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
