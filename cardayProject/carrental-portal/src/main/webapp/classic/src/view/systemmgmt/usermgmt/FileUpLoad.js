Ext.define('Admin.view.systemmgmt.usermgmt.FileUpLoad', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.Panel'],
	constrain : true,
	closable : true,
	resizable : false,
	ghost : false,
    modal: true,
    title:'批量导入',
 //   fileUpload:true,  
	resizable : false,// 窗口大小是否可以改变
	controller : 'usermgmtcontroller',
	
	layout : 'fit',
	items : [{ 
			xtype : 'form',
			fieldDefaults: {
       			// msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
       			 msgTarget: 'side'
    		},
			width: 400,
			bodyPadding: 10,
			items : [{
				 xtype: 'filefield',
				 name: 'file',
				 fieldLabel: '文件上传',
				 labelStyle : 'color:red;',
				 labelWidth: 80,
				 allowBlank: false,
				 anchor: '100%',
				 emptyText:'请选择文件',
				 buttonText: '请选择...',
				 //regex:/\.csv$/,
                 //regexText :'文件格式不正确'
			},{  
	        	xtype: 'label',  
	            name : 'templateLink',
	            html: "<a href='./user/loadTemplate'>模板下载</a>"  
       		}],
       		dockedItems: [{
				xtype: 'toolbar',
				dock: 'bottom',
				items: ['->', {
					xtype: 'button',
					text: '导入',
					formBind: true,
					handler: 'uploadCSV'
				}, {
					xtype: 'button',
					text: '关闭',
					handler : function() {
						this.up('window').close();
					}
				}]
       		}]
		}],
   		initComponent: function() {
			this.callParent();
		}
});