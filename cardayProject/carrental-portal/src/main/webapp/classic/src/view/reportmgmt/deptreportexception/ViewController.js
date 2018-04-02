/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.deptreportexception.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [ 
			'Ext.window.MessageBox'
	],
	alias : 'controller.deptreportexceptioncontroller',
	
	onBeforeLoadOverSpeed : function() {

 		//曲线图
 		this.getViewModel().getStore("overSpeedStatistics").load();
 		//列表
 		this.getViewModel().getStore("overSpeedList").load();
	},

	onBeforeLoadOutMarker : function() {
 		//曲线图
 		this.getViewModel().getStore("outMarkerStatistics").load();
 		//列表
 		this.getViewModel().getStore("outMarkerList").load();
	},

	onBeforeLoadBackStation : function() {
 		//曲线图
 		this.getViewModel().getStore("backStationStatistics").load();
 		//列表
 		this.getViewModel().getStore("backStationList").load();
	},

	initParam : function(searchParam){
		var startDay = searchParam.startDate;
		var endDay = searchParam.endDate;
		var selfDept,childDept;

		if(searchParam.includeSelf == undefined){
			selfDept = false;
		}else{
			selfDept = true;
		}

		if(searchParam.includeChild == undefined){
			childDept = false;
		}else{
			childDept = true;
		}

		if(endDay == ""){
			var time = new Date();
            var m = time.getMonth() + 1;
            var date = time.getDate();
            if(m < 10){
            	m = "0"+ m;
            }

            if(date < 10){
            	date = "0"+ date;
            }

            endDay = time.getFullYear() + "-" + m + "-" + date;
		}

		if(startDay == ""){
            var initStartDay = new Date(endDay);
            /*查询30天前数据*/
            initStartDay = new Date(initStartDay.getTime() - 29*24*60*60*1000);
            var m = initStartDay.getMonth() + 1;
            var date = initStartDay.getDate();
            if(m < 10){
            	m = "0"+ m;
            }

            if(date < 10){
            	date = "0"+ date;
            }

            startDay = initStartDay.getFullYear() + "-"  + m + "-" + date;
		}

		var param = {};
		param.startDay = startDay;
		param.endDay = endDay;
		param.selfDept = selfDept;
		param.childDept = childDept;
		param.orgId = searchParam.deptId;

		return param;
	},
	
	onBeforeLoadOverSpeedUse : function() {
		var searchParam = Ext.getCmp('overSpeedSearchFormId').getValues();	
        var param = this.initParam(searchParam);
        if(searchParam.startDate == ""){
        	Ext.getCmp('overSpeedSearchFormId').getForm().findField('startDate').setValue(param.startDay);
        }
		var parm = {
 				'startDay': param.startDay, 
 				'endDay': param.endDay, 
				"orgId": param.orgId,
				"selfDept": param.selfDept,				
				"childDept": param.childDept,
 				'alertType': 'OVERSPEED',
 			};
		parm = Ext.encode(parm);		
		this.getViewModel().getStore("overSpeedStatistics").proxy.extraParams = {
			"json" : parm
		}
	},

	onBeforeLoadOutMarkerUse : function() {
		var searchParam = Ext.getCmp('outMarkerSearchFormId').getValues();	
        var param = this.initParam(searchParam);
        if(searchParam.startDate == ""){
        	Ext.getCmp('outMarkerSearchFormId').getForm().findField('startDate').setValue(param.startDay);
        }
		var parm = {
 				'startDay': param.startDay, 
 				'endDay': param.endDay,
				"orgId": param.orgId,
				"selfDept": param.selfDept,				
				"childDept": param.childDept,
 				'alertType': 'OUTBOUND', 
 			};
		parm = Ext.encode(parm);		
		this.getViewModel().getStore("outMarkerStatistics").proxy.extraParams = {
			"json" : parm
		}
	},

	onBeforeLoadBackStationUse : function() {
		var searchParam = Ext.getCmp('backStationSearchFormId').getValues();	
        var param = this.initParam(searchParam);
        if(searchParam.startDate == ""){
        	Ext.getCmp('backStationSearchFormId').getForm().findField('startDate').setValue(param.startDay);
        }
		var parm = {
 				'startDay': param.startDay, 
 				'endDay': param.endDay,
				"orgId": param.orgId,
				"selfDept": param.selfDept,				
				"childDept": param.childDept,
 				'alertType': 'VEHICLEBACK', 
 			};
		parm = Ext.encode(parm);		
		this.getViewModel().getStore("backStationStatistics").proxy.extraParams = {
			"json" : parm
		}
	},
	
	onBeforeOverSpeedList : function() {
		var searchParam = Ext.getCmp('overSpeedSearchFormId').getValues();
        var param = this.initParam(searchParam);
        if(searchParam.startDate == ""){
        	Ext.getCmp('overSpeedSearchFormId').getForm().findField('startDate').setValue(param.startDay);
        }
		var page=Ext.getCmp('overSpeedPageId').store.currentPage;
		var limit=Ext.getCmp('overSpeedPageId').store.pageSize;
		
		parm = {
				'startDay': param.startDay, 
 				'endDay': param.endDay,
				"orgId": param.orgId,
				"selfDept": param.selfDept,				
				"childDept": param.childDept,
 				'alertType': 'OVERSPEED', 
			    "currentPage" 	: page,
				"numPerPage" : limit,
			};
		parm = Ext.encode(parm);		
		this.getViewModel().getStore("overSpeedList").proxy.extraParams = {
			"json" : parm
		}
	},


	onBeforeOutMarkerList : function() {
		var searchParam = Ext.getCmp('outMarkerSearchFormId').getValues();
        var param = this.initParam(searchParam);
        if(searchParam.startDate == ""){
        	Ext.getCmp('outMarkerSearchFormId').getForm().findField('startDate').setValue(param.startDay);
        }
		var page=Ext.getCmp('outMarkerPageId').store.currentPage;
		var limit=Ext.getCmp('outMarkerPageId').store.pageSize;
		
		parm = {
				'startDay': param.startDay, 
 				'endDay': param.endDay,
				"orgId": param.orgId,
				"selfDept": param.selfDept,				
				"childDept": param.childDept,
 				'alertType': 'OUTBOUND', 
			    "currentPage" 	: page,
				"numPerPage" : limit,
			};
		parm = Ext.encode(parm);		
		this.getViewModel().getStore("outMarkerList").proxy.extraParams = {
			"json" : parm
		}
	},

	onBeforeBackStationList : function() {
		var searchParam = Ext.getCmp('backStationSearchFormId').getValues();
        var param = this.initParam(searchParam);
        if(searchParam.startDate == ""){
        	Ext.getCmp('backStationSearchFormId').getForm().findField('startDate').setValue(param.startDay);
        }
		var page=Ext.getCmp('backStationPageId').store.currentPage;
		var limit=Ext.getCmp('backStationPageId').store.pageSize;
		
		parm = {
				'startDay': param.startDay, 
 				'endDay': param.endDay,
				"orgId": param.orgId,
				"selfDept": param.selfDept,				
				"childDept": param.childDept,
 				'alertType': 'VEHICLEBACK', 
			    "currentPage" 	: page,
				"numPerPage" : limit,
			};
		parm = Ext.encode(parm);		
		this.getViewModel().getStore("backStationList").proxy.extraParams = {
			"json" : parm
		}
	},

	onExcelOverSpeedClick:function(){
		var searchParam = Ext.getCmp('overSpeedSearchFormId').getValues();	
        var param = this.initParam(searchParam);
        if(searchParam.startDate == ""){
        	Ext.getCmp('overSpeedSearchFormId').getForm().findField('startDate').setValue(param.startDay);
        }
		var parm = {
 				'startTime': param.startDay + ' 00:00:00', 
 				'endTime': param.endDay + ' 23:59:59',
				"orgId": param.orgId,
				"selfDept": param.selfDept,				
				"childDept": param.childDept,
 			};
    	window.location.href = 'vehicleAlert/exportOverspeedAlertData?json='  + Ext.encode(parm);
	},

	onExcelOutMarkerClick:function(){
		var searchParam = Ext.getCmp('outMarkerSearchFormId').getValues();	
        var param = this.initParam(searchParam);
        if(searchParam.startDate == ""){
        	Ext.getCmp('outMarkerSearchFormId').getForm().findField('startDate').setValue(param.startDay);
        }
		var parm = {
 				'startTime': param.startDay + ' 00:00:00', 
 				'endTime': param.endDay + ' 23:59:59',
				"orgId": param.orgId,
				"selfDept": param.selfDept,				
				"childDept": param.childDept,
 			};
    	window.location.href = 'vehicleAlert/exportOutboundAlertData?json='  + Ext.encode(parm);
	},

	onExcelBackStationClick:function(){
		var searchParam = Ext.getCmp('backStationSearchFormId').getValues();	
        var param = this.initParam(searchParam);
        if(searchParam.startDate == ""){
        	Ext.getCmp('backStationSearchFormId').getForm().findField('startDate').setValue(param.startDay);
        }
		var parm = {
 				'startTime': param.startDay + ' 00:00:00', 
 				'endTime': param.endDay + ' 23:59:59',
				"orgId": param.orgId,
				"selfDept": param.selfDept,				
				"childDept": param.childDept,
 			};
    	window.location.href = 'vehicleAlert/exportVehiclebackAlertData?json='  + Ext.encode(parm);
	},

	openDeptChooseWin: function(combo, event, eOpts){
		var win = Ext.create("Admin.view.reportmgmt.deptreportexception.DeptChooseWin",{
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
 		var form;

 		switch (Ext.getCmp("deptReportExceptionTab").getActiveTab().title) {
            case '超速报警':
            	form = Ext.getCmp("overSpeedSearchFormId").getForm();
            break;
            case '越界报警':
            	form = Ext.getCmp("outMarkerSearchFormId").getForm();
            break;
            case '回车报警':
            	form = Ext.getCmp("backStationSearchFormId").getForm();
            break;
        };

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
