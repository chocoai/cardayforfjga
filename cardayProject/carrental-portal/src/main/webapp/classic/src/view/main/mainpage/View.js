Ext.define('Admin.view.main.mainpage.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'admindashboard',
    id:'mainpageView',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Border',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.main.MainModel'
    ],

    controller: {
        xclass: 'Admin.view.main.mainpage.ViewController'
    },

    listeners:{
        afterrender: 'afterrenderMap'
    },
	/*viewModel: {
        xclass: 'Admin.view.main.MainModel'
    },*/
    mapdata: null,
    trackLine: null,	//实时数据轨迹线
    pointArray:new Array(),
    autoScroll: true,
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
    bodyPadding: 5,

    items : [
             {
                margin: '0 -2 0 0',
                xtype: 'panel',
                flex: 2,
                 layout: {
                    type: 'vbox',
                    pack: 'start',
                    align: 'stretch'
                },
                frame: true,
                items : [
                          {
                                xtype:'panel',
                                id:'VehicleStaticInfo',
                                reference: 'mainMapInfo',
                                viewModel: {
                                    data: {
                                        totalNumber: '0',
                                        onLineNumber: '0',
                                		drivingNumber: '0',
                                		stopNumber: '0',
                                		offLineNumber: '0',
                                		count:'10'
                                    }
                                },
//                                bind: {
//                                   html:'<div class="mainPageMapBanner"><table id="mainPageTable"><tr><th>车辆总数 {totalNumber}</th><th>在线 {onLineNumber}</th><th>行驶 {drivingNumber}</th><th>停止 {stopNumber}</th><th>离线 {offLineNumber}</th></tr><tr><th colspan="5">地图将在{count}秒后刷新</th></tr></table></div>',
                                   html:'<div class="mainPageMapBanner"><table id="mainPageTable"><tr><th>车辆总数 0</th><th>在线 0</th><th>行驶 0</th><th>停止 0</th><th>离线 0</th></tr><tr><th colspan="5">地图将在10秒后刷新</th></tr></table></div>',
//                                },
                                xclass: 'Admin.view.main.mainpage.MainPageMap',
                          },
                          
                        ],
                },
               {
                xtype: 'panel',
                id: 'mainPageAlarmInfo',
                flex: 1,
                 layout: {
                    type: 'vbox',
                    pack: 'start',
                    align: 'stretch',
                },
                //bodyPadding: 10,
                items : [{
                            xtype: 'panel',
                            id: 'messagePanel',
                            reference: 'messagePanel',
                            //collapsible: true,
//                            flex: 1.5,
                            height:140,
                            viewModel: {
                                data: {
                                    systemMessageOne: '',
                                    systemMessageTwo: '',
                                    systemMessageThree: '',
                                    systemMessageOneTime: '',
                                    systemMessageTwoTime: '',
                                    systemMessageThreeTime: '',
                                    systemMessageOneContent: '',
                                    systemMessageTwoContent: '',
                                    systemMessageThreeContent: '',
                                }
                            },
                            title: '公告',
                            html:'<div style="color:gray; font-size:13px; padding:10px; ">暂无公告！</div>'
                            //bodyStyle: 'background:#f2f2f2',
//                            bind: {
//                                html: '<ul class="messageContent"><li><a onclick="getFirstMessage(this)" >{systemMessageOne}</a><span>{systemMessageOneTime}</span><p hidden="hidden">{systemMessageOneContent}</p></li><li><a onclick="getSecondMessage(this)">{systemMessageTwo}</a><span>{systemMessageTwoTime}</span><p hidden="hidden">{systemMessageTwoContent}</p></li><li><a onclick="getThirdMessage(this)">{systemMessageThree}</a><span>{systemMessageThreeTime}</span><p hidden="hidden">{systemMessageThreeContent}</p></li><ul>'
//                            },
                         },
                         {
//                            flex: 5,
                        	 flex:1,
                            xclass: 'Admin.view.main.mainpage.GridAlarmInfo',
                         },
                         {
                        	id: 'mainPageToDoInfo',
                            xtype: 'panel',
                            reference: 'backlogPanel',
                            //collapsible: true,
                            //bodyStyle: 'background:#f2f2f2',
//                            flex: 1,
                            height:100,
                         	viewModel: {
				    	        data: {
				    	            outbound: '0',
				    	            overspeed: '0',
				    	            vehicleback: '0'
				    	        }
    	    				},
                            title: '报警统计',
                            bind: {
                            	//html: '<div style="margin: 20px 30px;font-size: 15px;"><div style="margin-bottom: 10px;">订单审核: {unCheck}</div><div style="margin-bottom: 10px;">订单排车: {unArrange}</div><div style="margin-bottom: 10px;">报警处置: {unDealAlert}</div><div style="margin-bottom: 10px;">紧急救助: 0</div></div>'
                            	//html:'<div style="margin: 20px 30px;font-size: 15px;"><div style="margin-bottom: 10px;">订单审核: {unCheck}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp报警处置: {unDealAlert}</div><div style="margin-bottom: 10px;">订单排车: {unArrange}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp紧急救助: 0</div></div>'
                                //html:'<div style="margin: 20px 50px;font-size: 13px;"><span>订单审核: {unCheck}</span><span style="margin: 0 150px;">订单排车: {unArrange}</span></div>'
                                html:'<div  class="backlog"><span onclick="backlogOnClick(this)" onmouseover="onmouseoverbacklog(this);" onmouseout = "onmouseoutbacklog(this);">超速报警: {overspeed}</span><span onclick="backlogOnClick(this)" onmouseover="onmouseoverbacklog(this);" onmouseout = "onmouseoutbacklog(this);">越界报警: {outbound}</span><span onclick="backlogOnClick(this)" onmouseover="onmouseoverbacklog(this);" onmouseout = "onmouseoutbacklog(this);">回车报警: {vehicleback}</span></div>'
                            }

                            //xclass: 'Admin.view.vehiclemgmt.stationmgmt.BacklogInfo',
                         }],
                }
             ],
    initComponent: function() {
        this.callParent();
    }
});
