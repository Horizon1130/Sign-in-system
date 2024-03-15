layui.use(['table', 'form', 'util', 'handler'], function () {
    var table = layui.table;
    var $ = layui.jquery;
    var util = layui.util;
    var handler = layui.handler;
    var form = layui.form;

    function createTable() {
        table.render({
            elem: '#lay_table_main',
            url: parent.appBaseUri + '/v1/resources/news/list',
            cols: [[{
                checkbox: true,
                fixed: true
            }, {
                field: 'title',
                title: '新闻标题',
                width: 400
            }, {
                field: 'type',
                title: '新闻分类',
                width: 200,
                templet: function (d) {
                    var title = '无分类';
                    if (d.type){
                        if (typeList){
                            typeList.forEach(function(v, i){
                                if (v.dictionary_id == d.type) {
                                    title = v.title;
                                }
                            });
                        }
                    }
                    return title;
                }
            }, {
                field: 'content',
                title: '新闻描述',
                width: 500
            }, {
                field: 'status',
                title: '状态',
                width: 120,
                align: 'center',
                templet: function (d) {
                    var status = d.status;
                    var text = '未发布';
                    if (status == 1) {
                        text = '已发布';
                    }
                    return '<span style="color: #1E9FFF">' + text + '</span>';
                }
            }, {
                field: 'createTime',
                title: '创建时间',
                sort: true,
                width: 180,
                align: 'center',
                templet: function (d) {
                    var createTime = util.toDateString(d.createTime, "yyyy-MM-dd HH:mm:ss");
                    return createTime;
                }
            }]],
            id: 'mainTable',
            page: true,
            method: 'post',
            loading: true,
            height: 'full-200',
            response: {
                // 数据状态的字段名称，默认：code
                statusName: 'code',
                // 成功的状态码，默认：0
                statusCode: 10000,
                // 状态信息的字段名称，默认：msg
                msgName: 'errorMsg',
                // 数据总数的字段名称，默认：count
                countName: 'total',
                // 数据列表的字段名称，默认：data
                dataName: 'list'
            },
            request: {
                // 页码的参数名称，默认：page
                pageName: 'current',
                // 每页数据量的参数名，默认：limit
                limitName: 'size'
            },
            limits: [20, 50],
            limit: 20
        });
    }

    $(function () {
        loadTypeAll();
    });

    var typeList;

    function loadTypeAll() {
        $.ajax({
            url: parent.appBaseUri + '/v1/resources/dictionary/all?classify=classify',
            type: 'post',
            dataType: 'json',
            success: function (data) {
                handler(data, function () {
                    typeList = data.list;
                    createTable();
                });
            },
            error: function (error) {
                layer.alert(error.errorMsg, {
                    icon: 2
                });
            }
        });
    }

    $('#btn_search').on('click', function () {
        tableReload();
    });

    $('#btn_reset').on('click', function () {
        $("input").val("");
        $("select").val("");
        tableReload();
    });

});

function tableReload() {
    var table = layui.table;
    var $ = layui.jquery;
    table.reload('mainTable', {
        where: {
            title: $('#search_text_question').val(),
            status: $('#search_cmd_status').val()
        }, page: {
            curr: 1 //重新从第 1 页开始
        }
    });
}
