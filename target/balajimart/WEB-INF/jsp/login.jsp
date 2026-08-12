<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp4/jstl/core" %>

<c:set var="pageTitle" value="Sign In - BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div class="auth-wrapper">
    <div class="auth-card">
        <h2 class="auth-title">Welcome Back</h2>
        <p class="auth-subtitle">Sign in to your BALAJIMART account</p>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label class="form-label">Email Address</label>
                <input type="email" name="email" class="form-control" placeholder="name@example.com" value="${email}" required>
            </div>

            <div class="form-group">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; padding: 12px; margin-top: 10px;">
                <i class="fa-solid fa-right-to-bracket"></i> Sign In
            </button>
        </form>

        <div style="margin-top: 24px; text-align: center; font-size: 0.9rem; color: var(--gray-500);">
            Don't have an account? <a href="${pageContext.request.contextPath}/register" style="color: var(--primary); font-weight: 700; text-decoration: none;">Register Now</a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
