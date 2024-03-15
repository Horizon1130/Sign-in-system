layui.use([ 'form' ], function() {
	var $ = layui.jquery;

	$('#btn_edit').on('click', function() {
		var selected = mainTree.getSelected();

		if (selected.length < 1) {
			layer.alert('请选择一条修改记录', {
				icon : 0
			});
			return;
		} else if(selected.length > 1){
			layer.alert('修改操作不可多选', {
				icon : 0
			});
			return;
		}
		;
		var id = selected[0].id;
		layer.open({
			title : '修改资源',
			type : 2,
			// 设置遮挡层
			shade : [ 0.7, '#393D49' ],
			// 点击遮挡层是否关闭
			shadeClose : false,
			anim : 3,
			area : ['500px','560px'],
			resize : false,
			scrollbar : false,
			fixed : false,
			content : parent.appBaseUri + '/settings/resource/modify?resourceId=' + id
		});
	});
});
