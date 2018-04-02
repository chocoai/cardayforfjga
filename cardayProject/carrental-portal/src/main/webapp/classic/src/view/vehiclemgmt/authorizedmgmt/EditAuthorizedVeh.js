Ext.define('Admin.view.systemmgmt.drivermgmt.EditAuthorizedVeh', {
    extend: 'Ext.window.Window',
	requires: [
		'Ext.form.Panel'
	],
	
	alias: "widget.editAuthorizedVeh",
	reference: 'editAuthorizedVeh',
	id: 'editAuthorizedVeh',
    controller: {
        xclass: 'Admin.view.vehiclemgmt.authorizedmgmt.ViewController'
    },
	title : '修改编制车辆信息',
    constrain: true,
	closable:false,//窗口是否可以改变
    modal: true,
    resizable: false,// 窗口大小是否可以改变
    bodyStyle : "padding:30px 20px",
	items: [{
			 xtype:'form',
			 layout: 'vbox',
		     resizable: false,
			 width: 440,
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
            				Ext.Msg.alert('提示', '修改此编制成功！');
							btn.up("editAuthorizedVeh").close();
						}
					},
					{
						text: '取消',
						handler: function(btn){
							btn.up("editAuthorizedVeh").close();
						}
					}
					]
				}],

		        items: [
					{ 
						fieldLabel: '单位名称',
						xtype: 'displayfield',
					    name: 'deptName',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					},
					{ 
						fieldLabel: '级别',
						xtype: 'displayfield',
					    name: 'levelType',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					},
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
	}],
});