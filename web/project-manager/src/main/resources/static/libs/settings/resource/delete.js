layui.use([ 'form', 'handler' ], function() {
	var $ = layui.$;
	var handler = layui.handler;

	$('#btn_delete').on('click', function() {
		var selected = mainTree.getSelected();
		if (selected.length < 1) {
			layer.alert('请选择一条删除记录', {
				icon : 0
			});
			return;
		} else {
			var ids = new Array();
			selected.forEach(function(e) {
				ids.push(e.id);
			});
			layer.confirm('您选中的记录，确定要删除吗?', {
				icon : 3,
				title : '提示',
				yes : function(index) {
					layer.closeAll('dialog');
					var loadding = layer.msg('玩命加载中，请稍候...',{icon: 16,time:false,shade:0.4});
					$.ajax({
						url : parent.appBaseUri + '/v1/settings/resource/delete',
						type : 'delete',
						data : {
							ids : JSON.stringify(ids)
						},
						dataType : 'json',
						success : function(data) {
							handler(data, function(){
								layer.msg(data.msg, {
									icon: 1
								});
								tableReload();
							});
							layer.close(loadding);
						},
						error : function(data, status, e) {
							layer.close(loadding);
							layer.alert("删除错误", {
								icon : 2
							});
						}
					});
				}
			});
		}
	});
});