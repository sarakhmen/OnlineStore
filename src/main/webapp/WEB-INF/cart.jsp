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
    <table style="float: left">
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
                        <input type="hidden" name="orderId" value="${order.id}"\>
                        <button>
                            Delete
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>