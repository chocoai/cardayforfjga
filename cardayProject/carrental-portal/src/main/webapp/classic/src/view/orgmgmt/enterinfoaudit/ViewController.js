/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.enterinfoaudit.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
			'Admin.view.orgmgmt.enterinfoaudit.refuseWinow'
			],
	onView:function(){
		this.getViewModel().getStore('auditResults').load({params: {status : '0'}});
	},
			//查询机构
	onSearchClick : function(){
		var me =this;
		var enterinfo = this.lookupReference('searchEnterAudit').getValues();
		var compName=enterinfo['name'];
		if(null==compName||''==compName){
			Ext.Msg.alert('消息提示','请输入要查询的公司名称');
			return;
		}
		var input={
			"name":compName
		};
		
		Ext.Ajax.request({
	   		url: 'organization/listAuditByOrgName?json='+Ext.encode(input),
	        method : 'GET',
	        headers : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var data = respText.data;
				me.getViewModel().getStore('auditResults').loadData(data);
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        }*/
 		});
	},		
	refuseEvent	: function(btn){
		var enterinfo = this.lookupReference('refuseForm').getValues();
		var input={
      				"status":'3',
      				"id":enterinfo['id'],
      				"comments":enterinfo['comments']
      			};
      	var json = Ext.encode(input);
		Ext.Ajax.request({
			url: 'organization/audit/check',//?json='+Ext.encode(input),
	        method : 'POST',
	        params:{json:json},
	        //headers : {'Content-type' : 'application/json;utf-8'},
	     //   scope:this,
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var	retStatus = respText.status;
					if (retStatus == 'success') {
						btn.up('refuseWinow').close();
						Ext.Msg.alert('提示信息','修改成功');   //ceil table异常
						Ext.getCmp('auditId').getStore("auditResults").load();
					}
		        }
	        /*,
		    failure : function() {
		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
		        }*/
		});
	},
	onRefuseClick:function(grid, rowIndex, colIndex){
		var win = Ext.widget("refuseWinow");
		var rec = grid.getStore().getAt(rowIndex);	
		win.down("form").getForm().setValues({'id':rec.get('id')});
		win.show();
	},
	onApproveClick:function(grid, rowIndex, colIndex){
      	Ext.MessageBox.confirm('消息提示', '确定要审核通过?', function(btn){
      		if(btn=='yes'){
      			var input={
      				"status":'1',
      				"id":grid.getStore().getAt(rowIndex).get('id')
      			};
      	    var json = Ext.encode(input);
      		Ext.Ajax.request({
		   		url: 'organization/audit/check',//?json='+Ext.encode(input),
		        method : 'POST',
	        	params:{json:json},
		        //headers : {'Content-type' : 'application/json;utf-8'},
		        scope:this,
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
							grid.getStore().removeAt(rowIndex);
						}
			        }
	        	/*,
			    failure : function() {
			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
			        }*/
				
				});	
      		}
      	},this);
	},
	checkEnterInfo : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('checkEnterInfo', {
					title : '查看企业信息',
					ghost:false
				});
			win.down("form").loadRecord(rec);
			win.show();
    }
});
