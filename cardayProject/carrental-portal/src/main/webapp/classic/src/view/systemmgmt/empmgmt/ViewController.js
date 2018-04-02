/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.empmgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.systemmgmt.empmgmt.View',
			'Admin.view.systemmgmt.empmgmt.SearchForm',
			'Admin.view.systemmgmt.empmgmt.AddEmp',
			'Admin.view.systemmgmt.empmgmt.EmpViewModel',
			'Admin.view.systemmgmt.empmgmt.EmpDataModel'
			],
	alias : 'controller.empmgmtcontroller',

	onSearchClick : function() {
		//查询时，默认从第一页开始查询
		Ext.getCmp('grid_emp_store_id').getStore().currentPage = 1;
		this.getViewModel().getStore("empsResults").load();	
	},
	
	//获取所有emp信息，
	onBeforeLoad : function() {
		var parm = this.lookupReference('searchForm').getValues();
//		if (typeof parm.organizationId == 'string') {
//			parm.organizationId = '';
//		}
		//增加了隐藏字段organizationId，默认值为空
		if(parm.organizationId != ""){
			parm.organizationId = parseInt(parm.organizationId);
		}
		
		parm.currentPage = Ext.getCmp('grid_emp_store_id').store.currentPage;
		parm.numPerPage = Ext.getCmp('grid_emp_store_id').store.pageSize;
		parm = Ext.encode(parm);		
		this.getViewModel().getStore("empsResults").proxy.extraParams = {
			"json" : parm
		}
	},
	
	//添加员工
	onAddClick : function() {
		//Ext.Msg.alert("Message Box", "添加按钮");
		win = Ext.widget('addemp');
		win.show();
	},
	
	//添加员工，选择企业
	selectOrg : function() {
		Ext.Ajax.request({
			url: 'user/loadCurrentUser',
	        method : 'POST',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var userCategory = respText.data.userCategory;
				var organizationName = respText.data.organizationName;
				var organizationId = respText.data.organizationId;
				
				//部门管理员
				if (userCategory == 3) {
					var comstore = new Ext.data.Store();
					//var dept ='部门';
					var comstore = {
		            	data : [
		            	        {'id': organizationId, 'name': organizationName}
		            	       ]
		            };
		            
					Ext.getCmp('add_emp_org_id').setStore(comstore);
					return;
				}
				
				//企业管理员,如果员工没有分配部门，就属于企业，前台显示 暂未分配。
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
								}
							}
							if (Ext.getCmp('add_emp_org_id')) {
								Ext.getCmp('add_emp_org_id').setStore(respText);
							}
							return;
				        }
				        /*,
				        failure : function() {
				            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
				        },*/
				    });
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	//选择员工类别
	selectUserCategory : function() {
		var comstore=Ext.create('Ext.data.Store', {
     		proxy: {
	         	type: 'ajax',
	         	//url: 'app/data/systemmgmt/usermgmt/usercategory.json',
	         	url: 'user/listUserType',
	     		reader: {
		        	type: 'json',
		         	rootProperty: 'data',
		         	successProperty: 'status'
	     		}
     		},
         	autoLoad: false 
         });
         this.getView().down('form').getComponent('itemuc').setStore(comstore);
	
	},
	
	//选择员工角色
	selectRoles : function() {
		Ext.Ajax.request({
	   		url: 'role',
	        method : 'POST',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
//				var data = respText.data;
//				data=[{"id": 1, "role": "超级管理员"},
//				          {"id": 2, "role": "机构管理员"}
//				];
				Ext.getCmp("addemp_role_id").setStore(respText);
				//下面这种loadData的方法不行
				//Ext.getCmp("gridroleid").getStore('rolesResults').loadData(data);
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	
	},
	
	//完成添加员工
	onAddClickDone : function(btn) {
		var empInfo = this.getView().down('form').getForm().getValues();
		
		empInfo.empPassword = Ext.getCmp('add_emp_password_id').getValue();
		var input = {
        		'username': empInfo.empUserName,
        		'password': empInfo.empPassword,
        		'roleId': empInfo.userRoles,
        		'organizationId': empInfo.userOrg,
        		'userCategory': empInfo.userCategory,
        		'realname': empInfo.realname,
        		'phone': empInfo.phone,
        		'email': empInfo.empEmail,
        		'city': empInfo.city,
        		'idnumber': empInfo.idnumber,
        		'monthLimitvalue': 100, 
        		'orderCustomer': 0,
        		'orderSelf': 0,
        		'orderApp': 0,
        		'orderWeb': 0,
			};
		var json = Ext.encode(input);
	   	 Ext.Ajax.request({
	   		url : 'employee/create',//?json='+ Ext.encode(input),
	        method : 'POST',
	        params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var retStatus = respText.status;
				if (retStatus == 'success') {
					btn.up('addemp').close();
					Ext.Msg.alert('提示信息','添加成功');
					Ext.getCmp("gridempid").getStore('empsResults').load();
				} else if (retStatus == 'failure') {
					Ext.Msg.alert('提示信息', respText.msg);
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        }*/
	    });
	},
	
	//查看员工信息
	viewEmp : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var empId = rec.data.id;
		url = 'employee/'+empId+'/update';
		
		var empRet = new Ext.data.Model();
		//根据id查询员工信息
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
	    		data.userCategory = userCategory;
	    		//用车额度转换
	    		//用车额度   -1：不限额； 月累计限额， 不填默认为0， 如果填写限制必须是大于等于0的 数字 
	    		if (data.monthLimitvalue == -1) {
	    			data.monthLimitvalue = '不限';
	    		} else {
	    			data.monthLimitvalue = data.monthLimitvalue + '元/月';
	    		}
	    		//下单权限
	    		//orderCustomer;//代客户下单   0:不代客户下单 1：代客户下单
	    		//orderSelf;//员工自己下单  0:员工不自己下单 1：员工自己下单
	    		if (data.orderCustomer==1 && data.orderSelf==0) {
	    			data.orderType = '代客户下单';
	    		} else if (data.orderSelf==1 &&　data.orderCustomer==0) {
	    			data.orderType = '员工自己下单';
	    		} else if (data.orderCustomer==1 && data.orderSelf == 1) {
	    			data.orderType = '代客户下单, 员工自己下单';
	    		} else {
	    			data.orderType = '--';
	    		}
	    		
	    		//下单方式
	    		if (data.orderApp==1 && data.orderWeb==0) {
	    			data.orderStyle = 'App下单';
	    		} else if (data.orderWeb==1 &&　data.orderApp==0) {
	    			data.orderStyle = 'Web下单';
	    		} else if (data.orderApp==1 && data.orderWeb == 1) {
	    			data.orderStyle = 'App下单, Web下单';
	    		} else {
	    			data.orderStyle = '--';
	    		}
	    		
	    		//企业管理员登录，员工还未分配，属于企业，前台显示为 暂未分配
	    		var organizationName = window.sessionStorage.getItem('organizationName');
            	var userCategory = window.sessionStorage.getItem('userType');	
            	//alert(organizationName);
            	if ((userCategory==2 || userCategory==6) && data.organizationName==organizationName) {
//            		data.organizationName = '暂未分配';
            	}
            	
            	data.policeNumber = '000001';
				if (retStatus == 'success') {
					var win = Ext.widget("viewemp");
					empRet.data = data;
					win.down("form").loadRecord(empRet);
					win.show();
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	//修改员工信息,根据员工ID查询员工信息
	editEmp : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);
		var empId = rec.data.id;
		url = 'employee/'+empId+'/update';
		var empRet = new Ext.data.Model();
		
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
	    		data.userCategory = userCategory;
	    		
	    		var organizationName = window.sessionStorage.getItem('organizationName');
            	var userCategory = window.sessionStorage.getItem('userType');	
            	//alert(organizationName);
            	/*if ((userCategory==2 || userCategory==6) && data.organizationName==organizationName) {
            		data.organizationName = '暂未分配';
            	}*/
            	
            	//邮箱，密码前后台名字不一致
            	data.empUserName = data.username;
            	data.empEmail = data.email;
            	data.empPassword = data.password;
            	
				if (retStatus == 'success') {
					var win = Ext.widget("editemp");
					empRet.data = data;
					win.down("form").loadRecord(empRet);
					win.show();
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	//完成用户信息修改
	clickEditDone : function(btn) {
		//后台需要参数：
		var empInfo = this.getView().down('form').getForm().getValues();
		var empUsername = Ext.getCmp('emp_username').getValue();
		
		userCategory = empInfo.userCategory;
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
		empInfo.userCategory = userCategory;
		
		var input = {
			'id': empInfo.id,
			//'username': empInfo.username,
			'organizationId': empInfo.organizationId,
			'roleId': empInfo.roleId,
			'userCategory': empInfo.userCategory,
			'realname': empInfo.realname,
			'phone': empInfo.phone,
			'email': empInfo.empEmail,
        	'idnumber': empInfo.idnumber,
			
			'city': empInfo.city,
			'monthLimitvalue': 1000,
			'orderCustomer': 0,
			'orderSelf': 0,
			'orderApp': 0,
			'orderWeb': 0,
		};
		var json = Ext.encode(input);
		Ext.Ajax.request({
	   		url: 'employee/update',//?json='+ Ext.encode(input),
	        method : 'POST',
	        params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var retStatus = respText.status;
				if (retStatus == 'success') {
					btn.up('editemp').close();
					Ext.Msg.alert('提示信息','修改成功');
					Ext.getCmp("gridempid").getStore('empsResults').load();
				} else if (retStatus == 'failure') {
					Ext.Msg.alert('提示信息', respText.msg);
				}      	
	        },
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	        scope:this
		});
	},
	
	//修改用户，添加组织
	editSelectOrg : function() {
		Ext.Ajax.request({
			url: 'user/loadCurrentUser',
	        method : 'POST',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var userCategory = respText.data.userCategory;
				var organizationName = respText.data.organizationName;
				var organizationId = respText.data.organizationId;
				//部门管理员
				if (userCategory == 3) {
					var comstore = new Ext.data.Store();
					//var dept ='部门';
					var comstore = {
		            	data : [
		            	        {'id': organizationId, 'name': organizationName}
		            	       ]
		            };
		            
					Ext.getCmp('editemp_org_id').setStore(comstore);
					return;
				}
				
				//用车企业管理员、租车企业管理员,如果员工没有分配部门，就属于企业，前台显示 暂未分配。
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
								}
							}
							Ext.getCmp('editemp_org_id').setStore(respText);
							return;
				        }
				        /*,
				        failure : function() {
				            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
				        },*/
				    });
					return;
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	//用户组织改变监听方法
	SelectOrgDone : function(items) {
		var orgId = items.getValue();
		Ext.getCmp('editemp_org_id_id').setValue(orgId);
	},
	
	//选择用户类别
	selectUserCategory : function() {
		var comstore=Ext.create('Ext.data.Store', {
     		proxy: {
	         	type: 'ajax',
	         	//url: 'app/data/systemmgmt/usermgmt/usercategory.json',
	         	url: 'user/listUserType',
	     		reader: {
		        	type: 'json',
		         	rootProperty: 'data',
		         	successProperty: 'status'
	     		}
     		},
         	autoLoad: false 
         });
         this.getView().down('form').getComponent('itemuc').setStore(comstore);
	},
	
	//选择用户角色
	editSelectRoles : function() {
		Ext.Ajax.request({
	   		url: 'role',
	        method : 'POST',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				Ext.getCmp("editemp_role_id").setStore(respText);
				//下面这种loadData的方法不行
				//Ext.getCmp("gridroleid").getStore('rolesResults').loadData(data);
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	//修改员工信息，完成角色修改
	SelectRoleDone : function(items) {
		var roleId = items.getValue();
		Ext.getCmp('editemp_role_id_id').setValue(roleId);
	},
	
	//删除员工信息
	deleteEmp : function(grid, rowIndex, colIndex) {
		Ext.Msg.confirm('消息提示','确定要删除吗！！！',function(btn){
			if (btn == 'yes') {
				var empID = grid.getStore().getAt(rowIndex).id;
				var url = 'employee/'+empID+'/delete';
					
				Ext.Ajax.request({
		   		url: url,
		        method : 'POST',
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
							//删除成功后，刷新页面
							Ext.getCmp("gridempid").getStore('empsResults').load();
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
		var win = Ext.create('Admin.view.systemmgmt.empmgmt.FileUpLoad');
		win.show();
	},
	
	//导入文件
	uploadCSV:function(btn){
		var formPanel=this.getView().down('form');
		var form=this.getView().down('form').getForm();
		if (form.isValid()) {
            form.submit({
                url: 'employee/import',
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
        		 	Ext.getCmp("gridempid").getStore('empsResults').load();
                }
            });
			
		}
	},

	//打开给用户分配规则窗口，显示当前用户可分配规则
	assignRule: function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
    	var win = Ext.widget("assignRule", {
			title: '规则管理',
			closable: false,
			buttonAlign : 'center',
			empId: rec.data.id,
			empName: rec.data.realname,
			buttons : [{
				text : '返回',
				handler: 'toBackEmpView'
			}]
		});
		win.show();
	},

		//返回站点管理页面
	toBackEmpView: function() {
     	console.log('to back emp view');
     	var VehicleStore = Ext.getCmp('empmgmt').getViewModel().getStore('empsResults');
		VehicleStore.currentPage = 1;
		Ext.getCmp('empmgmt').getViewModel().getStore("empsResults").load();
     	this.view.close();
    },

    onAfterLoadEmpRule: function(){
		Ext.getCmp("assignedRuleGrid").getViewModel().getStore("empRuleAssignedStore").load();
    },

    loadEmpRule: function() {
 		var empId = Ext.getCmp("assignRule").empId;
		var input = {
				"userId":empId,
			};
		var pram = Ext.encode(input);
		Ext.getCmp("assignedRuleGrid").getViewModel().getStore("empRuleAssignedStore").proxy.extraParams = {
			"json" : pram
		};

     },

          //打开可分配车辆窗口
    showAvailiableRuleView: function(){
    	var assignedRuleCount = Ext.getCmp('assignedRuleGrid').getStore().getCount();
     	var empId = Ext.getCmp("assignRule").empId;
     	var empName = Ext.getCmp("assignRule").empName;

     	if(assignedRuleCount >= 3){
    		Ext.Msg.alert('消息提示', '用户 ' + empName + '已绑定三个规则，不能绑定更多！');
    	}else{
		 	console.log('+++showAvailiableRuleView+++empId: ' + empId);
		 	var win = Ext.widget("addRuleToEmp", {
					title: '规则管理',
					buttonAlign : 'center',
					closable: false,
					empId : empId,
					empName: empName,
					assignedRuleCount: assignedRuleCount,
					buttons : [{
						text : '返回',
						handler: 'toAssignRuleView'
					}]
				});
		 	Ext.getCmp("assignRule").close();
				win.show();
 		}
     },

    toAssignRuleView: function() {
 		var empId = Ext.getCmp("addRuleToEmp").empId;
 		var empName = Ext.getCmp("addRuleToEmp").empName;

     	var win = Ext.widget("assignRule", {
 			title: '规则管理',
 			closable: false,
 			buttonAlign : 'center',
 			empId: empId,
 			empName: empName,
 			buttons : [{
 				text : '返回',
 				handler: 'toBackEmpView'
 			}]
 		});
     	this.view.close();
 		win.show();
     },

    loadEmpAvialiableRule: function() {
  		var empId = Ext.getCmp("addRuleToEmp").empId;
		var input = {
				"userId":empId,
			};
		var pram = Ext.encode(input);
		Ext.getCmp("addRuleToEmpView").getViewModel().getStore("empRuleStore").proxy.extraParams = {
			"json" : pram
		};

      },

	onAfterEmpAvialiableRule: function(){
		Ext.getCmp("addRuleToEmpView").getViewModel().getStore("empRuleStore").load();
	},

	confirmAddRule: function() {
     	var empId = Ext.getCmp("addRuleToEmp").empId;
     	var empName = Ext.getCmp("addRuleToEmp").empName;
     	var assignedRuleCount = Ext.getCmp("addRuleToEmp").assignedRuleCount;
     	console.log('to confirm add rule to emp');
     	var gridPanel = Ext.getCmp('addRuleToEmpView');
 		var record = gridPanel.getSelectionModel().getSelection();
 		console.log('length:' + record.length);
 		if(gridPanel.getViewModel().getStore('empRuleStore').totalCount == 0) {
 			Ext.Msg.alert('消息提示', '无规则可选择');
 			return;
 		}
 		if(record.length == 0) {
 			Ext.Msg.alert('消息提示', '请选择规则');
 			return;
 		}
 		
 		var ruleList = new Array();

 		var remainAssignedRule = 3-assignedRuleCount;

 		if(record.length > remainAssignedRule){
 			Ext.Msg.alert('消息提示', '用户 ' + empName + ' 最多绑定三个规则,已绑定 ' + assignedRuleCount + ' 个规则，最多只能绑定 ' + remainAssignedRule + ' 个！' );
 		}else{

		 		for (var i=0; i<record.length; i++) {
		 			ruleList.push(record[i].data.ruleId);
		 		}
		 		
		 		var input = {
		 			'userId' : empId,
		 			'ruleList' : ruleList
		 		};

		 		var json = Ext.encode(input);
		 		
		 		Ext.Ajax.request({
		 			url : "rule/userBindingRule",
		 			method : 'POST',
					params:{ json:json},
		 			success : function(response, options) {
		 				var respText = Ext.util.JSON.decode(response.responseText);
		 				var status = respText.status;
		 				var input = {
		 			 			'empId' : Ext.getCmp("addRuleToEmp").empId
		 			 		};
		 				var win = Ext.widget("assignRule", {
		 					title: '规则管理',
		 					closable: false,
		 					buttonAlign : 'center',
		 					empId: Ext.getCmp("addRuleToEmp").empId,
		 					empName: Ext.getCmp("addRuleToEmp").empName,
		 					buttons : [{
		 						text : '返回',
		 						handler: 'toBackEmpView'
		 					}]
		 				});
		 		    	if(status == 'success') {
		 		    		Ext.Msg.alert('消息提示', '添加成功', function(text) {
		                       	Ext.getCmp("addRuleToEmp").close();
		 		    		    win.show();
		                    });
		 		    	}
		 			},
//		 			failure : function() {
//		 				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//		 			},
		 			scope : this
		 		});
 	    }
     },

    //移除已分配车辆
	unassignRule: function(grid, rowIndex, colIndex) {
     	var rec = grid.getStore().getAt(rowIndex);
     	var empId = Ext.getCmp("assignRule").empId;
     	var empName = Ext.getCmp("assignRule").empName;
     	var msg = '是否解除规则 ' + rec.data.ruleName + ' 与用户 ' +  empName + ' 的绑定 ?';
     	console.log('+++unassignRule+++empId: ' + empId);
     	Ext.Msg.confirm('消息提示', msg, function(btn){
     		if (btn == 'yes') {
     			//调用部门移除接口 start
     			var input = {'userId' : empId,
     					     'ruleId': rec.data.ruleId};
 		        var json = Ext.encode(input);
             	Ext.Ajax.request({
         			url : "rule/removeBindingRule",
         			method : 'POST',
			        params:{ json:json},
         			success : function(response, options) {
         				var respText = Ext.util.JSON.decode(response.responseText);
         				var status = respText.status;
         		    	console.log('length:' + status);
         		    	if(status == 'success') {
         		    		Ext.Msg.alert('消息提示', '移除成功');
							Ext.getCmp("assignedRuleGrid").getViewModel().getStore("empRuleAssignedStore").load();
         		    	}
         			},
//         			failure : function() {
//         				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//         			},
         			scope : this
         		});
     			//调用已分配车辆移除接口 end
     		}
     	});
     },
     
     
     //修改部门选择为多层组织架构 2017.6.19
     //查询选择部门
     openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.systemmgmt.empmgmt.DeptChooseWin",{
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
     	var form = Ext.getCmp("emp_searchform_id").down("form").getForm();
     	form.findField("organizationId").setValue(deptId);
//     	form.findField("organizationId").setDisplayField(deptName);
     	form.findField("organizationName").setValue(deptName);
     	btn.up("window").close();
     },
     
     //新增选择部门
     openAddEmpDeptChooseWin: function(combo, event, eOpts){
    	var win = Ext.create("Admin.view.systemmgmt.empmgmt.DeptChooseWin",{
    		parentId:combo.up("form").getForm().findField("userOrg").getValue(),
    		buttons:[{
    	    	text:'确定',
    	    	listeners:{
    	    		click:'chooseAddEmpDept',
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
     
     chooseAddEmpDept: function(btn, e, eOpts){
    	 var tree = btn.up("window").down("treepanel");
      	var selection = tree.getSelectionModel().getSelection();
      	if(selection.length == 0){
      		Ext.Msg.alert('提示', '请选择部门！');
      		return;
      	}
      	var select = selection[0].getData();
  		var deptId = select.id;
  		var deptName = select.text;
//      	var form = Ext.getCmp("emp_searchform_id").down("form").getForm();
      	Ext.getCmp("add_emp_org_id").setValue(deptId);
  		Ext.getCmp("add_emp_org_name").setValue(deptName);
  		Ext.getCmp("add_emp_org_name").validate();
      	btn.up("window").close();
     },
     
     //修改选择部门
     openEditEmpDeptChooseWin: function(combo, event, eOpts){
    	var win = Ext.create("Admin.view.systemmgmt.empmgmt.DeptChooseWin",{
    		parentId:combo.up("form").getForm().findField("editemp_org_id_id").getValue(),
    		buttons:[{
    	    	text:'确定',
    	    	listeners:{
    	    		click:'chooseEditEmpDept',
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
     
     chooseEditEmpDept: function(btn, e, eOpts){
    	 var tree = btn.up("window").down("treepanel");
      	var selection = tree.getSelectionModel().getSelection();
      	if(selection.length == 0){
      		Ext.Msg.alert('提示', '请选择部门！');
      		return;
      	}
      	var select = selection[0].getData();
  		var deptId = select.id;
  		var deptName = select.text;
//      	var form = Ext.getCmp("emp_searchform_id").down("form").getForm();
      	Ext.getCmp("editemp_org_id_id").setValue(deptId);
  		Ext.getCmp("editemp_org_name").setValue(deptName);
  		Ext.getCmp("editemp_org_name").validate();
      	btn.up("window").close();
     },
     
     checkIsGroupNull: function(chk, newValue, oldValue, eOpts ){
     	var group = chk.up("checkboxgroup");
     	var value = group.getValue();
     	if(value.selfDept == null && value.childDept == null){
//     		chk.setChecked(true);
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
