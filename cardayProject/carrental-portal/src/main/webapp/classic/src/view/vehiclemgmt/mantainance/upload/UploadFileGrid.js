Ext.define('Admin.view.vehiclemgmt.mantainance.upload.UploadFileGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
    ],
    id: 'uploadFileGrid',
    reference: 'uploadFileGrid',
    title: '导入文件列表',
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.mantainance.upload.UploadFileModel'
    },
    bind: {
        store: '{uploadFileStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    listeners: {
    	afterrender : 'loadUploadFileData',
    },
    stateful: true,
    collapsible: true,
    forceFit: false,
    mask: true,
    // collapsed: true,
    multiSelect: false,
    columnLines: true, // 加上表格线
    align: 'center',
    
    columns: [
		{
		    header: '文件',
		    sortable: false,
		    menuDisabled: true,
		    dataIndex: 'fileName',
		    flex: 1
		}, {
		xtype : 'actioncolumn',
		items : [ {
			xtype : 'button',
			tooltip : '删除',
			iconCls : 'x-fa fa-refresh',
			handler : 'onClickReset'
		} ],

		cls : 'content-column',
		width : 200,
		align: 'center',
		text : '操作',
		//tooltip : 'edit '

	}
    ],
    initComponent: function() {
        this.callParent();
    },
});
