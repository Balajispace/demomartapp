<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp4/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp4/jstl/fmt" %>

<c:set var="pageTitle" value="Products Catalog - BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 24px;">
    <h1 style="font-family: 'Outfit', sans-serif; font-size: 2.2rem; font-weight: 800;">
        <c:choose>
            <c:when test="${not empty searchQuery}">Search Results for "${searchQuery}"</c:when>
            <c:when test="${not empty selectedCategory}">${selectedCategory} Products</c:when>
            <c:otherwise>All Products</c:otherwise>
        </c:choose>
    </h1>
    <p style="color: var(--gray-500);">Explore our collection of top-rated items</p>
</div>

<!-- Category Pills -->
<div class="category-pills">
    <a href="${pageContext.request.contextPath}/products" class="category-pill ${empty selectedCategory ? 'active' : ''}">All</a>
    <a href="${pageContext.request.contextPath}/products?category=Electronics" class="category-pill ${selectedCategory == 'Electronics' ? 'active' : ''}">Electronics</a>
    <a href="${pageContext.request.contextPath}/products?category=Books" class="category-pill ${selectedCategory == 'Books' ? 'active' : ''}">Books</a>
    <a href="${pageContext.request.contextPath}/products?category=Clothing" class="category-pill ${selectedCategory == 'Clothing' ? 'active' : ''}">Clothing</a>
    <a href="${pageContext.request.contextPath}/products?category=Accessories" class="category-pill ${selectedCategory == 'Accessories' ? 'active' : ''}">Accessories</a>
    <a href="${pageContext.request.contextPath}/products?category=Home" class="category-pill ${selectedCategory == 'Home' ? 'active' : ''}">Home</a>
</div>

<c:choose>
    <c:when test="${empty products}">
        <div class="table-card" style="padding: 60px; text-align: center;">
            <i class="fa-solid fa-box-open" style="font-size: 3rem; color: var(--gray-300); margin-bottom: 16px;"></i>
            <h3 style="font-size: 1.4rem; font-weight: 700; margin-bottom: 8px;">No Products Found</h3>
            <p style="color: var(--gray-500); margin-bottom: 20px;">We couldn't find any products matching your selection.</p>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Clear Filters</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="product-grid">
            <c:forEach items="${products}" var="p">
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
