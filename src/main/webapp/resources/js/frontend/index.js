$(function () {
    var initUrl = "/o2o/frontend/getmainpageinfo";

    $.getJSON(initUrl, function (data) {
        if (data.success) {
            var headLineHtml = "";
            data.headLineList.map(function (item, index) {
                headLineHtml += "<div class=\"swiper-slide img-wrap\">" +
                    "<img class=\"banner-img\" data-link='"+item.lineLink+"' src='"+item.lineImg+"' alt='"+item.lineName+"'>" +
                "</div>";
            });
            $(".swiper-wrapper").html(headLineHtml);

            $(".swiper-container").swiper({
                autoplay: 3000,
                autoplayDisableOnInteraction: false
            });

            var shopCategoryHtml = "";
            data.shopCategoryList.map(function (item) {
                shopCategoryHtml += "<div class=\"col-50 shop-classify\" data-id='"+item.shopCategoryId+"'>" +
                    "<div class='word'><p class='shop-title'>"+item.shopCategoryName+"</p><p class='shop-desc'>"+item.shopCategoryDesc+"</p></div>" +
                    "<div class='shop-classify-img-warp'><img class='shop-img' src='"+item.shopCategoryImg+"'></div></div>"
            });
            $(".row").html(shopCategoryHtml);
        }
    });
    $(document).on("click", ".shop-classify", function (e) {
        var parentId = e.currentTarget.dataset.id;
        window.location.href="/o2o/frontend/shoplist?parentId="+parentId;
    });

    $(document).on("click", "#me", function () {
        $.openPanel("#panel-left-demo");
    });
});