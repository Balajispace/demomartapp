<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Sign In - BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div class="auth-wrapper">
    <div class="auth-card">
        <h2 class="auth-title">Welcome Back</h2>
        <p class="auth-subtitle">Select your account type to sign in</p>

        <c:set var="activeTab" value="${not empty selectedRole ? selectedRole : 'BUYER'}"/>

        <div class="role-switcher">
            <a href="${pageContext.request.contextPath}/login?role=BUYER" class="role-tab ${activeTab == 'BUYER' ? 'active' : ''}">
                <i class="fa-solid fa-user"></i> Buyer Login
            </a>
            <a href="${pageContext.request.contextPath}/login?role=SELLER" class="role-tab ${activeTab == 'SELLER' ? 'active' : ''}">
                <i class="fa-solid fa-shop"></i> Seller Login
            </a>
            <a href="${pageContext.request.contextPath}/login?role=ADMIN" class="role-tab ${activeTab == 'ADMIN' ? 'active' : ''}">
                <i class="fa-solid fa-shield-halved"></i> Admin Login
            </a>
        </div>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <input type="hidden" name="role" value="${activeTab}">

            <div class="form-group">
                <label class="form-label">Email Address</label>
                <input type="email" name="email" class="form-control" placeholder="name@example.com" value="${email}" required>
            </div>

            <div class="form-group">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; padding: 12px; margin-top: 10px;">
                <i class="fa-solid fa-right-to-bracket"></i> Sign In as ${activeTab}
            </button>
        </form>

        <div style="margin-top: 24px; text-align: center; font-size: 0.9rem; color: var(--gray-500);">
            Don't have an account? <a href="${pageContext.request.contextPath}/register?role=${activeTab == 'SELLER' ? 'SELLER' : 'BUYER'}" style="color: var(--primary); font-weight: 700; text-decoration: none;">Register Now</a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
