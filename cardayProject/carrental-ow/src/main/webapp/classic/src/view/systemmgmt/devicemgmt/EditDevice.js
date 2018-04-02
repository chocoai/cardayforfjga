Ext.define('Admin.view.systemmgmt.devicemgmt.EditDevice', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.editdevice",
    id: 'edit_device_id',
    controller: 'devicemgmtcontroller',
	reference: 'editdevice',
	title : '修改终端设备信息',
	width : 420,
	height : 800,
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	scrollable : true, // 滚动条
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	bodyStyle : "padding:30px 20px",
	frame : true,
	items : [{
		xtype:'form',
		//layout : 'form',
		layout: {
            type: 'vbox', 
            //type: 'form', //form，字段验证时，整体都会改变
            align: 'stretch'
        },
        fieldDefaults: {
        	labelWidth: 110,
            width: 230,
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
					text : '保存',
					handler: 'onEditClickDone',
					disabled: true,
			        formBind: true
				},
				{
					text: '取消',
					handler: function(btn){
						btn.up("editdevice").close();
					}
				} 
			]
		}],
		items: [ 
		{
			fieldLabel: '设备Id',
			xtype: 'textfield',
		    name: 'id',
		    hidden: true
		},
		{
			id: 'edit_snNumber_id',
    		fieldLabel: 'SN设备号',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			msgTarget: 'side', //字段验证，提示红色标记
    		xtype: 'displayfield',
            name: 'snNumber'
    	},
    	{
    		id: 'edit_imeiNumber_id',
    		fieldLabel: '设备IMEI号',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			msgTarget: 'side', //字段验证，提示红色标记
    		xtype: 'displayfield',
            name: 'imeiNumber'
    	},
    	{
    		fieldLabel: '设备类型',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			msgTarget: 'side', //字段验证，提示红色标记
        	xtype: 'combo',
            name: 'deviceType',
			editable:false,
			displayField:'value',
			store : {
    			fields : ['value' ],
    			data : [
    			        {'value' : 'OBD'},
    			        {'value' : 'GPS'},
    			]
    		}
    	},
    	{
    		fieldLabel: '设备型号',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			msgTarget: 'side', //字段验证，提示红色标记
    		xtype: 'textfield',
            name: 'deviceModel'
    	},
    	{
    		fieldLabel: '设备供应商编号',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			regex: /^[A-Za-z0-9]+$/,
            regexText: '你输入的编号有误，该编号由数字和字母组成 ',
			msgTarget: 'side', //字段验证，提示红色标记
            xtype: 'textfield',
            name: 'deviceVendorNumber',
    	}, 
        {
    		fieldLabel: '设备厂家',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			msgTarget: 'side', //字段验证，提示红色标记
            xtype: 'combo',
            name: 'deviceVendor',
            editable:false,
			displayField:'value',
			store : {
    			fields : ['value' ],
    			data : [
    			        {'value' : 'DBJ'},
    			        {'value' : 'GOSAFE'},
    			        {'value' : 'LS_GENIUS'},
    			        {'value' : 'DH'}
    			]
    		}
    	}, 
    	{
    		fieldLabel: '固件版本',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			msgTarget: 'side', //字段验证，提示红色标记
            xtype: 'textfield',
            name: 'firmwareVersion',
    	},
    	{
    		fieldLabel: '软件版本',
            xtype: 'textfield',
            name: 'softwareVersion',
    	},
    	{
    		fieldLabel: '采购时间',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			msgTarget: 'side', //字段验证，提示红色标记
    		editable: false,
    		xtype: 'datefield',
	    	format: 'Y-m-d',
            name: 'purchaseTime',
            maxValue: new Date(),
    	},
    	{
    		fieldLabel: '保修到期日',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			msgTarget: 'side', //字段验证，提示红色标记
    		editable: false,
    		xtype: 'datefield',
	    	format: 'Y-m-d',
            name: 'maintainExpireTime',
        },
        {
			fieldLabel: 'ICCID编号',
			id: 'iccidNumber_id',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			regex:/^[A-Za-z0-9]{20}$/,
			regexText: '你输入的编号有误，该编号由20位数字、字母组成 ',
    		msgTarget: 'side', //字段验证，提示红色标记
//	        xtype: 'displayfield',
	        xtype: 'textfield',
	        name: 'iccidNumber',
    	},
    	{
			fieldLabel: 'SIM卡号',
	        xtype: 'textfield',
	        name: 'simNumber',
    	},
    	
    	{
    		id: 'edit_device_vehicleNumber_id',
			fieldLabel: '绑定车牌号',
			xtype: 'combo',
			name: 'vehicleNumber',
			queryMode: 'remote',
			valueField : 'id',
			editable: true,
			displayField:'vehicleNumber',
			hideTrigger: true,
			minChars: 1,
			typeAhead: true,
            matchFieldWidth: true,
	        store:new Ext.data.Store({
	        	proxy: {
                    type: 'ajax',
                    url: 'vehicle/listUnBindDeviceVehicle',
                    actionMethods : 'get', 
                    reader: { 
                        type: 'json', 
                        totalProperty: 'totalRows', 
                        rootProperty: 'data' 
                    } 
                },
                listeners : {
                	'beforeload' : function(store, operation, eOpts) {
		        		console.log('+++++beforeload++++');
		        		var vehicleNumberCombo = Ext.getCmp('edit_device_vehicleNumber_id').getValue();
		        		var input = {'vehicleNumber' : vehicleNumberCombo};
		        		var param = {'json': Ext.encode(input)};
		        		Ext.apply(store.proxy.extraParams, param);
		        	}
                },
                autoLoad : false, 
	        }),
	        listeners:{
	        	select: 'fillVehicleInfo',
	        	change: 'vehicleNumberChange'
	        }
    	},
    	{
			fieldLabel: '车辆来源',
	        xtype: 'displayfield',
	        id : 'vehicleSource',
	        name: 'vehicleSource',
    	},
    	{
			fieldLabel: '车辆VIN号',
	        xtype: 'displayfield',
	        id : 'vehicleIdentification',
	        name: 'vehicleIdentification',
    	},
    	{
			fieldLabel: '车辆限速',
	        xtype: 'displayfield',
	        id : 'limitSpeed',
	        name: 'limitSpeed',
    	},
    	{
			fieldLabel: '下发执行状态',
	        xtype: 'displayfield',
	        id : 'commandStatus',
	        name: 'commandStatus',
	        renderer : function (value) {
	        	if (value == "excuting") {
	        		return value="下发限速执行中";
	        	} else if (value == "success") {
	        		return value="下发限速成功";
	        	} else if (value == "failure") {
	        		return value="下发限速失败";
	        	} else {
	        		return value="";
	        	}
	        },
    	},
    	{
			fieldLabel: '最新下发限速',
	        xtype: 'displayfield',
	        id : 'latestLimitSpeed',
	        name: 'latestLimitSpeed',
    	},
    	{
    		id: 'edit_device_vehicleId_id',
			fieldLabel: '车辆Id',
	        xtype: 'textfield',
	        name: 'vehicleId',
	        hidden: true
    	},
    	{
			fieldLabel: '设备状态',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			msgTarget: 'side', //字段验证，提示红色标记
	        xtype: 'combo',
	        name: 'deviceStatus',
	        valueField: 'id',
            displayField: 'name',
            store: {
            	fields:['id', 'name'],
            	data : [
			        {'id': '1', 'name': '正常'},
			        {'id': '2', 'name': '未配置'},
			        {'id': '3', 'name': '故障'},
				]
            }
    	},
    	{
			fieldLabel: '设备批次',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			msgTarget: 'side', //字段验证，提示红色标记
	        xtype: 'textfield',
	        name: 'deviceBatch',
    	},
	]}
	]
});