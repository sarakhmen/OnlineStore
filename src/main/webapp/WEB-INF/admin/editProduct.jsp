<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title><fmt:message key="editProductPage"/></title>
</head>
<body>

<div class="container">
    <c:import url="header.jsp"/>
    <div class="d-flex justify-content-center pt-4 mt-3 fs-4">
        <fmt:message key="editingProductWith"/> id=${requestScope.productId}
    </div>
    <div class="d-flex justify-content-center pt-3">
        <form action="${pageContext.request.contextPath}/main/admin/updateProduct">
            <input type="hidden" name="productId" value="${requestScope.productId}"/>
            <div class="mb-3">
                <label for="name" class="form-label"><fmt:message key="productNameEn"/></label>
                <input type="text" name="name" class="form-control" id="name"
                       placeholder="<fmt:message key="enterProdNameInEnglish"/>" value="${requestScope.name}">
            </div>
            <div class="mb-3">
                <label for="price" class="form-label"><fmt:message key="priceInDollars"/></label>
                <input type="number" name="price" class="form-control" id="price" min="0"
                       placeholder="<fmt:message key="enterPriceInDollars"/>" value="${requestScope.price}">
            </div>
            <div class="mb-3">
                <label for="props" class="form-label"><fmt:message key="propertyNamesEn"/></label>
                <input type="text" name="propertyNames" class="form-control" id="props"
                       placeholder="<fmt:message key="propertyNamesExampleEn"/>" value="${requestScope.propertyNames}">
            </div>
            <div class="mb-3">
                <label for="values" class="form-label"><fmt:message key="propertyValuesEn"/></label>
                <input type="text" name="propertyValues" class="form-control" id="values"
                       placeholder="<fmt:message key="propertyValuesExampleEn"/>" value="${requestScope.propertyValues}">
            </div>
            <button type="submit" class="btn btn-primary"><fmt:message key="updateProduct"/></button>
        </form>
    </div>
</div>
</body>
</html>

