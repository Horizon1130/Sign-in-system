;layui.define("jquery", function (e) {
    "use strict";
    var t = layui.$, i = function (data, callback, errorCallback) {
        if (data.code == '10000') {
            callback();
        } else if (data.code > 10000) {
            if (errorCallback) {
                errorCallback();
            }else {
                layer.alert(data.errorMsg, {
                    icon: 2
                });
            }
        }
    };
    e("handler", i)
});