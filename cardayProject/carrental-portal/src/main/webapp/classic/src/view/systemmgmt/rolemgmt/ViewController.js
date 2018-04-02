/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.rolemgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.systemmgmt.rolemgmt.View',
			'Admin.view.systemmgmt.rolemgmt.SearchForm',
			'Admin.view.systemmgmt.rolemgmt.AddRole',
			'Admin.view.systemmgmt.rolemgmt.EditRole',
			'Admin.view.systemmgmt.rolemgmt.ViewRole',
			'Admin.view.systemmgmt.rolemgmt.PrivilegeWindow',
			'Admin.view.systemmgmt.rolemgmt.EditPrivilegeWindow'
			],
	alias : 'controller.rolemgmtcontroller',
	
	
	onSearchClick : function(it, e) {
		Ext.Msg.alert("Message Box", "查询按钮");
	},

	onBeforeLoad : function() {
		var frmValues = this.lookupReference('searchForm').getValues();
		var pram = Ext.encode(frmValues);
		var pram='';
		this.getViewModel().getStore("rolesResults").proxy.extraParams = {
			"json" : pram
		}
		this.getViewModel().getStore("rolesResults").load();
	},

	//根据用户的输入的角色名称查询
	searchByRolename :　function() {
		var rolename = this.lookupReference('searchForm').getValues().rolename;	
//		if (rolename == '') {
//			Ext.Msg.alert('提示信息', '请输入要查询的角色名称！')
//			return;
//		}
		var input = {
			'role': rolename
		}
		var json = Ext.encode(input);
		Ext.Ajax.request({
	   		url: 'role',//?json='+ Ext.encode(input),
	        method : 'POST',
	        params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	
	        //alert(response.responseText);
			var respText = Ext.util.JSON.decode(response.responseText);
				var data = respText.data;
		    	this.getViewModel().getStore("rolesResults").loadData(data);
	        },
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	        scope:this
		});
	},
	
	//添加用户
	onAddClick : function() {
		//Ext.Msg.alert("Message Box", "添加按钮");
		win = Ext.widget('addrole');
		win.show();
		
	},
	
	//添加角色，选择组织
	selectOrg : function() {
		Ext.Ajax.request({
			url: 'user/loadCurrentUser',
	        method : 'POST',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				userCategory = respText.data.userCategory;				
				var url ='';
				
				if (userCategory == 0) {//超级管理员
					url = 'role/ownerRentList',
					Ext.Ajax.request({
						url: url,
				        method : 'GET',
				        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
				        success : function(response,options) {
							var respText = Ext.util.JSON.decode(response.responseText);
							var length = respText.data.length;
							//respText.data[length] = {'name': '通用'};
							Ext.getCmp('add_role_org_id').setStore(respText);
				        }
//				        ,
//				        failure : function() {
//				            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//				        },
				    });
				} else {//租户管理员
					var comstore = Ext.create('Ext.data.Store', {
					    fields: ['rentId', 'name'],
					    data : [
					        {"rentId": 0, "name":"通用"},
					    ]
					});
					Ext.getCmp('add_role_org_id').setStore(comstore);
				}
	        }
//	        ,
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	    });
	},
	
	//添加角色，选择角色模板
	selectRoleTemplate : function() {
		Ext.Ajax.request({
			url: 'role/roleTemplateList',
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				Ext.getCmp("add_role_roletemplate_id").setStore(respText);
				//Ext.getCmp("privilege_text").setValue(respText.data[0].resourceNames);
				//下面这种loadData的方法不行
				//Ext.getCmp("gridroleid").getStore('rolesResults').loadData(data);
	        }
//	        ,
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	    });
	},
	
	SelectRoleDone : function(items) {
		var roletemplateId = items.getValue();
		var url = 'role/' + roletemplateId + '/template';
		Ext.Ajax.request({
			url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				Ext.getCmp("privilege_text").setValue(respText.data.resourceNames);
				Ext.getCmp("privilege_id").setValue(respText.data.resourceIds);
	        }
//	        ,
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	    });
	},
	
	//完成添加角色
	addRoleDone : function(btn) {
		//organizationId,resourceIds,role,description
		var roleInfo = this.getView().down('form').getForm().getValues();
		//谁创建的role，organizationId就是谁。超级管理员，organizationId=0； 租户管理员organizationId=1
		var organizationId = window.sessionStorage.getItem('organizationId');
		
		var input = {
        		'organizationId': organizationId,
        		'resourceIds': roleInfo.privilegeIds,
        		'role': roleInfo.rolename,
        		'description': roleInfo.description,
        		'templateId': roleInfo.roletemplate,
			};
		var json = Ext.encode(input);
	   	 Ext.Ajax.request({
			url : 'role/create',//?json='+ Ext.encode(input),
	        method : 'POST',
	        params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var retStatus = respText.status;
				if (retStatus == 'success') {
					btn.up('addrole').close();
					Ext.Msg.alert('提示信息','添加成功');
					Ext.getCmp("gridroleid").getStore('rolesResults').load();
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        }*/
	    });
	},
	
	//修改角色，选择组织
	editSelectOrg : function() {
		Ext.Ajax.request({
			url: 'user/loadCurrentUser',
	        method : 'POST',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				userCategory = respText.data.userCategory;				
				var url ='';
				
				if (userCategory == 0) {//超级管理员
					url = 'role/ownerRentList',
					Ext.Ajax.request({
						url: url,
				        method : 'GET',
				        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
				        success : function(response,options) {
							var respText = Ext.util.JSON.decode(response.responseText);
							var length = respText.data.length;
							//respText.data[length] = {'name': '通用'};
							Ext.getCmp('editrole_org_id').setStore(respText);
				        }
				        /*,
				        failure : function() {
				            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
				        },*/
				    });
				} else {//租户管理员
					var comstore = Ext.create('Ext.data.Store', {
					    fields: ['rentId', 'name'],
					    data : [
					        {"rentId": 0, "name":"通用"},
					    ]
					});
					Ext.getCmp('editrole_org_id').setStore(comstore);
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	//修改角色，选择角色模板
	selectEditRoleTemplate : function() {
		Ext.Ajax.request({
			url: 'role/roleTemplateList',
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				Ext.getCmp("edit_role_roletemplate_id").setStore(respText);
				//下面这种loadData的方法不行
				//Ext.getCmp("gridroleid").getStore('rolesResults').loadData(data);
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	selectEditRoleTemplateDone : function(items) {
		var roleTemplateId = items.getValue();
		var url = 'role/' + roleTemplateId + '/template';
		Ext.getCmp('edit_role_roletemplate_id_id').setValue(roleTemplateId);
		
		Ext.Ajax.request({
			url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				Ext.getCmp("edit_privilege_text").setValue(respText.data.resourceNames);
				Ext.getCmp("edit_privilege_id").setValue(respText.data.resourceIds);
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	
	SelectOrgDone : function(items) {
		var orgId = items.getValue();
		Ext.getCmp('editrole_org_id_id').setValue(orgId);
		
	},
	
	//打开角色信息修改,根据id查询角色信息
	editRole : function(grid, rowIndex, colIndex){
		//var rec = grid.getStore().getAt(rowIndex);
		//var win = Ext.widget("editrole");
		//win.down("form").loadRecord(rec);
		//win.show();		
		var roleId = grid.getStore().getAt(rowIndex).data.id;
		var url = 'role/'+roleId+'/update';
		
		var rec = new Ext.data.Model();

		Ext.Ajax.request({
				url : url,
		        method : 'GET',
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
		        	var respText = Ext.util.JSON.decode(response.responseText);
		        	var data = respText.data;
		        	var retStatus = respText.status;
		        	var organizationId = respText.organizationId;
					if (retStatus == 'success') {
						var win = Ext.widget("editrole");
						if (data.organizationId == 0 || data.organizationId == -1) {
							data.organizationName = '通用';  //除了租户管理员之外，其他角色的所属租户都是 “通用”
						}
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
	
	//修改用户，完成角色修改
//	editPrivilige : funticon() {
//		var selNodes = Ext.getCmp('privilegetree_id').getChecked();
//		var text = new Array();
//		var roleId = new Array();
//		for (var i=0; i<selNodes.length; i++) {
//			text[i] = selNodes[i].data.text;
//			roleId[i] = selNodes[i].data.id;
//		}
//		Ext.getCmp('privilege_text').setValue(text.toString());
//		Ext.getCmp('privilege_id').setValue(roleId.toString());
//		btn.up('privilegewindow').close();
//	},
	
	
	//完成角色信息修改
	editRoleDone : function(btn) {
		
		//Ext.Msg.alert('修改角色','修改角色信息');
		//id,organizationId,resourceIds,role,description
		var roleInfo = this.getView().down('form').getForm().getValues();
		var firstId = Ext.getCmp('editrole_org_id_id').getValue();
		//organizationName
		var id = roleInfo.id;
		var input = {
				'id': roleInfo.id,
        		'organizationId': roleInfo.organizationId,
        		'templateId': roleInfo.templateId,
        		'resourceIds': roleInfo.resourceIds,
        		'role': roleInfo.role,
        		'description': roleInfo.description,
			};
		var json = Ext.encode(input);
		Ext.Ajax.request({
	   		url: 'role/update',//?json='+ Ext.encode(input),
	        method : 'POST',
	        params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var retStatus = respText.status;
				if (retStatus == 'success') {
					btn.up('editrole').close();
					Ext.Msg.alert('提示信息','修改成功');
					Ext.getCmp("gridroleid").getStore('rolesResults').load();
				}	        	
	        },
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	        scope:this
		});
	},

	//查看角色信息
	viweRole : function(grid, rowIndex, colIndex) {
//		var rec = grid.getStore().getAt(rowIndex);		
//		var win = Ext.widget("viewrole");
//		//console.dir(win.down("form"));
//		win.down("form").loadRecord(rec);
//		win.show();
		
		var roleId = grid.getStore().getAt(rowIndex).data.id;
		var url = 'role/'+roleId+'/update';
		
		var rec = new Ext.data.Model();

		Ext.Ajax.request({
				url : url,
		        method : 'GET',
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
		        	var respText = Ext.util.JSON.decode(response.responseText);
		        	var data = respText.data;
		        	if (data.organizationName == null || data.organizationName == '') {
		        		data.organizationName = '--';
		        	}
		        	var retStatus = respText.status;
		        	var organizationId = respText.organizationId;
					if (retStatus == 'success') {
						var win = Ext.widget("viewrole");
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
	
	//删除
	deleteRole : function(grid, rowIndex, colIndex) {
		Ext.Msg.confirm('消息提示','确定要删除吗！！！',function(btn){
			if (btn == 'yes') {
				var roleID = grid.getStore().getAt(rowIndex).id;
				var url = 'role/'+roleID+'/delete';
					
				Ext.Ajax.request({
		   		url: url,
		        method : 'POST',
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
							//删除成功后，刷新页面
							Ext.getCmp("gridroleid").getStore('rolesResults').load();
						}else{
							Ext.Msg.alert('消息提示',respText.message);
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

	//添加，点击选择权限按钮
	selectPrivilige : function(str, functionName) {
		win = Ext.widget('privilegewindow');
		
		Ext.Ajax.request({
	   		url: 'resource',
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var	retStatus = respText.status;
					if (retStatus == 'success') {					
//						var treeStore = new Ext.data.TreeStore();
//						
//						treeStore = {root: {
//				            expanded: true,
//				            children: [{
//				                text: 'SFOxxx  &nbsp;✈&nbsp; DFWxxx',
//				                duration: '6h 55m',
//				                expanded: true,
//				                children: [{
//				                    text: 'SFO &nbsp;✈&nbsp; PHX',
//				                    duration: '2h 04m',
//				                    leaf: false
//				                }, {
//				                    text: 'PHX layover',
//				                    duration: '2h 36m',
//				                    isLayover: true,
//				                    leaf: true
//				                }, {
//				                    text: 'PHX &nbsp;✈&nbsp; DFW',
//				                    duration: '2h 15m',
//				                    leaf: true
//				                }]
//				            }]
//				        }}
						
						var treeStore = new Ext.data.TreeStore();
						treeStore = respText;
						var resourceIds = new Array()
						resourceIds = Ext.getCmp('privilege_id').getValue().split(',');
						
						for (var i=0; i<resourceIds.length; i++) {
							//一级菜单
							if (treeStore.root.children != null) {
								for (var j=0; j<treeStore.root.children.length; j++) {
									if (resourceIds[i] == treeStore.root.children[j].id) {
										treeStore.root.children[j].checked = true;
									}
									//二级菜单
									if (treeStore.root.children[j].children !=null) {
										for (var k=0; k<treeStore.root.children[j].children.length; k++) {
											if (resourceIds[i] == treeStore.root.children[j].children[k].id) {
												treeStore.root.children[j].children[k].checked = true;
											}
											
											//三级菜单
											if (treeStore.root.children[j].children[k].children != null) {
												for (var l=0; l<treeStore.root.children[j].children[k].children.length; l++) {
													if (resourceIds[i] == treeStore.root.children[j].children[k].children[l].id) {
														treeStore.root.children[j].children[k].children[l].checked = true;
													}
												}
											}
										}
									}
								}
							}
						}

					
						Ext.getCmp('privilegetree_id').setStore(treeStore);
					}
		        },
//		        failure : function() {
//		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//		        },
		        scope:this
			});	
		
		win.show();
	},
	
	//添加，完成权限选择
	selectPriviligeDone : function(btn,str,functionName) {
		//var rolename = this.lookupReference('privilegetree');
		//获取选择权限节点
		var selNodes = Ext.getCmp('privilegetree_id').getChecked();
		var text = new Array();
		var roleId = new Array();
		for (var i=0; i<selNodes.length; i++) {
			text[i] = selNodes[i].data.text;
			roleId[i] = selNodes[i].data.id;
		}
		Ext.getCmp('privilege_text').setValue(text.toString());
		Ext.getCmp('privilege_id').setValue(roleId.toString());
		btn.up('privilegewindow').close();
	},
	
	//修改，点击选择权限按钮
	editSelectPrivilige : function(str, functionName) {
		win = Ext.widget('editprivilegewindow');
		Ext.Ajax.request({
	   		url: 'resource',
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var	retStatus = respText.status;
					if (retStatus == 'success') {											
						var treeStore = new Ext.data.TreeStore();
						treeStore = respText;
						var resourceIds = new Array()
						resourceIds = Ext.getCmp('edit_privilege_id').getValue().split(',');
						
						for (var i=0; i<resourceIds.length; i++) {
							//一级菜单
							if (treeStore.root.children != null) {
								for (var j=0; j<treeStore.root.children.length; j++) {
									if (resourceIds[i] == treeStore.root.children[j].id) {
										treeStore.root.children[j].checked = true;
									}
									//二级菜单
									if (treeStore.root.children[j].children !=null) {
										for (var k=0; k<treeStore.root.children[j].children.length; k++) {
											if (resourceIds[i] == treeStore.root.children[j].children[k].id) {
												treeStore.root.children[j].children[k].checked = true;
											}
											
											//三级菜单
											if (treeStore.root.children[j].children[k].children != null) {
												for (var l=0; l<treeStore.root.children[j].children[k].children.length; l++) {
													if (resourceIds[i] == treeStore.root.children[j].children[k].children[l].id) {
														treeStore.root.children[j].children[k].children[l].checked = true;
													}
												}
											}
										}
									}
								}
							}
						}

					
						Ext.getCmp('privilegetree_id').setStore(treeStore);
					}
		        },
//		        failure : function() {
//		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//		        },
		        scope:this
			});
		win.show();
	},
	
	//修改，完成权限选择
	editSelectPriviligeDone : function(btn,str,functionName) {
		//获取选择权限节点
		var selNodes = Ext.getCmp('privilegetree_id').getChecked();
		var text = new Array();
		var roleId = new Array();
		for (var i=0; i<selNodes.length; i++) {
			text[i] = selNodes[i].data.text;
			roleId[i] = selNodes[i].data.id;
		}
		Ext.getCmp('edit_privilege_text').setValue(text.toString());
		Ext.getCmp('edit_privilege_id').setValue(roleId.toString());
		btn.up('editprivilegewindow').close();
	},
	
	//点击批量导入按钮
	openFileUpWindow:function(){
		var win = Ext.create('Admin.view.systemmgmt.rolemgmt.FileUpLoad');
		win.show();
	},
	
	//导入文件
	uploadCSV:function(btn){
		var formPanel=this.getView().down('form');
		var form=this.getView().down('form').getForm();
		if (form.isValid()) {
            form.submit({
                url: 'role/import',
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
        		 	Ext.getCmp("gridroleid").getStore('rolesResults').load();
                }
            });
			
		}
	},
	
	//批量导出
	batchExport : function() {
    	console.log('导出excel...');
    	var gridPanel = Ext.getCmp('gridroleid');
		var record = gridPanel.getSelectionModel().getSelection();
		if(record.length == 0) {
			Ext.Msg.alert('消息提示', '请选择导出的数据');
			return;
		}
		var id = '';
		for (var i=0; i<record.length; i++) {
			id += record[i].data.id + ',';
		}
		console.log('id:' + id.substring(0,id.length-1));
		var input = {
						'ids' : id.substring(0,id.length-1)
		};
		window.location.href = 'role/export?json='  + Ext.encode(input);
    },
    
    //导出权限参照表
    exportAuthorizationData : function() {
    	window.location.href = 'role/exportResource';
    }
});
