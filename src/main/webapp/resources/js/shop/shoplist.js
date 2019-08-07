$(function () {
    getShopList();

    function getShopList() {
        $.ajax({
            url:"/o2o/shop/getshoplist",
            type:"get",
            dataType:"json",
            success:function (data) {
                if(data.success){
                    handleList(data.shopList);
                    handleName(data.user);
                }
            }
        })
    }

    function handleName(user) {
        $("#user-name").html(user.name);
    }

    function handleList(data) {
        var tempHtml = "";
        data.map(function (item, index) {
            tempHtml += "<div class=\"row row-shop\"><div class=\"col-40 shop-name\">"+item.shopName+"</div><div class=\"col-40\">"
                +shopStatus(item.enableStatus)+"</div><div class=\"col-20\">"+
                goShop(item.enableStatus,item.shopId)+"</div></div>";
        });
        $(".shop-wrap").html(tempHtml);
    }
    
    function shopStatus(status) {
        if(status ==  0){
            return "审核中";
        } else if(status == 1){
            return "审核通过";
        } else {
            return "非法店铺";
        }
    }

    function goShop(status, shopId) {
        if(status == 1){
            return "<a href=\"/o2o/shop/shopmanage?shopId="+shopId+"\">进入</a>";
        } else {
            return "";
        }
    }
})