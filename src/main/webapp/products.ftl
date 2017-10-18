<#import "spring.ftl" as spring />
<html lang="en">
<head>
    <title>Product list</title>
    <#include "resources/ftl/head.ftl">
    <script src="<@spring.url '/resources/js/common.js'/>"></script>
    <script src="<@spring.url '/resources/js/api-product.js'/>"></script>
    <script src="<@spring.url '/resources/js/products.js'/>"></script>
</head>
<body>
<div class="container">
    <#include "resources/ftl/navbar.ftl">
    <h1>Products</h1>
    <select id="size-select"
            onchange="onSizeChange()">
        <option selected>10</option>
        <option>20</option>
        <option>50</option>
    </select>
    <table class="table">
        <thead>
        <tr>
            <th></th>
            <td>Product name</td>
            <td>Price</td>
        </tr>
        </thead>
        <tbody id="products"></tbody>
    </table>
    <!--TODO:прилепить внизу страницы и отключать кнопки в зависимости от количества-->
    <div class="text-center">
        <button id="prev-page"
                class="btn btn-primary"
                onclick="getProducts(currentPage - 1, currentSize ,onSuccessLoadProducts)">
            Previous
        </button>
        <button id="next-page" class="btn btn-primary"
                onclick="getProducts(currentPage + 1, currentSize ,onSuccessLoadProducts)">
            Next
        </button>
    </div>
</div>
</body>
</html>