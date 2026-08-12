<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp4/jstl/core" %>

<c:set var="pageTitle" value="404 Page Not Found - BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div class="table-card" style="padding: 80px 20px; text-align: center; max-width: 600px; margin: 40px auto;">
    <div style="font-size: 5rem; font-weight: 800; color: var(--primary); font-family: 'Outfit', sans-serif;">404</div>
    <h2 style="font-size: 1.8rem; font-weight: 800; margin-bottom: 12px;">Page Not Found</h2>
    <p style="color: var(--gray-500); margin-bottom: 28px;">The page you are looking for might have been removed, had its name changed, or is temporarily unavailable.</p>
    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary"><i class="fa-solid fa-house"></i> Return Home</a>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
