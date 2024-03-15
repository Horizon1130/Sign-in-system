var tabFilter, menu = [], liIndex, curNav, delMenu;
layui.define(["element", "jquery"], function (exports) {
    var element = layui.element,
        $ = layui.jquery,
        layId,
        Tab = function () {
            this.tabConfig = {
                closed: true,
                openTabNum: undefined,  //最大可打开窗口数量
                tabFilter: "tab_context",  //添加窗口的filter
                url: undefined  //获取菜单json地址
            }
        };

    //获取二级菜单数据
    Tab.prototype.render = function () {
        var url = this.tabConfig.url;
        $.post(url, function (data) {
            $(".navBar").html('');
            //显示左侧菜单
            if ($(".navBar").html() == '') {
                var _this = this;
                $(".navBar").html(navBar(data)).height($(window).height() - 245);
                element.init();  //初始化页面元素
                $(window).resize(function () {
                    $(".navBar").height($(window).height() - 245);
                })
            }
        })
    }

    //参数设置
    Tab.prototype.set = function (option) {
        var _this = this;
        $.extend(true, _this.tabConfig, option);
        return _this;
    };

    //通过title判断tab是否存在
    Tab.prototype.hasTab = function (menuId) {
        var result = false;
        $(".layui-tab-title li").each(function () {
            var layId = $(this).attr("lay-id");
            if (layId == menuId) {
                result = true;
            }
        });
        return result;
    }

    //右侧内容tab操作
    var tabIdIndex = 0;
    Tab.prototype.tabAdd = function (_this) {
        var that = this;
        var openTabNum = that.tabConfig.openTabNum;
        tabFilter = that.tabConfig.tabFilter;
        if (_this.target == "_blank") {
            window.location.href = _this.url;
        } else if (_this.url != undefined) {
            var title = '';
            if (_this.icon != undefined) {
                title += '<i class="layui-icon layui-tab-node">' + _this.icon + '</i>';
            }
            var id = _this.url;
            //已打开的窗口中不存在
            if (!that.hasTab(id)) {
                if ($(".layui-tab-title li").length == openTabNum) {
                    layer.msg('只能同时打开' + openTabNum + '个选项卡哦。不然系统会卡的！');
                    return;
                }
                tabIdIndex++;
                title += _this.text;
                element.tabAdd(tabFilter, {
                    title: title,
                    content: "<iframe src='" + id + "' data-id='" + tabIdIndex + "' class='layui-layer-load' onload=this.className=''></iframe>",
                    id: id
                })
                element.tabChange(tabFilter, id);
            } else {
                element.tabChange(tabFilter, id);
            }
        }
    }

    Tab.prototype.tabDel = function (_this) {
        var that = this;
        var openTabNum = that.tabConfig.openTabNum;
        tabFilter = that.tabConfig.tabFilter;
        if (that.hasTab(_this)) {
            element.tabDelete(tabFilter, _this);
        }
    }

    var bodyTabs = new Tab();
    exports("bodyTabs", function (option) {
        return bodyTabs.set(option);
    });
})