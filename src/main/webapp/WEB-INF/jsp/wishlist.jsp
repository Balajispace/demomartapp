<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="My Wishlist - BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 24px;">
    <h1 style="font-family: 'Outfit', sans-serif; font-size: 2.2rem; font-weight: 800;">
        <i class="fa-solid fa-heart" style="color: #ef4444;"></i> My Wishlist
    </h1>
    <p style="color: var(--gray-500);">Your saved items for future shopping</p>
</div>

<c:choose>
    <c:when test="${empty wishlistItems}">
        <div class="table-card" style="padding: 60px; text-align: center;">
            <i class="fa-regular fa-heart" style="font-size: 3.5rem; color: var(--gray-300); margin-bottom: 16px;"></i>
            <h3 style="font-size: 1.4rem; font-weight: 700; margin-bottom: 8px;">Your Wishlist is Empty</h3>
            <p style="color: var(--gray-500); margin-bottom: 20px;">Save items you love to your wishlist and shop them later.</p>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary"><i class="fa-solid fa-bag-shopping"></i> Browse Products</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="product-grid">
            <c:forEach items="${wishlistItems}" var="item">
                <c:set var="p" value="${item.product}"/>
                <div class="product-card">
                    <div class="product-img-wrap">
                        <img src="${p.imageUrl}" alt="${p.name}" class="product-img" onerror="this.src='https://images.unsplash.com/photo-1560343090-f0409e92791a?w=600'">
                        <c:choose>
                            <c:when test="${p.stock > 0}">
                                <span class="stock-tag">${p.stock} in stock</span>
                            </c:when>
                            <c:otherwise>
                                <span class="stock-tag out">Out of Stock</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="product-body">
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px;">
                            <span class="product-category">${p.category}</span>
                            <c:if test="${not empty p.sellerName}">
                                <span style="font-size: 0.75rem; color: var(--gray-500); font-weight: 600;">
                                    <i class="fa-solid fa-shop"></i> ${p.sellerName}
                                </span>
                            </c:if>
                        </div>

                        <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="product-name">${p.name}</a>
                        <p class="product-desc">${p.description}</p>

                        <div style="margin: 10px 0; display: flex; gap: 10px; align-items: center;">
                            <form action="${pageContext.request.contextPath}/wishlist/remove" method="post" style="display:inline;">
                                <input type="hidden" name="productId" value="${p.id}">
                                <button type="submit" class="btn btn-outline btn-sm" style="color: var(--danger); border-color: #fecaca;">
                                    <i class="fa-solid fa-trash-can"></i> Remove
                                </button>
                            </form>
                            <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="btn btn-outline btn-sm">
                                View Product
                            </a>
                        </div>

                        <div class="product-footer">
                            <span class="product-price">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></span>
                            <c:choose>
                                <c:when test="${p.stock > 0}">
                                    <form action="${pageContext.request.contextPath}/cart/add" method="post" style="display:inline;">
                                        <input type="hidden" name="productId" value="${p.id}">
                                        <input type="hidden" name="quantity" value="1">
                                        <button type="submit" class="btn btn-primary btn-sm"><i class="fa-solid fa-cart-plus"></i> Add to Cart</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-secondary btn-sm" disabled>Out of Stock</button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
