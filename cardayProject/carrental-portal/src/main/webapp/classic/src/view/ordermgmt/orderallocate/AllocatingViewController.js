
function getNowDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
}

function getOnlyNowDate() {
	var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
    return currentdate;
}

function parseString2Date(timeStr) {
	var planDate = new Date(timeStr.replace(/-/g,"/"));
	var seperator1 = "-";
	var month = planDate.getMonth() + 1;
	var strDate = planDate.getDate();
	if (month >= 1 && month <= 9) {
	    month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
	return  planDate.getFullYear() + seperator1 + month + seperator1 + strDate;
}

/**
 * This class is the template view for the application.
 */

var runner = new Ext.util.TaskRunner();
var task = {};

Ext.define('Admin.view.ordermgmt.orderallocate.AllocatingViewController', {
    extend: 'Ext.app.ViewController',
    alias : 'controller.allocatingViewController',
    requires: [
    ],
    
    onBeforeload : function() {
    	//console.log('+++++loadAllocatedReport++++++');
    	//所属部门
    	var frmValues=this.lookupReference('allocatingSearchForm').getValues();

    	if(''==frmValues['deptId']||null==frmValues['deptId']){
    		frmValues['deptId']='-1';
    	}
    	if(this.lookupReference('allocatingSearchForm').getForm().isValid()){
    		var parm = this.lookupReference('allocatingSearchForm').getValues();
        	var orderNo = parm.orderNo;
        	var page=Ext.getCmp('allocatingGrid').store.currentPage;
    		var limit=Ext.getCmp('allocatingGrid').store.pageSize;
        	
        	var input = {
    			    "currentPage" 	: page,
    				"numPerPage" : limit,
    				'orderNo': 	orderNo,
    				'status': 1,
    				'selfDept': parm.includeSelf || false,
    				'childDept': parm.includeChild || false,
    				'organizationId' : parm.deptId,
    			};
        	
        	input = Ext.encode(input);
        	this.getViewModel().getStore("allocatedCarReport").proxy.extraParams = {
    			"json" : input
    		}
    	}else{
        	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
        	return false;
        }
    	
    },
    
    searchAllotingOrderById : function() {
    	//搜索前将页数初始化
    	Ext.getCmp('allocatingGrid').getStore().currentPage = 1;
    	this.getViewModel().getStore("allocatedCarReport").load();
    },
    
    showAllotVehicleWindow : function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
    	//保存订单信息，分配司机界面需要用到
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
    	
    	//var id = rec.id;
    	var win = Ext.widget("allotVehicleWindow");
    	
    	var data = '[{"id":"' + rec.data.id + '","orderNo":"' + rec.data.orderNo + '","orderTimeF":"' + 
			    	rec.data.orderTimeF + '","orderUsername":"' + rec.data.orderUsername + '","vehicleType":"' +
			    	rec.data.vehicleType + '","city":"' + rec.data.city + '","planStTimeF":"' +
			    	rec.data.planStTimeF + '","passengerNum":"' + rec.data.passengerNum + '","fromPlace":"' + 
			    	rec.data.fromPlace + '","toPlace":"' +rec.data.toPlace+ '"}]';

    	Ext.getCmp("allocatedCarReportId").getViewModel().getStore("allocatedCarReport").loadData(JSON.parse(data));
    
    	//查询符合当前订单的可用车辆
    	var currentPage=Ext.getCmp('AllocatingVehicle_id').store.currentPage;
		var pageSize=Ext.getCmp('AllocatingVehicle_id').store.pageSize;
		parm = {
				'id': rec.data.id,
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
    
  //查看订单信息
    viweOrderInfo : function(grid, rowIndex, colIndex) {
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
 			var json = Ext.encode(input1);
			//订单审核信息
 			Ext.Ajax.request({
 				url : 'order/auditHistory',//?json='+ Ext.encode(input1),
 		        method : 'POST',
                params:{json:json},
 		        async: false,
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
//                params:{json:json},
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
 					
 					//rec.data.durationTime = respText.data.resultList[0].durationTime+'小时';
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
    
    //车辆分配界面，查看车辆详细信息
    viewVehicleInfo : function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.create('Admin.view.vehiclemgmt.vehicleInfomgmt.ViewVehicleInfo');
		var objectModel = new Ext.data.Model();
		var data={
				'vehicleNumber':rec.data.vehicleNumber
		}
		var json = Ext.encode(data);
		Ext.Ajax.request({
				url : 'vehicle/findVehicleInfoByVehicleNumber',
		        method : 'POST',
		        params:{json:json},
		       // defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
		        	var respText = Ext.util.JSON.decode(response.responseText);
		        	var data = respText.data;
					if (respText.status == 'success') {
		    				objectModel.data = data;
		    				win.down("form").loadRecord(objectModel);
		    				win.show();
					}
		        }
		        /*,
		        failure : function() {
		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
		        }*/
		   });
    },
    
    vehicleAlloNext : function() {
    	var record = Ext.getCmp('allocatingVehicleListGrid').getSelectionModel().getSelection();
    	if (record.length == 0) {
    		Ext.Msg.alert("提示信息", "请选择车辆！");
			return;
    	}
    	
    	//判断初始里程是否填写
    	if(record[0].data.stMileage == null ||　record[0].data.stMileage　== ""){
    		Ext.Msg.alert("提示信息", "请填写起始里程！");
			return;
    	}else{
    		var reg = /^\d{1,10}$/;
    		var regExp = new RegExp(reg);
    		if(!regExp.test(record[0].data.stMileage)){
    		　　	Ext.Msg.alert("提示信息", "请输入正确的里程（不支持小数）");	
    		　　	return;	
    		}
    	}
    	
    	if(record[0].data.factStTime == null ||　record[0].data.factStTime　== ""){
    		Ext.Msg.alert("提示信息", "请填写实际开始时间！");
			return;
    	}else{
    		var reg = /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d$/;
//    		var reg = /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d$/;
    		var regExp = new RegExp(reg);
    		if(!regExp.test(record[0].data.factStTime)){
    		　　	Ext.Msg.alert("提示信息", "时间格式不正确，正确格式为：yyyy-MM-dd HH:mm");	
    		　　	return;	
    		}
    	}
    	 	
    	var orderId = window.sessionStorage.getItem("orderId");
    	var orderNo = window.sessionStorage.getItem("orderNo");
    	var orderTimeF = window.sessionStorage.getItem("orderTimeF");
    	var orderUsername = window.sessionStorage.getItem("orderUsername");
    	var vehicleType = window.sessionStorage.getItem("vehicleType");
    	var city = window.sessionStorage.getItem("city");
    	var planStTimeF = window.sessionStorage.getItem("planStTimeF");
    	var fromPlace = window.sessionStorage.getItem("fromPlace");
    	var toPlace = window.sessionStorage.getItem("toPlace");  	
    	
    	var seatNumber = record[0].data.seatNumber;
    	var passengerNum = window.sessionStorage.getItem('passengerNum');
    	if ((seatNumber-1) < passengerNum) {
    		Ext.Msg.confirm('消息提示','该车辆的座位数小于订单的乘车人数，确定要选择该车辆吗？？？',function(btn) {
    			if (btn == 'yes') {	
    				var win = Ext.widget("allotDriverWindow",{
    					driverId: record[0].data.driverId
    				});
    				
    				//加载订单信息
    				var data = '[{"id":"' + orderId + '","orderNo":"' + orderNo + '","orderTimeF":"' + 
    		    	orderTimeF + '","orderUsername":"' + orderUsername + '","vehicleType":"' +
    		    	vehicleType + '","city":"' + city + '","planStTimeF":"' +
    		    	planStTimeF + '","fromPlace":"' + fromPlace + '","toPlace":"' + toPlace+ '"}]';
    		    	Ext.getCmp("allocatedDriverOrder_id").getViewModel().getStore("allocatedCarReport").loadData(JSON.parse(data));
    		    	
    		    	//查询符合当前订单的可用司机
    		    	var currentPage=Ext.getCmp('AllocatingDriver_id').store.currentPage;
    				var pageSize=Ext.getCmp('AllocatingDriver_id').store.pageSize;
    				var parm = {
    			    		'id': orderId,
    			    		'currentPage': currentPage,
    			    		'numPerPage': pageSize
    			    	};
    		    	parm = Ext.encode(parm);
    		    	Ext.getCmp('allocatingDriverListGrid').getViewModel().getStore("driversResults").proxy.extraParams = {
    					"json" : parm
    				};
    		    	Ext.getCmp('allocatingDriverListGrid').getViewModel().getStore("driversResults").load();
    				win.show();
    			}
    		})
    	} else {
    		var win = Ext.widget("allotDriverWindow",{
				driverId: record[0].data.driverId
			});
    		
    		//加载订单信息
			var data = '[{"id":"' + orderId + '","orderNo":"' + orderNo + '","orderTimeF":"' + 
	    	orderTimeF + '","orderUsername":"' + orderUsername + '","vehicleType":"' +
	    	vehicleType + '","city":"' + city + '","planStTimeF":"' +
	    	planStTimeF + '","fromPlace":"' + fromPlace + '","toPlace":"' + toPlace+ '"}]';
	    	Ext.getCmp("allocatedDriverOrder_id").getViewModel().getStore("allocatedCarReport").loadData(JSON.parse(data));
	    	
	    	//查询符合当前订单的可用司机
	    	var currentPage=Ext.getCmp('AllocatingDriver_id').store.currentPage;
			var pageSize=Ext.getCmp('AllocatingDriver_id').store.pageSize;
			var parm = {
		    		'id': orderId,
		    		'currentPage': currentPage,
		    		'numPerPage': pageSize
		    	};
	    	parm = Ext.encode(parm);
	    	Ext.getCmp('allocatingDriverListGrid').getViewModel().getStore("driversResults").proxy.extraParams = {
				"json" : parm
			};
	    	Ext.getCmp('allocatingDriverListGrid').getViewModel().getStore("driversResults").load();
    		win.show();
    	}  
    },
    
    checkAvialiableDriver : function (me, records, success, opts) {
        if (!success || !records || records.length == 0)
            return;

        //根据全局的选择，初始化选中的列
        var selModel = Ext.getCmp('allocatingDriverListGrid').getSelectionModel();
		for (var i = 0; i < records.length; i++) {
			var record = records[i];
			if (record.get("id") == this.getView().driverId) {
				selModel.select(record, true, true);    //选中record，并且保持现有的选择，不触发选中事件
			}
		}
    },
    
    //司机分配界面，点击上一步
    driverAlloLast : function() {
    	Ext.getCmp('allotDriverWindow').close();
    	//Ext.getCmp('allotVehicleWindow').setHidden(false);
    	//this.showAllotVehicleWindow();
    },
    
    //获取司机信息
    onBeforeLoadDriver : function() {
    	//alert('onBeforeLoadDriver');
    	console.log('++++onBeforeLoadDriver++++');
    	var orderId = window.sessionStorage.getItem('orderId');
    	currentPage = Ext.getCmp('AllocatingDriver_id').store.currentPage;
    	numPerPage = Ext.getCmp('AllocatingDriver_id').store.pageSize;
    	
//    	currentPage = Ext.getCmp('AllocatingDriverWindow_grid_id').store.currentPage;
//    	numPerPage = Ext.getCmp('AllocatingDriverWindow_grid_id').store.pageSize;
    	var parm = {
    		'id': orderId,
    		'currentPage': currentPage,
    		'numPerPage': numPerPage
    	};
    	parm = Ext.encode(parm);
    	
		this.getViewModel().getStore("driversResults").proxy.extraParams = {
			"json" : parm
		};
		this.getViewModel().getStore("driversResults").load();
    },
    
    //完成司机选择
    driverAlloDone : function() {
    	var Record = Ext.getCmp('allocatingDriverListGrid').getSelectionModel().getSelection();
    	if (Record.length == 0) {
    		Ext.Msg.alert("提示信息", "请选择司机！");
			return;
    	}    	
    	
    	var vehicleRecord = Ext.getCmp('allocatingVehicleListGrid').getSelectionModel().getSelection();
    	var driverRecord = Ext.getCmp('allocatingDriverListGrid').getSelectionModel().getSelection();
    	//订单调度需要的参数： 订单id、vehicleId、 driverId、 driverName、 driverPhone
    	//window.sessionStorage.setItem("vehicleRecord", vehicleRecord);
    	//window.sessionStorage.setItem("driverRecord", driverRecord);
    	window.sessionStorage.setItem("vehicleId", vehicleRecord[0].data.id);
    	window.sessionStorage.setItem("driverId", driverRecord[0].data.id);
    	window.sessionStorage.setItem("driverName", driverRecord[0].data.realname);
    	window.sessionStorage.setItem("driverPhone", driverRecord[0].data.phone);
    	window.sessionStorage.setItem("stationName", driverRecord[0].data.stationName);
    	
    	var rec = new Ext.data.Model();
    	rec.data.vehicleId = vehicleRecord[0].data.id;
    	rec.data.vehicleNumber = vehicleRecord[0].data.vehicleNumber;
    	rec.data.vehicleBrand = vehicleRecord[0].data.vehicleBrand;
    	rec.data.vehicleModel = vehicleRecord[0].data.vehicleModel;
    	
    	var vehicleType = vehicleRecord[0].data.vehicleType;
    	switch (vehicleType) {
    	  case '0': 
    		  vehicleType = '应急机要通信接待用车';
			  break;
		  case '1':
			  vehicleType = '行政执法用车';
			  break;
		  case '2':
			  vehicleType = '行政执法特种专业用车';
			  break;
		  case '3':
			  vehicleType = '一般执法执勤用车';
			  break;  
          case '4':
              vehicleType = '执法执勤特种专业用车';
              break; 
    	}
    	rec.data.vehicleType = vehicleType;
    	
    	rec.data.seatNumber = vehicleRecord[0].data.seatNumber;
    	rec.data.driverId =  driverRecord[0].data.id;
    	rec.data.realname =  driverRecord[0].data.realname;
    	rec.data.phone =  driverRecord[0].data.phone;
    	rec.data.stationName =  driverRecord[0].data.stationName;
    	rec.data.stMileage = vehicleRecord[0].data.stMileage;	//起始里程
    	rec.data.factStTime = vehicleRecord[0].data.factStTime;	//实际开始时间
    	
    	var win = Ext.widget("driverAllocatedWindow");
    	win.down("form").loadRecord(rec);
		win.show();
    },
    //确认车辆分配信息
    loadVehicleInfo : function() {
    	var vehicleRecord = Ext.getCmp('allocatingVehicleListGrid').getSelectionModel().getSelection();
    	var driverRecord = Ext.getCmp('allocatingDriverListGrid').getSelectionModel().getSelection();
    	/*var store = Ext.create('Ext.data.Store', {
    	    fields: ['vehicleId','vehicleNumber', 'vehicleBrand', 'vehicleModel','vehicleType', 
    	             'seatNumber', 'vehicleOutput','vehicleColor', 'driverId','realname', 'phone'],
    	    data: [
    	        { 
    	          'vehicleId': vehicleRecord[0].data.id,
    	          'vehicleNumber': vehicleRecord[0].data.vehicleNumber,
    	          'vehicleBrand': vehicleRecord[0].data.vehicleBrand,
    	          'vehicleModel': vehicleRecord[0].data.vehicleModel,
    	          'vehicleType': vehicleRecord[0].data.vehicleType,
    	          'seatNumber': vehicleRecord[0].data.seatNumber,
    	          'vehicleOutput': vehicleRecord[0].data.vehicleOutput,
    	          'vehicleColor': vehicleRecord[0].data.vehicleColor,
    	          'driverId': driverRecord[0].data.id,
    	          'realname': driverRecord[0].data.realname,
    	          'phone': driverRecord[0].data.phone
    	        }]
    	});*/
    	var rec = new Ext.data.Model();
    	rec.data.vehicleId = vehicleRecord[0].data.id;
    	rec.data.vehicleNumber = vehicleRecord[0].data.vehicleNumber;
    	rec.data.vehicleBrand = vehicleRecord[0].data.vehicleBrand;
    	rec.data.vehicleModel = vehicleRecord[0].data.vehicleModel;
    	rec.data.vehicleType = vehicleRecord[0].data.vehicleType;
    	rec.data.seatNumber = vehicleRecord[0].data.seatNumber;
    	rec.data.driverId =  driverRecord[0].data.id;
    	rec.data.realname =  driverRecord[0].data.realname;
    	rec.data.phone =  driverRecord[0].data.phone;
    	

    	
    	var win = Ext.widget("vieworder");
    	win.down("form").loadRecord(rec);
    	
    	
    	//Ext.getCmp("vehicle_driver_allocated_id").setStore(store); 
    },
    
    //确认车辆、司机已分配
    vehicleAndDriverAlloDone : function(btn) {
    	//btn.up("allocatingDriverWindow").close();
    	var orderInfo = Ext.getCmp("allocatedCarReportId").getStore().data.items[0].data;
//    	var vehicleRecord =  window.sessionStorage.getItem("vehicleRecord");
//    	var driverRecord =  window.sessionStorage.getItem("driverRecord");
    	
    	//var vehicleAndDriverInfo = Ext.getCmp("vehicle_driver_allocated_id").getStore().data.items[0].data;
    	//订单调度需要的参数： 订单id、vehicleId、 driverId、 driverName、 driverPhone
    	var vehicleId = window.sessionStorage.getItem("vehicleId");
    	var driverId = window.sessionStorage.getItem("driverId");
    	var driverName = window.sessionStorage.getItem("driverName");
    	var driverPhone = window.sessionStorage.getItem("driverPhone");
    	//添加起始里程
    	var stMileage = btn.up("window").down("form").getForm().findField("stMileage").getValue();
    	var factStTime = btn.up("window").down("form").getForm().findField("factStTime").getValue();
    	var input = {
    		'id': orderInfo.id,
    		'vehicleId': vehicleId,
			'driverId': driverId,
			'driverName': driverName,
			'driverPhone': driverPhone,
			'stMileage': stMileage,
			'factStTime': factStTime + ":00"	//加上秒
    	};
    	var json = Ext.encode(input);
    	Ext.Ajax.request({
	   		url:'order/arrange',//?json='+Ext.encode(input),
	        method : 'POST',
            params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
				if (respText.status == 'success') {
					win = Ext.widget('orderallocateddone', {
						title : '订单调度已完成',
						bodyStyle: {
							//  background: '#ffc',
							    padding: '15px 0 0 50px'  //与边界的距离
						},
						items: {
								//id : 'valueId',
								xtype : 'displayfield',
								value: '订单编号'+orderInfo.orderNo+'调度已完成！',
								lableWidth : 220,
								width : 420,
						    }
					});
					win.show();
					//调度成功，关闭对话框
					Ext.getCmp('allocatedCarReportId').up('allotVehicleWindow').close();
					Ext.getCmp('allocatingDriverListGrid').up('allotDriverWindow').close();
					Ext.getCmp('vehicle_driver_allocated_id').up('driverAllocatedWindow').close();
					
					var win1 = Ext.getCmp('orderauditdone_id');
					if (win1) {
						Ext.getCmp('orderauditdone_id').close();
					}
					
					var win2 = Ext.getCmp('orderauditwin_id');
					if (win2) {
						Ext.getCmp('orderauditwin_id').up('orderauditwin').close();
					}
					
					//重新load待调度订单
					var win3 = Ext.getCmp("allocatingGrid");
					if (win3) {
						Ext.getCmp("allocatingGrid").getStore("allocatedCarReport").load();
					}
					
					//订单审核-已审核订单页面，完成调度后，重新load该页面数据
					var win4 = Ext.getCmp("gridorderaudited_id");
					if (win4) {
						Ext.getCmp("gridorderaudited_id").getStore("ordersResults").load();
					}
				} else {
					Ext.Msg.alert('消息提示', respText.data);
				}
	        }
            /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
    },
    
    showAllotVehicleMapWindow : function() {
    	console.log('++++showAllotVehicleMapWindow++++');
    	var win = Ext.widget("allotRealtimeWindow", {
			title: '车辆分配',
			closable: true,
		});
    	win.show();
    },
    
    //获取车辆实时位置
//    onAfterRenderAllotRealtimeMap : function(panel, options) {
////    	var vehicleNum = this.getView().vehicleNum;
////    	console.log('onAfterRenderAllotRealtimeMap++vehicleNum:' + vehicleNum);
//    	var map = this.lookupReference('allotRealtimeMapPanel').bmap;
//    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
//    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
//    	map.addControl(top_left_control);        
//		map.addControl(top_left_navigation);     
//		map.addControl(new BMap.MapTypeControl({mapTypes:[BMAP_NORMAL_MAP,BMAP_SATELLITE_MAP]}));
////    	map.addControl(new BMap.MapTypeControl());
//    	
//		var imei = Ext.getCmp('allotRealtimeWindow').imei;
//		var tracetime = getNowDate();
//		
//		//定时任务
//		var offsetSideMap = function() {
//    		console.log("10秒更新实时定位");
//    		//ajax调用接口获取实时经纬度
//    		var input = {'deviceNumber' : imei};
//    		var json = Ext.encode(input);
//    		Ext.Ajax.request({
//    	   		url: 'vehicle/queryObdLocationByImei',
//    	        method : 'POST',
//    	        timeout : 3000, //超时时间设置，单位毫秒,3秒请求没有返回，则认为请求失败。
//    	        params : {json:json},
//    	        success : function(response,options) {
//    	        	var respText = Ext.util.JSON.decode(response.responseText);
//    				var data = [];
//    		    	data[0] = respText.data;
//    		    	var speed = data[0].speed;
//    		    	var longitude = data[0].longitude;
//    		    	var latitude = data[0].latitude;
//    		    	
//    		    	map.clearOverlays();
//    	        	
//    		    	console.log("*****imei******"+imei);
//    		    	console.log("*****经纬度信息******"+longitude+"*******"+latitude);
//    	    		var point = new BMap.Point(longitude, latitude);
////    		    	var point = new BMap.Point(114.414308, 30.483367);
//    	        	map.centerAndZoom(point, 15);
//    	        	var marker = new BMap.Marker(point);  // 创建标注
//    	        	map.addOverlay(marker);               // 将标注添加到地图中
//    	        	marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
//    	        	//var content = '经度：114.414318<br/>纬度：30.483377';
//    	        	var content = '时间：' + tracetime + '<br/>经度：' + longitude + '<br/>纬度：' + latitude;
//    	    		marker.addEventListener("click",function(e){
//    	    			var opts = {
//    	    					width : 250,     // 信息窗口宽度
//    	    					height: 80,     // 信息窗口高度
//    	    					title : "信息窗口" , // 信息窗口标题
//    	    					enableMessage:true//设置允许信息窗发送短息
//    	    				   };
//    	    			var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
//    	    			map.openInfoWindow(infoWindow,point); //开启信息窗口
//    	    		}
//    	    		);
//    	        },
//    	        failure : function() {
//    	        	//数据请求失败，关闭定时任务
//    	        	Ext.util.TaskManager.stop(task); 
//    	            Ext.Msg.alert('消息提示', '没有查询到该车辆的实时位置，请稍后再试！',function(){  
//    	                //关闭后执行 
//    	            	if (Ext.getCmp('allotRealtimeWindow')) {
////    	            		Ext.getCmp('allotRealtimeWindow').close();
//    	            	}
//    	            });
//    	            
//    	        },
//    	    });
//		};
//    		
//    	//定时器10秒轮询
//        var runner = new Ext.util.TaskRunner(),
//            task = runner.start({
//                 run: offsetSideMap,
//                 interval: 10000,
//        });
//    },
    
    loadAllotRealtimeReport: function() {
    	console.log('search for vehicle num for');
//    	Ext.getCmp("allotRealtimeDataGrid").getViewModel().getStore("allotVehicleRealtimeStore").proxy.url = "app/data/ordermgmt/allocatecar/allotVehicleData.json";
//    	Ext.getCmp("allotRealtimeDataGrid").getViewModel().getStore("allotVehicleRealtimeStore").reload();
    },
    
    toVehicleSchedule : function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
    	var vehicleNumber = rec.data.vehicleNumber;
    	var vehicleId = rec.data.id;
    	console.log('++++toVehicleSchedule++++');
    	var win = Ext.widget("vehicleScheduleWindow", {
			title: '车辆实时位置和任务信息——' + vehicleNumber,
			closable: true,
			buttonAlign : 'center',
			vehicleNumber: vehicleNumber,
			vehicleId : vehicleId,
			listeners:{
				close:function() {
					runner.stop(task);
				}
			}
		});
    	Ext.getCmp("scheduleTopPanel").getViewModel().set('vehicleNum', vehicleNumber);
    	Ext.getCmp("scheduleTopPanel").getViewModel().set('vehicleBoard', rec.data.vehicleBrand);
    	win.show();
    },
    
    loadScheduleInfo : function() {
    	var animals;
    	var win = Ext.getCmp("vehicleScheduleWindow");
    	var vehicleId = win.vehicleId;
    	var planStTimeF = window.sessionStorage.getItem("planStTimeF");
    	console.log('planStTimeF:' + planStTimeF);
    	var input = {
    			'vehicleId' : vehicleId,
    			'planStTimeF': planStTimeF
    	};
        var json = Ext.encode(input);
    	Ext.Ajax.request({
   		url: 'order/vehicleSchedule',//?json='+Ext.encode(input),
//   		async: false,
        params:{json:json},
        method : 'POST',
       // defaultHeaders : {'Content-type' : 'application/json;utf-8'},
        success : function(response,options) {
        	var respText = Ext.util.JSON.decode(response.responseText);
	    	var data = respText.data;
        	var retStatus = respText.status;
			if (retStatus == 'success') {
				animals = data;
				var time = parseString2Date(planStTimeF);
		    	$('#dashboardCalendar').fullCalendar({
		            defaultDate: time,
		            events:animals
		    	});
			}
        }
        /*,
        failure : function() {
            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
        },*/
    });
    	
    	/*    	$('#dashboardCalendar').fullCalendar({
            defaultDate: time,
            events:animals
            events: [
                {
                    title: '订单1001',
                    start: '2017-02-12'
                },
                {
                    id: 999,
                    title: '订单1003',
                    start: '2017-02-12T16:30:00',
                    end: '2017-02-12T17:30:00',
                    color: '#257e4a'
                },
                {
                    id: 999,
                    title: '订单1004',
                    start: '2017-02-12T16:00:00'
                },
                {
                    title: '订单1005',
                    start: '2017-02-12T06:30:00',
                    end: '2017-02-12T08:30:00',
                    color: '#ff9f89'
                },
                {
                    title: '订单1006',
                    start: '2017-02-12T10:30:00',
                    end: '2017-02-12T12:30:00'
                },
                {
                    title: '订单1007',
                    start: '2017-02-12T19:20:00',
                    end: '2017-02-12T19:50:00'
                },
                {
                    title: '订单1008',
                    start: '2017-02-12T20:20:00',
                    end: '2017-02-12T10:50:00'
                },
                {
                    title: '订单1009',
                    start: '2017-02-12T09:20:00',
                    end: '2017-02-12T11:50:00'
                },
                {
                    title: '订单1010',
                    start: '2017-02-12T12:20:00',
                    end: '2017-02-12T14:50:00'                
                },
                {
                    title: '订单1011',
                    start: '2017-02-12T15:20:00',
                    end: '2017-02-12T15:50:00'
                }
            ]
        });*/
    	
    },
    
    onResetClick : function() {
    	this.getView().getForm().reset();
    },
    
    onAfterRenderRealtimeMap: function() {
    	var win = Ext.getCmp("vehicleScheduleWindow");
    	var vehicleId = win.vehicleId;
    	console.log('++++onAfterRenderRealtimeMap++++vehicleId++++' + vehicleId);
    	var map = this.lookupReference('realtimeMapPanel').bmap;
    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    	map.addControl(top_left_control);        
		map.addControl(top_left_navigation);     
    	map.addControl(new BMap.MapTypeControl());
    	map.clearOverlays();
    	var tracetime = getNowDate();
    	
    	
    	var input = {'vehicleId' : vehicleId};
    	var json = Ext.encode(input);
		Ext.Ajax.request({
//	   		url: 'vehicle/queryObdLocationByVehicleId?json=' + Ext.encode(input),
	   		url: 'vehicle/queryObdLocationByVehicleId',
	        method : 'POST',
//	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        params:{json:json},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
				var data = [];
		    	if(respText.data == '') {
		    		console.log('data is null');
		    		function myFun(result){
                		var cityName = result.name;
                		console.log('initView:' + cityName);
                		map.setCenter(cityName);
                		map.centerAndZoom(cityName,12);
                    	}
	                	var myCity = new BMap.LocalCity();
	                	myCity.get(myFun);
		    	} else {
		    		data[0] = respText.data;
		    	
			    	//地图定位
		    		map.clearOverlays();
		    		var point = new BMap.Point(data[0].longitude, data[0].latitude);
		        	map.centerAndZoom(point, 15);
		        	var speed = data[0].speed;
		        	var myIcon;
		    		if (typeof(speed) == undefined || speed == '0') {
		    			myIcon = new BMap.Icon("resources/images/icons/icon_bussiness_offline.png", new BMap.Size(60,50),{    //小车图片
		    				imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
		    			});
		    		}else{
		    			myIcon = new BMap.Icon("resources/images/icons/icon_home_drivinglocated.png", new BMap.Size(60,50),{    //小车图片
		    				imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
		    			});
		    		}
	
		        	var marker = new BMap.Marker(point,{icon:myIcon});
	//	        	var marker = new BMap.Marker(point);  // 创建标注
		        	map.addOverlay(marker);               // 将标注添加到地图中
	//	        	marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
		        	var opts = {
/*							width : 230,     // 信息窗口宽度
							height: 120,     // 信息窗口高度
	//						title : "信息窗口" , // 信息窗口标题*/
							enableMessage:true//设置允许信息窗发送短息
		        	};
		        	
		        	
		        	var geoc = new BMap.Geocoder();
		        	var address = '';
		        	//逆地址解析
		    		geoc.getLocation(point, function(rs){
		    			var addComp = rs.addressComponents;
		    			if(addComp.province == '北京市') {
		    				address = addComp.province + addComp.district + addComp.street + addComp.streetNumber;
		    			} else {
		    				address = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
		    			}
		    			var content = '时间：' + tracetime + '<br/>状态：' + data[0].status + '<br/>经度：' + data[0].longitude + '<br/>纬度：' + data[0].latitude + '<br/>'
	    				+ '速度：约' + data[0].speed + 'km/h<br/>地址：' + address;
	    	
						var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
						map.openInfoWindow(infoWindow,point); //开启信息窗口
			    		marker.addEventListener("click",function(e){
			    			var opts = {
/*			    					width : 230,     // 信息窗口宽度
			    					height: 120,     // 信息窗口高度
			//    					title : "信息窗口" , // 信息窗口标题*/
			    					enableMessage:true//设置允许信息窗发送短息
			    				   };
			    			var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
			    			map.openInfoWindow(infoWindow,point); //开启信息窗口
			    		}
			    		);
		    		});
		    	}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
    },
    loadTimerElement : function() {
    	var win = Ext.getCmp("vehicleScheduleWindow");
    	var vehicleNumber = win.vehiclenumber;
    	var vehicleId = win.vehicleId;
    	var i = 10;
//    	var imei = '898602B5091581519192';
    	
    	//定义地图控件
    	var map = this.lookupReference('realtimeMapPanel').bmap;
    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    	map.addControl(top_left_control);        
		map.addControl(top_left_navigation);     
    	map.addControl(new BMap.MapTypeControl());
    	
    	var resetMkPoint = function() {
    		console.log('i::' + i);
    		if(i > 1){
				i--;
			}else {
				offsetSideMap(vehicleId);
//				loadVehicleCountInfo();
				i = 10;
			}
    		Ext.getCmp("scheduleTopPanel").getViewModel().set('secondes', i);
    	};
    	
    	task = {  
    		    run: resetMkPoint,  
    		    interval: 1000 //1 second  
    		};
    	
    	runner.start(task);
    	
    	
    	//定时任务
    	var offsetSideMap = function(vehicleId) {
    		console.log("10秒更新实时定位");
    		var tracetime = getNowDate();
    		console.log('tracetime:' + tracetime);
    		//ajax调用接口获取实时经纬度
    		var input = {'vehicleId' : vehicleId};
    		var json = Ext.encode(input);
    		Ext.Ajax.request({
//    	   		url: 'vehicle/queryObdLocationByVehicleId?json=' + Ext.encode(input),
    	   		url: 'vehicle/queryObdLocationByVehicleId',
    	        method : 'POST',
//    	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
    	        params:{json:json},
    	        success : function(response,options) {
    	        	var respText = Ext.util.JSON.decode(response.responseText);
    				var data = [];
    				
    				if(respText.data == '') {
    					console.log('data is null');
    					function myFun(result){
                    		var cityName = result.name;
                    		console.log('initView:' + cityName);
                    		map.setCenter(cityName);
                    		map.centerAndZoom(cityName,12);
                        	}
    	                	var myCity = new BMap.LocalCity();
    	                	myCity.get(myFun);
    				}else {
    					data[0] = respText.data;
        		    	
        		    	//地图定位
        	    		map.clearOverlays();
        	    		var point = new BMap.Point(data[0].longitude, data[0].latitude);
        	        	map.centerAndZoom(point, 15);
        	        	var speed = data[0].speed;
        	        	var myIcon;
    		    		if (typeof(speed) == undefined || speed == '0') {
    		    			myIcon = new BMap.Icon("resources/images/icons/icon_bussiness_offline.png", new BMap.Size(60,50),{    //小车图片
    		    				imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
    		    			});
    		    		}else{
    		    			myIcon = new BMap.Icon("resources/images/icons/icon_home_drivinglocated.png", new BMap.Size(60,50),{    //小车图片
    		    				imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
    		    			});
    		    		}

        	        	var marker = new BMap.Marker(point,{icon:myIcon});
//        	        	var marker = new BMap.Marker(point);  // 创建标注
        	        	map.addOverlay(marker);               // 将标注添加到地图中
//        	        	marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
        	        	var opts = {
/*        						width : 230,     // 信息窗口宽度
        						height: 120,     // 信息窗口高度
//        						title : "信息窗口" , // 信息窗口标题*/
        						enableMessage:true//设置允许信息窗发送短息
        	        	};
        	        	
        	        	
        	        	var geoc = new BMap.Geocoder();
        	        	var address = '';
        	        	//逆地址解析
        	    		geoc.getLocation(point, function(rs){
        	    			var addComp = rs.addressComponents;
        	    			address = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
        	    			var content = '时间：' + tracetime + '<br/>状态：' + data[0].status + '<br/>经度：' + data[0].longitude + '<br/>纬度：' + data[0].latitude + '<br/>'
            				+ '速度：约' + data[0].speed + 'km/h<br/>地址：' + address;
            	
    						var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
    						map.openInfoWindow(infoWindow,point); //开启信息窗口
    			    		marker.addEventListener("click",function(e){
    			    			var opts = {
/*    			    					width : 230,     // 信息窗口宽度
    			    					height: 120,     // 信息窗口高度
    			//    					title : "信息窗口" , // 信息窗口标题*/
    			    					enableMessage:true//设置允许信息窗发送短息
    			    				   };
    			    			var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
    			    			map.openInfoWindow(infoWindow,point); //开启信息窗口
    			    		}
    			    		);
        	    		});
    				}
    	        }
    	        /*,
    	        failure : function() {
    	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
    	        },*/
    	    });
    	};
    },
	/******多级部门筛选*******/
	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.ordermgmt.orderallocate.DeptChooseWin",{
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
     	var form = Ext.getCmp("allocatingsearchform_id").getForm();
     	form.findField("deptId").setValue(deptId);
//     	form.findField("organizationId").setDisplayField(deptName);
     	form.findField("deptName").setValue(deptName);
     	btn.up("window").close();
     },
    
});

