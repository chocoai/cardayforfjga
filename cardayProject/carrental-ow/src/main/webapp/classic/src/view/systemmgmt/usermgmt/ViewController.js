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
	onBeforeLoad : function(it, e) {
		var realname = this.lookupReference('searchForm').getValues().realname;
		var page=Ext.getCmp('userpageid').store.currentPage;
		var limit=Ext.getCmp('userpageid').pageSize;
		
		var input= {
				"currentPage" : page,
				"numPerPage" : limit,
			    'realname': realname
		};
		
		var pram = Ext.encode(input);
		this.getViewModel().getStore("usersResults").proxy.extraParams = {
			"json" : pram
		}
	},

	 onSearchClick : function() {	
		var EnterInfoStore = this.lookupReference('griduser').getStore();
		EnterInfoStore.currentPage = 1;
		this.getViewModel().getStore('usersResults').load();
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
		userInfo.adminPassword = Ext.getCmp('adminPassword_id').getValue();
		var input = {
        		'username': userInfo.adminUserName,
        		'password': userInfo.adminPassword,
        		'roleId': userInfo.userRoles,
        		'organizationId': userInfo.userOrg,
        		'userCategory': userInfo.userCategory,
        		'realname': userInfo.realname,
        		'phone': userInfo.phone,
        		'email': userInfo.adminEmail
			};
		 var json_input = Ext.encode(input);
	   	 Ext.Ajax.request({
			url : 'user/create',//?json='+ Ext.encode(input),
	        method : 'POST',
	        params:{ json:json_input},
//	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
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
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        }*/
	    });
	},

	//添加用户，选择组织
	selectOrg : function() {
		var userCategory = window.sessionStorage.getItem("userType");
		var url = '';
		//只有系统管理员角色，拥有管理员管理模块功能
		if (userCategory == -9) {
			url = 'organization/audited/list';
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
	   if (Ext.getCmp('add_user_org_id')) {
		   Ext.getCmp('add_user_org_id').setStore(comstore);
	   }
	},

	selectOrgDone : function(combo,record,eOpts) {
		if(Ext.getCmp("adduser_role_id")){
			Ext.getCmp("adduser_role_id").setValue('');
		}else if(Ext.getCmp("edituser_role_id")){			
			Ext.getCmp("edituser_role_id").setValue('');
		}
        var input = {
            "orgId":record.data.id,
        };
        var json = Ext.encode(input);

   		Ext.Ajax.request({
	   		url: 'role',
	        method : 'POST',
	        params:{ json:json},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				if(Ext.getCmp("adduser_role_id")){
					Ext.getCmp("adduser_role_id").setStore(respText);
				}else if(Ext.getCmp("edituser_role_id")){	
					Ext.getCmp("edituser_role_id").setStore(respText);
				}
	        }
	    });
	},
	
	//修改用户，选择组织
	editSelectOrg : function() {
		var userCategory = window.sessionStorage.getItem("userType");
		var url = '';
		//只有系统管理员角色，拥有管理员管理模块功能
		if (userCategory == -9) {
			url = 'organization/audited/list';
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
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
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
		    		
		    		data.adminUsername = data.username;
		    		data.adminEmail = data.email;
					if (retStatus == 'success') {
						var win = Ext.widget("edituser");
						rec.data = data;
						win.down("form").loadRecord(rec);
						win.show();	

				        $('#edituser input[name="phone"]').keypress(function(e) {              
				            if (!String.fromCharCode(e.keyCode).match(/[0-9\.]/)) {                
				                return false;             
				            }
				            if($('#edituser input[name="phone"]').val() != ''){      
				                if (!$('#edituser input[name="phone"]').val().match(/^\d{1,10}$/)) {                
				                    return false;     
				                }     
				            }      
				         });   

				        $('#edituser input[name="phone"]').keyup(function(){
				            $(this)[0].value=$(this)[0].value.replace(/[^\d]/g,'');
				        });
					}
		        }
		        /*,
		        failure : function() {
		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
		        }*/
		   });
	},
	
	//完成用户信息修改
	clickEditDone : function(btn) {
		//后台需要参数：id,username,roleId,organizationId
		var userInfo = this.getView().down('form').getForm().getValues();
		
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
			'email': userInfo.adminEmail
		};
		var json_input = Ext.encode(input);
		Ext.Ajax.request({
	   		url: 'user/update',//?json='+ Ext.encode(input),
	        method : 'POST',
	        params:{ json:json_input},
//	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
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
		        /*,
		        failure : function() {
		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
		        }*/
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
                }
            });
			
		}
	},
});
