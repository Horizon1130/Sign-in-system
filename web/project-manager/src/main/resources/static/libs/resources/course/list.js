layui.use(['table', 'form', 'util', 'handler'], function () {
    var table = layui.table;
    var $ = layui.jquery;
    var util = layui.util;
    var handler = layui.handler;
    var form = layui.form;

    var menu_table = table.render({
        elem: '#lay_table_main',
        url: parent.appBaseUri + '/v1/resources/course/list',
        cols: [[{
            checkbox: true,
            fixed: true
        }, {
            field: 'title',
            title: '课程标题',
            width: 300
        },
            {
                field: 'teacher',
                title: '任教老师',
                width: 200
            },
            {
                field: 'time',
                title: '教课时间',
                width: 200
            },
            {
                field: 'content',
                title: '课程描述',
                width: 300
            }, {
                field: 'create_time',
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
            title: $('#search_text_title').val()
        }, page: {
            curr: 1 //重新从第 1 页开始
        }
    });
}
