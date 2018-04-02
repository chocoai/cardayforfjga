Ext.define('Admin.view.main.mainpage.ViewMessage', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.viewMainMessage",
     controller: {
        xclass: 'Admin.view.main.mainpage.ViewController'
    },

	title : '查看公告消息',
	width : 600,
	height : 280,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	scrollable: 'y',
	
	items : [{
		xtype:'form',
		layout : 'vbox',
		defaultType: 'displayfield',
		
		bodyStyle: {
			    padding: '15px'  //与边界的距离
		},

		items: [
		{
			fieldLabel: '公告时间',
			id: 'mainMessageTime',
		    name: 'time',
		},
    	{
    		fieldLabel: '公告标题',
			id: 'mainMessageTitle',
            name: 'title',
        	width : 550,
    	},
    	{
    		fieldLabel: '公告消息',
			id: 'mainMessageMsg',
            name: 'msg',
        	width : 550,
    	},
	]}
	],

	buttonAlign : 'center',
	buttons : [{
				text: '关闭',
				handler: function(btn){
					btn.up("viewMainMessage").close();
				}
			} ]
});