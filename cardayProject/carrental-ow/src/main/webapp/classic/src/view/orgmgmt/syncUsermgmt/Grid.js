Ext.define('Admin.view.orgmgmt.syncUsermgmt.Grid', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date'
	],
	id:'syncUserId',
	reference: 'syncUserId',
	//cls : 'user-grid',  //图片用户样式
	title : '客户同步记录列表',
	bind : {
		store : '{syncUserResult}'
	},
	viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
	scrollable : false,
	forceFit: false,
	stateful: true,
   	columnLines: true, 
	columns : [
		/**
			{
				width : 120,
				dataIndex : 'id',
				header : 'ID',
				hidden:true,
				align: 'center',
				menuDisabled: true,
				sortable: true,
			},
			{
				dataIndex : 'creationDate',
				header : 'creationDate',
				flex : 1,
				align: 'center',
				menuDisabled: true,
				sortable: true,
				hidden:true,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			},
			**/
			{
				dataIndex : 'ecName',
				flex : 1,
				header : '客户名称',
				align: 'center',
				menuDisabled: true,
				sortable: true,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, 
			{
				flex : 1,
				dataIndex : 'ecAbbrev',
				header : '客户简称',
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			},
			{
				dataIndex : 'ecAddress',
				header : '地址',
				flex : 1,
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, {
				dataIndex : 'ecIntro',
				header : '客户介绍',
				flex : 1,
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, 
			{
				dataIndex : 'kuName',
				header : '关键人姓名',
				flex : 1,
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, 
			{
				dataIndex : 'kuPhone',
				header : '联系电话',
				flex : 1,
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, 
			{
				dataIndex : 'kuEmail',
				header : '邮箱',
				flex : 1,
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			},
			{
				dataIndex : 'syncStatus',
				header : '处理结果',
				flex : 1,
				menuDisabled: true,
				sortable: false,
				align: 'center',
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			},
			{
				dataIndex : 'syncMsg',
				header : '结果信息',
				flex : 1,
				menuDisabled: true,
				sortable: false,
				align: 'center',
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}],

	dockedItems : [{
            id:'syncUserPage',
            pageSize:10,
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
                store: '{syncUserResult}'
            },
            displayInfo: true
        }],
	initComponent : function() {
		this.callParent();
	}
});