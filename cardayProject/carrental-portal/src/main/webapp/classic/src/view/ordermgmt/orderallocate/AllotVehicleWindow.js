Ext.define('Admin.view.ordermgmt.orderallocate.AllotVehicleWindow', {
	extend: 'Ext.window.Window',
	
    alias: "widget.allotVehicleWindow",
    controller: 'allocatingViewController',
	reference: 'allotVehicleWindow',
	id: 'allotVehicleWindow',
	title : '订单调度--车辆分配',
	width : 1100,
	//height : 700,
	closable: true,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	frame : true,
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    listeners:{
//    	afterrender: 'loadTripTraceWindowInfo',
//    	afterrender: function() {alert(this.organizationId)}
    },
    bodyPadding: 8,
 /*   defaults: {  //对items的组件生效
        frame: true,
    },*/
    margin: '5 5 50 5',
	items : [ 
	{
		id:'allocatedCarReportId',
        xtype: 'gridpanel',
        bind: {
        	store : '{allocatedCarReport}'
        },
        viewModel: {
            xclass: 'Admin.view.ordermgmt.orderallocate.AllocateModel2'
        },
        scrollable: false,
        columnLines: true, // 加上表格线
        frame: true,
        columns: [
		{
		    header: '序号',
		    sortable: false,
            menuDisabled: true,
		    dataIndex: 'id',
		    flex: 1,
            align: 'center',
		    hidden: true
		},         
        {
            header: '订单编号',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'orderNo',
            align: 'center',
            flex: 2.5,
        }, 
        {
            header: '订单日期',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'orderTimeF',
            align: 'center',
            flex: 1.5
        }, 
        {
        	header: '用车人',
        	menuDisabled: true,
            sortable: false,
            dataIndex: 'orderUsername',
            align: 'center',
            flex: 1.5
        },
        {
            header: '公车性质',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'vehicleType',
            name: 'vehicleType',
            align: 'center',
            flex: 1.5,
            renderer : function(value) {
            	switch (value) {
            	case '0':
            		value='应急机要通信接待用车';
            		break;
            	case '1':
            		value='行政执法用车';
            		break;
            	case '2':
            		value='行政执法特种专业用车';
            		break;
            	case '3':
            		value='一般执法执勤用车';
            		break;
                case '4':
                    value='执法执勤特种专业用车';
                    break;
            	}
            	return value;
			}
        },
        {
            header: '用车城市',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'city',
            align: 'center',
            flex: 1
        },
        {
            header: '预约用车时间',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'planStTimeF',
            name: 'planStTimeF',
            align: 'center',
            flex: 2,
            renderer : function(value) {
            	if (value==null || value=="") {
            		return value="--";
            	} else {
            		return value;
            	}
            }
        },
        {
            header: '乘车人数',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'passengerNum',
            name: 'passengerNum',
            align: 'center',
            flex: 1.5,
        },
        {
            header: '出发地',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'fromPlace',
            name: 'fromPlace',
            align: 'center',
            flex: 1.2,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '目的地',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'toPlace',
            name: 'toPlace',
            align: 'center',
            flex: 1.2,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
        	xtype : 'actioncolumn',
    		cls : 'content-column',
            align: 'center',
    		width : 50,
    		text : '操作',
    		//tooltip : 'edit',
    		items : [
    		{
    		    tooltip:'查看',
    		    iconCls: 'x-fa fa-eye',
    		    handler: 'viweOrderInfo'
    		}],
        }]
	},
	{
	    viewModel: {
	    	xclass: 'Admin.view.ordermgmt.orderallocate.AllocatingVehicleModel'
	    },
		xclass : 'Admin.view.ordermgmt.orderallocate.AllocatingVehicleListGrid',
		frame: true,
	}
	/*,{
		xclass : 'Admin.view.ordermgmt.orderallocate.AllotVehicleSearchForm',
		frame: true,
	}*/
	/*,{
		xclass : 'Admin.view.vehiclemgmt.realtime_monitoring.TripTraceMap',
	}, {
		xclass : 'Admin.view.vehiclemgmt.realtime_monitoring.TripTraceErrorEventGrid',
	} */],
	
	buttonAlign : 'right',
	buttons : [
		{
			text : '下一步',
			handler: 'vehicleAlloNext',
			align: 'right'
		}
	],
    
    initComponent: function() {
        this.callParent();
    }
});