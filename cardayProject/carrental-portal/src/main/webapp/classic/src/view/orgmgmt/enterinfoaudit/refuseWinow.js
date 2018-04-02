Ext.define('Admin.view.orgmgmt.enterinfoaudit.refuseWinow', {
	extend: 'Ext.window.Window',	
    alias: "widget.refuseWinow",
	width : 400,
	resizable : false,// 窗口大小是否可以改变
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	controller: {
        xclass: 'Admin.view.orgmgmt.enterinfoaudit.ViewController'
    },
	header:false,
	items:[{
		xtype:'form',
		reference: 'refuseForm',
		layout : 'form',		
		items:[{
			xtype: 'textareafield',
	        name: 'comments',
	        emptyText:'驳回原因'
		},{
			xtype:'hidden',
			name:'id',
			value:''
		}]
	}],
	buttonAlign : 'center',
	buttons : [{
				text : '确定',
				handler:'refuseEvent'
			},{
				text: '关闭',
				handler: function(btn){
					btn.up('refuseWinow').close();
				}
			} ]
	
});