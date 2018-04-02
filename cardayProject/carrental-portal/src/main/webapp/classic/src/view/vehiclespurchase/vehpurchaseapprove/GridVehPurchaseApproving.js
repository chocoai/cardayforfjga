Ext.define('Admin.view.vehiclespurchase.vehpurchaseapprove.GridVehPurchaseApproving', {
	extend : 'Ext.grid.Panel',
	reference: 'gridVehPurchaseApproving',
	requires : [
		'Ext.grid.column.Action'
	],

	title : '车辆购置待审核列表',
    bind:{
    	store:'{vehiPurchaseApprovingResults}'
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
			handler: 'viewVehiPurchaseApproving'
        },
        refuse: {
            tooltip : '驳回',
			iconCls : 'x-fa fa-close',
            handler: 'refuseVehiPurchaseApproving'
        },
        audited: {
            tooltip : '审核通过',
            iconCls : 'x-fa fa-check',
            handler: 'vehiPurchaseApproved'
        }
    },
    columns:[
          {
            header: '申请表ID',
            dataIndex: 'id',
            hidden: true
          },{
            header: '申请单位',
            flex: 1,
            align:'center',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'deptName',
            renderer:function (value, metaData){  
            	value = window.sessionStorage.getItem("organizationName");
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            header: '组织机构代码',
            flex: 1,
            align:'center',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'institutionCode',
            renderer:function (value, metaData){  
            	value = window.sessionStorage.getItem("institutionCode");
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            },
            listeners: {
            	afterrender: function(field){
            		var isInstitution = window.sessionStorage.getItem("isInstitution");
            		if(isInstitution == "false"){
            			field.hide();
            		}
            	}
            }
        },{
            header: '单位性质',
            flex: 1,
            align:'center',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'institutionFeature',
            renderer:function (value, metaData){  
            	value = window.sessionStorage.getItem("institutionFeature");
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            },
            listeners: {
            	afterrender: function(field){
            		var isInstitution = window.sessionStorage.getItem("isInstitution");
            		if(isInstitution == "false"){
            			field.hide();
            		}
            	}
            }
        },{
            header: '级别',
            dataIndex: 'levelType',
            align:'center',
            sortable: false,
            menuDisabled: true,
            renderer: function(val,metaData) {
            	var val = window.sessionStorage.getItem("institutionLevel");
            	if(val == null){
            		return "";
            	}
            	val = parseInt(val);
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
                    case 6:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('科级') + '"'; 
                        return '科级';
                    case 7:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('副科级') + '"'; 
                        return '副科级';
                    case 4:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('县级') + '"'; 
                        return '县级';
                    case 5:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('乡科级') + '"'; 
                        return '乡科级';
                }
            },
            listeners: {
            	afterrender: function(field){
            		var isInstitution = window.sessionStorage.getItem("isInstitution");
            		if(isInstitution == "false"){
            			field.hide();
            		}
            	}
            }
          },{
            header: '申请数量',
            flex: 1,
            align:'center',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'applyNumber',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            header: '总金额',
            flex: 1,
            align:'center',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'sumAccount',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
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
        },{
			xtype : 'actioncolumn',
            flex: 1,
			cls : 'content-column',
			header : '操作',
            align: 'center',
            menuDisabled: true,
			items: ['@view', '@refuse', '@audited']
    	  }],
	dockedItems : [{
			id:'vehiPurchaseApprovingPage',
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
                store: '{vehiPurchaseApprovingResults}'
            },
            displayInfo: true
		}],
	initComponent : function() {
		this.callParent();
	}
});