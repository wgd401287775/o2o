$(function () {
    var productId = getQueryString("productId");
    var editUrl = "";
    var categoryUrl = "/o2o/shop/getproductcategorylistbyshopid";
    if(productId){
        editUrl = "/o2o/shop/productmodify";
        getProductInfo(productId);
    } else {
        editUrl = "/o2o/shop/productinsert";
        getProductInitInfo();
    }

    function getProductInfo(productId) {

    }

    $("#detail-img").on("change",".detail-img:last-child",function () {
        if($(".detail-img").length < 6 && $(this).val()){
            $("#detail-img").append("<input type=\"file\" class=\"detail-img\">");
        }
    });

    function getProductInitInfo(){
        $.getJSON(categoryUrl, function (data) {
            if(data.success){
                var categoryList = data.productCategoryList;
                var optionHtml = "";
                $.map(categoryList, function (item, index) {
                    optionHtml += "<option data-id='"+item.productCategoryId+"'>"+item.productCategoryName+"</option>"
                });
                $("#category").html(optionHtml);
            }
        })
    }

    $("#submit").click(function () {
        var product = {};

    });
})