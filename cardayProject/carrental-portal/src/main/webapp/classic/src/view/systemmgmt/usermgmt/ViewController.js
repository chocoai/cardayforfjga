/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.usermgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.systemmgmt.usermgmt.View',
			'Admin.view.systemmgmt.usermgmt.SearchForm',
			'Admin.view.systemmgmt.usermgmt.AddUser',
			'Admin.view.systemmgmt.usermgmt.EditUser',
			'Admin.view.systemmgmt.usermgmt.ViewUser',
			'Admin.view.systemmgmt.usermgmt.UserModel'
			],
	alias : 'controller.usermgmtcontroller',

	//根据id查询用户信息
	onSearchClick : function(it, e) {
		var realname = this.lookupReference('searchForm').getValues().realname;
//		if (realname=='' || realname==null) {
//			Ext.Msg.alert('提示信息', '请输入要查询的角色名称！')
//			return;
//		}
		
		var input= {
			'realname': realname
		};
		 
		Ext.Ajax.request({
			url : 'user?json='+ Ext.encode(input),
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
		        //alert(response.responseText);
				var respText = Ext.util.JSON.decode(response.responseText);
			    	this.getViewModel().getStore("usersResults").loadData(respText.data);
	        },
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	        scope:this
		});
	},

	onBeforeLoad : function() {
		var frmValues = this.lookupReference('searchForm').getValues();
		var pram = Ext.encode(frmValues);
		var pram='';
		this.getViewModel().getStore("usersResults").proxy.extraParams = {
			"json" : pram
		}
		this.getViewModel().getStore("usersResults").load();
	},
	
	onAddClick : function() {
		//Ext.Msg.alert("Message Box", "添加按钮");
		win = Ext.widget('adduser');
		win.show();
	},
	
	//添加用户
	onAddClickDone : function(btn) {
		//username,password,roleId,organizationId,userCategory
		var userInfo = this.getView().down('form').getForm().getValues();
		//手机号码验证
		var regx1 = /[1][3578]\d{9}$/;
		var res1 = regx1.test(userInfo.phone);
		if (!res1) {
			Ext.Msg.alert('提示信息', '你输入的手机号码有误，请重新输入！');
			return;
		}
		
		//邮箱验证
		var regx2 = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		var res2 = regx2.test(userInfo.email);
		if (!res2) {
			Ext.Msg.alert('提示信息', '你输入的邮箱地址有误，请重新输入！');
			return;
		}
		
		//密码验证
		var regx3 = /^([a-zA-Z0-9]){6,20}$/;
		var res3 = regx3.test(userInfo.password);
		if (!res3) {
			Ext.Msg.alert('提示信息', '你输入的密码格式不符合要求，请重新输入6-20位字符,含数字、字母！');
			return;
		}
		
		//所属机构不能为空验证
		if (userInfo.userOrg==null || userInfo.userOrg=='') {
			Ext.Msg.alert('提示信息', '你没有选择用户所属机构，请选择！');
			return;
		}
		
		//用户角色不能为空验证
		if (userInfo.userRoles==null || userInfo.userRoles=='') {
			Ext.Msg.alert('提示信息', '你没有选择用户角色，请选择！');
			return;
		}
		
		var input = {
        		'username': userInfo.userName,
        		'password': userInfo.password,
        		'roleId': userInfo.userRoles,
        		'organizationId': userInfo.userOrg,
        		'userCategory': userInfo.userCategory,
        		'realname': userInfo.realname,
        		'phone': userInfo.phone,
        		'email': userInfo.email
			};
		var json = Ext.encode(input);
	   	 Ext.Ajax.request({
			url : 'user/create',//?json='+ Ext.encode(input),
	        method : 'POST',
	        params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var retStatus = respText.status;
				if (retStatus == 'success') {
					btn.up('adduser').close();
					Ext.Msg.alert('提示信息','添加成功');
					Ext.getCmp("griduserid").getStore('usersResults').load();
				} else if (retStatus == 'failure') {
					Ext.Msg.alert('提示信息',respText.msg);
				}
	        }
//	        ,
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        }
	    });
	},

	//添加用户，选择组织
	selectOrg : function() {
		Ext.Ajax.request({
			url: 'user/loadCurrentUser',
	        method : 'POST',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				userCategory = respText.data.userCategory;
				var url ='';
				//只有admin可以添加用户（企业管理员）
				if (userCategory == 0) {
					url = 'organization/auditedByAdmin/list';
				}
				var comstore=Ext.create('Ext.data.Store', {
		     		proxy: {
			         	type: 'ajax',
			         	url: url,
			     		reader: {
				        	type: 'json',
				         	rootProperty: 'data',
				         	successProperty: 'status'
			     		}
		     		},
		         	autoLoad: false 
	         });
			Ext.getCmp('add_user_org_id').setStore(comstore);
	         //this.getView().down('form').getComponent('itemorg').setStore(comstore);
	        }
//	        ,
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	    });
	},
	
	//修改用户，选择组织
	editSelectOrg : function() {
		Ext.Ajax.request({
			url: 'user/loadCurrentUser',
	        method : 'POST',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				userCategory = respText.data.userCategory;
				var url ='';
				//只有admin可以添加用户（企业管理员）
				if (userCategory == 0) {
					url = 'organization/auditedByAdmin/list';
				}
				Ext.Ajax.request({
					url: url,
			        method : 'GET',
			        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			        success : function(response,options) {
						var respText = Ext.util.JSON.decode(response.responseText);
						Ext.getCmp("edituser_org_id").setStore(respText);
						//下面这种loadData的方法不行
						//Ext.getCmp("gridroleid").getStore('rolesResults').loadData(data);
			        }
//			        ,
//			        failure : function() {
//			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			        },
			    });
	        }
//	        ,
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	    });
	},
	
	//用户组织改变监听方法
	SelectOrgDone : function(items) {
		var orgId = items.getValue();
		Ext.getCmp('edituser_org_id_id').setValue(orgId);
	},
	
	SelectRoleDone : function(items) {
		var roleId = items.getValue();
		Ext.getCmp('edituser_role_id_id').setValue(roleId);
	},
	
	//添加用户，选择角色
	selectRoles : function() {
		var userType = window.sessionStorage.getItem('userType');
		
		//目前只有admin能创建用户，只能创建企业管理员
		var input = {
				'templateId' : 2,
		};
		var json = Ext.encode(input);
		Ext.Ajax.request({
	   		url: 'role',//?json='+ parm,
	        method : 'POST',
	        params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
//				var data = respText.data;
//				data=[{"id": 1, "role": "超级管理员"},
//				          {"id": 2, "role": "机构管理员"}
//				];
				Ext.getCmp("adduser_role_id").setStore(respText);
	        }
//	        ,
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	    });
	},
	
	//修改用户，选择角色
	editSelectRoles : function() {
		var userType = window.sessionStorage.getItem('userType');
		
		var input = {
				'templateId' : 2,
			};
		var json = Ext.encode(input);
		Ext.Ajax.request({
	   		url: 'role',//?json='+ parm,
	        method : 'POST',
	        params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
//				var data = respText.data;
//				data=[{"id": 1, "role": "超级管理员"},
//				          {"id": 2, "role": "机构管理员"}
//				];
				Ext.getCmp("edituser_role_id").setStore(respText);
				//下面这种loadData的方法不行
				//Ext.getCmp("gridroleid").getStore('rolesResults').loadData(data);
	        }
//	        ,
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	    });
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
	
	//修改用户信息，根据id查询用户信息
	editUser : function(grid, rowIndex, colIndex) {
		var userId = grid.getStore().getAt(rowIndex).data.id;
		var url = 'user/'+userId+'/update';
		
		var rec = new Ext.data.Model();

		Ext.Ajax.request({
				url : url,
		        method : 'GET',
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
		        	var respText = Ext.util.JSON.decode(response.responseText);
		        	var data = respText.data;
		        	var retStatus = respText.status;
		        	
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
		    		
					if (retStatus == 'success') {
						var win = Ext.widget("edituser");
						rec.data = data;
						win.down("form").loadRecord(rec);
						win.show();
						//Ext.getCmp("edit_role_id").getStore('rolesResults').load();
						$('#editUser input[name="phone"]').keypress(function(e) {              
							if (!String.fromCharCode(e.keyCode).match(/[0-9\.]/)) {                
								return false;             
							}
							if($('#editUser input[name="phone"]').val() != ''){      
								if (!$('#editUser input[name="phone"]').val().match(/^\d{1,10}$/)) {                
									return false;     
								}     
							}      
						 });    

				        $('#editUser input[name="phone"]').keyup(function(){
				            $(this)[0].value=$(this)[0].value.replace(/[^\d]/g,'');
				        });
					}
		        }
//		        ,
//		        failure : function() {
//		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//		        }
		   });
	},
	
	//完成用户信息修改
	clickEditDone : function(btn) {
		//后台需要参数：id,username,roleId,organizationId
		var userInfo = this.getView().down('form').getForm().getValues();
		
		//手机号码验证
		var regx1 = /[1][3578]\d{9}$/;
		var res1 = regx1.test(userInfo.phone);
		if (!res1) {
			Ext.Msg.alert('提示信息', '你输入的手机号码有误，请重新输入！');
			return;
		}
		
		//邮箱验证
		var regx2 = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		var res2 = regx2.test(userInfo.email);
		if (!res2) {
			Ext.Msg.alert('提示信息', '你输入的邮箱地址有误，请重新输入！');
			return;
		}
		
		//所属机构不能为空验证
		if (userInfo.organizationName==null || userInfo.organizationName=='') {
			Ext.Msg.alert('提示信息', '你没有选择用户所属机构，请选择！');
			return;
		}
		
		//用户角色不能为空验证
		if (userInfo.roleName==null || userInfo.roleName=='') {
			Ext.Msg.alert('提示信息', '你没有选择用户角色，请选择！');
			return;
		}
		
		userCategory = userInfo.userCategory;
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
		userInfo.userCategory = userCategory;
		
		var input = {
			'id': userInfo.id,
			//'username': userInfo.username,
			'organizationId': userInfo.organizationId,
			'roleId': userInfo.roleId,
			'userCategory': userInfo.userCategory,
			'realname': userInfo.realname,
			'phone': userInfo.phone,
			'email': userInfo.email
		};
		var json = Ext.encode(input);
		Ext.Ajax.request({
	   		url: 'user/update',//?json='+ Ext.encode(input),
	        method : 'POST',
	        params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var retStatus = respText.status;
				if (retStatus == 'success') {
					btn.up('edituser').close();
					Ext.Msg.alert('提示信息','修改成功');
					Ext.getCmp("griduserid").getStore('usersResults').load();
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
	
	//查看用户
	viewUser : function(grid, rowIndex, colIndex) {
		var userId = grid.getStore().getAt(rowIndex).data.id;
		var url = 'user/'+userId+'/update';
		var rec = new Ext.data.Model();

		Ext.Ajax.request({
				url : url,
		        method : 'GET',
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
		        	var respText = Ext.util.JSON.decode(response.responseText);
		        	var data = respText.data;
		        	var retStatus = respText.status;
		        	
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
		    		
					if (retStatus == 'success') {
						var win = Ext.widget("viewuser");
						rec.data = data;
						win.down("form").loadRecord(rec);
						win.show();
					}
		        }
//		        ,
//		        failure : function() {
//		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//		        }
		   });
	},

	//删除用户
	deleteUser : function(grid, rowIndex, colIndex) {
		Ext.Msg.confirm('消息提示','确定要删除吗！！！',function(btn){
			if (btn == 'yes') {
				var userID = grid.getStore().getAt(rowIndex).id;
				var url = 'user/'+userID+'/delete';
					
				Ext.Ajax.request({
		   		url: url,
		        method : 'POST',
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
							//删除成功后，刷新页面
							Ext.getCmp("griduserid").getStore('usersResults').load();
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
	
	onResetClick : function() {
		Ext.Msg.alert("Message Box", "重置按钮");
	},
	
	//点击批量导入按钮
	openFileUpWindow:function(){
		var win = Ext.create('Admin.view.systemmgmt.usermgmt.FileUpLoad');
		win.show();
	},
	
	//导入文件
	uploadCSV:function(btn){
		var formPanel=this.getView().down('form');
		var form=this.getView().down('form').getForm();
		if (form.isValid()) {
            form.submit({
                url: 'user/import',
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
        		 	
        		 	Ext.getCmp("griduserid").getStore('usersResults').load();
                }
            });
			
		}
	},
});
