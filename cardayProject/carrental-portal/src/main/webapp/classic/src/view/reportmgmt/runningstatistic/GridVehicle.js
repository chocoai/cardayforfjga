Ext.define('Admin.view.reportmgmt.runningstatistic.GridVehicle', {
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
    margin: '0 0 0 0',
    frame: true,
    columnLines: true, // 加上表格线
    
    columns: [
    {
        header: '序号',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'id',
        flex: 0.5,
        align: 'center',
        hidden:true,
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
        header: '出车时间',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'outtime',
        flex: 2,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '回车时间',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'intime',
        flex: 2,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '目的地',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'destination',
        flex: 2,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '申请单位',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'applyDept',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '用车人',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'vehicleUse',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '驾驶员',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'driver',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    }, 
    {
        header: '行驶公里',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'mileage',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '驾驶员费用',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'totalDrivingCost',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '过桥路费',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'totalCost',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '事由',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'reason',
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
	      id: 'GridVehicle_page',
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
	      displayInfo: true
	  }
   ],
   
   initComponent: function() {
        this.callParent();
    },
});
