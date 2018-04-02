Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.AddGeofenceToVehicleGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewModel'
    ],
    reference: 'addGeofenceToVehicleGrid',
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewModel'
    },
    title: '可分配地理围栏列表',
    width : 750,
    id: 'addGeofenceToVehicleGrid',
    xtype: 'addGeofenceToVehicleGrid',
    bind: {
        store: '{vehicleGeofenceStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    stateful: true,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    margin: '20 0 0 10',
    selModel: {
        injectCheckbox: 0,
        mode: "SIMPLE",     //"SINGLE"/"SIMPLE"/"MULTI"
        listeners: {
            select: 'checkVehSelect',
            deselect: 'checkVehdeSelect',
        }
    },
    selType: 'checkboxmodel',//可以对表格的数据进行多选
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
        }
],

    dockedItems : [{
        id:'addGeofenceToVehiclePage',
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
            store: '{vehicleGeofenceStore}'
        },
        displayInfo: true
    }],
    initComponent: function() {
        this.callParent();
    },
});
