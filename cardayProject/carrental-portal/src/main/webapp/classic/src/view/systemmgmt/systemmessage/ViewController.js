/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.systemmessage.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.systemmgmt.systemmessage.View',
			'Admin.view.systemmgmt.systemmessage.SearchForm',
			],
	alias : 'controller.systemmessagecontroller',

    afterrenderView: function(){
	    var userType = window.sessionStorage.getItem('userType');
	     if(userType == '2' || userType == '6'){
	         Ext.getCmp('onAddClick').show();
	     }
    	this.getViewModel().getStore("messageResults").load();
    },

	onBeforeLoad : function() {
		var page=Ext.getCmp('messageId').store.currentPage;
		var limit=Ext.getCmp('messageId').pageSize;
		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
			};
		var pram = Ext.encode(input);
		this.getViewModel().getStore("messageResults").proxy.extraParams = {
			"json" : pram
		}
	},

	onAddClick : function() {
		win = Ext.widget('addMessage');
		win.show();
	},

	onAddClickDone : function(btn) {
		var myMask = new Ext.LoadMask({
					    msg    : '请稍后，正在添加公告消息........',
					    target : this.getView()
					});
        myMask.show();  

		var messageInfo = this.getView().down('form').getForm().getValues();
		var input = {
        		'title': messageInfo.title,
        		'content': messageInfo.msg,
			};
		var json = Ext.encode(input);
		if (this.getView().down('form').getForm().isValid()) {
		
		   	 Ext.Ajax.request({
				url : 'sysMessage/addSysMessage',//?json='+ Ext.encode(input),
		        method : 'POST',
	        	params:{json:json},
		        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
		        	var respText = Ext.util.JSON.decode(response.responseText);
		        	var retStatus = respText.status;
					if (retStatus == 'success') {
						btn.up('addMessage').close();
						Ext.Msg.alert('提示信息','添加公告消息成功');
						Ext.getCmp("gridMessage").getStore('messageResults').load();
					}else{
						btn.up('addMessage').close();
						Ext.Msg.alert('消息提示','添加公告消息失败！');
						Ext.getCmp("gridMessage").getStore('messageResults').load();
					}
		        }
//	        	,
//		        failure : function() {
//					btn.up('addMessage').close();
//		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//		        }
		    });
	   	 }

	},

	viewMessage : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("viewMessage");
		win.down("form").loadRecord(rec);
		if(rec.data.title == null){
			win.down("form").getForm().findField('title').hide();
		}else{
			win.down("form").getForm().findField('title').show();
		}
		win.show();
	},
});
