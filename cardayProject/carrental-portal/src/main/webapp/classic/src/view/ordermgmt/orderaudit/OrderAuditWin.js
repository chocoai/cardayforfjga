Ext.define('Admin.view.ordermgmt.orderaudit.OrderAuditWin', {
	extend: 'Ext.window.Window',
	   	
    alias: "widget.orderauditwin",
    controller: 'orderauditcontroller',
	title : '订单审核',
	width : 400,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	closable : true,//右上角的x是否显示
	items : [{
		id: 'orderauditwin_id',
		xtype:'form',
		layout : 'vbox',
		defaultType: 'displayfield',
		
		bodyStyle: {
			//  background: '#ffc',
			    padding: '15px'  //与边界的距离
		},
		
		fieldDefaults: {
            labelAlign: 'left',
            labelWidth: 100,
            height:20,    
        },
        
		items: [{
			fieldLabel: '订单编号',
	        name: 'orderNo',
		}, 
		{
			fieldLabel: '订单日期',
	        name: 'orderTimeF',
		},
		{
			xtype: 'textfield',
			fieldLabel: '订单日期',
	        name: 'orderTimeF1',
	        hidden: true
		},
		{
			fieldLabel: '用车人',
	        name: 'orderUsername',
		}, 
		{	
			fieldLabel: '用车电话',
	        name: 'orderUserphone',
		},
		{
			fieldLabel: '预约用车时间',
			name: 'planStTimeF',
		},
		{
			xtype: 'textfield',
			fieldLabel: '订单Id',
			name: 'id',
			hidden: true
		},	
		{
			xtype: 'textfield',
			fieldLabel: '订单编号',
			name: 'orderNo1',
			hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '用车人',
	        name: 'orderUser1',
	        hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '用车人电话',
	        name: 'orderUserphone1',
	        hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '预约用车时间',
			name: 'planStTimeF1',
			hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '预约用车结束时间',
			name: 'planEdTimeF',
			hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '预约用车结束时间',
			name: 'planEdTimeF1',
			hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '状态',
			name: 'status',
			hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '城市',
			name: 'city',
			hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '公车性质',
			name: 'vehicleType',
			hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '出发地',
			name: 'fromPlace',
			hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '目的地',
			name: 'toPlace',
			hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '乘车人数',
			name: 'passengerNum',
			hidden: true
		},
		]
	}],

	buttonAlign : 'center',
	buttons : [
		{
			text : '审核通过',
			handler: 'orderAuditDone',
		},
		{
			text : '取消审核',
			handler: function(btn) {
				btn.up("orderauditwin").close();
			}
		}
	]
});