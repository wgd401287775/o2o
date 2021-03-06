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
        product.productName = $("#product-name").val();
        product.productCategory = {
            productCategoryId : $("#category").find("option").not(function () {
                return !this.selected;
            }).data("id")
        };
        product.productDesc = $('#product-desc').val();
        product.priority = $('#priority').val();
        product.normalPrice = $('#normal-price').val();
        product.promotionPrice = $('#promotion-price').val();
        var formData = new FormData();
        var smallImg = $("#small-img")[0].files[0];
        formData.append("productStr", product);
        formData.append("smallImg", smallImg);
        $(".detail-img").map(function (index, item) {
            if($(".detail-img")[index].files.length > 0){
                alert(index);
                $('.detail-img')[index].files[0];
                formData.append("detailImg"+index, $('.detail-img')[index].files[0]);
            }
        })

    });
})