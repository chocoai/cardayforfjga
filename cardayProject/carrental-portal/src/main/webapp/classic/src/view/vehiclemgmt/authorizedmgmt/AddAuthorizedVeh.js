Ext.define('Admin.view.vehiclemgmt.authorizedmgmt.AddAuthorizedVeh', {
    extend: 'Ext.window.Window',
	requires: [
		'Ext.form.Panel'
	],
	
	alias: "widget.addAuthorizedVeh",
	reference: 'addAuthorizedVeh',
	id:'addAuthorizedVeh',
    controller: {
        xclass: 'Admin.view.vehiclemgmt.authorizedmgmt.ViewController'
    },
	title : '新增编制车辆信息',
    bodyPadding: 20,
	closable:false,//窗口是否可以改变
    constrain: true,
    modal: true,
    resizable: false,// 窗口大小是否可以改变
	items: [{
			 xtype:'form',
		     resizable: false,
			 width: 440,
   			 layout: 'vbox',
			fieldDefaults: {
	            labelWidth: 200,
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
						disabled: true,
				        formBind: true,
						handler: function(btn){							
            				Ext.Msg.alert('提示', '新增编制成功！');
							btn.up("addAuthorizedVeh").close();
						}
					},
					{
						text: '取消',
						handler: function(btn){
							btn.up("addAuthorizedVeh").close();
						}
					}
					]
				}],
				
		        items: [{
		                xtype:'textfield',
		                name:'deptId',
		                hidden:true,
		                value: window.sessionStorage.getItem("organizationId"),               
		                listeners:{
		                    afterrender: function(text){
		                        text.setValue(window.sessionStorage.getItem("organizationId"));
		                    }
		                },
		            },{
		                xtype : 'combo',
		                name:'deptName',
		                fieldLabel : '单位名称',
		                itemId : 'deptId',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		                editable : false,
		                emptyText : '请选择...',
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
/*		            { 
		            	fieldLabel: '级别',
						xtype: 'textfield',
					    name: 'levelType',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
		            },*/
		            { 
		            	fieldLabel: '应急机要通信接待用车',
						xtype: 'textfield',
					    name: 'emergencyVehNum',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
		            },
		            { 
		            	fieldLabel: '行政执法用车',
						xtype: 'textfield',
					    name: 'enforcementVehNum',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
		            },
		            { 
		            	fieldLabel: '行政执法特种专业用车',
						xtype: 'textfield',
					    name: 'specialVehNum',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
		            },
		            { 
		            	fieldLabel: '一般执法执勤用车',
						xtype: 'textfield',
					    name: 'normalVehNum',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
		            },
		            { 
		            	fieldLabel: '执法执勤特种专业用车',
						xtype: 'textfield',
					    name: 'majorVehNum',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
		            },
	      		]
	}]
});