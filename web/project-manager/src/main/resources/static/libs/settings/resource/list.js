var mainTree, layer, form, $, handler;

layui.use(['form', 'treetable', 'layer', 'handler'], function () {
    layer = layui.layer, form = layui.form, $ = layui.jquery, handler = layui.handler;

    $('#btn_search').on('click', function () {
        tableReload();
    });

    $('#btn_reset').on('click', function () {
        $("input").val("");
        $("select").val("");
        tableReload();
    });

    tableReload();

});

function tableReload() {
    if (mainTree) {
        mainTree.destory();
    }

    $.ajax({
        url: parent.appBaseUri + '/v1/settings/resource/list',
        type: 'post',
        dataType: 'json',
        success: function (data) {
            handler(data, function () {
                createList(data.data);
            });

        },
        error: function (error) {
            layer.alert(error.errorMsg, {
                icon: 2
            });
        }
    });
}

function createList(nodes) {
    mainTree = layui.treetable({
        elem: '#lay_table_main', //传入元素选择器
        checkbox: true,
        nodes: nodes,
        layout: [
            {
                name: '标题',
                field: 'title',
                treeNodes: true,
                headerClass: 'value_col',
                colClass: 'value_col',
                style: 'width: 150'
                , render: function (d) {
                    console.log(d)
                }
            },
            {
                name: '请求地址', field: 'url', style: 'width: 220px;color: #01AAED'
                , render: function (d) {
                    var item = JSON.parse(d);
                    if (item.url) {
                        return item.url;
                    } else {
                        return '';
                    }
                }
            },
            {
                name: '权限标识', field: 'perms', style: 'width: 200px; color: #FF5722', render: function (d) {
                    var item = JSON.parse(d);
                    if (item.perms) {
                        return item.perms;
                    } else {
                        return '';
                    }
                }
            },
            {
                name: '图标', field: 'icon', style: 'width: 100px; text-align: center;', render: function (d) {
                    var item = JSON.parse(d);
                    if (item.icon) {
                        if (item.classify == 'BUTTON') {
                            return '<button class="' + item.style + '"><i class="layui-icon">' + item.icon + '</i></button>';
                        }
                        return '<i class="layui-icon">' + item.icon + '</i>';
                    }
                    else {
                        return '';
                    }
                }
            },
            {
                name: '资源分类',
                field: 'classify',
                style: 'width: 100px; text-align: center;',
                render: function (d) {
                    var item = JSON.parse(d);
                    if (item.classify == 'CATALOG') {
                        return '<span class="layui-badge layui-bg-green">目录</span>';
                    }
                    if (item.classify == 'MENU') {
                        return '<span class="layui-badge layui-bg-blue">菜单</span>';
                    }
                    return '<span class="layui-badge layui-bg-gray">按钮</span>';
                }
            },
            {name: '排序', field: 'sort', style: 'width: 80px; text-align: center;'},
            {
                name: '是否可见', field: 'visible', style: 'width: 100px; text-align: center;', render: function (d) {
                    var item = JSON.parse(d);
                    if (item.visible) {
                        return '<span class="layui-badge-dot layui-bg-green"></span>可见';
                    }
                    return '<span class="layui-badge-dot layui-bg-gray"></span>不可见';
                }
            },
            {
                name: '状态', field: 'status', style: 'width: 100px; text-align: center;', render: function (d) {
                    var item = JSON.parse(d);
                    if (item.status != 1) {
                        return '<span class="layui-badge-dot"></span>禁用';
                    }
                    return '<span class="layui-badge-dot layui-bg-green"></span>正常';
                }
            },
            {name: '更新时间', field: 'lastUpdateTime', style: 'width: 180px'}
        ]
    });
    form.render();
}