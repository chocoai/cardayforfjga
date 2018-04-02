/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.annualInspection.ViewController', {
    extend: 'Ext.app.ViewController',
    alias : 'controller.annualInspectionController',
    requires: [
    ],
    onBeforeLoad : function() {
    	var currentPage=Ext.getCmp('annualInspectionPage').getStore().currentPage;    	
        if(window.sessionStorage.getItem("annualInspection") == '1'){
            this.lookupReference('searchForm').getForm().findField('insuranceStatus').setValue('1M');
            this.lookupReference('searchForm').getForm().findField('inspectionStatus').setValue('');
            window.sessionStorage.setItem("annualInspection", '0');
        }else if(window.sessionStorage.getItem("annualInspection") == '2'){            
            this.lookupReference('searchForm').getForm().findField('insuranceStatus').setValue('');
            this.lookupReference('searchForm').getForm().findField('inspectionStatus').setValue('1M');
            window.sessionStorage.setItem("annualInspection", '0');
        }
        var parm = this.lookupReference('searchForm').getValues();
    	var vehicleNumber = parm.vehicleNumber;
    	var arrangedOrgName = parm.arrangedOrgName;
    	
    	var selfDept = parm.selfDept;
    	var childDept = parm.childDept;
    	if(selfDept == undefined){
    		selfDept = false;
    	}
    	if(childDept == undefined){
    		childDept = false;
    	}
    	var pram={
    			'currentPage' : currentPage,
    			'numPerPage': 10,
    			'vehicleNumber' : vehicleNumber,
    			'deptId' : arrangedOrgName,
    			'insuranceStatus' : parm.insuranceStatus,
    			'inspectionStatus' : parm.inspectionStatus,
    			'childDept': childDept,
    			'selfDept': selfDept
    	};
    	Ext.getCmp('annualInspectionGrid').getViewModel().getStore('annualInspectionStore').proxy.extraParams = {
			"json" : Ext.encode(pram)
		}
//    	Ext.getCmp('annualInspectionGrid').getViewModel().getStore('annualInspectionStore').load();
    },
    loadGirdData: function() {
    	var currentPage=Ext.getCmp('annualInspectionPage').getStore().currentPage;
    	var pram={
    			'currentPage' : currentPage,
    			'numPerPage': 10
    	};
    	Ext.getCmp('annualInspectionGrid').getViewModel().getStore('annualInspectionStore').proxy.extraParams = {
			"json" : Ext.encode(pram)
		}
    	Ext.getCmp('annualInspectionGrid').getViewModel().getStore('annualInspectionStore').load();
    },
    showResetInsuranceTimeWindow : function(grid, rowIndex, colIndex) {
    	console.log('++++showResetInsuranceTimeWindow++++');
    	var rec = grid.getStore().getAt(rowIndex);
    	console.log('id:' + rec.data.id);
    	var win = Ext.widget("resetInsuranceTimeWindow", {
    		title: '更新保险到期日',
    		closable: true,
    		buttonAlign : 'center',
    		mid : rec.data.id,
    	});
    	win.down("form").loadRecord(rec);
    	win.show();
    },
    onClickResetInsuranceTime : function(btn) {
    	console.log('++++resetInsuranceTime++++');
    	var annualInspection = this.getView().down('form').getForm().getValues();
    	
    	var insuranceDueTimeF = annualInspection.insuranceDueTimeF;
    	var insuranceDueTime = annualInspection.insuranceDueTime;
//    	console.log('insuranceDueTimeF:' + insuranceDueTimeF);
    	
    	var oDate1 = new Date(insuranceDueTime);
        var oDate2 = new Date(insuranceDueTimeF);
        if(oDate1.getTime() <= oDate2.getTime()) {
        	Ext.Msg.alert("提示信息", '设置的时间必须大于上次保险时间');
        	return;
        }
    	var json = Ext.encode(annualInspection);
    	Ext.Ajax.request({
        	url:'vehicleAnnualInspection/resetInsuranceTime',//?json='+Ext.encode(annualInspection),
			method:'POST',
            params:{json:json},
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
			success: function(res){
				var appendData=Ext.JSON.decode(res.responseText);
				if(appendData.status=='success'){
			 		btn.up('window').close();
//				    Ext.getCmp("searchVehicle").fireEvent("click");
			 		Ext.Msg.alert("提示信息", '更新成功');
			 		
			 		//表数据加载
			 		var pram={
			    			'currentPage' : 1,
			    			'numPerPage': 10
			    	};
			    	Ext.getCmp('annualInspectionGrid').getViewModel().getStore('annualInspectionStore').proxy.extraParams = {
						"json" : Ext.encode(pram)
					}
			    	Ext.getCmp('annualInspectionGrid').getViewModel().getStore('annualInspectionStore').load();
				}else{
					btn.up('window').close();
					Ext.Msg.alert("提示信息", '更新失败');
				}
			 }
            /*,
			failure : function() {
				Ext.Msg.alert('Failure','Call interface error!');
			}*/
        });
    },
    showResetInspectionTimeWindow : function(grid, rowIndex, colIndex) {
    	console.log('++++showResetInspectionTimeWindow++++');
    	var rec = grid.getStore().getAt(rowIndex);
    	console.log('id:' + rec.data.id);
    	var win = Ext.widget("resetInspectionTimeWindow", {
    		title: '更新下次年检日期',
    		closable: true,
    		buttonAlign : 'center',
    		mid : rec.data.id,
    	});
    	win.down("form").loadRecord(rec);
    	win.show();
    },
    onClickResetInspectionTime : function(btn) {
    	console.log('++++resetInspectionTime++++');
    	var annualInspection = this.getView().down('form').getForm().getValues();
    	var inspectionNextTime = annualInspection.inspectionNextTime;
    	var inspectionNextTimeF = annualInspection.inspectionNextTimeF;
    	var oDate1 = new Date(inspectionNextTime);
        var oDate2 = new Date(inspectionNextTimeF);
        if(oDate1.getTime() <= oDate2.getTime()) {
        	Ext.Msg.alert("提示信息", '设置的时间必须大于下次年检时间');
        	return;
        }
    	var json = Ext.encode(annualInspection);
    	Ext.Ajax.request({
        	url:'vehicleAnnualInspection/resetInspectionTime',//?json='+Ext.encode(annualInspection),
			method:'POST',
            params:{json:json},
			//headers: {'Content-Type':'application/json','charset':'UTF-8'},
			success: function(res){
				var appendData=Ext.JSON.decode(res.responseText);
				if(appendData.status=='success'){
			 		btn.up('window').close();
//				    Ext.getCmp("searchVehicle").fireEvent("click");
			 		Ext.Msg.alert("提示信息", '更新成功');
			 		
			 		//表数据加载
			 		var pram={
			    			'currentPage' : 1,
			    			'numPerPage': 10
			    	};
			    	Ext.getCmp('annualInspectionGrid').getViewModel().getStore('annualInspectionStore').proxy.extraParams = {
						"json" : Ext.encode(pram)
					}
			    	Ext.getCmp('annualInspectionGrid').getViewModel().getStore('annualInspectionStore').load();
				}else{
					btn.up('window').close();
					Ext.Msg.alert("提示信息", '更新失败');
				}
			 }
            /*,
			failure : function() {
				Ext.Msg.alert('Failure','Call interface error!');
			}*/
        });
    },
    onClickSearch : function() {
//    	console.log('++++on click search++++');
    	Ext.getCmp('annualInspectionPage').getStore().currentPage = 1;
    	var parm = this.lookupReference('searchForm').getValues();
    	var vehicleNumber = parm.vehicleNumber;
//    	console.log('vehicleNumber:' + vehicleNumber);
    	var arrangedOrgName = parm.arrangedOrgName;
//    	console.log('arrangedOrgName:' + arrangedOrgName);
    	var pram={
    			'currentPage' : 1,
    			'numPerPage' : 10,
    			'vehicleNumber' : vehicleNumber,
    			'deptId' : arrangedOrgName
    	};
    	Ext.getCmp('annualInspectionGrid').getViewModel().getStore('annualInspectionStore').proxy.extraParams = {
			"json" : Ext.encode(pram)
		}
    	Ext.getCmp('annualInspectionGrid').getViewModel().getStore('annualInspectionStore').load();
    },
    
    openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.vehiclemgmt.annualInspection.DeptChooseWin",{
     		deptId:combo.up("form").getForm().findField("arrangedOrgName").getValue()
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
     	var form = Ext.getCmp("annualInspectionSearchForm").getForm();
     	form.findField("arrangedOrgName").setValue(deptId);
//     	form.findField("organizationId").setDisplayField(deptName);
     	form.findField("arrangedOrgNameName").setValue(deptName);
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

