<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script>
        function confirmLogOut() {
            if (confirm("Are you sure?")) {
                return true;
            } else {
                return false;
            }
        }
    </script>
</head>
<div class="row justify-content-between my-3">
    <div class="col">
        <ul class="nav fs-4">
            <li class="nav-item">
                <a class="nav-link active" href="${pageContext.request.contextPath}/main/catalog"><fmt:message key="home"/></a>
            </li>
            <li class="nav-item">
                <a class="nav-link"
                   href="${pageContext.request.contextPath}/main/admin/cartView?userId=${sessionScope.userId}"><fmt:message key="cart"/></a>
            </li>
            <li class="nav-item">
                <a class="nav-link"
                   href="${pageContext.request.contextPath}/main/admin/management"><fmt:message key="management"/></a>
            </li>
            <li class="nav-item">
                <a class="nav-link"
                   href="${pageContext.request.contextPath}/main/admin/addProductView"><fmt:message key="newProduct"/></a>
            </li>
            <li class="nav-item">
                <a class="nav-link disabled"><fmt:message key="welcomePage"/></a>
            </li>
        </ul>
    </div>

    <div class="col-md-auto pt-1 pe-0">
        <a class="nav-link" href="?sessionLocale=en"><fmt:message key="en"/></a>
    </div>
    <div class="col-md-auto pt-1 ps-0 pe-2">
        <a class="nav-link" href="?sessionLocale=uk"><fmt:message key="ukr"/></a>
    </div>

    <div class="col-md-auto p-1">
        <form action="${pageContext.request.contextPath}/main/logout">
            <button type="submit" onclick="return confirmLogOut()" class="btn btn-outline-primary"><fmt:message key="logout"/>
            </button>
        </form>
    </div>
    <div class="col-md-auto fs-4 ms-4 p-1">
        <c:choose>
            <c:when test="${sessionScope.locale == 'en'}">
                <c:out value="${sessionScope.usernameEn}"/>
            </c:when>
            <c:otherwise>
                <c:out value="${sessionScope.usernameUk}"/>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>
</body>
</html>
