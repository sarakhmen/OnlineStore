<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title><fmt:message key="errorPageTitle"/></title>
</head>
<body>
<h1>${errorMessage}</h1>
<br>
${pageContext.exception}
<form action="${pageContext.request.contextPath}/">
    <input type="submit" value="<fmt:message key="returnBack"/>"/>
</form>
</body>
</html>
