Ext.define('Admin.view.alertMgmt.violateAlarm.ViewVehicleWindow', {
	extend: 'Ext.window.Window',	
   requires : ['Ext.layout.container.Table',
				'Ext.button.Button' ],
	reference: 'viewVehicle',
	alias: "widget.viewVehicleWindow",
    bodyPadding: 10,
    closable:false,//窗口是否可以改变
    ghost:false,
    modal: true,
    scrollable:false,
    resizable: false,// 窗口大小是否可以改变
    width:600,
	title: '查看车辆信息',
	items:[{
		xtype:'form',
		layout: {
	        type: 'table',
	        columns: 2
    	},
		defaultType:'displayfield',
		fieldDefaults: {
		     margin: '0 10 0 30'
		},
		items:[{
				fieldLabel:'车牌号',
				name: 'vehicleNumber'
			},{
				fieldLabel: '公车性质',
		        name: 'vehicleType',
		        renderer: function (value, field) {
		        	switch(value){
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
			},	{
	            name:'city',
	            fieldLabel: '所属城市',
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
			},{
				fieldLabel: '保险到期日',
		        name: 'insuranceExpiredate',
		        renderer: function (value, field) {
		        	return  Ext.util.Format.date(new Date(value),'Y-m-d');
           			//return Ext.util.Format.date(value,'Y-m-d');
        		}
			},{
				fieldLabel: '购买时间',
		        name: 'vehicleBuyTime',
		        renderer: function (value, field) {
           			return  Ext.util.Format.date(new Date(value),'Y-m-d');
        		}
			},{
				fieldLabel: '车辆用途',
		        name: 'vehiclePurpose'
			},{
				fieldLabel: '车位信息',
		        name: 'parkingSpaceInfo'
			},{
				fieldLabel: 'SIM卡',
		        name: 'simNumber'
			},{
				fieldLabel: '设备号',
		        name: 'deviceNumber'
			},{
				fieldLabel: '限速',
		        name: 'limitSpeed',
		        renderer: function (value, field) {
		            return  value + 'KM/H';
        		}
			}]	
	}],
	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up("window").close();
				}
			}]
});