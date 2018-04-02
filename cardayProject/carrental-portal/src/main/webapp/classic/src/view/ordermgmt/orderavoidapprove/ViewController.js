/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderavoidapprove.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
	        'Admin.view.ordermgmt.orderavoidapprove.GridOrderList',
	        'Admin.view.ordermgmt.orderavoidapprove.View',
	        'Admin.view.ordermgmt.orderavoidapprove.SearchForm',
	        'Admin.view.ordermgmt.orderavoidapprove.ViewOrder'
			],
	alias : 'controller.orderavoidapprovecontroller',
	
	//每次load数据会调用该方法
	onBeforeLoad : function() {
		//所属部门
		var frmValues=this.lookupReference('searchForm').getValues();
		if(''==frmValues['deptId']||null==frmValues['deptId']){
    		frmValues['deptId']='-1';
    	}
		if(this.lookupReference('searchForm').getForm().isValid()){
			var page=Ext.getCmp('avoidapproveorderlistpage').store.currentPage;
			var limit=Ext.getCmp('avoidapproveorderlistpage').store.pageSize;
			var parm = this.lookupReference('searchForm').getValues();
			parm = {
				    "currentPage" 	: page,
					"numPerPage" : limit,
					'orderNo': 	parm.orderNo,
				    'organizationId': parm.organizationId,
					'orderTime': parm.orderTime,
					'selfDept': parm.includeSelf || false,
    				'childDept': parm.includeChild || false,
    				'organizationId' : parm.deptId,
    				'secretLevel' : '3',
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
		Ext.getCmp('avoidapproveorderlistpage').getStore().currentPage = 1;
		this.getViewModel().getStore("ordersResults").load();
	},
	
	//查看订单信息
	viweOrder : function(grid, rowIndex, colIndex) {
		var orderInfo = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("orderavoidapprovewin");
		win.down("form").loadRecord(orderInfo);
		win.show();
	},
	
	
	avoidApproveOrder : function() {
		var win = Ext.widget('addorderavoidapprovewin');
		win.down('form').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
		var today = new Date();
		win.down('form').getForm().findField('applyTime').setValue(today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate()) ;
		win.show();
	},
	
	//选择用车人
	selectOrderUser : function() {
		Ext.Ajax.request({
			url: 'employee/listByDept',
	        method : 'POST',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				if (Ext.getCmp("orderavoidapprove_orderUser_id")) {
					Ext.getCmp("orderavoidapprove_orderUser_id").setStore(respText);
				}
				if (Ext.getCmp("edit_orderavoidapprove_orderUser_id")) {
					Ext.getCmp("edit_orderavoidapprove_orderUser_id").setStore(respText);
				}
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

				var orderUserOrgId = '';
				if (Ext.getCmp('edit_orderavoidapprove_orderUserOrganizationId_id')) {
					Ext.getCmp("edit_orderavoidapprove_orderUserphone_id").setValue(respText.data.phone);	
					Ext.getCmp("edit_orderavoidapprove_orderUserOrganizationId_id").setValue(respText.data.organizationId);
					Ext.getCmp('edit_orderavoidapprove_vehicle_id').setValue('');
					orderUserOrgId = Ext.getCmp('edit_orderavoidapprove_orderUserOrganizationId_id').getValue();
				}
				if (Ext.getCmp('orderavoidapprove_orderUserOrganizationId_id')) {					
					Ext.getCmp("orderavoidapprove_orderUserphone_id").setValue(respText.data.phone);	
					Ext.getCmp("orderavoidapprove_orderUserOrganizationId_id").setValue(respText.data.organizationId);
					orderUserOrgId = Ext.getCmp('orderavoidapprove_orderUserOrganizationId_id').getValue();
					Ext.getCmp("orderavoidapprove_vehicle_id").setValue('');
				}

				//var url = 'vehicle/'+ orderUserOrgId + '/listDeptVehicle';
				var url = 'vehicle/'+ orderUserOrgId + '/listDeptVehicle/NoNeedApprove';
				
				Ext.Ajax.request({
					url: url,
			        method : 'GET',
			        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			        success : function(response,options) {
						var respText = Ext.util.JSON.decode(response.responseText);
						if (Ext.getCmp("orderavoidapprove_vehicle_id")) {
							Ext.getCmp("orderavoidapprove_vehicle_id").setStore(respText);
						}
						if (Ext.getCmp('edit_orderavoidapprove_vehicle_id')) {
							Ext.getCmp('edit_orderavoidapprove_vehicle_id').setStore(respText);
						}
			        }
			        /*,
			        failure : function() {
			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
			        },*/
			    });		
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	
	//提交订单
	onSubmit : function(btn) {
		//var orderInfo = this.getView().down('form').getValues();
		var orderInfo = this.getView().down('form').getForm();
		var input = {
	            "unitName": orderInfo.findField('deptName').getValue(),      
				"orderUserid": orderInfo.findField('orderUser').getValue(),
				"orderUserphone": orderInfo.findField('orderUserphone').getValue(),
				"orderReason": orderInfo.findField('orderReason').getValue(),
				"vehicleId":orderInfo.findField('vehicleNumber').getValue(),
				"secretLevel":"3"
			};
		var json = Ext.encode(input);
	    Ext.Ajax.request({
			url : 'order/createWithoutApproval',
	        method : 'POST',
	        params:{json:json},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				if (respText.status == 'success') {
					btn.up('addorderavoidapprovewin').close();
					Ext.Msg.alert('消息提示', '订单已提交成功！');
					Ext.getCmp("orderavoidapprove_grid_id").getStore('ordersResults').load();
				} else {
					Ext.Msg.alert('消息提示', respText.data);
				}
	        },
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	        scope: this,
	    });
	},
	
	
	/******多级部门筛选*******/
	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.ordermgmt.orderavoidapprove.DeptChooseWin",{
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
     	var form = Ext.getCmp("orderavoidapprove_searchform_id").getForm();
     	form.findField("deptId").setValue(deptId);
     	form.findField("deptName").setValue(deptName);
     	btn.up("window").close();
     },
});
