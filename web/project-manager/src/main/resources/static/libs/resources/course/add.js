layui.use([ 'table', 'laydate', 'form' ], function() {
	var table = layui.table;
	var $ = layui.jquery;
	var laydate = layui.laydate;

	$('#btn_add').on('click', function() {
		layer.open({
			title : '添加课程',
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
			content : parent.appBaseUri + '/resources/course/add'
		});
	});
});
