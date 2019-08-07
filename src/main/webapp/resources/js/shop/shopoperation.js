$(function(){
    var shopId = getQueryString("shopId");
    var isEdit = shopId?true:false;
    var initUrl = "/o2o/shop/getshopinitinfo";
    var registerUrl = "/o2o/shop/registershop";
    var shopInfoUrl = "/o2o/shop/getshopbyid";
    var modifyUrl = "/o2o/shop/modifyshop";

    if(isEdit){
        getShopInfo(shopId);
    }else{
        getShopInitInfo();
    }

    function getShopInfo(shopId) {
        //动态加载下拉列表
        $.ajax({
            url:shopInfoUrl,
            type:"get",
            data:{"shopId":shopId},
            dataType:"json",
            success:function (data) {
                if(data.success) {
                    var shop = data.shop;
                    $("#shop-name").val(shop.shopName);
                    $("#shop-name").attr("disabled", "disabled");
                    $("#shop-addr").val(shop.shopAddr);
                    $("#shop-desc").val(shop.shopDesc);
                    $("#shop-phone").val(shop.phone);
                    var shopCategory  = "<option data-id = '" + shop.shopCategory.shopCategoryId
                        + "' selected>" + shop.shopCategory.shopCategoryName + "</option>";
                    $("#shop-category").html(shopCategory);
                    $("#shop-category").attr("disabled", "disabled");
                    var tempAreaHtml = "";
                    data.areaList.map(function (item, index) {
                        tempAreaHtml += "<option data-id='" + item.areaId + "'>" + item.areaName + "</option>";
                    });
                    $("#area").html(tempAreaHtml);
                    $("#area option[data-id='"+shop.area.areaId+"']").attr("selected", "selected");
                }
            },
            error:function () {
                $.alert("init shopinfo error");
            }
        })
    }
    function getShopInitInfo() {
        //动态加载下拉列表
        $.ajax({
            url:initUrl,
            type:"get",
            data:null,
            dataType:"json",
            success:function (data) {
                if(data.success) {
                    var tempHtml = "";
                    var tempAreaHtml = "";
                    data.categoryList.map(function (item, index) {
                        tempHtml += "<option data-id='" + item.shopCategoryId + "'>" + item.shopCategoryName + "</option>";
                    });
                    data.areaList.map(function (item, index) {
                        tempAreaHtml += "<option data-id='" + item.areaId + "'>" + item.areaName + "</option>";
                    });
                    $("#shop-category").html(tempHtml);
                    $("#area").html(tempAreaHtml);
                }
            },
            error:function () {
                $.alert("init error");
            }
        })
    }

    $("#submit").click(function () {
        var shop = {};
        if(isEdit){
            shop.shopId = shopId;
        }
        shop.shopName = $("#shop-name").val();
        shop.shopAddr = $("#shop-addr").val();
        shop.phone = $("#shop-phone").val();
        shop.shopDesc = $("#shop-desc").val();
        shop.shopCategory = {
            shopCategoryId : $("#shop-category").find("option").not(function () {
                return !this.selected;
            }).data("id")
        };
        shop.area = {
            areaId : $("#area").find("option").not(function () {
                return !this.selected;
            }).data("id")
            // areaId : $("#area").find("option:selected").data("id")
        }
        var shopImg = $("#shop-img")[0].files[0];
        var formData = new FormData();
        formData.append("shopImg", shopImg);
        formData.append("shopStr", JSON.stringify(shop));
        var verifyCode = $("#j_captcha").val();
        if(!verifyCode){
            $.alert("验证码不能为空!");
            return;
        }
        formData.append("verifyCode", verifyCode);
        $.ajax({
            type:"post",
            url:isEdit?modifyUrl:registerUrl,
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function (data) {
                if(data.success){
                    $.alert("店铺操作成功！");
                } else {
                    $.alert("店铺操作失败！"+data.errMsg);
                }
                $("#captcha_img").click();
            },
            error:function () {
                $.alert("submit error");
            }
        });
    });
});