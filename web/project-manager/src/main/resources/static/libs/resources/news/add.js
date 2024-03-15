layui.use([ 'table', 'laydate', 'form' ], function() {
	var table = layui.table;
	var $ = layui.jquery;
	var laydate = layui.laydate;

	$('#btn_add').on('click', function() {
		var layId = parent.$('.layui-tab-title .layui-this').attr('lay-id')

		var menuItem = {
			url: parent.appBaseUri + '/resources/news/add?layId=' + layId,
			text: '添加新闻',
			id: 'addNewsWindows'
		};
		parent.addTab(menuItem);
	});
});
