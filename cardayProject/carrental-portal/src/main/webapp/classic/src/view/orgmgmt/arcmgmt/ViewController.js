/**
 * This class is the template view for the application.
 */
function findchildnode(node,nodeId){
	var childnodes = node.childNodes;
	for(var i=0;i<childnodes.length;i++){  //从节点中取出子节点依次遍历
        var rootnode = childnodes[i];
        if(rootnode.data.id == nodeId){
        	rootnode.set('checked', true);
        }else{
        	rootnode.set('checked', false);
        }
        if(rootnode.childNodes.length>0){  //判断子节点下是否存在子节点
            findchildnode(rootnode,nodeId);    //如果存在子节点  递归
        }   
    }
}

Ext.define('Admin.view.orgmgmt.arcmgmt.ViewController', {
    extend: 'Ext.app.ViewController',
    requires: [
    ],
    alias : 'controller.arcmgmtcontroller',
    onBeforeLoad : function() {
    	var currentPage=Ext.getCmp('argmgmtpage').getStore().currentPage;
    	var input={
    			'currentPage' : currentPage,
    			'numPerPage': 20
    	};
    	Ext.getCmp('orgDepartmentGridId').getViewModel().getStore("arcMgmtResults").proxy.url = "department/listDirectChildrenWithCount";
    	Ext.getCmp('orgDepartmentGridId').getViewModel().getStore("arcMgmtResults").proxy.extraParams = {
			"json" : Ext.encode(input)
		};
    },
    onItemClick: function(node,e) {
    	var roonodes = Ext.getCmp("orgTreeList").getRootNode();
//    	console.log(e.id);
    	findchildnode(roonodes,e.id);  //开始递归
    	
    	//test
//    	var childnodes = roonodes.childNodes;
//    	for(var i=0;i<childnodes.length;i++){  //从节点中取出子节点依次遍历
//            var rootnode = childnodes[i];
//            console.log('path::' + rootnode.getPath());
//
//        }
//    	console.log(Ext.getCmp('orgTreeList').getStore().getNodeById('2').getPath());
    	//end
    	
    	
    	
    	//根据部门编号动态加载表格数据
//    	this.getViewModel().getStore("arcMgmtResults").proxy.url = 
//            "organization/" + DEPARTMENT_ID + "/tree/listByOrgId"
//       ;
    	var input = {"orgId" : e.id};
    	Ext.getCmp("orgDepartmentGridId").getStore("arcMgmtResults").proxy.url = 'department/listDirectChildrenWithCountByOrgId?json=' + Ext.encode(input),
    	Ext.getCmp('orgDepartmentGridId').getStore("arcMgmtResults").load();
    	
//    	var input = {"orgId" : e.id};
//    	Ext.Ajax.request({
//			url : 'organization/listDirectChildrenWithCountByOrgId?json=' + Ext.encode(input),
//			method : 'GET',
//			defaultHeaders : {
//				'Content-type' : 'application/json;utf-8'
//			},
//			success : function(response, options) {
//
//				var respText = Ext.util.JSON.decode(response.responseText);
//				var data = respText.data;
//				// this.getViewModel().getStore("personmgmtReport").loadData(data);
//				Ext.getCmp("orgDepartmentGridId").getStore("arcMgmtResults")
//						.loadData(data);
//			},
//			failure : function() {
//				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//			},
//			scope : this
//		});
    	
    },
    
    searchClick: function() {
//    	console.log('根据部门编号搜索');
    	var departmentId = this.lookupReference('searchForm').getValues().department_id;
		if (departmentId == '') {
			Ext.Msg.alert('提示信息', '请输入要查询的部门编号！')
			return;
		}
		
		var input = {
			"id" : 	departmentId
		};
    	Ext.Ajax.request({
			url : 'department/findById?json='+ Ext.encode(input),
			method : 'GET',
			defaultHeaders : {
				'Content-type' : 'application/json;utf-8'
			},
			success : function(response, options) {

				var respText = Ext.util.JSON.decode(response.responseText);
				var data = [];
		    	data[0] = respText.data;
		    	//接口只返回了一条记录
		    	if (data.length == 1) {
		    		Ext.getCmp("orgDepartmentGridId").getStore("arcMgmtResults")
					.loadData(data);
		    	}
			},
//			failure : function() {
//				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//			},
			scope : this
		});
    },
    
    addDepartmentClick: function() {
    	win = Ext.widget('departmentView');
		win.show();
    },
    
    queryArcMgmtInfo : function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);		
		var win = Ext.widget("queryAndDeleteView", {
			title: '修改企业信息',
			buttonAlign : 'center',
			buttons : [{
				text : '关闭',
				handler: 'clickDone'
			}]
		});
		console.dir(win.down("form"));
		win.down("form").loadRecord(rec);
		win.show();
    },
    clickDone: function() {
    	this.view.close();
    },
    addTreeData: function(btn) {
//    	 Ext.Ajax.request({
//    		 url : 'organization/tree',
//             method : 'GET',
//
//             defaultHeaders : {'Content-type' : 'application/json;utf-8'},
//             success : function(response,options) {
//             	alert(response.responseText);
//                 
//             },
//             failure : function() {
//                 Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//             }
//         });

    	var objectjson = this.getView().down('form').getForm().getValues();
		var departmentName = objectjson.name;
		//表单验证
		if(departmentName == '') {
			Ext.Msg.alert('消息提示','请输入名称！');
			return;
		}
		
		
//		var selNodes = Ext.getCmp("orgTreeList").getChecked();
		var departmentId = '';
		var text = new Array();
//		for (var i=0; i<selNodes.length; i++) {
//			text[i] = selNodes[i].data.id;
//		}
		
    	//获取登陆用户
		var organizationId = '';
    	Ext.Ajax.request({
            url: './user/loadCurrentUser',
            method: 'POST',
            async: false,
            success: function(response) {
                var resp = Ext.decode(response.responseText);
                if (typeof(resp.data.organizationId) != 'undefined') {
                	organizationId = resp.data.organizationId;
                }
            },
            failure: function(response) {
                var resp = Ext.decode(response.responseText);
                console.log('loadCurrentUser failed: ' + resp);
            }
        });
//		console.log('add department organization id:' + organizationId);
//		var organizationId = selNodes[0].data.id;
		
		//start
		var departmentinfo = this.getView().down('form').getForm().getValues();	
		Ext.Ajax.request({
			url:'department/' + organizationId + '/appendChild',
			method:'GET',
			headers: {'Content-Type':'application/json','charset':'UTF-8'},
            success: function(response) {
                var data=Ext.JSON.decode(response.responseText);
                if(data.status=='success'){
                	console.log('name:' + departmentinfo.name);
                	console.log('parentId:' + data.data.parentId);
                	console.log('parentIds:' + data.data.parentIds);
		              var input = {
						"name" : departmentinfo.name,
						"address" :departmentinfo.address,
						"linkmanPhone" :departmentinfo.telephone,
						"introduction" :departmentinfo.introduction,
						"parentId" : data.data.parentId,
						"parentIds":data.data.parentIds
						};
                    var json = Ext.encode(input);
                    Ext.Ajax.request({
//                    	url:'organization/' + organizationId + '/appendChild?json='+Ext.encode(input),
                    	url:'department/appendChild',//?json='+Ext.encode(input),
						method:'POST',
                        params:{json:json},
						//headers: {'Content-Type':'application/json','charset':'UTF-8'},
						success: function(response){
						 	btn.up('departmentView').close();
						 	Ext.Msg.alert("提示信息", '添加成功');
						 }
                    });
//                	//重新加载树
//                    var orgTreeStore = Ext.getCmp('orgTreeList').getStore();
//                	orgTreeStore.reload({callback: function(records, options, success){
//                		var nodePath = Ext.getCmp('orgTreeList').getStore().getNodeById(organizationId).getPath();
//                		Ext.getCmp('orgTreeList').getStore().getNodeById(organizationId).set('checked', true);
//                		Ext.getCmp('orgTreeList').expandPath(nodePath);
//                	}
//                	});
                	input = {"orgId" : organizationId};
                	Ext.getCmp('orgDepartmentGridId').getStore("arcMgmtResults").currentPage = 1;
                	Ext.getCmp("orgDepartmentGridId").getStore("arcMgmtResults").proxy.url = 'department/listDirectChildrenWithCount',
                	Ext.getCmp("orgDepartmentGridId").getStore("arcMgmtResults").load();
                }else{
                    Ext.MessageBox.alert("提示信息","Operation failed!");
                }
            }
			/*,
            failure : function() {
						Ext.Msg.alert('Failure','Call interface error!');
					}*/
			});
		//end
		
//		this.getViewModel().getStore("arcMgmtResults").proxy.url = "organization/" + organizationId + "/listDirectChildrenById";
//    	console.log('object1111::' + this.getViewModel());
//    	this.getViewModel().getStore("arcMgmtResults").load();
    },
    editEnterInfo: function(grid, rowIndex, colIndex){
//    	var selNodes = Ext.getCmp("orgTreeList").getChecked();
//    	if(selNodes.length == 0) {
//    		Ext.Msg.alert('提示信息','请先选择部门');
//    		return;
//    	}
    	
    		var rec = grid.getStore().getAt(rowIndex);		
    		var win = Ext.widget("departmentView", {
    			title: '修改部门信息',
    			buttonAlign : 'center',
    			buttons : [{
                    text: '修改',
                    handler: 'updateDepartmentInfo'
                },{
    				text : '关闭',
    				handler: 'clickDone'
    			}]
    		});
    		console.dir(win.down("form"));
    		win.down("form").loadRecord(rec);
    		win.show();
    	},
    	updateDepartmentInfo: function() {
//    		alert('修改成功');
    		var objectjson = this.getView().down('form').getForm().getValues();
    		var departmentName = objectjson.name;
    		var id = objectjson.id;
//    		console.log('current_organization_id:' + id)
    		//start
    		var input = {
            		'id': id,
            		'name': departmentName
    			};
    		
//    		var selNodes = Ext.getCmp("orgTreeList").getChecked();
//    		var organizationId = selNodes[0].data.id;
    		var json = Ext.encode(input);
    	   	 Ext.Ajax.request({
    			url : 'department/' + id + '/update',//?json='+ Ext.encode(input),
    	        method : 'POST',
                params:{json:json},
    	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
    	        success : function(response,options) {
    	        	var respText = Ext.util.JSON.decode(response.responseText);
    	        	var retStatus = respText.status;
//    	        	console.log('retStatus:' + retStatus);
    				if (retStatus == 'success') {
//    					btn.up('personView').close();
    					Ext.Msg.alert('提示信息','修改成功');
//    					var url = 'organization/' + organizationId + '/listDirectChildrenById';
    					Ext.getCmp('orgDepartmentGridId').getStore("arcMgmtResults").currentPage = 1;
                    	Ext.getCmp("orgDepartmentGridId").getStore("arcMgmtResults").proxy.url = 'department/listDirectChildrenWithCount',
                    	Ext.getCmp("orgDepartmentGridId").getStore("arcMgmtResults").load();
    				}
    	        }
//                ,
//    	        failure : function() {
//    	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//    	        }
    	    });
    		//end

    		//重新加载store
    		
    		this.view.close();
    	},
    	deleteArcMgmtInfo : function(grid, rowIndex, colIndex) {
    		var selNodes = Ext.getCmp("orgTreeList").getChecked();
        	if(selNodes.length == 0) {
        		Ext.Msg.alert('提示信息','请先选择部门');
        		return;
        	}
    		var rec = grid.getStore().getAt(rowIndex);		
    		var win = Ext.widget("queryAndDeleteView", {
    			title: '修改企业信息',
    			buttonAlign : 'center',
    			buttons : [{
    				text : '关闭',
    				handler: 'clickDone'
    			},{
    				text: '删除',
    				handler: 'delelteFormInfo'
    			} ]
    		});
    		console.dir(win.down("form"));
    		win.down("form").loadRecord(rec);
    		win.show();
        },
        delelteFormInfo: function(grid, rowIndex, colIndex) {
        	Ext.Msg.confirm('消息提示','部门删除后不可恢复，确认要删除？',function(btn){
    			if (btn == 'yes') {
    				var departmentID = grid.getStore().getAt(rowIndex).id;
//    				console.log('userID:' + departmentID);
    				var url = 'department/'+departmentID+'/delete';
    					
    				Ext.Ajax.request({
    		   		url: url,
    		        method : 'POST',
    		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
    		        success : function(response,options) {
    					var respText = Ext.util.JSON.decode(response.responseText);
    					var	retStatus = respText.status;
    					var data = respText.data;
    						if (retStatus == 'success') {
    							console.log('data:' + data);
    							if(data == '-1'){
    								Ext.Msg.alert('消息提示','该部门下有所属员工，无法删除！');
    								return;
    							}
    							if(data == '-2'){
    								Ext.Msg.alert('消息提示','该部门下有所属车辆，无法删除！');
    								return;
    							}
    							if(data == '-3'){
    								Ext.Msg.alert('消息提示','该部门下有所属订单，无法删除！');
    								return;
    							}
    							//删除成功后，刷新页面
    							Ext.getCmp('orgDepartmentGridId').getStore("arcMgmtResults").currentPage = 1;
    							Ext.getCmp("orgDepartmentGridId").getStore("arcMgmtResults").load();
//    							var selNodes = Ext.getCmp("orgTreeList").getChecked();
//    		                	var organizationId = selNodes[0].data.id;
//    							var orgTreeStore = Ext.getCmp('orgTreeList').getStore();
//    							orgTreeStore.reload({callback: function(records, options, success){
//    		                		var nodePath = Ext.getCmp('orgTreeList').getStore().getNodeById(organizationId).getPath();
//    		                		Ext.getCmp('orgTreeList').getStore().getNodeById(organizationId).set('checked', true);
////    		                		Ext.Msg.alert('info','加载完毕');
//    		                		console.log('path:' + nodePath);
//    		                		Ext.getCmp('orgTreeList').expandPath(nodePath);
//    		                	}
//    		                	});
    						}
    			        },
//    			        failure : function() {
//    			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//    			        },
    			        scope:this
    				});		
    			}
    		});
        },
        loadDepartmentsInfo: function() {
        	//获取登陆用户
//        	var organizationId = '';
//        	Ext.Ajax.request({
//                url: '/carrental-portal/user/loadCurrentUser',
//                method: 'POST',
//                async: false,
//                success: function(response) {
//                    var resp = Ext.decode(response.responseText);
//                    if (typeof(resp.data.organizationId) != 'undefined') {
//                    	organizationId = resp.data.organizationId;
//                    }
//                },
//                failure: function(response) {
//                    var resp = Ext.decode(response.responseText);
//                    console.log('loadCurrentUser failed: ' + resp);
//                }
//            });

//        	this.getViewModel().getStore("arcMgmtResults").proxy.url = "organization/" + organizationId + "/listDirectChildrenById";
        	this.getViewModel().getStore("arcMgmtResults").proxy.url = "department/listDirectChildrenWithCount";
        	this.getViewModel().getStore("arcMgmtResults").load();
        	
        },
        showEmployeemgmtView: function(grid, rowIndex, colIndex){
        	var rec = grid.getStore().getAt(rowIndex);
        	var win = Ext.widget("employeemgmt", {
    			title: '当前部门员工信息',
    			closable: false,
    			buttonAlign : 'center',
    			organizationId: rec.data.id,
    			buttons : [{
    				text : '关闭',
    				handler: 'toBackDeparmentView'
    			}]
    		});
    		win.show();
        },
        showAddEmployeemgmtView: function(){
        	var organizationId = Ext.getCmp("employeemgmt").organizationId;
//        	console.log('+++showAddEmployeemgmtView+++organization id : ' + organizationId);
        	var win = Ext.widget("addEmployeemgmt", {
    			title: '添加员工',
    			buttonAlign : 'center',
    			closable: false,
    			organizationId : organizationId,
    			buttons : [{
    				text : '返回',
    				handler: 'toBackEmployeemgmtView'
    			}]
    		});
        	this.view.close();
    		win.show();
        },
        loadOrganizationPersonInfo: function() {
        	var organizationId = Ext.getCmp("employeemgmt").organizationId;
//        	console.log('load person of departments info');
//        	console.log('organization id : ' + organizationId);
        	var input = {'orgId' : organizationId};
        	
        	Ext.getCmp("departmentPersonGrid").getViewModel().getStore("employeeStore").proxy.url = 'user/listDirectUserListByOrgId?json=' + Ext.encode(input),
        	Ext.getCmp('departmentPersonGrid').getViewModel().getStore("employeeStore").load();
//        	Ext.Ajax.request({
//    			url : "user/listDirectUserListByOrgId?json=" + Ext.encode(input),
//    			method : 'GET',
//    			defaultHeaders : {
//    				'Content-type' : 'application/json;utf-8'
//    			},
//    			success : function(response, options) {
//
//    				var respText = Ext.util.JSON.decode(response.responseText);
//    				var data = respText.data;
//    		    	console.log('length:' + data.length);
//    		    	Ext.getCmp('departmentPersonGrid').getViewModel().getStore('employeeStore')
//    					.loadData(data);
//    			},
//    			failure : function() {
//    				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//    			},
//    			scope : this
//    		});
        },
        loadDepartmentEmpInfo : function() {
        	var input = {
        		'currentPage':	1,
        		'numPerPage': 20
        	};
        	Ext.getCmp('orgDepartmentGridId').getViewModel().getStore("arcMgmtResults").proxy.url = "department/listDirectChildrenWithCount";
        	Ext.getCmp('orgDepartmentGridId').getViewModel().getStore("arcMgmtResults").proxy.extraParams = {
    			"json" : Ext.encode(input)
    		};
        	Ext.getCmp('orgDepartmentGridId').getViewModel().getStore("arcMgmtResults").load();
        },
        loadPersonInfo: function() {
//        	console.log('+++++++loadPersonInfo+++++++');
//        	var organizationId = Ext.getCmp("addEmployeemgmt").organizationId;
//        	console.log('+++loadPersonInfo+++organization id : ' + organizationId);
        	Ext.getCmp('addEmployeeView').getViewModel().getStore("employeeStore").proxy.url = "user/listEnterpriseRootNodeUserList";
        	Ext.getCmp('addEmployeeView').getViewModel().getStore("employeeStore").load();
        },
        toBackEmployeemgmtView: function() {
        	var organizationId = Ext.getCmp("addEmployeemgmt").organizationId;
//        	console.log('organization id : ' + organizationId);
        	var win = Ext.widget("employeemgmt", {
    			title: '部门员工信息',
    			closable: false,
    			buttonAlign : 'center',
    			organizationId: organizationId,
    			buttons : [{
    				text : '关闭',
    				handler: 'toBackDeparmentView'
    			}]
    		});
        	this.view.close();
    		win.show();
        },
        moveToRootNode: function(grid, rowIndex, colIndex) {
        	var rec = grid.getStore().getAt(rowIndex);
        	var msg = '是否确认将员工' + rec.data.realname + '移除' +  rec.data.organizationName;
        	Ext.Msg.confirm('消息提示', msg, function(btn){
        		if (btn == 'yes') {
//        			console.log('organizationId:' + rec.data.organizationId + '; id' + rec.data.id);
        			//调用部门移除接口 start
        			var input = {'orgId' : rec.data.organizationId,
        					     'userId': rec.data.id};
        			var json = Ext.encode(input);
                	Ext.Ajax.request({
//            			url : "user/removeUserToEnterpriseRootNode?json=" + Ext.encode(input),
            			url : "user/removeUserToEnterpriseRootNode",
            			method : 'POST',
            			params:{json:json},
            			success : function(response, options) {
            				var respText = Ext.util.JSON.decode(response.responseText);
            				var status = respText.status;
//            		    	console.log('length:' + status);
            		    	if(status == 'success') {
            		    		Ext.Msg.alert('消息提示', '移除成功');
            		    		var input = {'orgId' : rec.data.organizationId};
            		    		Ext.getCmp('departmentPersonGrid').getViewModel().getStore('employeeStore')
            					.proxy.url = "user/listDirectUserListByOrgId?json=" + Ext.encode(input);
            		    		Ext.getCmp('departmentPersonGrid').getViewModel().getStore('employeeStore')
            					.load();
            		    	}
            			},
//            			failure : function() {
//            				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//            			},
            			scope : this
            		});
        			//调用部门移除接口 end
        		}
        	});
        },
        confirmAddEmployee: function() {
        	var organizationId = Ext.getCmp("addEmployeemgmt").organizationId;
//        	console.log('to confirm add employee extjs function');
        	var gridPanel = Ext.getCmp('addEmployeeView');
    		var record = gridPanel.getSelectionModel().getSelection();
//    		console.log('length:' + record.length);
    		if(record.length == 0) {
    			Ext.Msg.alert('消息提示', '请选择一名员工');
    			return;
    		}
    		var userId = '';
    		var orgId = organizationId;
    		for (var i=0; i<record.length; i++) {
//    			console.log('user_id:' + record[i].data.id + ';organization_id:' + record[i].data.organizationId);
    			userId += record[i].data.id + ',';
    		}
//    		console.log('organization_id:' + orgId);
    		userId = userId.substr(0,userId.length-1);
//    		console.log('userId:' + userId);
    		
    		var input = {
    			'orgId' : orgId,
    			'userId' : userId
    		};
    		var json = Ext.encode(input);
    		Ext.Ajax.request({
//    			url : "user/batchChangeUserOrganization?json=" + Ext.encode(input),
    			url : "user/batchChangeUserOrganization",
    			method : 'POST',
    			params:{json:json},
    			success : function(response, options) {
    				var respText = Ext.util.JSON.decode(response.responseText);
    				var status = respText.status;
    				var win = Ext.widget("employeemgmt", {
            			title: '当前部门员工信息',
            			closable: false,
            			buttonAlign : 'center',
            			organizationId: organizationId,
            			buttons : [{
            				text : '关闭',
            				handler: 'toBackDeparmentView'
            			}]
            		});
                	
    		    	if(status == 'success') {
//    		    		Ext.Msg.alert('消息提示', '添加成功');
//    		    		Ext.getCmp('addEmployeeView').getViewModel().getStore("employeeStore").proxy.url = "user/listEnterpriseRootNodeUserList";
//    		        	Ext.getCmp('addEmployeeView').getViewModel().getStore("employeeStore").reload();
    		    		Ext.Msg.alert('消息提示', '添加成功', function(text) {
                           	Ext.getCmp("addEmployeemgmt").close();
     		    		    win.show();
                        });
    		    	}
    			},
//    			failure : function() {
//    				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//    			},
    			scope : this
    		});
        },
        toBackDeparmentView: function() {
//        	console.log('to back department view');
//        	var selNodes = Ext.getCmp("orgTreeList").getChecked();
//        	var organizationId = selNodes[0].data.id;
//        	console.log('++++toBackDeparmentView++organizationId+++' + organizationId);
//        	Ext.getCmp('orgDepartmentGridId').getStore("arcMgmtResults").proxy.url = "department/listDirectChildrenWithCountByOrgId";
//        	var param = {"orgId" : organizationId};
//        	param = Ext.encode(param);
        	Ext.getCmp('orgDepartmentGridId').getViewModel().getStore("arcMgmtResults").proxy.url = "department/listDirectChildrenWithCount";
        	Ext.getCmp('orgDepartmentGridId').getViewModel().getStore("arcMgmtResults").load();
        	this.view.close();
        }
});

