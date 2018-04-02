Ext.define('Admin.view.vehiclemgmt.authorizedmgmt.AddAuthorizedVehApply', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.Panel',
			'Admin.view.vehiclemgmt.authorizedmgmt.ViewController',
			'Admin.view.vehiclemgmt.authorizedmgmt.ViewModel'],
	controller : {
		xclass : 'Admin.view.vehiclemgmt.authorizedmgmt.ViewController'
	},
	viewModel : {
		xclass : 'Admin.view.vehiclemgmt.authorizedmgmt.ViewModel'
	},
	alias: "widget.addAuthorizedVehApply",
	reference: 'addAuthorizedVehApply',
	id:'addAuthorizedVehApply',
	bodyPadding : 10,
	constrain : true,
	closable : true,
	resizable : false,
	modal : true,
	resizable : false,// 窗口大小是否可以改变
	title : '新增编制车辆',
	items : [{
		xtype : 'form',
		reference : 'addAuthorizedVehApply',
		width : 750,
		minWidth : 710,
		minHeight : 300,
		//layout : 'column',
		layout: {
	        type: 'table',
	        columns: 2
    	},
		dockedItems : [{
					xtype : 'toolbar',
					dock : 'bottom',
					ui : 'footer',
					style : "background-color:#FFFFFF",
					items : [
							'->', {
								text : '新增',
								disabled : true,
								formBind : true,
								handler : 'AddAuthorizedApplyClick'
							}, {
								text : '关闭',
								handler : function() {
									this.up('window').close();
								}
							}]
				}],
		defaults : {
			xtype : 'container',
			layout : 'form',
			defaultType : 'textfield',
			//style : 'width: 50%'
		},
		fieldDefaults : {
			margin: '0 10 0 30'
		},
		items : [/*{*/
			/*items : [*/
			         {
                xtype:'textfield',
                name:'deptId',
                hidden:true,
                value: window.sessionStorage.getItem("organizationId"),               
                listeners:{
                    afterrender: function(text){
                        text.setValue(window.sessionStorage.getItem("organizationId"));
                    }
                },
            }, {
               // margin:'0 0 0 10',
                xtype : 'combo',
                name:'deptName',
                fieldLabel : '单位名称',
                itemId : 'deptId',
                editable : false,
                emptyText : '请选择...',
              //  width:320,
                value: window.sessionStorage.getItem("organizationName"),
                listeners:{
                    expand: 'openDeptChooseWin',
                    afterrender: function(combo){
                        console.log("name: " + window.sessionStorage.getItem("organizationName"));
                        console.log("Id: " + window.sessionStorage.getItem("organizationId"));
                        combo.setValue(window.sessionStorage.getItem("organizationName"));
                    }
                },
            },
            {
				fieldLabel : '新增警力数',
				name : 'policeAdd',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				xtype:'textfield',
			   // width:320,
			},
			{
				xtype : 'fieldcontainer',
				fieldLabel : '应急机要通信接待用车',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				layout : 'hbox',
				anchor : '100%',
				defaults : {
					hideLabel : true
				},
				items : [{
							name : 'emergencyVehAuthNum',
							xtype : 'textfield',
							minValue : 0,
							width : 100,
							margin : '0 5 0 0',
							emptyText:'编制数',
							allowBlank : false,// 不允许为空
						}, {
							name : 'emergencyVehRealNum',
							xtype : 'textfield',
							minValue : 0,
							width : 100,
							allowBlank : false,// 不允许为空
							margin : '0 5 0 0',
								emptyText:'实有数',
						},
						]
			},
			{
			 	xtype:'textfield',
				fieldLabel : '增加数量',
				name : 'emergencyVehAddNum',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				defaultValue:0,
			},
			{
				xtype : 'fieldcontainer',
				fieldLabel : '行政执法用车',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				layout : 'hbox',
				anchor : '100%',
				defaults : {
					hideLabel : true
				},
				items : [{
							name : 'enforcementVehAuthNum',
							xtype : 'textfield',
							emptyText:'编制数',
							minValue : 0,
							width : 100,
							allowBlank : false,// 不允许为空
							margin : '0 5 0 0'
						}, {
							name : 'enforcementVehRealNum',
							xtype : 'textfield',
							emptyText:'实有数',
							allowBlank : false,// 不允许为空
							minValue : 0,
							width : 100,
							margin : '0 5 0 0'
						},
						]
			},
			{
			 	xtype:'textfield',
				fieldLabel : '增加数量',
				name : 'enforcementVehAddNum',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				defaultValue:0,
			},
			{
				xtype : 'fieldcontainer',
				fieldLabel : '行政执法特种专业用车',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				layout : 'hbox',
				anchor : '100%',
				defaults : {
					hideLabel : true
				},
				items : [{
							name : 'specialVehAuthNum',
							xtype : 'textfield',
							emptyText:'编制数',
							minValue : 0,
							width : 100,
							allowBlank : false,// 不允许为空
							margin : '0 5 0 0'
						}, {
							name : 'specialVehRealNum',
							xtype : 'textfield',
							emptyText:'实有数',
							minValue : 0,
							width : 100,
							allowBlank : false,// 不允许为空
							margin : '0 5 0 0'
						},
						]
			},

			{
			 	xtype:'textfield',
				fieldLabel : '增加数量',
				name : 'specialVehAddNum',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				defaultValue:0,
			},
			{
				xtype : 'fieldcontainer',
				fieldLabel : '一般执法执勤用车',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				layout : 'hbox',
				anchor : '100%',
				defaults : {
					hideLabel : true
				},
				items : [{
							name : 'normalVehAuthNum',
							xtype : 'textfield',
							emptyText:'编制数',
							allowBlank : false,// 不允许为空
							minValue : 0,
							width : 100,
							margin : '0 5 0 0'
						}, {
							name : 'normalVehRealNum',
							xtype : 'textfield',
							emptyText:'实有数',
							allowBlank : false,// 不允许为空
							minValue : 0,
							width : 100,
							margin : '0 5 0 0'
						},
						]
			},
			
			{
			 	xtype:'textfield',
				fieldLabel : '增加数量',
				name : 'normalVehAddNum',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				defaultValue:0,
			},
			{
				xtype : 'fieldcontainer',
				fieldLabel : '执法执勤特种专业用车',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				layout : 'hbox',
				anchor : '100%',
				defaults : {
					hideLabel : true
				},
				items : [
				        
				         {
							name : 'majorVehAuthNum',
							xtype : 'textfield',
							emptyText:'编制数',
							allowBlank : false,// 不允许为空
							minValue : 0,
							width : 100,
							margin : '0 5 0 0'
						}, {
							name : 'majorVehRealNum',
							xtype : 'textfield',
							allowBlank : false,// 不允许为空
							emptyText:'实有数',
							minValue : 0,
							width : 100,
							margin : '0 5 0 0'
						},
						]
				},	

				{
				 	xtype:'textfield',
					fieldLabel : '增加数量',
					name : 'majorVehAddNum',
					minValue : 0,
					afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					allowBlank : false,// 不允许为空
					blankText : '不能为空',// 提示信息
					defaultValue:0,
				},
				 {
				 	xtype:'textfield',
					fieldLabel : '原因',
					name : 'cause',
					margin: '0 0 10 30',
				   // width:320,
				},

				{
				 	xtype:'button',
					text : '添加附件',
					handler : 'AddAttach'
				},
				{
		    		fieldLabel: '附件',
					//labelWidth: 120,
					anchor: '100%',
				    xtype: 'filefield',
				    margin: '0 0 10 30',
				    name: 'file',
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
		/*	]

		}, {
			items : [*/
				
				
				
					
			//]
		/*}*/]
	}]
});