Ext.define('Admin.view.ordermgmt.orderallocate.VehicleScheduleWindow', {
	extend: 'Ext.window.Window',
	
    alias: "widget.vehicleScheduleWindow",
    controller: 'allocatingViewController',
	reference: 'vehicleScheduleWindow',
	listeners:{
    	afterrender: 'loadScheduleInfo',
    },
	id: 'vehicleScheduleWindow',
	title : '',
	width :  1350,
	height : 580,
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	frame : true,
/*	height: 600,
    layout: {
        type: 'border',
        pack: 'start',
        align: 'stretch'
    },*/
    bodyPadding: 1,
//    defaults: {
//        frame: true,
//        collapsible: true,
//        margin: '0 0 3 0'
//    },
   /* items: [
            {
            	region:'center',
            	xclass: 'Admin.view.ordermgmt.orderallocate.VehicleScheduleView',
            	frame: true,
            },{
            	width: 130,
            	region:'west',
            	xtype:'form',
          		layout : 'form',
                frame: false,
                title: '车牌号',
          		items:[{
          			id: 'vehicleNumber_id',
        			xtype: 'field',
        			name: 'vehicleNumber',
        			//value: '沪A111111' 
        		}],
        		frame: true,
              }, {
            	  xtype:'form',
          		layout : 'form',
          		region:'north',
                frame: false,
          		items:[{
        			fieldLabel: '日期',
        			xtype: 'field',
        			name: 'name',
        			value: '2017-01-24' 
        		}]
            }
        {
            xtype:'panel',
            height: 830,
            width:1000,
            html:"<div id='dashboardCalendar'></div>",
        },],*/
        
        
        items : [{
	    	xtype: 'container',
	        flex: 1,
	        layout: {
	            type: 'hbox',
	            pack: 'start',
	            align: 'stretch'
	        },
	        margin: '0 0 3 0',
	        defaults: {
	            flex: 1,
	            frame: true,
	        },
	        items: [ {
	        	xtype: 'panel',
                flex: 1,
                 layout: {
                    type: 'vbox',
                    pack: 'start',
                    align: 'stretch'
                },
                //bodyPadding: 10,
                items : [
                          {
                                xtype:'panel',
                                reference: 'mainMapInfo',
                                id: 'scheduleTopPanel',
                                viewModel: {
                                    data: {
                                    	secondes: '10',
                                        vehicleNum: '',
                                        vehicleBoard: '',
                                    }
                                },
                                bind: {
                                   html:'<div style="width:600;font-size:11px;"><table style="width: 80%;" id="mainPageTable"><tr>' 
                                	   + '<th style="font-size:13px;font-family:微软雅黑,宋体;">实时位置信息--地图将在{secondes}秒后更新</th>' 
                                	   + '<th style="font-size:13px;font-family:微软雅黑,宋体;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;车牌号：{vehicleNum}</th>' 
                                	   + '<th style="font-size:13px;font-family:微软雅黑,宋体;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;品牌：{vehicleBoard}</th></tr></table></div>',
                                },
                                margin:10,
                          },
                         {
//                            flex: 2,
                            xclass: 'Admin.view.ordermgmt.orderallocate.schedule.RealtimeMap',
                         }],
					},{
						flex: 1,
						xtype:'panel',
			            height: 600,
			            width:700,
			            html:"<div id='dashboardCalendar'></div>",
	                }/*{
						xtype: 'panel',
		                flex: 1,
		                 layout: {
		                    type: 'vbox',
		                    pack: 'start',
		                    align: 'stretch'
		                },
		                bodyPadding: 10,
		                items : [
		                          {
		                        	    flex: 1,
		                                xtype:'panel',
		                                reference: 'scheduleTitle',
		                                html:'<div style="width:600;font-size:11px;">任务信息</div>',
//		                                margin:10,
		                          },
		                         {
		                            flex: 3,
		                            xtype:'panel',
		    			            height: 600,
		    			            width:800,
		    			            html:"<div id='dashboardCalendar'></div>",
		                         }],
					}*/]
	    }

   ],
        
    initComponent: function() {
        this.callParent();
    }
});