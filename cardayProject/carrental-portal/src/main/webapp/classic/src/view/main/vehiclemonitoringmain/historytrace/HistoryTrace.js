/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.main.vehiclemonitoringmain.historytrace.HistoryTrace', {
	extend: 'Ext.window.Window',
	requires: [
	            'Admin.view.main.vehiclemonitoringmain.historytrace.HistoryTraceController'
	],
//    controller: 'realtimeMonitoringcontroller',
    controller: {
    	xclass:'Admin.view.main.vehiclemonitoringmain.historytrace.HistoryTraceController'
    },
    id:'vehMoniHistoryTrace',
//	title : '历史轨迹',
	header:{
		//title : '历史轨迹',
		height:24,
		style:{
			lineHeight:24,
			padding: '0px 10px 4px 10px',
		}
	},
	cls:'my-short-header-window',
	width : 1000,
	height : 115,
	collapsible:true,
    collapseToolText:'',
    expandToolText:'',
    closeToolText:'',
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
//	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	frame : true,
	refreshMap:true,
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    listeners:{
//    	afterrender: 'loadTripTraceWindowInfo',
//    	afterrender: 'loadVehProperty',
    	close: 'onHistoryTraceClose',
    },
    closeNotRefreshMap: function(){
    	this.refreshMap = false;
    	this.close();
    },
//    x:255,
//    y:165,
    bodyPadding: 5,
 /*   defaults: {  //对items的组件生效
        frame: true,
    },*/
	items : [ {
		xclass : 'Admin.view.main.vehiclemonitoringmain.historytrace.HistorySearchForm',
	},{
		xtype:'container',
		height:42,
		bodyPadding: 1,
		style:{
			marginTop:'-5px'
		},
		layout: {
	        type: 'hbox',
	        pack: 'start',
	        align: 'stretch'
	    },
		items:[{
			xtype:'form',
//			border:true,
			height:42,
			id:'historyTraceVehData',
	    	layout: {
	            type: 'column',
	            align: 'stretch'
	        },
	        padding:'0 0 0 5',
	        defaults:{
				labelWidth:70,
				width:126,
				margin:'3 10 0 0',
				minHeight:25
			},
	    	items:[{
	    		xtype:'displayfield',
	    		name:'mileage',
	    		fieldLabel:'总里程',
	    		labelWidth:50,
	    		renderer: function(val) {
	    			if(val == null || val == ""){
	    				return '';
	    			}
	    			return val + 'km';
	    		}
	    	},{
	    		xtype:'displayfield',
	    		name:'fuel',
	    		fieldLabel:'总油耗',
	    		labelWidth:50,
	    		renderer: function(val) {
	    			if(val == null || val == ""){
	    				return '';
	    			}
	    			return val + 'L';
	    		}
	    	},{
	    		xtype:'displayfield',
	    		name:'drivetime',
	    		fieldLabel:'行驶时长',
	    		width:175,
	    		renderer: function(val) {
	    			if(val == null || val == ""){
	    				return '';
	    			}
	            	return formatTime(val);
	    		}
	    	},{
	    		xtype:'displayfield',
	    		name:'stoptime',
	    		fieldLabel:'停车时长',
	    		width:175,
	    		renderer: function(val) {
	    			if(val == null || val == ""){
	    				return '';
	    			}
	    			return formatTime(val);
	    		}
	    	}]
		},{
			xclass : 'Admin.view.main.vehiclemonitoringmain.historytrace.HistoryPlayTraceForm',
		}]
	}],
    initComponent: function() {
        this.callParent();
    }
});

function formatTime(value) {
	console.log('++++formatTime+++');
	var theTime = parseInt(value);// 秒
    var theTime1 = 0;// 分
    var theTime2 = 0;// 小时
    if(theTime > 60) {
        theTime1 = parseInt(theTime/60);
        theTime = parseInt(theTime%60);
        if(theTime1 > 60) {
            theTime2 = parseInt(theTime1/60);
            theTime1 = parseInt(theTime1%60);
        }
    }
    var result = ""+parseInt(theTime)+"秒";
    if(theTime1 > 0) {
        result = ""+parseInt(theTime1)+"分"+result;
    }
    if(theTime2 > 0) {
        result = ""+parseInt(theTime2)+"小时"+result;
    }
    return result;
}
