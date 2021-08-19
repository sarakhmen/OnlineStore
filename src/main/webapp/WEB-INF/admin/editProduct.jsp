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
<h2>Editing product with id=${requestScope.productId}</h2>
<form action="updateProduct" method="post">
    <input type="hidden" name="productId" value="${requestScope.productId}"/>
    <input type="text" id="nameEn" name="prodNameEn" value="${requestScope.prodNameEn}" placeholder="Product name in english"/>
    <label for="nameEn">Enter product name in english</label><br>
    <input type="text" id="nameUk" name="prodNameUk" value="${requestScope.prodNameUk}" placeholder="Product name in ukrainian"/>
    <label for="nameUk">Enter product name in ukrainian</label><br>
    <input type="number" id="price" name="price" value="${requestScope.price}" placeholder="Product price in dollars"/>
    <label for="price">Enter product price in dollars</label><br>
    <input type="text" id="propsEn" name="propertyNamesEn" value="${requestScope.propertyNamesEn}" placeholder="(example: model,color,size)"/>
    <label for="propsEn">Enter property names in english comma separated</label><br>
    <input type="text" id="valuesEn" name="propertyValuesEn" value="${requestScope.propertyValuesEn}" placeholder="(example: S,white,small)"/>
    <label for="valuesEn">Enter corresponding property names in english comma separated</label><br>
    <input type="text" id="propsUk" name="propertyNamesUk" value="${requestScope.propertyNamesUk}" placeholder="(example: model,color,size)"/>
    <label for="propsEn">Enter property names in ukrainian comma separated</label><br>
    <input type="text" id="valuesUk" name="propertyValuesUk" value="${requestScope.propertyValuesUk}" placeholder="(example: S,white,small)"/>
    <label for="valuesEn">Enter corresponding property names in ukrainian comma separated</label><br>
    <button>
        Update product
    </button>
</form>
</body>
</html>

