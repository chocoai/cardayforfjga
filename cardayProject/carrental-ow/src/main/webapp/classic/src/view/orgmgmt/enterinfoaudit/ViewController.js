/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.enterinfoaudit.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
			//'Admin.view.orgmgmt.enterinfoaudit.RefuseWindow'
			],

	onBeforeload: function(){
		console.log('onBeforeLoad');
		var frmValues = this.lookupReference('searchForm').getValues();
		var page=Ext.getCmp('auditIdPage').store.currentPage;
		var limit=Ext.getCmp('auditIdPage').pageSize;

		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"organizationName":frmValues.name,
				"organizationType" : '1',
				"status" : frmValues.status
			};
		var pram = Ext.encode(input);
		this.getViewModel().getStore("auditResults").proxy.extraParams = {
			"json" : pram
		}
	},

	onSearchClick:function(){
		console.log('onSearchClick');
		var EnterInfoStore = this.lookupReference('gridAuditEnterInfo').getStore();
		EnterInfoStore.currentPage = 1;
		this.getViewModel().getStore('auditResults').load();
	},

	checkEnterAuditInfo: function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('checkEnterAuditInfo');
		win.down("form").loadRecord(rec);
		win.show();

		if(rec.data.businessType != ''){
			if(rec.data.businessType.indexOf('自有车') >= 0){
				Ext.getCmp("businessTypeEnterAudit").items.items[0].setValue(true);
			}
			if(rec.data.businessType.indexOf('长租车') >= 0){
				Ext.getCmp("businessTypeEnterAudit").items.items[1].setValue(true);
			}
		}
	},

	openEvent: function(btn){
		var enterinfo = this.lookupReference('openForm').getValues();
		var input={
      				"status":'3',
      				"id":enterinfo['id'],
      				"endTime":enterinfo['endTime'],
      				"startTime":enterinfo['startTime']
      			}
		var json_input = Ext.encode(input);
		Ext.Ajax.request({
			url: 'organization/audit/check',//?json='+Ext.encode(input),
	        method : 'POST',
	        params:{ json:json_input},
//	        headers : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var	retStatus = respText.status;
					if (retStatus == 'success') {
						btn.up('openWindow').close();
						Ext.getCmp('auditId').getStore("auditResults").load();
					}else{
						btn.up('openWindow').close();
						Ext.Msg.alert('消息提示','续约服务失败！');
						Ext.getCmp('auditId').getStore("auditResults").load();
					}
		        }
	        /*,
		    failure : function() {
		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
		        }*/
		});
	},
	onOpenClick:function(grid, rowIndex, colIndex){
		var win = Ext.widget("openWindow");
		var rec = grid.getStore().getAt(rowIndex);	
		var serviceDate = Ext.util.Format.date(rec.data.startTime, "Y/m/d")+'一'+ Ext.util.Format.date(rec.data.endTime, "Y/m/d");
		win.down("form").getForm().setValues({'id':rec.get('id')});
		win.down("form").getForm().setValues({'entName':rec.get('name')});
		win.down("form").getForm().setValues({'serviceTime':serviceDate});
		win.down("form").getForm().setValues({'startTime':Ext.util.Format.date(rec.data.startTime, "Y/m/d")});
		win.down("form").getForm().setValues({'endTime':rec.get('endTime')});
		win.show();
	},

	afterrenderOpenWindow: function(){
		   var extendTitlePanel=this.lookupReference('openTitlePanel');		   
		   var enterinfo = this.lookupReference('openForm').getValues();
		   extendTitlePanel.getViewModel().set('entName', enterinfo['entName']);
		   extendTitlePanel.getViewModel().set('serviceTime', enterinfo['serviceTime']);
	},
	
	onApproveClick:function(grid, rowIndex, colIndex){
      	Ext.MessageBox.confirm('消息提示', '确定审核通过?', function(btn){
      		if(btn=='yes'){
      			var input={
      				"status":'2',
      				"id":grid.getStore().getAt(rowIndex).get('id')
      			}
      			var json_input = Ext.encode(input);
      		Ext.Ajax.request({
		   		url: 'organization/audit/check',//?json='+Ext.encode(input),
		        method : 'POST',
		        params:{ json:json_input},
//		        headers : {'Content-type' : 'application/json;utf-8'},
		        scope:this,
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
							Ext.getCmp('auditId').getStore("auditResults").load();
						}else{
							Ext.Msg.alert('消息提示','审核通过失败！');
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

	onPauseClick:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex).data;
		var serviceDate = Ext.util.Format.date(rec.startTime, "Y/m/d")+'一'+ Ext.util.Format.date(rec.endTime, "Y/m/d");
		
      	Ext.MessageBox.confirm('暂停服务', rec.name + '的用车服务期限为：'+ serviceDate +',确认暂停服务?', function(btn){
      		if(btn=='yes'){
      			var input={
      				"status":'5',
      				"id":grid.getStore().getAt(rowIndex).get('id')
      			}
      			
      			var json_input = Ext.encode(input);
      		Ext.Ajax.request({
		   		url: 'organization/audit/check', //?json='+Ext.encode(input),
		        method : 'POST',
//		        headers : {'Content-type' : 'application/json;utf-8'},
		        params:{ json:json_input},
		        scope:this,
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
							Ext.getCmp('auditId').getStore("auditResults").load();
						}else{
							Ext.Msg.alert('消息提示','暂停服务失败！');
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

	onContinueClick:function(grid, rowIndex, colIndex){
		var today = new Date();
		var rec = grid.getStore().getAt(rowIndex).data;
		var serviceDate = Ext.util.Format.date(rec.startTime, "Y/m/d")+'一'+ Ext.util.Format.date(rec.endTime, "Y/m/d");
		if(rec.endTime < today){
           Ext.Msg.alert('提示信息',rec.name + '的用车服务期限超期！');
		}else{
	      	Ext.MessageBox.confirm('继续服务', '确认继续服务?', function(btn){
	      		if(btn=='yes'){
	      			var input={
	      				"status":'3',
	      				"id":rec.id
	      			}
	      			var json_input = Ext.encode(input);
	      		Ext.Ajax.request({
			   		url: 'organization/audit/check',//?json='+Ext.encode(input),
			        method : 'POST',
			        params:{ json:json_input},
//			        headers : {'Content-type' : 'application/json;utf-8'},
			        scope:this,
			        success : function(response,options) {
						var respText = Ext.util.JSON.decode(response.responseText);
						var	retStatus = respText.status;
							if (retStatus == 'success') {
								Ext.getCmp('auditId').getStore("auditResults").load();
							}else{
								Ext.Msg.alert('消息提示','继续服务失败！');
							}
				        }
			        /*,
				    failure : function() {
				            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
				        }*/
					
					});	
	      		}
	      	},this);
      }
	},

	onStopClick:function(grid, rowIndex, colIndex){
		var today = new Date();
		var rec = grid.getStore().getAt(rowIndex).data;
		var serviceDate = Ext.util.Format.date(rec.startTime, "Y/m/d")+'一'+ Ext.util.Format.date(rec.endTime, "Y/m/d");
		
      	Ext.MessageBox.confirm('停止服务', rec.name + '的用车服务期限为：'+ serviceDate +',停止服务后，'+ rec.name +'的所有用户将无法登陆系统。确认停止服务?', function(btn){
      		if(btn=='yes'){
      			var input={
      				"status":'4',
      				"id":grid.getStore().getAt(rowIndex).get('id'),
      				"endTime": Ext.util.Format.date(today, "Y/m/d")
      			}
      			var json_input = Ext.encode(input);
      		Ext.Ajax.request({
		   		url: 'organization/audit/check',//?json='+Ext.encode(input),
		        method : 'POST',
		        params:{ json:json_input},
//		        headers : {'Content-type' : 'application/json;utf-8'},
		        scope:this,
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
							Ext.getCmp('auditId').getStore("auditResults").load();
						}else{
							Ext.Msg.alert('消息提示','停止服务失败！');
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

	refuseEvent	: function(btn){
		var enterinfo = this.lookupReference('refuseForm').getValues();
		var input={
      				"status":'1',
      				"id":enterinfo['id'],
      				"reason":enterinfo['comments']
      			}
		var json_input = Ext.encode(input);
		Ext.Ajax.request({
			url: 'organization/audit/check',//?json='+Ext.encode(input),
	        method : 'POST',
	        params:{ json:json_input},
//	        headers : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var	retStatus = respText.status;
					if (retStatus == 'success') {
						btn.up('refuseWindow').close();
						Ext.getCmp('auditId').getStore("auditResults").load();
					}else{
						btn.up('refuseWindow').close();
						Ext.Msg.alert('消息提示','驳回服务失败！');
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
		var win = Ext.widget("refuseWindow");
		var rec = grid.getStore().getAt(rowIndex);	
		win.down("form").getForm().setValues({'id':rec.get('id')});
		win.show();
	},

	extendEvent	: function(btn){
		var enterinfo = this.lookupReference('extendForm').getValues();
		var input={
      				"status":'3',
      				"id":enterinfo['id'],
      				"endTime":enterinfo['endTime']
      			}
		var json_input = Ext.encode(input);
		Ext.Ajax.request({
			url: 'organization/audit/check',//?json='+Ext.encode(input),
	        method : 'POST',
	        params:{ json:json_input},
//	        headers : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var	retStatus = respText.status;
					if (retStatus == 'success') {
						btn.up('extendWindow').close();
						Ext.getCmp('auditId').getStore("auditResults").load();
					}else{
						btn.up('extendWindow').close();
						Ext.Msg.alert('消息提示','续约服务失败！');
						Ext.getCmp('auditId').getStore("auditResults").load();
					}
		        }
	        /*,
		    failure : function() {
		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
		        }*/
		});
	},
	onExtendClick:function(grid, rowIndex, colIndex){
		var win = Ext.widget("extendWindow");
		var rec = grid.getStore().getAt(rowIndex);	
		var serviceDate = Ext.util.Format.date(rec.data.startTime, "Y/m/d")+'一'+ Ext.util.Format.date(rec.data.endTime, "Y/m/d");
		win.down("form").getForm().setValues({'id':rec.get('id')});
		win.down("form").getForm().setValues({'entName':rec.get('name')});
		win.down("form").getForm().setValues({'serviceTime':serviceDate});
		win.down("form").getForm().setValues({'endTime':rec.get('endTime')});
		win.show();
	},

	afterrenderExtendWindow: function(){
		   var extendTitlePanel=this.lookupReference('extendTitlePanel');		   
		   var enterinfo = this.lookupReference('extendForm').getValues();
		   extendTitlePanel.getViewModel().set('entName', enterinfo['entName']);
		   extendTitlePanel.getViewModel().set('serviceTime', enterinfo['serviceTime']);
	},

});
