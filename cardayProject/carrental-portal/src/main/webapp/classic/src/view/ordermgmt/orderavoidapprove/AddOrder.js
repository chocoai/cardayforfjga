Ext.define('Admin.view.ordermgmt.orderavoidapprove.AddOrder', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.addorderavoidapprovewin",
    controller: 'orderavoidapprovecontroller',
	title : '免审批订单',
	minWidth : 400,
	maxHeight : 600,
	scrollable:'y',
    closeToolText:'',
	closable:true,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	frame : true,
	bodyStyle : "padding:30px 20px",
	items : [{
		xtype:'form',
		//layout : 'form',
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
				handler: 'onSubmit',
				disabled: true,
		        formBind: true
			}]
		}],
		
		items: [
        {
			fieldLabel: '单位名称',
		    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			name: 'deptName',
		    xtype: 'displayfield',
      	},
        {
			fieldLabel: '申请日期',
		    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			name: 'applyTime',
		    xtype: 'displayfield',
      	},
        {
			id: 'orderavoidapprove_orderUser_id',
			fieldLabel: '用车人',
			name: 'orderUser',
		    xtype: 'combo',
		    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
		    displayField: 'realname',
		    valueField: 'id',
		    listeners:{
				afterrender:'selectOrderUser',
				select: 'SelectOrderUserDone'
			}
      	},
        { 
      		id: 'orderavoidapprove_orderUserphone_id', 
		    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],   		
		    xtype: 'displayfield',
      		fieldLabel: '电话',
			name: 'orderUserphone',
        },        
        { 
      		id: 'orderavoidapprove_orderUserOrganizationId_id',
        	fieldLabel: '用车人组织Id',
			xtype: 'textfield',
			name: 'organizationId',
			hidden: true
        },
		{
			id: 'orderavoidapprove_vehicle_id',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			fieldLabel: '车牌号',
			name: 'vehicleNumber',
		    xtype: 'combo',
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
		    displayField: 'vehicleNumber',
		    valueField: 'id',
/*		    listeners:{
		    	expand:'selectVehicle'
			}*/
      	},
		{
			name: 'orderReason',
			fieldLabel: '事由',
		    xtype: 'textarea',
	        maxLength: 40,
	        msgTarget: 'side'
		},
    ]
	}]
});