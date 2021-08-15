<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<body>
<h3><c:choose>
    <c:when test="${sessionScope.locale == 'en'}">
        <c:out value="${sessionScope.usernameEn}"/>
    </c:when>
    <c:otherwise>
        <c:out value="${sessionScope.usernameUk}"/>
    </c:otherwise>
</c:choose></h3>
</body>
</html>
