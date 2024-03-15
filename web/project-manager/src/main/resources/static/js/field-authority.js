layui.use(['form', 'element', 'handler', 'urlparams'], function () {
    var $ = layui.jquery;
    var form = layui.form;
    var handler = layui.handler;
    var urlparams = layui.urlparams;

    var shopId = urlparams('shopId');

    $.ajax({
        url: parent.parent.appBaseUri + '/v1/authority/fields?shopId=' + shopId,
        type: 'post',
        dataType: 'json',
        async:false,
        success: function (data) {
            handler(data, function () {
                if (data.skip) {
                    $('div[field-authority]').show();
                } else {
                    data.data.forEach(function (v, i) {
                        $('div[field-authority="' + v + '"]').show();
                    });
                }
                form.render();
            });
        },
        error: function (error) {
            layer.alert(error.errorMsg, {
                icon: 2
            });
        }
    });
});