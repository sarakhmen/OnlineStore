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
</head>
<body>
<div class="container">
    <c:import url="header.jsp"/>
    <div class="row mx-3 pt-4 gx-5">
        <div class="col-9">
            <div class="row">
                <div class="col fs-5">
                    All products:
                </div>
                <div class="col-md-auto">
                    <form id="sortForm" action="${pageContext.request.contextPath}/main/catalog">
                        <label for="sortSelect" class="fs-5">Sort by:</label>
                        <select name="options" id="sortSelect" onchange="sendForm('sortForm')">
                            <option id="option1" ${sessionScope.sortOption == "byNameAz" ? "selected" : "" }
                                    value="byNameAZ">product
                                name (a-z)
                            </option>
                            <option id="option2" ${sessionScope.sortOption == "byNameZA" ? "selected" : "" }
                                    value="byNameZA">product
                                name (z-a)
                            </option>
                            <option id="option3" ${sessionScope.sortOption == "priceHighLow" ? "selected" : "" }
                                    value="priceHighLow">
                                price: High-Low
                            </option>
                            <option id="option4" ${sessionScope.sortOption == "priceLowHigh" ? "selected" : "" }
                                    value="priceLowHigh">
                                price: Low-High
                            </option>
                            <option id="option5" ${sessionScope.sortOption == "newest" ? "selected" : 0 }
                                    value="newest">newest
                            </option>
                        </select>
                    </form>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Product name</th>
                            <th scope="col">Price</th>
                            <th scope="col">Creation date</th>
                            <th scope="col">Properties</th>
                            <th scope="col"></th>
                            <th scope="col"></th>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:set var="i" value="${(requestScope.currentPage - 1) * 10 + 1}"/>
                        <c:forEach items="${sessionScope.products}" var="product">
                            <tr>
                                <th scope="row">${i}</th>
                                <c:set var="i" value="${i+1}"/>
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
                                    <form action="${pageContext.request.contextPath}/main/addToCart" method="post">
                                        <input type="hidden" name="productId" value="${product.id}"/>
                                        <button class="btn btn-outline-dark btn-sm">
                                            Order
                                        </button>
                                    </form>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/main/admin/editProductView" method="post">
                                        <input type="hidden" name="productId" value="${product.id}"/>
                                        <button class="btn btn-outline-dark btn-sm">
                                            Edit
                                        </button>
                                    </form>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/main/admin/deleteProduct" method="post">
                                        <input type="hidden" name="productId" value="${product.id}"/>
                                        <button class="btn btn-outline-dark btn-sm">
                                            Delete
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                    <div class="row">
                        <div class="col-md-auto">
                            <%--For displaying Previous link except for the 1st page --%>
                            <nav aria-label="Page navigation">
                                <ul class="pagination">
                                    <c:if test="${requestScope.currentPage != 1}">
                                        <li>
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/main/catalog?page=${requestScope.currentPage - 1}">Previous</a>
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
                                                       href="${pageContext.request.contextPath}/main/catalog?page=${i}">${i}</a>
                                                </li>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>

                                    <%--For displaying Next link --%>
                                    <c:if test="${requestScope.currentPage lt requestScope.numberOfPages}">
                                        <td>
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/main/catalog?page=${requestScope.currentPage + 1}">Next</a>
                                        </td>
                                    </c:if>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-3">
            <div class="row">
                <div class="col fs-5">
                    Properties:
                </div>
            </div>
            <div class="row pt-5">
                <div class="col mt-3" style="height: 600px; overflow-y: scroll;">
                    <form id="formProperties" action="${pageContext.request.contextPath}/main/catalog">
                        <c:set var="j" value="${1}"/>
                        <c:forEach items="${sessionScope.productProperties}" var="property">
                            <fieldset>
                                <legend class="fs-5">${property.key}:</legend>
                                <c:forEach items="${property.value}" var="value">
                                    <input class="form-check-input" type="checkbox" id="${j}" name="${property.key}"
                                           value="${value}"/>
                                    <label for="${j}">${value}</label>
                                </c:forEach>
                            </fieldset>
                            <br>
                        </c:forEach>
                        <c:set var="j" value="${j+1}"/>
                    </form>
                </div>
            </div>
            <div class="row justify-content-center">
                <div class="col-md-auto fs-6 pt-5">
                    <button form="formProperties" class="btn btn-outline-dark">
                        Select by parameters
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>
</body>
</html>