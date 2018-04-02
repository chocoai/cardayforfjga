/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.drivermgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.systemmgmt.drivermgmt.View',
			'Admin.view.systemmgmt.drivermgmt.SearchForm',
			'Admin.view.systemmgmt.drivermgmt.AddDriver',
			],
	alias : 'controller.drivermgmtcontroller',		

	
	onSearchClick : function() {
		Ext.getCmp('griddriverid').getStore().currentPage = 1;
		this.getViewModel().getStore("driversResults").load();	
	},
	
	onBeforeLoad : function() {
		var parm = this.lookupReference('searchForm').getValues();
		if (typeof parm.organizationId == 'string') {
			if (parm.organizationId == '0' || parm.organizationId == '全部') {
				parm.organizationId = '';
			} else if (parm.organizationId == '-1' || parm.organizationId == '未分配') {
				parm.organizationId = -1;
			}
		}
		parm.currentPage = Ext.getCmp('grid_driver_store_id').store.currentPage;
		parm.numPerPage = Ext.getCmp('grid_driver_store_id').store.pageSize;
		parm = Ext.encode(parm);		
		this.getViewModel().getStore("driversResults").proxy.extraParams = {
			"json" : parm
		}
	},
	
	//点击添加司机按钮
	onAddClick : function() {
		//Ext.Msg.alert("Message Box", "添加按钮");
		win = Ext.widget('adddriver');
		win.show();
	},
	
	//添加，修改司机选择企业
	selectOrg : function() {
		var organizationName = window.sessionStorage.getItem("organizationName");
		if (Ext.getCmp("add_driver_org_id")) {
			Ext.getCmp("add_driver_org_id").setValue(organizationName);
			return;
		}
		
		if (Ext.getCmp("editdriver_org_id")) {
			Ext.getCmp("editdriver_org_id").setValue(organizationName);
			return;
		}
	},
	
	//添加，修改司机，设置企业ID
	selectOrgId : function() {
		var organizationId = window.sessionStorage.getItem("organizationId");
		if (Ext.getCmp("add_driver_org_id_id")) {
			Ext.getCmp("add_driver_org_id_id").setValue(organizationId);
			return;
		}
		
		if (Ext.getCmp("editdriver_org_id_id")) {
			Ext.getCmp("editdriver_org_id_id").setValue(organizationId);
			return;
		}
	},
	
	//添加，修改司机，设置部门ID
	selectDepId : function() {
		if (Ext.getCmp("add_driver_dept_id_id")) {
			if (Ext.getCmp("add_driver_dept_id").getValue() == '暂未分配') {
				Ext.getCmp("add_driver_dept_id_id").setValue(-1);
			}
			return;
		}
		
		if (Ext.getCmp("edit_driver_dept_id_id")) {
			if (Ext.getCmp("edit_driver_dept_id").getValue() == '暂未分配') {
				Ext.getCmp("edit_driver_dept_id_id").setValue(-1);
			}
			return;
		}
	},

	//添加,修改司机，选择部门
	selectDeptment : function() {
		var userCategory = window.sessionStorage.getItem("userType");
		
		//企业管理员,如果司机没有分配部门，就属于企业，前台显示 暂未分配。
		if (userCategory == 2 || userCategory == 6) {
			Ext.Ajax.request({
		   		url: 'department/list',
		        method : 'GET',
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var organizationId = window.sessionStorage.getItem('organizationId');
					for (var i=0; i<respText.data.length; i++) {
						if (respText.data[i].id == organizationId) {
							respText.data[i].name = '暂未分配';
							respText.data[i].id = -1;
						}
					}
					if (Ext.getCmp('add_driver_dept_id')) {
						Ext.getCmp('add_driver_dept_id').setStore(respText);
						return;
					}
					
					if (Ext.getCmp('edit_driver_dept_id')) {
						Ext.getCmp('edit_driver_dept_id').setStore(respText);
						return;
					}
					
		        }
		        /*,
		        failure : function() {
		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
		        },*/
		    });
		}
	},
	
	//添加司机，选择站点
	selectStation : function() {
		var userCategory = window.sessionStorage.getItem("userType");
		
		//企业管理员,如果司机没有分配部门，就属于企业，前台显示 暂未分配。
			Ext.Ajax.request({
		   		url: 'station/listByOrgId',
		        method : 'POST',
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					if (respText.data != null) {
						var length = respText.data.length;
						var temp = new Array();
						for (var i=0; i<length; i++) {
							temp[i] = respText.data[i];
						}
						respText.data[0] = {'id':'-1', 'stationName': '暂未分配'};
						for (var i=1; i<length+1; i++) {
							respText.data[i] = temp[i-1];
						}
						
						if (Ext.getCmp('add_driver_station_id')) {
							Ext.getCmp('add_driver_station_id').setStore(respText);
							return;
						}
						
						if (Ext.getCmp('edit_driver_station_id')) {
							Ext.getCmp('edit_driver_station_id').setStore(respText);
							return;
						}
						
					} else {
						respText.data = {'id':'-1', 'stationName': '暂未分配'};
						if (Ext.getCmp('add_driver_station_id')) {
							Ext.getCmp('add_driver_station_id').setStore(respText);
							return;
						}
						
						if (Ext.getCmp('edit_driver_station_id')) {
							Ext.getCmp('edit_driver_station_id').setStore(respText);
							return;
						}
					}
		        }
		        /*,
		        failure : function() {
		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
		        },*/
		    });
	},
	
	//完成司机信息添加
	onAddClickDone : function(btn) {
		var driverInfo = this.getView().down('form').getValues();

		var form=this.getView().down('form').getForm();
		if (form.isValid()) {
            form.submit({
                url: 'driver/create',
                method:'post',
          //      defaultHeaders : {'Content-type' : 'application/json;utf-8'},
                //async: true,
                waitMsg: 'Uploading your file...',
                success: function(form,action) {
                	console.log('成功'+action.response.responseText);
                	var result = Ext.util.JSON.decode(action.response.responseText);
                	btn.up('window').close();
                	Ext.Msg.alert('消息提示','添加成功!');
                	Ext.getCmp("griddriverid").getStore('driversResults').load();
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
	},
	
	//查看司机信息
	viewDriver : function(grid, rowIndex, colIndex) {
		console.log('viewDriver+++');
		var rec = grid.getStore().getAt(rowIndex);
		var driverId = rec.data.id;
		url = 'driver/'+driverId+'/update';
		
		var driverRet = new Ext.data.Model();
		//根据id查询员工信息
		Ext.Ajax.request({
	   		url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var data = respText.data;
	        	var retStatus = respText.status;
	        	//性别转换
	        	if (data.sex == '0') {
	        		data.sex = '男';
	        	} else if (data.sex == '1') {
	        		data.sex = '女';
	        	}
	    		
	        	data.drivingYears = data.drivingYears + '年';
	        	
	        	var reg=new RegExp("\\.0$");
	        	// 转换成带时区时间(IE下需要此过程)
	        	data.birthday = Ext.Date.parseDate(data.birthday.replace(reg,""), 'Y-m-d H:i:s');
	        	data.licenseBegintime = Ext.Date.parseDate(data.licenseBegintime.replace(reg,""), 'Y-m-d H:i:s');
	        	data.licenseExpiretime = Ext.Date.parseDate(data.licenseExpiretime.replace(reg,""), 'Y-m-d H:i:s');
	        	// 时间转换成Y-m-d
	        	data.birthday = Ext.util.Format.date(data.birthday,'Y-m-d');
	        	data.licenseBegintime = Ext.util.Format.date(data.licenseBegintime,'Y-m-d');
	        	data.licenseExpiretime = Ext.util.Format.date(data.licenseExpiretime,'Y-m-d');
	        	
				if (retStatus == 'success') {
					var win = Ext.widget("viewdriver");
					//设置查看附件链接地址
					var licenseAttach = data.licenseAttach;
					if (licenseAttach !=null && licenseAttach !="") {
						url=window.sessionStorage.getItem("imageUrl")+'resources/upload/driver/'+licenseAttach;
					//	url = 'https://uat.car-day.cn/resources/upload/driver/'+licenseAttach;
					//	url = 'http://221.181.100.251:8899/resources/upload/driver/'+licenseAttach;
						var html = "<a href="+url+" target='_blank'>查看驾照附件</a>";
						Ext.getCmp('view_licenseAttach_id').setHtml(html);
						Ext.getCmp('view_licenseAttach_id').setHidden(false);
					} else if (licenseAttach==null || licenseAttach=="") {
						Ext.getCmp('view_licenseAttach_id').setHidden(true);
					}
		        	
					driverRet.data = data;
					win.down("form").loadRecord(driverRet);
					win.show();
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	//修改司机信息,根据ID查询司机信息
	editDriver : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var driverId = rec.data.id;
		url = 'driver/'+driverId+'/update';	
		var driverRet = new Ext.data.Model();
		
		Ext.Ajax.request({
	   		url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var data = respText.data;
	        	var retStatus = respText.status;
	        	//用户类型转换
	        	userCategory = data.userCategory;
	    		if (userCategory == '0') {
	    			userCategory ='超级管理员';
	    		} else if (userCategory == '1') {
	    			userCategory ='租户管理员';
	    		} else if (userCategory == '2') {
	    			userCategory ='企业管理员';
	    		} else if (userCategory == '3') {
	    			userCategory ='部门管理员';
	    		} else if (userCategory == '4') {
	    			userCategory ='普通员工';
	    		} else if (userCategory == '5') {
	    			userCategory ='司机';
	    		} 
	    		//司机所属部门
	    		if (data.depName==null || data.depName=='') {
//	    			data.depName = '暂未分配';
	    			var userType = window.sessionStorage.getItem('userType');
	    			if(userType == '2' || userType == '6'){
	    	      		//2是用车企业 6是租车企业
	    	      		//3是部门
	    	        	var entName = window.sessionStorage.getItem("organizationName");
	    	        	data.depName = entName;
	    	      	}
	    		}
	    		//司机所属站点
	    		if (data.stationName==null || data.stationName=='') {
	    			data.stationName = '暂未分配';
	    		}
	    		
	    		data.userCategory = userCategory;
	    		
	    		var reg=new RegExp("\\.0$");
	        	// 转换成带时区时间(IE下需要此过程)
	    		data.birthday = Ext.Date.parseDate(data.birthday.replace(reg,""), 'Y-m-d H:i:s');
	    		data.licenseBegintime = Ext.Date.parseDate(data.licenseBegintime.replace(reg,""), 'Y-m-d H:i:s');
	        	data.licenseExpiretime = Ext.Date.parseDate(data.licenseExpiretime.replace(reg,""), 'Y-m-d H:i:s');
	        	data.sexValue = data.sex;
	        	
	        	data.driverUsername = data.username;
            	data.driverEmail = data.email;
            	data.driverPassword = data.password;
            	
				if (retStatus == 'success') {
					var win = Ext.widget("editdriver");
					driverRet.data = data;
					win.down("form").loadRecord(driverRet);
					win.show();
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	//修改司机信息，选择组织
	editSelectOrg : function() {
		Ext.Ajax.request({
			url: 'user/loadCurrentUser',
	        method : 'POST',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				userCategory = respText.data.userCategory;
				var url ='';
				switch (userCategory) {
					case 0: //超级管理员
						url = 'rent/list';
						break;
					case 1: //租户管理员
						url = 'organization/list';
						break;
					case 2: //企业管理员
						url = 'department/list';
					case 6: //企业管理员
						url = 'department/list';
						break;
				}
				
				Ext.Ajax.request({
			   		//url: 'organization/list',
					url: url,
			        method : 'GET',
			        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			        success : function(response,options) {
						var respText = Ext.util.JSON.decode(response.responseText);
						
						var organizationId = window.sessionStorage.getItem("organizationId");
						for (var i=0; i<respText.data.length; i++) {
							if (organizationId == respText.data[i].id) {
								respText.data[i].id = -1;
								respText.data[i].name = "暂未分配";
							}
						}
						Ext.getCmp("edit_driver_dept_id").setStore(respText);
						//下面这种loadData的方法不行
						//Ext.getCmp("gridroleid").getStore('rolesResults').loadData(data);
			        },
			        failure : function() {
			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
			        },
			    });
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	//司机添加、修改，完成部门选择,设置对应的部门id
	SelectDepDone : function(items) {
		var depId = items.getValue();
		if (Ext.getCmp('add_driver_dept_id_id')) {
			Ext.getCmp('add_driver_dept_id_id').setValue(depId);
		}
		if (Ext.getCmp('edit_driver_dept_id_id')) {
			Ext.getCmp('edit_driver_dept_id_id').setValue(depId);
		}
	},
		
	//添加修改司机，站点改变，设置相应的ID
	SelectStationDone : function(items) {
		var stationId = items.getValue();
		if (Ext.getCmp('adddriver_station_id_id')) {
			Ext.getCmp('adddriver_station_id_id').setValue(stationId);
		} 
		if (Ext.getCmp('editdriver_station_id_id')) {
			Ext.getCmp('editdriver_station_id_id').setValue(stationId);
		}
	},
	
	//完成司机信息修改
	editClickDone : function(btn) {
		//后台需要参数：
		var driverInfo = this.getView().down('form').getForm().getValues();	
		
		userCategory = driverInfo.userCategory;
		if (userCategory == '超级管理员') {
			userCategory ='0';
		} else if (userCategory == '租户管理员') {
			userCategory ='1';
		} else if (userCategory == '企业管理员') {
			userCategory ='2';
		} else if (userCategory == '部门管理员') {
			userCategory ='3';
		} else if (userCategory == '普通员工') {
			userCategory ='4';
		} else if (userCategory == '司机') {
			userCategory ='5';
		} 
		driverInfo.userCategory = userCategory;
		
		//上传驾照照片
		attachName = driverInfo.licenseNumber;
		var form=this.getView().down('form').getForm();
		if (form.isValid()) {
            form.submit({
                url: 'driver/update',
                method:'post',
                defaultHeaders : {'Content-type' : 'application/json;utf-8'},
                //async: true,
                waitMsg: 'Uploading your file...',
                success: function(form,action) {
                	var result = Ext.util.JSON.decode(action.response.responseText);
                	btn.up('window').close();
                	Ext.Msg.alert('消息提示','修改成功!');
                	Ext.getCmp("griddriverid").getStore('driversResults').load();
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
	},
	
	//删除司机信息
	deleteDriver : function(grid, rowIndex, colIndex) {
		Ext.Msg.confirm('消息提示','确定要删除吗！！！',function(btn){
			if (btn == 'yes') {
				var driverID = grid.getStore().getAt(rowIndex).id;
				var url = 'driver/'+driverID+'/delete';
					
				Ext.Ajax.request({
		   		url: url,
		        method : 'POST',
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
							//删除成功后，刷新页面
							Ext.getCmp("griddriverid").getStore('driversResults').load();
						}
			        },
//			        failure : function() {
//			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			        },
			        scope:this
				});		
			}
		});
	},
	
	//点击批量导入按钮
	openFileUpWindow:function(){
		var win = Ext.create('Admin.view.systemmgmt.drivermgmt.FileUpLoad');
		win.show();
	},
	
	//导入文件
	uploadCSV:function(btn){
		var formPanel=this.getView().down('form');
		var form=this.getView().down('form').getForm();
		if (form.isValid()) {
            form.submit({
                url: 'driver/import',
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
        		 	Ext.getCmp("griddriverid").getStore('driversResults').load();
                }
            });
			
		}
	},
	
	//修改部门选择为多层组织架构 2017.6.19
    //查询选择部门
    openDeptChooseWin: function(combo, event, eOpts){
    	var win = Ext.create("Admin.view.systemmgmt.drivermgmt.DeptChooseWin",{
    		parentId:combo.up("form").getForm().findField("organizationId").getValue(),
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
    	var form = Ext.getCmp("driver_searchform_id").down("form").getForm();
    	form.findField("organizationId").setValue(deptId);
//    	form.findField("organizationId").setDisplayField(deptName);
    	form.findField("organizationName").setValue(deptName);
    	btn.up("window").close();
    },
    
  //新增选择部门
    openAddDriverDeptChooseWin: function(combo, event, eOpts){
	   	var win = Ext.create("Admin.view.systemmgmt.drivermgmt.DeptChooseWin",{
	   		parentId:combo.up("form").getForm().findField("depId").getValue(),
	   		parentName:combo.up("form").getForm().findField("deptment").getValue(),
	   		buttons:[{
	   	    	text:'确定',
	   	    	listeners:{
	   	    		click:'chooseAddDriverDept',
	   	    	}
	   	    },{
	   	    	text:'取消',
	   	    	handler:function(){
	   	    		this.up("window").close();
	   	    	}
	   	    }], 
	     	});
	   	win.down("treepanel").getStore().load();
     	win.show();
    },
    
    chooseAddDriverDept: function(btn, e, eOpts){
   	 	var tree = btn.up("window").down("treepanel");
     	var selection = tree.getSelectionModel().getSelection();
     	if(selection.length == 0){
     		Ext.Msg.alert('提示', '请选择部门！');
     		return;
     	}
     	var select = selection[0].getData();
 		var deptId = select.id;
 		var deptName = select.text;
//     	var form = Ext.getCmp("emp_searchform_id").down("form").getForm();
//     	Ext.getCmp("add_driver_dept_id_id").setValue(deptId);
// 		Ext.getCmp("add_driver_dept_id").setValue(deptName);
// 		Ext.getCmp("add_driver_dept_id").validate();
 		var userType = window.sessionStorage.getItem('userType');
 		if(select.parentId == "root" && (userType == '2' || userType == '6')){
        	//如果选中根节点，且目前是企业或者租车公司
      		//2是用车企业 6是租车企业
      		//3是部门
 			Ext.getCmp("add_driver_dept_id_id").setValue("-1");
 			
 			var entName = window.sessionStorage.getItem("organizationName");
 			Ext.getCmp("add_driver_dept_id").setValue(entName);
 			Ext.getCmp("add_driver_dept_id").validate();
      	}else{
      		Ext.getCmp("add_driver_dept_id_id").setValue(deptId);
     		Ext.getCmp("add_driver_dept_id").setValue(deptName);
     		Ext.getCmp("add_driver_dept_id").validate();
      	}
 		
 		
     	btn.up("window").close();
    },
    
    //修改选择部门
    openEditDriverDeptChooseWin: function(combo, event, eOpts){
   	var win = Ext.create("Admin.view.systemmgmt.drivermgmt.DeptChooseWin",{
	   		parentId:combo.up("form").getForm().findField("edit_driver_dept_id_id").getValue(),
	   		parentName:combo.up("form").getForm().findField("depName").getValue(),
	   		buttons:[{
	   	    	text:'确定',
	   	    	listeners:{
	   	    		click:'chooseEditDriverDept',
	   	    	}
	   	    },{
	   	    	text:'取消',
	   	    	handler:function(){
	   	    		this.up("window").close();
	   	    	}
	   	    }], 
     	});
   		win.down("treepanel").getStore().load();
     	win.show();
    },
    
    chooseEditDriverDept: function(btn, e, eOpts){
   	 	var tree = btn.up("window").down("treepanel");
     	var selection = tree.getSelectionModel().getSelection();
     	if(selection.length == 0){
     		Ext.Msg.alert('提示', '请选择部门！');
     		return;
     	}
     	var select = selection[0].getData();
 		var deptId = select.id;
 		var deptName = select.text;
//     	Ext.getCmp("edit_driver_dept_id_id").setValue(deptId);
// 		Ext.getCmp("edit_driver_dept_id").setValue(deptName);
// 		Ext.getCmp("edit_driver_dept_id").validate();
 		var userType = window.sessionStorage.getItem('userType');
 		if(select.parentId == "root" && (userType == '2' || userType == '6')){
        	//如果选中根节点，且目前是企业或者租车公司
      		//2是用车企业 6是租车企业
      		//3是部门
 			Ext.getCmp("edit_driver_dept_id_id").setValue("-1");
 			
 			var entName = window.sessionStorage.getItem("organizationName");
 			Ext.getCmp("edit_driver_dept_id").setValue(entName);
 			Ext.getCmp("edit_driver_dept_id").validate();
      	}else{
      		Ext.getCmp("edit_driver_dept_id_id").setValue(deptId);
     		Ext.getCmp("edit_driver_dept_id").setValue(deptName);
     		Ext.getCmp("edit_driver_dept_id").validate();
      	}
     	btn.up("window").close();
    },
    
    checkIsGroupNull: function(chk, newValue, oldValue, eOpts ){
    	var group = chk.up("checkboxgroup");
    	var value = group.getValue();
    	if(value.selfDept == null && value.childDept == null){
//    		chk.setValue(true);
    		Ext.Msg.alert("提示信息", '本部门和子部门请至少选择一个！');
     		//setValue无法选中上次选中的，有bug
     		if(chk.boxLabel == "本部门"){
     			group.items.items[1].setValue(true);
     		}else{
     			group.items.items[0].setValue(true);
     		}
    	}
    }
});
