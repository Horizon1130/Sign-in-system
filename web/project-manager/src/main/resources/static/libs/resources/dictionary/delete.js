layui.use(['table', 'form', 'handler'], function () {
    var table = layui.table;
    var $ = layui.$;
    var handler = layui.handler;

    $('#btn_delete').on('click', function () {
        var checkStatus = table.checkStatus('mainTable');
        if (checkStatus.data.length < 1) {
            layer.alert('请选择一条删除记录', {
                icon: 0
            });
            return;
        } else {
            var ids = new Array();
            checkStatus.data.forEach(function (e) {
                ids.push(e.dictionary_id);
            });
            layer.confirm('您选中的记录，确定要删除吗?', {
                icon: 3,
                title: '提示',
                yes: function (index) {
                    layer.closeAll('dialog');
                    var loadding = layer.msg('玩命加载中，请稍候...', {icon: 16, time: false, shade: 0.4});
                    $.ajax({
                        url: parent.appBaseUri + '/v1/resources/dictionary/delete',
                        type: 'delete',
                        data: {
                            ids: ids.toString()
                        },
                        dataType: 'json',
                        success: function (data) {
                            handler(data, function(){
                                layer.msg(data.msg, {
                                    icon: 1
                                });
                                tableReload();
                            });
                            layer.close(loadding);
                        },
                        error: function (data, status, e) {
                            layer.close(loadding);
                            layer.alert("删除错误", {
                                icon: 2
                            });
                        }
                    });
                }
            });

        }
    });
});