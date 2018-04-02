Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.GridAssignedVehicle', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date',
		'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.VehicleMgmtModel'
	],

    id:'gridAssignedVehicle',

    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.VehicleMgmtModel'
    },

	reference: 'gridAssignedVehicle',
    AllSelectedAssignedRecords: new Array(),
	title : '现有车辆列表',
	bind : {
		store : '{assignedVehicleStore}'
	},
	viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
	scrollable : true,
	stateful: true,
   	columnLines: true, 

    selModel: {
        injectCheckbox: 0,
        mode: "SIMPLE",     //"SINGLE"/"SIMPLE"/"MULTI"
        //checkOnly: true     //只能通过checkbox选择
        listeners: {
            select: 'checkVehSelectAssigned',
            deselect: 'checkVehdeSelectAssigned',
        }
    },
    selType: 'checkboxmodel',//可以对表格的数据进行多选
	columns : [
			{
				xtype : 'gridcolumn',
				width : 120,
				dataIndex : 'id',
                align: 'center',
				text : '车辆ID',
                sortable: true,
                menuDisabled: true,
			},{
            header: '车牌号',
            dataIndex: 'vehicleNumber',
            align: 'center',
            sortable: false,
            menuDisabled: true,
            flex:1
          },{
            header: '公车性质',
            dataIndex: 'vehicleType',
            align: 'center',            
            sortable: false,
            menuDisabled: true,
            flex:1,
            renderer: function(val) {
                switch(val){
                    case '0':
                        return '应急机要通信接待用车';
                    case '1':
                        return '行政执法用车';
                    case '2':
                        return '行政执法特种专业用车';
                    case '3':
                        return '一般执法执勤用车';
                    case '4':
                        return '执法执勤特种专业用车';
                }
            }
          },{
            header: '品牌',
            align: 'center',
            dataIndex: 'vehicleBrand',
            sortable: false,
            menuDisabled: true,
            flex:1
          },{
            header: '型号',
            align: 'center',
            dataIndex: 'vehicleModel',
            sortable: false,
            menuDisabled: true,
            flex:1
          },{
            header: '车辆来源',
            align: 'center',
            dataIndex: 'vehicleFromName',
            sortable: false,
            menuDisabled: true,
            flex:1,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
          },{
            header: '车辆用途',
            align: 'center',
            dataIndex: 'vehiclePurpose',
            sortable: false,
            menuDisabled: true,
            hidden:true,
            flex:1
          },{
            xtype : 'actioncolumn',
            cls : 'content-column',
            align: 'center',
            flex:1,
            header : '操作',
				items : [{
							xtype : 'button',
							tooltip : '查看',
							iconCls : 'x-fa fa-eye',
							handler: 'viewVehicleInfo'
						},{
                            xtype : 'button',
                            tooltip : '移除',
                            iconCls : 'x-fa fa-close',
                            handler : 'unassignVehicle'
                        }]
			}],

	dockedItems : [{
            id:'assignedVehiclePage',
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
                store: '{assignedVehicleStore}'
            },
            displayInfo: true
        }],
	initComponent : function() {
		this.callParent();
	}
});