$(function () {
   getProductList();
   
   function getProductList() {
       $.getJSON("/o2o/shop/getproductlist", function (data) {
           if(data.success){
               var tempHtml = "";
                $.map(data.productList, function (item, index) {
                    tempHtml += "<div class=\"row row-product\"><div class=\"col-40 product-name\">"+item.productName+"</div>" +
                        "<div class=\"col-60 product-wrap\"><a href=\"#\" data-id='"+item.productId+"'>编辑</a>" +
                        "<a href=\"#\" data-id='"+item.productId+"'>删除</a><a href=\"#\" data-id='"+item.productId+"'>预览</a></div></div>";
                });
                $(".product-wrap").html(tempHtml);
           } else {
               $.alert("商品列表加载失败," + data.errorMsg)
           }
       })
   }

   $("#new").click(function () {
       window.location.href = "/o2o/shop/productedit";
   });
});