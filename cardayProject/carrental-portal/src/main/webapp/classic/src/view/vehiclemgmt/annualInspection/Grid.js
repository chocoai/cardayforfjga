Ext.define('Admin.view.vehiclemgmt.annualInspection.Grid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
    ],
    id: 'annualInspectionGrid',
    reference: 'annualInspectionGrid',
    title: '车辆保险年检记录',
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.annualInspection.ViewModel'
    },
    bind: {
        store: '{annualInspectionStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...',
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
    
    columns: [
        {
        	header: 'id',
        	hidden: true,
            sortable: false,
            dataIndex: 'id',
            menuDisabled: true,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
        	header: 'vehicleId',
        	hidden: true,
            sortable: false,
            dataIndex: 'vehicleId',
            menuDisabled: true,
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
        }, {
            header: '所属部门',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'arrangedOrgName',
            flex: 1,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
	    }, {
            header: '车辆来源',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'vehicleFromName',
            flex: 1,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
	    }, {
            header: '保险到期日',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'insuranceDueTimeF',
            flex: 1,
            align: 'center',
            renderer : function(value, metaData, record) {
            	var insuranceDueTimeFlag = record.data.insuranceDueTimeFlag;
            	console.log('insuranceDueTimeFlag:' + insuranceDueTimeFlag);
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            	if(insuranceDueTimeFlag) {
            		return '<font color="red">' + value + '</font>';
            	} else {
            		return value;
            	}
            	
            }
	    }, {
	    	header: '上次年检日期',
	    	sortable: false,
            dataIndex: 'inspectionLastTimeF',
            flex: 1,
            menuDisabled: true,
            align: 'center',
            renderer : function(value, metaData, record) {
            	var inspectionTimeFlag = record.data.inspectionTimeFlag;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            	if(inspectionTimeFlag) {
            		return '<font color="red">' + value + '</font>';
            	} else {
            		return value;
            	}
            	
            }
	    }, {
            header: '下次年检日期',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'inspectionNextTimeF',
            flex: 1,
            align: 'center',
            renderer : function(value, metaData, record) {
            	var inspectionTimeFlag = record.data.inspectionTimeFlag;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            	if(inspectionTimeFlag) {
            		return '<font color="red">' + value + '</font>';
            	} else {
            		return value;
            	}
            	
            }
	    }, {
		xtype : 'actioncolumn',
		items : [ {
			xtype : 'button',
			tooltip : '重置保险',
			iconCls : 'x-fa fa-pencil-square-o',
			handler : 'showResetInsuranceTimeWindow'
		},{
			
		},{
			xtype : 'button',
			tooltip : '重置年检',
			iconCls : 'x-fa  fa-rotate-right',
			handler : 'showResetInspectionTimeWindow'
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
		text : '操作',

	}
    ],
    dockedItems: [
        {
        	id:'annualInspectionPage',
        	pageSize: 10,
            xtype: 'pagingtoolbar',
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
                store: '{annualInspectionStore}'
            },
            displayInfo: true
        }
    ],
    initComponent: function() {
        this.callParent();
    },
});
