Ext.define('Admin.view.systemmgmt.drivermgmt.EditDriver', {
    extend: 'Ext.window.Window',
	requires: [
		'Ext.form.Panel'
	],
	
	alias: "widget.editdriver",
	reference: 'editdriver',
	id: 'editdriver_id',
	controller: 'drivermgmtcontroller',
	title : '修改司机信息',
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
		     defaults: {
		        layout: 'form',
		        xtype: 'container',
		        defaultType: 'textfield',
		        style: 'width: 50%'
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
						handler: 'editClickDone',
						disabled: true,
				        formBind: true
					},
					{
						text: '取消',
						handler: function(btn){
							btn.up("editdriver").close();
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
						fieldLabel: '司机ID',
						xtype: 'textfield',
					    name: 'id',
					    hidden: true
					},
					{ 
		            	fieldLabel: '用户名',
						xtype: 'displayfield',
					    name: 'driverUsername',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
		            },
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
		            	fieldLabel: '邮箱',
		            	xtype: 'textfield',
		            	//emptyText: '请填写有效的邮箱地址XX',
		            	regex: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
		                regexText: '你输入的邮箱地址有误，请输入正确的邮箱地址 ',
					    msgTarget: 'side', //字段验证，提示红色标记
					    name: 'driverEmail',
		            },
		            { 
		            	fieldLabel: '手机号码',
            			regex: /[1][3578]\d{9}$/,
		                regexText: '你输入的手机号码有误，请输入11位数字的手机号码 ',
		                afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		                emptyText: '请填写有效的11位手机号码',
		                allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
		                msgTarget: 'side', //字段验证，提示红色标记
					    xtype: 'textfield',
					    name: 'phone',
					    hidden: false 
		            },
		            { 
		            	fieldLabel: '登录密码',
						xtype: 'textfield',
					    name: 'driverPassword',
					    hidden: true
		            },
					{
						xtype: "radiogroup",
						fieldLabel: "性别",
//						labelWidth: 60,
//					    width: 220,
					    id: 'sexgroup_id',
					    items: [
					        { 
					        	boxLabel: "男", 
					        	name: "sex", 
					        	inputValue: "0",
					        	//checked: true
					        	listeners : {
					        		beforerender : function() {
					        			if (Ext.getCmp('sexvalue_id').getValue() == 0) {
					        				Ext.getCmp('sexgroup_id').items.get(0).setValue(true);
					        			}
					        		},
					        	}
					        },
					        { 
					        	boxLabel: "女", 
					        	name: "sex", 
					        	inputValue: "1",
					        	listeners : {
					        		beforerender : function() {
					        			if (Ext.getCmp('sexvalue_id').getValue() == 1) {
					        				Ext.getCmp('sexgroup_id').items.get(1).setValue(true);
					        			}
					        		},
					        	}
					        	
					        }
					    ],
					    name: 'sex',
					    hidden: false
					},
					{
			        	id : 'sexvalue_id',
			    		fieldLabel: '性别值',
			    		xtype: 'textfield',
			            name: 'sexValue',
			            hidden: true
			    	},
					{
			    		id: 'edit_birthday_id',
						fieldLabel: '出生日期',
						xtype: 'datefield',
						editable: false,
					    format: 'Y-m-d',
					    name: 'birthday',
					    maxValue: Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.YEAR,-18),"Y-m-d"),
	    			    value: Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.YEAR,-18),"Y-m-d"),
					},
					{
						id: 'editdriver_org_id',
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
//							afterrender:'editSelectOrg',
//							select: 'SelectOrgDone'
						}
		            },
		            {
						fieldLabel: '所属企业ID',
						id: 'editdriver_org_id_id',
					    xtype: 'textfield',
					    name: 'organizationId',
					    listeners:{
							afterrender:'selectOrgId'
						},
					    hidden: true
					},
					{
						id: 'edit_driver_dept_id',
					    itemId: 'itemdep',
						fieldLabel: '所属部门',
						name: 'depName',
				        xtype: 'combo',
				        editable: false,
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
//			            displayField: 'name',
//			            valueField: 'id',
			            listeners:{
//			            	afterrender:'editSelectOrg',
//							select: 'SelectDepDone'
			            	expand: 'openEditDriverDeptChooseWin',
			        	},
					},
					{
						fieldLabel: '所属部门ID',
						id: 'edit_driver_dept_id_id',
					    xtype: 'textfield',
					    name: 'depId',
					    listeners:{
//							afterrender:'selectDepId'
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
						id: 'edit_driver_station_id',
						fieldLabel: '所属站点',
						itemId: 'itemStation',
						xtype: 'combo',
						editable: false,
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
					    name: 'stationName',
					    displayField: 'stationName',
					    valueField: 'id',
					    listeners:{
			        		afterrender:'selectStation',
			        		select: 'SelectStationDone'
			        		
			        	}
					},
					{
						fieldLabel: '所属站点ID',
						id: 'editdriver_station_id_id',
					    xtype: 'textfield',
					    name: 'stationId',
					    hidden: true
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
			      		fieldLabel: '准驾类型',
			      		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记,
						name: 'licenseType',
					    xtype: 'combo',
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
				        editable: false,
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
			      		fieldLabel: '驾照号码',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
			      		regex: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
			      		regexText: '你输入的驾照号码有误，请输入正确的驾照号码 ',
						xtype: 'textfield',
						name: 'licenseNumber',
			      	},
		            { 
			      		id: 'edit_licenseBegintime_id',
			      		fieldLabel: '初次领证时间',
						xtype: 'datefield',
						format: 'Y-m-d',
						//领证时间，在出生日期和当前日期之间
						maxValue: new Date(),
					    name: 'licenseBegintime',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
				        editable: false,
				        listeners : {
				        	expand : function() {
				        		var birthday = new Date();
				        		birthday = Ext.getCmp('edit_birthday_id').getValue();
				        		this.setMinValue(Ext.util.Format.date(Ext.Date.add(birthday,Ext.Date.YEAR,18),"Y-m-d"));
				        	},
				        	select : function() {
				        		var end = new Date();
				        		var start = this.getValue();
				        		var drivingYears = Math.ceil((end.getTime() - start.getTime())/(86400000*365));
				        		Ext.getCmp('edit_drivingYears_id').setValue(drivingYears);
				        	}
				        }
			      	},
			      	{				
						xtype: 'fieldcontainer',
						layout: 'hbox',
						fieldLabel: '驾龄',
						items: [
						{
							id: 'edit_drivingYears_id',
						    xtype: 'textfield',
						    editable: false,
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
				        		licenseBegintime = Ext.getCmp('edit_licenseBegintime_id').getValue();
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
		                		console.log('filefield change'  + me.isValid());
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
	}],
});