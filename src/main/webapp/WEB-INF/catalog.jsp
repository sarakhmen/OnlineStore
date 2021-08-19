<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <title><fmt:message key="view.catalogPage"/></title>
    <script>
        function sendForm(formId) {
            document.getElementById(formId).submit();
        }
    </script>
</head>
<body>
<div class="container">
    <div class="row justify-content-between my-3">
        <div class="col">
            <ul class="nav fs-5">
                <li class="nav-item">
                    <a class="nav-link active" href="#">Active</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Link</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Link</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link disabled" href="#">Disabled</a>
                </li>
            </ul>
        </div>
        <div class="col-md-auto">
            <button type="button" class="btn btn-outline-primary">Log in</button>
            <button type="button" class="btn btn-outline-secondary">Sign up</button>
        </div>
    </div>


    <c:import url="header.jsp"/>
    <h2>MAIN PAGE CONTENT</h2>
    <input type="range" min="1" max="100" value="50" id="myRange">
    <form id="sortForm" action="${pageContext.request.contextPath}/main/catalog">
        <label for="sortSelect">Sort by:</label>
        <select name="options" id="sortSelect" onchange="sendForm('sortForm')">
            <option id="option1" ${sessionScope.sortOption == "byNameAz" ? "selected" : "" } value="byNameAZ">By product
                name (a-z)
            </option>
            <option id="option2" ${sessionScope.sortOption == "byNameZA" ? "selected" : "" } value="byNameZA">By product
                name (z-a)
            </option>
            <option id="option3" ${sessionScope.sortOption == "priceHighLow" ? "selected" : "" } value="priceHighLow">
                Price:
                High-Low
            </option>
            <option id="option4" ${sessionScope.sortOption == "priceLowHigh" ? "selected" : "" } value="priceLowHigh">
                Price:
                Low-High
            </option>
            <option id="option5" ${sessionScope.sortOption == "newest" ? "selected" : 0 } value="newest">Newest</option>
        </select>
    </form>

    <form id="formProperties" action="${pageContext.request.contextPath}/main/catalog">
        <c:forEach items="${sessionScope.productProperties}" var="property">
            <fieldset>
                <legend>${property.key}</legend>
                <c:forEach items="${property.value}" var="value">
                    <input type="checkbox" name="${property.key}" value="${value}"/>${value}
                </c:forEach>
            </fieldset>
        </c:forEach>
        <button>
            Select by parameters
        </button>
    </form>


    <div>
        <table>
            <tr>
                <th>Product name</th>
                <th>Price</th>
                <th>Creation date</th>
                <th>Properties</th>
                <th></th>
            </tr>
            <c:forEach items="${sessionScope.products}" var="product">
                <tr>
                    <td><c:out value="${product.name}"/></td>
                    <td><c:out value="${product.price}"/></td>
                    <td><c:out value="${product.creationDate}"/></td>
                    <td>
                        <c:forEach items="${product.properties}" var="property">
                            <c:out value="${property.key}: ${property.value}"/>
                            <br>
                        </c:forEach>
                    </td>
                    <td>
                        <form action="addToCart" method="post">
                            <input type="hidden" name="productId" value="${product.id}"/>
                            <button>
                                Order
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <%--For displaying Previous link except for the 1st page --%>
        <c:if test="${requestScope.currentPage != 1}">
            <td>
                <a href="${pageContext.request.contextPath}/main/catalog?page=${requestScope.currentPage - 1}">Previous</a>
            </td>
        </c:if>

        <%--For displaying Page numbers.
        The when condition does not display a link for the current page--%>
        <table border="1" cellpadding="5" cellspacing="5">
            <tr>
                <c:forEach begin="1" end="${requestScope.numberOfPages}" var="i">
                    <c:choose>
                        <c:when test="${requestScope.currentPage eq i}">
                            <td>${i}</td>
                        </c:when>
                        <c:otherwise>
                            <td><a href="${pageContext.request.contextPath}/main/catalog?page=${i}">${i}</a></td>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
        </table>

        <%--For displaying Next link --%>
        <c:if test="${requestScope.currentPage lt requestScope.numberOfPages}">
            <td><a href="${pageContext.request.contextPath}/main/catalog?page=${requestScope.currentPage + 1}">Next</a>
            </td>
        </c:if>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>
</body>
</html>