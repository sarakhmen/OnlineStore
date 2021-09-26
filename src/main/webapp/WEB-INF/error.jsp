<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title><fmt:message key="errorPageTitle"/></title>
</head>
<body>
<div class="container">
    <c:import url="header.jsp"/>
    Oops, ${requestScope.errorMessage}
    <form action="${pageContext.request.contextPath}/">
        <input type="submit" value="<fmt:message key="returnBack"/>"/>
    </form>
</div>
</body>
</html>
