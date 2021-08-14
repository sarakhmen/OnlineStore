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

<hr>
<h3>${sessionScope.username}</h3>
<h4>${sessionScope.role}</h4>
<form action="../logout">
  <input type="submit" onclick="return confirmLogOut()" value="Log out"/>
</form>

<form action="management">
  <input type="submit" value="Management"/>
</form>
<hr/>

<h2>User id=${sessionScope.cartUserId} orders:</h2>
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
          <form action="../deleteOrder" method="post">
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
            <button <c:if test="${order.status == 'Registered'}"> disabled </c:if> />
            Register
            </button>
          </form>
        </td>
        <td>
          <form action="orderStatus" method="post">
            <input type="hidden" name="orderId" value="${order.id}"/>
            <input type="hidden" name="orderStatus" value="Paid"/>
            <button <c:if test="${order.status == 'Paid'}"> disabled </c:if> />
            Paid
            </button>
          </form>
        </td>
        <td>
          <form action="orderStatus" method="post">
            <input type="hidden" name="orderId" value="${order.id}"/>
            <input type="hidden" name="orderStatus" value="Canceled"/>
            <button <c:if test="${order.status == 'Canceled'}"> disabled </c:if> />
            Cancel
            </button>
          </form>
        </td>
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