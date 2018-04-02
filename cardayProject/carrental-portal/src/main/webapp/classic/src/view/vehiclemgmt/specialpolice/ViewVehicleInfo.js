Ext.define('Admin.view.vehiclemgmt.specialpolice.ViewVehicleInfo', {
	extend: 'Ext.window.Window',	
   requires : ['Ext.layout.container.Table',
				'Ext.button.Button' ],
	reference: 'viewVehicleInfo',
    bodyPadding: 10,
    ghost:false,
    modal: true,
    resizable: false,// 窗口大小是否可以改变
	title: '查看车辆信息',
	width : 620,
	maxHeight : 590,
	autoScroll:true,
	
	controller: {
       xclass: 'Admin.view.vehiclemgmt.specialpolice.ViewController'
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
			},	
         	{
		        name: 'arrangedEntName',
		        fieldLabel: '分配企业',
		        listeners:{
			       afterrender: function(){
		        		var userType = window.sessionStorage.getItem('userType');
		        		 if(userType == '3'||userType == '2'){
				                this.hidden=true;
		                 }
    				}
		        }
	      	},
	      	{
	            name:'provinceName',
	            fieldLabel: '所属省',
	            
         	},
	      	
	      	{
		        name: 'arrangedOrgName',
		        fieldLabel: '所属部门'
	      	},
	      	{
	            name:'cityName',
	            fieldLabel: '所属市',
	            
         	},
         	{
				fieldLabel: '保险到期日',
		        name: 'insuranceExpiredate',
		        renderer: function (value, field) {
           			return Ext.util.Format.date(value,'Y-m-d');
        		}
			},{
				fieldLabel: '购买时间',
		        name: 'vehicleBuyTime',
		        renderer: function (value, field) {
           			return Ext.util.Format.date(value,'Y-m-d');
        		}
			},{
				fieldLabel: '年检到期日',
		        name: 'inspectionExpiredate',
		        renderer: function (value, field) {
           			return Ext.util.Format.date(value,'Y-m-d');
        		}
			},{
				fieldLabel: '所属站点',
		        //name: 'parkingSpaceInfo'
				name: 'stationName'
			},{
				fieldLabel: 'SIM卡号',
		        name: 'simNumber'
			},{
				fieldLabel: '设备IMEI号',
		        name: 'deviceNumber'
			},{
				fieldLabel: 'SN设备号',
		        name: 'snNumber'
			},{
				fieldLabel: 'ICCID编号',
		        name: 'iccidNumber'
			},
			{
				fieldLabel: '设备供应商编号',
		        name: 'deviceVendorNumber'
			},
			{
				fieldLabel: '限速',
		        name: 'limitSpeed',
		        renderer: function (value, field) {
		            return  value + 'KM/H';
        		}
			},{
				fieldLabel: '分配司机姓名',
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
			},
			{
				fieldLabel: '车辆状态',
		        name: 'vehStatus',
		        renderer: function (value, field) {	            
	                switch(value){
	                    case 0:
	                        return '已出车';
	                    case 1:
	                        return '待维修';
	                    case 2:
	                        return '维修';
	                    case 3: 
	                        return '保养';
	                    case 4:
	                        return '年检';
	                    case 5:
	                        return '备勤';
	                    case 6: 
	                        return '机动';
	                    case 7:
	                        return '专用';
	                    case 8: 
	                        return '不调度';
	                    case 9:
	                        return '计划锁定';
	                    case 10:
	                        return '封存';
	                    case 11:
	                        return '报废';
	                }
        		}
			},
			{
				fieldLabel: '登记编号',
		        name: 'registrationNumber'
			},
			{
				fieldLabel: '编制文号',
		        name: 'authorizedNumber'
			},
			{
				fieldLabel: '变动原因',
		        name: 'reasonOfChanging'
			},
			{
				fieldLabel: 'M2M状态',
		        name: 'enableTrafficPkg',
		        renderer: function (value, field) {	            
	                switch(value){
	                    case 0:
	                        return '流量关闭';
	                    case 1:
	                        return '有效开启';
	                }
        		}
			},
			{
				fieldLabel: '涉密状态',
		        name: 'enableSecret',
		        renderer: function (value, field) {	            
	                switch(value){
	                    case 0:
	                        return '未涉密';
	                    case 1:
	                        return '已涉密';
	                }
        		}
			},
			]	
	}],
	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up("window").close();
				}
			}]
});