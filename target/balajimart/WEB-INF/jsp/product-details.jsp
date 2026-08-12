<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp4/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp4/jstl/fmt" %>

<c:set var="pageTitle" value="${product.name} - BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 20px;">
    <a href="${pageContext.request.contextPath}/products" class="btn btn-outline btn-sm"><i class="fa-solid fa-arrow-left"></i> Back to Products</a>
</div>

<div class="table-card" style="padding: 30px; display: grid; grid-template-columns: 1fr 1fr; gap: 40px; align-items: start;">
    <div style="width: 100%; height: 380px; border-radius: var(--radius-md); overflow: hidden; background: var(--gray-100);">
        <img src="${product.imageUrl}" alt="${product.name}" style="width: 100%; height: 100%; object-fit: cover;" onerror="this.src='https://images.unsplash.com/photo-1560343090-f0409e92791a?w=600'">
    </div>

    <div>
        <span class="product-category">${product.category}</span>
        <h1 style="font-family: 'Outfit', sans-serif; font-size: 2.2rem; font-weight: 800; margin: 8px 0 16px 0;">${product.name}</h1>
        
        <div style="font-size: 2.2rem; font-weight: 800; color: var(--primary); margin-bottom: 20px;">
            ₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>
        </div>

        <p style="font-size: 1.05rem; color: var(--gray-700); margin-bottom: 24px; line-height: 1.7;">
            ${product.description}
        </p>

        <div style="margin-bottom: 24px;">
            <c:choose>
                <c:when test="${product.stock > 0}">
                    <span class="status-pill status-completed"><i class="fa-solid fa-check"></i> In Stock (${product.stock} available)</span>
                </c:when>
                <c:otherwise>
                    <span class="status-pill status-pending" style="background: var(--danger-light); color: var(--danger);"><i class="fa-solid fa-xmark"></i> Out of Stock</span>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${product.stock > 0}">
            <form action="${pageContext.request.contextPath}/cart/add" method="post">
                <input type="hidden" name="productId" value="${product.id}">
                <input type="hidden" name="redirect" value="product">
                <div style="display: flex; gap: 16px; align-items: center; margin-bottom: 24px;">
                    <label style="font-weight: 700;">Quantity:</label>
                    <input type="number" name="quantity" value="1" min="1" max="${product.stock}" class="form-control" style="width: 90px; text-align: center;">
                </div>
                <button type="submit" class="btn btn-primary" style="width: 100%; padding: 14px 24px; font-size: 1.1rem;">
                    <i class="fa-solid fa-cart-shopping"></i> Add to Cart
                </button>
            </form>
        </c:if>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
