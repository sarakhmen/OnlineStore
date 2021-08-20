<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title>Sign up page</title>
</head>
<body>
<div class="container">
    <c:import url="WEB-INF/header.jsp"/>
    <div class="d-flex justify-content-center pt-4 mt-3 fs-4">
        Registration
    </div>
    <div class="d-flex justify-content-center pt-3">
        <form action="${pageContext.request.contextPath}/main/signup">
            <div class="mb-3">
                <label for="inputNameEn" class="form-label">Username in english</label>
                <input type="text" name="usernameEn" class="form-control" id="inputNameEn"
                       placeholder="Enter username in english">
            </div>
            <div class="mb-3">
                <label for="inputNameUk" class="form-label">Username in ukrainian</label>
                <input type="text" name="usernameUk" class="form-control" id="inputNameUk"
                       placeholder="Enter username in ukrainian">
            </div>
            <div class="mb-3">
                <label for="inputEmail1" class="form-label">Email address</label>
                <input type="text" name="login" class="form-control" id="inputEmail1" placeholder="Enter email">
                <div id="emailHelp" class="form-text">We'll never share your email with anyone else.</div>
            </div>
            <div class="mb-3">
                <label for="inputPassword1" class="form-label">Password</label>
                <input type="password" name="password" class="form-control" id="inputPassword1" placeholder="Password">
            </div>
            <button type="submit" class="btn btn-primary">Submit</button>
        </form>
    </div>
</div>
</body>
</html>