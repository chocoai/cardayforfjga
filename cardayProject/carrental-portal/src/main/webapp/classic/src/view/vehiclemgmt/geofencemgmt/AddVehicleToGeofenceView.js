Ext.define('Admin.view.vehiclemgmt.geofencemgmt.AddVehicleToGeofenceView', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.vehiclemgmt.geofencemgmt.ViewModel'
    ],
    reference: 'addVehicleToGeofenceView',
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.geofencemgmt.ViewModel'
    },
    title: '可分配车辆列表',
    width : 750,
    id: 'addVehicleToGeofenceView',
    xtype: 'addVehicleToGeofenceView',
    bind: {
        store: '{geofenceVehicleStore}'
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
    //align: 'center',
    // height: 350,  
    cls: 'shadow',
    margin: '20 0 0 10',
    selModel: {
        injectCheckbox: 0,
        mode: "SIMPLE",     //"SINGLE"/"SIMPLE"/"MULTI"
        //checkOnly: true     //只能通过checkbox选择
        listeners: {
            select: 'checkVehSelect',
            deselect: 'checkVehdeSelect',
        }
    },
    selType: 'checkboxmodel',//可以对表格的数据进行多选
    columns: [{
        header: '编号ID',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'id',
        flex: 1,
        align: 'center',
        hidden: true
    },{
        header: '车牌号',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'vehicleNumber',
        align: 'center',
        flex: 1
    }, 
    {
        header: '公车性质',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'vehicleType',
        align: 'center',
        flex: 1,
        renderer: function(val) {
            switch(val){
                case '0':
                    return '应急机要通信接待用车';
                case '1':
                    return '行政执法用车';
                case '2':
                    return '行政执法特种专业用车';
                case '3':
                    return '一般执法执勤用车';
                case '4':
                    return '执法执勤特种专业用车';
            }
        }
    }, 
    {
        header: '车辆品牌',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'vehicleBrand',
        align: 'center',
        flex: 1
    }, 
    {
        header: '车辆型号',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'vehicleModel',
        align: 'center',
        flex: 1
    }, 
    {
        header: '颜色',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'vehicleColor',
        align: 'center',
        flex: 1
    }, 
    {
        header: '车辆来源',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'vehicleSource',
        align: 'center',
        flex: 1
    }, 
    {
        header: '车辆所属部门',
        sortable: true,
        menuDisabled: true,
        align: 'center',
        flex: 1,
        dataIndex: 'vehicleBelongTo',
    }
],

    dockedItems : [{
        id:'addVehicleToGeofencePage',
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
            store: '{geofenceVehicleStore}'
        },
        displayInfo: true
    }],
    initComponent: function() {
        this.callParent();
    },
});
