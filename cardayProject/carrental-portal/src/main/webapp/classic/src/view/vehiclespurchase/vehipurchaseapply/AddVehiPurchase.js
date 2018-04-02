Ext.define('Admin.view.vehiclespurchase.vehipurchaseapply.AddVehiPurchase', {
	extend: 'Ext.window.Window',
	
    alias: "widget.addVehiPurchase",
	reference: 'addVehiPurchase',

	controller: {
        xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.ViewController'
    },

    viewModel: {
        xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.ViewModel'
    },

	title : '新增申请单',
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
        afterrender: 'onBeforeLoadAdd',
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
                    value:'福州市鼓楼区公安局',
                    renderer:function (value, metaData){  
                        return window.sessionStorage.getItem("organizationName"); 
                    }
                }, 
                {
                    labelWidth: 90,
                    fieldLabel: '组织机构代码',
                    name: 'institutionCode',
                    value:'003610082',
                    renderer:function (value, metaData){  
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
            xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.GridVehiPurchaseTypeAdd',
            frame: true
        },{
            xtype:'container',
            margin:'10 0 0 0',
            items:[{
                xtype:'button',
                text: "新增申请车辆",
                handler:'addVehicle'
            }]
        },
        {
        	margin:'10 0 0 0',
            xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.GridVehiPurchaseInfoAdd',
            frame: true
        },
        {
            margin:'10 0 0 0',
            xtype:'form',
            layout: {
                type: 'hbox', 
                align: 'stretch'
            },
            items: [
                    {
                        margin:'0 15 0 0',
                        width:200,
                        anchor: '100%',
                        xtype: 'filefield',
                        name: 'authorizedAttach',
                        emptyText:'请上传编制文件',
                        buttonText: '编制文件',
                    },{
                        margin:'0 15 0 0',
                        width:220,
                        anchor: '100%',
                        xtype: 'filefield',
                        name: 'capitalSourceAttach',
                        emptyText:'请上传资金来源表',
                        buttonText: '资金来源表',
                    },{
                        margin:'0 15 0 0',
                        width:200,
                        anchor: '100%',
                        xtype: 'filefield',
                        name: 'applicationAttach',
                        emptyText:'请上传申请报告',
                        buttonText: '申请报告',
                    },{
                        width:200,
                        anchor: '100%',
                        xtype: 'filefield',
                        name: 'otherAttach',
                        emptyText:'请上传其他文件',
                        buttonText: '其他',
                    }]
        }
    ],

	buttonAlign : 'center',
	buttons : [{
			text : '确定',
			handler: function(btn){
				Ext.Msg.alert('消息提示', '新增成功！');
				btn.up('window').close();
			}
		},{
				text : '关闭',
				handler: function(btn){
					btn.up('addVehiPurchase').close();
				}
			}
	]
});