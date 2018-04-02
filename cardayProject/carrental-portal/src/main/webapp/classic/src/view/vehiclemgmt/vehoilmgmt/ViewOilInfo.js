Ext.define('Admin.view.vehiclemgmt.vehoilmgmt.ViewOilInfo', {
	extend: 'Ext.window.Window',	
   requires : ['Ext.layout.container.Table',
				'Ext.button.Button' ],
	reference: 'viewOilInfo',
    bodyPadding: 10,
    ghost:false,
    modal: true,
    resizable: false,// 窗口大小是否可以改变
	title: '查看加油信息',
	width : 700,
	maxHeight : 590,
	autoScroll:true,
	
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
				fieldLabel : '车辆属性',
				name : 'vehicleAttribute',
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
				fieldLabel : '品牌',
				name : 'vehicleBrand',
			},{
				fieldLabel : '车型',
				name : 'vehicleModel',
			},{
				fieldLabel : '司机',
				name : 'driver',
			},{
				fieldLabel : '加油日期',
				name : 'oilTime',
			},{
				fieldLabel: '油科类别',
		        name: 'oilType',
		        renderer: function (value, field) {
		        	switch(value){
		    			case 0:
		    				return '90号汽油';
		    			case 1:
		    				return '93号汽油';
		    			case 2:
		    				return '97号汽油';
		    			case 3:
		    				return '0号柴油';
	    			}
		        }
			},{
				fieldLabel: '单价',
		        name: 'unitPrice',
		        renderer: function (value, field) {
		    		return value + '元/升';
		        }
			},{
				fieldLabel: '数量',
		        name: 'amount',
		        renderer: function (value, field) {
		    		return value + '升';
		        }
			},{
				fieldLabel: '总金额',
		        name: 'totalAccount',
		        renderer: function (value, field) {
		    		return value + '元';
		        }
			},{
				fieldLabel : '卡号',
				name : 'cardNumber',
			},{
				fieldLabel : '上次加油里程',
				name : 'lastMileage',
			},{
				fieldLabel : '本次加油里程',
				name : 'mileage',
			},{
				fieldLabel : '行驶里程',
				name : 'driveMileage',
			},{
				fieldLabel : '百公里油耗',
				name : 'theoreticalFuelCon',
			},{
				fieldLabel : '加油点',
				name : 'oilAddress',
			},{
				fieldLabel : '备注',
				name : 'remark',
			},{
				fieldLabel : '所属部门',
				name : 'arrangedOrgName',
			}
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