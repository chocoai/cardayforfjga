Ext.define('Admin.view.vehiclemgmt.authorizedmgmt.GridAuthorizedRefuse', {
	extend : 'Ext.grid.Panel',
	reference: 'gridAuthorizedRefuse',
	requires : [
		'Ext.grid.column.Action'
	],
	id: 'gridAuthorizedRefuse',
	title : '编制申请已驳回列表',
    bind:{
    	store:'{authorizedApplyRefuseResults}'
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
		//	handler: 'viewAuthorizedVehicle'
        },
        modify: {
            tooltip : '修改',
			iconCls : 'x-fa fa-edit',
          //  handler: 'updateAuthorizedVehicle'
        },
        delete: {
            tooltip : '删除',
            iconCls : 'x-fa fa-close',
          //  handler: 'deleteAuthorizedVehicle'
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
            header: '单位ID',
            dataIndex: 'id',
            hidden: true
    	  },{
            header: '单位名称',
            dataIndex: 'deptName',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
            },{
            header: '级别',
            dataIndex: 'levelType',
            renderer: function(val,metaData) {
        		switch(val){
        			case 0:
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('正厅级') + '"'; 
	        				return '正厅级';
	        			case 1:
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('副厅级') + '"'; 
	        				return '副厅级';
	        			case 2:
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('处级') + '"'; 
	        				return '处级';
	        			case 3:
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('副处级') + '"'; 
	        				return '副处级';
                        case 4:
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('县级') + '"'; 
                            return '县级';
                        case 5:
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('乡科级') + '"'; 
                            return '乡科级';
        		}
            }
    	  },{
            header: '应急机要通信接待用车',
            dataIndex: 'emergencyVehNum',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	  },{
            header: '行政执法用车',
            dataIndex: 'enforcementVehNum',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	  },{
	        header: '行政执法特种专业用车',
	        dataIndex: 'specialVehNum',
	        renderer:function (value, metaData){
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            },  
   		  },{
	        header: '一般执法执勤用车',
	        dataIndex: 'normalVehNum',
	        renderer:function (value, metaData){
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            }, 
   		  },{
	        header: '执法执勤特种专业用车',
	        dataIndex: 'majorVehNum',
	        renderer:function (value, metaData){
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            }, 
   		  },
   		{
              header: '状态',
              flex: 1,
              align:'center',
              sortable: false,
              menuDisabled: true,
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
          },
   		  /*{
			xtype : 'actioncolumn',
			cls : 'content-column',
			width:180,
			header : '操作',
            align: 'center',
			items: ['@view', '@modify','@delete']
    	  }*/
   		  ]
      },
	dockedItems : [{
			id:'authorizedApplyRefusePage',
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
                store: '{authorizedApplyRefuseResults}'
            },
            displayInfo: true
		}],
	initComponent : function() {
		this.callParent();
	}
});