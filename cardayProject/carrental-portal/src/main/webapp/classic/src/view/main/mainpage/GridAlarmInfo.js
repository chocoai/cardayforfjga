Ext.define('Admin.view.main.mainpage.GridAlarmInfo', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
    ],
    
    id: 'gridAlarmInfo',
    reference: 'gridAlarmInfo',
    title: '报警信息',
    cls : 'user-grid',  //图片用户样式
/*    bind:{
    	store:'{stationsResults}'
    },*/
   viewConfig: {
        loadMask: true,
       	loadingText: '加载中...',
       	emptyText: '无记录！',
        deferEmptyText: false
    },
     controller: {
        xclass: 'Admin.view.main.mainpage.ViewController'
    },
	viewModel: {
        xclass: 'Admin.view.main.MainModel'
    },
     bind: {
        store: '{AlertInfo}'
    },
    listeners:{
    	afterrender: 'onSearchClick',
        itemclick: 'itemGridAlarmInfoClick',
    },
    
    stateful: true,
    //collapsible: true,
    forceFit: false,
    mask: true,
    //columnLines: true, // 加上表格线
    align: 'center',
    selModel: {
        injectCheckbox: 0,
        mode: "SINGLE",     //"SINGLE"/"SIMPLE"/"MULTI"
        checkOnly: true     //只能通过checkbox选择
    },
    //bodyStyle: 'background:#f2f2f2',

    columns: [{
            xtype : 'gridcolumn',
            header: '报警ID',
            sortable: true,
            dataIndex: 'id',
            align:'center',
            menuDisabled: true,
            flex: 3,
            name: 'id',
            hidden: true,
        },{
    		xtype : 'gridcolumn',
            header: '报警时间',
            sortable: true,
            dataIndex: 'alertTime',
            align:'center',
            menuDisabled: true,
            formatter: 'date("Y-m-d H:i:s")',
            flex:4,
            minWidth:120,
            name: 'alarmTime',
        }, 
        {
            header: '车牌号',
            sortable: true,
            dataIndex: 'vehicleNumber',
            align:'center',
            menuDisabled: true,
            flex: 2.5,
            minWidth:80,
            name: 'plateNum',
        },
        {
            header: '所属部门',
            sortable: true,
            dataIndex: 'currentuseOrgName',
            align:'center',
            menuDisabled: true,
            flex: 2.5,
            minWidth:80,
            name: 'department',
        },
        {
            header: '报警类型',
            sortable: true,
            dataIndex: 'alertType',
            align:'center',
            menuDisabled: true,
            flex: 2.5,
            minWidth:80,
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
    dockedItems : [{
         	id:'alertPage',
            pageSize:10,
            xtype: 'pagingtoolbar',
            dock: 'bottom',
            displayMsg: '',
            emptyMsg : '',
            beforePageText: "第",
            afterPageText: "页，共{0}页",
            nextText: "下一页",
            prevText: "上一页",
            refreshText: "刷新",
            firstText: "第一页",
            lastText: "最后一页",
 			bind: {
                store: '{AlertInfo}'
            },
            displayInfo: true
        }],
    
    initComponent: function() {
        this.callParent();

    },
});
