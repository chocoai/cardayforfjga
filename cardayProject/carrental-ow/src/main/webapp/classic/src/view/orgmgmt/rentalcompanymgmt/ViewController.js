/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.rentalcompanymgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.orgmgmt.rentalcompanymgmt.SearchForm',
			'Admin.view.orgmgmt.rentalcompanymgmt.Grid',
			],
	init: function(view) {
        this.roleType = window.sessionStorage.getItem('userType');
        this.getViewModel().set('status',this.roleType);

    },
/**
 * 查看企业信息
 * @param {} grid
 * @param {} rowIndex
 * @param {} colIndex
 */
	checkRentalCompanyInfo : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('checkRentalCompanyInfo');
		win.down("form").loadRecord(rec);
		win.show();

		if(rec.data.businessType != ''){
			if(rec.data.businessType.indexOf('自有车') >= 0){
				Ext.getCmp("businessTypeCheckGroupRental").items.items[0].setValue(true);
			}
			if(rec.data.businessType.indexOf('长租车') >= 0){
				Ext.getCmp("businessTypeCheckGroupRental").items.items[1].setValue(true);
			}
		}
    },
    
/**
 * 添加企业
 * @param {} btn
 */
	addRentalCompanyInfo : function(btn){

		var businessTypeArray = new Array();
		var enterinfo = this.lookupReference('addRentalCompanyInfo').getValues();	
		if (this.lookupReference('addRentalCompanyInfo').getForm().isValid()) {
			if(typeof enterinfo['businessType'] == 'string'){
	            businessTypeArray.push(enterinfo['businessType']);
			}else{
				businessTypeArray = enterinfo['businessType'];
			}
 			var input = {
				"name" 			: enterinfo['name'],
				"shortname" 	: enterinfo['shortname'],
				"address" 		: enterinfo['address'],
				"introduction" 	: enterinfo['introduction'],
				"linkman" 		: enterinfo['linkman'],
				"linkmanPhone" 	: enterinfo['linkmanPhone'],
				"linkmanEmail" 	: enterinfo['linkmanEmail'],
				"startTime" 	: enterinfo['startTime'],
				"endTime" 		: enterinfo['endTime'],
				"businessType" 	 : businessTypeArray,
				"enterprisesType" : '0',
			};
 			var json_input = Ext.encode(input);
	        Ext.Ajax.request({
	        	url:'organization/addOrganization',//?json='+Ext.encode(input),
				method:'POST',
				params:{ json:json_input},
//				headers: {'Content-Type':'application/json','charset':'UTF-8'},
				success: function(res){
					var appendData=Ext.JSON.decode(res.responseText);
					if(appendData.status=='success'){
						btn.up('addRentalCompanyInfo').close();
				 		Ext.Msg.alert("提示信息", '添加租车公司成功');
				 		Ext.getCmp("rentalCompanyId").getStore('usersResults').load();
					}else{
						btn.up('addRentalCompanyInfo').close();
						Ext.MessageBox.alert("提示信息","添加租车公司失败");
				 		Ext.getCmp("rentalCompanyId").getStore('usersResults').load();
					}
				 	
				 },
				failure : function() {
					Ext.Msg.alert('失败提醒','调用接口失败!');
				}
	        });
		 }else{
		 	Ext.Msg.alert('消息提示', '输入的信息格式不正确');
		 }
	},
	
/**
 * 修改企业信息
 * @param {} grid
 * @param {} rowIndex
 * @param {} colIndex
 */
	editRentalCompanyInfo : function(grid, rowIndex, colIndex){

		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('editRentalCompanyInfo');
		win.down("form").loadRecord(rec);
		win.show();

		if(rec.data.businessType != ''){
			if(rec.data.businessType.indexOf('自有车') >= 0){
				Ext.getCmp("businessTypeCheckGroupEditRental").items.items[0].setValue(true);
			}
			if(rec.data.businessType.indexOf('长租车') >= 0){
				Ext.getCmp("businessTypeCheckGroupEditRental").items.items[1].setValue(true);
			}
		}
		if(rec.data.status == '3'){
			Ext.getCmp("editRentalCompanyInfo").startTime = Ext.util.Format.date(rec.data.startTime, "Y-m-d");
		    Ext.getCmp("editRentalCompanyInfo").endTime = Ext.util.Format.date(rec.data.endTime, "Y-m-d");
			Ext.getCmp("startTimeEditRental").setDisabled(true);
			Ext.getCmp("endTimeEditRental").setDisabled(true);
		}
	},
	
/**
 * 更新企业信息
 * @param {} rec
 * @param {} enterinfo
 * @param {} grid
 */
	updateRentalCompanyInfo: function(btn){

		var businessTypeArray = new Array();
		var enterinfo = this.lookupReference('editRentalCompanyInfo').getValues();	
		if (this.lookupReference('editRentalCompanyInfo').getForm().isValid()) {
			if(typeof enterinfo['businessType'] == 'string'){
	            businessTypeArray.push(enterinfo['businessType']);
			}else{
				businessTypeArray = enterinfo['businessType'];
			}
			var status;
			if(enterinfo['status'] == 1){
				status = '0';
			}else{
				status = enterinfo['status'];
			}

			if(enterinfo['status'] == '3'){
                enterinfo['startTime'] = Ext.getCmp("editRentalCompanyInfo").startTime;
                enterinfo['endTime'] = Ext.getCmp("editRentalCompanyInfo").endTime;
			}
 			var input = {
						"id"  			 : enterinfo['id'],
						"name" 			 : enterinfo['name'],
						"shortname" 	 : enterinfo['shortname'],
						"address" 		 : enterinfo['address'],
						"introduction" 	 : enterinfo['introduction'],
						"linkman"        : enterinfo['linkman'],
						"linkmanPhone" 	 : enterinfo['linkmanPhone'],
						"linkmanEmail" 	 : enterinfo['linkmanEmail'],
						"startTime" 	 : enterinfo['startTime'],
						"endTime" 		 : enterinfo['endTime'],
						"businessType" 	 : businessTypeArray,
						"status"         : status,
			};
 			var json_input = Ext.encode(input);
	        Ext.Ajax.request({
				url:'organization/'+enterinfo['id']+'/update',//?json='+Ext.encode(input),
				method:'POST',
				params:{ json:json_input},
//				headers: {'Content-Type':'application/json','charset':'UTF-8'},
	            success: function(response) {
	                var data=Ext.JSON.decode(response.responseText);
	                if(data.status=='success'){
	                	btn.up('editRentalCompanyInfo').close();
	               		Ext.Msg.alert("消息提示", '租车公司信息修改成功！');
				 		Ext.getCmp("rentalCompanyId").getStore('usersResults').load();
	               	}else{
	                    Ext.Msg.alert("消息提示", '租车公司信息修改失败！');
				 		Ext.getCmp("rentalCompanyId").getStore('usersResults').load();
	               	}
	            },
	            failure : function() {
					Ext.Msg.alert('失败提醒','调用接口失败!');
				}	
			});
		 }else{
		 	Ext.Msg.alert('消息提示', '输入的信息格式不正确!');
		 }
	},
	
	onBeforeload: function(){
		console.log('onBeforeLoad');
		var frmValues = this.lookupReference('searchForm').getValues();
		var page=Ext.getCmp('rentalCompanyIdPage').store.currentPage;
		var limit=Ext.getCmp('rentalCompanyIdPage').pageSize;

		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"organizationName":frmValues.name,
				"organizationType" : '0',
				"status" : frmValues.status
			};
		var pram = Ext.encode(input);
		this.getViewModel().getStore("usersResults").proxy.extraParams = {
			"json" : pram
		}
	},
// 渲染View,加载数据
	onSearchClick:function(){
		console.log('onSearchClick');
		var EnterInfoStore = this.lookupReference('gridRentalInfo').getStore();
		EnterInfoStore.currentPage = 1;
		this.getViewModel().getStore('usersResults').load();
	},

//点击添加按钮，弹出添加企业信息窗口
	onAddClick : function() {
		var win = Ext.widget('addRentalCompanyInfo');
		var startTime = new Date();
		Ext.getCmp('addRentalCompanyInfo').down('form').getForm().findField('startTime').setValue(startTime);
	    var endTime = new Date(startTime.getTime() + 30*24*60*60*1000);
		Ext.getCmp('addRentalCompanyInfo').down('form').getForm().findField('endTime').setValue(endTime);
		win.show();
	},

	showAuditInfo:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
    	var win = Ext.widget("showAuditInfoRental", {
			title: '审核信息',
			buttonAlign : 'center',
			companyId: rec.data.id,
			buttons : [{
				text : '关闭',
				handler: function(){
					this.up('showAuditInfoRental').close();
				},
			}]
		});
		win.show();
	},

	onAfterrenderShowAudit: function(){
            var companyId = Ext.getCmp("showAuditInfoRental").companyId;
            var input = {
            	'id':companyId,
            };
			var pram = Ext.encode(input);
			Ext.getCmp("gridAuditInfoRental").getViewModel().getStore("auditInfoStore").proxy.extraParams = {
				"json" : pram
			},
			Ext.getCmp("gridAuditInfoRental").getViewModel().getStore("auditInfoStore").load();
	},

	resourcesInfoRentalCompany: function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
    	var win = Ext.widget("showResourcesInfoRental", {
			title: '查看租车公司资源',
			buttonAlign : 'center',
			companyId: rec.data.id,
			buttons : [{
				text : '关闭',
				handler: function(){
					this.up('showResourcesInfoRental').close();
				},
			}]
		});
		win.show();
		var input = {
			"entId" : rec.data.id,
			"name" : rec.data.name,
		};
		var pram = Ext.encode(input);
		Ext.getCmp('searchFormVehsResourcesRental').form.findField('arrangeEnt').getStore().proxy.extraParams = {
         "json":Ext.encode(input)
        }   
	//	Ext.getCmp('searchFormVehsResourcesRental').form.findField('fromOrgId').getStore().proxy.url = 'vehicle/listVehicleFrom?json=' + pram;
	
	},

/*用车企业车辆资源信息*/
	onBeforeloadVehsResources: function(){
		var companyId = Ext.getCmp("showResourcesInfoRental").companyId;
		var frmValues = this.lookupReference('searchFormVehsResources').getValues();
		var page=Ext.getCmp('vehsResourcesRentalPage').store.currentPage;
		var limit=Ext.getCmp('vehsResourcesRentalPage').pageSize;


		if(frmValues.arrangeEnt == ""){
			frmValues.arrangeEnt = '-1';
		}
		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"vehicleNumber": frmValues.vehicleNumber,
				"vehicleType": frmValues.vehicleType,
				"arrangeEnt":frmValues.arrangeEnt,
				"deptId":companyId,
				"fromOrgId":companyId,
				"selfDept":true,
				"childDept":true
			};
		var pram = Ext.encode(input);
		this.getViewModel().getStore("vehsResourcesStore").proxy.extraParams = {
			"json" : pram
		}
	},

	onSearchClickforVehsResources :　function() {
		var VehicleStore = this.lookupReference('gridVehsResources').getStore();
		VehicleStore.currentPage = 1;
		this.getViewModel().getStore("vehsResourcesStore").load();
	},

	checkVehsResourcesInfo: function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('checkVehsResourcesInfoRental');
		win.down("form").loadRecord(rec);
		win.show();
	},

	rentalInfoRentalCompany: function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
    	var win = Ext.widget("showRentalInfoRental", {
			title: '关联用车企业',
			buttonAlign : 'center',
			companyId: rec.data.id,
			buttons : [{
				text : '保存',
				handler: function(btn){
					var jsonArray = [];
					var unRelatedArray=[];
					var length = Ext.getCmp('gridRentalInfoRental').getStore().getCount();
					var store = Ext.getCmp('gridRentalInfoRental').getStore();
					var entId = Ext.getCmp('showRentalInfoRental').companyId;
					for(var i=0;i<length;i++){
					   var record=store.getAt(i).data;
					   if(record.isRelated){
					   	if(record.vehicleNumber<record.realVehicleNumber){
								 Ext.Msg.alert("消息提示", '企业实际用车数大于签约用车数，关联失败！');
								 return;
							}
					   	jsonArray.push(record);
					   }else{
							if(record.realVehicleNumber > 0){
								 Ext.Msg.alert("消息提示", '企业实际用车数非0，不能取消关联！');
								 return;
							}
						   	unRelatedArray.push(record);
					   }
					}
		            var json = Ext.encode(jsonArray);
		             var unRelated=Ext.encode(unRelatedArray);
					Ext.Ajax.request({
		                url: 'organization/updateRelatedRentCompany', 
		                method: 'POST',
		                params:{json:json,entId:entId,unRelatedArray:unRelated},
		                success: function(response) {
		                    var respText = Ext.util.JSON.decode(response.responseText);
		                   	if(respText.status=='success'){
			                	btn.up('showRentalInfoRental').close();
			               		Ext.Msg.alert("消息提示", '租车公司租赁信息修改成功！');
						 		Ext.getCmp("rentalCompanyId").getStore('usersResults').load();
			               	}else{
			               		btn.up('showRentalInfoRental').close();
			                    Ext.Msg.alert("消息提示", '租车公司租赁信息修改失败！');
						 		Ext.getCmp("rentalCompanyId").getStore('usersResults').load();
			               	}
		                },
		                failure: function(response) {
		                    Ext.Msg.alert('失败提醒','调用接口失败!');
		                }
		            });
				},
			},{
				text : '取消',
				handler: function(){
					this.up('showRentalInfoRental').close();
				},
			}]
		});
		win.show();
	},


	onAfterrenderShowRentalInfo: function(){
            var companyId = Ext.getCmp("showRentalInfoRental").companyId;
            var input = {
            	'entId':companyId,
            };
			var pram = Ext.encode(input);
			Ext.getCmp("gridRentalInfoRental").getViewModel().getStore("rentalInfoStore").proxy.extraParams = {
				"json" : pram
			},
			Ext.getCmp("gridRentalInfoRental").getViewModel().getStore("rentalInfoStore").load();
	},

	creditLimitInfo:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
    	var win = Ext.widget("showCreditLimitInfoRental", {
			orgId: rec.data.id,
			orgName: rec.data.name,
		});
		win.show();
	},

	onRechargeClick:function() {
		var win = Ext.widget('rechargeWindowRental');
		win.down("form").getForm().findField('name').setValue(Ext.getCmp('showCreditLimitInfoRental').orgName);
		win.show();
    },

    onBeforeloadPayHistory: function(){
		var currentPage=Ext.getCmp('payHistoryPage').store.currentPage;
		var numPerPage=Ext.getCmp('payHistoryPage').pageSize;
        var input = {
        	'orgId':Ext.getCmp('showCreditLimitInfoRental').orgId,
        	'currentPage':currentPage,
        	'numPerPage':numPerPage
        };
		var pram = Ext.encode(input);
		Ext.getCmp("gridPayHistory").getViewModel().getStore("payHistoryStore").proxy.extraParams = {
			"json" : pram
		}
    },

    onAfterrenderShowCreditLimitInfoRental: function(){    	
   		var html = '<div class="companyForCreditLimit">'+Ext.getCmp('showCreditLimitInfoRental').orgName+'</div>'
   		Ext.getCmp('companyForCreditLimit').setHtml(html);
    	/*获取可分配额度与总额度*/
		Ext.Ajax.request({
            url: 'organization/findOrgCreditById/' + Ext.getCmp('showCreditLimitInfoRental').orgId, 
            method: 'GET',
            success: function(response) {
                var respText = Ext.util.JSON.decode(response.responseText);
               	if(respText.status=='success'){
               		Ext.getCmp('availableAccount').setValue(respText.data.availableCredit);
               		Ext.getCmp('totalAccount').setValue(respText.data.limitedCredit);
               	}else{
                    Ext.Msg.alert("消息提示", '无法获取所需信息！');
               	}
            },
            failure: function(response) {
                Ext.Msg.alert('失败提醒','调用接口失败!');
            }
        });

		/*获取充值历史*/
		Ext.getCmp("gridPayHistory").getStore().currentPage = 1;
		Ext.getCmp("gridPayHistory").getViewModel().getStore("payHistoryStore").load();
    },

    rechargeDone: function(){
    	if (Ext.getCmp("rechargeWindowRental").down('form').getForm().isValid()) {
	    	var rechargeNum = Ext.getCmp("rechargeWindowRental").down("form").getForm().getValues().rechargeNum;
		    var input = {
	            'orgId': Ext.getCmp('showCreditLimitInfoRental').orgId,
	            'creditValue': rechargeNum,
	        };
	        var json = Ext.encode(input);
	    	Ext.Ajax.request({
	            url: 'organization/recharageCredit', 
	            method: 'POST',
                params:{json:json},
	            success: function(response) {
	                var respText = Ext.util.JSON.decode(response.responseText);
	               	if(respText.status == 'success'){
	               		Ext.Msg.alert("消息提示", '充值成功！');
	               		Ext.getCmp("rechargeWindowRental").close();

	               		var html = '<div class="companyForCreditLimit">'+Ext.getCmp('showCreditLimitInfoRental').orgName+'</div>'
				   		Ext.getCmp('companyForCreditLimit').setHtml(html);
				    	/*获取可分配额度与总额度*/
						Ext.Ajax.request({
				            url: 'organization/findOrgCreditById/' + Ext.getCmp('showCreditLimitInfoRental').orgId, 
				            method: 'GET',
				            success: function(response) {
				                var respText = Ext.util.JSON.decode(response.responseText);
				               	if(respText.status=='success'){
				               		Ext.getCmp('availableAccount').setValue(respText.data.availableCredit);
				               		Ext.getCmp('totalAccount').setValue(respText.data.limitedCredit);
				               	}else{
				                    Ext.Msg.alert("消息提示", '无法获取所需信息！');
				               	}
				            },
				            failure: function(response) {
				                Ext.Msg.alert('失败提醒','调用接口失败!');
				            }
				        });

						/*获取充值历史*/
						Ext.getCmp("gridPayHistory").getStore().currentPage = 1;
						Ext.getCmp("gridPayHistory").getViewModel().getStore("payHistoryStore").load();
	               	}else{	                    
	               		Ext.Msg.alert("消息提示", '充值失败！');
	               		Ext.getCmp("rechargeWindowRental").close();
	               		
	               		var html = '<div class="companyForCreditLimit">'+Ext.getCmp('showCreditLimitInfoRental').orgName+'</div>'
				   		Ext.getCmp('companyForCreditLimit').setHtml(html);
				    	/*获取可分配额度与总额度*/
						Ext.Ajax.request({
				            url: 'organization/findOrgCreditById/' + Ext.getCmp('showCreditLimitInfoRental').orgId, 
				            method: 'GET',
				            success: function(response) {
				                var respText = Ext.util.JSON.decode(response.responseText);
				               	if(respText.status=='success'){
				               		Ext.getCmp('availableAccount').setValue(respText.data.availableCredit);
				               		Ext.getCmp('totalAccount').setValue(respText.data.limitedCredit);
				               	}else{
				                    Ext.Msg.alert("消息提示", '无法获取所需信息！');
				               	}
				            },
				            failure: function(response) {
				                Ext.Msg.alert('失败提醒','调用接口失败!');
				            }
				        });

						/*获取充值历史*/
						Ext.getCmp("gridPayHistory").getStore().currentPage = 1;
						Ext.getCmp("gridPayHistory").getViewModel().getStore("payHistoryStore").load();
	               	}
	            },
	            failure: function(response) {
	                Ext.Msg.alert('失败提醒','调用接口失败!');
	            }
	        });
	    }else{	    	
            Ext.Msg.alert('失败提醒','输入的充值金额有误，请重新输入!');
	    }
    },

    closeRechargeWindow: function(){
	    Ext.getCmp("rechargeWindowRental").close();
    	this.onAfterrenderShowCreditLimitInfoRental();
    },

});
