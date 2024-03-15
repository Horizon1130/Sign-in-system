var $, tab;
layui.config({
    base: appBaseUri + "/js/"
}).use(['bodyTabs', 'form', 'element', 'layer', 'jquery', 'handler'], function () {
    var layer = layui.layer,
        element = layui.element,
        form = layui.form,
        handler = layui.handler;
    $ = layui.jquery;

    function loadMenus(){
        tab = layui.bodyTabs({
            openTabNum: "20",  //最大可打开窗口数量
            url: appBaseUri + "/v1/authority/menus" //获取菜单json地址
        });

        //渲染左侧菜单
        tab.render();
    }

    $(document).ready(function () {
        loadMenus();
    });

    // 添加新窗口
    $("body").on("click", "a[name='menus']", function () {
        //如果不存在子级
        if ($(this).siblings().length == 0) {
            var menuItem = {
                target: $(this).attr("target"),
                url: $(this).attr("data-url"),
                icon: $(this).find("i.layui-icon").attr("data-icon"),
                text: $(this).text()
            };
            addTab(menuItem);
        }
    });

    $("#btn_exit").on("click", function () {
        layer.confirm('您确认要退出系统吗？', {
            icon: 3,
            title: '提示',
            yes: function (index) {
                var loadding = layer.msg('玩命加载中，请稍候...', {icon: 16, time: false, shade: 0.4});
                $.ajax({
                    url: appBaseUri + '/logout',
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        layer.close(loadding);
                        if (data.code) {
                            location.href = appBaseUri + '/login';
                        }
                    },
                    error: function (data, status, e) {
                        layer.close(loadding);
                        layer.alert("操作错误", {
                            icon: 2
                        });
                    }
                });
            }
        });
    });

    $('#btn_update_pwd').on('click', function () {
        layer.open({
            title: '修改密码',
            type: 2,
            // 设置遮挡层
            shade: [0.7, '#393D49'],
            // 点击遮挡层是否关闭
            shadeClose: false,
            anim: 3,
            area: ['400px', '320px'],
            resize: false,
            scrollbar: false,
            fixed: false,
            content: appBaseUri + '/modify/password'
        });
    });

    $('#btn_user_info').on('click', function () {
        var layId = parent.$("li.layui-this").attr("lay-id");

        var menuItem = {
            url: parent.appBaseUri + '/modify/user',
            text: '更改信息',
            id: 'modifyUserWindows'
        };
        parent.addTab(menuItem);
    });

})

//打开新窗口
function addTab(_this) {
    tab.tabAdd(_this);
}

//打开新窗口
function delTab(_this) {
    tab.tabDel(_this);
}
