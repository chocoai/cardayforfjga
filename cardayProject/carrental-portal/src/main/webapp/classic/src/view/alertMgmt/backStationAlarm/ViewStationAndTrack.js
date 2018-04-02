Ext.define('Admin.view.alertMgmt.backStationAlarm.ViewStationAndTrack', {
	extend: 'Ext.window.Window',
	
    alias: "widget.stationAndTrackWindow",
    controller: 'backStationMgmtController',
    viewModel : {
		type : 'backStationModel' 
	},
	title : '车辆回车报警',
	width : 1030,
	height : 590,
	closable:true,//窗口是否可以改变
	closeToolText:'关闭窗口',
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	ghost:false,
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
  //  bodyPadding: 5,   
    items: [{
    	xclass : 'Admin.view.alertMgmt.backStationAlarm.BackStationMap'
    },{
   		margin:'20 0 0 5',
   		xtype: 'tbtext',
		text: '异常事件信息',
		style: {
			fontSize: '14px',
			fontWeight: 'bold'
		}
   },{	
   		xclass : 'Admin.view.alertMgmt.backStationAlarm.AbnormBackStationInfo',
   		//border:true,
   		height:100,
   		style:{
        			borderTop:'1px solid #35baf6'
        		}
   }],
/*   		{
			xtype: 'tbtext',
			//text: '<h2>审核信息</h2>',
			html:'<div style="font:18px arial;color:#0D85CA;margin: 10px 0px 20px 0px">订单信息</div>'
		},*/
   
   
   /* buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up("window").close();
				}
			}],*/
    initComponent: function() {
        this.callParent();
    }
});