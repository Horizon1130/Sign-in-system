layui.use([ 'form' ], function() {
	var $ = layui.jquery;

	$('#btn_add').on('click', function() {
		var layId = parent.$('.layui-tab-title .layui-this').attr('lay-id')

		var menuItem = {
			url: parent.appBaseUri + '/resources/staff/add?layId=' + layId,
			text: '添加学生',
			id: 'addPersonnelWindows'
		};
		parent.addTab(menuItem);
	});
});
