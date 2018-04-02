Ext.define('Admin.view.vehiclemgmt.mantainance.Grid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
    ],
    id: 'mantaingrid',
    reference: 'mantaingrid',
    title: '车辆保养记录',
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.mantainance.ViewModel'
    },
    bind: {
        store: '{mantainanceStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    listeners: {
    	afterrender : 'loadGirdData',
    },
    stateful: true,
    collapsible: false,
    forceFit: false,
    mask: true,
    // collapsed: true,
    multiSelect: false,
    columnLines: true, // 加上表格线
    align: 'center',
    
    selModel: {
        injectCheckbox: 0,
        mode: "SIMPLE",     //"SINGLE"/"SIMPLE"/"MULTI"
//        checkOnly: true     //只能通过checkbox选择
    },
    selType: 'checkboxmodel',//可以对表格的数据进行多选
    columns: [
        {
        	header: 'id',
        	hidden: true,
            sortable: false,
            menuDisabled: true,
            dataIndex: 'id',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '车牌号',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'vehicleNumber',
            flex: 1,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
        	header: '车辆品牌',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'vehicleBrand',
            flex: 1,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, {
            header: '车辆型号',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'vehicleModel',
            flex: 1,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, {
            header: '所属部门',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'orgName',
            flex: 1,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
	    }, {
            header: '当前表头里程数',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'headerLatestMileage',
            flex: 1,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
	    }, {
            header: '上次保养表头里程数',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'headerMaintenanceMileage',
            flex: 1,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
	    }, {
            header: '保养里程数',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'maintenanceMileage',
            flex: 1,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
	    }, {
            header: '已行驶里程',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'travelMileage',
            flex: 1,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
	    }, {
            header: '剩余里程',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'remainingMileage',
            flex: 1,
            align: 'center',
            renderer : function(value, metaData, record) {
            	var alertMileage = record.data.alertMileage;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            	if(parseInt(value)<=parseInt(alertMileage)) {
            		return '<font color="red">' + value + '</font>';
            	}
            	return value;
            }
	    }, {
            header: '上次保养时间',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'curTimeF',
            flex: 1,
            align: 'center',
            renderer : function(value, metaData, record) {
            	var maintenanceRemainingTime = record.data.maintenanceRemainingTime;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            	if(parseInt(maintenanceRemainingTime)<=1 && parseInt(maintenanceRemainingTime) != -9999) {
            		return '<font color="red">' + value + '</font>';
            	}
            	return value;
            }
	    }, {
	    	header: '维保剩余时间',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'maintenanceRemainingTime',
            hidden: true,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
	    }, {
	    	header: '下次保养时间',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'maintenanceNextTime',
            align: 'center',
            renderer : function(value, metaData, record) {
            	var maintenanceRemainingTime = record.data.maintenanceRemainingTime;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            	if(parseInt(maintenanceRemainingTime)<=1  && parseInt(maintenanceRemainingTime) != -9999) {
            		return '<font color="red">' + value + '</font>';
            	}
            	return value;
            }
	    }, {
			header: 'maintenanceTime',
        	hidden: true,
            sortable: false,
            menuDisabled: true,
            dataIndex: 'maintenanceTime',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
		}, {
			header: 'alertMileage',
        	hidden: true,
            sortable: false,
            menuDisabled: true,
            dataIndex: 'alertMileage',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
		}, {
			header: 'thresholdMonth',
        	hidden: true,
            sortable: false,
            menuDisabled: true,
            dataIndex: 'thresholdMonth',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
		}, {
			header: 'maintenanceDueTimeF',
        	hidden: true,
            sortable: false,
            menuDisabled: true,
            dataIndex: 'maintenanceDueTimeF',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
		}, {
		xtype : 'actioncolumn',
		items : [ {
			xtype : 'button',
			tooltip : '重置',
			iconCls : 'x-fa fa-refresh',
			handler : 'showResetView'
			
		},{
			
		},{
			xtype : 'button',
			tooltip : '设置',
			iconCls : 'x-fa fa-pencil',
			handler : 'onClickSetup'
			
		},/*{
            getClass: function(v, meta, record) {
                var marginValue = this.width/2-20;                        
                this.setMargin('0 -'+marginValue+' 0 '+marginValue);
            }

        }*/],

		cls : 'content-column',
		width : 80,
//		align: 'left',
		align: 'center',
		text : '清零'
	}
    ],
    dockedItems: [
        {
        	id: 'mantainGridPage',
            xtype: 'pagingtoolbar',
            pageSize: 10,
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
                store: '{mantainanceStore}'
            },
            displayInfo: true
        }
    ],
    initComponent: function() {
        this.callParent();
    },
});
