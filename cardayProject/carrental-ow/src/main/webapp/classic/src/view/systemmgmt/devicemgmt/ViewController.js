/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.devicemgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.systemmgmt.devicemgmt.View',
			'Admin.view.systemmgmt.devicemgmt.SearchForm',
			'Admin.view.systemmgmt.devicemgmt.AddDevice',
			'Admin.view.systemmgmt.devicemgmt.EditDevice',
			],
	alias : 'controller.devicemgmtcontroller',

	onBeforeLoad : function() {

		var parm = this.lookupReference('searchForm').getValues();
		
		if (parm.deviceStatus == '全部' || parm.deviceStatus == '0' ) {
			parm.deviceStatus = '';
		}

		if (parm.deviceVendor == '全部' || parm.deviceVendor == '0') {
			parm.deviceVendor = '';
		}
		
		parm.currentPage = Ext.getCmp('griddeviceid').store.currentPage;
		parm.numPerPage = Ext.getCmp('griddeviceid').store.pageSize;
		
		parm = Ext.encode(parm);		
		this.getViewModel().getStore("devicesResults").proxy.extraParams = {
			"json" : parm
		}
	},
	
	onSearchClick : function() {
		//在load()之前会调用onBeforeLoad方法
		Ext.getCmp('griddeviceid').getStore().currentPage = 1;
		this.getViewModel().getStore("devicesResults").load();
	},
	
	//添加设备
	onAddClick : function() {
		win = Ext.widget('adddevice');
		win.show();
	},

	//完成设备添加
	onAddClickDone : function(btn) {
		var deviceInfo = this.getView().down('form').getForm().getValues();
		// display栏目值设定
		deviceInfo.vehicleSource = Ext.getCmp('addVehicleSource').getValue();
		deviceInfo.vehicleVin = Ext.getCmp('addVehicleVin').getValue();
		deviceInfo.limitSpeed = Ext.getCmp('addLimitSpeed').getValue();
		deviceInfo.latestLimitSpeed = Ext.getCmp('addLatestLimitSpeed').getValue();

		var inputvVehicleNumber = Ext.getCmp('add_device_vehicleNumber_id').getRawValue();
		
		switch(deviceInfo.deviceStatus) {
		case '正常':
			deviceInfo.deviceStatus = 1;
			break;
		case '未配置':
			deviceInfo.deviceStatus = 2;
			break;
		case '故障':
			deviceInfo.deviceStatus = 3;
			break;
		}
		
		var json = Ext.encode(deviceInfo);
		
		
		// 查询车牌号是否存在
		var input= {
				'vehicleNumber': inputvVehicleNumber
		};
		var findVehicleJson = Ext.encode(input);
		Ext.Ajax.request({
	   		url: 'vehicle/findVehicleByVehicleNumber',//?json='+ Ext.encode(deviceInfo),
	        method : 'GET',
	        params:{json:findVehicleJson},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var data = respText.data; 
	        	if (deviceInfo.vehicleNumber!="" && data == null) {
	        		Ext.Msg.alert("提示信息", "绑定的车牌号不存在，请重新填写！");
	    			return;
	        	} else {
	        		if (deviceInfo.vehicleNumber!="" && 
	        				((deviceInfo.vehicleSource=="" && deviceInfo.vehicleVin=="")
	        					||(deviceInfo.vehicleSource==null && deviceInfo.vehicleVin==null))) {
	        			Ext.Msg.alert("提示信息", "该车牌号已经绑定!");
	        			return;
	        		}
	        		Ext.Ajax.request({
	        			url : 'device/create',//?json='+ Ext.encode(deviceInfo),
	        			method : 'POST',
	        			params:{json:json},
	        			//defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        			success : function(response,options) {
	        				var respText = Ext.util.JSON.decode(response.responseText);
	        				var retStatus = respText.status;
	        				if (retStatus == 'success') {
	        					btn.up('adddevice').close();
	        					Ext.Msg.alert('提示信息','添加成功');
	        					Ext.getCmp("griddeviceid").getStore('devicesResults').load();
	        				} else if (retStatus == 'sendCmdFail') {
	        					// 修改成功,下发限速失败的时候
	        					btn.up('adddevice').close();
	        					Ext.Msg.alert('提示信息',respText.msg);
	        					Ext.getCmp("griddeviceid").getStore('devicesResults').load();
	        				} else if (retStatus == 'failure') {
	        					Ext.Msg.alert('提示信息',respText.msg);
	        				}
	        			}
	        			/*,
				        failure : function() {
				            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
				        }*/
	        		});
	        	}
	        },
	        scope:this
		});
	},
	
	//车牌号修改，车辆来源、车辆VIN号置为空
	vehicleNumberChange : function() {
		if (Ext.getCmp('edit_device_id')) {
			var form = Ext.getCmp('edit_device_id').down('form').getForm();
	    	form.findField('vehicleSource').setValue(null);
	    	form.findField('vehicleIdentification').setValue(null);
	    	form.findField('vehicleId').setValue(null);
	    	form.findField('limitSpeed').setValue(null);
		}
		
		if (Ext.getCmp('add_device_id')) {
			var form = Ext.getCmp('add_device_id').down('form').getForm();
	    	form.findField('vehicleSource').setValue(null);
	    	form.findField('vehicleVin').setValue(null);
	    	form.findField('vehicleId').setValue(null);
	    	form.findField('limitSpeed').setValue(null);
		}
		
	},
	
	//选择车牌还，自动补齐车辆来源、车辆VIN号
	fillVehicleInfo : function(combo , record , eOpts) {
		//console.log('+++fillVehicleInfo+++' + record.data.id);
		Ext.Ajax.request({
			url : 'vehicle/monitor/' + record.data.id + '/update',
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var vehicleSource = "";
	        	if (respText.data.entName != null) {
	        		vehicleSource = respText.data.entName;
	        	} else if (respText.data.rentName != null) {
	        		vehicleSource = respText.data.rentName;
	        	}
	        	var vehicleVin = respText.data.vehicleIdentification;
	        	var vehicleId = respText.data.id;
	        	var limitSpeed = respText.data.limitSpeed;
	        	var latestLimitSpeed = respText.data.latestLimitSpeed;
	        	if (Ext.getCmp('add_device_id')) {
	        		var form = Ext.getCmp('add_device_id').down('form').getForm();
		        	form.findField('vehicleSource').setValue(vehicleSource);
		        	form.findField('vehicleVin').setValue(vehicleVin);
		        	form.findField('vehicleId').setValue(vehicleId);
		        	form.findField('limitSpeed').setValue(limitSpeed);
		        	form.findField('latestLimitSpeed').setValue(latestLimitSpeed);
	        	}
	        	
	        	if (Ext.getCmp('edit_device_id')) {
	        		var form = Ext.getCmp('edit_device_id').down('form').getForm();
		        	form.findField('vehicleSource').setValue(vehicleSource);
		        	form.findField('vehicleIdentification').setValue(vehicleVin);
		        	form.findField('vehicleId').setValue(vehicleId);
		        	form.findField('limitSpeed').setValue(limitSpeed);
		        	form.findField('latestLimitSpeed').setValue(latestLimitSpeed);
	        	}
	        	
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        }*/
	    });
	},
	
	//打开批量导入窗口
	openFileUpWindow : function() {
		var win = Ext.create('Admin.view.systemmgmt.devicemgmt.FileUpLoad');
		win.show();
	},
	
	//导入文件
	uploadCSV:function(btn){
		var formPanel=this.getView().down('form');
		var form=this.getView().down('form').getForm();
		if (form.isValid()) {
            form.submit({
                url: 'device/import',
                method:'post',
                waitMsg: 'Uploading your file...',
                success: function(form,action) {
                    btn.up('window').close();
            		//Ext.getCmp("searchVehicle").fireEvent("click");
        		 	Ext.MessageBox.show({
	                    title: '消息提示',
	                    msg: action.result.msg,
	                    icon: Ext.MessageBox.INFO,
	                    buttons: Ext.Msg.OK
                	});
        		 	Ext.getCmp("griddeviceid").getStore('devicesResults').load();
                }
            });
			
		}
	},
	
	//查看终端设备信信息
	viewDevice : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("viewdevice");
		win.down("form").loadRecord(rec);
		win.show();
	},
	
	//修改终端设备信息,打开修改窗口
	editDevice : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("editdevice");
		win.down("form").loadRecord(rec);
		win.show();
		Ext.getCmp("edit_device_vehicleId_id").setValue(rec.data.vehicleId);
	},
	
	//提交修改信息
	onEditClickDone : function(btn) {
		var deviceInfo = this.getView().down('form').getForm().getValues();
		// display栏目值设定
		deviceInfo.vehicleSource = Ext.getCmp('vehicleSource').getValue();
		deviceInfo.vehicleIdentification = Ext.getCmp('vehicleIdentification').getValue();
		deviceInfo.limitSpeed = Ext.getCmp('limitSpeed').getValue();
		deviceInfo.commandStatus = Ext.getCmp('commandStatus').getValue();
		deviceInfo.latestLimitSpeed = Ext.getCmp('latestLimitSpeed').getValue();
		
		deviceInfo.imeiNumber = Ext.getCmp('edit_imeiNumber_id').getValue();
		deviceInfo.iccidNumber = Ext.getCmp('iccidNumber_id').getValue();
		deviceInfo.snNumber = Ext.getCmp('edit_snNumber_id').getValue();
		var json = Ext.encode(deviceInfo);
		
		var inputvVehicleNumber = Ext.getCmp('edit_device_vehicleNumber_id').getRawValue();
		
		// 查询车牌号是否存在
		var input= {
				'vehicleNumber': inputvVehicleNumber
		};
		var findVehicleJson = Ext.encode(input);
		Ext.Ajax.request({
	   		url: 'vehicle/findVehicleByVehicleNumber',//?json='+ Ext.encode(deviceInfo),
	        method : 'GET',
	        params:{json:findVehicleJson},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var data = respText.data; 
	        	if (deviceInfo.vehicleNumber!="" && data == null) {
	        		Ext.Msg.alert("提示信息", "绑定的车牌号不存在，请重新填写！");
	    			return;
	        	} else {
	        		if (deviceInfo.vehicleNumber!="" && 
	        				((deviceInfo.vehicleSource=="" && deviceInfo.vehicleIdentification=="")
	        					|| (deviceInfo.vehicleSource==null && deviceInfo.vehicleIdentification==null))) {
	        			Ext.Msg.alert("提示信息", "该车牌号已经绑定!");
	        			return;
	        		}
	        		
	        		// 更新设备
	        		Ext.Ajax.request({
	        	   		url: 'device/update',//?json='+ Ext.encode(deviceInfo),
	        	        method : 'POST',
	        	        params:{json:json},
	        	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        	        success : function(response,options) {
	        	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	        	var retStatus = respText.status;
	        				if (retStatus == 'success') {
	        					btn.up('editdevice').close();
	        					Ext.Msg.alert('提示信息','修改成功');
	        					Ext.getCmp("griddeviceid").getStore('devicesResults').load();
	        				} else if (retStatus == 'sendCmdFail') {
	        					// 修改成功,下发限速失败的时候
	        					btn.up('editdevice').close();
	        					Ext.Msg.alert('提示信息',respText.msg);
	        					Ext.getCmp("griddeviceid").getStore('devicesResults').load();
	        				} else {
	        					Ext.Msg.alert('提示信息',respText.msg);
	        				}      	
	        	        },
	        	        /*failure : function() {
	        	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        	        },*/
	        	        scope:this
	        		});
	        	}
	        },
	        scope:this
		});
	},
	
	//license绑定
    licenseBind : function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
    	var input = {
    			"deviceId": rec.data.id,
    			"imeiNumber":rec.data.imeiNumber,
    			"iccidNumber": rec.data.iccidNumber,
    			"snNumber": rec.data.snNumber,
    			"deviceVendor": rec.data.deviceVendor
    	}
		var json = Ext.encode(input);
		Ext.Ajax.request({
			url : 'device/licenseBind',//?json='+ Ext.encode(input),
			method : 'POST',
	        params:{json:json},
			//defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			success : function(response,options) {
				var data = Ext.util.JSON.decode(response.responseText);
				if (data.status == "success") {
					Ext.Msg.alert('提示信息','绑定成功！');
					Ext.getCmp("griddeviceid").getStore('devicesResults').load();
				} else {
					Ext.Msg.alert('提示信息', data.msg);
					return;
				}
			}
	        /*,
			failure : function() {
				Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
			},*/
		});
    },
    
    //license激活，选择服务时间
    licenseActive : function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
    	win = Ext.widget('licenseActivateWin');
    	win.down("form").loadRecord(rec);
		win.show();
    },
    
	//license激活
    licenseActiveDone : function(grid, rowIndex, colIndex) {
    	var start_date = Ext.getCmp('license_start_time_id').getValue();
    	var end_date = Ext.getCmp('license_end_time_id').getValue();
    	start_date = Ext.util.Format.date(start_date,'Y-m-d');
    	end_date = Ext.util.Format.date(end_date,'Y-m-d');
    	//end_date = Ext.util.Format.date(end_date,'Y-m-d H:m:s');
    	var userId = window.sessionStorage.getItem("userId");
        
		var input = {
				"deviceId": Ext.getCmp('active_deviceId_id').getValue(),
        		"licenseNo" : Ext.getCmp('active_licenseNo_id').getValue(),
        		"startDate" : start_date,
        		"endDate" : end_date,
        		"userId" : userId,
			};
		var json = Ext.encode(input);
		Ext.Ajax.request({
			url : 'device/licenseActive',//?json='+ Ext.encode(input),
			method : 'POST',
	        params:{json:json},
			//defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			success : function(response,options) {
						var data = Ext.util.JSON.decode(response.responseText);
						if (data.status == "success") {
							Ext.Msg.alert("消息提示", data.msg);
							Ext.getCmp('licenseActivateWin_id').close();
							Ext.getCmp("griddeviceid").getStore('devicesResults').load();
						} else {
							Ext.Msg.alert("消息提示", data.msg);
							Ext.getCmp('licenseActivateWin').close();
							return;
						}
					}
	        /*,
			failure : function() {
				Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
			},*/
		});
    },
    
  //license挂起
    licenseSuspend : function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
    	var userId = window.sessionStorage.getItem("userId");
    	var input = {
    			"deviceId": rec.data.id,
    			"licenseNo":rec.data.licenseNumber,
    			"userId": userId,
    	};

		var json = Ext.encode(input);
		Ext.Ajax.request({
			url : 'device/licenseSuspend',//?json='+ Ext.encode(input),
			method : 'POST',
			params:{json:json},
			//defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			success : function(response,options) {
						var data = Ext.util.JSON.decode(response.responseText);
						if (data.status == "success") {
							Ext.Msg.alert("消息提示", data.msg);
							Ext.getCmp("griddeviceid").getStore('devicesResults').load();
						} else {
							Ext.Msg.alert("消息提示", data.msg);
							return;
						}
					}
			/*,
			failure : function() {
				Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
			},*/
		});
    },
    
    //license重新激活，选择服务时间
    licenseReactive : function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
    	win = Ext.widget('licenseReactivateWin');
    	win.down("form").loadRecord(rec);
		win.show();
    },
    
    //License重新激活
    licenseReactiveDone : function(grid, rowIndex, colIndex) {
    	var start_date = Ext.getCmp('re_license_start_time_id').getValue();
    	var end_date = Ext.getCmp('re_license_end_time_id').getValue();
    	start_date = Ext.util.Format.date(start_date,'Y-m-d');
    	end_date = Ext.util.Format.date(end_date,'Y-m-d');
    	//end_date = Ext.util.Format.date(end_date,'Y-m-d H:m:s');
    	var userId = window.sessionStorage.getItem("userId");
        
		var input = {
				"deviceId": Ext.getCmp('reactive_deviceId_id').getValue(),
        		"licenseNo" : Ext.getCmp('reactive_licenseNo_id').getValue(),
        		"startDate" : start_date,
        		"endDate" : end_date,
        		"userId" : userId,
			};
	    var json = Ext.encode(input);
		Ext.Ajax.request({
			url : 'device/licenseReactive',//?json='+ Ext.encode(input),
			method : 'POST',
			params:{json:json},
			//defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			success : function(response,options) {
						var data = Ext.util.JSON.decode(response.responseText);
						if (data.status == "success") {
							Ext.Msg.alert("消息提示", data.msg);
							Ext.getCmp('licenseReactivateWin_id').close();
							Ext.getCmp("griddeviceid").getStore('devicesResults').load();
						} else {
							Ext.Msg.alert("消息提示", data.msg);
							Ext.getCmp('licenseReactivateWin').close();
							return;
						}
					}
			/*,
			failure : function() {
				Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
			},*/
		});
    },
    
    //License终止
    licenseTerminated : function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
    	var userId = window.sessionStorage.getItem("userId");
    	var input = {
    			"deviceId": rec.data.id,
    			"licenseNo":rec.data.licenseNumber,
    			"userId": userId,
    	};
    	var json = Ext.encode(input);
		Ext.Ajax.request({
			url : 'device/licenseTerminated',//?json='+ Ext.encode(input),
			method : 'POST',
	        params:{json:json},
			//defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			success : function(response,options) {
						var data = Ext.util.JSON.decode(response.responseText);
						if (data.status == "success") {
							Ext.Msg.alert("消息提示", data.msg);
							Ext.getCmp("griddeviceid").getStore('devicesResults').load();
						} else {
							Ext.Msg.alert("消息提示", data.msg);
							return;
						}
					}
	        /*,
			failure : function() {
				Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
			},*/
		});
    },
    
    //License解绑
    licenseUnbind : function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
    	var userId = window.sessionStorage.getItem("userId");
    	var input = {
    			"deviceId": rec.data.id,
    			"licenseNo":rec.data.licenseNumber,
    			"userId": userId,
    	};
    	var json = Ext.encode(input);
		Ext.Ajax.request({
			url : 'device/licenseUnbind',//?json='+ Ext.encode(input),
			method : 'POST',
	        params:{json:json},
			//defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			success : function(response,options) {
						var data = Ext.util.JSON.decode(response.responseText);
						if (data.status == "success") {
							Ext.Msg.alert("消息提示", data.msg);
							Ext.getCmp("griddeviceid").getStore('devicesResults').load();
						} else {
							Ext.Msg.alert("消息提示", data.msg);
							return;
						}
					}
	        /*,
			failure : function() {
				Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
			},*/
		});
    },
    
});
