Ext.define('Admin.view.alertMgmt.alarmConfig.AlarmConfigGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.alertMgmt.alarmConfig.ViewModel'
    ],
    reference: 'alarmConfigGrid',
    id:'alarmConfigGrid',
    title: '报警配置',
    bind:{
    	store:'{alarmConfigResults}'
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
             	header: '报警类型',
       		 	dataIndex: 'eventType',
            	flex:1,
            	renderer: function(val,metaData) {
	        		switch(val){
	        			case 'OVERSPEED':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('超速报警') + '"'; 
	        				return '超速报警';
	        			case 'OUTBOUND':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('越界报警') + '"'; 
	        				return '越界报警';
	        			case 'VEHICLEBACK':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('回车报警') + '"'; 
	        				return '回车报警';
	        			case 'VIOLATE':
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('违规用车报警') + '"'; 
	        				return '违规用车报警';
	        		}
            	}
        	},{
	            header: '是否开启报警',
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
                        handler: 'modifyAlarmConfig'
	                }],
	            cls: 'content-column',
                align:'center',
	            text: '操作'//,
        }]
    },
});
