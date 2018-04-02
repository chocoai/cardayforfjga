Ext.define('Admin.view.ordermgmt.orderlist.AddOrder', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.addorder",
    controller: 'orderlistcontroller',
	reference: 'addorder',
	title : '新增订单',
	width : 460,
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
				handler: 'onAddClickDone',
				disabled: true,
		        formBind: true
			}]
		}],
		
		items: [
		{
			id: 'add_order_organizationName_id',
			fieldLabel: '单位名称',
			xtype: 'displayfield',
		    name: 'organizationName',
		},
		{
			id: 'add_order_orderUserid_id',
			fieldLabel: '用车人Id',
			xtype: 'textfield',
		    name: 'orderUserid',
		    hidden: true
		},
		{
			id: 'add_order_organizationId_id',
			fieldLabel: '组织Id',
			xtype: 'textfield',
		    name: 'organizationId',
		    hidden: true
		},
		{
			id: 'add_order_orderUsername_id',
			fieldLabel: '用车人',
			xtype: 'displayfield',
		    name: 'orderUsername',
		    height:32,
		},
    	{
			id: 'add_order_orderUserphone_id',
    		fieldLabel: '用车人电话',
    		xtype: 'displayfield',
            name: 'orderUserphone',
            height:32,
    	},
        {
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
    		editable:false,
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
    		fieldLabel: '用车城市',
            xtype: 'combo',
            name: 'city',
            value: '福州市',
            displayField: 'name',
            store: {
            	fields:['id', 'name'],
            	data : [
				        {'id': '0', 'name': '福州市'},
				        {'id': '1', 'name': '厦门市'},
				        {'id': '2', 'name': '莆田市'},
				        {'id': '3', 'name': '三明市'},
				        {'id': '4', 'name': '泉州市'},
				        {'id': '5', 'name': '漳州市'},
				        {'id': '6', 'name': '南平市'},
				        {'id': '7', 'name': '龙岩市'},
				        {'id': '8', 'name': '宁德市'},

				]
            }
    	}, 
    	{
    		id:'addOrderFromPlaceId',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
    		fieldLabel: '出发地',
            xtype: 'textfield',
            name: 'fromPlace',
    	},
    	{
    		id:'addOrderFromPlace_lng_id',
    		fieldLabel: '出发地经度',
            xtype: 'textfield',
            name: 'fromPlaceLongitude',
            hidden: true,
    	},
    	{
    		id:'addOrderFromPlace_lat_id',
    		fieldLabel: '出发地纬度',
            xtype: 'textfield',
            name: 'fromPlaceLatitude',
            hidden: true,
    	},
    	{
    		id:'addOrderToPlaceId',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
    		fieldLabel: '目的地',
    		xtype: 'textfield',
            name: 'toPlace'
    	},
    	{
    		id:'addOrderToPlace_lng_id',
    		fieldLabel: '目的地经度',
            xtype: 'textfield',
            name: 'toPlaceLongitude',
            hidden: true,
    	},
    	{
    		id:'addOrderToPlace_lat_id',
    		fieldLabel: '目的地纬度',
            xtype: 'textfield',
            name: 'toPlaceLatitude',
            hidden: true,
    	},
    	{
    		id:'addOrder_planTime_id',
    		fieldLabel: '预约用车时间',
            xtype: 'textfield',
            name: 'planTime',
            hidden: true,
    	},
    	{
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
    		xtype: 'fieldcontainer',
			layout: 'hbox',
//			labelWidth: 60,
			fieldLabel: '预约用车时间',
			height:32,
			items: [
			    {
			    	id: 'add_order_planTime_id',
			    	xtype: 'datefield',
			    	format: 'Y-m-d',
			    	width: 125,
			    	name: 'date',
			    	value : new Date(),
			    	minValue: new Date(),
			    	listeners:{
			    		select: 'selectPlanTime'
					} 
			    },
			    {
					xtype: 'displayfield',
					value: '日',
				},
			    {
					id: 'plan_hour_id',
			    	xtype: 'combo',
			    	editable: false,
			    	width: 70,
			    	value: '00',
			    	displayField: 'time',
			    	name: 'hour',
			    	displayField: 'time',
		            valueField: 'id',
			        listeners:{
			        	select: 'selectHour'
			    	} 
			    },
			    {
			    	xtype: 'displayfield',
					value: '时',
			    },
			    {
			    	id: 'plan_minute_id',
			    	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    		allowBlank: false,//不允许为空
			        blankText: '不能为空',//提示信息
			        msgTarget: 'side',
			    	xtype: 'numberfield',
			    	width: 70,
			    	minValue: 0,
			    	maxValue: 59,
			    	name: 'tminute1',
			    },
			    {
			    	xtype: 'displayfield',
					value: '分',
			    }
			 ]
    	},
    	{
    		id:'addorder_durationTime1_id',
    		fieldLabel: '预计行车时间',
            xtype: 'textfield',
            name: 'durationTime1',
            hidden: true,
    	},
    	{
    		id:'addorder_durationTime_id',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
    		xtype: 'fieldcontainer',
    		layout: 'hbox',
    		fieldLabel: '预计行程时间',
    		height:32,
    		items: [
				{
					xtype: 'combo',
					width: 75,
					displayField: 'value',
					valueField: 'value',
					value: 0,
					name:'durationTime',
					editable: false,
					store: {
						fields:['name', 'value'],
				    	data : [
				    	        {name: '0', value: '0'},
						        {name: '1', value: '1'},
						        {name: '2', value: '2'},
						        {name: '3', value: '3'},
						        {name: '4', value: '4'},
						        {name: '5', value: '5'},
						        {name: '6', value: '6'},
						        {name: '7', value: '7'},
						]
					}
				},
		    	{
			    	xtype: 'displayfield',
					value: '小时',
			    },
			    {
			    	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    		allowBlank: false,//不允许为空
			        blankText: '不能为空',//提示信息
			        msgTarget: 'side',
			    	xtype: 'numberfield',
			    	width: 70,
			    	value: 0,
			    	minValue: 0,
			    	maxValue: 59,
			    	name: 'tminute2',
			    },
			    {
			    	xtype: 'displayfield',
					value: '分',
			    }
    		]
    	},
    	{
			xtype: 'radiogroup',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
	        msgTarget: 'side',
			fieldLabel: '是否往返',
			height:32,
			layout: 'hbox',
	        items: [
			        { 
			        	boxLabel: "是", 
			        	name: "returnType", 
			        	inputValue: "0",
			        	style:{
			        		marginRight:'20px',
			        	}
			        },
			        { 
			        	boxLabel: "否", 
			        	name: "returnType",
			        	inputValue: "1" ,
			        	checked: true
			        }
			    ],
		    listeners : {
		    	change : function (obj, newValue , oldValue) {
		    		if (newValue.returnType == 1 && oldValue.returnType == 0) {
		    			//选中否，隐藏 ‘等待时长’字段
		    			Ext.getCmp('addorder_waitTime_id').setHidden(true);
		    		} else if (newValue.returnType == 0 && oldValue.returnType == 1) {
		    			//选中是，显示 ‘等待时长’字段
		    			Ext.getCmp('addorder_waitTime_id').setHidden(false);
		    		}
		    	}
		   }
    	},
    	{
    		id:'addorder_waitTime1_id',
    		fieldLabel: '等待时长',
            xtype: 'textfield',
            name: 'waitTime1',
            hidden: true,
    	},
    	{
    		id : 'addorder_waitTime_id',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
    		xtype: 'fieldcontainer',
    		layout: 'hbox',
    		fieldLabel: '等待时长',
    		editable: false,
    		hidden: true,
    		items: [
				{
					xtype: 'combo',
					width: 75,
					value: 0,
					displayField: 'value',
					valueField: 'value',
					name:'waitTime',
					store: {
						fields:['name', 'value'],
				    	data : [
				    	        {name: '0', value: '0'},
						        {name: '1', value: '1'},
						        {name: '2', value: '2'},
						        {name: '3', value: '3'},
						        {name: '4', value: '4'},
						        {name: '5', value: '5'},
						        {name: '6', value: '6'},
						        {name: '7', value: '7'},
						]
					}
				},
		    	{
			    	xtype: 'displayfield',
					value: '小时',
			    },
			    {
			    	xtype: 'numberfield',
			    	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    		allowBlank: false,//不允许为空
			        blankText: '不能为空',//提示信息
			        msgTarget: 'side',
			    	width: 70,
			    	value: 0,
			    	minValue: 0,
			    	maxValue: 59,
			    	name: 'tminute3',
			    },
			    {
			    	xtype: 'displayfield',
					value: '分',
			    }
    		]
    	},
    	{
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
    		fieldLabel: '公车性质',
    		editable:false,
	        xtype: 'combo',
	        name: 'vehicleType',
            displayField: 'name',
            valueField: 'id',//combo可以获取的值
            store: {
            	fields:['id', 'name'],
            	data : [
				        {'id': '0', 'name': '应急机要通信接待用车'},
				        {'id': '1', 'name': '行政执法用车'},
				        {'id': '2', 'name': '行政执法特种专业用车'},
				        {'id': '3', 'name': '一般执法执勤用车'},
				        {'id': '4', 'name': '执法执勤特种专业用车'},
				]
            }
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
			fieldLabel: '车辆用途',
			editable:false,
			name: 'vehicleUsage',
	        xtype: 'combo',
            displayField: 'name',//combo显示的值
            valueField: 'id',//combo可以获取的值
            store: {
            	fields:['id', 'name'],
            	data : [
				        {'id': '0', 'name': '市区'},
				        {'id': '1', 'name': '省外'},

				]
            }
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
			fieldLabel: '用车事由',
			editable:false,
			name: 'orderReason',
	        xtype: 'combo',
            displayField: 'name',//combo显示的值
            valueField: 'id',//combo可以获取的值
            store: {
            	fields:['id', 'name'],
            	data : [
				        {'id': '0', 'name': '送文件'},
				        {'id': '1', 'name': '特殊警务'},
				        {'id': '2', 'name': '常规巡逻'}

				]
            }
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
			fieldLabel: '驾驶类型',
			editable:false,
			name: 'drivingType',
	        xtype: 'combo',
            displayField: 'name',//combo显示的值
            valueField: 'id',//combo可以获取的值
            store: {
            	fields:['id', 'name'],
            	data : [
				        {'id': '0', 'name': '派遣驾驶员'},
				        {'id': '1', 'name': '民警自驾'},
				        {'id': '2', 'name': '临时驾驶员'}

				]
            }
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
			fieldLabel: '涉密级别',
			editable:false,
			name: 'secretLevel',
	        xtype: 'combo',
            displayField: 'name',//combo显示的值
            valueField: 'id',//combo可以获取的值
		    listeners:{
				afterrender:function(me,eOpts){
					var secretLevelStore;
					if(window.sessionStorage.getItem("hasSpecialServicePerm") == "true"){
						secretLevelStore = {
					    		data: [{'id': '0', 'name': '不涉密'},{'id': '1', 'name': '机密'},{'id': '2', 'name': '绝密'}]
							};
					}else{
						secretLevelStore = {
					    		data: [{'id': '0', 'name': '不涉密'}]
							};
					}
					me.setStore(secretLevelStore);
				},
			}
		},
		{
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
    		xtype: 'fieldcontainer',
    		layout: 'hbox',
    		fieldLabel: '随车人数',
    		height:32,
    		editable: false,
    		items: [
			    {
			    	xtype: 'numberfield',
			    	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    		allowBlank: false,//不允许为空
			        blankText: '不能为空',//提示信息
			        msgTarget: 'side',
			    	width: 100,
			    	minValue: 1,
			    	name: 'passengerNum',
			    },
			    {
			    	xtype: 'displayfield',
					value: '人',
			    }
    		]
    	},
    	{
			fieldLabel: '用车备注',
			name: 'comments',
	        xtype: 'textarea',
	        maxLength: 40,
	        msgTarget: 'side',
	        hidden: true
    	},
		{
    		fieldLabel: '附件',
			//labelWidth: 120,
			anchor: '100%',
		    xtype: 'filefield',
		    name: 'orderAttach',
		    emptyText:'请选择jpg格式图片',
		    regex:/\.jpg$/,
		    regexText :'文件格式不正确',
		    buttonText: '',
            buttonConfig: {
                iconCls: 'fa-file'
            },
            hidden: false,
            listeners: {
            	change: function(me,value,eOpts){
            		console.log('filefield change' + me.isValid());
            		if(!me.isValid()){
            			Ext.Msg.alert('消息提示','文件格式不正确，请重新选择！');
            			me.setRawValue('');		                			
            		}
            	},
			},
		},
    	{
			flex: 2,
	        xclass: 'Admin.view.ordermgmt.orderlist.OrderMap',
	        hidden: true
        }
	]}],
});