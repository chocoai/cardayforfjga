Ext.define('Admin.view.alertMgmt.overSpeedAlarm.OverSpeedVehicleGrid', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.column.Action'
	],
	title : '报警事件列表',
	reference: 'overSpeedVehicleGrid',
    id:'overSpeedVehicleGrid',
    bind:{
    	store:'{OverSpeedResults}'
    },
	viewConfig: {
        loadMask: true,
       	loadingText: '加载中...',
       	emptyText: '无记录！',
        deferEmptyText: false
    },
  //  cls : 'user-grid',  //图片用户样式
	stateful: true,
    collapsible: false,
	multiSelect: false,
	forceFit: false,
    mask: true,
   	columnLines: true,  //表格线	
    actions: {
        view: {
            tooltip : '查看位置',
			iconCls : 'x-fa fa-map-pin',
			handler: 'viewVehiclePosition'
        },
        checkVehicle:{
            tooltip:'查看车辆信息',
            iconCls: 'x-fa fa-car',
            handler: 'viewVehicle'
        }
    },
	columns:{
		defaults:{
			align:'center',
			sortable: false,
			menuDisabled: true,
			flex:1
			},
		items:[{
                header: '报警编号',
                dataIndex: 'id',
                renderer:function (value, metaData){
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                    return value;
                },
		  },{
            header: '车牌号',
            dataIndex: 'vehicleNumber',
            flex:1,
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
        		}
            }
    	  },{
            header: '异常类型',
            dataIndex: 'alertType',
            renderer: function(val) {
            	return '超速';
            },
            flex:1
    	  },{
            header: '报警时间',
            dataIndex: 'alertTime',
            //formatter: 'date("Y-m-d H:i:s")',
            flex:2,
            renderer:function (value, metaData){
                value = Ext.util.Format.date(value,'Y-m-d H:i:s');
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            },    
    	  },{
	        header: '实际速度',
	        dataIndex: 'alertSpeed',
	        renderer: function(value) {
            	return value + 'km/h';
            },
	        flex:1
   		  },{
	        header: '超速比例',
	        dataIndex: 'overspeedPercent',
	        flex:1,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
   		  },{
            header: '超速位置',
            dataIndex: 'alertPosition',
            flex:1,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	  },{
            header: '车辆来源',
            dataIndex: 'vehicleSource',
            flex:1,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	  },{
            header: '部门',
            dataIndex: 'currentuseOrgName',
            renderer: function(val,metaData) {
            	if(val==''||val==null){
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(val) + '"';  
	            		return '未分配';
	            	}/*else if(val==window.sessionStorage.getItem('organizationName')){
	            		return '未分配';
	            	}*/else{
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(val) + '"';  
	            		return val;
	            	}
            },
            flex:1
    	  },{
            header: '司机姓名',
            dataIndex: 'driverName',
            flex:1,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	  },{
            header: '司机电话',
            dataIndex: 'driverPhone',
            flex:1,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	  },{
            header : '操作',
			xtype : 'actioncolumn',
			cls : 'content-column',
			width: 120,			
			items: ['@view','@checkVehicle',/*{
                getClass: function(v, meta, record) {
                    var marginValue = this.width/2-20;                        
                    this.setMargin('0 -'+marginValue+' 0 '+marginValue);
                }
            }*/],
//            align: 'left',
            align: 'center',
    	  }]
      },
	dockedItems : [{
            xtype: 'pagingtoolbar',
            id:'overSpeedAlarmPage',
            pageSize:10,
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
                store: '{OverSpeedResults}'
            },
            displayInfo: true,
		}],
	initComponent : function() {
		this.callParent();
	}
});