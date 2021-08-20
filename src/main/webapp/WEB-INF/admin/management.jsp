<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title><fmt:message key="managementPage"/></title>
</head>
<body>

<div class="container">
    <c:import url="header.jsp"/>
    <div class="row mx-3 pt-4 gx-5">
        <div class="col fs-5">
            <fmt:message key="allUsers"/>
        </div>
    </div>
    <div class="row mx-3 pt-4 gx-5">
        <div class="col">
            <table class="table">
                <thead>
                <tr>
                    <th scope="col"><fmt:message key="id"/></th>
                    <th scope="col"><fmt:message key="userName"/></th>
                    <th scope="col"><fmt:message key="email"/></th>
                    <th scope="col"><fmt:message key="role"/></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${sessionScope.users}" var="user">
                    <tr>
                        <th scope="row"><c:out value="${user.id}"/></th>
                        <td><c:out value="${user.name}"/></td>
                        <td><c:out value="${user.login}"/></td>
                        <td><c:out value="${user.role}"/></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/main/admin/cartView">
                                <input type="hidden" name="cartUserId" value="${user.id}">
                                <button class="btn btn-outline-dark btn-sm">
                                    <fmt:message key="cart"/>
                                </button>
                            </form>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${user.role == 'ADMIN'}">
                                    <button disabled class="btn btn-outline-dark btn-sm">
                                        <fmt:message key="block"/>
                                    </button>
                                </c:when>
                                <c:when test="${user.blocked == 'unblocked'}">
                                    <form action="${pageContext.request.contextPath}/main/admin/userStatus" method="post">
                                        <input type="hidden" name="userId" value="${user.id}"/>
                                        <input type="hidden" name="role" value="${user.role}"/>
                                        <input type="hidden" name="newBlockStatus" value="blocked"/>
                                        <button class="btn btn-outline-dark btn-sm">
                                            <fmt:message key="block"/>
                                        </button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/main/admin/userStatus" method="post">
                                        <input type="hidden" name="userId" value="${user.id}"/>
                                        <input type="hidden" name="role" value="${user.role}"/>
                                        <input type="hidden" name="newBlockStatus" value="unblocked"/>
                                        <button class="btn btn-outline-dark btn-sm">
                                            <fmt:message key="unblock"/>
                                        </button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <div class="row mx-3 pt-4 gx-5">
        <div class="col-md-auto">
            <%--For displaying Previous link except for the 1st page --%>
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <c:if test="${requestScope.currentPage != 1}">
                        <li>
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/main/admin/management?page=${requestScope.currentPage - 1}"><fmt:message key="previous"/></a>
                        </li>
                    </c:if>

                    <%--For displaying Page numbers.
                    The when condition does not display a link for the current page--%>
                    <c:forEach begin="1" end="${requestScope.numberOfPages}" var="i">
                        <c:choose>
                            <c:when test="${requestScope.currentPage eq i}">
                                <li class="page-item active">
                                    <a class="page-link" aria-disabled="true">${i}</a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li>
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/main/admin/management?page=${i}">${i}</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <%--For displaying Next link --%>
                    <c:if test="${requestScope.currentPage lt requestScope.numberOfPages}">
                        <td>
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/main/admin/management?page=${requestScope.currentPage + 1}"><fmt:message key="next"/></a>
                        </td>
                    </c:if>
                </ul>
            </nav>
        </div>
    </div>
</div>
</body>
</html>
