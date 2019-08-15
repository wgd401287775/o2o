$(function () {
   getProductList();
   
   function getProductList() {
       $.getJSON("/o2o/shop/getproductlist", function (data) {
           if(data.success){
               var tempHtml = "";
                $.map(data.productList, function (item, index) {
                    tempHtml += "<div class=\"row row-product\"><div class=\"col-40 product-name\">"+item.productName+"</div>" +
                        "<div class=\"col-60 product-wrap\"><a href=\"#\" class='edit' data-id='"+item.productId+"'>编辑</a>" +
                        "<a href=\"#\" class='delete' data-id='"+item.productId+"'>删除</a><a href=\"#\" class='show' data-id='"+item.productId+"'>预览</a></div></div>";
                });
                $(".product-wrap").html(tempHtml);
           } else {
               $.alert("商品列表加载失败," + data.errorMsg);
           }
       });
   }

   $("#new").click(function () {
       window.location.href = "/o2o/shop/productedit";
   });

   $(".product-wrap").on("click", "a", function (e) {
        var target = $(e.currentTarget);
        if(target.hasClass("edit")){
            window.location.href = "/o2o/shop/productedit?productId="+target.data("id");
        } else if(target.hasClass("show")){
            window.location.href = "";
        }
   })
});