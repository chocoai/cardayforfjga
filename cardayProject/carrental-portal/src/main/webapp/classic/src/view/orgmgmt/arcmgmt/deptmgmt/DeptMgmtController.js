Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.DeptMgmtController', {
    extend: 'Ext.app.ViewController',
    requires: [
    ],
    
    onTreeSelect: function(tree, record, index, eOpts){
    	var data = record.getData();
    	var deptId = data.id;
    	if(Ext.getCmp("deptMgmtTreeList") != null){
    		Ext.getCmp("deptMgmtTreeList").lastSelectedId = deptId;
    	}
    	
    	//树节点变化，重新加载当前tab页的信息
    	var tabpanel =　Ext.getCmp("orgmgmtDeptmgmtTab");
    	if(tabpanel != null){
    		var activatePanel = tabpanel.getActiveTab();
	    	if(activatePanel.title == '组织信息'){
	    		activatePanel.loadData();
	    	}else{
	    		var panel = tabpanel.child("#deptMgmtDeptInfo");
	    		tabpanel.setActiveTab(panel);
	    	}	
    	}


        if(window.sessionStorage.getItem("userType") == '2' || window.sessionStorage.getItem("userType") == '6'){
            if(record.data.parentId == "root"){
                var length = tabpanel.items.items.length;
                var i = 0;
                while(i < length){
                    if(i != 0){
                        tabpanel.items.items[1].close();                        
                    }
                    i++;
                }
            }else{
                if(tabpanel.items.items.length > 2){
                    return;
                }

                var empMgmtPanel = Ext.create('Ext.tab.Panel',{
                    title:'民警管理',
                    id:'EmpMgmtTabId',
                    region:'center',
                    listeners:{
                        activate: 'onActivateforEmpMgmt',
                    },
                    items:[
                        {
                            title:'现有民警',
                            xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.ShowAssignedEmp',
                        },{
                            title:'可分配民警',
                            xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.ShowAvailableAssignEmp',
                        }]
                });
                //tabpanel.add(empMgmtPanel);
                /*插入到第二个*/
                tabpanel.add(empMgmtPanel);
                //tabpanel.insert(1,empMgmtPanel);

/*                var creditLimitPanel = Ext.create('Admin.view.orgmgmt.arcmgmt.deptmgmt.creditlimitmgmt.ShowCreditLimitMgmt',{
                    title:'额度管理',  
                });
                tabpanel.add(creditLimitPanel);*/

                var vehicleMgmtPanel = Ext.create('Ext.tab.Panel',{
                    title:'车辆管理',                
                    id:'vehicleMgmtTabId',
                    region:'center',
                    listeners:{
                         activate: 'onActivateforVehicleMgmt',
                    },
                    items:[
                    {
                        title:'现有车辆', 
                        tFlag : '0',
                        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.ShowAssignedVehicle',
                    },{
                        title:'可分配车辆',
                        tFlag : '1',
                        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.ShowAvailableAssignVehicle',
                    }]
                });

                tabpanel.add(vehicleMgmtPanel);

                var driverMgmtPanel = Ext.create('Ext.tab.Panel',{
                    title:'司机管理',  
                    id:'driverMgmtTabId',
                    region:'center',
                    listeners:{
                         activate: 'onActivateforDriverMgmt',
                    },
                    items:[
                    {
                        title:'现有司机', 
                        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.ShowAssignedDriver',
                    },{
                        title:'可分配司机',
                        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.ShowAvailableAssignDriver',
                    }]
                });
                tabpanel.add(driverMgmtPanel);
            }

        }
    },
    
    addDept: function(){
    	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
    	if(selection.length == 0){
    		Ext.Msg.alert('提示', '请选择组织！');
    		return;
    	}
//    	var win = Ext.create("Admin.view.orgmgmt.arcmgmt.deptmgmt.deptinfo.AddDept");
    	
    	var select = selection[0].getData();
		var parentId = select.id;
		Ext.Ajax.request({
		    url: 'department/appendChild/show/' + parentId,
		    method : 'GET',
		    success: function(resp, opts) {
		    	var result = Ext.util.JSON.decode(resp.responseText);
		    	console.log(resp);
                if(result.data.isInstitution){
    		    	var win = Ext.create("Admin.view.orgmgmt.arcmgmt.deptmgmt.deptinfo.AddDept");
    		    	win.down("form").reset();
    		    	var child = {};
    		    	child.parentName = result.data.parentName;
    		    	child.level = result.data.level;
    		    	child.parentId = result.data.parentId;
    		    	win.down("form").getForm().setValues(child);
    		    	win.show();
                }else{                    
                    Ext.Msg.alert('提示', '部门下不能创建单位或者部门！');
                    return;
                }
		    }
		});
    },

    onActivateforVehicleMgmt: function(me,eOpts){
        console.log('onActivateforVehicleMgmt'); 
        Ext.getCmp("vehicleMgmtTabId").setActiveTab(0);     
        Ext.getCmp("searchFormAssignedVehicle").getForm().reset();        
        var VehicleStore = Ext.getCmp("gridAssignedVehicle").getStore();
        VehicleStore.currentPage = 1;
        Ext.getCmp('gridAssignedVehicle').AllSelectedAssignedRecords.length = 0; 
        Ext.getCmp('gridAssignedVehicle').getSelectionModel().clearSelections();
        Ext.getCmp("gridAssignedVehicle").getViewModel().getStore("assignedVehicleStore").load();
    },

    onActivateforDriverMgmt: function(me,eOpts){
        console.log('onActivateforDriverMgmt'); 
        Ext.getCmp("driverMgmtTabId").setActiveTab(0);     
        Ext.getCmp("searchFormAssignedDriver").getForm().reset();        
        var VehicleStore = Ext.getCmp("gridAssignedDriver").getStore();
        VehicleStore.currentPage = 1;
        Ext.getCmp('gridAssignedDriver').getSelectionModel().clearSelections();
        Ext.getCmp('gridAssignedDriver').AllSelectedAssignedRecords.length = 0;
        Ext.getCmp("gridAssignedDriver").getViewModel().getStore("assignedDriverStore").load();
    },

    onActivateforEmpMgmt: function(me,eOpts){
        console.log('onActivateforEmpMgmt');
        Ext.getCmp("EmpMgmtTabId").setActiveTab(0);
        Ext.getCmp("searchFormAssignedEmp").getForm().reset();
        var VehicleStore = Ext.getCmp("gridAssignedEmp").getStore();
        VehicleStore.currentPage = 1;
        Ext.getCmp('gridAssignedEmp').getSelectionModel().clearSelections();
        Ext.getCmp('gridAssignedEmp').AllSelectedAssignedRecords.length = 0;
        Ext.getCmp("gridAssignedEmp").getViewModel().getStore("assignedEmpStore").load();
    },
})