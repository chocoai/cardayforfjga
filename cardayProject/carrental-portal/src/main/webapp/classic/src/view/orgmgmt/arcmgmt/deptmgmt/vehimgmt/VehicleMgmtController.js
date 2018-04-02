Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.VehicleMgmtController', {
    extend: 'Ext.app.ViewController',
    requires: [
    ],

    onBeforeLoadforAssignedVehicle: function () {
    	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
    	if(selection.length == 0){
    		return;
    	}
    	var select = selection[0].getData();
		var deptId = select.id;
		var vehicleModel;
		console.log('onBeforeLoadforAssignedVehicle');
		var frmValues = Ext.getCmp('searchFormAssignedVehicle').getValues();
		var page=Ext.getCmp('assignedVehiclePage').store.currentPage;
		var limit=Ext.getCmp('assignedVehiclePage').pageSize;
		if(frmValues.vehicleModel == "全部"){
			vehicleModel = '-1';
		}else{
			vehicleModel = frmValues.vehicleModel;
		}
		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"vehicleNumber":frmValues.vehicleNumber.replace(/\s/g, ""),
				"vehicleModel" : vehicleModel,
				"vehicleFromId":frmValues.fromOrgId,
				"vehiclePurpose":frmValues.vehiclePurpose,
				"deptId":deptId,
			};
		var pram = Ext.encode(input);
		Ext.getCmp('gridAssignedVehicle').getViewModel().getStore("assignedVehicleStore").proxy.extraParams = {
			"json" : pram
		}
	},

	onSearchClickforAssignedVehicle :　function() {
		console.log('onSearchClickforAssignedVehicle');
		var VehicleStore = this.lookupReference('gridAssignedVehicle').getStore();
		VehicleStore.currentPage = 1;
		Ext.getCmp('gridAssignedVehicle').getViewModel().getStore("assignedVehicleStore").load();
	},

	onResetClickforAssignedVehicle : function() {
		this.lookupReference('searchFormAssignedVehicle').getForm().reset();
	},

    onBeforeLoadforAvailableAssignVehicle: function () {
    	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
    	if(selection.length == 0){
    		return;
    	}
    	var select = selection[0].getData();
		var deptId = select.id;
		var vehicleModel;

		console.log('onBeforeLoadforAvailableAssignVehicle');
		var frmValues = Ext.getCmp('searchFormAvailableAssignVehicle').getValues();
		var page=Ext.getCmp('availableAssignVehiclePage').store.currentPage;
		var limit=Ext.getCmp('availableAssignVehiclePage').pageSize;
		if(frmValues.vehicleModel == "全部"){
			vehicleModel = '-1';
		}else{
			vehicleModel = frmValues.vehicleModel;
		}
		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"vehicleNumber":frmValues.vehicleNumber.replace(/\s/g, ""),
				"vehicleModel" : vehicleModel,
				"vehicleFromId":frmValues.fromOrgId,
				"vehiclePurpose":frmValues.vehiclePurpose,
				"deptId":deptId,
			};
		var pram = Ext.encode(input);
		Ext.getCmp("gridAvailableAssignVehicle").getViewModel().getStore("availableAssignVehicleStore").proxy.extraParams = {
			"json" : pram
		}
	},

	onSearchClickforAvailableAssignVehicle :　function() {
		console.log('onSearchClickforAvailableAssignVehicle');
		var VehicleStore = this.lookupReference('gridAvailableAssignVehicle').getStore();
		VehicleStore.currentPage = 1;
		Ext.getCmp("gridAvailableAssignVehicle").getViewModel().getStore("availableAssignVehicleStore").load();
	},

	onResetClickforAvailableAssignVehicle : function() {
		this.lookupReference('searchFormAvailableAssignVehicle').getForm().reset();
	},

	//打开查看车辆信息窗体
	viewVehicleInfo:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.create('Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.ViewVehicleInfo');
		var objectModel = new Ext.data.Model();
		var data={
				'vehicleNumber':rec.data.vehicleNumber
		}
		var json = Ext.encode(data);
		Ext.Ajax.request({
        	url:'vehicle/findVehicleInfoByVehicleNumber',//?json='+Ext.encode(vehiclInfo),
			method:'POST',
	        params:{json:json},
			success: function(response){
				var respText = Ext.util.JSON.decode(response.responseText);
    	    	var data = respText.data;
            	var retStatus = respText.status;
    			if (retStatus == 'success') {
    				objectModel.data = data;
    				
    				//将未分配改为企业名显示
    				if (objectModel.data.arrangedOrgName=="未分配") {
//    					data.depName = '暂未分配';
    					var userType = window.sessionStorage.getItem('userType');
    					if(userType == '2' || userType == '6'){
    			      		//2是用车企业 6是租车企业
    			      		//3是部门
    			        	var entName = window.sessionStorage.getItem("organizationName");
    			        	objectModel.data.arrangedOrgName = entName;
    			      	}
    				}
    				
    				win.down("form").loadRecord(objectModel);
    				win.show();
    			}
			 },
			failure : function() {
				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
			},
        });
	},

    removeVehicle: function(vehicleIds,vehicleNumber,deptName) {
        var msg = '是否确认将车辆 ' + vehicleNumber + ' 从部门 ' +  deptName + ' 移除?';
        console.log('+++unassignVehicle+++vehicleId: ' + vehicleIds);
        Ext.Msg.confirm('消息提示', msg, function(btn){
            if (btn == 'yes') {
                //调用部门移除接口 start
                var input = {'ids' : vehicleIds,};
                var json = Ext.encode(input);
                Ext.Ajax.request({
                    url : "vehicle/organization/removeVehicleFromOrg",
                    //url: 'app/data/arcmgmtinfo/vehicleRemoveErrorMsg.json',
                    method : 'POST',
                    params:{ json:json},
                    success : function(response, options) { 
                        var respText = Ext.util.JSON.decode(response.responseText);
                        var status = respText.status;
                        if(status == 'success') {
                            Ext.Msg.alert('消息提示', '移除车辆成功！');
                            Ext.getCmp("searchFormAssignedVehicle").getForm().reset();
                            var assignVehicleStore = Ext.getCmp('assignedVehiclePage').store;
                            assignVehicleStore.currentPage = 1;
                            Ext.getCmp('gridAssignedVehicle').AllSelectedAssignedRecords.length = 0; 
                            Ext.getCmp('gridAssignedVehicle').getSelectionModel().clearSelections();
                            Ext.getCmp("gridAssignedVehicle").getViewModel().getStore("assignedVehicleStore").load();
                        }else{                         
                            var win = Ext.widget("showVehicleRemoveErrorWindow");
                            for (var i = 0; i < respText.data.length; i++) {
                                if(i==0){
                                    var errorMsg;
                                    if(respText.data[i].driverInfo != null && respText.data[i].orderList != null){
                                        errorMsg = '请先解绑司机并处理未完成订单！'; 
                                    }else if(respText.data[i].driverInfo == null && respText.data[i].orderList != null){
                                        errorMsg = '请先处理未完成订单！';
                                    }else if(respText.data[i].driverInfo != null && respText.data[i].orderList == null){
                                        errorMsg = '请先解绑司机！';
                                    }
                                    var html = '<div class="errorVehcileNum">'+ respText.data[i].titleName +'</div><div class="errorMsg">' + errorMsg +'</div>';
                                    Ext.getCmp("showVehicleRemoveErrorWindow").items.items[i].items.items[0].setHtml(html);
                                    if(respText.data[i].driverInfo != null){
                                        Ext.getCmp("showVehicleRemoveErrorWindow").items.items[i].items.items[1].getViewModel().getStore("driverInfoStore").insert(0,respText.data[i].driverInfo);
                                    }else{
                                        Ext.getCmp("showVehicleRemoveErrorWindow").items.items[i].items.items[1].hide();
                                    }

                                    if(respText.data[i].orderList != null){
                                        for(var n = 0; n < respText.data[i].orderList.length; n++){
                                            Ext.getCmp("showVehicleRemoveErrorWindow").items.items[i].items.items[2].getViewModel().getStore("tasksInfoStore").insert(n,respText.data[i].orderList[n]);
                                        }
                                    }else{
                                        Ext.getCmp("showVehicleRemoveErrorWindow").items.items[i].items.items[2].hide();
                                    }
                                }else{
                                    var item = {
                                        xtype:'confirmVehicleRemoveError',
                                    };
                                    Ext.getCmp('showVehicleRemoveErrorWindow').add(item);
                                    var errorMsg;
                                    if(respText.data[i].driverInfo != null && respText.data[i].orderList != null){
                                        errorMsg = '请先解绑司机并处理未完成订单！'; 
                                    }else if(respText.data[i].driverInfo == null && respText.data[i].orderList != null){
                                        errorMsg = '请先处理未完成订单！';
                                    }else if(respText.data[i].driverInfo != null && respText.data[i].orderList == null){
                                        errorMsg = '请先解绑司机！';
                                    }
                                    var html = '<div class="errorVehcileNum">'+ respText.data[i].titleName +'</div><div class="errorMsg">' + errorMsg +'</div>';
                                    Ext.getCmp("showVehicleRemoveErrorWindow").items.items[i].items.items[0].setHtml(html);
                                    if(respText.data[i].driverInfo != null){
                                        Ext.getCmp("showVehicleRemoveErrorWindow").items.items[i].items.items[1].getViewModel().getStore("driverInfoStore").insert(0,respText.data[i].driverInfo);
                                    }else{
                                        Ext.getCmp("showVehicleRemoveErrorWindow").items.items[i].items.items[1].hide();
                                    }

                                    if(respText.data[i].orderList != null){
                                        for(var n = 0; n < respText.data[i].orderList.length; n++){
                                            Ext.getCmp("showVehicleRemoveErrorWindow").items.items[i].items.items[2].getViewModel().getStore("tasksInfoStore").insert(n,respText.data[i].orderList[n]);
                                        }
                                    }else{
                                        Ext.getCmp("showVehicleRemoveErrorWindow").items.items[i].items.items[2].hide();
                                    }
                                }
                            }
                            win.show();
                            Ext.getCmp("searchFormAssignedVehicle").getForm().reset();
                            var assignVehicleStore = Ext.getCmp('assignedVehiclePage').store;
                            assignVehicleStore.currentPage = 1;
                            /*清除选中*/
                            Ext.getCmp('gridAssignedVehicle').AllSelectedAssignedRecords.length = 0; 
                            Ext.getCmp('gridAssignedVehicle').getSelectionModel().clearSelections();
                            Ext.getCmp("gridAssignedVehicle").getViewModel().getStore("assignedVehicleStore").load();                         
                        }
                    },
                    failure : function() {
                        Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
                    },
                    scope : this
                });
                //调用已分配车辆移除接口 end
            }
        });
    },

	//移除已分配车辆
	unassignVehicle: function(grid, rowIndex, colIndex) {
     	var rec = grid.getStore().getAt(rowIndex);
     	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
    	if(selection.length == 0){
    		return;
    	}
    	var select = selection[0].getData();
        var vehicleIds = rec.data.id;
        var vehicleNumber = rec.data.vehicleNumber;
        var deptName = select.text;

        this.removeVehicle(vehicleIds, vehicleNumber,deptName);
     },

    assignVehicleforDept: function(vehicleIds,allocateDepId,vehicleNumbers,deptName) {
        var msg = '是否确认将车辆 ' + vehicleNumbers + ' 分配到部门 ' +  deptName + ' ?';
        console.log('+++unassignVehicle+++vehicleId: ' + vehicleIds);
        Ext.Msg.confirm('消息提示', msg, function(btn){
            if (btn == 'yes') {
                //调用部门移除接口 start
                var input = {'ids' : vehicleIds,
                             'allocateDepId': allocateDepId};
                var json = Ext.encode(input);
                Ext.Ajax.request({
                    url : "vehicle/organization/addVehicelToCurrOrg",
                    method : 'POST',
                    params:{ json:json},
                    success : function(response, options) {
                        var respText = Ext.util.JSON.decode(response.responseText);
                        var status = respText.status;
                        if(status == 'success') {
                            Ext.Msg.alert('消息提示', '分配车辆成功！');
                            Ext.getCmp("searchFormAvailableAssignVehicle").getForm().reset();
                            var avialiableVehicleStore = Ext.getCmp('availableAssignVehiclePage').store;
                            avialiableVehicleStore.currentPage = 1;
                            Ext.getCmp('gridAvailableAssignVehicle').AllSelectedUnAssignedRecords.length = 0;
                            Ext.getCmp('gridAvailableAssignVehicle').getSelectionModel().clearSelections();
                            Ext.getCmp("gridAvailableAssignVehicle").getViewModel().getStore("availableAssignVehicleStore").load();
                        }else{
                            Ext.Msg.alert('消息提示', '分配车辆失败！');
                            Ext.getCmp("searchFormAvailableAssignVehicle").getForm().reset();
                            var avialiableVehicleStore = Ext.getCmp('availableAssignVehiclePage').store;
                            avialiableVehicleStore.currentPage = 1;
                            Ext.getCmp('gridAvailableAssignVehicle').AllSelectedUnAssignedRecords.length = 0;
                            Ext.getCmp('gridAvailableAssignVehicle').getSelectionModel().clearSelections();
                            Ext.getCmp("gridAvailableAssignVehicle").getViewModel().getStore("availableAssignVehicleStore").load();
                        }
                    },
                    failure : function() {
                        Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
                    },
                    scope : this
                });
                //调用已分配车辆移除接口 end
            }
        });
    },

   	//分配车辆
	assignVehicle: function(grid, rowIndex, colIndex) {
     	var rec = grid.getStore().getAt(rowIndex);
     	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
    	if(selection.length == 0){
    		return;
    	}
    	var select = selection[0].getData();
        var vehicleIds = rec.data.id;
        var allocateDepId = select.id;
        var vehicleNumbers = rec.data.vehicleNumber;
        var deptName = select.text;

        this.assignVehicleforDept(vehicleIds,allocateDepId,vehicleNumbers,deptName);

     },

    /*已有车辆的选中识别*/
    checkVehSelectAssigned: function (me, record, index, opts) {
        Ext.getCmp('gridAssignedVehicle').AllSelectedAssignedRecords.push(record);
    },

    checkVehdeSelectAssigned: function (me, record, index, opts) {
	    Ext.getCmp('gridAssignedVehicle').AllSelectedAssignedRecords = Ext.Array.filter(Ext.getCmp('gridAssignedVehicle').AllSelectedAssignedRecords, function (item) {
	        return item.get("id") != record.get("id");
	    });
	},
	loadforAssignedVehicle: function (me, records, success, opts) {
        if (!success || !records || records.length == 0)
            return;

        //根据全局的选择，初始化选中的列
        var selModel = Ext.getCmp('gridAssignedVehicle').getSelectionModel();
        Ext.Array.each(Ext.getCmp('gridAssignedVehicle').AllSelectedAssignedRecords, function () {
            for (var i = 0; i < records.length; i++) {
                var record = records[i];
                if (record.get("id") == this.get("id")) {
                    selModel.select(record, true, true);    //选中record，并且保持现有的选择，不触发选中事件
                }
            }
        });
    },

	/*可分配车辆的选中识别*/
    checkVehSelectUnAssigned: function (me, record, index, opts) {
        Ext.getCmp('gridAvailableAssignVehicle').AllSelectedUnAssignedRecords.push(record);
    },

    checkVehdeSelectUnAssigned: function (me, record, index, opts) {
	    Ext.getCmp('gridAvailableAssignVehicle').AllSelectedUnAssignedRecords = Ext.Array.filter(Ext.getCmp('gridAvailableAssignVehicle').AllSelectedUnAssignedRecords, function (item) {
	        return item.get("id") != record.get("id");
	    });
	},
	loadforAvailableAssignVehicle: function (me, records, success, opts) {
        if (!success || !records || records.length == 0)
            return;

        //根据全局的选择，初始化选中的列
        var selModel = Ext.getCmp('gridAvailableAssignVehicle').getSelectionModel();
        Ext.Array.each(Ext.getCmp('gridAvailableAssignVehicle').AllSelectedUnAssignedRecords, function () {
            for (var i = 0; i < records.length; i++) {
                var record = records[i];
                if (record.get("id") == this.get("id")) {
                    selModel.select(record, true, true);    //选中record，并且保持现有的选择，不触发选中事件
                }
            }
        });
    },

	/*批量移除车辆*/
    batchUnassignedVehicle: function() {
     	var record = Ext.getCmp('gridAssignedVehicle').AllSelectedAssignedRecords;
     	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();

        if(record.length < 1){
            Ext.Msg.alert('消息提示', '请先选择要移除的车辆！');
            return;
        }
    	if(selection.length == 0){
    		return;
    	}
    	var select = selection[0].getData();
    	var vehicleNumbers,vehicleIds;
    	for(var i = 0 ; i < record.length; i++){
    		if(i==0){
    			vehicleNumbers = record[i].data.vehicleNumber;
    			vehicleIds = record[i].data.id;
    		}else{
    			vehicleNumbers += ',' + record[i].data.vehicleNumber;
    			vehicleIds += ',' + record[i].data.id ;
    		}    		
    	}
        var vehicleNumber = vehicleNumbers;
        var deptName = select.text;
        this.removeVehicle(vehicleIds, vehicleNumber,deptName);
    },

	/*批量分配车辆*/
    batchAssignedVehicle: function() {
     	var record = Ext.getCmp('gridAvailableAssignVehicle').AllSelectedUnAssignedRecords;
     	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();

        if(record.length < 1){
            Ext.Msg.alert('消息提示', '请先选择要分配的车辆！');
            return;
        }
        
    	if(selection.length == 0){
    		return;
    	}
    	var select = selection[0].getData();
    	var vehicleNumbers,vehicleIds;        
        var allocateDepId = select.id;
    	for(var i = 0 ; i < record.length; i++){
    		if(i==0){
    			vehicleNumbers = record[i].data.vehicleNumber;
    			vehicleIds = record[i].data.id;
    		}else{
    			vehicleNumbers +=  ',' + record[i].data.vehicleNumber;
    			vehicleIds += ',' + record[i].data.id;
    		}    		
    	}        
        var deptName = select.text;
        this.assignVehicleforDept(vehicleIds,allocateDepId,vehicleNumbers,deptName);
    },

    /*切换车辆管理下的tab页*/
    onActivateforAssignedVehicle: function(me,eOpts){
    	console.log('onActivateforAssignedVehicle');        
        Ext.getCmp('gridAssignedVehicle').AllSelectedAssignedRecords.length = 0; 
        Ext.getCmp('gridAssignedVehicle').getSelectionModel().clearSelections();
    	this.onResetClickforAssignedVehicle();
    	this.onSearchClickforAssignedVehicle();
    },

    onActivateforUnAssignedVehicle: function(me,eOpts){
    	console.log('onActivateforUnAssignedVehicle');
        Ext.getCmp('gridAvailableAssignVehicle').AllSelectedUnAssignedRecords.length = 0; 
        Ext.getCmp('gridAvailableAssignVehicle').getSelectionModel().clearSelections();
    	this.onResetClickforAvailableAssignVehicle();
    	this.onSearchClickforAvailableAssignVehicle();
    },
});

function clickVehicleNumber(me) {
    var vehicleNumber = me.innerHTML;

        Ext.getCmp('showVehicleRemoveErrorWindow').close();

        this.treeItemClassReset();
        var main = Ext.getCmp('main').getController();
        var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();

        $("#vehicleMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
        $(".app-header-toolbar-button-vehicleMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_veclemgmt_pre.png) no-repeat center");
        $("#vehicleMgmtButton-btnInnerEl").css("color", "#fff");

        Ext.getCmp('vehiclemonitoringcontainer').hide();
        Ext.getCmp('maincontainerwrap').show();
        for (var i in navigationTreeListStore.data.items) {
            navigationTreeListStore.data.items[i].collapse();
        }
        navigationTreeListStore.getNodeById('vehicleMgmt').expand();
        Ext.TaskManager.stopAll();        
        window.sessionStorage.setItem("vehicleMgmt", '1');
        window.sessionStorage.setItem("vehicleNumber", vehicleNumber);
        main.redirectTo('VehicleInfoMgmt');
}

function clickOrderList(me) {
    var orderNumber = me.innerHTML;

        Ext.getCmp('showVehicleRemoveErrorWindow').close();

        this.treeItemClassReset();
        var main = Ext.getCmp('main').getController();
        var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();

        $("#orderMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
        $(".app-header-toolbar-button-orderMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_ordermgmt_pre.png) no-repeat center");
        $("#orderMgmtButton-btnInnerEl").css("color", "#fff");

        Ext.getCmp('vehiclemonitoringcontainer').hide();
        Ext.getCmp('maincontainerwrap').show();
        for (var i in navigationTreeListStore.data.items) {
            navigationTreeListStore.data.items[i].collapse();
        }
        navigationTreeListStore.getNodeById('orderMgmt').expand();
        Ext.TaskManager.stopAll();        
        window.sessionStorage.setItem("orderMgmt", '1');
        window.sessionStorage.setItem("orderNumber", orderNumber);
        main.redirectTo('orderlist');
}

function treeItemClassReset() {
    /*首页*/
    $("#mainPageButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
    $(".app-header-toolbar-button-navigationMain").css("background", "url(resources/images/icons/manicons/icon_home_home.png) no-repeat center");
    $("#mainPageButton-btnInnerEl").css("color", "#DDDDDD");

    /*首页监控*/
    $("#vehicleMonitoringMainButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
    $(".app-header-toolbar-button-vehicleMonitoringMain").css("background", "url(resources/images/icons/manicons/icon_home_veclemonitoring.png) no-repeat center");
    $("#vehicleMonitoringMainButton-btnInnerEl").css("color", "#DDDDDD");

    /*车辆监控*/
    $("#vehicleMonitoringButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
    $(".app-header-toolbar-button-vehicleMonitoring").css("background", "url(resources/images/icons/manicons/icon_home_veclemonitoring.png) no-repeat center");
    $("#vehicleMonitoringButton-btnInnerEl").css("color", "#DDDDDD");

    /*订单管理*/
    $("#orderMgmtButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
    $(".app-header-toolbar-button-orderMgmt").css("background", "url(resources/images/icons/manicons/icon_home_ordermgmt.png) no-repeat center");
    $("#orderMgmtButton-btnInnerEl").css("color", "#DDDDDD");

    /*报警管理*/
    $("#alertMgmtButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
    $(".app-header-toolbar-button-alertMgmt").css("background", "url(resources/images/icons/manicons/icon_home_abnormalwarningmgmt.png) no-repeat center");
    $("#alertMgmtButton-btnInnerEl").css("color", "#DDDDDD");

    /*车辆管理*/
    $("#vehicleMgmtButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
    $(".app-header-toolbar-button-vehicleMgmt").css("background", "url(resources/images/icons/manicons/icon_home_veclemgmt.png) no-repeat center");
    $("#vehicleMgmtButton-btnInnerEl").css("color", "#DDDDDD");

    /*站点管理*/
    $("#stationMaintainMgmtButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
    $(".app-header-toolbar-button-stationMaintainMgmt").css("background", "url(resources/images/icons/manicons/icon_home_stationmgmt.png) no-repeat center");
    $("#stationMaintainMgmtButton-btnInnerEl").css("color", "#DDDDDD");

    /*统计报表*/
    $("#reportMgmtButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
    $(".app-header-toolbar-button-reportMgmt").css("background", "url(resources/images/icons/manicons/icon_home_report.png) no-repeat center");
    $("#reportMgmtButton-btnInnerEl").css("color", "#DDDDDD");

    /*组织管理*/
    $("#permissionMgmtButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
    $(".app-header-toolbar-button-permissionMgmt").css("background", "url(resources/images/icons/manicons/icon_home_orgmgmt.png) no-repeat center");
    $("#permissionMgmtButton-btnInnerEl").css("color", "#DDDDDD");

    /*机构管理*/
    $("#organizationMgmtButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
    $(".app-header-toolbar-button-organizationMgmt").css("background", "url(resources/images/icons/manicons/icon_home_orgmgmt.png) no-repeat center");
    $("#organizationMgmtButton-btnInnerEl").css("color", "#DDDDDD");

    /*规则管理*/
    $("#ruleMgmtButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
    $(".app-header-toolbar-button-ruleMgmt").css("background", "url(resources/images/icons/manicons/icon_home_rule_mgmt.png) no-repeat center");
    $("#ruleMgmtButton-btnInnerEl").css("color", "#DDDDDD");
}