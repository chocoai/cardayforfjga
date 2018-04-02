Ext.define('Admin.view.vehiclemgmt.vehiclemaintain.ViewVehicleMaintain', {
	extend: 'Ext.window.Window',	
   requires : ['Ext.layout.container.Table',
				'Ext.button.Button' ],
	reference: 'viewVehicleMaintain',
    bodyPadding: 10,
    ghost:false,
    modal: true,
    resizable: false,// 窗口大小是否可以改变
	title: '查看车辆维修信息',
	width : 300,
	
	items:[{
		xtype:'form',
		margin:'0 20 0 20',
		layout : 'vbox',
		defaultType: 'displayfield',
		fieldDefaults: {
            labelWidth: 100,
        },
		items:[{
				fieldLabel:'车牌号',
				name: 'vehicleNumber'
			},{
				fieldLabel : '维修项目',
				name : 'maintenanceItem',
			},{
				fieldLabel: '数量',
		        name: 'amount',
		        renderer: function (value, field) {
		    		return value;
		        }
			},{
				fieldLabel: '单价',
		        name: 'unitPrice',
		        renderer: function (value, field) {
		    		return value + '元';
		        }
			},{
				fieldLabel: '费用',
		        name: 'totalAccount',
		        renderer: function (value, field) {
		    		return value + '元';
		        }
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