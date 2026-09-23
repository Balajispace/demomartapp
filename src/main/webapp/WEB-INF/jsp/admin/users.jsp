<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="User Management - Admin BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 24px; display: flex; justify-content: space-between; align-items: center;">
    <div>
        <h1 style="font-family: 'Outfit', sans-serif; font-size: 2.2rem; font-weight: 800;">
            <i class="fa-solid fa-users-gear"></i> User Management
        </h1>
        <p style="color: var(--gray-500);">View, filter, activate or disable user accounts</p>
    </div>
    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline btn-sm">
        <i class="fa-solid fa-arrow-left"></i> Dashboard
    </a>
</div>

<!-- Role Filters -->
<div class="category-pills" style="margin-bottom: 20px;">
    <a href="${pageContext.request.contextPath}/admin/users?role=ALL" class="category-pill ${selectedRole == 'ALL' ? 'active' : ''}">All Users</a>
    <a href="${pageContext.request.contextPath}/admin/users?role=BUYER" class="category-pill ${selectedRole == 'BUYER' ? 'active' : ''}">Buyers</a>
    <a href="${pageContext.request.contextPath}/admin/users?role=SELLER" class="category-pill ${selectedRole == 'SELLER' ? 'active' : ''}">Sellers</a>
    <a href="${pageContext.request.contextPath}/admin/users?role=ADMIN" class="category-pill ${selectedRole == 'ADMIN' ? 'active' : ''}">Admins</a>
</div>

<div class="table-card">
    <table class="table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Status</th>
                <th>Registered At</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${users}" var="u">
                <tr>
                    <td>#${u.id}</td>
                    <td><strong>${u.name}</strong></td>
                    <td>${u.email}</td>
                    <td>
                        <c:choose>
                            <c:when test="${u.role == 'ADMIN'}">
                                <span class="role-badge role-badge-admin">ADMIN</span>
                            </c:when>
                            <c:when test="${u.role == 'SELLER'}">
                                <span class="role-badge role-badge-seller">SELLER</span>
                            </c:when>
                            <c:otherwise>
                                <span class="role-badge role-badge-buyer">BUYER</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${u.status == 'ACTIVE'}">
                                <span class="status-pill status-completed">ACTIVE</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-pill" style="background: #fef2f2; color: #dc2626;">DISABLED</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><fmt:formatDate value="${u.createdAt}" pattern="dd MMM yyyy, HH:mm"/></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/admin/users/status" method="post" style="display:inline;">
                            <input type="hidden" name="userId" value="${u.id}">
                            <c:choose>
                                <c:when test="${u.status == 'ACTIVE'}">
                                    <input type="hidden" name="status" value="DISABLED">
                                    <button type="submit" class="btn btn-outline btn-sm" style="color: var(--danger); border-color: #fecaca;" ${sessionScope.user.id == u.id ? 'disabled' : ''}>
                                        <i class="fa-solid fa-user-slash"></i> Disable
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="status" value="ACTIVE">
                                    <button type="submit" class="btn btn-primary btn-sm">
                                        <i class="fa-solid fa-user-check"></i> Activate
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
