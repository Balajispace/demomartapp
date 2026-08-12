<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp4/jstl/core" %>

<c:set var="pageTitle" value="Create Account - BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div class="auth-wrapper">
    <div class="auth-card">
        <h2 class="auth-title">Create Account</h2>
        <p class="auth-subtitle">Join BALAJIMART to start shopping</p>

        <form action="${pageContext.request.contextPath}/register" method="post">
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
                <i class="fa-solid fa-user-plus"></i> Register
            </button>
        </form>

        <div style="margin-top: 24px; text-align: center; font-size: 0.9rem; color: var(--gray-500);">
            Already have an account? <a href="${pageContext.request.contextPath}/login" style="color: var(--primary); font-weight: 700; text-decoration: none;">Sign In</a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
