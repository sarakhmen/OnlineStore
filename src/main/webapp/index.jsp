<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title><fmt:message key="welcomePage"/></title>
</head>
<body>
<div class="container">
    <c:import url="WEB-INF/header.jsp"/>
    <div class="row mx-3 pt-4 gx-5">
        <div class="d-flex justify-content-center fs-4"><fmt:message key="welcomeBack"/></div>
        <div class="col-md-auto mt-4">
            <div class="fs-5">
                <fmt:message key="task.name"/>
            </div>
            <fmt:message key="task.description"/>
        </div>
    </div>
</div>
</body>
</html>
