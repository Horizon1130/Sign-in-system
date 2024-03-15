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
		var id = checkStatus.data[0].sysUserId;

		var layId = parent.$('.layui-tab-title .layui-this').attr('lay-id');

		var menuItem = {
			url: parent.appBaseUri + '/resources/staff/modify?userId=' + id + '&layId=' + layId,
			text: '修改学生',
			id: id
		};
		parent.addTab(menuItem);
	});
});
