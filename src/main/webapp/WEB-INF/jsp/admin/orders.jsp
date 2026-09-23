<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="All Orders - Admin BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 24px; display: flex; justify-content: space-between; align-items: center;">
    <div>
        <h1 style="font-family: 'Outfit', sans-serif; font-size: 2.2rem; font-weight: 800;">
            <i class="fa-solid fa-receipt"></i> Global Orders Log
        </h1>
        <p style="color: var(--gray-500);">Complete list of user transactions across the platform</p>
    </div>
    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline btn-sm">
        <i class="fa-solid fa-arrow-left"></i> Dashboard
    </a>
</div>

<c:choose>
    <c:when test="${empty orders}">
        <div class="table-card" style="padding: 60px; text-align: center;">
            <i class="fa-solid fa-box-open" style="font-size: 3rem; color: var(--gray-300); margin-bottom: 16px;"></i>
            <h3 style="font-size: 1.4rem; font-weight: 700;">No Orders Found</h3>
            <p style="color: var(--gray-500);">No transactions have been processed yet.</p>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-card">
            <table class="table">
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>User ID</th>
                        <th>Total Amount</th>
                        <th>Status</th>
                        <th>Date & Time</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${orders}" var="o">
                        <tr>
                            <td><strong>#${o.id}</strong></td>
                            <td>User #${o.userId}</td>
                            <td style="font-weight: 700; color: var(--primary);">₹<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></td>
                            <td>
                                <span class="status-pill status-completed">${o.status}</span>
                            </td>
                            <td><fmt:formatDate value="${o.createdAt}" pattern="dd MMM yyyy, HH:mm"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
