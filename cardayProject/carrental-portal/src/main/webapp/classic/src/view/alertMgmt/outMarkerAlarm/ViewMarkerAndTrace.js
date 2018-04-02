Ext.define('Admin.view.alertMgmt.outMarkerAlarm.ViewMarkerAndTrace', {
	extend: 'Ext.window.Window',
	requires : ['Admin.view.alertMgmt.outMarkerAlarm.OutMarkerMap'],
    alias: "widget.markerAndTraceWindow",
    controller: 'outMarkerMgmtController',
    viewModel : {
		type : 'outMarkerModel' 
	},
	title : '车辆越界报警',
/*    minWidth : 1000,
    maxHeight: 620,*/
    width : 1030,
    height : 590,
    autoScroll:true,
	closable:true,//窗口是否可以改变
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
    	      margin:10,
    	   	  xtype:'form',
    	   	  layout:'hbox',
    	   	  items:[{
    	   	  		xtype:'container',
    	   	  		flex:1
    	   	  	},{
				fieldLabel : '请选择时间',
				name : 'startTime',
				xtype : 'datefield',
				width: 400,
				emptyText : '请选择时间',
				editable : false,
				format : 'Y-m-d',
				listeners:{
					select:'changeTrace'
				}
			},{
    	   	  		xtype:'container',
    	   	  		flex:1
    	   	  	}]
    	   	
    	   },{
        		xclass : 'Admin.view.alertMgmt.outMarkerAlarm.OutMarkerMap'
           },{
           		margin:'10 0 0 5',
           		xtype: 'tbtext',
				text: '异常事件信息',
				style: {
					fontSize: '14px',
					fontWeight: 'bold'
				}
           },{
           		xclass : 'Admin.view.alertMgmt.outMarkerAlarm.AbnormOutMarkerInfo',
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