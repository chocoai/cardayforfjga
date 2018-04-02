Ext.define('Admin.view.vehiclemgmt.vehiclemaintain.Grid', {
	extend : 'Ext.grid.Panel',
	reference: 'gridVehicleMaintain',
	requires : [
		'Ext.grid.column.Action'
	],
	id: 'vehicleMaintainGrid',
	title : '车辆维修列表',
    bind:{
    	store:'{vehicleMaintainResults}'
    },

	viewConfig: {
        loadMask: true,
       	loadingText: '加载中...',
       	emptyText: '无记录！',
        deferEmptyText: false
    },
	stateful: true,
	multiSelect: false,
	forceFit: false,
    mask: true,
   	columnLines: true,  //表格线

    actions: {
        view: {
            tooltip : '查看',
			iconCls : 'x-fa fa-eye',
			handler: 'viewVehicleMaintain'
        },
        modify: {
            tooltip : '修改',
			iconCls : 'x-fa fa-edit',
            handler: 'updateVehicleMaintain'
        },
        delete: {
            tooltip : '删除',
            iconCls : 'x-fa fa-close',
            handler: 'deleteVehicleMaintain'
        }
    },
	columns:{
		defaults:{
			align:'center',
			sortable: false,
			menuDisabled: true,
			flex:1
			},
		items:[
		  {
            header: '加油ID',
            dataIndex: 'id',
            hidden: true
    	  },{
            header: '车牌号',
            dataIndex: 'vehicleNumber',
            renderer:function (value, metaData){  
                if(Ext.getCmp('main').vehicelList != null){
                    var vehicleNumber = Ext.getCmp('main').vehicelList[metaData.recordIndex].vehicleNumber;
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(vehicleNumber) + '"';  
                    return vehicleNumber;  
                }else{                    
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                    return value; 
                }
            }
            },{
            header: '维修项目',
            dataIndex: 'maintenanceItem',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	  },{
            header: '数量',
            dataIndex: 'amount',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
          },{
            header: '单价',
            dataIndex: 'unitPrice',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value + '元') + '"';  
                return value + '元';  
            }
          },{
            header: '费用',
            dataIndex: 'totalAccount',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value + '元') + '"';  
                return value + '元';  
            }
          },{
			xtype : 'actioncolumn',
			cls : 'content-column',
			width:180,
			header : '操作',
            align: 'center',
			items: ['@view', '@modify','@delete']
    	  }]
      },
	dockedItems : [{
			id:'vehicleMaintainPage',
			pageSize:5,
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
                store: '{vehicleMaintainResults}'
            },
            displayInfo: true
		}],
	initComponent : function() {
		this.callParent();
	}
});