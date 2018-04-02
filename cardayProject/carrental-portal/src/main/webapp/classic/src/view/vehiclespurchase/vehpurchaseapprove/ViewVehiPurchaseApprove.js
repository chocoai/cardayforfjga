Ext.define('Admin.view.vehiclespurchase.vehipurchaseapply.ViewVehiPurchaseApprove', {
	extend: 'Ext.window.Window',
	
    alias: "widget.viewVehiPurchaseApprove",
	reference: 'viewVehiPurchaseApprove',

	controller: {
        xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.ViewController'
    },

    viewModel: {
        xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.ViewModel'
    },

	title : '车辆购置申请单查看',
    width : 1048,
    maxHeight: 600,
    scrollable: true,
	closable: true,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作

    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },

    bodyPadding: 20,
    listeners:{
        afterrender: 'onBeforeLoadView',
    },

    items: [
        {
            xtype:'form',
            margin:'0 10 0 0',
            layout : 'hbox',
            defaultType: 'displayfield', 
            items: [
                {
                    labelWidth: 70,
                    fieldLabel: '申请单位',
                    name: 'deptName',
                    margin:'0 30 0 0',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
//                        return value;  
                        return window.sessionStorage.getItem("organizationName"); 
                    }
                },
                {
                    labelWidth: 90,
                    fieldLabel: '组织机构代码',
                    name: 'institutionCode',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
//                        return value;  
                        return window.sessionStorage.getItem("institutionCode"); 
                    },
                    listeners: {
                    	afterrender: function(field){
                    		var isInstitution = window.sessionStorage.getItem("isInstitution");
                    		if(isInstitution == "false"){
                    			field.hide();
                    		}
                    	}
                    }
                },        
            ]
        },
        {
            xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.GridVehiPurchaseType',
            frame: true
        },
        {
        	margin:'20 0 0 0',
            xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.GridVehiPurchaseInfo',
            frame: true
        }
    ],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up('viewVehiPurchaseApprove').close();
				}
			}
	]
});