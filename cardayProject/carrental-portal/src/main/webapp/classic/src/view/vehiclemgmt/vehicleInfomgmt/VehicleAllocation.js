Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.VehicleAllocation', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.Panel'],
	reference : 'vehicleAllocation',
	bodyPadding : 10,
	constrain : true,
	closable : true,
	resizable : false,
	ghost : false,
	modal: true,
	resizable : false,// 窗口大小是否可以改变
	controller : {
		xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewController'
	},
	viewModel : {
		xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewModel'
	},
	
	
	
	layout : 'hbox',
	items : [{
				xtype : 'component',
				width : 100,
				height : 100,
				/*
				 * border: 1, style: { borderColor: 'red', borderStyle: 'solid' },
				 */
				margin : '0 50 0 0',
				autoEl : {
					tag : 'img',
					src : 'resources/images/car.png'
				}
			}, { 
				xtype : 'form',
				defaultType : 'textfield',
				items : [{
							xtype : 'tbtext',
							name:'allocTbtext',
							text : '分配到所属机构',
							style : 'font-size: 20px',
							margin : '0 0 20 0'
						}, {
							xtype : 'combo',
							valueField : 'id',
							itemId: 'itemOrg',
							name: 'arrangedOrgName',
							hideLable : true,
					//		queryMode : 'local',
							queryMode : 'remote',
							reference : 'deptId',
							publishes : 'rawValue',
							editable  : false,
							displayField : 'name',
							width : 180,
							emptyText : '请选择...',
							store : Ext.create('Ext.data.Store', {
										proxy : {
											type : 'ajax',
											url : 'vehicle/listAvailableAssignedEnterprise',
											reader : {
												type : 'json',
												rootProperty : 'data',
												successProperty : 'status'
											}
										},
										autoLoad : false,
										listeners : {
											load : function(store, records,
													options) {
												store.insert(0, {
															"name" : "暂不分配",
															"id" : "-1"
														})
											}
										}
									}),
								listeners:{
							//		select: 'alloSelectOrgDone'
								}
						},/*{
							xtype : 'combo',
							valueField : 'id',
							itemId: 'itemVehicle',
							name: 'arrangedOrgName',
							hideLable : true,
							queryMode : 'local',
							reference : 'deptId',
							publishes : 'rawValue',
							editable  : false,
							displayField : 'name',
							width : 180,
							emptyText : '请选择...',
							store : Ext.create('Ext.data.Store', {
										proxy : {
											type : 'ajax',
											url : 'vehicle/listAvailableAssignedEnterprise',
											reader : {
												type : 'json',
												rootProperty : 'data',
												successProperty : 'status'
											}
										},
										autoLoad : true,
										//autoLoad : false,
										listeners : {
											load : function(store, records,
													options) {
												store.insert(0, {
															"name" : "暂不分配",
															"id" : "-1"
														})
											}
										}
									}),
								listeners:{
									select: 'alloSelectOrgDone'
								},
								hidden:true
						},*/
						{
							id: 'arranged_Org_Id',
							fieldLabel : '所属部门Id',
							name : 'arrangedOrgId',
							hidden : true
						}, 
						{
							fieldLabel : '所属部门名称',
							name : 'orgName',
							bind : '{deptId.rawValue}',
							hidden : true
						}, {
							fieldLabel : '车辆Id',
							name : 'vehicleId',
							hidden : true
						}]
			}],
				buttonAlign : 'center',
				buttons : [{
							text : '分配',
							handler :'onAllocateVehicleClick'
						}, {
							text : '关闭',
							handler : function(btn) {
								this.up('window').close();
							}
						}],
});