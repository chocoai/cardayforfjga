Ext.define('Admin.view.alertMgmt.overSpeedAlarm.abnormAlarm', {
	extend: 'Ext.window.Window',
	
    alias: "widget.overSpeedWindow",
    controller: 'alarmcontroller',
    viewModel : {
		type : 'overSpeedModel' 
	},
	title : '车辆异常报警',
//	width : 1030,
//	height : 880,
	width : 1030,
	height : 590,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	ghost:false,
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 1,   
    items: [{
        		xclass : 'Admin.view.alertMgmt.overSpeedAlarmg.overSpeedAlarmMap'
           },{
           		margin:'10 0 0 5',
           		xtype: 'tbtext',
				text: '异常事件信息',
				style: {
					fontSize: '14px',
					fontWeight: 'bold'
				}
           },{
           		xclass : 'Admin.view.alertMgmt.overSpeedAlarmg.AbnormOverSpeedInfo'
           }],
    buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up("window").close();
				}
			}],
    initComponent: function() {
        this.callParent();
    }
});