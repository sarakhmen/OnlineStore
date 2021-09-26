<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title><fmt:message key="cartPage"/></title>
</head>
<body>

<div class="container">
    <c:import url="header.jsp"/>
    <div class="row mx-3 pt-4 gx-5">
        <div class="col fs-5">
            <fmt:message key="yourOrders"/>
        </div>
    </div>
    <div class="row mx-3 pt-4 gx-5">
        <div class="col">
            <table class="table">
                <thead>
                <tr>
                    <th scope="col"><fmt:message key="orderId"/></th>
                    <th scope="col"><fmt:message key="productName"/></th>
                    <th scope="col"><fmt:message key="price"/></th>
                    <th scope="col"><fmt:message key="status"/></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${sessionScope.orders}" var="order">
                    <tr>
                        <th scope="row"><c:out value="${order.id}"/></th>
                        <td><c:out value="${order.name}"/></td>
                        <td><c:out value="${order.price}"/></td>
                        <td><c:out value="${order.status}"/></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/main/deleteOrder" method="post">
                                <input type="hidden" name="orderId" value="${order.id}"/>
                                <button class="btn btn-outline-dark btn-sm">
                                    <fmt:message key="delete"/>
                                </button>
                            </form>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/main/orderStatus" method="post">
                                <input type="hidden" name="orderId" value="${order.id}"/>
                                <input type="hidden" name="orderStatus" value="Registered"/>

                                <button class="btn btn-outline-dark btn-sm" <c:if
                                        test="${order.status != 'Unregistered'}"> disabled </c:if> />
                                <fmt:message key="order"/>
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <div class="row mx-3 pt-4 gx-5">
        <div class="col-md-auto">
            <%--For displaying Previous link except for the 1st page --%>
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <c:if test="${requestScope.currentPage != 1}">
                        <li>
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/main/cartView?page=${requestScope.currentPage - 1}"><fmt:message key="previous"/></a>
                        </li>
                    </c:if>

                    <%--For displaying Page numbers.
                    The when condition does not display a link for the current page--%>
                    <c:forEach begin="1" end="${requestScope.numberOfPages}" var="i">
                        <c:choose>
                            <c:when test="${requestScope.currentPage eq i}">
                                <li class="page-item active">
                                    <a class="page-link" aria-disabled="true">${i}</a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li>
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/main/cartView?page=${i}">${i}</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <%--For displaying Next link --%>
                    <c:if test="${requestScope.currentPage lt requestScope.numberOfPages}">
                        <td>
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/main/cartView?page=${requestScope.currentPage + 1}"><fmt:message key="next"/></a>
                        </td>
                    </c:if>
                </ul>
            </nav>
        </div>
    </div>
</div>

</body>
</html>