<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Create Account - BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div class="auth-wrapper">
    <div class="auth-card">
        <h2 class="auth-title">Create Account</h2>
        <p class="auth-subtitle">Join BALAJIMART as a Buyer or Seller</p>

        <c:set var="currentRole" value="${not empty selectedRole ? selectedRole : 'BUYER'}"/>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="form-group">
                <label class="form-label">I want to register as:</label>
                <div style="display: flex; gap: 16px;">
                    <label style="flex: 1; border: 1.5px solid ${currentRole == 'BUYER' ? 'var(--primary)' : 'var(--gray-300)'}; padding: 12px; border-radius: var(--radius-md); text-align: center; cursor: pointer; background: ${currentRole == 'BUYER' ? 'var(--primary-light)' : '#ffffff'}; font-weight: 700; color: ${currentRole == 'BUYER' ? 'var(--primary)' : 'var(--gray-700)'}; transition: all 0.2s ease;">
                        <input type="radio" name="role" value="BUYER" ${currentRole == 'BUYER' ? 'checked' : ''} style="margin-right: 6px;" onchange="this.form.submit()">
                        <i class="fa-solid fa-cart-shopping"></i> Buyer
                    </label>
                    <label style="flex: 1; border: 1.5px solid ${currentRole == 'SELLER' ? 'var(--primary)' : 'var(--gray-300)'}; padding: 12px; border-radius: var(--radius-md); text-align: center; cursor: pointer; background: ${currentRole == 'SELLER' ? 'var(--primary-light)' : '#ffffff'}; font-weight: 700; color: ${currentRole == 'SELLER' ? 'var(--primary)' : 'var(--gray-700)'}; transition: all 0.2s ease;">
                        <input type="radio" name="role" value="SELLER" ${currentRole == 'SELLER' ? 'checked' : ''} style="margin-right: 6px;" onchange="this.form.submit()">
                        <i class="fa-solid fa-store"></i> Seller
                    </label>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">Full Name</label>
                <input type="text" name="name" class="form-control" placeholder="John Doe" value="${name}" required>
            </div>

            <div class="form-group">
                <label class="form-label">Email Address</label>
                <input type="email" name="email" class="form-control" placeholder="name@example.com" value="${email}" required>
            </div>

            <div class="form-group">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>

            <div class="form-group">
                <label class="form-label">Confirm Password</label>
                <input type="password" name="confirmPassword" class="form-control" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; padding: 12px; margin-top: 10px;">
                <i class="fa-solid fa-user-plus"></i> Register as ${currentRole}
            </button>
        </form>

        <div style="margin-top: 24px; text-align: center; font-size: 0.9rem; color: var(--gray-500);">
            Already have an account? <a href="${pageContext.request.contextPath}/login?role=${currentRole}" style="color: var(--primary); font-weight: 700; text-decoration: none;">Sign In</a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
