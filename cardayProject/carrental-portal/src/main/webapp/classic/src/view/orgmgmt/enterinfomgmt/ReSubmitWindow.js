Ext.define('Admin.view.orgmgmt.enterinfomgmt.ReSubmitWindow', {
	extend: 'Ext.window.Window',	
    alias: "widget.reSubmitWindow",
	reference: 'reSubmitWindow',
	title : '重新提交审核',
	width : 400,
	height:350,
	scrollable: true,
	closable:false,
	ghost:false,
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:10px 5px",	
	defaults:{
	},	
	controller: {
        xclass: 'Admin.view.orgmgmt.enterinfomgmt.ViewController'
    },
	frame : true,
	bodyPadding:10,
	items:[{
			xtype:'container',
			html:'<div style="font-size:16px;color:red;font-weight: bold;text-align: center;">审核历史记录<div>',
			margin : '0 0 20 0'
		//	style:'font-size: 16px'
		},{
			xtype:'container',
			name:'auditHistory',
			//margin : '0 0 20 0',
			html:''
		},{
			xtype:'hidden',
			fieldLabel:'企业Id',
			name : 'id'
	}],
	buttonAlign: 'center',
	buttons:[{
				text: '确定',
				handler: 'reSubmit'
			},{
				text : '取消',
				handler: function(btn){
					btn.up("window").close();
				}
			}]
});