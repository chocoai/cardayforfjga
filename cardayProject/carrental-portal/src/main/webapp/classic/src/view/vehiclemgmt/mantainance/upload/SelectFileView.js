Ext.define('Admin.view.vehiclemgmt.mantainance.upload.SelectFileView', {
	extend : 'Ext.window.Window',
	alias: "widget.selectFileView",
	requires : ['Ext.form.Panel'],
	reference : 'selectFileView',
	id:'selectFileView',
//	bodyPadding : 10,
 //   frame: true,
	controller: 'mantainanceUploadController',
	constrain : true,
	closable : true,
	resizable : false,
	ghost : false,
    modal: true,
    title:'批量导入',
    fileUpload:true,  
	resizable : false,// 窗口大小是否可以改变
//	controller : {
//		xclass : 'Admin.view.vehiclemgmt.mantainance.upload.WindowController'
//	},
//	viewModel : {
//		xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewModel'
//	},
	layout : 'fit',
	items : [{ 
			xtype : 'form',
			fieldDefaults: {
       			// msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
       			 msgTarget: 'side'
    		},
			width: 400,
			bodyPadding: 10,
			frame: true,
			items : [{
//				 xtype: 'filefield',
//				 name: 'uploadFile',
//				 id: 'selectUploadPath',
//				 fieldLabel: '文件上传',
//				 labelStyle : 'color:red;',
//				 labelWidth: 80,
//				 allowBlank: false,
//				 anchor: '100%',
//				 emptyText:'请选择csv格式的文件',
//				 buttonText: '请选择...',
//				 regex:/\.csv$/,
//                 regexText :'文件格式不正确'
				
				xtype: 'textfield', 
			    fieldLabel: '文 件上传',   
			    name: 'uploadPath',
			    id: 'uploadPath',
			    text: '浏览',
			    inputType: 'file',   
			    allowBlank: false,   
			    blankText: '请上传文件',   
			    anchor: '90%',  // anchor width by percentage  
			    regex:/\.csv$/,
	            regexText :'文件格式不正确'

 
			}],
       		dockedItems: [{
				xtype: 'toolbar',
				dock: 'bottom',
				items: ['->', {
					xtype: 'button',
					text: '确定',
			//		glyph: 0xf0c7,
					formBind: true,
					handler: 'addGirdStore'
				}, {
					xtype: 'button',
					text: '关闭',
				//	glyph: 0xf057,
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