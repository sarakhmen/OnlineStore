<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>

<h3><c:choose>
    <c:when test="${sessionScope.locale == 'en'}">
        <c:out value="${sessionScope.usernameEn}"/>
    </c:when>
    <c:otherwise>
        <c:out value="${sessionScope.usernameUk}"/>
    </c:otherwise>
</c:choose></h3>
<h4>${sessionScope.role}</h4>
<c:choose>
    <c:when test="${sessionScope.role != 'GUEST'}">
        <form action="logout">
            <input type="submit" onclick="return confirmLogOut()" value="Log out"/>
        </form>
    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/login.jsp">
            <input type="submit" value="Log in"/>
        </form>
    </c:otherwise>
</c:choose>

<form action="cartView">
    <input type="hidden" name="userId" value="${sessionScope.userId}"/>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>