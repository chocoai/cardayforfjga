Ext.define('Admin.view.orgmgmt.enterinfoaudit.RefuseWindow', {
	extend: 'Ext.window.Window',	
    alias: "widget.refuseWindow",

	controller: {
        xclass: 'Admin.view.orgmgmt.enterinfoaudit.ViewController'
    },
	title : '审核驳回',
	width : 400,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作

	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 10,
	items:[{
			xtype: 'panel',
			width:300,
            html: '<div>请填写驳回理由：</div>',
	},{
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
				text: '取消',
				handler: function(btn){
					btn.up('refuseWindow').close();
				}
			} ]
	
});