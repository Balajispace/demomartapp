<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="BALAJIMART - Fast, Reliable E-Commerce Shopping" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<section class="hero">
    <div class="hero-content">
        <span class="hero-tag"><i class="fa-solid fa-bolt"></i> Mega Electronics & Books Sale</span>
        <h1 class="hero-title">Upgrade Your Tech & Everyday Style</h1>
        <p class="hero-subtitle">Discover high quality products with instant delivery, verified reviews, and secure seamless checkout.</p>
        <div style="display: flex; gap: 14px;">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary"><i class="fa-solid fa-bag-shopping"></i> Shop Now</a>
            <a href="${pageContext.request.contextPath}/products?category=Electronics" class="btn btn-secondary">Browse Electronics</a>
        </div>
    </div>
</section>

<!-- Category Pills -->
<div class="category-pills">
    <a href="${pageContext.request.contextPath}/products" class="category-pill active">All Products</a>
    <a href="${pageContext.request.contextPath}/products?category=Electronics" class="category-pill">Electronics</a>
    <a href="${pageContext.request.contextPath}/products?category=Books" class="category-pill">Books</a>
    <a href="${pageContext.request.contextPath}/products?category=Clothing" class="category-pill">Clothing</a>
    <a href="${pageContext.request.contextPath}/products?category=Accessories" class="category-pill">Accessories</a>
    <a href="${pageContext.request.contextPath}/products?category=Home" class="category-pill">Home</a>
</div>

<!-- Featured Products Section -->
<div class="section-header">
    <h2 class="section-title">Featured Products</h2>
    <a href="${pageContext.request.contextPath}/products" class="btn btn-outline btn-sm">View All <i class="fa-solid fa-arrow-right"></i></a>
</div>

<div class="product-grid">
    <c:forEach items="${featuredProducts}" var="p">
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
                <span class="product-category">${p.category}</span>
                <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="product-name">${p.name}</a>
                <p class="product-desc">${p.description}</p>
                <div class="product-footer">
                    <span class="product-price">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></span>
                    <c:choose>
                        <c:when test="${p.stock > 0}">
                            <form action="${pageContext.request.contextPath}/cart/add" method="post" style="display:inline;">
                                <input type="hidden" name="productId" value="${p.id}">
                                <input type="hidden" name="quantity" value="1">
                                <button type="submit" class="btn btn-primary btn-sm"><i class="fa-solid fa-cart-plus"></i> Add</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-secondary btn-sm" disabled>Unavailable</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
