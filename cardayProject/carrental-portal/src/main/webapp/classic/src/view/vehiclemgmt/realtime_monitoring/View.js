/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.View', {
    extend: 'Ext.panel.Panel',
//    xtype: 'vehicle_realtime_monitoring',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Border',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
//        'Admin.view.vehiclemgmt.realtime_monitoring.VehicleModel',
        'Admin.view.vehiclemgmt.realtime_monitoring.VehicleModel'
    ],
    
    controller: {
        xclass: 'Admin.view.vehiclemgmt.realtime_monitoring.ViewController'
    },
    viewModel: {
//        xclass: 'Admin.view.vehiclemgmt.realtime_monitoring.VehicleModel'
    	  xclass: 'Admin.view.vehiclemgmt.realtime_monitoring.VehicleModel'
    },
    listeners:{
    	afterrender: 'loadVehicleRealtimeReport',
    },
    
    height: 600,
    layout: {
        type: 'border',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 1,
    defaults: {
        frame: true,
        collapsible: true,
        margin: '0 0 3 0'
    },
    items: [
            {
            	xclass: 'Admin.view.vehiclemgmt.realtime_monitoring.VehicleTrackingMap',
            	region:'center',
//                margins: '5 0 0 0',
//                cmargins: '5 5 0 0',
            },{
            	width: 180,
            	region:'west',
                xtype: 'gridpanel',
                cls: 'user-grid',
                title: '车辆列表',
                routeId: 'user',
                bind: {
                	store : '{vehicleRealtimeReport}'
                },
                scrollable: false,
                columns: [{
                    header: '车牌号',
                    sortable: false,
                    menuDisabled: true,
                    align: 'center',
                    dataIndex: 'vehicle_num',
                    flex: 1,
                    renderer : function(value) {
                    	return '<a style="text-decoration: none;" href="javascript:void(0);" onclick="showVehicle(\'' +  value + '\')">' + value + '</a>';
                    }
                   
                }, {
                	header: 'imei',
                    hidden: true,
                    menuDisabled: true,
                    align: 'center',
//                    sortable: false,
                    dataIndex: 'imei',
                    flex: 1
                    
                },{
                	header: '经度',
                    hidden: true,
                    menuDisabled: true,
                    align: 'center',
//                    sortable: false,
                    dataIndex: 'longitude',
                    flex: 1
                    
                },  {
                	header: '纬度',
                    hidden: true,
                    menuDisabled: true,
                    align: 'center',
//                    sortable: false,
                    dataIndex: 'latitude',
                    flex: 1
                    
                },  {
                	xtype : 'actioncolumn',
            		items : [ {
            			xtype : 'button',
            			tooltip : '实时数据',
            			iconCls : 'x-fa fa-eye',
            			handler : 'showRealtimeDataWindow'
            		}, {
            			xtype : 'button',
            			tooltip : '历史数据',
            			iconCls : 'x-fa fa-youtube-play',
            			handler : 'showTripTraceWindow'
            		} ],

            		cls : 'content-column',
            		width : 80,
                    align: 'center',
            		text : '操作',
            		//tooltip : 'edit '
                }
            ],
            /*dockedItems: [
                {
                    xtype: 'pagingtoolbar',
                    pageSize: 20,
                    dock: 'bottom',
                    displayMsg: '第 {0} - {1} 条记录，共 {2} 条记录',
                    emptyMsg : '无数据！',
                    beforePageText: "第",
                    afterPageText: "页，共{0}页",
                    nextText: "下一页",
                    prevText: "上一页",
                    refreshText: "刷新",
                    firstText: "第一页",
                    lastText: "最后一页",
                    bind: {
                        store: '{vehicleRealtimeReport}'
                    },
                    displayInfo: true
                }
            ],*/
            }, {
                xclass: 'Admin.view.vehiclemgmt.realtime_monitoring.SearchForm',
                region:'north',
                frame: false,
//                margins: '5 0 0 0'
            }],     
    initComponent: function() {
        this.callParent();
    }
});
