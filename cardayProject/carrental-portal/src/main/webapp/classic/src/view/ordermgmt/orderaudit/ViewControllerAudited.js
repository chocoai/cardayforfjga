/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderaudit.ViewControllerAudited', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
	        'Admin.view.ordermgmt.orderaudit.AuditedView',
	        'Admin.view.ordermgmt.orderaudit.GridOrderAudited',	        
	        'Admin.view.ordermgmt.orderaudit.SearchFormAudited',
			],
	alias : 'controller.orderauditedcontroller',
	
	onBeforeLoad : function() {
		//所属部门
    	var frmValues=this.lookupReference('SearchFormAudited').getValues();

    	if(''==frmValues['deptId']||null==frmValues['deptId']){
    		frmValues['deptId']='-1';
    	}
    	if(this.lookupReference('SearchFormAudited').getForm().isValid()){
    		var parm = this.lookupReference('SearchFormAudited').getValues();
    		var status = parm.status;
    		if (status == '全部' || status == '-1') {
    			status = '';
    		}
    		
    		var page=Ext.getCmp('orderauditedpage_id').store.currentPage;
    		var limit=Ext.getCmp('orderauditedpage_id').store.pageSize;

    		parm = {
    			    "currentPage" 	: page,
    				"numPerPage" : limit,
    				'orderNo': 	parm.orderNo,
    				'status': status,
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
		var VehicleStore = Ext.getCmp('gridorderaudited_id').getStore();
		VehicleStore.currentPage = 1;
		this.getViewModel().getStore("ordersResults").load();
	},
	
	onResetClick : function() {
		this.getView().getForm().reset();
	},
	
	//已排车，查看车辆司机分配信息
	queryOrderVehicleDriverInfo : function(grid, rowIndex, colIndex) {
    	console.log('+++++queryOrderVehicleDriverInfo++++');
    	var orderInfo = grid.getStore().getAt(rowIndex);
    	var driverName = orderInfo.data.driverName;
    	var driverPhone = orderInfo.data.driverPhone;
    	var driverSource = orderInfo.data.orgName;  //司机所在部门就是司机来源，与当前订单部门一致
    	var vehicleId = orderInfo.data.vehicleId;
    	var vehicleType = orderInfo.data.vehicleType;
    	var stationName = orderInfo.data.stationName;
    	var driverStationName = '';
    	if (stationName != null) {
    		driverStationName = orderInfo.data.stationName;
    	} else {
    		driverStationName = '暂未分配';
    	}
    	switch(vehicleType) {
    		case '0':
    			vehicleType='应急机要通信接待用车';
    			break;
    		case '1':
    			vehicleType='行政执法用车';
    			break;
    		case '2':
    			vehicleType='行政执法特种专业用车';
    			break;
    		case '3':
    			vehicleType='一般执法执勤用车';
    			break;
            case '3':
                vehicleType='执法执勤特种专业用车';
                break;
    	}
    	
//    	var url='vehicle/'+vehicleId+'/update';   	
    	var url='vehicle/monitor/'+vehicleId+'/update';
    	
    	Ext.Ajax.request({
	   		url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var rec = new Ext.data.Model();
	        	rec.data = respText.data
	        	rec.data.driverName = driverName;
	        	rec.data.driverPhone = driverPhone;
	        	rec.data.vehicleType = vehicleType;
	        	rec.data.driverStationName = driverStationName;
	        	rec.data.driverSource = driverSource;
	        	
				if (respText.status == 'success') {	
					var win = Ext.widget("queryOrderVehicleDriverView");
					win.down("form").loadRecord(rec);
					win.show();	
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
    	});
    },
    
    //待排车状态，去排车
    showAllotVehicleWindow : function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
    	window.sessionStorage.setItem("orderId", rec.data.id);
    	window.sessionStorage.setItem("orderNo", rec.data.orderNo);
    	window.sessionStorage.setItem("orderTimeF", rec.data.orderTimeF);
    	window.sessionStorage.setItem("orderUsername", rec.data.orderUsername);
    	window.sessionStorage.setItem("vehicleType", rec.data.vehicleType);
    	window.sessionStorage.setItem("city", rec.data.city);
    	window.sessionStorage.setItem("planStTimeF", rec.data.planStTimeF);
    	window.sessionStorage.setItem("fromPlace", rec.data.fromPlace);
    	window.sessionStorage.setItem("toPlace", rec.data.toPlace);
    	window.sessionStorage.setItem("passengerNum", rec.data.passengerNum);
    	var win = Ext.widget("allotVehicleWindow", {
			title: '车辆分配',
			closable: true,
			//buttonAlign : 'center',
		});
    	
    	var data = '[{"id":"' + rec.data.id + '","orderNo":"' + rec.data.orderNo + '","orderUsername":"' + 
    				rec.data.orderUsername + '","planStTimeF":"' + rec.data.planStTimeF + '","orderUserphone":"' + 
    				rec.data.orderUserphone+ '","orderTimeF":"' + rec.data.orderTimeF + '","vehicleType":"' + 
    				rec.data.vehicleType + '","passengerNum":"' + rec.data.passengerNum + '","city":"' + 
    				rec.data.city + '","fromPlace":"' + rec.data.fromPlace + '","toPlace":"' + 
    				rec.data.toPlace + '"}]';
    	
    	Ext.getCmp("allocatedCarReportId").getViewModel().getStore("allocatedCarReport").loadData(JSON.parse(data));
    
    	//查询符合当前订单的可用车辆
    	var currentPage=Ext.getCmp('AllocatingVehicle_id').store.currentPage;
		var pageSize=Ext.getCmp('AllocatingVehicle_id').store.pageSize;
		parm = {
				'id':rec.data.id,
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
    
    //车辆跟踪
    vehicleTrack : function(grid, rowIndex, colIndex) {
    	var orderInfo = grid.getStore().getAt(rowIndex);
    	
    	var vehicleId = orderInfo.data.vehicleId;
    	var url='vehicle/'+vehicleId+'/update';
    	var vehicleNumber = '';
    	Ext.Ajax.request({
	   		url: url,
	        method : 'GET',
	        async: false,//同步请求，ajax请求完毕后，才能继续执行后面的代码
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	vehicleNumber = respText.data.vehicleNumber;
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
    	});	

    	var win = Ext.widget("allotRealtimeWindow", {
//			title: '车辆追踪——'+vehicleNumber+'——窗口将在10秒后刷新',
    		imei : orderInfo.data.deviceNumber,
    		vehicleNumber : vehicleNumber,
    		factStTimeF : orderInfo.data.factStTimeF,
			closable: true,
			buttonAlign : 'center',
		});
    	win.show();
    },
    
    //车辆跟踪
    vehicleTrace: function(grid, rowIndex, colIndex) {
    	//console.log('++++showAllocatedTraceWindow++++');
    	var orderInfo = grid.getStore().getAt(rowIndex);
    	var factEdTimeF = orderInfo.data.factEdTimeF;
    	var factStTimeF = orderInfo.data.factStTimeF;
    	var factMileage = orderInfo.data.factMileage;

    	var vehicleId = orderInfo.data.vehicleId;
    	var url='vehicle/'+vehicleId+'/update';
    	var vehicleNumber = '';
    	Ext.Ajax.request({
	   		url: url,
	        method : 'GET',
	        async: false,//同步请求，ajax请求完毕后，才能继续执行后面的代码
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	vehicleNumber = respText.data.vehicleNumber;
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
    	});	
    	
    	var win = Ext.widget("allocatedTraceWindow", {
			title: '订单车辆轨迹——'+vehicleNumber,
			closable: true,
			buttonAlign : 'center',
			imei : orderInfo.data.deviceNumber,
			factStTimeF : factStTimeF,
			factEdTimeF : factEdTimeF,
			factMileage : factMileage,
		});
    	win.show();
    },
    
  //订单详细信息查看
	viweOrder : function(grid, rowIndex, colIndex) {
		var orderInfo = grid.getStore().getAt(rowIndex);
		var orderNo = orderInfo.data.orderNo;
		
		var auditUserName;
		var auditUserPhone;
		var auditStatus;
		var auditTime;
		
		//待审核、补录的订单没有审核信息
		if ((orderInfo.data.status!=0)&&(orderInfo.data.planStTimeF!=null)) {
			//alert('有审核信息的订单')
			var input1 = {
 	 				'id': 	orderInfo.data.id,
 	 			};
 			
			//订单审核信息
			var json = Ext.encode(input1);
 			Ext.Ajax.request({
 				url : 'order/auditHistory',//?json='+ Ext.encode(input1),
 		        method : 'POST',
 		        async: false,
        		params:{json:json},
 		        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
 		        success : function(response,options) {
 					var respText = Ext.util.JSON.decode(response.responseText);
					//只显示第一条审核信息（后台已经做排序，第一条就是最新的）
 					auditUserName = respText.data[0].auditUserName;
					auditUserPhone = respText.data[0].auditUserPhone;
					auditStatus = respText.data[0].status;
					if (auditStatus == 1) {
						auditStatus = '审核通过';
					} else if (auditStatus == 5) {
						auditStatus = '被驳回';
					}
					auditTime = respText.data[0].auditTimeF;
 		        },
// 		        failure : function() {
// 		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
// 		        },
 		        scope:this
 			});
		}
		

 			Ext.Ajax.request({
// 				url : 'order',//?json='+ Ext.encode(input),
 				url: "order/"+orderInfo.data.id+'/queryBusiOrderByOrderNo',
 		        method : 'GET',
	        	params:{json:json},
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
 					rec.data.auditUserName = auditUserName;
 					rec.data.auditUserPhone = auditUserPhone;
 					rec.data.auditStatus = auditStatus;
 					rec.data.auditTime = auditTime;
 					
 					if (rec.data.returnType == 0) {
 						rec.data.returnType = '是';
 					} else if (rec.data.returnType == 1) {
 						rec.data.returnType = '否';
 					}
 					
 					var win = Ext.widget("vieworder");
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
// 		        failure : function() {
// 		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
// 		        },
 		        scope:this
 			});
	},
	/******多级部门筛选*******/
	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.ordermgmt.orderaudit.DeptChooseWinForAudited",{
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
     	var form = Ext.getCmp("orderaudit_searchformaudited_id").getForm();
     	form.findField("deptId").setValue(deptId);
//     	form.findField("organizationId").setDisplayField(deptName);
     	form.findField("deptName").setValue(deptName);
     	btn.up("window").close();
     },
});
