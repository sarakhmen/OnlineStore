<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.locale}">
<head>
    <title>Add product page</title>
</head>
<body>
<form action="addProduct" method="post">
    <input type="text" id="nameEn" name="prodNameEn" placeholder="Product name in english"/>
    <label for="nameEn">Enter product name in english</label><br>
    <input type="text" id="nameUk" name="prodNameUk" placeholder="Product name in ukrainian"/>
    <label for="nameUk">Enter product name in ukrainian</label><br>
    <input type="text" id="price" name="price" placeholder="Product price in dollars"/>
    <label for="price">Enter product price in dollars</label><br>
    <input type="text" id="propsEn" name="propertyNamesEn" placeholder="(example: model,color,size)"/>
    <label for="propsEn">Enter property names in english comma separated</label><br>
    <input type="text" id="valuesEn" name="propertyValuesEn" placeholder="(example: S,white,small)"/>
    <label for="valuesEn">Enter corresponding property names in english comma separated</label><br>
    <input type="text" id="propsUk" name="propertyNamesUk" placeholder="(example: model,color,size)"/>
    <label for="propsEn">Enter property names in ukrainian comma separated</label><br>
    <input type="text" id="valuesUk" name="propertyValuesUk" placeholder="(example: S,white,small)"/>
    <label for="valuesEn">Enter corresponding property names in ukrainian comma separated</label><br>
    <button>
        Add product
    </button>
</form>
</body>
</html>
