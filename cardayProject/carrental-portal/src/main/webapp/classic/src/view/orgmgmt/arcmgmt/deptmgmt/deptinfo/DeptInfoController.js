Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.deptinfo.DeptInfoController', {
    extend: 'Ext.app.ViewController',
    requires: [
    ],
    
    loadDeptInfo: function(panel, eOpts ){
    	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
    	if(selection.length == 0){
//    		Ext.Msg.alert('提示', '请选择部门！');
    		return;
    	}
    	var select = selection[0].getData();
		var deptId = select.id;
		Ext.Ajax.request({
		    url: 'department/findOrganizationByOrgId/' + deptId,
		    method : 'GET',
		    success: function(resp, opts) {
		    	var result = Ext.util.JSON.decode(resp.responseText);
		    	Ext.getCmp("deptMgmtDeptInfo").reset();
		    	var form = Ext.getCmp("deptMgmtDeptInfo").getForm();                
                form.setValues(result.data);
                if(result.data.isInstitution){
                    Ext.getCmp("companyId").show();
                    Ext.getCmp("deptId").hide();
                    form.findField("institutionCode").show();
                    form.findField("institutionFeature").show();
                    form.findField("institutionLevel").show();
                    
                    form.findField('address').show();
                    form.findField('vehicleAdministrator').show();
                    form.findField('phone').show();
                    form.findField('parentName').show();
                }else{
                    Ext.getCmp("companyId").hide();
                    Ext.getCmp("deptId").show();
                    Ext.getCmp("deptId").setValue(result.data.name);
                    form.findField("institutionCode").hide();
                    form.findField("institutionFeature").hide();
                    form.findField("institutionLevel").hide();
                    
                    form.findField('address').hide();
                    form.findField('vehicleAdministrator').hide();
                    form.findField('phone').hide();
                    form.findField('parentName').hide();
                }
		    }
		});
    },
    
    addDept: function(btn){
    	var form = btn.up("form");
    	var params;
        if(form.getValues().deptType == ''){
            Ext.Msg.alert('提示', '请输入完整的信息！');
            return;
        }else{
            if(form.getValues().deptType == '0'){
                if(form.getValues().name[0] == '' || form.getValues().institutionCode == ''){
                    Ext.Msg.alert('提示', '请输入完整的信息！');
                    return;
                }else{
                    params = {
                        "name" : form.getValues().name[0],
                        "level" : form.getValues().level,
                        "parentId" : form.getValues().parentId,
                        "parentName" : form.getValues().parentName,
                        "institutionCode" : form.getValues().institutionCode,
                        "institutionFeature" : form.getValues().institutionFeature,
                        "institutionLevel" : form.getValues().institutionLevel,
                        "isInstitution" : true,
                        "address" : form.getValues().address,
                        "vehicleAdministrator" : form.getValues().vehicleAdministrator,
                        "phone" : form.getValues().phone
                    };
                }
            }else{
                if(form.getValues().name[1] == ''){
                    Ext.Msg.alert('提示', '请输入完整的信息！');
                    return;
                }else{
                    params = {
                        "name" : form.getValues().name[1],
                        "level" : form.getValues().level,
                        "parentId" : form.getValues().parentId,
                        "parentName" : form.getValues().parentName,
                        "isInstitution" : false,
                    };    
                }        
            }
        }
    	var json = Ext.encode(params);
    	Ext.Ajax.request({
		    url: 'department/appendChild',
		    method : 'POST',
		    params:{json:json},
		    success: function(resp, opts) {
		    	//{"data":"","status":"success"}
		    	var result = Ext.util.JSON.decode(resp.responseText);
		    	if(result.status == "success"){
		    		Ext.Msg.alert("提示信息", '添加成功');
		    		Ext.getCmp("deptMgmtTreeList").getStore().load();
		    	}else{
		    		Ext.Msg.alert("提示信息", '添加失败');
		    	}
		    	btn.up("window").close();
		    },
//		    failure: function(){
//		    	Ext.Msg.alert("提示信息", '添加失败');
//		    	btn.up("window").close();
//		    }
		});
    },
    
    deleteDept: function(){
    	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
    	if(selection.length == 0){
    		Ext.Msg.alert('提示', '请选择部门！');
    		return;
    	}
    	
    	Ext.MessageBox.confirm('确认', '您确定要删除此部门吗？', function(button){
    		if(button == "yes"){
    			var select = selection[0].getData();
    			var deptId = select.id;
    			Ext.Ajax.request({
    			    url: 'department/delete/' + deptId,
    			    method : 'GET',
    			    success: function(resp, opts) {
    			    	//{"data":"","status":"success"}
    			    	var result = Ext.util.JSON.decode(resp.responseText);
    			    	if(result.status == "success"){
    			    		Ext.Msg.alert("提示信息", '删除成功');
    			    		Ext.getCmp("deptMgmtTreeList").getStore().load();
    			    	}else{
    			    		if(result.data != null){
    			    			Ext.Msg.alert("提示信息", result.data);
    			    		}else{
    			    			Ext.Msg.alert("提示信息", '删除失败');
    			    		}
    			    	}
    			    },
//    			    failure: function(){
//    			    	Ext.Msg.alert("提示信息", '删除失败');
//    			    }
    			});
    		}
    	});
    },
    
    editDept: function(btn){
    	var form = btn.up("form");
    	var params;
//    	if(form.getValues().isInstitution == 'true' || form.getValues().isInstitution){            
        if(form.getValues().isInstitution == 'true'){            
            if(form.getValues().name[0] == '' || form.getValues().institutionCode == ''){
                Ext.Msg.alert('提示', '请输入完整的信息！');
                return;
            }else{
                params = {
                    "id":form.getValues().id,
                    "name" : form.getValues().name[0],
                    "level" : form.getValues().level,
                    "parentId" : form.getValues().parentId,
                    "parentName" : form.getValues().parentName,
                    "organizationId" : form.getValues().organizationId,
                    "institutionCode" : form.getValues().institutionCode,
                    "institutionFeature" : form.getValues().institutionFeature,
                    "institutionLevel" : form.getValues().institutionLevel,
                    "isInstitution" : true,
                    "address" : form.getValues().address,
                    "vehicleAdministrator" : form.getValues().vehicleAdministrator,
                    "phone" : form.getValues().phone
                };
            }
        }else{
                if(form.getValues().name[1] == ''){
                    Ext.Msg.alert('提示', '请输入完整的信息！');
                    return;
                }else{
                    params = {
                        "id":form.getValues().id,
                        "name" : form.getValues().name[1],
                        "level" : form.getValues().level,
                        "parentId" : form.getValues().parentId,
                        "parentName" : form.getValues().parentName,
                        "organizationId" : form.getValues().organizationId,
                        "isInstitution" : false,
                    };   
                }         
        }
    	var json = Ext.encode(params);
    	Ext.Ajax.request({
		    url: 'department/updateOrganization',
		    method : 'POST',
		    params:{json:json},
		    success: function(resp, opts) {
		    	//{"data":"","status":"success"}
		    	var result = Ext.util.JSON.decode(resp.responseText);
		    	if(result.status == "success"){
		    		Ext.Msg.alert("提示信息", '修改成功');
		    		Ext.getCmp("deptMgmtTreeList").getStore().load();
		    	}else{
		    		if(result.data != null){
		    			Ext.Msg.alert("提示信息", result.data);
		    		}else{
		    			Ext.Msg.alert("提示信息", '修改失败');
		    		}
		    	}
		    },
//		    failure: function(){
//		    	Ext.Msg.alert("提示信息", '修改失败');
//		    }
		});
    },
    
    openDeptChooseWin: function(combo, event, eOpts){
    	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
    	if(selection.length == 0){
//    		Ext.Msg.alert('提示', '请选择部门！');
    		return;
    	}
    	var select = selection[0].getData();
    	if(select.parentId == "root"){
			return;
		}
    	
    	if(Ext.getCmp("deptInfoDelBtn").disabled == true){
    		return;
    	}
    	
    	
    	var win = Ext.create("Admin.view.orgmgmt.arcmgmt.deptmgmt.deptinfo.DeptChooseWin",{
    		parentId: combo.up("form").getForm().findField("parentId").getValue(),
//    		id:combo.up("form").getForm().findField("id").getValue()
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
		var parentLevel = select.level;
    	var form = Ext.getCmp("deptMgmtDeptInfo").getForm();
    	form.findField("parentId").setValue(deptId);
    	form.findField("parentName").setValue(deptName);
    	form.findField("level").setValue(parentLevel + 1);
    	btn.up("window").close();
    },
    
    preventSelfDeptSelect: function(treepanel, record, index, eOpts){
    	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
    	if(selection.length == 0){
    		return false
    	}
    	var select = selection[0].getData();
		var deptId = select.id;
		
		var selectedDeptId = record.data.id;
		if(deptId == selectedDeptId){
			return false;
		}
		return true;
    }
})