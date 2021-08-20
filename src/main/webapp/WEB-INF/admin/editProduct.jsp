<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title>Edit product page</title>
</head>
<body>

<div class="container">
    <c:import url="header.jsp"/>
    <div class="d-flex justify-content-center pt-4 mt-3 fs-4">
        Editing product with id=${requestScope.productId}
    </div>
    <div class="d-flex justify-content-center pt-3">
        <form action="${pageContext.request.contextPath}/main/admin/updateProduct">
            <input type="hidden" name="productId" value="${requestScope.productId}"/>
            <div class="mb-3">
                <label for="nameEn" class="form-label">Product name in english</label>
                <input type="text" name="prodNameEn" class="form-control" id="nameEn"
                       placeholder="Enter name in english" value="${requestScope.prodNameEn}">
            </div>
            <div class="mb-3">
                <label for="nameUk" class="form-label">Product name in ukrainian</label>
                <input type="text" name="prodNameUk" class="form-control" id="nameUk"
                       placeholder="Enter name in ukrainian" value="${requestScope.prodNameUk}">
            </div>
            <div class="mb-3">
                <label for="price" class="form-label">Product price in dollars</label>
                <input type="number" name="price" class="form-control" id="price"
                       placeholder="Enter product price in dollars" value="${requestScope.price}">
            </div>
            <div class="mb-3">
                <label for="propsEn" class="form-label">Property names in english (comma separated)</label>
                <input type="text" name="propertyNamesEn" class="form-control" id="propsEn"
                       placeholder="(example: model,color,size)" value="${requestScope.propertyNamesEn}">
            </div>
            <div class="mb-3">
                <label for="valuesEn" class="form-label">Corresponding property values in english comma
                    separated</label>
                <input type="text" name="propertyValuesEn" class="form-control" id="valuesEn"
                       placeholder="(example: model,color,size)" value="${requestScope.propertyValuesEn}">
            </div>
            <div class="mb-3">
                <label for="propsUk" class="form-label">Property names in ukrainian (comma separated)</label>
                <input type="text" name="propertyNamesUk" class="form-control" id="propsUk"
                       placeholder="(example: model,color,size)" value="${requestScope.propertyNamesUk}">
            </div>
            <div class="mb-3">
                <label for="valuesEn" class="form-label">Corresponding property values in ukrainian comma
                    separated</label>
                <input type="text" name="propertyValuesUk" class="form-control" id="valuesUk"
                       placeholder="(example: model,color,size)" value="${requestScope.propertyValuesUk}">
            </div>
            <button type="submit" class="btn btn-primary">Update product</button>
        </form>
    </div>
</div>
</body>
</html>

