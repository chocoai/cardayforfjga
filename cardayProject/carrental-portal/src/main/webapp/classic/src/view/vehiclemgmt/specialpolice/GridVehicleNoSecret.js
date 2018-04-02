Ext.define('Admin.view.vehiclemgmt.specialpolice.GridVehicleNoSecret', {
	extend : 'Ext.grid.Panel',
	reference: 'gridVehicleNoSecret',
	requires : [
		'Ext.grid.column.Action'
	],

    id:'gridVehicleNoSecret',
	title : '未涉密车辆列表',
    bind:{
    	store:'{vehiNoSecretResults}'
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
			handler: 'viewVehicleInfo'
        },
        modifyTrafficPkg: {
            tooltip : '修改M2M状态',
            iconCls : 'x-fa fa-edit',
            handler: 'updateTrafficPkg'
        },
        modifyNoSecret: {
            tooltip : '开启涉密',
            iconCls : 'x-fa fa-check',
            handler: 'modifySecretStatus'
        },
        modifyNoApprove: {
            tooltip : '修改审批状态',
            iconCls : 'x-fa fa-pencil',
            handler: 'modifyApproveStatus'
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
            header: '车辆id',
            dataIndex: 'id',
            hidden: true
          },{
            header: '车牌号',
            dataIndex: 'vehicleNumber',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
            },{
            header: '公车性质',
            dataIndex: 'vehicleType',
            renderer: function(val,metaData) {
                switch(val){
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
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
          },{
            header: '车辆型号',
            dataIndex: 'vehicleModel',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
          },{
            header: '车架号',
            dataIndex: 'vehicleIdentification',
            hidden:true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
          },{
            header: 'ICCID编号',
            dataIndex: 'iccidNumber',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
          },{
              header: 'SIM卡号',
              dataIndex: 'simNumber',
              renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
                }
          },{
            header: 'M2M状态',
            dataIndex: 'enableTrafficPkg',
            renderer: function(val,metaData) {
                switch(val){
                    case 0:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('流量关闭') + '"'; 
                        return "<span style='color:red;'>流量关闭</span>";
                    case 1:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('有效开启') + '"'; 
                        return "<span style='color:blue;'>有效开启</span>";
                }
            }
          },{
            header: '涉密状态',
            dataIndex: 'enableSecret',
            renderer: function(val,metaData) {
                switch(val){
                    case 0:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('未涉密') + '"'; 
                        return "<span style='color:blue;'>未涉密</span>";
                    case 1:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('已涉密') + '"'; 
                        return "<span style='color:red;'>已涉密</span>";
                }
            }
          },{
            header: '审批状态',
            dataIndex: 'noNeedApprove',
            renderer: function(val,metaData) {
                switch(val){
                    case 1:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('免审') + '"'; 
                        return "<span style='color:red;'>免审</span>";
                    default:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('未免审') + '"'; 
                        return "<span style='color:blue;'>未免审</span>";
                }
            }
          },{
			xtype : 'actioncolumn',
            flex: 1,
			cls : 'content-column',
			header : '操作',
            align: 'center',
            menuDisabled: true,
			items: ['@view', '@modifyTrafficPkg', '@modifyNoSecret', '@modifyNoApprove']
    	  }]},
	dockedItems : [{
			id:'vehiNoSecretPage',
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
                store: '{vehiNoSecretResults}'
            },
            displayInfo: true
		}],
	initComponent : function() {
		this.callParent();
	}
});