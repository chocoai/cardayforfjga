Ext.define('Admin.view.systemconfiguration.vehSendConfiguration.AddVehSend', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.Panel',
			'Admin.view.systemconfiguration.vehSendConfiguration.ViewController',
			'Admin.view.systemconfiguration.vehSendConfiguration.ViewModel'],
	controller : {
		xclass : 'Admin.view.systemconfiguration.vehSendConfiguration.ViewController'
	},
    viewModel: {
        xclass: 'Admin.view.systemconfiguration.vehSendConfiguration.ViewModel'
    },
	id:'addVehSendWindow',

	title:'新增派车审批设置',

	width : 400,
	closable:true,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	bodyStyle : "padding:30px 20px",
	frame : true,
	items : [{
		xtype:'form',
		layout: {
            type: 'vbox', 
            align: 'stretch'
        },
        fieldDefaults: {
            msgTarget : Ext.supports.Touch ? 'side' : 'qtip'
        },
        
        dockedItems : [{
			xtype : 'toolbar',
			dock : 'bottom',
			ui : 'footer',
			style : "background-color:#FFFFFF",
			layout: {pack: 'center'},//button居中
			items : [
			{
				width: 100,
				text : '确认',
				disabled: true,
		        formBind: true,
				handler : function() {
					Ext.Msg.alert('提示', '添加成功！');
					this.up('window').close();
				}
			},
			{
				width: 100,
				text : '取消',
				handler : function() {
					this.up('window').close();
				}
			},]
		}],
		
		items: [
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
			msgTarget: 'side',
			fieldLabel: '模式名称',
			xtype: 'textfield',
		    name: 'patternName',
		},{
			xtype : 'combo',
			valueField : 'value',
			name : 'approvalRating',
			fieldLabel : '审批级数',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			queryMode : 'local',
			editable : false,
			emptyText : '请选择审批级数',
			displayField : 'name',
		    listeners:{
		    	select: 'onApprovalRatingSelect'
		    },
			store : {
				fields : ['name', 'value'],
				data : [{
							"name" : "一级",
							"value" : "0"
						}, {
							"name" : "二级",
							"value" : "1"
						}, {
							"name" : "三级",
							"value" : "2"
						}, {
							"name" : "四级",
							"value" : "3"
						}, {
							"name" : "五级",
							"value" : "4"
						}]
			}
		},
		{
			xtype : 'fieldcontainer',
			fieldLabel : '第一级节点',
			hidden:true,
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			id:'adminNameFirst',
			layout : 'hbox',
			anchor : '100%',
			height : 32,
			defaults : {
				hideLabel : true,
				margin : '0 5 0 0'
			},
			items : [{
						name : 'adminNameFirst',
						xtype : 'textfield',
						width : 120
					}, {
						xtype : 'button',
						text : '选择',
					    listeners:{
					    	click: 'openAdminChooseWin'
					    },
					}]
		},
		{
			xtype : 'fieldcontainer',
			fieldLabel : '第二级节点',
			hidden:true,
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			id:'adminNameSecond',
			layout : 'hbox',
			anchor : '100%',
			height : 32,
			defaults : {
				hideLabel : true,
				margin : '0 5 0 0'
			},
			items : [{
						name : 'adminNameSecond',
						xtype : 'textfield',
						width : 120
					}, {
						xtype : 'button',
						text : '选择',
					    listeners:{
					    	click: 'openAdminChooseWin'
					    },
					}]
		},
		{
			xtype : 'fieldcontainer',
			fieldLabel : '第三级节点',
			hidden:true,
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			id:'adminNameThird',
			layout : 'hbox',
			anchor : '100%',
			height : 32,
			defaults : {
				hideLabel : true,
				margin : '0 5 0 0'
			},
			items : [{
						name : 'adminNameThird',
						xtype : 'textfield',
						width : 120
					}, {
						xtype : 'button',
						text : '选择',
					    listeners:{
					    	click: 'openAdminChooseWin'
					    },
					}]
		},
		{
			xtype : 'fieldcontainer',
			fieldLabel : '第四级节点',
			hidden:true,
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			id:'adminNameForth',
			layout : 'hbox',
			anchor : '100%',
			height : 32,
			defaults : {
				hideLabel : true,
				margin : '0 5 0 0'
			},
			items : [{
						name : 'adminNameForth',
						xtype : 'textfield',
						width : 120
					}, {
						xtype : 'button',
						text : '选择',
					    listeners:{
					    	click: 'openAdminChooseWin'
					    },
					}]
		},
		{
			xtype : 'fieldcontainer',
			fieldLabel : '第五级节点',
			hidden:true,
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			id:'adminNameFifth',
			layout : 'hbox',
			anchor : '100%',
			height : 32,
			defaults : {
				hideLabel : true,
				margin : '0 5 0 0'
			},
			items : [{
						name : 'adminNameFifth',
						xtype : 'textfield',
						width : 120
					}, {
						xtype : 'button',
						text : '选择',
					    listeners:{
					    	click: 'openAdminChooseWin'
					    },
					}]
		},
	]}],
});