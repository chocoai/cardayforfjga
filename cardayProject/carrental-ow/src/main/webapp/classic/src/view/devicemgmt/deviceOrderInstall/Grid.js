Ext.define('Admin.view.devicemgmt.deviceOrderInstall.Grid', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date',
		'Admin.view.devicemgmt.deviceOrderInstall.DeviceOrderInstallModel'
	],
	id:'gridDeviceOrderInstall',
	reference: 'gridDeviceOrderInstall',

	title : '安装预约列表',
	bind : {
		store : '{orderInstallResults}'
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
			{
				dataIndex : 'id',
				flex : 1,
				header : '预约ID',
				align: 'center',
				menuDisabled: true,
				sortable: true,
			},
			{
				dataIndex : 'orderTime',
				flex : 1,
				header : '预约安装时间',
				align: 'center',
				menuDisabled: true,
				sortable: true,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, {
				flex : 1,
				dataIndex : 'corporateCustomer',
				header : '企业客户',
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			},
			{
				dataIndex : 'installVehNum',
				header : '安装车辆数',
				flex : 1,
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, {
				dataIndex : 'installDeviceNum',
				header : '安装设备数',
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
				dataIndex : 'installAddress',
				header : '安装预约地点',
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
				dataIndex : 'businessContact',
				header : '企业联系人',
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
				dataIndex : 'contactPhone',
				header : '联系人电话',
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
				dataIndex : 'status',
				header : '状态',
				flex : 1,
				menuDisabled: true,
				sortable: false,
				align: 'center',
				renderer:function (value, metaData){ 
					if(value=="0") {
						return "已完成";
					}else if(value=="1") {
						return "未完成";
					}
	            }
			}, 
			{
				xtype : 'actioncolumn',
				sortable: false,
				menuDisabled: true,
				width: 100,
				align: "center",
				cls : 'content-column',
				header : '操作',
				items : [{
							xtype : 'button',
							tooltip : '查看',
							iconCls : 'x-fa fa-eye',
							handler: 'checkOrderInstall'
						},{
							xtype : 'button',
							tooltip : '安装确认',
							iconCls : 'x-fa fa-check',
							handler : 'confirmOrderInstall'
						},]
			}],

	dockedItems : [{
            id:'orderInstallPage',
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
                store: '{orderInstallResults}'
            },
            displayInfo: true
        }],
	initComponent : function() {
		this.callParent();
	}
});