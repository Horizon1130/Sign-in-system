layui.use(['table', 'form', 'util'], function () {
	var table = layui.table;
	var $ = layui.jquery;
	var util = layui.util;

	var menu_table = table.render({
		elem: '#lay_table_main',
		url: parent.appBaseUri + '/v1/settings/role/list',
		cols: [[{
			checkbox: true,
			fixed: true
		}, {
			field: 'title',
			title: '角色标题',
			width: 220
		}, {
			field: 'code',
			title: '角色编码',
			width: 150,
		}, {
			field : 'status',
			title : '状态',
			width : 100,
			align : 'center',
			templet: function (d) {
				if (d.status != 1) {
					return '<span class="layui-badge-dot"></span>禁用';
				}
				return '<span class="layui-badge-dot layui-bg-green"></span>正常';
			}
		}, {
			field: 'detail',
			title: '角色描述',
			width: 300
		}]],
		id : 'mainTable',
		page : true,
		method : 'post',
		loading : true,
		height : 'full-200',
		response : {
			// 数据状态的字段名称，默认：code
			statusName : 'code',
			// 成功的状态码，默认：0
			statusCode : 10000,
			// 状态信息的字段名称，默认：msg
			msgName : 'errorMsg',
			// 数据总数的字段名称，默认：count
			countName : 'total',
			// 数据列表的字段名称，默认：data
			dataName : 'list'
		},
		request : {
			// 页码的参数名称，默认：page
			pageName : 'current',
			// 每页数据量的参数名，默认：limit
			limitName : 'size'
		},
		limits: [20, 50],
		limit: 20
	});

	table.on('checkbox(main)', function (obj) {
		//console.log(obj.checked); // 当前是否选中状态
		//console.log(obj.data); // 选中行的相关数据
		//console.log(obj.type); // 如果触发的是全选，则为：all，如果触发的是单选，则为：one
	});

	$('#btn_search').on('click', function () {
		tableReload();
	});

	$('#btn_reset').on('click', function () {
		$("input").val(null);
		$("select").val(null);
		tableReload();
	});
});

function tableReload() {
	var table = layui.table;
	var $ = layui.jquery;
	table.reload('mainTable', {
		where: {
			title: $('#search_text_title').val(),
			code: $("#search_text_code").val(),
			status: $("#search_cmd_status").val(),
		}, page: {
			curr: 1 //重新从第 1 页开始
		}
	});
}
