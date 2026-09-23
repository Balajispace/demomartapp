<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="isEdit" value="${formAction == 'edit'}"/>
<c:set var="pageTitle" value="${isEdit ? 'Edit Product' : 'Add New Product'} - Seller BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 20px;">
    <a href="${pageContext.request.contextPath}/seller/products" class="btn btn-outline btn-sm"><i class="fa-solid fa-arrow-left"></i> Back to My Products</a>
</div>

<div class="auth-wrapper" style="max-width: 650px;">
    <div class="auth-card" style="padding: 36px;">
        <h2 class="auth-title" style="text-align: left;">
            <i class="fa-solid ${isEdit ? 'fa-pen-to-square' : 'fa-plus-circle'}"></i> ${isEdit ? 'Edit Product' : 'Add New Product'}
        </h2>
        <p class="auth-subtitle" style="text-align: left; margin-bottom: 24px;">Enter details for your product listing</p>

        <form action="${pageContext.request.contextPath}/seller/products/${isEdit ? 'edit' : 'add'}" method="post">
            <c:if test="${isEdit}">
                <input type="hidden" name="id" value="${product.id}">
            </c:if>

            <div class="form-group">
                <label class="form-label">Product Name *</label>
                <input type="text" name="name" class="form-control" placeholder="e.g. Wireless Noise-Cancelling Headphones" value="${product.name}" required>
            </div>

            <div class="form-group">
                <label class="form-label">Description</label>
                <textarea name="description" class="form-control" rows="4" placeholder="Detailed product specifications and highlights...">${product.description}</textarea>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                <div class="form-group">
                    <label class="form-label">Price (₹) *</label>
                    <input type="number" step="0.01" min="0" name="price" class="form-control" placeholder="1299.00" value="${product.price}" required>
                </div>

                <div class="form-group">
                    <label class="form-label">Stock Quantity *</label>
                    <input type="number" min="0" name="stock" class="form-control" placeholder="25" value="${product.stock != null ? product.stock : 10}" required>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">Category *</label>
                <select name="category" class="form-control" required>
                    <option value="Electronics" ${product.category == 'Electronics' ? 'selected' : ''}>Electronics</option>
                    <option value="Books" ${product.category == 'Books' ? 'selected' : ''}>Books</option>
                    <option value="Clothing" ${product.category == 'Clothing' ? 'selected' : ''}>Clothing</option>
                    <option value="Accessories" ${product.category == 'Accessories' ? 'selected' : ''}>Accessories</option>
                    <option value="Home" ${product.category == 'Home' ? 'selected' : ''}>Home</option>
                </select>
            </div>

            <div class="form-group">
                <label class="form-label">Image URL</label>
                <input type="url" name="imageUrl" class="form-control" placeholder="https://images.unsplash.com/photo-..." value="${product.imageUrl}">
            </div>

            <div style="display: flex; gap: 16px; margin-top: 10px;">
                <button type="submit" class="btn btn-primary" style="flex: 1; padding: 12px;">
                    <i class="fa-solid fa-check"></i> ${isEdit ? 'Save Changes' : 'List Product'}
                </button>
                <a href="${pageContext.request.contextPath}/seller/products" class="btn btn-outline" style="padding: 12px 24px;">Cancel</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
