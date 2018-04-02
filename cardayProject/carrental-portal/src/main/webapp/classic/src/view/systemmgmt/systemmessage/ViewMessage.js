Ext.define('Admin.view.systemmgmt.systemmessage.ViewMessage', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.viewMessage",
    controller: 'empmgmtcontroller',
	reference: 'systemmessagecontroller',
	title : '查看公告消息',
	width : 600,
  height : 320,
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
		    name: 'time',
		},
    	{
    		fieldLabel: '公告类型',
            name: 'type',
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                switch(v)
                {
                case 'MAINTAIN':
                  return "车辆维保";
                  break;
                case 'OUTBOUND':
                  return "越界报警";
                  break;
                case 'OVERSPEED':
                  return "超速报警";
                  break;
                case 'VEHICLEBACK':
                  return "回车报警";
                  break;
                case 'SYSTEM':
                  return "系统消息";
                  break;
                case 'TASK':
                  return "任务消息";
                  break;
                case 'TRAVEL':
                  return "行程消息";
                  break;
                default:
                  return "其他";
                }
            }
    	},
    	{
    		fieldLabel: '公告标题',
        name: 'title',
        width : 550,
    	},
    	{
    		fieldLabel: '公告消息',
        name: 'msg',
        width : 550,
    	},
	]}
	],

	buttonAlign : 'center',
	buttons : [{
				text: '关闭',
				handler: function(btn){
					btn.up("viewMessage").close();
				}
			} ]
});