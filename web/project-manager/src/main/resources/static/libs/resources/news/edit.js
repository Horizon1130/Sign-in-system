layui.use([ 'table', 'laydate', 'form' ], function() {
	var table = layui.table;
	var $ = layui.jquery;
	var laydate = layui.laydate;

	$('#btn_edit').on('click', function() {
		var checkStatus = table.checkStatus('mainTable');
		if (checkStatus.data.length < 1) {
			layer.alert('请选择一条修改记录', {
				icon : 0
			});
			return;
		} else if(checkStatus.data.length > 1){
			layer.alert('修改操作不可多选', {
				icon : 0
			});
			return;
		}
		var id = checkStatus.data[0].newsId;


		var layId = parent.$('.layui-tab-title .layui-this').attr('lay-id')

		var menuItem = {
			url: parent.appBaseUri + '/resources/news/modify?layId=' + layId + '&newsId=' + id,
			text: '修改新闻',
			id: 'modifyNewsWindows'
		};
		parent.addTab(menuItem);
	});
});
