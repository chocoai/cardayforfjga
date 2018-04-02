Ext.define('Admin.view.orgmgmt.dialrecordmgmt.Grid', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date',
		'Admin.view.orgmgmt.dialrecordmgmt.DialRecordModel'
	],
	id:'dialRecordId',
	reference: 'gridDialRecord',
	//cls : 'user-grid',  //图片用户样式
	title : '客户来电记录列表',
	bind : {
		store : '{dialrecordResults}'
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
				width : 120,
				dataIndex : 'id',
				header : 'ID',
				hidden:true,
				align: 'center',
				menuDisabled: true,
				sortable: true,
			},
			{
				dataIndex : 'dialTime',
				flex : 1,
				header : '来电时间',
				align: 'center',
				menuDisabled: true,
				sortable: true,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, {
				flex : 1,
				dataIndex : 'dialName',
				header : '来电人姓名',
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			},
			{
				dataIndex : 'dialOrganization',
				header : '来电人单位',
				flex : 1,
				align: 'center',
				menuDisabled: true,
				sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, {
				dataIndex : 'dialPhone',
				header : '来电号码',
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
				dataIndex : 'dialType',
				header : '来电类型',
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
				dataIndex : 'recorder',
				header : '记录人',
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
				dataIndex : 'dealResult',
				header : '处理结果',
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
				dataIndex : 'dialContent',
				header : '来电内容',
				flex : 1,
				hidden: true,
				menuDisabled: true,
				sortable: false,
				align: 'center',
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, 
			{
				dataIndex : 'orderNo',
				header : '行程订单号',
				flex : 1,
				hidden: true,
				menuDisabled: true,
				sortable: false,
				align: 'center',
			}, 
			{
				dataIndex : 'vehicleNumber',
				header : '车牌号',
				flex : 1,
				hidden: true,
				align: 'center',
				menuDisabled: true,
				sortable: false,
			}, 
			{
				dataIndex : 'deviceNo',
				header : '终端设备号',
				flex : 1,
				hidden: true,
				align: 'center',
				menuDisabled: true,
				sortable: false,
			},
			{
				xtype : 'actioncolumn',
				sortable: false,
				menuDisabled: true,
				width: 100,
				//align: 'left',
				align: "center",
				cls : 'content-column',
				header : '操作',
				//tooltip : 'edit',
				items : [{
							xtype : 'button',
							tooltip : '查看',
							iconCls : 'x-fa fa-eye',
							handler: 'checkDialRecord'
						},{
							xtype : 'button',
							tooltip : '编辑',
							iconCls : 'x-fa fa-edit',
							handler : 'editDialRecord'
						},{
							xtype : 'button',
							tooltip : '删除',
							iconCls : 'x-fa fa-close',
							handler : 'deleteDialRecord'
						},/*{
							getClass: function(v, meta, record) {
	                        	var marginValue = this.width/2-55;                        
	                        	this.setMargin('0 -'+marginValue+' 0 '+marginValue);
	                        }
						}*/]
			}],

	dockedItems : [{
            id:'dialRecordPage',
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
                store: '{dialrecordResults}'
            },
            displayInfo: true
        }],
	initComponent : function() {
		this.callParent();
	}
});