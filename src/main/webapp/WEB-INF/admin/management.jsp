<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title>Cart page</title>
    <style type="text/css">
        TABLE {
            width: 600px;
            border-collapse: collapse;
        }

        TD, TH {
            padding: 3px;
            border: 1px solid black;
        }

        TH {
            background: #b0e0e6;
        }
    </style>
</head>
<body>
<c:import url="header2.jsp"/>
<h2>All users</h2>
<div>
    <table style="float: left">
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Login</th>
            <th>Role</th>
        </tr>
        <c:forEach items="${sessionScope.users}" var="user">
            <tr>
                <td><c:out value="${user.id}"/></td>
                <td><c:out value="${user.name}"/></td>
                <td><c:out value="${user.login}"/></td>
                <td><c:out value="${user.role}"/></td>
                <td>
                    <form action="cart">
                        <input type="hidden" name="userId" value="${user.id}"/>
                        <button>
                            Cart
                        </button>
                    </form>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${user.role == 'ADMIN'}">
                            <button disabled>
                                Block
                            </button>
                        </c:when>
                        <c:when test="${user.blocked == 'unblocked'}">
                            <form action="userStatus" method="post">
                                <input type="hidden" name="userId" value="${user.id}"/>
                                <input type="hidden" name="role" value="${user.role}"/>
                                <input type="hidden" name="newBlockStatus" value="blocked"/>
                                <button>
                                    Block
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form action="userStatus" method="post">
                                <input type="hidden" name="userId" value="${user.id}"/>
                                <input type="hidden" name="role" value="${user.role}"/>
                                <input type="hidden" name="newBlockStatus" value="unblocked"/>
                                <button>
                                    Unblock
                                </button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
