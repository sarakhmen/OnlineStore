<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title>Cart page</title>
    <style type="text/css">
        TABLE {
            width: 600px;
            border-collapse: collapse;
        }

        TD, TH {
            padding: 3px;
            border: 1px solid black;
        }

        TH {
            background: #b0e0e6;
        }
    </style>
</head>
<body>
<c:import url="header.jsp"/>
<h2>Your orders:</h2>
<div>
    <table>
        <tr>
            <th>Order id</th>
            <th>Product name</th>
            <th>Price</th>
            <th>Status</th>
        </tr>
        <c:forEach items="${sessionScope.orders}" var="order">
            <tr>
                <td><c:out value="${order.id}"/></td>
                <td><c:out value="${order.name}"/></td>
                <td><c:out value="${order.price}"/></td>
                <td><c:out value="${order.status}"/></td>
                <td>
                    <form action="deleteOrder" method="post">
                        <input type="hidden" name="orderId" value="${order.id}"/>
                        <button>
                            Delete
                        </button>
                    </form>
                </td>
                <td>
                    <form action="orderStatus" method="post">
                        <input type="hidden" name="orderId" value="${order.id}"/>
                        <input type="hidden" name="orderStatus" value="Registered"/>

                        <button <c:if test="${order.status != 'Unregistered'}"> disabled </c:if> />
                            Order
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>

    <%--For displaying Previous link except for the 1st page --%>
    <c:if test="${requestScope.currentPage != 1}">
        <td><a href="${pageContext.request.contextPath}/main/cartView?page=${requestScope.currentPage - 1}">Previous</a></td>
    </c:if>

    <%--For displaying Page numbers.
    The when condition does not display a link for the current page--%>
    <table border="1" cellpadding="5" cellspacing="5">
        <tr>
            <c:forEach begin="1" end="${requestScope.numberOfPages}" var="i">
                <c:choose>
                    <c:when test="${requestScope.currentPage eq i}">
                        <td>${i}</td>
                    </c:when>
                    <c:otherwise>
                        <td><a href="${pageContext.request.contextPath}/main/cartView?page=${i}">${i}</a></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
    </table>

    <%--For displaying Next link --%>
    <c:if test="${requestScope.currentPage lt requestScope.numberOfPages}">
        <td><a href="${pageContext.request.contextPath}/main/cartView?page=${requestScope.currentPage + 1}">Next</a></td>
    </c:if>
</div>

</body>
</html>