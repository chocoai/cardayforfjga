Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.DriverAllocation', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.Panel'],
	reference : 'driverAllocation',
	bodyPadding : 10,
	constrain : true,
	closable : true,
	resizable : false,
	ghost : false,
	modal: true,
	width: 380,
	height: 220,
	resizable : false,// 窗口大小是否可以改变
	controller : {
		xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewController'
	},
//	viewModel : {
//		xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewModel'
//	},
	
	layout : 'hbox',
	items : [
	{
		xtype : 'component',
		width : 100,
		height : 100,
		/*
		 * border: 1, style: { borderColor: 'red', borderStyle: 'solid' },
		 */
		margin : '0 50 0 0',
		autoEl : {
			tag : 'img',
			src : 'resources/images/driver.png'
		}
	},
	{ 
		xtype : 'form',
		defaultType : 'textfield',
		items : [
		{
			id: 'driver_allocate_id',
			xtype : 'textfield',
			name:'vehicleId',
			fieldLabel : '车辆Id',
			hidden: true,
		},
		{
			xtype : 'tbtext',
			name:'allocTbtext',
			text : '分配默认司机',
			style : 'font-size: 20px',
			margin : '0 0 20 0'
		},
		{
			xtype : 'combo',
			valueField : 'id',
			itemId: 'itemDriver',
			name: 'arrangedDriver',
			hideLable : true,
			queryMode : 'remote',
			publishes : 'rawValue',
			editable  : false,
			displayField : 'realnameAndPhone',
			width : 200,
			emptyText : '请选择...',
			store : Ext.create('Ext.data.Store', {
				proxy : {
					type : 'ajax',
					url : 'vehicle/findAvailableDriversByVehicleId',
					reader : {
						type : 'json',
						rootProperty : 'data',
						successProperty : 'status'
					}
				},
				autoLoad : false,
				listeners : {
					load : function(store, records,options) {
						store.insert(0, {"realnameAndPhone" : "暂不分配","id" : "-1"});
					},
				
					beforeload : function(store, operation, eOpts) {
		        		console.log('+++++beforeload++++');
		        		var vehicleId = Ext.getCmp('driver_allocate_id').getValue();
		        		var input = {'vehicleId' : vehicleId};
		        		var param = {'json': Ext.encode(input)};
		        		Ext.apply(store.proxy.extraParams, param);
		        	}
				}
			}),
		}]
	}],
			
	buttonAlign : 'center',
	buttons : [
        {
			text : '分配',
			handler :'onAllocateDriverClick'
		}, 
		{
			text : '关闭',
			handler : function(btn) {
				this.up('window').close();
		}
	}],
});