Ext.define('Admin.view.orgmgmt.enterinfoaudit.OpenWindow', {
	extend: 'Ext.window.Window',	
    alias: "widget.openWindow",
    xtype:'openWindow',

	controller: {
        xclass: 'Admin.view.orgmgmt.enterinfoaudit.ViewController'
    },

    listeners:{
        afterrender: 'afterrenderOpenWindow'
    },

    title : '开通服务',
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
            reference: 'openTitlePanel',
            viewModel: {
    	        data: {
    	            entName: '0',
    	            serviceTime: '0',
    	        }
			},
			width:300,
			bind: {
                html: '<div>{entName}的用车服务期限为：{serviceTime}</div><div>请设置新的服务期限：</div>',
            }
	},{
		xtype:'form',
		reference: 'openForm',
		layout : 'form',		
		items:[{
			fieldLabel: '服务结束时间',
			xtype: 'datefield',
	        name: 'endTime',
	        format:'Y-m-d',
	        editable:false,
	        allowBlank:false,
	        minValue: new Date(),
		},{
			xtype:'hidden',
			name:'id',
			value:''
		},{
			xtype:'hidden',
			name:'entName',
			value:''
		},{
			xtype:'hidden',
			name:'serviceTime',
			value:''
		},{
			xtype:'hidden',
			name:'startTime',
			value:''
		}],
	buttonAlign : 'center',
	buttons : [{
				text : '确定',
				disabled : true,
                formBind : true,
				handler:'openEvent'
			},{
				text: '取消',
				handler: function(btn){
					btn.up('openWindow').close();
				}
			} ]
	}],
	
});