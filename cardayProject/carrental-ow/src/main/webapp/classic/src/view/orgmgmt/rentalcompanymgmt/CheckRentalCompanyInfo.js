Ext.define('Admin.view.orgmgmt.rentalcompanymgmt.CheckRentalCompanyInfo', {
	extend: 'Ext.window.Window',	
    alias: 'widget.checkRentalCompanyInfo',
    xtype:'checkRentalCompanyInfo',
	title : '查看企业信息',
	width : 420,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	controller: {
        xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.ViewController'
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
			fieldLabel: '企业名称',
			name: 'name',
			 height:20
		}, {
			fieldLabel: '企业简称',
			 height:20,
	        name: 'shortname'
		},{
			fieldLabel: '企业地址',
	        name: 'address',
	         height:20
		},{
			fieldLabel: '企业介绍',
	        name: 'introduction',
	        height:20
		}, {
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
		},/*{
			fieldLabel: '计划用车数',
			 height:20,
	        name: 'vehileNum'
		},{
			fieldLabel: '用车城市',
			 height:20,
	        name: 'city'
		},*/{
			fieldLabel: '服务开始日期',
	        name: 'startTime',
	         height:20,
	        renderer: function (value, field) {
           		return Ext.util.Format.date(value,'Y-m-d');
        	}
		},{
			fieldLabel: '服务结束日期',
	        name: 'endTime',
	         height:20,
	        renderer: function (value, field) {
            	return Ext.util.Format.date(value,'Y-m-d');
	        }
		},{
			xtype: 'checkboxgroup',
			id:'businessTypeCheckGroupRental',
	        fieldLabel: '业务类型',
	        columns: 2,
	        vertical: true,
	        items: [
	            { boxLabel: '自有车',inputValue: '0', readOnly: true },
	            { boxLabel: '长租车',inputValue: '1', readOnly: true },
	        ],
	        name: 'businessType',
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