Ext.define('Admin.view.vehiclespurchase.vehpurchaseapprove.VehPurchaseRefuseWin', {
	extend: 'Ext.window.Window',
	   	
    alias: "widget.vehPurchaseRefuseWin",
    controller: {
        xclass: 'Admin.view.vehiclespurchase.vehpurchaseapprove.ViewController'
    },
	title : '申请单驳回',
	width : 400,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	closable : true,//右上角的x是否显示
	items : [{
		xtype:'form',
		layout : 'vbox',
		
		bodyStyle: {
			padding: '30px'  //与边界的距离
			},
		items: [
			{
				xtype: 'displayfield',
				fieldLabel: '请填写驳回理由',
			}, 
			{
				xtype: 'textarea',
				width: 300,
				name: 'refuseComments'
			},]
	}],

	buttonAlign : 'center',
	buttons : [
		{
			text : '确认驳回',
			handler: function(btn) {
                Ext.Msg.alert('提示', '驳回此申请单！');
				btn.up("vehPurchaseRefuseWin").close();
			}
		},
		{
			text : '取消',
			handler: function(btn) {
				btn.up("vehPurchaseRefuseWin").close();
			}
		}
	]
});