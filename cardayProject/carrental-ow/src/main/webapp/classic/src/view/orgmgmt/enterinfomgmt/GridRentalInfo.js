Ext.define('Admin.view.orgmgmt.enterinfomgmt.GridRentalInfo', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.orgmgmt.enterinfomgmt.EnterInfoModel'
    ],
    reference: 'gridRentalInfo',
    viewModel: {
        xclass: 'Admin.view.orgmgmt.enterinfomgmt.EnterInfoModel'
    },
    title: '租车公司列表',
    width : 750,
    xtype: 'gridRentalInfo',
    id : 'gridRentalInfo',
    bind: {
        store: '{rentalInfoStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },

    scrollable: true,
    stateful: true,
//    collapsible: true,
    forceFit: false,
    mask: true,
    multiSelect: false,
    columnLines: true, // 加上表格线
    //align: 'center',
    margin: '20 0 0 10',
    columns: [{
        xtype: 'checkcolumn',
        header: '是否关联',
        dataIndex: 'isRelated',
        menuDisabled: true,
        sortable: false,
        align: 'center',
        flex: 1,
    },{
        header: '公司名称',
        dataIndex: 'rentName',
        menuDisabled: true,
        sortable: false,
        align: 'center',
        flex: 1
    }, 
    {
        header: '签约车辆数',
        dataIndex: 'vehicleNumber',
        menuDisabled: true,
        sortable: false,
        align: 'center',
        flex: 1,
        editor:{
            allowBlank:false
        },
    }, 
/*    {
        header: '签约司机数',
        align: 'center',
        dataIndex: 'driverNumber',
        flex: 1
    },*/ 
    {
        header: '实际车辆数',
        menuDisabled: true,
        sortable: false,
        align: 'center',
        dataIndex: 'realVehicleNumber',
        flex: 1,
    }, 
/*    {
        header: '实际司机数',
        align: 'center',
        dataIndex: 'realDrivaerNumber',
        flex: 1
    },*/
    ],

      plugins:{
        ptype: 'cellediting',
        clicksToEdit: 1,
        listeners : {
            beforeedit : function(editor, e) {
                if(e.colIdx == 2 && !e.record.data.isRelated){
                    return false;
                }else{
                    return true;
                }
            }
        }
      },
    initComponent: function() {
        this.callParent();
    },
});
