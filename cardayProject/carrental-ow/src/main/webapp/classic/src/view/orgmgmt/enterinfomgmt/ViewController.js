/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.enterinfomgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.orgmgmt.enterinfomgmt.SearchForm',
			'Admin.view.orgmgmt.enterinfomgmt.Grid',
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
	checkEnterInfo : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('checkEnterInfo');
		win.down("form").loadRecord(rec);
		win.show();

		if(rec.data.businessType != ''){
			if(rec.data.businessType.indexOf('自有车') >= 0){
				Ext.getCmp("businessTypeCheckGroup").items.items[0].setValue(true);
			}
			if(rec.data.businessType.indexOf('长租车') >= 0){
				Ext.getCmp("businessTypeCheckGroup").items.items[1].setValue(true);
			}
		}
    },
    
/**
 * 添加企业
 * @param {} btn
 */
	addEnterInfo : function(btn){
        var businessTypeArray = new Array();
		var enterinfo = this.lookupReference('addEnterInfo').getValues();	
		if (this.lookupReference('addEnterInfo').getForm().isValid()) {
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
				"enterprisesType" : '1',
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
						btn.up('addEnterInfo').close();
				 		Ext.Msg.alert("提示信息", '添加用车企业成功');
				 		Ext.getCmp("enterId").getStore('usersResults').load();
					}else{
						btn.up('addEnterInfo').close();
						Ext.MessageBox.alert("提示信息","添加用车企业失败");
				 		Ext.getCmp("enterId").getStore('usersResults').load();
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
	editEnterInfo : function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget('editEnterInfo');
		win.down("form").loadRecord(rec);
		win.show();

		if(rec.data.businessType != ''){
			if(rec.data.businessType.indexOf('自有车') >= 0){
				Ext.getCmp("businessTypeCheckGroupEdit").items.items[0].setValue(true);
			}
			if(rec.data.businessType.indexOf('长租车') >= 0){
				Ext.getCmp("businessTypeCheckGroupEdit").items.items[1].setValue(true);
			}
		}

		if(rec.data.status == '3'){
			Ext.getCmp("editEnterInfo").startTime = Ext.util.Format.date(rec.data.startTime, "Y-m-d");
		    Ext.getCmp("editEnterInfo").endTime = Ext.util.Format.date(rec.data.endTime, "Y-m-d");
			Ext.getCmp("startTimeEdit").setDisabled(true);
			Ext.getCmp("endTimeEdit").setDisabled(true);
		}
	},
	
/**
 * 更新企业信息onAddClick
 * @param {} rec
 * @param {} enterinfo
 * @param {} grid
 */
	updateEnterInfo: function(btn){
        var businessTypeArray = new Array();
		var enterinfo = this.lookupReference('editEnterInfo').getValues();	
		if (this.lookupReference('editEnterInfo').getForm().isValid()) {
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
                enterinfo['startTime'] = Ext.getCmp("editEnterInfo").startTime;
                enterinfo['endTime'] = Ext.getCmp("editEnterInfo").endTime;
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
	                	btn.up('editEnterInfo').close();
	               		Ext.Msg.alert("消息提示", '用车企业信息修改成功！');
				 		Ext.getCmp("enterId").getStore('usersResults').load();
	               	}else{
	                    Ext.Msg.alert("消息提示", '用车企业信息修改失败！');
				 		Ext.getCmp("enterId").getStore('usersResults').load();
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
		var page=Ext.getCmp('enterIdPage').store.currentPage;
		var limit=Ext.getCmp('enterIdPage').pageSize;

		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"organizationName":frmValues.name,
				"organizationType" : '1',
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
		var EnterInfoStore = this.lookupReference('gridEnterInfo').getStore();
		EnterInfoStore.currentPage = 1;
		this.getViewModel().getStore('usersResults').load();
	},

//点击添加按钮，弹出添加企业信息窗口
	onAddClick : function() {
		var win = Ext.widget('addEnterInfo');

		var startTime = new Date();
		Ext.getCmp('addEnterInfo').down('form').getForm().findField('startTime').setValue(startTime);
	    var endTime = new Date(startTime.getTime() + 30*24*60*60*1000);
		Ext.getCmp('addEnterInfo').down('form').getForm().findField('endTime').setValue(endTime);
		win.show();
	},


	showAuditInfo:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
    	var win = Ext.widget("showAuditInfo", {
			title: '审核信息',
			buttonAlign : 'center',
			companyId: rec.data.id,
			buttons : [{
				text : '关闭',
				handler: function(){
					this.up('showAuditInfo').close();
				},
			}]
		});
		win.show();
	},

	onAfterrenderShowAudit: function(){
            var companyId = Ext.getCmp("showAuditInfo").companyId;
            var input = {
            	'id':companyId,
            };
			var pram = Ext.encode(input);
			Ext.getCmp("gridAuditInfo").getViewModel().getStore("auditInfoStore").proxy.extraParams = {
				"json" : pram
			},
			Ext.getCmp("gridAuditInfo").getViewModel().getStore("auditInfoStore").load();
	},

	resourcesInfo: function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
    	var win = Ext.widget("showResourcesInfo", {
			title: '查看用车企业资源',
			buttonAlign : 'center',
			companyId: rec.data.id,
			buttons : [{
				text : '关闭',
				handler: function(){
					this.up('showResourcesInfo').close();
				},
			}]
		});
		win.show();
		var input = {
			"entId" : rec.data.id,
			"name" : rec.data.name
		};
		Ext.getCmp('searchFormVehsResources').form.findField('fromOrgId').getStore().proxy.extraParams = {
             "json":Ext.encode(input)
        }
	//	Ext.getCmp('searchFormVehsResources').form.findField('fromOrgId').getStore().proxy.url = 'vehicle/listVehicleFrom?json=' + pram;
	},

/*用车企业车辆资源信息*/
	onBeforeloadVehsResources: function(){
		var companyId = Ext.getCmp("showResourcesInfo").companyId;
		var frmValues = this.lookupReference('searchFormVehsResources').getValues();
		if(frmValues['fromOrgId']==''){
			frmValues['fromOrgId']='-1'
		}
		var page=Ext.getCmp('vehsResourcesPage').store.currentPage;
		var limit=Ext.getCmp('vehsResourcesPage').pageSize;
		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"vehicleNumber": frmValues.vehicleNumber,
				"vehicleType": frmValues.vehicleType,
				"fromOrgId":frmValues.fromOrgId,
				"deptId":companyId,
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
		var win = Ext.widget('checkVehsResourcesInfo');
		win.down("form").loadRecord(rec);
		win.show();
	},

	rentalInfo: function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
    	var win = Ext.widget("showRentalInfo", {
			title: '关联租车公司',
			buttonAlign : 'center',
			companyId: rec.data.id,
			buttons : [{
				text : '保存',
				handler: function(btn){
					var jsonArray = [];
					var unRelatedArray = [];
					var length = Ext.getCmp('gridRentalInfo').getStore().getCount();
					var store = Ext.getCmp('gridRentalInfo').getStore();
					var entId = Ext.getCmp('showRentalInfo').companyId;
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
			                	btn.up('showRentalInfo').close();
			               		Ext.Msg.alert("消息提示", '用车企业租赁信息修改成功！');
						 		Ext.getCmp("enterId").getStore('usersResults').load();
			               	}else{
			                	btn.up('showRentalInfo').close();
			                    Ext.Msg.alert("消息提示", '用车企业租赁信息修改失败！');
						 		Ext.getCmp("enterId").getStore('usersResults').load();
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
					this.up('showRentalInfo').close();
				},
			}]
		});
		win.show();
	},


	onAfterrenderShowRentalInfo: function(){
            var companyId = Ext.getCmp("showRentalInfo").companyId;
            var input = {
            	'entId':companyId,
            };
			var pram = Ext.encode(input);
			Ext.getCmp("gridRentalInfo").getViewModel().getStore("rentalInfoStore").proxy.extraParams = {
				"json" : pram
			},
			Ext.getCmp("gridRentalInfo").getViewModel().getStore("rentalInfoStore").load();
	},

	creditLimitInfo:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
    	var win = Ext.widget("showCreditLimitInfo", {
			orgId: rec.data.id,
			orgName: rec.data.name,
		});
		win.show();
	},

	onRechargeClick:function() {
		var win = Ext.widget('rechargeWindow');
		win.down("form").getForm().findField('name').setValue(Ext.getCmp('showCreditLimitInfo').orgName);
		win.show();
    },

    onBeforeloadPayHistory: function(){
		var currentPage=Ext.getCmp('payHistoryPage').store.currentPage;
		var numPerPage=Ext.getCmp('payHistoryPage').pageSize;
        var input = {
        	'orgId':Ext.getCmp('showCreditLimitInfo').orgId,
        	'currentPage':currentPage,
        	'numPerPage':numPerPage
        };
		var pram = Ext.encode(input);
		Ext.getCmp("gridPayHistory").getViewModel().getStore("payHistoryStore").proxy.extraParams = {
			"json" : pram
		}
    },

    onAfterrenderShowCreditLimitInfo: function(){    	
   		var html = '<div class="companyForCreditLimit">'+Ext.getCmp('showCreditLimitInfo').orgName+'</div>'
   		Ext.getCmp('companyForCreditLimit').setHtml(html);
    	/*获取可分配额度与总额度*/
		Ext.Ajax.request({
            url: 'organization/findOrgCreditById/' + Ext.getCmp('showCreditLimitInfo').orgId, 
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
    	if (Ext.getCmp("rechargeWindow").down('form').getForm().isValid()) {
	    	var rechargeNum = Ext.getCmp("rechargeWindow").down("form").getForm().getValues().rechargeNum;
		    var input = {
	            'orgId': Ext.getCmp('showCreditLimitInfo').orgId,
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
	               		Ext.getCmp("rechargeWindow").close();

	               		var html = '<div class="companyForCreditLimit">'+Ext.getCmp('showCreditLimitInfo').orgName+'</div>'
				   		Ext.getCmp('companyForCreditLimit').setHtml(html);
				    	/*获取可分配额度与总额度*/
						Ext.Ajax.request({
				            url: 'organization/findOrgCreditById/' + Ext.getCmp('showCreditLimitInfo').orgId, 
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
	               		Ext.getCmp("rechargeWindow").close();
	               		
	               		var html = '<div class="companyForCreditLimit">'+Ext.getCmp('showCreditLimitInfo').orgName+'</div>'
				   		Ext.getCmp('companyForCreditLimit').setHtml(html);
				    	/*获取可分配额度与总额度*/
						Ext.Ajax.request({
				            url: 'organization/findOrgCreditById/' + Ext.getCmp('showCreditLimitInfo').orgId, 
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
	    Ext.getCmp("rechargeWindow").close();
    	this.onAfterrenderShowCreditLimitInfo();
    },
});
