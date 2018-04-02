Ext.define('Admin.view.orgmgmt.enterinfomgmt.CheckEnterInfo', {
	extend: 'Ext.window.Window',	
    alias: 'widget.checkEnterInfo',
    xtype:'checkEnterInfo',
	title : '查看企业信息',
	width : 420,
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	ghost:false,//是否显示背影
//	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	controller: {
        xclass: 'Admin.view.orgmgmt.enterinfomgmt.ViewController'
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
			fieldLabel: '公司名称',
			name: 'name',
			 height:20
		}, {
			fieldLabel: '公司简称',
			 height:20,
	        name: 'shortname'
		},  {
			fieldLabel: '联系人',
			 height:20,
	        name: 'linkman'
		},{
			fieldLabel: '联系人电话',
			 height:20,
	        name: 'linkmanPhone'
		},{
			fieldLabel: '联系人邮箱',
			 height:20,
	        name: 'linkmanEmail'
		},{
			fieldLabel: '计划用车数',
			 height:20,
	        name: 'vehileNum'
		},{
			fieldLabel: '用车城市',
			 height:20,
	        name: 'city'
		},{
			fieldLabel: '服务起始日期',
	        name: 'startTime',
	         height:20,
	        renderer: function (value, field) {
           		return Ext.util.Format.date(value,'Y-m-d');
        	}
		},{
			fieldLabel: '服务到期日期',
	        name: 'endTime',
	         height:20,
	        renderer: function (value, field) {
            	return Ext.util.Format.date(value,'Y-m-d');
	        }
		},{
			fieldLabel: '公司地址',
	        name: 'address',
	         height:20
		},{
			fieldLabel: '公司介绍',
	        name: 'introduction',
	        height:20
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