Ext.define('Admin.view.orgmgmt.enterinfomgmt.OpenServiceWindow', {
	extend: 'Ext.window.Window',	
    alias: "widget.openServiceWindow",
	reference: 'openServiceWindow',
	title : '开通服务',
	width : 330,
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
	bodyPadding:20,
	items:[{
			xtype:'container',
			html:'',
			style:'font-size: 16px'
		},{
			xtype:'hidden',
			name:'entId'
		//	hidden:true
		}],
	buttonAlign: 'center',
	buttons:[{
				text: '开通',
				handler: 'openService'
			},{
				text : '取消',
				handler: function(btn){
					btn.up("window").close();
				}
			}]
});