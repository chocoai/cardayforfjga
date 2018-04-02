/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.mantainance.upload.WindowController', {
    extend: 'Ext.app.ViewController',
    alias: "controller.mantainanceUploadController",
    requires: [
    ],
    onAddClick : function() {
    	var win = Ext.widget("selectFileView", {
    		title: '新增保养记录',
    		closable: true,
    		buttonAlign : 'center',
    	});
    	win.show();
    },
    loadUploadFileData : function() {
    	Ext.getCmp('uploadFileGrid').getViewModel().getStore('uploadFileStore').load();
    },
    addGirdStore : function(btn) {
		var form=this.getView().down('form').getForm();
		var path=form.getValues().uploadPath;
    	console.log('fileName:' + path);
    	
//    	var obj = document.getElementById('uploadPath'); 
//    	obj.select(); 
//    	window.parent.document.body.focus(); 
//    	var path = document.selection.createRange().text;
//    	console.log('fileName:' + path);
    	
    	var store = Ext.getCmp('uploadFileGrid').getViewModel().getStore('uploadFileStore');
    	store.insert(0,{"fileName":path});
    	btn.up("selectFileView").close();
    },
	uploadCSV:function(btn){
		var formPanel=this.getView().down('form');
		var form=this.getView().down('form').getForm();
		if (form.isValid()) {
            form.submit({
                url: 'maintenance/import',
//            	url: 'vehicle/importCSV',
                method:'post',
                waitMsg: 'Uploading your file...',
                success: function(form,action) {
                    btn.up('window').close();
//            		Ext.getCmp("searchVehicle").fireEvent("click");
            		  Ext.MessageBox.show({
                        title: '消息提示',
                        msg: action.result.msg,
                        icon: Ext.MessageBox.INFO,
                        buttons: Ext.Msg.OK
                    });
            		  
            		//表数据加载
  					var pram={
  							'currentPage' : 1,
  							'numPerPage': 10
  					};
  					Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').proxy.extraParams = {
  						"json" : Ext.encode(pram)
  					}
  					Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').load();
                }
            });
			
		}
	},
});

