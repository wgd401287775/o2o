$(function () {
    var loading = false;
    var maxItems = 999;
    var pageSize = 3;
    var pageNum = 1;
    var shopInfoUrl = '/o2o/frontend/getshoppageinfo';
    var productsUrl = '/o2o/frontend/listproducts';
    var shopId = getQueryString("shopId");
    var productCategoryId = "";
    var productName = "";

    getShopInfo();
    function getShopInfo() {
        $.getJSON(shopInfoUrl, {shopId:shopId}, function (data) {
            if(data.success){
                var shop = data.shop;
                $("#shop-cover-pic").attr("src", shop.shopImg);
                $("#shop-update-time").html(new Date(shop.lastEditTime).Format("yyyy-MM-dd"));
                $('#shop-name').html(shop.shopName);
                $('#shop-desc').html(shop.shopDesc);
                $('#shop-addr').html(shop.shopAddr);
                $('#shop-phone').html(shop.phone);
                var productCategoryList = data.productCategoryList;
                var html = '';
                productCategoryList.map(function (item) {
                    html += "<a href=\"#\" class=\"button\" data-id='"+item.productCategoryId+"'>" +
                            item.productCategoryName + "</a>"
                });
                $("#shopdetail-button-div").html(html);
            }
        });
    }

    addItems(pageSize, pageNum);
    function addItems(pageSize, pageIndex) {
        loading = true;
        var url = productsUrl +"?pageSize="+pageSize+"&pageNum="+pageIndex+"&shopId="+shopId
                    +"&productCategoryId="+productCategoryId+"&productName="+productName;
        $.getJSON(url, function (data) {
            if(data.success){
                var productList = data.productList;
                var html = "";
                $.map(productList, function (item, index) {
                    html += '<div class="card" data-product-id='
                        + item.productId + '>'
                        + '<div class="card-header">' + item.productName
                        + '</div>' + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + item.imgAddr + '" width="44">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.productDesc
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(item.lastEditTime).Format("yyyy-MM-dd")
                        + '更新</p>' + '<span>点击查看</span>' + '</div>'
                        + '</div>';
                });
                $(".list-div").append(html);
                maxItems = data.count;
                var total = $(".list-div .card").length;
                if (total >= maxItems) {
                    // 加载完毕，则注销无限加载事件，以防不必要的加载
                    $.detachInfiniteScroll($('.infinite-scroll'));
                    // 删除加载提示符
                    $('.infinite-scroll-preloader').remove();
                }
                pageNum += 1;
                loading = false;
                $.refreshScroller();
            }
        })
    }

    $(document).on('input', '#search', function (e) {
        productName = e.currentTarget.value;
        pageNum = 1;
        $(".list-div").empty();
        addItems(pageSize, pageNum);
        $.init();
    });
    $(document).on('click', '.button', function (e) {
        if($(e.target).hasClass("button-fill")){
            $(e.target).removeClass("button-fill");
            productCategoryId = "";
        } else {
            $(e.target).addClass("button-fill").siblings().removeClass("button-fill");
            productCategoryId = e.currentTarget.dataset.id;
        }
        pageNum = 1;
        $(".list-div").empty();
        addItems(pageSize, pageNum);
        $.init();
    });

    $(document).on('infinite', '.infinite-scroll-bottom', function() {
        if (loading)
            return;
        addItems(pageSize, pageNum);
    });
    $(document).on("click", "#me", function () {
        $.openPanel("#panel-left-demo");
    });
    $.init();
});