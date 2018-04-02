/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderlist.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
	        'Admin.view.ordermgmt.orderlist.GridOrderList',
	        'Admin.view.ordermgmt.orderlist.View',
	        'Admin.view.ordermgmt.orderlist.SearchForm',
	        'Admin.view.ordermgmt.orderlist.AddOrder',
	        'Admin.view.ordermgmt.orderlist.ViewOrder',
	        'Admin.view.ordermgmt.orderlist.OrderMap'
			],
	alias : 'controller.orderlistcontroller',

	id: 'orderlistcontroller',
	
	onBeforeLoad : function() {
		//所属部门
    	var frmValues=this.lookupReference('searchForm').getValues();

    	if(''==frmValues['deptId']||null==frmValues['deptId']){
    		frmValues['deptId']='-1';
    	}
    	if(this.lookupReference('searchForm').getForm().isValid()){
    		var parm = this.lookupReference('searchForm').getValues();
    		if (parm.status == '全部' || parm.status == -1) {
    			parm.status = '';
    		}

    		if (parm.orderCat == '全部' || parm.orderCat == -1) {
    			parm.orderCat = '';
    		}
    		var page=Ext.getCmp('orderlistpage').store.currentPage;
    		var limit=Ext.getCmp('orderlistpage').store.pageSize;

    		parm = {
    			    "currentPage" 	: page,
    				"numPerPage" : limit,
    				'orderNo': 	parm.orderNo,
    				'status': parm.status,
    				'orderCat' : parm.orderCat,
//    				'organizationId': 1,
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
		//在load()之前会调用onBeforeLoad方法
		Ext.getCmp('orderlistpage').getStore().currentPage = 1;
		this.getViewModel().getStore("ordersResults").load();
	},
	
	onResetClick : function() {
		this.getView().getForm().reset();
	},

	//添加订单
	addOrder: function() {
//		Ext.Msg.alert("Message Box", "添加订单");
//		return;
		
		Ext.Ajax.request({
			url: 'rule/balanceCheck',
	        method : 'POST',
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				if(respText.status == 'success'){
					if(respText.data == '' || respText.data == null){
						Ext.Ajax.request({
						url: 'user/loadCurrentUser',
				        method : 'POST',
				        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
				        success : function(response,options) {
							var respText = Ext.util.JSON.decode(response.responseText);
							var orderUserid = respText.data.id;
							var orderUsername = respText.data.realname;
							var orderUserphone = respText.data.phone;
							var organizationId = respText.data.organizationId;
							var organizationName = window.sessionStorage.getItem('organizationName');
							Ext.getCmp('add_order_orderUserid_id').setValue(orderUserid);
							Ext.getCmp('add_order_organizationId_id').setValue(organizationId);
							Ext.getCmp('add_order_orderUsername_id').setValue(orderUsername);
							Ext.getCmp('add_order_orderUserphone_id').setValue(orderUserphone);
							Ext.getCmp('add_order_organizationName_id').setValue(organizationName);
				        }
				        /*,
				        failure : function() {
				            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
				        },*/
				    });

						win = Ext.widget('addorder');
						win.getController().selectHourAndMinute();
						win.show();
						this.renderPositionInfoOnMapFromPlace();
						this.renderPositionInfoOnMapToPlace();
					}else{
						Ext.Msg.alert('消息提示',respText.data);
					}

				}else{
                       Ext.Msg.alert('消息提示','用车额度检查有误，不能新增订单');
				}
	        },
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	        scope: this
	    });
	},
	
	//添加、修改订单，日期选定之后，设置对应小时、分钟
	selectHourAndMinute : function() {
		var comstore = new Ext.data.Store();
		var nowHour = new Date().getHours();
		var nowMinute = new Date().getMinutes();
		switch (nowHour)
		{
		case 0:
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
		case 1:
			comstore = {
	    		data: [
	    		    {'id': '1', 'time': '01'},{'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
			        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 2:
			comstore = {
	    		data: [
	    		    {'id': '2', 'time': '02'},{'id': '3', 'time': '03'},
			        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 3:
			comstore = {
	    		data: [
	    		    {'id': '3', 'time': '03'},
			        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 4:
			comstore = {
	    		data: [
			        {'id': '4', 'time': '04'},{'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 5:
			comstore = {
	    		data: [
			        {'id': '5', 'time': '05'},{'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 6:
			comstore = {
	    		data: [
			        {'id': '6', 'time': '06'},{'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 7:
			comstore = {
	    		data: [
			        {'id': '7', 'time': '07'},
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 8:
			comstore = {
	    		data: [
			        {'id': '8', 'time': '08'},{'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 9:
			comstore = {
	    		data: [
			        {'id': '9', 'time': '09'},{'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 10:
			comstore = {
	    		data: [
			        {'id': '10', 'time': '10'},{'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 11:
			comstore = {
	    		data: [
			        {'id': '11', 'time': '11'},
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 12:
			comstore = {
	    		data: [
			        {'id': '12', 'time': '12'},{'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 13:
			comstore = {
	    		data: [
			        {'id': '13', 'time': '13'},{'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 14:
			comstore = {
	    		data: [
			        {'id': '14', 'time': '14'},{'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 15:
			comstore = {
	    		data: [
			        {'id': '15', 'time': '15'},
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 16:
			comstore = {
	    		data: [
			        {'id': '16', 'time': '16'},{'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 17:
			comstore = {
	    		data: [
			        {'id': '17', 'time': '17'},{'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 18:
			comstore = {
	    		data: [
			        {'id': '18', 'time': '18'},{'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 19:
			comstore = {
	    		data: [
			        {'id': '19', 'time': '19'},
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 20:
			comstore = {
	    		data: [
			        {'id': '20', 'time': '20'},{'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 21:
			comstore = {
	    		data: [
			        {'id': '21', 'time': '21'},{'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 22:
			comstore = {
	    		data: [
			        {'id': '22', 'time': '22'},{'id': '23', 'time': '23'}]
			}
		break;
		case 23:
			comstore = {
	    		data: [
			        {'id': '23', 'time': '23'}]
			}
		break;
		}

		if (Ext.getCmp('plan_hour_id') && Ext.getCmp('plan_minute_id')) {//添加订单
			Ext.getCmp('plan_hour_id').setValue(nowHour);
			Ext.getCmp('plan_hour_id').setStore(comstore);
			Ext.getCmp('plan_minute_id').setMinValue(nowMinute);
			Ext.getCmp('plan_minute_id').setValue(nowMinute);
		} else if (Ext.getCmp('edit_plan_hour_id') && Ext.getCmp('edit_plan_minute_id')) { //修改订单
			Ext.getCmp('edit_plan_hour_id').setValue(nowHour);
			Ext.getCmp('edit_plan_hour_id').setStore(comstore);
			Ext.getCmp('edit_plan_minute_id').setMinValue(nowMinute);
			Ext.getCmp('edit_plan_minute_id').setValue(nowMinute);
		}
	},

	selectHour: function(me,record,eOpts ){	
		var nowHour = new Date().getHours();
		var nowMinute = new Date().getMinutes();		
		var nowDate = Ext.util.Format.date(new Date(),'Y-m-d');

		switch (me.id)
		{
			case 'plan_hour_id':
				var callTime = Ext.util.Format.date(Ext.getCmp('add_order_planTime_id').getValue(),'Y-m-d');
				if (callTime == nowDate) {
					if(nowHour == record.data.time){						
						Ext.getCmp('plan_minute_id').setMinValue(nowMinute);
						Ext.getCmp('plan_minute_id').setValue(nowMinute);
					}else{
						Ext.getCmp('plan_minute_id').setMinValue('0');						
					}
				}
			break;
			case 'edit_plan_hour_id':
				var callTime = Ext.util.Format.date(Ext.getCmp('edit_order_planTime_id').getValue(),'Y-m-d');
				if (callTime == nowDate) {
					if(nowHour == record.data.time){						
						Ext.getCmp('edit_plan_minute_id').setMinValue(nowMinute);
						Ext.getCmp('edit_plan_minute_id').setValue(nowMinute);
					}else{
						Ext.getCmp('edit_plan_minute_id').setMinValue('0');						
					}
				}
			break;
		}
	},
	
	//添加，修改订单，完成订单预约时间选择
	selectPlanTime : function() {
		var planTime = '';
		if (Ext.getCmp('add_order_planTime_id')) {
			planTime = Ext.util.Format.date(Ext.getCmp('add_order_planTime_id').getValue(),'Y-m-d');
		} else if(Ext.getCmp('edit_order_planTime_id')) {
			planTime = Ext.util.Format.date(Ext.getCmp('edit_order_planTime_id').getValue(),'Y-m-d');
		}
		
		var nowDate = Ext.util.Format.date(new Date(),'Y-m-d');
		if (planTime != nowDate) {
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
			if (Ext.getCmp('plan_hour_id') &&　Ext.getCmp('plan_minute_id')) { //添加订单
				Ext.getCmp('plan_hour_id').setValue('00');
				Ext.getCmp('plan_hour_id').setStore(comstore);
				Ext.getCmp('plan_minute_id').setMinValue('0');
				Ext.getCmp('plan_minute_id').setValue('0');
			} else if (Ext.getCmp('edit_plan_hour_id')　&& Ext.getCmp('edit_plan_minute_id')) { //修改订单
				Ext.getCmp('edit_plan_hour_id').setValue('00');
				Ext.getCmp('edit_plan_hour_id').setStore(comstore);
				Ext.getCmp('edit_plan_minute_id').setMinValue('0');
				Ext.getCmp('edit_plan_minute_id').setValue('0');
			}
		} else {
			this.selectHourAndMinute();
		}
	},
	
	//完成添加订单
	onAddClickDone : function(btn) {
		var orderInfo = this.getView().down('form').getValues();
		if (orderInfo.hour=='' || orderInfo.minute=='') {
			Ext.Msg.alert('消息提示','请正确的选择订单的预约用车时间！');
	    	return;
		}
		
		orderInfo.planTime = orderInfo.date + ' ' + orderInfo.hour + ':' + orderInfo.tminute1 + ':00';
		Ext.getCmp('addOrder_planTime_id').setValue(orderInfo.planTime);
		
		if (orderInfo.durationTime==null || orderInfo.durationTime=='') {
			orderInfo.durationTime = 0;
		}
		if (orderInfo.tminute2==null || orderInfo.tminute2=='') {
			orderInfo.tminute2 = 0;
		}
		orderInfo.durationTime = parseInt(orderInfo.durationTime*60) + parseInt(orderInfo.tminute2);
		Ext.getCmp('addorder_durationTime1_id').setValue(orderInfo.durationTime);
		
		if (orderInfo.waitTime==null || orderInfo.waitTime=='') {
			orderInfo.waitTime = 0;
		}
		if (orderInfo.tminute3==null || orderInfo.tminute3=='') {
			orderInfo.tminute3 = 0;
		}
		orderInfo.waitTime = parseInt(orderInfo.waitTime*60) + parseInt(orderInfo.tminute3);
		Ext.getCmp('addorder_waitTime1_id').setValue(orderInfo.waitTime);
		
		if(orderInfo.secretLevel == '0'){
			orderInfo.secretLevel = null;
		}
		
	    var form=this.getView().down('form').getForm();
		if (form.isValid()) {
            form.submit({
                url: 'order/create',
                method:'post',
          //      defaultHeaders : {'Content-type' : 'application/json;utf-8'},
                //async: true,
                waitMsg: 'Uploading your file...',
                success: function(form,action) {
                	console.log('成功'+action.response.responseText);
                	var result = Ext.util.JSON.decode(action.response.responseText);
                	btn.up('window').close();
                	Ext.Msg.alert('消息提示','添加成功!');
                	Ext.getCmp("gridorderlist_id").getStore('ordersResults').load();
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
//		var input = {
//		'orderUserid':  Ext.getCmp('add_order_orderUserid_id').getValue(),
//		'orderUsername': Ext.getCmp('add_order_orderUsername_id').getValue(),
//		'orderUserphone': Ext.getCmp('add_order_orderUserphone_id').getValue(),
//		'organizationId': Ext.getCmp('add_order_organizationId_id').getValue(),
//		'city': orderInfo.city,
//		'fromPlace': orderInfo.fromPlace,
//		'fromLng': orderInfo.fromPlaceLongitude,
//		'fromLat': orderInfo.fromPlaceLatitude,
//		'toPlace': orderInfo.toPlace,
//		'toLng': orderInfo.toPlaceLongitude,
//		'toLat': orderInfo.toPlaceLatitude,
//		'planTime': orderInfo.planTime,
//		'durationTime': orderInfo.durationTime,
//		'waitTime' : orderInfo.waitTime,
//		'vehicleType': orderInfo.vehicleType,
//		'orderReason': orderInfo.orderReason,
//		'passengerNum': orderInfo.passengerNum,
//		'returnType': orderInfo.returnType,
//		'comments': orderInfo.comments,
//		'vehicleUsage': orderInfo.vehicleUsage,
//		'drivingType': orderInfo.drivingType,
//		'secretLevel':orderInfo.secretLevel
//		
//	};
//		var json = Ext.encode(input);
//		Ext.Ajax.request({
//	   		url : 'order/create',//?json='+ Ext.encode(input),
//	        method : 'POST',
//	        params:{json:json},
//	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
//	        success : function(response,options) {
//	        	var respText = Ext.util.JSON.decode(response.responseText);
//	        	var retStatus = respText.status;
//				if (retStatus == 'success') {
//					btn.up('addorder').close();
//					Ext.Msg.alert('提示信息','添加成功');
//					Ext.getCmp("gridorderlist_id").getStore('ordersResults').load();
//					
//				}else{
//					Ext.Msg.alert('提示信息',respText.data);
//				}
//	        }
//	        /*,
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },*/
//	    });
	},
	
	//查看订单
	viweOrder : function(grid, rowIndex, colIndex) {
		var orderInfo = grid.getStore().getAt(rowIndex);
		var orderNo = orderInfo.data.orderNo;
		
		var auditUserName;
		var auditUserPhone;
		var auditStatus;
		var auditTime;
		var auditRecordLength = 0;
		
		//待审核、补录、审核之前取消的订单没有审核信息
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
	            params:{json:json},
 		        async: false,
 		        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
 		        success : function(response,options) {
 					var respText = Ext.util.JSON.decode(response.responseText);
 					auditRecordLength = respText.data.length;
 					if(respText.data.length > 0){
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
 					}
 		        },
// 		        failure : function() {
// 		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
// 		        },
 		        scope:this
 			});
		}
		
 			Ext.Ajax.request({
 				//url : 'order',//?json='+ Ext.encode(input),
 				url: 'order/'+ orderInfo.data.id + '/queryBusiOrderByOrderNo',
 		        method : 'GET',
//	        	params:{json:json},
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
 					
 					rec.data.organizationName = window.sessionStorage.getItem('organizationName');
 					if (rec.data.returnType == 0) {
 						rec.data.returnType = '是';
 					} else if (rec.data.returnType == 1) {
 						rec.data.returnType = '否';
 					}
 					
 					//待审核、补录、审核之前取消的订单没有审核信息
 					if ((orderInfo.data.status==0)||(orderInfo.data.planStTimeF==null)||(orderInfo.data.status==6&&auditRecordLength==0)) {
 						var win = Ext.widget("vieworder");
 					} else { //其他类型的订单，有审批信息
 						var win = Ext.widget("vieworder",{
 							height: 800,
 							scrollable: true,
 							});
 					}
 					
 					if (respText.status == 'success') {						
 						win.down("form").loadRecord(rec);
 						
 						var orderAttach = rec.data.orderAttach;
 						if (orderAttach !=null && orderAttach !="") {
 							var url=window.sessionStorage.getItem("imageUrl")+'resources/upload/order/'+orderAttach;
 							var html = "<a href="+url+" target='_blank'>查看订单附件</a>";
 							Ext.getCmp('create_orderAttach_id').setHtml(html);
 							Ext.getCmp('create_orderAttach_id').setHidden(false);
 						} else {
 							Ext.getCmp('recreate_orderAttach_id').setHidden(true);
 						}
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
 						
 						//待审核、补录、审核之前取消的订单没有审核信息
 						if ((orderInfo.data.status==0)||(orderInfo.data.planStTimeF==null)||(orderInfo.data.status==6&&auditRecordLength==0)) {
 							Ext.getCmp('viewOrder_audit_id').setHidden(true);
 							Ext.getCmp('auditUserName_id').setHidden(true);
 							Ext.getCmp('auditUserPhone_id').setHidden(true);
 							Ext.getCmp('auditStatus_id').setHidden(true);
 							Ext.getCmp('auditTime_id').setHidden(true);
 							Ext.getCmp('paperOrderNo_id').setHidden(true);
 						}
 					}
 		        },
// 		        failure : function() {
// 		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
// 		        },
 		        scope:this
 			});
	},
	
	//删除订单
	deleteOrder : function(grid, rowIndex, colIndex) {
		var recid = grid.getStore().getAt(rowIndex).data.id;
//		win = Ext.widget('deleteorder');
//		win.show();
//		
			Ext.Msg.confirm('消息提示','确定要删除吗！！！',function(btn){
 				if (btn == 'yes') {
 					var driverID = grid.getStore().getAt(rowIndex).id;
 					var url = 'order/'+recid+'/delete';
 						
 					Ext.Ajax.request({
 			   		url: url,
 			        method : 'POST',
 			        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
 			        success : function(response,options) {
 						var respText = Ext.util.JSON.decode(response.responseText);
 						var	retStatus = respText.status;
 							if (retStatus == 'success') {
 								//删除成功后，刷新页面
 								Ext.Msg.alert('消息提示','订单已删除！');
 								Ext.getCmp("gridorderlist_id").getStore('ordersResults').load();
 							}
 				        },
// 				        failure : function() {
// 				            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
// 				        },
 				        scope:this
 					});		
 				}
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
	        async: false,
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				if (respText.status == 'success') {
					var win = Ext.widget("editorder");
					orderRet.data = respText.data;
					var orderUsername1 = respText.data.orderUsername;
					orderRet.data.orderUsername1 = orderUsername1;
					
					var planTime = respText.data.planStTimeF;
					plantDate = planTime.substring(0,10);
					plantHour = planTime.substring(11,13);
					plantMinu = planTime.substring(14,16);
					
					var durationTime = respText.data.durationTime;
					var durationHour = parseInt(durationTime/60);
					var durationMinute = durationTime % 60;
					
					//往返，显示等待时长，不往返，不显示等待时长 0：是  1：否
					if (orderRet.data.returnType == 0) {
						Ext.getCmp('editorder_waitTime_id').setHidden(false);
					} else if (orderRet.data.returnType == 1) {
						Ext.getCmp('editorder_waitTime_id').setHidden(true);
					}
					
					var waitTime = respText.data.waitTime;
					var waitHour = parseInt(waitTime/60);
					var waitMinute = waitTime % 60;
					
					orderRet.data.organizationName = window.sessionStorage.getItem('organizationName');

					if(orderRet.data.secretLevel == null){
						orderRet.data.secretLevel = '0';
					}
					
					win.down("form").loadRecord(orderRet);
					
					win.getController().selectHourAndMinute();
					
					win.down('form').getForm().findField('date').setValue(plantDate);
					win.down('form').getForm().findField('hour').setValue(plantHour);
					win.down('form').getForm().findField('minute').setValue(plantMinu);
					
					win.down('form').getForm().findField('durationHour').setValue(durationHour);
					win.down('form').getForm().findField('durationMinute').setValue(durationMinute);
					win.down('form').getForm().findField('waitHour').setValue(waitHour);
					win.down('form').getForm().findField('waitMinute').setValue(waitMinute);
					win.show();
					
					this.renderPositionInfoOnMapFromPlace(respText.data.fromPlace);
					this.renderPositionInfoOnMapToPlace(respText.data.toPlace);
					
				}
	        },
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	        scope: this
	    });
	},
	
	//完成订单修改
	editOrderDone : function(btn) {
		var orderInfo = this.getView().down('form').getForm().getValues();
		orderInfo.planTime = orderInfo.date + ' ' + orderInfo.hour + ':' + orderInfo.minute + ':00';
		Ext.getCmp("editOrder_planTime_id").setValue(orderInfo.planTime);
		
		if (orderInfo.durationTime==null || orderInfo.durationTime=='') {
			orderInfo.durationTime = 0;
		}
		if (orderInfo.durationMinute==null || orderInfo.durationMinute=='') {
			orderInfo.durationMinute = 0;
		}
		orderInfo.durationTime = parseInt(orderInfo.durationHour*60) + parseInt(orderInfo.durationMinute);
		Ext.getCmp("editorder_durationTime_id").setValue(orderInfo.durationTime);
		
		if (orderInfo.waitTime==null || orderInfo.waitTime=='') {
			orderInfo.waitTime = 0;
		}
		if (orderInfo.waitMinute==null || orderInfo.waitMinute=='') {
			orderInfo.waitMinute = 0;
		}
		orderInfo.waitTime = parseInt(orderInfo.waitHour*60) + parseInt(orderInfo.waitMinute);
		Ext.getCmp("editorder_waitTime_id1").setValue(orderInfo.waitTime);
		
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

		if(orderInfo.secretLevel == '0'){
			orderInfo.secretLevel = null;
		}
		//上传订单照片
		var form=this.getView().down('form').getForm();
		if (form.isValid()) {
            form.submit({
                url: 'order/update',
                method:'post',
                defaultHeaders : {'Content-type' : 'application/json;utf-8'},
                //async: true,
                waitMsg: 'Uploading your file...',
                success: function(form,action) {
                	var result = Ext.util.JSON.decode(action.response.responseText);
                	btn.up('window').close();
                	Ext.Msg.alert('消息提示','修改成功!');
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
//        	url:'order/update',//?json='+Ext.encode(orderInfo),
//			method:'POST',
//	        params:{json:json},
//			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
//			success: function(response,options){
//				var respText = Ext.util.JSON.decode(response.responseText);
//				if(respText.status=='success'){
//				 	Ext.Msg.alert("提示信息", '修改成功');
// 					var orderList = Ext.getCmp('gridorderlist_id');
// 					//orderList.getStore("ordersResults").currentPage = 1;
// 					btn.up('editorder').close();
// 					orderList.getStore("ordersResults").load();
//				}else{
//					Ext.MessageBox.alert("提示信息", respText.data);
//					//btn.up('editorder').close();
//				}
//			 	
//			 }
//	        /*,
//			failure : function() {
//				Ext.Msg.alert('Failure','Call interface error!');
//			}*/
//        });
		
	},
	
	updateOrder : function(grid, rowIndex, colIndex) {
		console.log('Update Order');
		var rec = grid.getStore().getAt(rowIndex);
		var planTime = rec.data.planStTimeF;
		plantDate = planTime.substring(0,10);
		plantHour = planTime.substring(11,13);
		plantMinu = planTime.substring(14,16);
		var me=this;
		var win=Ext.create("Admin.view.ordermgmt.orderlist.AddOrder", {
			title: '修改订单',
			buttons: [{
				text: '修改',
				handler: function(btn){
					var orderInfo=btn.up('window').down('form').getForm().getValues();
					orderInfo.planTime = orderInfo.date + ' ' + orderInfo.hour + ':' + orderInfo.minute + ':00';
					orderInfo.orderUserid = Ext.getCmp('add_order_orderUserid_id').getValue(),
			        orderInfo.orderUsername = Ext.getCmp('add_order_orderUsername_id').getValue(),
			        orderInfo.orderUserphone = Ext.getCmp('add_order_orderUserphone_id').getValue(),
			        orderInfo.organizationId =Ext.getCmp('add_order_organizationId_id').getValue(),
					orderInfo.id=rec.get('id');
					btn.up("window").close();
					me.modifyOrder(orderInfo);
				}
			},{
				text: '关闭',
				handler: function(){
					this.up('window').close();
				}
			}]
		});
		win.down('form').loadRecord(rec);
		win.down('form').getForm().findField('date').setValue(plantDate);
		win.down('form').getForm().findField('hour').setValue(plantHour);
		win.down('form').getForm().findField('minute').setValue(plantMinu);

		win.show();
	},

	modifyOrder:function(orderInfo) {
	    var json = Ext.encode(orderInfo);			
		Ext.Ajax.request({
        	url:'order/update',//?json='+Ext.encode(orderInfo),
			method:'POST',
	        params:{json:json},
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
			success: function(res){
				var appendData=Ext.JSON.decode(res.responseText);
				if(appendData.status=='success'){
				 	Ext.Msg.alert("提示信息", '修改成功');
 					var VehicleList = Ext.getCmp('gridorderlist_id');
		            VehicleList.getStore("ordersResults").currentPage = 1;
		            VehicleList.getStore("ordersResults").load();
//				 		me.onSearchClick();
				}else{
					Ext.MessageBox.alert("提示信息","修改失败");
				}
			 	
			 }
	        /*,
			failure : function() {
				 Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
			}*/
        });
	},
	
	//已排车订单，点击开始，状态变成进行中
	startOrder : function(grid, rowIndex, colIndex) {
		var orderInfo = grid.getStore().getAt(rowIndex);
		var input = {
				'id': orderInfo.id
		};
		
		var json = Ext.encode(input);
		Ext.Ajax.request({
        	url:'order/start',//?json='+Ext.encode(input),
			method:'POST',
	        params:{json:json},
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
			success: function(res){
				var resp=Ext.JSON.decode(res.responseText);
				if (resp.status == 'success') {
					Ext.Msg.alert("提示信息", '该订单已进入进行中状态！');
					this.onSearchClick();
				}
			 },
//			failure : function() {
//				Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			},
			scope: this
        });
	},
	
	//进行中订单，点击结束，进入已完成状态
	finishOrder : function(grid, rowIndex, colIndex) {
		var orderInfo = grid.getStore().getAt(rowIndex);
		var input = {
				'id': orderInfo.id
		};
		var json = Ext.encode(input);
		Ext.Ajax.request({
        	url:'order/finish',//?json='+Ext.encode(input),
			method:'POST',
	        params:{json:json},
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
			success: function(res){
				var resp=Ext.JSON.decode(res.responseText);
				if (resp.status == 'success') {
					Ext.Msg.alert("提示信息", '该订单已完成！');
					this.onSearchClick();
				}
			 },
//			failure : function() {
//				Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			},
			scope: this
        });
	},
	
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
	        	
				if (respText.status == 'success') {	
					var win = Ext.widget("OrderAllocateView");
					win.down("form").loadRecord(rec);
					win.show();	
				} else {
					Ext.Msg.alert('消息提示','该订单没有查询到相关的排车信息!');
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
    	});
	},
	
	//取消订单
	cancelOrder : function(grid, rowIndex, colIndex) {
		var orderInfo = grid.getStore().getAt(rowIndex);
		var orderNo = orderInfo.data.orderNo;
		var input = {
				'id': orderInfo.id
		};
		
		Ext.Msg.confirm('消息提示','确定要取消吗！！！',function(btn){
				if (btn == 'yes') {	
					var json = Ext.encode(input);
					Ext.Ajax.request({
					url:'order/cancel',//?json='+Ext.encode(input),
			        method : 'POST',
	        		params:{json:json},
			        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			        success: function(res){
						var resp=Ext.JSON.decode(res.responseText);
						if (resp.status == 'success') {
							Ext.Msg.alert("提示信息", '该订单已取消！');
							Ext.getCmp("gridorderlist_id").getStore('ordersResults').load();
						}
					 },
//					failure : function() {
//						Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//					},
					scope: this
					});		
				}
			});	
	},
	
	//出发地自动补齐
	renderPositionInfoOnMapFromPlace: function(position){
		var map = Ext.getCmp("ordermapdis_bmappanel").bmap;
		var input;
		if (Ext.getCmp('addOrderFromPlaceId')) {
			input = "addOrderFromPlaceId-inputEl";
		}
		if(Ext.getCmp('editOrderFromPlaceId')) {
			input = "editOrderFromPlaceId-inputEl";
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
					if (Ext.getCmp('addOrderFromPlace_lng_id')) {
						Ext.getCmp('addOrderFromPlace_lng_id').setValue(pp.lng);
					}
					if (Ext.getCmp('addOrderFromPlace_lat_id')) {
						Ext.getCmp('addOrderFromPlace_lat_id').setValue(pp.lat);
					}
					if (Ext.getCmp('editOrderFromPlace_lng_id')) {
						Ext.getCmp('editOrderFromPlace_lng_id').setValue(pp.lng);
					}
					if (Ext.getCmp('editOrderFromPlace_lat_id')) {
						Ext.getCmp('editOrderFromPlace_lat_id').setValue(pp.lat);
					}
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
		if (Ext.getCmp('addOrderToPlaceId')) {
			input = "addOrderToPlaceId-inputEl";
		}
		if(Ext.getCmp('editOrderToPlaceId')) {
			input = "editOrderToPlaceId-inputEl";
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
					//添加
					if (Ext.getCmp('addOrderToPlace_lng_id')) {
						Ext.getCmp('addOrderToPlace_lng_id').setValue(pp.lng);	
					}
					if (Ext.getCmp('addOrderToPlace_lat_id')) {
						Ext.getCmp('addOrderToPlace_lat_id').setValue(pp.lat);
					}
					//修改
					if (Ext.getCmp('editOrderToPlace_lng_id')) {
						Ext.getCmp('editOrderToPlace_lng_id').setValue(pp.lng);	
					}
					if (Ext.getCmp('editOrderToPlace_lat_id')) {
						Ext.getCmp('editOrderToPlace_lat_id').setValue(pp.lat);
					}
					
				}
			});
			local.search(myValue);
		});
	},
	/******多级部门筛选*******/
	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.ordermgmt.orderlist.DeptChooseWin",{
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
     	var form = Ext.getCmp("orderlist_searchForm_id").getForm();
     	form.findField("deptId").setValue(deptId);
//     	form.findField("organizationId").setDisplayField(deptName);
     	form.findField("deptName").setValue(deptName);
     	btn.up("window").close();
     },

    checkIsGroupNull: function(chk, newValue, oldValue, eOpts ){
     	var group = chk.up("checkboxgroup");
     	var value = group.getValue();
     	if(value.includeSelf == null && value.includeChild == null){
//     		chk.setChecked(true);
     		Ext.Msg.alert("提示信息", '本部门和子部门请至少选择一个！');
     		//setValue无法选中上次选中的，有bug
     		if(chk.boxLabel == "本部门"){
     			group.items.items[1].setValue(true);
     		}else{
     			group.items.items[0].setValue(true);
     		}
     	}
     },

    finishOrder: function(grid, rowIndex, colIndex){
        var orderInfo = grid.getStore().getAt(rowIndex);
        var input = {
            'id': orderInfo.data.id
        };
        
        Ext.Msg.confirm('消息提示','确认完成该订单？',function(btn){
            if (btn == 'yes') {
              var json = Ext.encode(input);
                Ext.Ajax.request({
                    url : 'order/finish',
                    method : 'POST',
                    params:{json:json},
                    success : function(response,options) {
                        var respText = Ext.util.JSON.decode(response.responseText);
                        if (respText.status == 'success') {
                            Ext.Msg.alert('提示', '此订单已完成！');
                        }else{                            
                            Ext.Msg.alert('提示', '此订单完成失败！');
                        }
                        Ext.getCmp('gridorderlist_id').getStore("ordersResults").load();
                    },
                    scope:this
                });
            }
        })
     },

    showOrderTraceWindow: function(grid, rowIndex, colIndex) {
    	var orderInfo = grid.getStore().getAt(rowIndex);
    	
//    	orderInfo.factStTime = 1502869808000;
//    	orderInfo.factEdTime = 1502948347000;
    	
    	var factStTime = orderInfo.data.factStTime;
    	var factEdTime = orderInfo.data.factEdTime;
    	var stMileage = orderInfo.data.stMileage;
    	var edMileage = orderInfo.data.edMileage;
    	
    	
    	if(factStTime == null || factEdTime == null){
    		Ext.Msg.alert('提示', '订单时间信息不全！');
            return;
    	}
    	
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
    	
    	var win = Ext.widget("orderTraceWindow", {
			title: '订单车辆轨迹——'+vehicleNumber,
			closable: true,
			buttonAlign : 'center',
			imei : orderInfo.data.deviceNumber,
			vehicleNumber: vehicleNumber,
			factStTime: factStTime,
			factEdTime: factEdTime,
			stMileage: stMileage,
			edMileage: edMileage
		});
    	win.show();
    },

    loadTraceInfo : function() {
    	console.log('++++loadTraceInfo++++');   	
//    	var data = '[{"startTime":"2017-09-18 19:02:31","endTime":"2017-09-18 20:12:36","mileage":"14","speed":"23"}]';
    	var panel = Ext.getCmp("orderTraceCountGrid");
    	var window = panel.up("window");
    	var input = {};
    	input.vehname = window.vehicleNumber;
    	var factStTime = window.factStTime;
    	var factEdTime = window.factEdTime;
    	var stMileage = window.stMileage;
    	var edMileage = window.edMileage;
    	
    	if(factStTime == null || factEdTime == null){
    		//Ext.Msg.alert('提示', '轨迹查询失败！');
    		return;
    	}
    	
    	var data = {};
    	data.startTime = datetimeFormat(factStTime);
        data.endTime = datetimeFormat(factEdTime);
        
    	if(stMileage != null && edMileage != null && edMileage > stMileage){
    		data.mileage = edMileage - stMileage;
    		var minutes = GetDateDiff(data.startTime, data.endTime, 'minute');
	        var averageSpeed = Math.round(data.mileage/minutes * 60);
	        data.speed = averageSpeed;
    	}else{
    		data.mileage = "--";
    	}
        
        var griddata = [];
        griddata.push(data);
		Ext.getCmp("orderTraceCountGrid").getViewModel().getStore("orderTraceStore").loadData(griddata);
//    	var json = Ext.encode(input);
//        Ext.Ajax.request({
//            url : 'vehicle/monitor/findTripPropertyDataByTimeRangeByName',
//            method : 'POST',
////            async : false,
//            params:{json:json},
//            success : function(response,options) {
//                var respText = Ext.util.JSON.decode(response.responseText);
//                if (respText.status == 'success') {
//                	if(respText.data != null){
//                		data = respText.data;
//                	}
//                }else{                            
////                    Ext.Msg.alert('提示', '轨迹查询失败！');
//                    return;
//                }
//            },
//            scope:this
//        });
    	
//		Ext.getCmp("orderTraceCountGrid").getViewModel().getStore("orderTraceStore").loadData(JSON.parse(data));
        
    },

    loadTripTraceMapView : function(panel) {
    	var window = panel.up("window");
    	var traceMap = this.lookupReference('bmappanelOrder').bmap;
    	traceMap.centerAndZoom(new BMap.Point(119.314088, 26.08788), 11);
    	
    	var input = {};
    	input.vehname = window.vehicleNumber;
    	var factStTime = window.factStTime;
    	var factEdTime = window.factEdTime;
    	
    	if(factStTime == null || factEdTime == null){
    		//Ext.Msg.alert('提示', '轨迹查询失败！');
    		return;
    	}
    	
    	input.starttime = datetimeFormat(factStTime);
    	input.endtime = datetimeFormat(factEdTime);
    	
    	

//		var traceMockData = '{"data":[{"tracetime":"2017-09-15 19:03:25","longitude":"121.63296912346","latitude":"31.217214586227","speed":"7","address":"上海市浦东新区祖冲之路","status":"行驶"},{"tracetime":"2017-09-15 19:05:39","longitude":"121.63207006526","latitude":"31.217128216634","speed":"15","address":"上海市浦东新区祖冲之路","status":"行驶"},{"tracetime":"2017-09-15 19:18:35","longitude":"121.62899833353","latitude":"31.188321594644","speed":"0","address":"上海市浦东新区申江路立交桥","status":"停止"},{"tracetime":"2017-09-15 19:20:59","longitude":"121.62780512303","latitude":"31.182485355054","speed":"69","address":"上海市浦东新区申江路","status":"行驶"},{"tracetime":"2017-09-15 19:21:15","longitude":"121.62752663257","latitude":"31.179765126237","speed":"67","address":"上海市浦东新区申江路","status":"行驶"},{"tracetime":"2017-09-15 19:22:16","longitude":"121.62773058752","latitude":"31.172754919856","speed":"7","address":"上海市浦东新区X377(申江路)","status":"行驶"},{"tracetime":"2017-09-15 19:29:03","longitude":"121.63373294771","latitude":"31.149163172053","speed":"66","address":"上海市浦东新区申江南路辅路","status":"行驶"},{"tracetime":"2017-09-15 19:34:02","longitude":"121.64253565338","latitude":"31.128566732109","speed":"62","address":"上海市浦东新区申江南路","status":"行驶"},{"tracetime":"2017-09-15 19:35:36","longitude":"121.6447063285","latitude":"31.121284136686","speed":"52","address":"上海市浦东新区申江南路辅路","status":"行驶"},{"tracetime":"2017-09-15 19:36:02","longitude":"121.64635824607","latitude":"31.117477836377","speed":"67","address":"上海市浦东新区申江路653号","status":"行驶"}],"status":"success"}';
//		var location = Ext.util.JSON.decode(traceMockData);
    	var location = [];
    	var json = Ext.encode(input);
    	Ext.Ajax.request({
            url : 'vehicle/monitor/findVehicleHistoryTrackWithoutAddress',
            method : 'POST',
//            async : false,
            params:{json:json},
            success : function(response,options) {
                var location = Ext.util.JSON.decode(response.responseText);
                	
            	traceMap.clearOverlays();
            	
            	if (location.status == 'success') {
                    if(location.data.length == 0) {
                        Ext.MessageBox.alert("消息提示","未查询到轨迹记录！");
                        locationArrayList = new Array();
                        return;
                    }

        	        var BPointArr = new Array();
        	        var pstart, pend, startPoint, endPoint;
        	        var numArray = 0;

        	        locationArrayList = new Array();
        	        for (var i = 0; i < location.data.length; i++) {
        	            if (location.data[i].longitude != null
        	                && location.data[i].latitude != null) {
        	                locationArrayList[numArray] = location.data[i];
        	                numArray++;
        	            }
        	        }

        	        for (var i = 0; i < locationArrayList.length; i++) {
        	            pstart = new BMap.Point(locationArrayList[i].longitude,locationArrayList[i].latitude);
        	            BPointArr.push(new BMap.Point(locationArrayList[i].longitude,locationArrayList[i].latitude));
        	        }

        	        //设置起点和终点
        	        if (locationArrayList.length > 0) {
        	            startPoint = new BMap.Point(locationArrayList[0].longitude,locationArrayList[0].latitude);
        	            endPoint = new BMap.Point(locationArrayList[locationArrayList.length - 1].longitude,locationArrayList[locationArrayList.length - 1].latitude);

        	            var startMarker = new BMap.Marker(startPoint); // 创建起点标注
        	            startMarker.setIcon(new BMap.Icon(
        	                    'resources/images/icons/mappin/Map-Marker-Marker-Outside-Chartreuse-icon.png',
        	                    new BMap.Size(
        	                        32, 32)));
        	            var startLabel = new BMap.Label("跟踪起点",{offset : new BMap.Size(25, -15)});
        	            startMarker.setLabel(startLabel);
        	            traceMap.addOverlay(startMarker); // 将标注添加到地图中

        	            if(locationArrayList.length != 1){
        	                var endMarker = new BMap.Marker(endPoint); // 创建终点标注
        	                endMarker.setIcon(new BMap.Icon(
        	                        'resources/images/icons/mappin/Map-Marker-Marker-Outside-Azure-icon.png',
        	                        new BMap.Size(
        	                            32, 32)));
        	                var endLabel = new BMap.Label("跟踪终点",{offset : new BMap.Size(25, -15)});
        	                endMarker.setLabel(endLabel);
        	                traceMap.addOverlay(endMarker); // 将标注添加到地图中
        	            }
        	        }
        	        
        	        var polyline = new BMap.Polyline(
        	            BPointArr, {
        	                strokeColor : "blue",
        	                strokeWeight : 8,
        	                strokeOpacity : 0.8,
        	                icons:[draw_line_direction(10)]
        	            });
        	        traceMap.addOverlay(polyline);

        	        traceMap.setViewport(BPointArr); //先设置viewport，再设置中心点
        	        traceMap.zoomOut();										
        		} else {
        			Ext.MessageBox.show({
        				title : '异常提示',
        				msg : '未查询到历史记录！',
        				icon : Ext.MessageBox.ERROR,
        				buttons : Ext.Msg.OK
        			});
        		}
            },
        });
    	
//		console.log('location::' + JSON.stringify(location));
    	
		
    },
    
    //打开回车登记窗口
    openBackOrder: function(grid, rowIndex, colIndex){
    	var orderInfo = grid.getStore().getAt(rowIndex);
    	var win = Ext.create("Admin.view.ordermgmt.orderlist.BackOrder");
//    	win.down("form").loadRecord(orderInfo);
    	var secretLevel = orderInfo.data.secretLevel;
    	var id = orderInfo.data.id;
    	
    	var form = win.down("form");
    	if(secretLevel == 3){	//免审订单
			form.getForm().findField("stMileage").hide();
			form.queryById("backorderEdMileageField").hide();
			
			form.getForm().findField("driverName").hide();
			form.getForm().findField("driverPhone").hide();
			
			form.getForm().findField("fromPlace").hide();
			form.getForm().findField("toPlace").hide();
			
			form.queryById("viewOrder_audit_id").hide();
			form.getForm().findField("auditUserName").hide();
			form.getForm().findField("auditStatus").hide();
			
			form.getForm().findField("passengerNum").hide();
			
		}
		
		win.down("form").getForm().findField("id").setValue(id);
    	
    	
    	
    	Ext.Ajax.request({
        	url:'order/' + id + '/queryVehReturnRegistByOrderNo',//?json='+Ext.encode(input),
			method:'GET',
			asycn:false,
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
			success: function(res){
				var resp=Ext.JSON.decode(res.responseText);
				if (resp.status == 'success') {
					var form = win.down("form");
					
					form.getForm().setValues(resp.data);
					
					var startTime = resp.data.factStTimeF;
	    			var startDate = new Date(startTime.replace(/-/,"/")) ;
	    			
	    			var field = form.getForm().findField("date");
	    			field.setMinValue(startDate);
	    			
				}
			 },
//			failure : function() {
//				Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			},
        });
    	
    	win.secretLevel = secretLevel;
    	win.show();
    },
    
    //回车登记
    backOrder : function(btn) {
		var data = btn.up("window").down('form').getValues();
		var secretLevel = btn.up("window").secretLevel;
		
		if(secretLevel == 3){
			
		}else{
			//判断初始里程是否填写
	    	if(data.edMileage == null ||　data.edMileage　== ""){
	    		Ext.Msg.alert("提示信息", "请填写结束里程！");
				return;
	    	}else{
	    		var reg = /^\d{1,10}$/;
	    		var regExp = new RegExp(reg);
	    		if(!regExp.test(data.edMileage)){
	    		　　	Ext.Msg.alert("提示信息", "请输入正确的里程（不支持小数）");	
	    		　　	return;	
	    		}
	    	}
		}
    	
    	if(data.date == null ||　data.date　== "" || data.hour == null ||　data.hour　== "" 
    		|| data.minute == null ||　data.minute　== "" ){
    		Ext.Msg.alert("提示信息", "请选择结束时间！");
			return;
    	}
	
		var endDate = Ext.getCmp("backorder_time_id").getValue();
		var date = (endDate.getFullYear()) + "-" +  (endDate.getMonth() + 1) + "-" + (endDate.getDate());
		
		var endHour = Ext.getCmp("backorder_hour_id").getValue();
		var endMinute = Ext.getCmp("backorder_minute_id").getValue();
		var factEdTimeF = date + ' ' + endHour + ':' + endMinute + ":00";	//手动加上秒
		data.factEdTimeF = factEdTimeF;
		
		delete data.date;
		delete data.hour;
		delete data.minute;
		
		var json = Ext.encode(data);
		Ext.Ajax.request({
        	url:'order/updateVehReturnRegist',//?json='+Ext.encode(input),
			method:'POST',
	        params:{json:json},
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
			success: function(res){
				var resp=Ext.JSON.decode(res.responseText);
				if (resp.status == 'success') {
					Ext.Msg.alert("提示信息", '该订单已登记回车！');
					btn.up("window").close();
					Ext.getCmp("gridorderlist_id").getStore('ordersResults').load();
				}
			 },
//			failure : function() {
//				Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			},
			scope: this
        });
	},
	
	selectBackOrderTime : function(field,value,eOpts) {
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
				case 'backorder_time_id':
					Ext.getCmp('backorder_hour_id').setValue('00');
					Ext.getCmp('backorder_hour_id').setStore(comstore);
					Ext.getCmp('backorder_minute_id').setMaxValue('59');
					Ext.getCmp('backorder_minute_id').setValue('0');
				break;
			}
		} else {
			this.selectBackOrderHourAndMinute(field.id);
		}
	},
	
	selectBackOrderHourAndMinute : function(id) {
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
			case 'backorder_time_id':
				Ext.getCmp('backorder_hour_id').setValue(nowHour);
				Ext.getCmp('backorder_hour_id').setStore(comstore);
				Ext.getCmp('backorder_minute_id').setMaxValue(nowMinute);
				Ext.getCmp('backorder_minute_id').setValue(nowMinute);
			break;
			default:
			break;
		}
	},

	selectBackOrderHour: function(me,record,eOpts ){	
		var nowHour = new Date().getHours();
		var nowMinute = new Date().getMinutes();		
		var nowDate = Ext.util.Format.date(new Date(),'Y-m-d');

		switch (me.id)
		{
			case 'backorder_hour_id':
				var callTime = Ext.util.Format.date(Ext.getCmp('backorder_time_id').getValue(),'Y-m-d');
				if (callTime == nowDate) {
					if(nowHour == record.data.time){						
						Ext.getCmp('backorder_minute_id').setMaxValue(nowMinute);
						Ext.getCmp('backorder_minute_id').setValue(nowMinute);
					}else{
						Ext.getCmp('backorder_minute_id').setMaxValue('59');						
					}
				}
			break;
		}
	},
});

function datetimeFormat(longTypeDate){  
    var datetimeType = "";  
    var date = new Date();  
    date.setTime(longTypeDate);  
    datetimeType+= date.getFullYear();   //年  
    datetimeType+= "-" + getMonth(date); //月   
    datetimeType += "-" + getDay(date);   //日  
    datetimeType+= " " + getHours(date);   //时  
    datetimeType+= ":" + getMinutes(date);      //分
    datetimeType+= ":" + getSeconds(date);      //分
    return datetimeType;
} 

//返回 01-12 的月份值   
function getMonth(date){  
    var month = "";  
    month = date.getMonth() + 1; //getMonth()得到的月份是0-11  
    if(month<10){  
        month = "0" + month;  
    }  
    return month;  
}  
//返回01-30的日期  
function getDay(date){  
    var day = "";  
    day = date.getDate();  
    if(day<10){  
        day = "0" + day;  
    }  
    return day;  
}
//返回小时
function getHours(date){
    var hours = "";
    hours = date.getHours();
    if(hours<10){  
        hours = "0" + hours;  
    }  
    return hours;  
}
//返回分
function getMinutes(date){
    var minute = "";
    minute = date.getMinutes();
    if(minute<10){  
        minute = "0" + minute;  
    }  
    return minute;  
}
//返回秒
function getSeconds(date){
    var second = "";
    second = date.getSeconds();
    if(second<10){  
        second = "0" + second;  
    }  
    return second;  
}

function GetDateDiff(startTime, endTime, diffType) {
    //将xxxx-xx-xx的时间格式，转换为 xxxx/xx/xx的格式 
    startTime = startTime.replace(/\-/g, "/");
    endTime = endTime.replace(/\-/g, "/");
    //将计算间隔类性字符转换为小写
    diffType = diffType.toLowerCase();
    var sTime =new Date(startTime); //开始时间
    var eTime =new Date(endTime); //结束时间
    //作为除数的数字
    var timeType =1;
    switch (diffType) {
        case"second":
            timeType =1000;
        break;
        case"minute":
            timeType =1000*60;
        break;
        case"hour":
            timeType =1000*3600;
        break;
        case"day":
            timeType =1000*3600*24;
        break;
        default:
        break;
    }
    return parseInt((eTime.getTime() - sTime.getTime()) / parseInt(timeType));
}

function draw_line_direction(weight) {
    var icons=new BMap.IconSequence(
        new BMap.Symbol(BMap_Symbol_SHAPE_BACKWARD_CLOSED_ARROW, {
            scale: weight/20,
            strokeWeight: 1,
            rotation: 0,
            fillColor: 'white',
            fillOpacity: 1,
            strokeColor:'white'
        }),'100%','5%',false);
    console.log(icons);
    return icons;
}
