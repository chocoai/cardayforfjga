Ext.define('Admin.view.reportmgmt.coststatistics.GridVehicle', {
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
        header: '单位',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'deptName',
        flex: 1,
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
        header: '维修保养',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'maintainCost',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '保险',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'insuranceCost',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '通行费用',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'trafficCost',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '油耗',
        align:'center',
        menuDisabled: true,
        flex: 2,

            columns: [{
                    header: '数量(升)',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'count',
                    flex: 1,
                    align: 'center',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                    }
                },
                {
                    header: '金额',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'totalCost',
                    flex: 1,
                    align: 'center',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                    }
                },]
        },
    {
        header: '小计',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'sumCost',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    }, 
    ],
    dockedItems: [
	  {
	      xtype: 'pagingtoolbar',
	      id: 'GridVehicle_page_coststatistics',
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
