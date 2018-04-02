Ext.define('Admin.view.vehiclemgmt.authorizedmgmt.GridAuthorizedApply', {
	extend : 'Ext.grid.Panel',
	reference: 'gridAuthorizedApply',
	id:'gridAuthorizedApply',
	requires : [
		'Ext.grid.column.Action'
	],

	title : '编制调整申请列表',
    bind:{
    	store:'{authorizedApplyResults}'
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
			handler: 'viewAuthorizedVehiApply'
        },
      refuse: {
            tooltip : '驳回',
            iconCls : 'x-fa fa-close',
            handler: 'refuseAuthorizedVehi'
        },
        audited: {
            tooltip : '审核通过',
            iconCls : 'x-fa fa-check',
            handler: 'authorizedVehiApproved'
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
            header: '申请表ID',
            dataIndex: 'id',
            hidden: true
    	  },{
            header: '申请单位',
            dataIndex: 'deptName',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            header: '单据编号',
            dataIndex: 'docCode',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            header: '新增警力',
            dataIndex: 'policeAdd',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '应急机要通信接待用车编制数量',
            dataIndex: 'emergencyVehAuthNum',
           /* renderer: function(val,metaData) {
            	 metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                 return value;  
            }*/
          },
              {
              header: '应急机要通信接待用车实有数量',
              dataIndex: 'emergencyVehAddNum',
              /*renderer: function(val,metaData) {
              	 metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                   return value;  
              }*/
            },
            {
                header: '应急机要通信接待用车申请数量',
                dataIndex: 'emergencyVehRealNum',
              /*  renderer: function(val,metaData) {
                	 metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                     return value;  
                }*/
              },
              
          {
              header: '行政执法用车编制数量',
              dataIndex: 'enforcementVehAuthNum',
              /*renderer: function(val,metaData) {
              	 metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                   return value;  
              }*/
            },
            {
                header: '行政执法用车实有数量',
                dataIndex: 'enforcementVehRealNum',
               /* renderer: function(val,metaData) {
                	 metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                     return value;  
                }*/
             },
             {
                 header: '行政执法用车申请数量',
                 dataIndex: 'enforcementVehAddNum',
                /* renderer: function(val,metaData) {
                 	 metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                      return value;  
                 }*/
              },
              
              {
                  header: '行政执法特种专业用车编制数量',
                  dataIndex: 'specialVehAuthNum',
                  /*renderer: function(val,metaData) {
                  	 metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                       return value;  
                  }*/
                },
                {
                    header: '行政执法特种专业用车实有数量',
                    dataIndex: 'specialVehRealNum',
                   /* renderer: function(val,metaData) {
                    	 metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                         return value;  
                    }*/
                 },
                 {
                     header: '行政执法特种专业用车申请数量',
                     dataIndex: 'specialVehAddNum',
                     /*renderer: function(val,metaData) {
                     	 metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                          return value;  
                     }*/
                  },
              
             {
                  header: '一般执法执勤用车编制数量',
                  dataIndex: 'normalVehAuthNum',
                 /* renderer: function(val,metaData) {
                  	 metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                       return value;  
                  }*/
               },
               {
                   header: '一般执法执勤用车实有数量',
                   dataIndex: 'normalVehRealNum',
                   /*renderer: function(val,metaData) {
                   	 metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                   }*/
                },
                {
                    header: '一般执法执勤用车申请数量',
                    dataIndex: 'normalVehAddNum',
                    /*renderer: function(val,metaData) {
                    	 metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                         return value;  
                    }*/
                 },
          {
            header: '执法执勤特种专业用车编制数量',
            dataIndex: 'majorVehAuthNum',
           /* renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }*/
        },
        {
            header: '执法执勤特种专业用车实有数量',
            dataIndex: 'majorVehRealNum',
            /*renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }*/
        },
        {
            header: '执法执勤特种专业用车申请数量',
            dataIndex: 'majorVehAddNum',
           /* renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }*/
        },
        
        {
            header: '原因',
            dataIndex: 'cause',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        /*{
            header: '申请时间',
            dataIndex: 'applyTime',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
            
        },*/
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
        {
            header: '附件',
            dataIndex: 'attachName',
            hidden:true,
        },
        {
			xtype : 'actioncolumn',
			cls : 'content-column',
			header : '操作',
            align: 'center',
			items: ['@view', '@refuse', '@audited']//
    	  }]
		},
	dockedItems : [{
			id:'authorizedApplyPage',
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
                store: '{authorizedApplyResults}'
            },
            displayInfo: true
		}],
	initComponent : function() {
		this.callParent();
	}
});