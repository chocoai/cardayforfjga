Ext.define('Admin.view.systemmgmt.systemmessage.AddMessage', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.addMessage",
    controller: 'systemmessagecontroller',
	reference: 'addMessage',
	title : '新增公告',
	//width : 600,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	frame : true,

	bodyStyle: {
		padding: '15px'  //与边界的距离
	},
	
	items : [{
		xtype:'form',
		layout: {
            type: 'vbox', 
            align: 'stretch'
        },
		items: [
    	{
    		fieldLabel: '<span style="color:red;">*</span>公告标题',
    		xtype: 'textfield',
            labelWidth: 80,
            width: 220,
            name: 'title',
            allowBlank:false,
            maxLength:200,
    	},
    	{
    		fieldLabel: '<span style="color:red;">*</span>公告内容',
    		xtype: 'textareafield',
            labelWidth: 80,
            width: 420,
            name: 'msg',
            allowBlank:false,
            maxLength:2000,
    	},
	],
	buttonAlign : 'center',
	buttons : [{
				text : '确定',
				disabled : true,
                formBind : true,
				handler: 'onAddClickDone',
			},{
				text: '取消',
				handler: function(btn){
					btn.up("addMessage").close();
				}
			} ]
		}],
});