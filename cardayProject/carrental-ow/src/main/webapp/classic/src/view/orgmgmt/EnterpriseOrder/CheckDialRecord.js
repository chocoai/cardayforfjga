Ext.define('Admin.view.orgmgmt.dialrecordmgmt.CheckDialRecord', {
	extend: 'Ext.window.Window',	
    alias: 'widget.checkDialRecord',
    xtype:'checkDialRecord',
	title : '查看客户来电记录',
	width : 420,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	controller: {
        xclass: 'Admin.view.orgmgmt.dialrecordmgmt.ViewController'
    },
    layout: 'fit',
	items:[{
		xtype:'form',
		layout : 'vbox',
		layout: {
            type: 'vbox', 
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		defaultType:'displayfield',
		fieldDefaults: {
            labelAlign: 'left',
            labelWidth: 100
        },
		items:[{
			fieldLabel: '来电号码',
			name: 'dialPhone',
			 height:20
		}, {
			fieldLabel: '来电人姓名',
			 height:20,
	        name: 'dialName'
		},{
			fieldLabel: '来电时间',
	        name: 'dialTime',
	         height:20
		},{
			fieldLabel: '来电人单位',
	        name: 'dialOrganization',
	        height:20
		}, {
			fieldLabel: '来电类型',
			 height:20,
	        name: 'dialType'
		},{
			fieldLabel: '来电内容',
			 height:20,
	        name: 'dialContent'
		},{
			fieldLabel: '行程订单号',
			 height:20,
	        name: 'orderNo'
		},{
			fieldLabel: '车牌号',
			 height:20,
	        name: 'vehicleNumber'
		},{
			fieldLabel: '终端设备号',
			 height:20,
	        name: 'deviceNo'
		},{
			fieldLabel: '处理结果',
	        name: 'dealResult',
	         height:20,
		},{
			fieldLabel: '记录人',
	        name: 'recorder',
	         height:20,
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