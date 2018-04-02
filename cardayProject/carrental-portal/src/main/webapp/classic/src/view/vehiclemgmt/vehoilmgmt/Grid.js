Ext.define('Admin.view.vehiclemgmt.vehoilmgmt.Grid', {
	extend : 'Ext.grid.Panel',
	reference: 'gridAuthorizedVehicle',
	requires : [
		'Ext.grid.column.Action'
	],
	id: 'vehoilmgmtGrid',
	title : '加油信息列表',
    bind:{
    	store:'{vehoilmgmtResults}'
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
			handler: 'viewVehoilmgmt'
        },
        modify: {
            tooltip : '修改',
			iconCls : 'x-fa fa-edit',
            handler: 'updateVehoilmgmt'
        },
        delete: {
            tooltip : '删除',
            iconCls : 'x-fa fa-close',
            handler: 'deleteVehoilmgmt'
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
                if(Ext.getCmp('main').vehicelList != null && Ext.getCmp('main').vehicelList.length > metaData.recordIndex){
                    var vehicleNumber = Ext.getCmp('main').vehicelList[metaData.recordIndex].vehicleNumber;
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(vehicleNumber) + '"';  
                    return vehicleNumber;  
                }else{                    
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                    return value; 
                }
            }
            },{
            header: '公车性质',
            dataIndex: 'vehicleType',
            renderer: function(val,metaData) {
                var vehicleType;
                if(Ext.getCmp('main').vehicelList != null && Ext.getCmp('main').vehicelList.length > metaData.recordIndex){
                    vehicleType = Ext.getCmp('main').vehicelList[metaData.recordIndex].vehicleType;    
                }else{     
                    vehicleType = val;
                }

                switch(vehicleType){
                    case '0':
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('应急机要通信接待用车') + '"'; 
                        return '应急机要通信接待用车';
                    case '1':
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('行政执法用车') + '"'; 
                        return '行政执法用车';
                    case '2':
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('行政执法特种专业用车') + '"'; 
                        return '行政执法特种专业用车';
                    case '3':
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('一般执法执勤用车') + '"'; 
                        return '一般执法执勤用车';
                    case '4':
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('执法执勤特种专业用车') + '"'; 
                        return '执法执勤特种专业用车';
                }
            }
          },{
            header: '车辆品牌',
            dataIndex: 'vehicleBrand',
            renderer:function (value, metaData){  
                if(Ext.getCmp('main').vehicelList != null && Ext.getCmp('main').vehicelList.length > metaData.recordIndex){
                    var vehicleBrand = Ext.getCmp('main').vehicelList[metaData.recordIndex].vehicleBrand;
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(vehicleBrand) + '"';  
                    return vehicleBrand;  
                }else{                    
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                    return value; 
                }
            }
    	  },{
            header: '车辆型号',
            dataIndex: 'vehicleModel',
            renderer:function (value, metaData){  
                if(Ext.getCmp('main').vehicelList != null && Ext.getCmp('main').vehicelList.length > metaData.recordIndex){
                    var vehicleModel = Ext.getCmp('main').vehicelList[metaData.recordIndex].vehicleModel;
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(vehicleModel) + '"';  
                    return vehicleModel;  
                }else{                    
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                    return value; 
                } 
            }
    	  },{
            header: '油科类别',
            dataIndex: 'oilType',
            renderer: function(val,metaData) {
                switch(val){
                    case 0:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('90号汽油') + '"'; 
                        return '90号汽油';
                    case 1:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('93号汽油') + '"'; 
                        return '93号汽油';
                    case 2:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('97号汽油') + '"'; 
                        return '97号汽油';
                    case 3:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('0号柴油') + '"'; 
                        return '0号柴油';
                }
            }
          },{
            header: '单价',
            dataIndex: 'unitPrice',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value + '元/升') + '"';  
                return value + '元/升';  
            }
          },{
            header: '数量',
            dataIndex: 'amount',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value + '升') + '"';  
                return value + '升';  
            }
          },{
            header: '加油日期',
            dataIndex: 'oilTime',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
          },{
            header: '司机',
            dataIndex: 'driver',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
          },{
            header: '车辆属性',
            dataIndex: 'vehicleAttribute',
            hidden:true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
          },{
            header: '总金额',
            dataIndex: 'totalAccount',
            hidden:true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
          },{
            fieldLabel : '卡号',
            dataIndex : 'cardNumber',
            hidden:true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            fieldLabel : '上次加油里程',
            dataIndex : 'lastMileage',
            hidden:true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            fieldLabel : '本次加油里程',
            dataIndex : 'mileage',
            hidden:true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            fieldLabel : '行驶里程',
            dataIndex : 'driveMileage',
            hidden:true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            fieldLabel : '百公里油耗',
            dataIndex : 'theoreticalFuelCon',
            hidden:true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            fieldLabel : '加油点',
            hidden:true,
            dataIndex : 'oilAddress',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            header: '所属部门',
            dataIndex: 'arrangedOrgName',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
          },{
            fieldLabel : '备注',
            hidden:true,
            dataIndex : 'remark',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
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
			id:'vehoilmgmtPage',
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
                store: '{vehoilmgmtResults}'
            },
            displayInfo: true
		}],
	initComponent : function() {
		this.callParent();
	}
});