Ext.define('Admin.view.alertMgmt.outMarkerAlarm.OutMarkerVehicleGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.alertMgmt.outMarkerAlarm.ViewModel'
    ],
    reference: 'outMarkerVehicleGrid',
    id:'outMarkerVehicleGrid',
    title: '报警事件列表',
  //  cls : 'user-grid',  //图片用户样式
    bind:{
    	store:'{outMarkerResults}'
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
			    header: '报警编号',
	            dataIndex: 'id',
                renderer:function (value, metaData){  
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                    return value;  
                }
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
            	flex:1,
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
	            header: '异常类型',
	            dataIndex: 'alertType',
	            renderer: function(val) {
            		return '越界';
           	 	}
        	},{
	            header: '首次发生时间',
	            dataIndex: 'firstOutboundtime',
	            flex:2,
                //formatter: 'date("Y-m-d H:i:s")',   
                renderer:function (value, metaData){
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                    return value;
                },                
      		},{
	            header: '解除时间',
	            dataIndex: 'outboundReleasetime',
	            flex:2,
	            //formatter: 'date("Y-m-d H:i:s")',
                renderer:function (value, metaData){
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                    return value;
                },    
        	},{
	            header: '越界时长(分钟)',
	            dataIndex: 'outboundMinutes',
                renderer:function (value, metaData){  
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                    return value;  
                }
        	},{
	            header: '越界里程(KM)',
	            dataIndex: 'outboundKilos',
	            renderer:function(val,metaData){
                    val = Ext.util.Format.number(val, '0.000');
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(val) + '"';  
	            	return val;
	            }
        	},{
	            header: '车辆来源',
	            dataIndex: 'vehicleSource',
	            renderer:function (value, metaData){  
                	metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                	return value;  
            	}
        	},{
	            header: '部门',
	            dataIndex: 'currentuseOrgName',
				renderer: function(value,metaData) {
	            	if(value==''||value==null){
	            		return '未分配';
	            	}/*else if(val==window.sessionStorage.getItem('organizationName')){
	            		return '未分配';
	            	}*/else{
	            		metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value; 
	            	}
            	}
        	},{
	            header: '司机姓名',	             
	            dataIndex: 'driverName',
                renderer:function (value, metaData){  
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                    return value;  
                }
        	},{
	            header: '司机电话',
	            dataIndex: 'driverPhone',
	            renderer:function (value, metaData){  
                	metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                	return value;  
            	}
        	},{
	            xtype: 'actioncolumn',
	            items: [{
	                    xtype: 'button',
	                    tooltip:'查看轨迹',
	                    iconCls: 'x-fa fa-map-marker',
	                  //  handler: 'checkVehicleTrice'
	                   handler: 'viewOutMarker'
	                },{
	                    xtype: 'button',
	                    tooltip:'查看车辆信息',
	                    iconCls: 'x-fa fa-car',
	                    handler: 'viewVehicle'
	                },/*{
                        getClass: function(v, meta, record) {
                            var marginValue = this.width/2-20;                        
                            this.setMargin('0 -'+marginValue+' 0 '+marginValue);
                        }
                    }*/],
	            cls: 'content-column',
	            width:150,
//                align: 'left',
                align: 'center',
	            text: '操作'//,
	            //tooltip: 'edit'
        }]
    },
    dockedItems : [{
            id:'outMarkerPage',
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
                store: '{outMarkerResults}'
            },
            displayInfo: true
        }]
});
