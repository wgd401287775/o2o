$(function () {
    var loading = false;
    var maxItems = 999;
    var pageSize = 3;
    var pageNum = 1;
    var listUrl = '/o2o/frontend/listshops';
    var searchDivUrl = '/o2o/frontend/listshopspageinfo';
    var shopUrl = "/o2o/frontend/shopdetail";

    var parentId = getQueryString("parentId");
    var areaId = '';
    var shopCategoryId = '';
    var shopName = '';
    var flag = true;
    if(parentId){
        flag = false;
    }

    getSearchDivData();
    function getSearchDivData(){
        $.ajax({
            type:"post",
            url:searchDivUrl,
            data:{parentId:parentId==''?null:parentId},
            dataType:"json",
            success:function (data) {
                if(data.success){
                    var categoryHtml = '<a href="#" class="button" data-category-id=""> 全部类别  </a>';
                    data.shopCategoryList.map(function (item) {
                        categoryHtml += '<a href="#" class="button" data-category-id="'+item.shopCategoryId+'">'+item.shopCategoryName+'</a>';
                    });
                    $("#shoplist-search-div").html(categoryHtml);
                    var optionHtml = '<option value="">全部街道</option>';
                    data.areaList.map(function (item) {
                        optionHtml += '<option value="'+item.areaId+'">'+item.areaName+'</option>';
                    });
                    $("#area-search").html(optionHtml);
                }
            }
        });
    }

    addItems(pageSize, pageNum);
    function addItems(pageSize, pageIndex){
        // 生成新条目的HTML
        loading = true;
        var url = listUrl +"?pageIndex="+pageIndex+"&pageSize="+pageSize+"&parentId="+parentId
                    +"&areaId="+areaId+"&shopCategoryId="+shopCategoryId+"&shopName="+shopName;
        $.getJSON(url,function (data) {
            if(data.success){
                var html = "";
                maxItems = data.count
                data.shopList.map(function (item) {
                    html += '<div class="card" data-shop-id="'+item.shopId+'">' +
                        '<div class="card-header">'+item.shopName+'</div>' +
                        '<div class="card-content"><div class="list-block media-list">' +
                        '<ul><li class="item-content"><div class="item-media">' +
                        '<img src="'+item.shopImg+'" width="44"></div><div class="item-inner">' +
                        '<div class="item-subtitle"></div></div></li></ul></div></div>' +
                        '<div class="card-footer">' +
                        '<p class="color-gray">'+new Date(item.lastEditTime).Format("yyyy-MM-dd")+'更新</p>' +
                        '<span>点击查看</span></div></div>';
                });
                $(".shop-list").append(html);
                var total = $('.list-div .card').length;
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
        });
    }

    $(document).on("click", ".card", function (e) {
        var shopId = e.currentTarget.dataset.shopId;
        window.location.href=shopUrl+"?shopId=" +shopId;
    })

    $(document).on('infinite', '.infinite-scroll-bottom', function() {
        if (loading)
            return;
        addItems(pageSize, pageNum);
    });

    $(document).on("click", "#me", function () {
        $.openPanel("#panel-left-demo");
    });

    $("#shoplist-search-div").on("click", ".button", function (e) {
        if(flag){
            parentId = "";
        }
        var categoryId = e.currentTarget.dataset.categoryId;
        if (parentId) {// 如果传递过来的是一个父类下的子类
            shopCategoryId = categoryId;
            if ($(e.target).hasClass("button-fill")) {
                $(e.target).removeClass('button-fill');
                shopCategoryId = "";
            } else {
                $(e.target).addClass('button-fill').siblings().removeClass('button-fill');
            }
            $(".list-div").empty();
            pageNum = 1;
            addItems(pageSize, pageNum);
            $.init();
        } else {// 如果传递过来的父类为空，则按照父类查询
            parentId = categoryId;
            if ($(e.target).hasClass("button-fill")) {
                $(e.target).removeClass('button-fill');
                parentId = "";
            } else {
                $(e.target).addClass('button-fill').siblings().removeClass('button-fill');
            }
            $(".list-div").empty();
            pageNum = 1;
            addItems(pageSize, pageNum);
            $.init();
        }

    });

    $("#area-search").change(function () {
        areaId = $(this).val();
        $(".list-div").empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
        $.init();
    });

    $("#search").on('input', function (e) {
        shopName = e.currentTarget.value;
        $(".list-div").empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
        $.init();
    });

    $.init();
});