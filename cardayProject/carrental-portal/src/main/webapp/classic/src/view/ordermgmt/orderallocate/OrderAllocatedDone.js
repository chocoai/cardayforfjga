Ext.define('Admin.view.ordermgmt.orderaudit.OrderAllocatedDone', {
	extend: 'Ext.window.Window',
	   	
    alias: "widget.orderallocateddone",
    controller: 'allocatingViewController',
	width: 400,
	height: 150,
	closable:false,//窗口是否可以改变
	resizable: false,// 窗口大小是否可以改变
	draggable: true,// 窗口是否可以拖动
	modal: true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	closable: true,//右上角的x是否显示

	buttonAlign : 'center',
	buttons : [
		{
			text : '完成',
			handler: function(btn) {
				btn.up("orderallocateddone").close();
			}
		}
	]
});