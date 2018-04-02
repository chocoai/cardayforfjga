Ext.define('Admin.view.orgmgmt.rentalcompanymgmt.GridAuditInfo', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.orgmgmt.rentalcompanymgmt.RentalCompanyModel'
    ],
    reference: 'gridAuditInfo',
    viewModel: {
        xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.RentalCompanyModel'
    },
    //title: '已分配车辆列表',
    width : 750,
    xtype: 'gridAuditInfo',
    id : 'gridAuditInfoRental',
    bind: {
        store: '{auditInfoStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },

    stateful: true,
    collapsible: true,
    collapseToolText:'',
    expandToolText:'',
    forceFit: false,
    mask: true,
    multiSelect: false,
    columnLines: true, // 加上表格线
    columns: [{
        header: '审核人',
        dataIndex: 'realname',
        align: 'center',
        menuDisabled: true,
        sortable: true,
        flex: 1
    }, 
    {
        header: '审核人角色',
        dataIndex: 'role',
        align: 'center',
        menuDisabled: true,
        sortable: false,
        flex: 1
    }, 
    {
        header: '手机号码',
        dataIndex: 'phone',
        align: 'center',
        menuDisabled: true,
        sortable: false,
        flex: 1
    }, 
    {
        header: '审核状态',
        dataIndex: 'status',
        align: 'center',
        menuDisabled: true,
        sortable: false,
        flex: 1,
        renderer: function(val) {
            switch(val){
                case '0':
                    return '待审核';
                case '1':
                    return '审核未通过';
                case '2':
                    return '待服务开通';
                case '3':
                    return '服务中';
                case '4':
                    return '服务到期';
                case '5':
                    return '服务暂停';
            }
        },
    }, 
    {
        header: '驳回理由(审核未通过)',
        dataIndex: 'refuseComments',
        align: 'center',
        menuDisabled: true,
        sortable: false,
        flex: 1.5,
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    }, 
    {
        header: '审核时间',
        dataIndex: 'auditTime',
        align: 'center',
        menuDisabled: true,
        sortable: false,
        flex: 1
    }
    ],
    initComponent: function() {
        this.callParent();
    },
});
