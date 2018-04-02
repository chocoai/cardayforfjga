/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderaudit.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
	        'Admin.view.ordermgmt.orderaudit.AuditView',
	        'Admin.view.ordermgmt.orderaudit.GridOrderAudit',	        
	        'Admin.view.ordermgmt.orderaudit.SearchFormAudit',
			],
	alias : 'controller.orderauditcontroller',
	
	
	onBeforeLoad : function() {
		//所属部门
    	var frmValues=this.lookupReference('SearchFormAudit').getValues();

    	if(''==frmValues['deptId']||null==frmValues['deptId']){
    		frmValues['deptId']='-1';
    	}
    	if(this.lookupReference('SearchFormAudit').getForm().isValid()){
    		var parm = this.lookupReference('SearchFormAudit').getValues();
    		
    		var page=Ext.getCmp('orderauditpage').store.currentPage;
    		var limit=Ext.getCmp('orderauditpage').store.pageSize;
    		
    		
    		parm = {
    			    "currentPage" 	: page,
    				"numPerPage" : limit,
    				'orderNo': 	parm.orderNo,
    				'status': '0',//只查询待审核状态订单
    				'orderTime': parm.orderTime,
    				'planTime': parm.planTime,
    				'selfDept': parm.includeSelf || false,
    				'childDept': parm.includeChild || false,
    				'organizationId' : parm.deptId,
    			};
    		
    		parm = Ext.encode(parm);		
    		this.getViewModel().getStore("ordersResults").proxy.extraParams = {
    			"json" : parm
    		}
    	}else{
        	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
        	return false;
        }
		
	},	
	
	onSearchClick : function() {
		var VehicleStore = Ext.getCmp('gridorderaudit_id').getStore();
		VehicleStore.currentPage = 1;
		this.getViewModel().getStore("ordersResults").load();
	},
	
	onResetClick : function() {
		this.getView().getForm().reset();
	},
	
	//待审核订单查看
	viweOrder : function(grid, rowIndex, colIndex) {
		var orderInfo = grid.getStore().getAt(rowIndex);
		var orderNo = orderInfo.data.orderNo;
		
		Ext.Ajax.request({
//			url : 'order',//?json='+ Ext.encode(input),
			url: "order/"+orderInfo.data.id+'/queryBusiOrderByOrderNo',
	        method : 'GET',
//	        params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
					var rec = new Ext.data.Model();
					rec.data = respText.data;
					
					var durationTime = respText.data.durationTime;
					var durationHour = parseInt(durationTime/60);
					var durationMinute = durationTime % 60;
				
					var waitTime = respText.data.waitTime;
					var waitHour = parseInt(waitTime/60);
					var waitMinute = waitTime % 60;
				
					rec.data.durationTime = durationHour+'小时' + durationMinute + '分钟';
					rec.data.waitTime = waitHour+'小时' + waitMinute + '分钟';
//					rec.data.auditUserName = auditUserName;
//					rec.data.auditUserPhone = auditUserPhone;
//					rec.data.auditStatus = auditStatus;
//					rec.data.auditTime = auditTime;
					
					if (rec.data.returnType == 0) {
						rec.data.returnType = '是';
					} else if (rec.data.returnType == 1) {
						rec.data.returnType = '否';
					}
				
					//待审批、补录的订单，没有审批信息
 					if ((orderInfo.data.status==0)||(orderInfo.data.planStTimeF==null)) {
						console.log("进入了if")
 						var win = Ext.widget("vieworder");
 					} else { //其他类型的订单，有审批信息
                        console.log("进入了else")
 						var win = Ext.widget("vieworder",{
 							// height: 800,
 							// scrollable: true,
 							});
 					}
 					
 					if (respText.status == 'success') {						
 						win.down("form").loadRecord(rec);
 						win.show();
 						
 						//往返，显示等待时长， 不往返，不显示等待时长
 						if (rec.data.returnType == '是') {
 	 						Ext.getCmp('view_order_wait_time_id').setHidden(false);
 	 					} else if (rec.data.returnType == '否') {
 	 						Ext.getCmp('view_order_wait_time_id').setHidden(true);
 	 					}
 						
 						//被驳回的订单，需要显示驳回原因
 						if (rec.data.status=='5') {
 							Ext.getCmp('refuseComments_id').setHidden(false);
 						} else {
 							Ext.getCmp('refuseComments_id').setHidden(true);
 						}
 						
 						if ((orderInfo.data.status==0)||(orderInfo.data.planStTimeF==null)) {
 							Ext.getCmp('viewOrder_audit_id').setHidden(true);
 							Ext.getCmp('auditUserName_id').setHidden(true);
 							Ext.getCmp('auditUserPhone_id').setHidden(true);
 							Ext.getCmp('auditStatus_id').setHidden(true);
 							Ext.getCmp('auditTime_id').setHidden(true);
 						}
				}
	        },
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	        scope:this
		});
	},
	
	//订单审核
	orderAudit : function(grid, rowIndex, colIndex) {
		var orderInfo = grid.getStore().getAt(rowIndex);
		var orderNo = orderInfo.data.orderNo;
		
		Ext.Ajax.request({
			//url : 'order',//?json='+ Ext.encode(input),
			url: "order/"+orderInfo.data.id+'/queryBusiOrderByOrderNo',
			method : 'GET',
	        //params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var rec = new Ext.data.Model();
				rec.data = respText.data;
				rec.data.orderNo1 = rec.data.orderNo;
				rec.data.orderTimeF1 = rec.data.orderTimeF;
				rec.data.orderUser1 = rec.data.orderUsername;
				rec.data.orderUserphone1 = rec.data.orderUserphone;
				rec.data.planStTimeF1 = rec.data.planStTimeF;
				rec.data.planEdTimeF1 = rec.data.planEdTimeF;
				var win = Ext.widget("orderauditwin");
				if (respText.status == 'success') {						
					win.down("form").loadRecord(rec);
					win.show();
				}
	        },
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	        scope:this
		});
	},
	

	//完成订单审核
	orderAuditDone : function(btn1) {
		var orderInfo = this.getView().down('form').getValues();
		orderInfo.status = '1';//审核通过的订单，状态由待审核（0）变成待排车（1）
		var orderNo = orderInfo.orderNo1;
		
		var msg = '订单'+orderNo+'已审核通过';
		var phone = parseInt(orderInfo.orderUserphone1);
		//phone = '18717126513';
		var sendMsg = {
			"sendMsg": msg,
			"phoneNumber": phone
		};
		orderInfo.sendMsg = sendMsg;
		
		Ext.Msg.confirm('消息提示','确认审核通过？',function(btn){
			if (btn == 'yes') {
				//btn1.up('orderauditwin').close();
				var json = Ext.encode(orderInfo);
				Ext.Ajax.request({
					url : 'order/audit',//?json='+ Ext.encode(orderInfo),
			        method : 'POST',
	                params:{json:json},
			        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			        success : function(response,options) {
						var respText = Ext.util.JSON.decode(response.responseText);
						if (respText.status == 'success') {
							win = Ext.widget('orderauditdone', {
								title : '审核已完成',
								bodyStyle: {
									//  background: '#ffc',
									    padding: '15px 0 0 50px'  //与边界的距离
								},
								items: {
										//id : 'valueId',
										xtype : 'displayfield',
										value: '订单编号'+orderNo+'已审核通过！',
										lableWidth : 220,
										width : 420,
								    }
							});
							win.show();
							//审核結束，給手機發送消息
							var json = Ext.encode(sendMsg);
							Ext.Ajax.request({
								url : 'order/auditSendMsg',//?json='+ Ext.encode(sendMsg),
						        method : 'POST',
	        					params:{json:json},
						        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
						        success : function(response,options) {
									var respText = Ext.util.JSON.decode(response.responseText);
									
						        }
	        					/*,
						        failure : function() {
						            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
						        },*/
							});
							
						}
						Ext.getCmp('gridorderaudit_id').getStore("ordersResults").load();
			        },			    	
//			        failure : function() {
//			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			        },
			        scope:this
				});
			}
		})
	},
	
	//订单审核完成，去排车
	orderAllocate : function() {
		//var rec = grid.getStore().getAt(rowIndex);
		var rec = Ext.getCmp('orderauditwin_id').getForm().getValues();
		//保存订单信息，车辆分配，司机分配要展示订单信息
		window.sessionStorage.setItem("orderId", rec.id);
    	window.sessionStorage.setItem("orderNo", rec.orderNo1);
    	window.sessionStorage.setItem("orderTimeF", rec.orderTimeF1);
    	window.sessionStorage.setItem("orderUsername", rec.orderUser1);
    	window.sessionStorage.setItem("vehicleType", rec.vehicleType);
    	window.sessionStorage.setItem("city", rec.city);
    	window.sessionStorage.setItem("planStTimeF", rec.planStTimeF1);
    	window.sessionStorage.setItem("fromPlace", rec.fromPlace);
    	window.sessionStorage.setItem("toPlace", rec.toPlace);
    	window.sessionStorage.setItem("passengerNum", rec.passengerNum);
    	
    	var win = Ext.widget("allotVehicleWindow", {
			title: '车辆分配',
			closable: true,
			//buttonAlign : 'center',
		});
    	
    	var data = '[{"id":"' + rec.id + '","orderNo":"' + rec.orderNo1 + '","orderTimeF":"' + 
			    	rec.orderTimeF1 + '","orderUsername":"' + rec.orderUser1 + '","vehicleType":"' +
			    	rec.vehicleType + '","city":"' + rec.city + '","passengerNum":"' + rec.passengerNum + '","planStTimeF":"' +
			    	rec.planStTimeF1 + '","fromPlace":"' + rec.fromPlace + '","toPlace":"' +rec.toPlace+ '"}]';
    	Ext.getCmp("allocatedCarReportId").getViewModel().getStore("allocatedCarReport").loadData(JSON.parse(data));
    	
    	//查询符合当前订单的可用车辆
    	var currentPage=Ext.getCmp('AllocatingVehicle_id').store.currentPage;
		var pageSize=Ext.getCmp('AllocatingVehicle_id').store.pageSize;
		parm = {
				'id': rec.id,
				'currentPage': currentPage,
			    'numPerPage': pageSize,
			};
		parm = Ext.encode(parm);		
		Ext.getCmp('allocatingVehicleListGrid').getViewModel().getStore("allocatingVehicleReport").proxy.extraParams = {
			"json" : parm
		};
		Ext.getCmp("allocatingVehicleListGrid").getViewModel().getStore("allocatingVehicleReport").load();
    	win.show();
	},
	
	//订单驳回
	orderRefuse: function(grid, rowIndex, colIndex) {
		var orderInfo = grid.getStore().getAt(rowIndex);
		win = Ext.widget('orderrefusewin');
		win.down("form").loadRecord(orderInfo);
		win.show();
	},
	
	//完成订单驳回
	orderRefuseDone : function(btn1) {
		var orderInfo = this.getView().down('form').getValues();
		orderInfo.status = '5';//被驳回的订单，状态由待审核（0）变成被驳回（5）
		var orderNo = orderInfo.orderNo;
		
		var msg = '订单'+orderNo+'被驳回';
		var phone = parseInt(orderInfo.orderUserphone);
		//phone = 18717126513;
		var sendMsg = {
			"sendMsg": msg,
			"phoneNumber": phone
		};
		orderInfo.sendMsg = sendMsg;
		
		Ext.Msg.confirm('消息提示','确认驳回该订单？',function(btn){
			if (btn == 'yes') {
				btn1.up('orderrefusewin').close();
				var json = Ext.encode(orderInfo);
				Ext.Ajax.request({
					url : 'order/audit',//?json='+ Ext.encode(orderInfo),
			        method : 'POST',
	        		params:{json:json},
			        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			        success : function(response,options) {
						var respText = Ext.util.JSON.decode(response.responseText);
						if (respText.status == 'success') {
							win = Ext.widget('orderrefusedone', {
								title : '订单已驳回',
								items: {
										//id : 'valueId',
										xtype : 'displayfield',
										value: '订单编号'+orderNo+'申请已驳回！',
										lableWidth : 220,
										width : 420,
										style:'margin-left: 50px'
								    }
							});
							win.show();
							//訂單駁回，給手機發送消息
							var json = Ext.encode(sendMsg);
							Ext.Ajax.request({
								url : 'order/auditSendMsg',//?json='+ Ext.encode(sendMsg),
						        method : 'POST',
	        					params:{json:json},
						        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
						        success : function(response,options) {
									var respText = Ext.util.JSON.decode(response.responseText);
									
						        }
	        					/*,
						        failure : function() {
						            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
						        },*/
							});
						}
						Ext.getCmp('gridorderaudit_id').getStore("ordersResults").load();
			        },
			    	
//			        failure : function() {
//			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			        },
			        scope:this
				});
			}
		})
	},
	/******多级部门筛选*******/
	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.ordermgmt.orderaudit.DeptChooseWin",{
     	});
     	win.down("treepanel").getStore().load();
     	win.show();
     },
     
     chooseDept: function(btn, e, eOpts){
     	var tree = btn.up("window").down("treepanel");
     	var selection = tree.getSelectionModel().getSelection();
     	if(selection.length == 0){
     		Ext.Msg.alert('提示', '请选择部门！');
     		return;
     	}
     	var select = selection[0].getData();
 		var deptId = select.id;
 		var deptName = select.text;
     	var form = Ext.getCmp("orderaudit_searchformaudit_id").getForm();
     	form.findField("deptId").setValue(deptId);
//     	form.findField("organizationId").setDisplayField(deptName);
     	form.findField("deptName").setValue(deptName);
     	btn.up("window").close();
     },
});
