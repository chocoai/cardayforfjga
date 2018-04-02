Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.VehicleInfoView', {
	extend: 'Ext.window.Window',
	
    alias: "widget.VehicleInfoView",
    controller: 'realtimeMonitoringcontroller',
	reference: 'VehicleInfoView',
	id: 'VehicleInfoView',
	title : '',
	width : 680,
	//height : 550,
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	frame : true,
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    listeners:{
//    	afterrender: 'loadTripTraceWindowInfo',
//    	afterrender: function() {alert(this.organizationId)}
    },
    bodyPadding: 1,
 /*   defaults: {  //对items的组件生效
        frame: true,
    },*/
    items:[{
		xtype:'form',
//		width: 500,
		layout: {
	        type: 'table',
	        columns: 2
    	},
		defaultType:'displayfield',
		fieldDefaults: {
		     margin: '0 10 0 30'
		//	 labelWidth: 80,
		//	 width:200
			
		},
		items:[{
				fieldLabel:'车牌号',
				name: 'vehicleNumber'
			},{
				fieldLabel: '公车性质',
		        name: 'vehicleType',
		        renderer: function(val) {
	        		switch(val){
	        			case '0':
	        				return '应急机要通信接待用车';
	        			case '1':
	        				return '行政执法用车';
	        			case '2':
	        				return '行政执法特种专业用车';
	        			case '3':
	        				return '一般执法执勤用车';
	        			case '4':
	        				return '执法执勤特种专业用车';
	        		}
	            }
			},{
				fieldLabel: '车辆品牌',
		        name: 'vehicleBrand'
			},{
				fieldLabel: '车辆型号',
		        name: 'vehicleModel'
			},{
				fieldLabel: '车架号',
		        name: 'vehicleIdentification'
			},{
				fieldLabel: '理论油耗',
		        name: 'theoreticalFuelCon',
		        renderer: function (value, field) {
		            return  value + 'L/百公里';
        		}
			},{
				fieldLabel: '车辆颜色',
		        name: 'vehicleColor'
			},{
				fieldLabel: '车辆座位数',
		        name: 'seatNumber'
			},{
				fieldLabel: '排量',
		        name: 'vehicleOutput'
			},{
				fieldLabel: '燃油号',
		        name: 'vehicleFuel'
			},{
				fieldLabel: '车辆来源',
		        name: 'vehicleFromName'
			},{
				fieldLabel: '车辆所属城市',
		        name: 'city',
		        renderer: function (value, field) {
	       			switch(value){
		    			case '0':
		    				return '北京';
		    			case '1':
		    				return '上海';
		    			case '2':
		    				return '武汉';
		    			case '3':
		    				return '重庆';
	    			}
        		}
			},{
				fieldLabel: '车辆所属部门',
		        name: 'arrangedOrgName',
		        colspan: 2
			},/*{
				fieldLabel: '准驾类型',
		        name: 'licenseType'
			},*/{
				fieldLabel: '购买时间',
		        name: 'vehicleBuyTime',
		        renderer: function (value, field) {
           			return Ext.util.Format.date(value,'Y-m-d');
        		}
			},{
				fieldLabel: '保险到期日',
		        name: 'insuranceExpiredate',
		        renderer: function (value, field) {
           			return Ext.util.Format.date(value,'Y-m-d');
        		}
			},{
				fieldLabel: '车位信息',
		        name: 'parkingSpaceInfo'
			},{
				fieldLabel: '车辆用途',
		        name: 'vehiclePurpose'
			},{
				fieldLabel: '设备号',
		        name: 'deviceNumber'
			},{
				fieldLabel: 'SIM卡',
		        name: 'simNumber'
			},{
				fieldLabel: '限速',
		        name: 'limitSpeed',
		        renderer: function (value, field) {
		            return  value + 'KM/H';
        		}
			},{
				fieldLabel: '司机姓名',
		        name: 'realname',
	        	renderer: function (value, field) {
		            if (value == null || value == '') {
		            	return value = '--';
		            } else {
		            	return value;
		            }
        		}
			},{
				fieldLabel: '司机手机号码',
		        name: 'phone',
		        renderer: function (value, field) {
		            if (value == null || value == '') {
		            	return value = '--';
		            } else {
		            	return value;
		            }
        		}
			}]	
	}],
    initComponent: function() {
        this.callParent();
    }
});