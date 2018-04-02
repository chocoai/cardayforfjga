Ext.define('Admin.view.orgmgmt.rentalcompanymgmt.CheckVehsResourcesInfo', {
	extend: 'Ext.window.Window',	
    alias: 'widget.checkVehsResourcesInfoRental',
    xtype:'checkVehsResourcesInfoRental',
	title : '查看车辆信息',

	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	controller: {
        xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.ViewController'
    },

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
				fieldLabel: '车辆类型',
		        name: 'vehicleType',
		        renderer: function (value, field) {
		        	switch(value){
		    			case '0':
		    				return '经济型';
		    			case '1':
		    				return '舒适型';
		    			case '2':
		    				return '商务型';
		    			case '3':
		    				return '豪华型';
	    			}
		        }
			},{
				fieldLabel: '品牌',
		        name: 'vehicleBrand'
			},{
				fieldLabel: '型号',
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
				fieldLabel: '颜色',
		        name: 'vehicleColor'
			},{
				fieldLabel: '座位数',
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
				fieldLabel: '所属部门',
		        name: 'arrangedOrgName',
			},{
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
				fieldLabel: '用途',
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
			}]	
	}],
	buttonAlign : 'center',
	buttons : [{
				id : 'button',
				text : '关闭',
				handler: function(btn){
					btn.up("window").close();
				}
			}]
});