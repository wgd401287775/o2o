$(function () {
    var listUrl = "/o2o/shop/getproductcategorylist";
    var addUrl = '/o2o/shop/addproductcategorys';
    var removeUrl = "/o2o/shop/removeproductcategory"

    getList();

    function getList(){
        $.getJSON(listUrl,function (data) {
            var tempHtml = "";
            if(data.success) {
                data.data.map(function (item, index) {
                    tempHtml += "<div class='row row-product-category now'><div class='col-33 product-category-name'>"+item.productCategoryName+"</div><div class='col-33'>"
                        +item.priority+"</div><div class='col-33'><a href='#' class='button delete' data-id='"+item.productCategoryId+"'>删除</a></div></div>";
                });
                $(".category-wrap").html(tempHtml);
            } else {
                $.alert("商品类别加载失败," + data.errorMsg);
            }
        })
    }

    $("#new").click(function () {
        var tempHtml = "<div class='row row-product-category temp'>" +
            "<div class='col-33'><input type='text' class='category-input category' placeholder='分类名称'></div>" +
            "<div class='col-33'><input type='number' class='category-input priority' placeholder='优先级'></div>" +
            "<div class='col-33'><a href='#' class='button delete'>删除</a></div>" +
            "</div>";
        $(".category-wrap").append(tempHtml);
    });

    $("#submit").click(function () {
        var tempArr = $(".temp");
        var productCategorys = [];
        $.map(tempArr, function (item, index) {
            var productCategory = {};
            productCategory.productCategoryName = $(item).find(".category").val();
            productCategory.priority = $(item).find(".priority").val();
            if(productCategory.productCategoryName && productCategory.priority){
                productCategorys.push(productCategory);
            }
        });
        $.ajax({
            url:addUrl,
            type:"post",
            data:JSON.stringify(productCategorys),
            contentType:"application/json",
            dataType:"json",
            success:function (data) {
                if(data.success){
                    $.alert("提交成功！");
                    getList();
                } else {
                    $.alert("提交失败！");
                    $.alert(data.errorMsg);
                }
            }
        });
    });

    $(".category-wrap").on("click", ".row-product-category.now .delete" ,function () {
        var cateId = $(this).data("id");
        $.confirm("确定要删除吗？", function() {
            $.ajax({
                type:"post",
                url:removeUrl,
                data:{productCategoryId :cateId},
                dataType:"json",
                success:function (data) {
                    if(data.success){
                        $.alert("删除成功！");
                        getList();
                    } else {
                        $.alert("删除失败！");
                        $.alert(data.errorMsg);
                    }
                }
            });
        })
    })

    $(".category-wrap").on("click", ".row-product-category.temp .delete", function () {
        $(this).parent().parent().remove();
    })

})