Ext.define('Admin.view.reportmgmt.vehicleusereport.GridVehicle', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.ordermgmt.orderallocate.AllocatingVehicleModel'
    ],
    
    reference: 'gridvehicle',
    title: '车辆列表',
    width: '100%',
    //height: 290,
    
    bind: {
        store: '{vehicleList}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },

    scrollable: false,
    stateful: true,
    forceFit: false,
    align: 'left',
    //cls : 'user-grid',  //图片用户样式
    margin: '0 0 0 0',
    frame: true,
    columnLines: true, // 加上表格线
    
//	  selModel: {
//		injectCheckbox: 0,
//		mode: 'SINGLE',
//		checkOnly: false,
//		allowDeselect: true
//	},
	//selType: 'checkboxmodel',	//自5.1.0已废弃
    columns: [
    {
        header: '序号',
//    	text:'<span data-qtip="序号">序号</span>',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'id',
        flex: 0.5,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '车牌号',
//        text:'<span data-qtip="车牌号">车牌号</span>',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'vehicleNumber',
        flex: 0.8,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '车辆品牌',
//        text:'<span data-qtip="车辆品牌">车辆品牌</span>',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'vehicleBrand',
        flex: 0.5,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '排量',
//        text:'<span data-qtip="排量">排量</span>',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'vehicleOutput',
        flex: 0.5,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '燃油号',
//    	text:'<span data-qtip="燃油号">燃油号</span>',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'vehicleFuel',
        flex: 0.5,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '部门',
//    	text:'<span data-qtip="部门">部门</span>',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'currentuseOrgName',
        flex: 0.8,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    }, 
    {
        header: '总里程（千米）',
//        text:'<span data-qtip="总里程（千米）">总里程（千米）</span>',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'totalMileage',
        flex: 0.8,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '总耗油量（升）',
//        text:'<span data-qtip="总耗油量（升）">总耗油量（升）</span>',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'totalFuelcons',
        flex: 0.8,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '总行驶时长（小时）',
//        text:'<span data-qtip="总行驶时长（小时）">总行驶时长（小时）</span>',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'totalDrivingtime',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '使用率',
//    	text:'<span data-qtip="使用率">使用率</span>',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'usagePercent',
        flex: 0.5,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '平均里程（千米/天）',
//        text:'<span data-qtip="平均里程（千米/天）">平均里程（千米/天）</span>',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'avgMileage',
        flex: 0.9,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '平均耗油量（升/天）',
//        text:'<span data-qtip="平均耗油量（升/天）">平均耗油量（升/天）</span>',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'avgFuelcons',
        flex: 0.9,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '平均行驶时长（小时/天）',
//    	text:'<span data-qtip="平均行驶时长（小时/天）">平均行驶时长（小时/天）</span>',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'avgDrivingtime',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    }
    ],
    dockedItems: [
	  {
	      xtype: 'pagingtoolbar',
	      id: 'GridVehicle_id',
	      //pageSize: 5,
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
//	      bind: {
//	      	store: '{allocatingVehicleReport}'
//	      },
	      displayInfo: true
	  }
   ],
   
   initComponent: function() {
        this.callParent();
    },
});
