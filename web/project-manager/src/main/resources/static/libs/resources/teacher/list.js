layui.use(['table', 'form', 'util', 'handler'], function () {
    var table = layui.table;
    var $ = layui.jquery;
    var util = layui.util;
    var handler = layui.handler;
    var form = layui.form;

    var menu_table = table.render({
        elem: '#lay_table_main',
        url: parent.appBaseUri + '/v1/resources/teacher/list',
        cols: [[{
            checkbox: true,
            fixed: true
        }, {
            field: 'code',
            title: '教师  编号',
            width: 220
        }, {
            field: 'nice',
            title: '姓名',
            width: 160,
        }, {
            field: 'phone',
            title: '联系电话',
            width: 160,
        }, {
            field: 'position',
            title: '教师职称',
            width: 160,
        }, {
            field: 'email',
            title: '邮箱地址',
            width: 220,
        }, {
            field: 'createTime',
            title: '创建时间',
            sort: true,
            width: 180,
            align: 'center',
            templet: function (d) {
                var createTime = util.toDateString(d.create_time, "yyyy-MM-dd HH:mm:ss");
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
            code: $('#search_text_code').val(),
            nice: $('#search_text_nice').val()
        }, page: {
            curr: 1 //重新从第 1 页开始
        }
    });
}
