<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title><fmt:message key="view.logInPage"/></title>
</head>
<body>
<form action="main/signup" method="post">
    <input type="text" name="username" placeholder="
        <fmt:message key="view.enterUsername" />"
           title=<fmt:message key="view.enterUsername"/>>
    <input type="text" name="login" placeholder="
        <fmt:message key="view.enterLogin" />"
           title=<fmt:message key="view.enterLogin"/>>
    <input type="password" name="password"
           placeholder="<fmt:message key="view.enterPassword"/>"
           title=<fmt:message key="view.enterPassword"/>/>
    <br>
    <button><fmt:message key="view.createAccount"/></button>
</form>
</body>
</html>