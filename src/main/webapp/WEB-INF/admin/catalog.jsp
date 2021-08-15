<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title><fmt:message key="view.catalogPage"/></title>
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

<hr>
<c:import url="header.jsp"/>
<h4>${sessionScope.role}</h4>
<form action="logout">
    <input type="submit" onclick="return confirmLogOut()" value="Log out"/>
</form>

<form action="admin/cartView">
    <c:set var="cartUserId" value="${sessionScope.userId}"/>
    <input type="submit" value="Cart"/>
</form>

<form action="admin/management">
    <input type="submit" value="Management"/>
</form>
<hr/>

<h2>MAIN PAGE CONTENT</h2>
<form action="admin/addProductView">
    <input type="submit" value="Add new product"/>
</form>
<div>
    <table style="float: left">
        <tr>
            <th>Product name</th>
            <th>Price</th>
            <th>Creation date</th>
            <th>Properties</th>
            <th></th>
        </tr>
        <c:forEach items="${sessionScope.products}" var="product">
            <tr>
                <td><c:out value="${product.name}"/></td>
                <td><c:out value="${product.price}"/></td>
                <td><c:out value="${product.creationDate}"/></td>
                <td>
                    <c:forEach items="${product.properties}" var="property">
                        <c:out value="${property.key}: ${property.value}"/>
                        <br>
                    </c:forEach>
                </td>
                <td>
                    <form action="addToCart" method="post">
                        <input type="hidden" name="productId" value="${product.id}"/>
                        <button>
                            Order
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>

    <table style="float: left; width: 100px">
        <tr>
            <th>Property list</th>
        </tr>
        <c:forEach items="${sessionScope.productProperties}" var="property">
            <tr>
                <td><c:out value="${property}"/></td>
            </tr>
        </c:forEach>
    </table>
</div>

<script>
    function confirmLogOut() {
        if (confirm("Are you sure?")) {
            return true;
        } else {
            return false;
        }
    }
</script>

</body>
</html>