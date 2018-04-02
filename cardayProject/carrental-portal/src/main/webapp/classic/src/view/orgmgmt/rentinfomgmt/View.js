Ext.define('Admin.view.rentinfomgmt.View', {
    extend: 'Ext.grid.Panel',
    xtype: 'RentInfoMgmt',
    id:'rentId',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date'
    ],
    controller: {
        xclass: 'Admin.view.orgmgmt.rentinfomgmt.ViewController'
    },
    viewModel : {
		type : 'rentInfoModel' 
	},
	bind : {
		store : '{rentsResults}'
	},
	listeners:{
    	afterrender: 'onView'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    title : '租户列表',
    frame: true,
    scrollable : false,
	//stateful: true,
   	columnLines: true,  //表格线
   
	columns:{
		defaults:{
   				align:'center'
   				},
		items:[{
				xtype : 'gridcolumn',
				width : 200,
				dataIndex : 'name',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
				text : '租户名称'

			},{
				xtype : 'gridcolumn',
				flex : 1,
				dataIndex : 'linkman',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
				text : '联系人'
			},{
				xtype : 'gridcolumn',
				cls : 'content-column',
				dataIndex : 'linkmanEmail',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
				text : '联系人邮箱',
				flex : 1
			},{
				xtype : 'gridcolumn',
				cls : 'content-column',
				dataIndex : 'linkmanPhone',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
				text : '联系电话',
				flex : 1
			}]
   	},
	/*tbar: [{
			xtype: "textfield",
			emptyText:'租户名称',
			width:220
		 },{
			xtype: "button",
			text: '<i class="fa fa-search"></i>&nbsp;查询'
			//handler: ""
		 }],*/
	bbar: {
			xtype: 'pagingtoolbar',
//            dock: 'bottom',
            displayMsg: 'show {0} - {1} records, {2} records in total',
            emptyMsg : 'No relevant records, please modify the query conditions!',
//            bind: {
//                store: '{usersResults}'
//            },
            displayInfo: true
		 }
});
