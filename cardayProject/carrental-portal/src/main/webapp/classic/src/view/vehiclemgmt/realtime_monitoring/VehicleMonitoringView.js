/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.VehicleMonitoringView', {
	extend: 'Ext.panel.Panel',
    xtype: 'vehicle_realtime_monitoring',
    requires: [
               'Ext.layout.container.VBox',
               'Ext.layout.container.Border',
               'Ext.layout.container.Fit',
               'Ext.window.MessageBox',
               'Ext.grid.plugin.RowEditing',
//               'Admin.view.vehiclemgmt.realtime_monitoring.VehicleModel',
               'Admin.view.vehiclemgmt.realtime_monitoring.VehicleModel'
           ],
    controller: {
        xclass: 'Admin.view.vehiclemgmt.realtime_monitoring.ViewController'
    },  
    viewModel: {
    	xclass: 'Admin.view.vehiclemgmt.realtime_monitoring.VehicleModel'
    },
    listeners:{
        afterrender: 'loadVehicleRealtimeReport',
    },

    //autoScroll: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 10,

    items: [{
        	xclass: 'Admin.view.vehiclemgmt.realtime_monitoring.SearchForm',
    }, {
        layout: {
            type: 'hbox',
            pack: 'start',
            align: 'stretch'
        },
        defaults: {
            frame: true,
        },
        items: [{  
            flex: 1,          
            //
            margin: '0 -2 0 0',
            xtype: 'gridpanel',
            cls: 'user-grid',
            title: '车辆列表',
            minWidth: 206,
            //height: 736,
            routeId: 'user',
            id:'vehicleMonitoringListGrid',
            bind: {
            	store : '{vehicleRealtimeReport}'
            },
//            store : {
//            	data: [
//                       { id: '108', vehiclenumber: "京AX77G6", imei:'41042502439', vehicle_type: "X5", longitude: '114.29821255382512', latitude: "30.589583523896508", realname:"张三", phone: "123456787977"},
//                       { id: '141', vehiclenumber: "鄂A20417", imei:'356449063022221',  vehicle_type: "X5", longitude: "114.27598547116384", latitude: "30.57530194127433",realname:"张三", phone: "123456787977"},
//                       { id: '139', vehiclenumber: "鄂A88885", imei:'20170414002017041401', vehicle_type: "X5", longitude: "114.226751", latitude: "30.58431",realname:"张三", phone: "123456787977"},
//                       { id: '126', vehiclenumber: "鄂A88888", imei:'356449062938476', vehicle_type: "Q7", longitude: "114.226751", latitude: "30.58431",realname:"张三", phone: "123456787977"}
//                       ]
//    		},
            //scrollable: 'y',
            columns: [{
            	header: 'id',
                hidden: true,
//                sortable: false,
                dataIndex: 'id',
                menuDisabled: true,
                align: 'center',
                flex: 1
                
            }, {
                header: '车牌号',
                sortable: true,
                menuDisabled: true,
                align: 'center',
                dataIndex: 'vehiclenumber',
//                renderer : function(value) {
//                	return '<a style="text-decoration: none;" href="javascript:void(0);" onclick="showVehicle({id})">' + value + '</a>';
//                }
               
            }, {
            	header: 'imei',
                hidden: true,
                menuDisabled: true,
                align: 'center',
                dataIndex: 'imei',
                flex: 1
                
            }, {
            	header: '经度',
                hidden: true,
                menuDisabled: true,
                align: 'center',
                dataIndex: 'longitude',
                
            },  {
            	header: '纬度',
                hidden: true,
                menuDisabled: true,
                align: 'center',
                dataIndex: 'latitude',
                
            }, 
            {
            	header: '司机姓名',
                hidden: true,
                menuDisabled: true,
                align: 'center',
                dataIndex: 'realname',
            },  
            {
            	header: '司机手机号码',
                hidden: true,
                menuDisabled: true,
                align: 'center',
                dataIndex: 'phone',
                
            },  {
            	xtype : 'actioncolumn',
        		items : [ {
        			xtype : 'button',
        			tooltip : '车辆信息',
        			iconCls : 'x-fa fa-eye',
        			handler : 'showVehicle'
        		},  {
        			xtype : 'button',
        			tooltip : '实时数据',
        			iconCls : 'x-fa fa-car',
        			handler : 'showRealtimeDataWindow'
        		},  {
        			xtype : 'button',
        			tooltip : '历史数据',
        			iconCls : 'x-fa fa-youtube-play',
        			handler : 'showTripTraceWindow'
        		} ],

                align: 'center',
        		text : '操作',
        		menuDisabled: true//,
        		//tooltip : 'edit '
            }
        ],
        },{
        	xclass: 'Admin.view.vehiclemgmt.realtime_monitoring.VehicleTrackingMap',
            flex: 7,
            //frame: true,
//            split: true,
        }]
    }],   
    initComponent: function() {
        this.callParent();
    }
});
