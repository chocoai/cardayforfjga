Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.DeptAllocation', {
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
	
	id:'vehMgmtDeptAllocationWin',
	
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
							text : '分配到所属部门',
							style : 'font-size: 20px',
							margin : '0 0 20 0'
						}, {
							xtype : 'combo',
//							valueField : 'id',
							itemId: 'itemOrg',
							name: 'arrangedOrgName',
							hideLable : true,
					//		queryMode : 'local',
//							queryMode : 'remote',
							reference : 'deptId',
//							publishes : 'rawValue',
							editable  : false,
//							displayField : 'name',
							blankText: '不能为空',//提示信息
							editable : false,
							formBind:true,
							width : 180,
							emptyText : '请选择...',
							listeners:{
								expand:'openDeptSetWin',
							}
						},
						{
//							id: 'arranged_Org_Id',
							fieldLabel : '所属部门Id',
							name : 'arrangedOrgId',
							hidden : true
						}, 
						{
							fieldLabel : '所属部门名称',
							name : 'orgName',
							formBind:true,
//							bind : '{deptId.rawValue}',
							hidden : true
						}, {
							fieldLabel : '车辆Id',
							name : 'vehicleId',
							hidden : true
						}],
				buttonAlign : 'center',
				buttons : [{
							text : '分配',
							handler :'onAllocateDeptClick'
						}, {
							text : '关闭',
							handler : function(btn) {
								this.up('window').close();
							}
						}],
			}],
				/*buttonAlign : 'center',
				buttons : [{
							text : '分配',
							handler :'onAllocateDeptClick'
						}, {
							text : '关闭',
							handler : function(btn) {
								this.up('window').close();
							}
						}],*/
});