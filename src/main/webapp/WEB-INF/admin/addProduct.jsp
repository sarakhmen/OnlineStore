<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title><fmt:message key="addProductPage"/></title>
</head>
<body>

<div class="container">
    <c:import url="header.jsp"/>
    <div class="d-flex justify-content-center pt-4 mt-3 fs-4">
        <fmt:message key="addingNewProd"/>
    </div>
    <div class="d-flex justify-content-center pt-3">
        <form action="${pageContext.request.contextPath}/main/admin/addProduct">
            <div class="mb-3">
                <label for="nameEn" class="form-label"><fmt:message key="productNameEn"/></label>
                <input type="text" name="prodNameEn" class="form-control" id="nameEn"
                       placeholder="<fmt:message key="enterProdNameInEnglish"/>">
            </div>
            <div class="mb-3">
                <label for="nameUk" class="form-label"><fmt:message key="productNameUk"/></label>
                <input type="text" name="prodNameUk" class="form-control" id="nameUk"
                       placeholder="<fmt:message key="enterProdNameInUkrainian"/>">
            </div>
            <div class="mb-3">
                <label for="price" class="form-label"><fmt:message key="priceInDollars"/></label>
                <input type="number" name="price" class="form-control" id="price"
                       placeholder="<fmt:message key="enterPriceInDollars"/>">
            </div>
            <div class="mb-3">
                <label for="propsEn" class="form-label"><fmt:message key="propertyNamesEn"/></label>
                <input type="text" name="propertyNamesEn" class="form-control" id="propsEn"
                       placeholder="<fmt:message key="propertyNamesExampleEn"/>">
            </div>
            <div class="mb-3">
                <label for="valuesEn" class="form-label"><fmt:message key="propertyValuesEn"/></label>
                <input type="text" name="propertyValuesEn" class="form-control" id="valuesEn"
                       placeholder="<fmt:message key="propertyValuesExampleEn"/>">
            </div>
            <div class="mb-3">
                <label for="propsUk" class="form-label"><fmt:message key="propertyNamesUk"/></label>
                <input type="text" name="propertyNamesUk" class="form-control" id="propsUk"
                       placeholder="<fmt:message key="propertyNamesExampleUk"/>">
            </div>
            <div class="mb-3">
                <label for="valuesEn" class="form-label"><fmt:message key="propertyValuesUk"/></label>
                <input type="text" name="propertyValuesUk" class="form-control" id="valuesUk"
                       placeholder="<fmt:message key="propertyValuesExampleUk"/>">
            </div>
            <button type="submit" class="btn btn-primary"><fmt:message key="addProduct"/></button>
        </form>
    </div>
</div>
</body>
</html>
