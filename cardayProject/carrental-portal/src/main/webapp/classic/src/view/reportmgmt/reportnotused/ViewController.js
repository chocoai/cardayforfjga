/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.reportnotused.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.reportmgmt.reportnotused.View',
			'Admin.view.reportmgmt.reportnotused.SearchForm',
			],

	onBeforeLoad : function() {
		console.log('onBeforeLoad');
		if(this.lookupReference('searchForm').getValues().startDate == ''){
			var today = new Date();
			var sevenDays = new Date(today-30*24*3600*1000);
			var formValue = this.lookupReference('searchForm').form;
			formValue.findField('endDate').setValue(today);
			formValue.findField('startDate').setValue(sevenDays);
		}

		var frmValues = this.lookupReference('searchForm').getValues();
		var page=Ext.getCmp('noUsedDrivingGridPage').store.currentPage;
		var limit=Ext.getCmp('noUsedDrivingGridPage').pageSize;
		var selfDept,childDept;

		if(frmValues.includeSelf == undefined){
			selfDept = false;
		}else{
			selfDept = true;
		}

		if(frmValues.includeChild == undefined){
			childDept = false;
		}else{
			childDept = true;
		}

		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"orgId":frmValues.deptId,
				"selfDept": selfDept,				
				"childDept": childDept,
				"starttime" : frmValues.startDate,
				"endtime" : frmValues.endDate,
			};
		var pram = Ext.encode(input);
		this.getViewModel().getStore("drivingGridStore").proxy.extraParams = {
			"json" : pram
		}
	},


	onSearchClick :　function() {
		console.log('onSearchClick');		
		var VehicleStore = this.lookupReference('noUsedDrivingGrid').getStore();
		VehicleStore.currentPage = 1;
		this.getViewModel().getStore("drivingGridStore").load();
	},

	onSearchLatestSevenDays :　function() {
		console.log('onSearchLatestSevenDays');
		var today = new Date();
		var sevenDays = new Date(today-7*24*3600*1000);
		var formValue = this.lookupReference('searchForm').form;
		formValue.findField('endDate').setValue(today);
		formValue.findField('startDate').setValue(sevenDays);
		var VehicleStore = this.lookupReference('noUsedDrivingGrid').getStore();
		VehicleStore.currentPage = 1;
		this.getViewModel().getStore("drivingGridStore").load();
	},

	onSearchLatestFiveDays :　function() {
		console.log('onSearchLatestFiveDays');
		var today = new Date();
		var sevenDays = new Date(today-5*24*3600*1000);
		var formValue = this.lookupReference('searchForm').form;
		formValue.findField('endDate').setValue(today);
		formValue.findField('startDate').setValue(sevenDays);
		var VehicleStore = this.lookupReference('noUsedDrivingGrid').getStore();
		VehicleStore.currentPage = 1;
		this.getViewModel().getStore("drivingGridStore").load();
	},

	onSearchLatestThreeDays :　function() {
		console.log('onSearchLatestThreeDays');
		var today = new Date();
		var sevenDays = new Date(today-3*24*3600*1000);
		var formValue = this.lookupReference('searchForm').form;
		formValue.findField('endDate').setValue(today);
		formValue.findField('startDate').setValue(sevenDays);
		var VehicleStore = this.lookupReference('noUsedDrivingGrid').getStore();
		VehicleStore.currentPage = 1;
		this.getViewModel().getStore("drivingGridStore").load();
	},

	onExcelClick :　function() {
		console.log('onExcelClick');
		var frmValues = this.lookupReference('searchForm').getValues();
		var selfDept,childDept;

		if(frmValues.includeSelf == undefined){
			selfDept = false;
		}else{
			selfDept = true;
		}

		if(frmValues.includeChild == undefined){
			childDept = false;
		}else{
			childDept = true;
		}

		var input = {				
				"orgId":frmValues.deptId,
				"selfDept": selfDept,				
				"childDept": childDept,
				"starttime" : frmValues.startDate,
				"endtime" : frmValues.endDate,
			};
		window.location.href = 'usage/report/exportIdleVehicleList?json='  + Ext.encode(input);
	},

	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.reportmgmt.reportnotused.DeptChooseWin",{
     		deptId:combo.up("form").getForm().findField("deptId").getValue()
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
     	var form = Ext.getCmp("reportNotUsedSearchForm").getForm();
     	form.findField("deptId").setValue(deptId);
     	form.findField("deptName").setValue(deptName);
     	btn.up("window").close();
    },

    checkIsGroupNull: function(chk, newValue, oldValue, eOpts ){
     	var group = chk.up("checkboxgroup");
     	var value = group.getValue();
     	if(value.includeSelf == null && value.includeChild == null){
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
