Ext.define('Admin.view.carnumbermgmt.carnumberapply.GridRefuse', {
	extend : 'Ext.grid.Panel',
	reference: 'gridRefuse',
	requires : [
		'Ext.grid.column.Action'
	],

	title : '警车号牌已驳回列表',
    bind:{
    	store:'{refuseResults}'
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
			handler: 'viewRefuse'
        },
        modify: {
            tooltip : '修改',
			iconCls : 'x-fa fa-edit',
            handler: 'editRefuse'
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
            header: 'id',
            dataIndex: 'id',
            hidden: true
    	  },{
            header: '申请单位',
            dataIndex: 'applyOrganization',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(window.sessionStorage.getItem("organizationName")) + '"';  
                return window.sessionStorage.getItem("organizationName");  
            }
          },{
              header: '品牌型号',
              dataIndex: 'vehicleModel',
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
            header: '车辆识别代号/车架号',
            dataIndex: 'vin',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	  },{
              header: '发动机号',
              dataIndex: 'engineNumber',
              renderer:function (value, metaData){  
                  metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                  return value;  
              }
      	  },{
              header: '联系人',
              dataIndex: 'contactPerson',
              renderer:function (value, metaData){  
                  metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                  return value;  
              }
      	  },{
              header: '电话',
              dataIndex: 'phone',
              renderer:function (value, metaData){  
                  metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                  return value;  
              }
      	  },{
              header: '驳回理由',
              dataIndex: 'refuseReason',
              renderer:function (value, metaData){  
                  metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                  return value;  
              }
      	  },{
              header: '申请理由',
              hidden: true,
              dataIndex: 'applyReason',
              renderer:function (value, metaData){  
                  metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                  return value;  
              }
      	  },{
	        header: '驳回时间',
	        //formatter: 'date("Y-m-d")',
	        dataIndex: 'refuseTime',
	        renderer:function (value, metaData){
                value = Ext.util.Format.date(value,'Y-m-d');
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            },  
   		  },
   		{
  	        header: '申请时间',
  	        hidden: true,
  	        //formatter: 'date("Y-m-d")',
  	        dataIndex: 'applyTime',
  	        renderer:function (value, metaData){
                  value = Ext.util.Format.date(value,'Y-m-d');
                  metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                  return value;
              },  
     	},{
              header: '状态',
              dataIndex: 'status',
              renderer: function(val,metaData) {
                  switch(val){
                      case "0":
                          metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('申请中') + '"'; 
                          return "<span style='color:blue;'>申请中</span>";
                      case "1":
                          metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('已驳回') + '"'; 
                          return "<span style='color:red;'>已驳回</span>";
                      case "2":
                          metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('审核通过') + '"'; 
                          return "<span style='color:green;'>审核通过</span>";
                  }
              }
      	  },{
			xtype : 'actioncolumn',
			cls : 'content-column',
			width:180,
			header : '操作',
            align: 'center',
			items: ['@view', '@modify']
    	  }]
      },
	dockedItems : [{
			//id:'authorizedVehiclePage',
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
/*            bind: {
                store: '{authorizedVehicleResults}'
            },*/
            displayInfo: true
		}],
	initComponent : function() {
		this.callParent();
	}
});