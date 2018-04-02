Ext.define('Admin.view.orgmgmt.personmgmt.PersonView', {
	extend: 'Ext.window.Window',
	xtype: 'personSearchView',
    alias: "widget.personView",
    controller: 'personmgmtcontroller',
	//id : 'roleWin',
	reference: 'personView',
	title : '添加人员信息',
	width : 600,
//	height : 550,
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	layout : 'form',
//	labelAlign : 'center',
//	lableWidth : 80,
	frame : true,
	items:[{
		xtype:'form',
		layout : 'form',		
		bodyStyle : "background-color:#FFF0F5",
	items : [
	{
		fieldLabel: '姓名',
		xtype: 'textfield',
		name: 'username',
        labelWidth: 30,
        width: 40, 
	}, {
        xtype: "radiogroup",
        columns: 3,
        items: [
            { boxLabel: "男", name: "sex", inputValue: "male" },
            { boxLabel: "女", name: "sex", inputValue: "female" }
        ]
    }, {
		fieldLabel: '电话',
        xtype: 'textfield',
        name: 'phone_number',
        labelWidth: 60,
        width: 60,
	}, {
		fieldLabel: '密码',
        xtype: 'textfield',
        inputType: 'password',
        name: 'pwd',
        labelWidth: 80,
        width: 100,
	}, {
		fieldLabel: '邮箱',
        xtype: 'textfield',
        name: 'Email',
        labelWidth: 80,
        width: 100,
	}, {
		xtype: "radiogroup",
		fieldLabel: "企业自有车限额",
        columns: 3,
        items: [
            { boxLabel: "不限", name: "vehcle_limit", inputValue: "non_limit",
            	listeners : {
					'change' : function(checkbox, checked) {
						if (checked) {// 只有在点击时触发
//							console.log('not click month_limit');
							Ext.getCmp("month_limit_mileage").setReadOnly(true);
							Ext.getCmp("month_limit_mileage").setDisabled(true);
						}
					}
				}
            },
             {
				boxLabel : "月累计限额",
				name : "vehcle_limit",
				inputValue : "month_limit",
				listeners : {
					'change' : function(checkbox, checked) {
						if (checked) {// 只有在点击时触发
//							console.log('click month_limit');
							Ext.getCmp("month_limit_mileage").setReadOnly(false);
							Ext.getCmp("month_limit_mileage").setDisabled(false);
						}
					}
				}
			}
        ]
	}, {
		fieldLabel: '企业自有车月累计限额',
        xtype: 'textfield',
        name: 'month_limit_mileage',
        id: 'month_limit_mileage',
        emptyText: 'Km',
        readOnly: true,
        disabled: true,
        labelWidth: 80,
        width: 100,
	}, {
		xtype: "radiogroup",
		fieldLabel: "约车限额",
        columns: 3,
        items: [
            { boxLabel: "不限", name: "call_limit", inputValue: "non_limit",
            	listeners : {
					'change' : function(checkbox, checked) {
						if (checked) {// 只有在点击时触发
//							console.log('click month_limit');
							Ext.getCmp("month_limit_amount").setReadOnly(true);
							Ext.getCmp("month_limit_amount").setDisabled(true);
						}
					}
				}
            },
            { boxLabel: "月累计限额", name: "call_limit", inputValue: "month_limit",
            	listeners : {
					'change' : function(checkbox, checked) {
						if (checked) {// 只有在点击时触发
//							console.log('click month_limit');
							Ext.getCmp("month_limit_amount").setReadOnly(false);
							Ext.getCmp("month_limit_amount").setDisabled(false);
						}
					}
				}
            }
        ]
	},{
		fieldLabel: '约车月累计限额',
        xtype: 'textfield',
        name: 'month_limit_amount',
        id: 'month_limit_amount',
        emptyText: '元',
        readOnly: true,
        disabled: true,
        labelWidth: 80,
        width: 100,
	}, {
		xtype : "checkboxgroup",
		fieldLabel : '公车性质',
		columns: 4,
		items : [ 
		      { boxLabel : '应急机要通信接待用车(5人座)', name: "use_car_type", inputValue : 'jjx'}, 
		      { boxLabel : '行政执法用车(5人座)', name: "use_car_type", inputValue : 'ssx'}, 
		      { boxLabel : '行政执法特种专业用车(7人座)', name: "use_car_type", inputValue : 'swx'},
		      { boxLabel : '一般执法执勤用车(5人座)', name: "use_car_type", inputValue : 'hhx'},
		      { boxLabel : '执法执勤特种专业用车(7人座)', name: "use_car_type", inputValue : 'hhx'}
		]
	}, {
		xtype : "checkboxgroup",
		fieldLabel : '用车范围',
		columns: 2,
		items : [ 
		      { boxLabel : '已有车辆', name: "car_range", inputValue : 'haved'}, 
		      { boxLabel : '首汽约车', name: "car_range", inputValue : 'sq'}
		]
	}]
	}],

	buttonAlign : 'center',
	buttons : [{
				id : 'button1',
				text : '关闭',
				handler: 'clickDone',
			},{
				id: 'button2',
				text: '添加',
				handler: 'addPersonInfo'
			} ]
});