layui.use(['element', 'form', 'handler'], function () {
	var element = layui.element;
	var $ = layui.$;
	var handler = layui.handler;
	var form = layui.form;

	$(function () {
		var loadding = layer.msg('玩命加载中，请稍候...', {
			icon: 16,
			time: false,
			shade: 0.4
		});
		$.ajax({
			url: appBaseUri + '/v1/modify/user',
			type: 'get',
			dataType: 'json',
			success: function (res) {
				layer.close(loadding);
				handler(res, function () {
					$('#full_user_name').text(res.data.nice);
					$('#small_user_name').text(res.data.nice);
					if (res.data.face) {
						$('#full_user_face').attr('src', parent.appBaseUri + '/img/' + res.data.face);
						$('#small_user_face').attr('src', parent.appBaseUri + '/img/' + res.data.face);
					}else {
						$('#full_user_face').attr('src', parent.appBaseUri + '/images/empty_face.png');
						$('#small_user_face').attr('src', parent.appBaseUri + '/images/empty_face.png');
					}
					form.render();
				});
			},
			error: function (data, status, e) {
				layer.alert(data.errorMsg, {
					icon: 2
				});
				layer.close(loadding);
			}
		});
	});

	//进行切换
	var fullScreenClickCount = 0;
	//调用各个浏览器提供的全屏方法
	var handleFullScreen = function () {
		var de = document.documentElement;

		if (de.requestFullscreen) {
			de.requestFullscreen();
		} else if (de.mozRequestFullScreen) {
			de.mozRequestFullScreen();
		} else if (de.webkitRequestFullScreen) {
			de.webkitRequestFullScreen();
		} else if (de.msRequestFullscreen) {
			de.msRequestFullscreen();
		} else {
			layer.alert("当前浏览器不支持全屏！", {
				icon: 2
			});
		}

	};
	//调用各个浏览器提供的退出全屏方法
	var exitFullscreen = function () {
		if (document.exitFullscreen) {
			document.exitFullscreen();
		} else if (document.mozCancelFullScreen) {
			document.mozCancelFullScreen();
		} else if (document.webkitExitFullscreen) {
			document.webkitExitFullscreen();
		}
	}

	$('a[layadmin-event="fullscreen"]').click(function () {
		if (fullScreenClickCount % 2 == 0) {
			handleFullScreen();
		} else {
			exitFullscreen();
		}
		fullScreenClickCount++;
	});

	$('a[layadmin-event="refresh"]').click(function () {
		var layId = $('li.layui-this').eq(0).attr('lay-id');
		$('iframe[src="' + layId + '"]').attr('src', $('iframe[src="' + layId + '"]').attr('src'));
	});

});