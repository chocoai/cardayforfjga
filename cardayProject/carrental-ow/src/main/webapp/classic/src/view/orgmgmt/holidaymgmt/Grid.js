Ext.define('Admin.view.orgmgmt.holidaymgmt.Grid', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date',
		'Admin.view.orgmgmt.holidaymgmt.HolidayModel'
	],
	id:'holidayId',
	reference: 'gridHoliday',
	//cls : 'user-grid',  //图片用户样式
	title : '节假日列表',
	bind : {
		store : '{holidayResults}'
	},
	viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
	scrollable : false,
	stateful: true,
   	columnLines: true,
   	forceFit: false,
	columns : [
			{
				width : 120,
				dataIndex : 'id',
				text : 'ID',
				hidden:true,
				align: 'center',
		        menuDisabled: true,
		        sortable: true,
			},
			{
				dataIndex : 'holidayYear',
				flex : 1,
				text : '年份',
				align: 'center',
		        menuDisabled: true,
		        sortable: true,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, {
				flex : 1,
				dataIndex : 'holidayType',
				text : '节假日',
				align: 'center',
		        menuDisabled: true,
		        sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			},
			{
				dataIndex : 'holidayTime',
				text : '休息日',
				flex : 3,
				align: 'center',
		        menuDisabled: true,
		        sortable: false,
				renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
			}, {
				dataIndex : 'adjustHolidayTime',
				text : '调休上班日',
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
				xtype : 'actioncolumn',
				sortable: false,
				menuDisabled: true,
				width: 100,
				align: "center",
				cls : 'content-column',
				text : '操作',
				//align: 'left',
				//tooltip : 'edit',
				items : [{
							xtype : 'button',
							tooltip : '查看',
							iconCls : 'x-fa fa-eye',
							handler: 'viewHoliday'
						},{
							xtype : 'button',
							tooltip : '编辑',
							iconCls : 'x-fa fa-edit',
							handler : 'editHoliday'
						},{
							xtype : 'button',
							tooltip : '删除',
							iconCls : 'x-fa fa-close',
							handler : 'deleteHoliday'
						},/*{
							getClass: function(v, meta, record) {
	                        	var marginValue = this.width/2-55;                        
	                        	this.setMargin('0 -'+marginValue+' 0 '+marginValue);
	                    	}
						}*/]
			}],

	dockedItems : [{
            id:'holidayPageId',
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
                store: '{holidayResults}'
            },
            displayInfo: true
        }],
	initComponent : function() {
		this.callParent();
	}
});