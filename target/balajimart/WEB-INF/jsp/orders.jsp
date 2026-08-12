<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp4/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp4/jstl/fmt" %>

<c:set var="pageTitle" value="My Orders - BALAJIMART" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>

<div style="margin-bottom: 24px;">
    <h1 style="font-family: 'Outfit', sans-serif; font-size: 2.2rem; font-weight: 800;">My Orders</h1>
    <p style="color: var(--gray-500);">Track your previous purchases and order statuses</p>
</div>

<c:choose>
    <c:when test="${empty orders}">
        <div class="table-card" style="padding: 60px; text-align: center;">
            <i class="fa-solid fa-box-open" style="font-size: 3.5rem; color: var(--gray-300); margin-bottom: 16px;"></i>
            <h3 style="font-size: 1.4rem; font-weight: 700; margin-bottom: 8px;">No Orders Placed Yet</h3>
            <p style="color: var(--gray-500); margin-bottom: 24px;">You haven't placed any orders with us yet.</p>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary"><i class="fa-solid fa-cart-plus"></i> Start Shopping</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-card">
            <table class="table">
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Date</th>
                        <th>Total Amount</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${orders}" var="ord">
                        <tr>
                            <td style="font-weight: 700;">#${ord.id}</td>
                            <td><fmt:formatDate value="${ord.createdAt}" pattern="dd MMM yyyy, hh:mm a"/></td>
                            <td style="font-weight: 700; color: var(--primary);">₹<fmt:formatNumber value="${ord.totalAmount}" pattern="#,##0.00"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${ord.status == 'COMPLETED'}">
                                        <span class="status-pill status-completed">Completed</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-pill status-pending">${ord.status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/order?id=${ord.id}" class="btn btn-outline btn-sm">
                                    <i class="fa-solid fa-eye"></i> Details
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
