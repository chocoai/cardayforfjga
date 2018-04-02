Ext.define('Admin.view.systemmgmt.smsconfig.smsConfigGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.systemmgmt.smsconfig.ViewModel'
    ],
    reference: 'smsconfigGrid',
    id:'smsconfigGrid',
    title: '短信提醒',
    bind:{
    	store:'{smsconfigResults}'
    },
   	viewConfig: {
        loadMask: true,
       	loadingText: '加载中...',
       	emptyText: '无记录！',
        deferEmptyText: false
    },
    stateful: true,
    collapsible: false,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    columns: {
    	defaults:{
				align:'center',
				flex:1,
				sortable: false,
				menuDisabled: true
		},
		items:[{
             	header: '提醒模块',
       		 	dataIndex: 'eventType',
            	flex:1,
            	renderer: function(val,metaData) {
	        		switch(val){
	        			case 'ALLOCATE':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('分配车辆') + '"'; 
	        				return '分配车辆';
	        		}
            	}
        	},{
	            header: '是否开启提醒',
                dataIndex: 'enable',
                renderer: function(val,metaData) {
                    switch(val){
                        case true:
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('是') + '"'; 
                            return "<span style='color:blue;'>是</span>";
                        case false:
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('否') + '"'; 
                            return "<span style='color:red;'>否</span>";
                    }
                }  
      		},
        	{
	            xtype: 'actioncolumn',
	            items: [{
	                    xtype: 'button',
                        tooltip : '设置',
                        iconCls : 'x-fa fa-check',
                        handler: 'modifysmsconfig'
	                },{
	                    xtype: 'button',
                        tooltip : '配置短信模板',
                        iconCls : 'x-fa fa-pencil-square-o',
                        handler: 'openConfigTemplate'
	                }],
	            cls: 'content-column',
                align:'center',
	            text: '操作'
        	}]
    },
});
