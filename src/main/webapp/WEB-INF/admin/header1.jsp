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
        <form action="logout">
            <input type="submit" onclick="return confirmLogOut()" value="Log out"/>
        </form>

<form action="cartView">
    <input type="hidden" name="userId" value="${sessionScope.userId}"/>
    <input type="submit" value="Cart"/>
</form>

<form action="admin/management">
    <input type="submit" value="Management"/>
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