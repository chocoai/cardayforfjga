Ext.define('Admin.view.orgmgmt.holidaymgmt.ViewHoliday', {
	extend: 'Ext.window.Window',	
    alias: 'widget.viewHoliday',
    xtype:'viewHoliday',
	title : '查看节假日详情',
	width : 420,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	controller: {
        xclass: 'Admin.view.orgmgmt.holidaymgmt.ViewController'
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
			fieldLabel: '年份',
			name: 'holidayYear',
			 height:20
		}, {
			fieldLabel: '节假日',
			 height:20,
	        name: 'holidayType'
		},{
			fieldLabel: '休息日',
	        name: 'holidayTime',
	         height:20
		},{
			fieldLabel: '调休日',
	        name: 'adjustHolidayTime',
	        height:20
		}]	
	}],
	buttonAlign : 'center',
	buttons : [{
				id : 'button',
				text : '关闭',
				handler: function(btn){
					btn.up("viewHoliday").close();
				}
			}]
});