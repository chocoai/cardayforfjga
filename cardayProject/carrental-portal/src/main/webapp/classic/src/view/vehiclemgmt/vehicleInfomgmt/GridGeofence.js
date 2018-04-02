Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.GridGeofence', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewModel'
    ],
    reference: 'assignedVehicleGeofenceGrid',
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewModel'
    },
    title: '已分配地理围栏列表',
    width : 750,
    xtype: 'assignedVehicleGeofenceGrid',
    id : 'assignedVehicleGeofenceGrid',
    bind: {
        store: '{vehicleGeofenceAssignedStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    listeners: {
    },
    stateful: true,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    margin: '20 0 0 10',
    columns: [{
            header: '编号',
            dataIndex: 'id',
            align:'center',
            sortable: true,
            menuDisabled: true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            header: '地理围栏名称',
            dataIndex: 'markerName',
            align:'center',
            sortable: false,
            menuDisabled: true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, {
         header: '省/直辖市',
         dataIndex: 'province',
            align:'center',
            sortable: false,
            menuDisabled: true,
         renderer:function (value, metaData){  
             metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
             return value;  
         }
        }, {
            header: '城市',
            dataIndex: 'city',
            align:'center',
            sortable: false,
            menuDisabled: true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, {
            header: '位置',
            dataIndex: 'position',
            align:'center',
            sortable: false,
            menuDisabled: true,
            flex:2,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '绘制类型',
            dataIndex: 'type',
            align:'center',
            sortable: false,
            menuDisabled: true,
            renderer:function(val, cellValues, rec){
                if(val == '0'){
                    return '行政区划分';
                }else{
                    return '自主绘制';
                }
            },
        }, {
    	xtype : 'actioncolumn',
        header : '操作',
        align:'center',
		items : [ {
			xtype : 'button',
			tooltip : '移除',
			iconCls : 'x-fa fa-close',
			handler : 'unassignGeofence'
		} ],
    }
    ],

    dockedItems : [{
        id:'assignedVehicleGeofencePage',
        pageSize:5,
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
            store: '{vehicleGeofenceAssignedStore}'
        },
        displayInfo: true
    }],
    initComponent: function() {
        this.callParent();
    },
});
