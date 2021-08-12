<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
</head>
<hr>
<h3>${sessionScope.username}</h3>
<h4>${sessionScope.role}</h4>
<c:choose>
    <c:when test="${sessionScope.role != 'GUEST'}">
        <form action="main/logout">
            <input type="submit" onclick="return confirmLogOut()" value="Log out"/>
        </form>
    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/login.jsp">
            <input type="submit" value="Log in"/>
        </form>
    </c:otherwise>
</c:choose>

<form action="${pageContext.request.contextPath}/WEB-INF/cart.jsp">
        <input type="submit" value="Cart"/>
</form>


<script>
    function confirmLogOut() {
        if (confirm("Are you sure?")) {
            return true;
        } else {
            return false;
        }
    }
</script>
<hr/>
</body>
</html>