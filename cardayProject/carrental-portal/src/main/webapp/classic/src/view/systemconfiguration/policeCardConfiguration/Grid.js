Ext.define('Admin.view.systemconfiguration.policeCardConfiguration.Grid', {
	extend : 'Ext.grid.Panel',
	reference: 'gridPoliceCardConfiguration',
	requires : [
		'Ext.grid.column.Action'
	],
	id: 'gridPoliceCardConfiguration',
	title : '警牌审批设置列表',
    bind:{
    	store:'{policeCardResults}'
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
			handler: 'viewPoliceCard'
        },
        modify: {
            tooltip : '修改',
			iconCls : 'x-fa fa-edit',
            handler: 'updatePoliceCard'
        },
        delete: {
            tooltip : '删除',
            iconCls : 'x-fa fa-close',
            handler: 'deletePoliceCard'
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
            header: '模式ID',
            dataIndex: 'id',
            hidden: true
    	  },{
            header: '模式名称',
            dataIndex: 'patternName',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
            },{
            header: '审批级数',
            dataIndex: 'approvalRating',
            renderer: function(val,metaData) {
        		switch(val){
        			case '0':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('一级') + '"'; 
	        				return '一级';
	        			case '1':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('二级') + '"'; 
	        				return '二级';
	        			case '2':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('三级') + '"'; 
	        				return '三级';
	        			case '3':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('四级') + '"'; 
	        				return '四级';
                        case '4':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('五级') + '"'; 
                            return '五级';
        		}
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
			id:'policeCardPage',
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
                store: '{policeCardResults}'
            },
            displayInfo: true
		}],
	initComponent : function() {
		this.callParent();
	}
});