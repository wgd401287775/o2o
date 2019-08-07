$(function () {
    var shopId = getQueryString("shopId");
    var shopInfoUrl = "/o2o/shop/getshopmanageinfo?shopId="+shopId;
    $.getJSON(shopInfoUrl,function (data) {
        if(data.redirect){
            window.location.href = data.url;
        } else {
            if(data.shopId != undefined && data.shopId != null){
                shopId = data.shopId;
            }
            $("#shopInfo").prop("href", "/o2o/shop/shopoperation?shopId="+shopId);
        }
    })
})