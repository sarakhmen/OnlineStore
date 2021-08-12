<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title><fmt:message key="view.logInPage"/></title>
</head>
<body>
<form action="main/login" method="post">
    <input type="text" name="login" placeholder="
        <fmt:message key="view.enterLogin" />"
           title=<fmt:message key="view.enterLogin"/>>
    <input type="password" name="password"
           placeholder="<fmt:message key="view.enterPassword"/>"
           title=<fmt:message key="view.enterPassword"/>/>
    <br>
    <button><fmt:message key="view.logIn"/></button>
</form>
<br>
<form action="signup.jsp">
    <fmt:message key="view.dontHaveAccountQuestion"/>
    <button><fmt:message key="view.createAccount"/></button>
</form>
</body>
</html>