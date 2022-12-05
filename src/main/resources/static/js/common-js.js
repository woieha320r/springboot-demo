const api = "http://localhost:9000/api"
let github_client_id;
// 需于github配置一致
const github_redirect_uri = "http://localhost:9000/api/static/login.html";
// github获取授权码的接口地址
const github_get_code_url = "https://github.com/login/oauth/authorize";

/**
 * 生成指定长度的字符串
 * @param {Number} hex 代表进制，取值范围[2 - 36]，最大不能超过 36, 数字越大字母占比越高，小于11为全数字
 * @param {Number} len 字符串长度
 */
function randomStr(hex, len) {
    if (hex < 2 || hex > 36) {
        throw new RangeError("hex argument must be between 2 and 36");
    }

    var res = Math.random().toString(hex).slice(2);
    var resLen = res.length;

    while (resLen < len) {
        res += Math.random().toString(hex).slice(2);
        resLen = res.length;
    }
    return res.substr(0, len);
}

/**
 * 从url处获取路径参数
 */
function getQueryString(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    let r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}

/**
 * 对象为空判断
 */
function isEmpty(obj) {
    return typeof obj == "undefined" || obj == null || obj === "";
}
