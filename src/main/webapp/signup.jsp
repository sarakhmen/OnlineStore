<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title><fmt:message key="signupPage"/></title>
</head>
<body>
<div class="container">
    <c:import url="WEB-INF/header.jsp"/>
    <div class="d-flex justify-content-center pt-4 mt-3 fs-4">
        <fmt:message key="registration"/>
    </div>
    <div class="d-flex justify-content-center pt-3">
        <form action="${pageContext.request.contextPath}/main/signup">
            <div class="mb-3">
                <label for="inputNameEn" class="form-label"><fmt:message key="usernameEn"/></label>
                <input type="text" name="usernameEn" class="form-control" id="inputNameEn"
                       placeholder="<fmt:message key="enterUsernameEn"/>">
            </div>
            <div class="mb-3">
                <label for="inputNameUk" class="form-label"><fmt:message key="usernameUk"/></label>
                <input type="text" name="usernameUk" class="form-control" id="inputNameUk"
                       placeholder="<fmt:message key="enterUsernameUk"/>">
            </div>
            <div class="mb-3">
                <label for="inputEmail1" class="form-label"><fmt:message key="email"/></label>
                <input type="email" name="login" class="form-control" id="inputEmail1" placeholder="<fmt:message key="enterEmail"/>">
                <div id="emailHelp" class="form-text"><fmt:message key="safety"/></div>
            </div>
            <div class="mb-3">
                <label for="inputPassword1" class="form-label"><fmt:message key="password"/></label>
                <input type="password" name="password" class="form-control" id="inputPassword1" placeholder="<fmt:message key="enterPassword"/>">
            </div>
            <button type="submit" class="btn btn-primary"><fmt:message key="submit"/></button>
        </form>
    </div>
</div>
</body>
</html>