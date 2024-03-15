layui.use([ 'table', 'form' ], function() {
	var $ = layui.jquery , table = layui.table;

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
		var id = checkStatus.data[0].id;
		layer.open({
			title : '修改课程',
			type : 2,
			// 设置遮挡层
			shade : [ 0.7, '#393D49' ],
			// 点击遮挡层是否关闭
			shadeClose : false,
			anim : 3,
			area : ['490px','470px'],
			resize : false,
			scrollbar : false,
			fixed : false,
			content : parent.appBaseUri + '/resources/course/modify?id=' + id
		});
	});
});
