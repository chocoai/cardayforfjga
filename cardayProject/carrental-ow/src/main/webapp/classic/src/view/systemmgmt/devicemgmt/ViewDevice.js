Ext.define('Admin.view.systemmgmt.devicemgmt.ViewDevice', {
	extend: 'Ext.window.Window',
	
    alias: "widget.viewdevice",
    controller: 'devicemgmtcontroller',
	reference: 'viewdevice',
	title : '查看终端设备信息',
	width : 400,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	//bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	items : [{
		xtype:'form',
		layout : 'vbox',
		defaultType: 'displayfield',
		
		bodyStyle: {
			//  background: '#ffc',
			    padding: '15px'  //与边界的距离
		},
		
		fieldDefaults: {
            labelAlign: 'left',
            labelWidth: 100,
            height:20,    
        },
        
		items: [
			{
				fieldLabel: 'SN设备号',
			    name: 'snNumber'
			},
			{
				fieldLabel: '设备IMEI号',
			    name: 'imeiNumber'
			},
			{
				fieldLabel: '设备类型',
			    name: 'deviceType'
			},
			{
				fieldLabel: '设备型号',
			    name: 'deviceModel'
			},
			{
				fieldLabel: '设备供应商编号',
			    name: 'deviceVendorNumber',
			}, 
			{
				fieldLabel: '设备厂家',
			    name: 'deviceVendor',
			}, 
			{
				fieldLabel: '固件版本',
			    name: 'firmwareVersion',
			},
			{
	    		fieldLabel: '软件版本',
	            name: 'softwareVersion',
	    	},
			{
				fieldLabel: '采购时间',
			    name: 'purchaseTime'
			},
			{
				fieldLabel: '保修到期日',
			    name: 'maintainExpireTime'
			},
			{
				fieldLabel: 'SIM卡号',
			    name: 'simNumber',
			},
			{
				fieldLabel: 'ICCID编号',
			    name: 'iccidNumber',
			},
			{
				fieldLabel: '绑定车牌号',
			    name: 'vehicleNumber',
			},
			{
				fieldLabel: '车辆来源',
			    name: 'vehicleSource',
			},
			{
				fieldLabel: '车辆VIN号',
			    name: 'vehicleIdentification',
			},
			{
				fieldLabel: '车辆限速',
		        xtype: 'displayfield',
		        name: 'limitSpeed',
	    	},
	    	{
				fieldLabel: '下发执行状态',
				xtype: 'displayfield',
		        name: 'commandStatus',
		        renderer : function (value) {
		        	if (value == "excuting") {
		        		return value="下发限速执行中";
		        	} else if (value == "success") {
		        		return value="下发限速成功";
		        	} else if (value == "failure") {
		        		return value="下发限速失败";
		        	} else {
		        		return value="";
		        	}
		        },
	    	},
	    	{
				fieldLabel: '最新下发限速',
		        xtype: 'displayfield',
		        name: 'latestLimitSpeed',
	    	},
			{
				fieldLabel: '设备状态',
				name: 'deviceStatus',
				renderer : function(value) {
	            	switch (value) {
	            	case 1:
	            		value='正常';
	            		break;
	            	case 2:
	            		value='未配置';
	            		break;
	            	case 3:
	            		value='故障';
	            		break;
	            	}
	            	return value;
				}
			},
			{
				fieldLabel: '设备批次',
		        name: 'deviceBatch',
	    	},
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up("viewdevice").close();
				}
			}]
});