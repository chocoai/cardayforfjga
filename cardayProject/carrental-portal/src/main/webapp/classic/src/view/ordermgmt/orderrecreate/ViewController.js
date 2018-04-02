/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderrecreate.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
	        'Admin.view.ordermgmt.orderrecreate.GridOrderList',
	        'Admin.view.ordermgmt.orderrecreate.View',
	        'Admin.view.ordermgmt.orderrecreate.AddOrder',
	        'Admin.view.ordermgmt.orderrecreate.SearchForm',
	        'Admin.view.ordermgmt.orderrecreate.ViewOrder'
			],
	alias : 'controller.orderrecreatecontroller',
	
	//每次load数据会调用该方法
	onBeforeLoad : function() {
		//所属部门
		var frmValues=this.lookupReference('searchForm').getValues();
		if(''==frmValues['deptId']||null==frmValues['deptId']){
    		frmValues['deptId']='-1';
    	}
		if(this.lookupReference('searchForm').getForm().isValid()){
			var page=Ext.getCmp('recreateorderlistpage').store.currentPage;
			var limit=Ext.getCmp('recreateorderlistpage').store.pageSize;
			var parm = this.lookupReference('searchForm').getValues();
			parm = {
				    "currentPage" 	: page,
					"numPerPage" : limit,
					'orderNo': 	parm.orderNo,
					'status': '', //查询所有订单
					'orderCat' : 2,//2 表示补录订单
				    'organizationId': parm.organizationId,
					'orderTime': parm.orderTime,
					'planTime': '',
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
	
	//查询
	onSearchClick : function() {
		//每次查询，从第一页开始查询
		Ext.getCmp('recreateorderlistpage').getStore().currentPage = 1;
		this.getViewModel().getStore("ordersResults").load();
	},
	
	//重置
	onResetClick : function() {
		this.getView().getForm().reset();
	},
	
	//查看订单信息
	viweOrder : function(grid, rowIndex, colIndex) {
		var orderInfo = grid.getStore().getAt(rowIndex);
		var orderNo = orderInfo.data.orderNo;

 			Ext.Ajax.request({
 				//url : 'order',//?json='+ Ext.encode(input),
 				url: "order/"+orderInfo.data.id+'/queryBusiOrderByOrderNo',
 		        method : 'GET',
//        		params:{json:json},
 		        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
 		        success : function(response,options) {
 					var respText = Ext.util.JSON.decode(response.responseText);
 					var rec = new Ext.data.Model();
 					rec.data = respText.data;
 					rec.data.organizationName = window.sessionStorage.getItem('organizationName');
 					var waitTime = respText.data.waitTime;
 					var waitHour = parseInt(waitTime/60);
 					var waitMinute = waitTime % 60;
 					rec.data.waitTime = waitHour+'小时' + waitMinute + '分钟';
 					
 					var win = Ext.widget("orderrecreatewin");
 					if (respText.status == 'success') {						
 						win.down("form").loadRecord(rec);
 						
 						//往返，显示等待时长， 不往返，不显示等待时长
 						if (rec.data.returnType == '0') {
 	 						Ext.getCmp('recreate_view_order_wait_time_id').setHidden(false);
 	 					} else if (rec.data.returnType == '1') {
 	 						Ext.getCmp('recreate_view_order_wait_time_id').setHidden(true);
 	 					}
 						
 						var orderAttach = rec.data.orderAttach;
 						if (orderAttach !=null && orderAttach !="") {
 							var url=window.sessionStorage.getItem("imageUrl")+'resources/upload/order/'+orderAttach;
 							var html = "<a href="+url+" target='_blank'>查看订单附件</a>";
 							Ext.getCmp('recreate_orderAttach_id').setHtml(html);
 							Ext.getCmp('recreate_orderAttach_id').setHidden(false);
 						} else {
 							Ext.getCmp('recreate_orderAttach_id').setHidden(true);
 						}
 						win.show();
 					}
 		        },
// 		        failure : function() {
// 		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
// 		        },
 		        scope:this
 			});
	},
	
	//订单补录
	recreateOrder : function() {
		var win = Ext.widget('addorderrecreatewin');
		var organizationName = window.sessionStorage.getItem('organizationName');
		Ext.getCmp('recreate_add_order_organizationName_id').setValue(organizationName);
		win.show();
		this.renderPositionInfoOnMapFromPlace();
		this.renderPositionInfoOnMapToPlace();
		this.selectHourAndMinute();
	},
	
	//选择员工
	selectOrderUser : function() {
		Ext.Ajax.request({
			url: 'employee/listByDept',
	        method : 'POST',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				Ext.getCmp("ordercreate_orderUser_id").setStore(respText);
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	//用车姓名、人电话、用车人Id、organizationId,
	SelectOrderUserDone : function(items) {
		var empId = items.getValue();
		var url = 'employee/'+ empId + '/update';
		Ext.Ajax.request({
			url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				Ext.getCmp("ordercreate_orderUserId_id").setValue(respText.data.id);
				Ext.getCmp("ordercreate_orderUserName_id").setValue(respText.data.realname);
				Ext.getCmp("ordercreate_orderUserphone_id").setValue(respText.data.phone);
				Ext.getCmp("ordercreate_orderUserOrganizationId_id").setValue(respText.data.organizationId);
				
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	//选择车辆
	selectVehicle : function() {
		var orderUserOrgId = '';
		if (Ext.getCmp('edit_ordercreate_orderUserOrganizationId_id')) {
			orderUserOrgId = Ext.getCmp('edit_ordercreate_orderUserOrganizationId_id').getValue();
		}
		if (Ext.getCmp('ordercreate_orderUserOrganizationId_id')) {
			orderUserOrgId = Ext.getCmp('ordercreate_orderUserOrganizationId_id').getValue();
		}
		if (orderUserOrgId == '' || orderUserOrgId == null) {
			Ext.Msg.alert('消息提示','请选择用车人，然后再选择车辆！');
			return;
		}
		var url = 'vehicle/'+ orderUserOrgId + '/listDeptVehicle';
		
		Ext.Ajax.request({
			url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				if (Ext.getCmp("ordercreate_vehicle_id")) {
					Ext.getCmp("ordercreate_vehicle_id").setStore(respText);
				}
				if (Ext.getCmp('edit_ordercreate_vehicle_id')) {
					Ext.getCmp('edit_ordercreate_vehicle_id').setStore(respText);
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	selectVehicleDone : function(items) {
		var vehicleId = items.getValue();
		var url = 'vehicle/'+ vehicleId + '/update';
		Ext.Ajax.request({
			url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				Ext.getCmp("ordercreate_seatNumber_id").setValue(respText.data.seatNumber);
	        }
	    });
	},
	
	//选择司机
	selectDriver : function() {
		var orderUserOrgId = '';
		if (Ext.getCmp('edit_ordercreate_orderUserOrganizationId_id')) {
			orderUserOrgId = Ext.getCmp('edit_ordercreate_orderUserOrganizationId_id').getValue();
		}
		if (Ext.getCmp('ordercreate_orderUserOrganizationId_id')) {
			orderUserOrgId = Ext.getCmp('ordercreate_orderUserOrganizationId_id').getValue();
		}
		if (orderUserOrgId == '' || orderUserOrgId == null) {
			Ext.Msg.alert('消息提示','请选择用车人，然后再选择车辆！');
			return;
		}
		var url = 'driver/'+ orderUserOrgId + '/listByDept';
		Ext.Ajax.request({
			url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				if (Ext.getCmp("ordercreate_driver_id")) {
					Ext.getCmp("ordercreate_driver_id").setStore(respText);
				}
				if (Ext.getCmp("edit_ordercreate_driver_id")) {
					Ext.getCmp("edit_ordercreate_driver_id").setStore(respText);
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	selectDriverDone : function(items) {
		var driverId = items.getValue();
		var url = 'driver/'+ driverId + '/update';
		Ext.Ajax.request({
			url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				Ext.getCmp("ordercreate_driverId_id").setValue(respText.data.id);
				Ext.getCmp("ordercreate_driverName_id").setValue(respText.data.realname);
				Ext.getCmp("ordercreate_driverPhone_id").setValue(respText.data.phone);
				
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	//提交补录订单
	onSubmit : function(btn) {
		orderInfo = this.getView().down('form').getValues();
		var eddate = orderInfo.eddate;
		var edhour = orderInfo.edhour;
		var edminute = orderInfo.edminute;
		if (eddate=='' || edhour=='' || edminute=='') {
			Ext.Msg.alert('消息提示','请正确的选择订单的结束时间！');
	    	return;
		}
		var edTime = eddate+' '+edhour+': '+edminute;
		var  str1 = edTime.toString().replace(/-/g,"/");
	    var endDate = new Date(str1);
		
		var stdate = orderInfo.stdate;
		var sthour = orderInfo.sthour;
		var stminute = orderInfo.stminute;
		if (stdate=='' || sthour=='' || stminute=='') {
			Ext.Msg.alert('消息提示','请正确的选择订单的开始时间！');
	    	return;
		}
		var stTime = stdate+' '+sthour+': '+stminute;
		var str2 = stTime.toString().replace(/-/g,"/");
	    var startDate = new Date(str2);
	    
	    //天数
	    //var date3=endDate.getTime()-startDate.getTime()
	    var days=Math.floor((endDate.getTime()-startDate.getTime())/(24*3600*1000));
	    //小时数
	    var leave1 = (endDate.getTime()-startDate.getTime())%(24*3600*1000);
	    var hours=Math.floor(leave1/(3600*1000));
	    //分钟数
	    var leave2=leave1%(3600*1000);
	    var minutes=Math.floor(leave2/(60*1000));
	    
	    //durationTime以分钟为单位
	    durationTime = (days*24*60+hours*60+minutes);
	    if (durationTime<0 || durationTime==0) {
	    	Ext.Msg.alert('消息提示','实际开始时间应该在实际结束时间之前，请重新填写！');
	    	return;
	    }
	    orderInfo.durationTime = durationTime;
	    
	    orderInfo.factStTime = stdate + ' ' + sthour + ':' + stminute + ':00';
	    orderInfo.factEdTime = eddate + ' ' + edhour + ':' + edminute + ':00';
	    Ext.getCmp('recreate_addOrder_factStTime_id').setValue(orderInfo.factStTime);
	    Ext.getCmp('recreate_addOrder_factEdTime_id').setValue(orderInfo.factEdTime);
	    //计算等待时长   0:往返      1：不往返
	    if (orderInfo.returnType == 0) {
	    	var waitHour = orderInfo.waitHour;
			var waitMinute = orderInfo.waitMinute;
			orderInfo.waitTime =  parseInt(waitHour*60) + parseInt(waitMinute);
	    }
	    
	    var form=this.getView().down('form').getForm();
		if (form.isValid()) {
            form.submit({
                url: 'order/recreate',
                method:'post',
          //      defaultHeaders : {'Content-type' : 'application/json;utf-8'},
                //async: true,
                waitMsg: 'Uploading your file...',
                success: function(form,action) {
                	console.log('成功'+action.response.responseText);
                	var result = Ext.util.JSON.decode(action.response.responseText);
                	btn.up('window').close();
                	Ext.Msg.alert('消息提示','添加成功!');
                	Ext.getCmp("orderrecreate_grid_id").getStore('ordersResults').load();
                },
		        failure : function(form,action) {
		        	console.log('失败'+action.response.responseText);
		        	var result = Ext.util.JSON.decode(action.response.responseText);
		        	if (result.msg!=null || result.msg!='') {
		        		Ext.Msg.alert('消息提示', result.msg);
		        	}
		        },
	        });
		}
//		var json = Ext.encode(orderInfo);		
//	    Ext.Ajax.request({
//			url : 'order/recreate',//?json='+ Ext.encode(orderInfo),
//	        method : 'POST',
//	        params:{json:json},
//	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
//	        success : function(response,options) {
//				var respText = Ext.util.JSON.decode(response.responseText);
//				if (respText.status == 'success') {
//					btn.up('addorderrecreatewin').close();
//					Ext.Msg.alert('消息提示', '订单已提交成功！');
//					Ext.getCmp("orderrecreate_grid_id").getStore('ordersResults').load();
//				} else {
//					Ext.Msg.alert('消息提示', respText.data);
//				}
//	        },
////	        failure : function() {
////	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
////	        },
//	        scope: this,
//	    });
	},
	
	//查看订单分配信息
	viewAllocateInfo : function(grid, rowIndex, colIndex) {
		var orderInfo = grid.getStore().getAt(rowIndex);
    	var driverName = orderInfo.data.driverName;
    	var driverPhone = orderInfo.data.driverPhone;
    	var vehicleId = orderInfo.data.vehicleId;
    	var vehicleType = orderInfo.data.vehicleType;
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
    		case '4':
    			vehicleType='执法执勤特种专业用车';
    			break;
    	}
    	
    	var url='vehicle/'+vehicleId+'/update';
    	
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
	        	rec.data.stMileage = orderInfo.data.stMileage;
	        	rec.data.edMileage = orderInfo.data.edMileage;
				if (respText.status == 'success') {	
					var win = Ext.create('Admin.view.ordermgmt.orderrecreate.OrderAllocateView');
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
	
	//订单修改
	editOrder : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var orderId = rec.data.id;
		url = 'order/'+orderId+'/update';
		var orderRet = new Ext.data.Model();
		
		Ext.Ajax.request({
	   		url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				if (respText.status == 'success') {
					var win = Ext.widget("recreateEditOrderWin");
					orderRet.data = respText.data;
					var orderUsername1 = respText.data.orderUsername;
					orderRet.data.orderUsername1 = orderUsername1;
					var factStTimeF = rec.data.factStTimeF;
		        	var stDate = factStTimeF.substring(0,10);
					var stHour = factStTimeF.substring(11,13);
					var stMinute = factStTimeF.substring(14,16);
					win.down('form').getForm().findField('stDate').setValue(stDate);
					win.down('form').getForm().findField('stHour').setValue(stHour);
					win.down('form').getForm().findField('stMinute').setValue(stMinute);
					
					var factEdTimeF = rec.data.factEdTimeF;
					var edDate = factEdTimeF.substring(0,10);
					var edHour = factEdTimeF.substring(11,13);
					var edMinute = factEdTimeF.substring(14,16);
					win.down('form').getForm().findField('edDate').setValue(edDate);
					win.down('form').getForm().findField('edHour').setValue(edHour);
					win.down('form').getForm().findField('edMinute').setValue(edMinute);
					
					//往返，显示等待时长，不往返，不显示等待时长 0：是  1：否
					if (orderRet.data.returnType == 0) {
						Ext.getCmp('recreate_editorder_waitTime_id').setHidden(false);
					} else if (orderRet.data.returnType == 1) {
						Ext.getCmp('recreate_editorder_waitTime_id').setHidden(true);
					}
					
					var waitTime = respText.data.waitTime;
					var waitHour = parseInt(waitTime/60);
					var waitMinute = waitTime % 60;
					win.down('form').getForm().findField('waitHour').setValue(waitHour);
					win.down('form').getForm().findField('waitMinute').setValue(waitMinute);
					
					orderRet.data.driverName1 = rec.data.driverName;
					
					orderRet.data.vehicleNumber1 = orderRet.data.vehicleNumber;
					orderRet.data.organizationName = orderRet.data.unitName;
					win.down("form").loadRecord(orderRet);
					win.show();
					this.renderPositionInfoOnMapFromPlace(respText.data.fromPlace);
					this.renderPositionInfoOnMapToPlace(respText.data.toPlace);
					this.selectHourAndMinute();
				}
	        },
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	        scope: this
	    });
	},
	
	//修改订单，修改车辆，设置对应的vehicleId
	editSelectVehicleDone : function(items) {
		var vehicleId = items.getValue();
		Ext.getCmp('edit_ordercreate_vehicle_id_id').setValue(vehicleId);
		var url = 'vehicle/'+ vehicleId + '/update';
		Ext.Ajax.request({
			url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				Ext.getCmp("edit_ordercreate_seatNumber_id").setValue(respText.data.seatNumber);
	        }
	    });	
	},
	
	//修改订单，修改司机，设置对应的driverId
	editSelectDriverDone : function(items) {
		var driverId = items.getValue();
		Ext.getCmp('edit_ordercreate_driver_id_id').setValue(driverId);
		
		var url = 'driver/'+ driverId + '/update';
		Ext.Ajax.request({
			url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				Ext.getCmp("edit_ordercreate_driver_name_id").setValue(respText.data.realname);
				Ext.getCmp("edit_ordercreate_driver_phone_id").setValue(respText.data.phone);
				
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	//完成订单修改
	editOrderDone : function(btn) {
		var orderInfo = this.getView().down('form').getForm().getValues();
		//orderInfo.planTime = orderInfo.date + ' ' + orderInfo.hour + ':' + orderInfo.minute + ':00';
		var edDate = orderInfo.edDate;
		var edHour = orderInfo.edHour;
		var edMinute = orderInfo.edMinute;
		if (edDate=='' || edHour=='' || edMinute=='') {
			Ext.Msg.alert('消息提示','请正确的选择订单的结束时间！');
	    	return;
		}
		var edTime = edDate+' '+edHour+': '+edMinute;
		var  str1 = edTime.toString().replace(/-/g,"/");
	    var endDate = new Date(str1);
		
		var stDate = orderInfo.stDate;
		var stHour = orderInfo.stHour;
		var stMinute = orderInfo.stMinute;
		if (stDate=='' || stHour=='' || stMinute=='') {
			Ext.Msg.alert('消息提示','请正确的选择订单的开始时间！');
	    	return;
		}
		var stTime = stDate+' '+stHour+': '+stMinute;
		var str2 = stTime.toString().replace(/-/g,"/");
	    var startDate = new Date(str2);
	    
	    //天数
	    //var date3=endDate.getTime()-startDate.getTime()
	    var days=Math.floor((endDate.getTime()-startDate.getTime())/(24*3600*1000));
	    //小时数
	    var leave1 = (endDate.getTime()-startDate.getTime())%(24*3600*1000);
	    var hours=Math.floor(leave1/(3600*1000));
	    //分钟数
	    var leave2=leave1%(3600*1000);
	    var minutes=Math.floor(leave2/(60*1000));
	    
	    //durationTime以分钟为单位
	    durationTime = (days*24*60+hours*60+minutes);
	    //alert(durationTime);
	    if (durationTime<0 || durationTime==0) {
	    	Ext.Msg.alert('消息提示','实际开始时间应该在实际结束时间之前，请重新填写！');
	    	return;
	    }
	    orderInfo.durationTime = durationTime;
	    
	    if (orderInfo.returnType == 0) {
	    	orderInfo.waitTime = parseInt(orderInfo.waitHour*60) + parseInt(orderInfo.waitMinute);
	    } else if (orderInfo.returnType == 1) {
	    	orderInfo.waitTime = 0;
	    }
	    
	    orderInfo.factStTime = stDate + ' ' + stHour + ':' + stMinute + ':00';
	    orderInfo.factEdTime = edDate + ' ' + edHour + ':' + edMinute + ':00';
	    orderInfo.driverName = orderInfo.driverName1;
	    Ext.getCmp("recreate_editOrder_factStTime_id").setValue(orderInfo.factStTime);
	    Ext.getCmp("recreate_editOrder_factEdTime_id").setValue(orderInfo.factEdTime);
		switch (orderInfo.vehicleType) {
			case '应急机要通信接待用车':
				orderInfo.vehicleType = '0';
				break;
			case '行政执法用车':
				orderInfo.vehicleType = '1';
				break;
			case '行政执法特种专业用车':
				orderInfo.vehicleType = '2';
				break;
			case '一般执法执勤用车':
				orderInfo.vehicleType = '3';
				break;
			case '执法执勤特种专业用车':
				orderInfo.vehicleType = '4';
				break;
		}
		//上传驾照照片
		var attachName = orderInfo.paperOrderNo;
		var form=this.getView().down('form').getForm();
		if (form.isValid()) {
            form.submit({
                url: 'order/recreateUpdate',
                method:'post',
                defaultHeaders : {'Content-type' : 'application/json;utf-8'},
                //async: true,
                waitMsg: 'Uploading your file...',
                success: function(form,action) {
                	var result = Ext.util.JSON.decode(action.response.responseText);
                	btn.up('window').close();
                	Ext.Msg.alert('消息提示','修改成功!');
//                	Ext.getCmp("griddriverid").getStore('driversResults').load();
                },
		        failure : function(form,action) {
		        	var result = Ext.util.JSON.decode(action.response.responseText);
		        	//Ext.Msg.alert('消息提示','修改失败');
		        	if (result.msg!=null || result.msg!='') {
		        		Ext.Msg.alert('消息提示', result.msg);
		        	}
		        },
	        });
		}
//		var json = Ext.encode(orderInfo);
//		Ext.Ajax.request({
//        	url:'order/recreateUpdate',//?json='+Ext.encode(orderInfo),
//			method:'POST',
//	        params:{json:json},
//			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
//			success: function(response,options){
//				var respText = Ext.util.JSON.decode(response.responseText);
//				if(respText.status=='success'){
//				 	Ext.Msg.alert("提示信息", '修改成功');
// 					var orderList = Ext.getCmp('orderrecreate_grid_id');
// 					//orderList.getStore("ordersResults").currentPage = 1;
// 					btn.up('recreateEditOrderWin').close();
// 					orderList.getStore("ordersResults").load();
//				}else{
//					Ext.MessageBox.alert("提示信息", respText.data);
//					//btn.up('recreateEditOrderWin').close();
//				}
//			 	
//			 }
//	        /*,
//			failure : function() {
//				 Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			}*/
//        });
	},
	
	//出发地自动补齐
	renderPositionInfoOnMapFromPlace: function(position){
		var map = Ext.getCmp("ordermapdis_bmappanel").bmap;
		var input;
		if (Ext.getCmp('recreateAddOrderFromPlaceId')) {
			input = "recreateAddOrderFromPlaceId-inputEl";
		}
		if(Ext.getCmp('recreateEditOrderFromPlaceId')) {
			input = "recreateEditOrderFromPlaceId-inputEl";
		}
		//建立一个自动完成的对象
    	var ac = new BMap.Autocomplete({
	 				"input" : input,
	 				"location" : map
		});
    	

		if(position != ""  && position != null){
			ac.setInputValue(position);
		}

		ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
			var str = "";
			var _value = e.fromitem.value;
			var value = "";
			if (e.fromitem.index > -1) {
				value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
			}
			str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

			value = "";
			if (e.toitem.index > -1) {
				_value = e.toitem.value;
				value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
			}    
			str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
		});

		var myValue;
		ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
			var _value = e.item.value;
			myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
			var local = new BMap.LocalSearch(map, { //智能搜索
				onSearchComplete: function(){
				var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
				}
			});
			local.search(myValue);
		});
	},
	
	//目的地自动补齐
	renderPositionInfoOnMapToPlace: function(position){
		var map = Ext.getCmp("ordermapdis_bmappanel").bmap;
		//建立一个自动完成的对象
		var input;
		if (Ext.getCmp('recreateAddOrderToPlaceId')) {
			input = "recreateAddOrderToPlaceId-inputEl";
		}
		if(Ext.getCmp('recreateEditOrderToPlaceId')) {
			input = "recreateEditOrderToPlaceId-inputEl";
		}
    	var ac = new BMap.Autocomplete({
	 				"input" : input,
	 				"location" : map
		});

    	if(position != ""  && position != null){
			ac.setInputValue(position);
		}
    	
		ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
			var str = "";
			var _value = e.fromitem.value;
			var value = "";
			if (e.fromitem.index > -1) {
				value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
			}
			str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

			value = "";
			if (e.toitem.index > -1) {
				_value = e.toitem.value;
				value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
			}    
			str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
		});

		var myValue;
		ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
			var _value = e.item.value;
			myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
			var local = new BMap.LocalSearch(map, { //智能搜索
				onSearchComplete: function(){
				var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
				}
			});
			local.search(myValue);
		});
	},
	/******多级部门筛选*******/
	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.ordermgmt.orderrecreate.DeptChooseWin",{
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
     	var form = Ext.getCmp("orderrecreate_searchform_id").getForm();
     	form.findField("deptId").setValue(deptId);
//     	form.findField("organizationId").setDisplayField(deptName);
     	form.findField("deptName").setValue(deptName);
     	btn.up("window").close();
     },

	selectCallTime : function(field,value,eOpts) {
		var callTime = Ext.util.Format.date(Ext.getCmp(field.id).getValue(),'Y-m-d');

		var nowDate = Ext.util.Format.date(new Date(),'Y-m-d');
		if (callTime != nowDate) {
			var comstore = new Ext.data.Store();

			comstore = {
		    		data: [
		    		    {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
				        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
				        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
				}

			switch (field.id)
			{
				case 'add_order_start_id':
					Ext.getCmp('add_hour_start_id').setValue('00');
					Ext.getCmp('add_hour_start_id').setStore(comstore);
					Ext.getCmp('add_minute_start_id').setMaxValue('59');
					Ext.getCmp('add_minute_start_id').setValue('0');
				break;
				case 'add_order_end_id':
					Ext.getCmp('add_hour_end_id').setValue('00');
					Ext.getCmp('add_hour_end_id').setStore(comstore);
					Ext.getCmp('add_minute_end_id').setMaxValue('59');
					Ext.getCmp('add_minute_end_id').setValue('0');
				break;
				case 'edit_order_start_id':
					Ext.getCmp('edit_hour_start_id').setValue('00');
					Ext.getCmp('edit_hour_start_id').setStore(comstore);
					Ext.getCmp('edit_minute_start_id').setMaxValue('59');
					Ext.getCmp('edit_minute_start_id').setValue('0');
				break;
				case 'edit_order_end_id':
					Ext.getCmp('edit_hour_end_id').setValue('00');
					Ext.getCmp('edit_hour_end_id').setStore(comstore);
					Ext.getCmp('edit_minute_end_id').setMaxValue('59');
					Ext.getCmp('edit_minute_end_id').setValue('0');
				break;
			}
		} else {
			this.selectHourAndMinute(field.id);
		}
	},

	selectHourAndMinute : function(id) {
		var comstore = new Ext.data.Store();
		var nowHour = new Date().getHours();
		var nowMinute = new Date().getMinutes();

		switch (nowHour)
		{
		case 0:
			comstore = {
	    		data: [
	    		    {'id': '0', 'time': '00'}]
			}
		break;

		case 1:
			comstore = {
	    		data: [
	    		    {'id': '0', 'time': '00'},{'id': '1', 'time': '01'}]
			}
		break;

		case 2:
			comstore = {
	    		data: [
	    		      {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'}]
			}
		break;

		case 3:
			comstore = {
	    		data: [
	    		      {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'}]
			}
		break;

		case 4:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'}]
			}
		break;

		case 5:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'}]
			}
		break;

		case 6:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				       {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'}]
			}
		break;

		case 7:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				       {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'}]
			}
		break;

		case 8:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				       {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				       {'id': '8', 'time': '08'}]
			}
		break;

		case 9:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'}]
			}
		break;

		case 10:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'}]
			}
		break;

		case 11:
			comstore = {
	    		data: [
			          {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
			        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'}]
			}
		break;

		case 12:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'}
				       ]
			}
		break;

		case 13:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'}]
			}
		break;

		case 14:
			comstore = {
	    		data: [
			          {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
			        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'}]
			}
		break;

		case 15:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'}]
			}
		break;

		case 16:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
				        {'id': '16', 'time': '16'}]
			}
		break;

		case 17:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
				        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'}]
			}
		break;

		case 18:
			comstore = {
	    		data: [
			          {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
			        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'}]
			}
		break;

		case 19:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
				        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'}]
			}
		break;

		case 20:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
				        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
				        {'id': '20', 'time': '20'}]
			}
		break;
			
		case 21:
			comstore = {
	    		data: [
			          {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
			        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'}]
			}
		break;
		case 22:
			comstore = {
	    		data: [
	    		       {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
				        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
				        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
				        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
				        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
				        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'}]
			}
		break;
		case 23:
			comstore = {
	    		data: [
			          {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
			        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		}

		switch (id)
		{
			case 'add_order_start_id':
				Ext.getCmp('add_hour_start_id').setValue(nowHour);
				Ext.getCmp('add_hour_start_id').setStore(comstore);
				Ext.getCmp('add_minute_start_id').setMaxValue(nowMinute);
				Ext.getCmp('add_minute_start_id').setValue(nowMinute);
			break;
			case 'add_order_end_id':
				Ext.getCmp('add_hour_end_id').setValue(nowHour);
				Ext.getCmp('add_hour_end_id').setStore(comstore);
				Ext.getCmp('add_minute_end_id').setMaxValue(nowMinute);
				Ext.getCmp('add_minute_end_id').setValue(nowMinute);
			break;
			case 'edit_order_start_id':
				Ext.getCmp('edit_hour_start_id').setValue(nowHour);
				Ext.getCmp('edit_hour_start_id').setStore(comstore);
				Ext.getCmp('edit_minute_start_id').setMaxValue(nowMinute);
				Ext.getCmp('edit_minute_start_id').setValue(nowMinute);
			break;
			case 'edit_order_end_id':
				Ext.getCmp('edit_hour_end_id').setValue(nowHour);
				Ext.getCmp('edit_hour_end_id').setStore(comstore);
				Ext.getCmp('edit_minute_end_id').setMaxValue(nowMinute);
				Ext.getCmp('edit_minute_end_id').setValue(nowMinute);
			break;
			default:
				if (Ext.getCmp('add_hour_start_id') && Ext.getCmp('add_minute_start_id') && Ext.getCmp('add_hour_end_id') && Ext.getCmp('add_minute_end_id')) {
					Ext.getCmp('add_hour_start_id').setValue(nowHour);
					Ext.getCmp('add_hour_start_id').setStore(comstore);
					Ext.getCmp('add_minute_start_id').setMaxValue(nowMinute);
					Ext.getCmp('add_minute_start_id').setValue(nowMinute);

					Ext.getCmp('add_hour_end_id').setValue(nowHour);
					Ext.getCmp('add_hour_end_id').setStore(comstore);
					Ext.getCmp('add_minute_end_id').setMaxValue(nowMinute);
					Ext.getCmp('add_minute_end_id').setValue(nowMinute);

				} else if (Ext.getCmp('edit_hour_start_id') && Ext.getCmp('edit_minute_start_id') && Ext.getCmp('edit_hour_end_id') && Ext.getCmp('edit_minute_end_id')) { 					
					var nowHour = new Date().getHours();		
					var nowDate = Ext.util.Format.date(new Date(),'Y-m-d');
					var comstore2 = {
			    		data: [
					          {'id': '0', 'time': '00'},{'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
					        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
					        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
					        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
					        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
					        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
					};

					var callTime = Ext.util.Format.date(Ext.getCmp('edit_order_start_id').getValue(),'Y-m-d');
					if (callTime == nowDate) {
						Ext.getCmp('edit_hour_start_id').setStore(comstore);
						if(nowHour == Ext.getCmp('edit_hour_start_id').getValue()){						
							Ext.getCmp('edit_minute_start_id').setMaxValue(nowMinute);
						}else{
							Ext.getCmp('edit_minute_start_id').setMaxValue('59');						
						}
					}else{
						Ext.getCmp('edit_hour_start_id').setStore(comstore2);
					}

					var callTime2 = Ext.util.Format.date(Ext.getCmp('edit_order_end_id').getValue(),'Y-m-d');
					if (callTime2 == nowDate) {
						Ext.getCmp('edit_hour_end_id').setStore(comstore);
						if(nowHour == Ext.getCmp('edit_hour_end_id').getValue()){						
							Ext.getCmp('edit_minute_end_id').setMaxValue(nowMinute);
						}else{
							Ext.getCmp('edit_minute_end_id').setMaxValue('59');						
						}
					}else{
						Ext.getCmp('edit_hour_end_id').setStore(comstore2);
					}
				}
			break;
		}
	},

	selectHour: function(me,record,eOpts ){	
		var nowHour = new Date().getHours();
		var nowMinute = new Date().getMinutes();		
		var nowDate = Ext.util.Format.date(new Date(),'Y-m-d');

		switch (me.id)
		{
			case 'add_hour_start_id':
				var callTime = Ext.util.Format.date(Ext.getCmp('add_order_start_id').getValue(),'Y-m-d');
				if (callTime == nowDate) {
					if(nowHour == record.data.time){						
						Ext.getCmp('add_minute_start_id').setMaxValue(nowMinute);
						Ext.getCmp('add_minute_start_id').setValue(nowMinute);
					}else{
						Ext.getCmp('add_minute_start_id').setMaxValue('59');						
					}
				}
			break;
			case 'add_hour_end_id':
				var callTime = Ext.util.Format.date(Ext.getCmp('add_order_end_id').getValue(),'Y-m-d');
				if (callTime == nowDate) {
					if(nowHour == record.data.time){						
						Ext.getCmp('add_minute_end_id').setMaxValue(nowMinute);
						Ext.getCmp('add_minute_end_id').setValue(nowMinute);
					}else{
						Ext.getCmp('add_minute_end_id').setMaxValue('59');						
					}
				}
			break;
			case 'edit_hour_start_id':
				var callTime = Ext.util.Format.date(Ext.getCmp('edit_order_start_id').getValue(),'Y-m-d');
				if (callTime == nowDate) {
					if(nowHour == record.data.time){						
						Ext.getCmp('edit_minute_start_id').setMaxValue(nowMinute);
						Ext.getCmp('edit_minute_start_id').setValue(nowMinute);
					}else{
						Ext.getCmp('edit_minute_start_id').setMaxValue('59');						
					}
				}
			break;
			case 'edit_hour_end_id':
				var callTime = Ext.util.Format.date(Ext.getCmp('edit_order_end_id').getValue(),'Y-m-d');
				if (callTime == nowDate) {
					if(nowHour == record.data.time){						
						Ext.getCmp('edit_minute_end_id').setMaxValue(nowMinute);
						Ext.getCmp('edit_minute_end_id').setValue(nowMinute);
					}else{
						Ext.getCmp('edit_minute_end_id').setMaxValue('59');						
					}
				}
			break;
		}
	},
});
