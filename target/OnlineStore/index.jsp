<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title><fmt:message key="view.onlineStore"/></title>
</head>
<body>
<h1><fmt:message key="view.welcomeBack"/></h1>
<form action="login.jsp">
    <button><fmt:message key="view.logIn"/></button>
</form>
<form action="signup.jsp">
    <fmt:message key="view.dontHaveAccountQuestion"/>
    <button><fmt:message key="view.createAccount"/></button>
</form>
<br>

<form action="main.jsp">
    <input type="hidden" name="command" value="main"/>
    <button>
        <fmt:message key="view.goToMainPage"/>
    </button>
</form>

<fmt:message key="view.chooseLocale"/>
<ul>
    <li><a href="?sessionLocale=en"><fmt:message key="view.english"/></a></li>
    <li><a href="?sessionLocale=uk"><fmt:message key="view.ukrainian"/></a></li>
</ul>
</body>
</html>
