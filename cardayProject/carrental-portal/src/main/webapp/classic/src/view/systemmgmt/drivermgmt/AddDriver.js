Ext.define('Admin.view.systemmgmt.drivermgmt.AddDriver', {
    extend: 'Ext.window.Window',
	requires: [
		'Ext.form.Panel'
	],
	
	alias: "widget.adddriver",
	reference: 'adddriver',
	id:'adddriver',
	controller: 'drivermgmtcontroller',
	title : '新增司机',
    bodyPadding: 10,
    constrain: true,
    closable: true,
    modal: true,
    resizable: false,// 窗口大小是否可以改变
    bodyStyle : "padding:30px 20px",
	items: [{
			 xtype:'form',
			 layout: 'column',
		     resizable: false,
			 width: 740,
   			 layout: 'column',
		     defaults: {
		        layout: 'form',
		        xtype: 'container',
		        defaultType: 'textfield',
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
						text : '确定',
						handler: 'onAddClickDone',
						disabled: true,
				        formBind: true
					},
					{
						text: '取消',
						handler: function(btn){
							btn.up("adddriver").close();
						}
					}
					]
				}],
				
		     items: [{
		    	width: 330,
		    	layout: {
		            type: 'vbox', 
		            //type: 'form', //form，字段验证时，整体都会改变
		            align: 'stretch'
		        },
		        items: [
					{ 
						fieldLabel: '司机姓名',
						xtype: 'textfield',
					    name: 'realname',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
					},
		            { 
		            	fieldLabel: '用户名',
						xtype: 'textfield',
					    name: 'driverUsername',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
		            },
		            { 
		            	id: 'add_driver_password_id',
		            	fieldLabel: '登录密码',
						xtype: 'textfield',
						inputType: 'password',
					    name: 'driverPassword',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
					    emptyText: '请输入6-20位字符,由字母数组组成',
				        blankText: '不能为空',//提示信息
			            regex: /^([a-zA-Z0-9]){6,20}$/,
			            regexText: '你输入的密码不符合要求，请输入6-20位字符,由字母数组组成 ',
		            },
		            {
						//浏览器会找到inputType为password的控件，自动填充密码和用户名，
						//新增一个同样的password，可以防止自动填充的问题
						xtype: 'textfield',
						inputType: 'password',
					    name: 'driverPassword',
					    hidden: true,
					},
		            { 
		            	fieldLabel: '邮箱',
		            	//emptyText: '请填写有效的邮箱地址',
		            	regex: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
		                regexText: '你输入的邮箱地址有误，请输入正确的邮箱地址 ',
	                	//afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记,
					    xtype: 'textfield',
					    name: 'driverEmail',
					    hidden: false
		            },
		            { 
		            	fieldLabel: '手机号码',
		            	emptyText: '请填写有效的11位手机号码',
		                //maskRe: /[\d\-]/,
            			regex: /[1][3578]\d{9}$/,
		                regexText: '你输入的手机号码有误，请输入11位数字的手机号码 ',
					    xtype: 'textfield',
					    name: 'phone',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
		            },
		            { 
		            	xtype: "radiogroup",
						fieldLabel: "性别",
					    items: [
					        { 
					        	boxLabel: "男", 
					        	name: "sex", 
					        	inputValue: "0",
					        	checked: true
					        },
					        { 
					        	boxLabel: "女", 
					        	name: "sex", 
					        	inputValue: "1" 
					        }
					    ],
					    name: 'sex',
					    hidden: false
	      			},
	      			{
	      				id: 'birthday_id',
	      				fieldLabel: '出生日期',
	    				xtype: 'datefield',
	    				format: 'Y-m-d',
	    			    name: 'birthday',
	    			    //司机年龄必须大于18岁
	    			    editable: false,
	    			    maxValue: Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.YEAR,-18),"Y-m-d"),
	    			    value: Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.YEAR,-18),"Y-m-d"),
	      			},
		            {
		            	id: 'add_driver_org_id',
						fieldLabel: '所属企业',
						name: 'organizationName',
					    xtype: 'textfield',
					    editable: false,
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
					    listeners:{
							afterrender:'selectOrg'
						}
			      	},
			      	{
		            	id: 'add_driver_org_id_id',
						fieldLabel: '所属企业ID',
						xtype: 'textfield',
						name: 'organizationId',
						listeners:{
							afterrender:'selectOrgId'
						},
					    hidden: true
			      	},{
				        xtype: 'combo',
				        valueField: 'value',
				        name: 'drvStatus',
				        fieldLabel: '司机状态',
				        queryMode: 'local',
				        editable:false,
				        displayField:'name',
				        value:'',
				        store:{
				            fields: ['name', 'value'],
				            data : [
				                {"name":"短途出车", "value":0},
				                {"name":"长途出车", "value":1},
				                {"name":"在岗", "value":2},
				                {"name":"值班锁定", "value":3},
				                {"name":"补假/休假", "value":4},
				                {"name":"计划锁定", "value":5},
				                {"name":"出场", "value":6},
				                {"name":"下班", "value":7}
				        ]}
			      	   },
	      		]
		     },
		     {
		    	width: 360,
		    	margin: '0 0 0 40',
		    	layout: {
		            type: 'vbox', 
		            //type: 'form', //form，字段验证时，整体都会改变
		            align: 'stretch'
		        },
		        items: [
			      	{
			    		id: 'add_driver_dept_id',
			    		itemId: 'itemorg',
						fieldLabel: '所属部门',
						name: 'deptment',
						emptyText: '请选择...',
//						value: '暂未分配',
				        xtype: 'combo',
				        editable: false,
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
//			            displayField: 'name',
//			            valueField: 'id',
			            listeners:{
//			        		afterrender:'selectDeptment',
//			        		select: 'SelectDepDone'
			            	expand: 'openAddDriverDeptChooseWin',
			        	}
			      	},
			      	{
		            	id: 'add_driver_dept_id_id',
						fieldLabel: '所属部门ID',
						xtype: 'textfield',
						name: 'depId',
						listeners:{
//							afterrender:'selectDepId'
						},
					    hidden: true
			      	},
			      	{ 
			      		id: 'add_driver_station_id',
			      		fieldLabel: '所属站点',
			      		itemId: 'itemStation',
						name: 'station',
						value: '暂未分配',
					    xtype: 'combo',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
				        editable: false,
					    displayField: 'stationName',
					    valueField: 'id',
					    listeners:{
			        		afterrender:'selectStation',
			        		select: 'SelectStationDone'
			        	}
//					    store : {
//							fields : ['id', 'name'],
//							data : [
//							        {'id' : '-1', 'name' : '暂不分配'},
//							        {'id' : '1', 'name' : '站点1'},
//							        {'id' : '2', 'name' : '站点2'}
//							]
//						}
			      	},
			      	{
						fieldLabel: '所属站点ID',
						id: 'adddriver_station_id_id',
					    xtype: 'textfield',
					    name: 'stationId',
					    hidden: true
					},
			      	{ 
			      		fieldLabel: '准驾类型',
			      		name: 'licenseType',
					    xtype: 'combo',
			      		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记,
					    editable: false,
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
					    displayField: 'value',
					    //valueField: 'id',
					    store : {
							fields : ['value' ],
							data : [
							        {'value' : 'A1'},
							        {'value' : 'A2'},
							        {'value' : 'A3'},
							        {'value' : 'B1'},
							        {'value' : 'B2'},
							        {'value' : 'C1'},
							        {'value' : 'C2'}
							]
						}
			      	},
		            { 
			      		fieldLabel: '员工类别',
					    xtype: 'textfield',
					    name: 'userCategory',
					    value: 5,  //员工类型，5：司机
						hidden: true 
			      	},
		            { 
			      		fieldLabel: '角色ID',
						name: 'roleId',
					    xtype: 'textfield',
					    value: 7, //司机没有角色，与后端约定，司机角色id传7给后端;
					    hidden: true
			      	},
		            
		            { 
			      		fieldLabel: '驾照号码',
			      		regex: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
			      		regexText: '你输入的驾照号码有误，请输入正确的驾照号码 ',
						xtype: 'textfield',
						name: 'licenseNumber',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
			      	},
		            {
			      		id: 'licenseBegintime_id',
			      		fieldLabel: '初次领证时间',
						xtype: 'datefield',
						format: 'Y-m-d',
						//初次领证时间是出生日期+18年之后的日期,
						//minValue: new Date(),
					    name: 'licenseBegintime',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    editable: false,
					    maxValue: new Date(),
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
				        listeners : {
				        	expand : function() {
				        		var birthday = new Date();
				        		birthday = Ext.getCmp('birthday_id').getValue();
				        		this.setMinValue(Ext.util.Format.date(Ext.Date.add(birthday,Ext.Date.YEAR,18),"Y-m-d"));
				        	},
				        	select : function() {
				        		var end = new Date();
				        		var start = this.getValue();
				        		var drivingYears = Math.ceil((end.getTime() - start.getTime())/(86400000*365));
				        		Ext.getCmp('drivingYears_id').setValue(drivingYears);
				        	}
				        }
			      	},
			      	{				
						xtype: 'fieldcontainer',
						layout: 'hbox',
						fieldLabel: '驾龄',
						items: [
						{
							id: 'drivingYears_id',
							editable: false,
						    xtype: 'textfield',
						    name: 'drivingYears',
						}, 
						{
							xtype: 'displayfield',
					    	value: '年',
						}
						]
					},
					{
						fieldLabel: '驾照到期时间',
					    xtype: 'datefield',
					    format: 'Y-m-d',
					    name: 'licenseExpiretime',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
				        editable: false,
				        listeners : {
				        	expand : function() {
				        		var licenseBegintime = new Date();
				        		licenseBegintime = Ext.getCmp('licenseBegintime_id').getValue();
				        		this.setMinValue(Ext.util.Format.date(Ext.Date.add(licenseBegintime,Ext.Date.Day,2),"Y-m-d"));
				        	}
				        }
					},
					{
						fieldLabel: '驾照附件',
						//labelWidth: 120,
						anchor: '100%',
					    xtype: 'filefield',
					    name: 'licenseAttach',
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
						
						xtype: 'fieldcontainer',
						layout: 'hbox',
						fieldLabel: '基本工资',
						items: [
						{
							id: 'salary_id',
							editable: true,
						    xtype: 'textfield',
						    name: 'salary',
						}, 
						{
							xtype: 'displayfield',
					    	value: '元',
						}
						]
					
					}
		        ]
		     }]
	}]
});