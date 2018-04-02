Ext.define('Admin.view.systemmgmt.driverAllowanceSetting.AllowanceConfigGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.systemmgmt.driverAllowanceSetting.ViewModel'
    ],
    reference: 'allowanceConfigGrid',
    id:'allowanceConfigGrid',
    title: '报警配置',
    bind:{
    	store:'{allowanceConfigResults}'
    },
   	viewConfig: {
        loadMask: true,
       	loadingText: '加载中...',
       	emptyText: '无记录！',
        deferEmptyText: false
    },
    stateful: true,
    collapsible: false,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    columns: {
    	defaults:{
				align:'center',
				flex:1,
				sortable: false,
				menuDisabled: true
		},
		items:[{
	   		 	dataIndex: 'id',
	        	hidden:true
	    	},{
             	header: '补贴类型',
       		 	dataIndex: 'allowanceName',
            	flex:1
        	},{
	            header: '配置值',
                dataIndex: 'allowanceValue',
                flex:1
      		},
        	{
	            xtype: 'actioncolumn',
	            items: [{
	                    xtype: 'button',
                        tooltip : '设置',
                        iconCls : 'x-fa fa-check',
                        handler: 'editAllowanceConfig'
	                }],
	            cls: 'content-column',
                align:'center',
	            text: '操作'//,
        }]
    },
});
