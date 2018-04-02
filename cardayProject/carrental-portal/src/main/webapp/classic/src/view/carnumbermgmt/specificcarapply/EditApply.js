Ext.define('Admin.view.carnumbermgmt.specificcarapply.EditApply', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.editapply",
    controller: 'specificcarapplycontroller',
	reference: 'editapply',
	title : '"闽O"号牌申请信息修改',
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
            //type: 'form', //form，字段验证时，整体都会改变
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
				text : '提交',
				handler: 'editApplyDone',
				disabled: true,
		        formBind: true
			}]
		}],
		
		items: [
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			fieldLabel: '申请单位',
			xtype: 'displayfield',
		    name: 'applyOrganization',
		},
		{
			
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
			msgTarget: 'side',
			fieldLabel: '品牌型号',
			xtype: 'textfield',
		    name: 'vehicleModel',
		},{
			xtype : 'combo',
			valueField : 'value',
			name : 'vehicleType',
			fieldLabel : '公车性质',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			queryMode : 'local',
			editable : false,
			emptyText : '请选择公车性质...',
			displayField : 'name',
			store : {
				fields : ['name', 'value'],
				data : [{
							"name" : "应急机要通信接待用车",
							"value" : "0"
						}, {
							"name" : "行政执法用车",
							"value" : "1"
						}, {
							"name" : "行政执法特种专业用车",
							"value" : "2"
						}, {
							"name" : "一般执法执勤用车",
							"value" : "3"
						}, {
							"name" : "执法执勤特种专业用车",
							"value" : "4"
						}]
			}
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
			msgTarget: 'side',
			fieldLabel: '车架号',
			xtype: 'textfield',
		    name: 'vin',
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
			msgTarget: 'side',
			fieldLabel: '发动机号',
			xtype: 'textfield',
		    name: 'engineNumber',
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
			msgTarget: 'side',
			fieldLabel: '联系人',
			xtype: 'textfield',
		    name: 'contactPerson',
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
			msgTarget: 'side',
			fieldLabel: '电话',
			xtype: 'textfield',
		    name: 'phone',
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
			msgTarget: 'side',
			fieldLabel: '申请理由',
			xtype: 'textfield',
		    name: 'applyReason',
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
			msgTarget: 'side',
			fieldLabel: '申请时间',
			xtype: 'datefield',
			format: 'Y-m-d',
		    name: 'applyTime',
		}
	]}],
});