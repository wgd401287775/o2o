function changeVerifyCode(img){
    img.src = "../Kaptcha?" + Math.floor(Math.random() * 100);
}

/**
 * 根据url路径获取参数
 * @param name
 */
function getQueryString(name) {
    var reg = new RegExp("(^|&)"+name+"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r != null){
        return decodeURIComponent(r[2]);
    }
    return '';
}