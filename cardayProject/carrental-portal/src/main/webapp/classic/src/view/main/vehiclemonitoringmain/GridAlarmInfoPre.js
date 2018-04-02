Ext.define('Admin.view.main.vehiclemonitoringmain.GridAlarmInfoPre', {
    extend: 'Ext.grid.Panel',
    requires: [
    ],
    
        id:'alertPreGrid', 
        flex: 1, 
        columnLines: true, // 加上表格线
        bind: {
            store: '{AlertInfoPreMonitoring}'
        },
        listeners:{
            itemclick: 'itemGridAlarmInfoClick',
        },
        columns: [{
                xtype : 'gridcolumn',
                header: '序号',
                sortable: true,
                align: 'center',
                menuDisabled: true,
                dataIndex: 'id',
                flex: 1,
                name: 'id',
            },{
                xtype : 'gridcolumn',
                header: '报警时间',
                dataIndex: 'alertTime',
                sortable: false,
                align: 'center',
                menuDisabled: true,
                formatter: 'date("Y-m-d H:i:s")',
                flex:2.5,
                name: 'alarmTime',
            }, 
            {
                header: '车牌号',
                dataIndex: 'vehicleNumber',
                sortable: false,
                align: 'center',
                menuDisabled: true,
                flex: 2,
                name: 'plateNum',
            },
            {
                header: '所属部门',
                dataIndex: 'currentuseOrgName',
                sortable: false,
                align: 'center',
                menuDisabled: true,
                flex: 2,
                name: 'department',
            },
            {
                header: '报警类型',
                dataIndex: 'alertType',
                sortable: false,
                align: 'center',
                menuDisabled: true,
                flex: 2,
                 renderer: function(val) {
                    switch(val){
                        case 'OUTBOUND':
                            return '越界报警';
                        case 'OVERSPEED':
                            return '超速报警';
                        case 'VEHICLEBACK':
                            return '回车报警';
                    }
                },
                name: 'alarmType',
            }
        ],

    
    initComponent: function() {
        this.callParent();

    },
});
