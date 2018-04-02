Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.DriverMgmtController', {
    extend: 'Ext.app.ViewController',
    requires: [
    ],
    loadData : function(){
    	
    },
    /*******加载前执行函数*******/
    onBeforeLoadforAssignedDriver: function () {
    	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
    	if(selection.length == 0){
    		return;
    	}
    	var select = selection[0].getData();
		var deptId = select.id;
		console.log('onBeforeLoadforAssignedDriver');
		var frmValues = this.lookupReference('searchFormAssignedDriver').getValues();
		var page=Ext.getCmp('assignedDriverPage').store.currentPage;
		var limit=Ext.getCmp('assignedDriverPage').pageSize;
		//去除空格
		var realname=frmValues.realname.replace(/\s/g, "");
		var username=frmValues.username.replace(/\s/g, "");
		var phone=frmValues.phone.replace(/\s/g, "");
		
		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"organizationId":deptId,
				"realname":realname,
				"username":username,
				"phone" : phone,
			};
		var pram = Ext.encode(input);
		this.getViewModel().getStore("assignedDriverStore").proxy.extraParams = {
			"json" : pram
		}
	},
    onBeforeLoadforAvailableAssignDriver: function () {
		var frmValues = this.lookupReference('searchFormAvailableAssignDriver').getValues();
		var page=Ext.getCmp('availableAssignDriverPage').store.currentPage;
		var limit=Ext.getCmp('availableAssignDriverPage').pageSize;
		//去除空格
		var realname=frmValues.realname.replace(/\s/g, "");
		var username=frmValues.username.replace(/\s/g, "");
		var phone=frmValues.phone.replace(/\s/g, "");
		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"realname":realname,
				"username":username,
				"phone" : phone,
			};
		var pram = Ext.encode(input);
		this.getViewModel().getStore("availableAssignDriverStore").proxy.extraParams = {
			"json" : pram
		}
	},
	/**********点击查询执行***********/
	onSearchClickforAssignedDriver :　function() {
		console.log('onSearchClickforAssignedDriver');
		var DriverStore = this.lookupReference('gridAssignedDriver').getStore();
		DriverStore.currentPage = 1;
		Ext.getCmp('gridAssignedDriver').getViewModel().getStore("assignedDriverStore").load();
	},
	onSearchClickforAvailableAssignDriver :　function() {
		console.log('onSearchClickforAvailableAssignDriver');
		var DriverStore = this.lookupReference('gridAvailableAssignDriver').getStore();
		DriverStore.currentPage = 1;
		Ext.getCmp('gridAvailableAssignDriver').getViewModel().getStore("availableAssignDriverStore").load();
	},
	/************点击重置************/
	onResetClickforAssignedDriver : function() {
		this.lookupReference('searchFormAssignedDriver').getForm().reset();
	},
	onResetClickforAvailableAssignDriver : function() {
		this.lookupReference('searchFormAvailableAssignDriver').getForm().reset();
	},
	/************多选框选择与放弃************/
	/*现有司机的选中识别*/
    checkDriSelectAssigned: function (me, record, index, opts) {
        Ext.getCmp('gridAssignedDriver').AllSelectedAssignedRecords.push(record);
    },
    checkDrideSelectAssigned: function (me, record, index, opts) {
	    Ext.getCmp('gridAssignedDriver').AllSelectedAssignedRecords = Ext.Array.filter(Ext.getCmp('gridAssignedDriver').AllSelectedAssignedRecords, function (item) {
	        return item.get("id") != record.get("id");
	    });
	},
	/*可分配司机的选中识别*/
    checkDriSelectUnAssigned: function (me, record, index, opts) {
        Ext.getCmp('gridAvailableAssignDriver').AllSelectedUnAssignedRecords.push(record);
    },
    checkDrideSelectUnAssigned: function (me, record, index, opts) {
	    Ext.getCmp('gridAvailableAssignDriver').AllSelectedUnAssignedRecords = Ext.Array.filter(Ext.getCmp('gridAvailableAssignDriver').AllSelectedUnAssignedRecords, function (item) {
	        return item.get("id") != record.get("id");
	    });
	},
	
    /*********切换车辆管理下的tab页*********/
    onActivateforAssignedDriver: function(me,eOpts){
        console.log('onActivateforAssignedDriver');
        Ext.getCmp('gridAssignedDriver').AllSelectedAssignedRecords.length = 0;        
        Ext.getCmp('gridAssignedDriver').getSelectionModel().clearSelections();
        this.onResetClickforAssignedDriver();
        this.onSearchClickforAssignedDriver();
    },

    onActivateforUnAssignedDriver: function(me,eOpts){
        console.log('onActivateforUnAssignedDriver');
        Ext.getCmp('gridAvailableAssignDriver').AllSelectedUnAssignedRecords.length = 0; 
        Ext.getCmp('gridAvailableAssignDriver').getSelectionModel().clearSelections();
        this.onResetClickforAvailableAssignDriver();
        this.onSearchClickforAvailableAssignDriver();
    },
    /*********打开查看车辆信息窗体*********/
    viewDriverInfo : function(grid, rowIndex, colIndex) {
		console.log('viewDriver+++');
		var rec = grid.getStore().getAt(rowIndex);
		var driverId = rec.data.id;
		url = 'driver/'+driverId+'/update';
		
		var driverRet = new Ext.data.Model();
		//根据id查询员工信息
		Ext.Ajax.request({
	   		url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
				var respText = Ext.util.JSON.decode(response.responseText);
				var data = respText.data;
	        	var retStatus = respText.status;
	        	//性别转换
	        	if (data.sex == '0') {
	        		data.sex = '男';
	        	} else if (data.sex == '1') {
	        		data.sex = '女';
	        	}
	        	
	        	// 部门未分配时,显示司机所属的企业名称
	        	if (data.depId == '-1') {
	        		data.depName = data.organizationName;
	        	}
	    		
	        	data.drivingYears = data.drivingYears + '年';
	        	
	        	var reg=new RegExp("\\.0$");
	        	// 转换成带时区时间(IE下需要此过程)
	        	data.birthday = Ext.Date.parseDate(data.birthday.replace(reg,""), 'Y-m-d H:i:s');
	        	data.licenseBegintime = Ext.Date.parseDate(data.licenseBegintime.replace(reg,""), 'Y-m-d H:i:s');
	        	data.licenseExpiretime = Ext.Date.parseDate(data.licenseExpiretime.replace(reg,""), 'Y-m-d H:i:s');
	        	// 时间转换成Y-m-d
	        	data.birthday = Ext.util.Format.date(data.birthday,'Y-m-d');
	        	data.licenseBegintime = Ext.util.Format.date(data.licenseBegintime,'Y-m-d');
	        	data.licenseExpiretime = Ext.util.Format.date(data.licenseExpiretime,'Y-m-d');
	        	
				if (retStatus == 'success') {
					var win = Ext.widget("viewdriver");
					//设置查看附件链接地址
					var licenseAttach = data.licenseAttach;
					if (licenseAttach !=null && licenseAttach !="") {
						url=window.sessionStorage.getItem("imageUrl")+'resources/upload/driver/'+licenseAttach;
					//	url = 'https://uat.car-day.cn/resources/upload/driver/'+licenseAttach;
					//	url = 'http://221.181.100.251:8899/resources/upload/driver/'+licenseAttach;
						var html = "<a href="+url+" target='_blank'>查看驾照附件</a>";
						Ext.getCmp('view_licenseAttach_id').setHtml(html);
						Ext.getCmp('view_licenseAttach_id').setHidden(false);
					} else if (licenseAttach==null || licenseAttach=="") {
						Ext.getCmp('view_licenseAttach_id').setHidden(true);
					}
		        	
					driverRet.data = data;
					win.down("form").loadRecord(driverRet);
					win.show();
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	    });
	},
	/************初始化选择的类************/
	loadforAssignedDriver: function (me, records, success, opts) {
        if (!success || !records || records.length == 0)
            return;

        //根据全局的选择，初始化选中的列
        var selModel = Ext.getCmp('gridAssignedDriver').getSelectionModel();
        Ext.Array.each(Ext.getCmp('gridAssignedDriver').AllSelectedAssignedRecords, function () {
            for (var i = 0; i < records.length; i++) {
                var record = records[i];
                if (record.get("id") == this.get("id")) {
                    selModel.select(record, true, true);    //选中record，并且保持现有的选择，不触发选中事件
                }
            }
        });
    },
	loadforAvailableAssignDriver: function (me, records, success, opts) {
        if (!success || !records || records.length == 0)
            return;

        //根据全局的选择，初始化选中的列
        var selModel = Ext.getCmp('gridAvailableAssignDriver').getSelectionModel();
        Ext.Array.each(Ext.getCmp('gridAvailableAssignDriver').AllSelectedUnAssignedRecords, function () {
            for (var i = 0; i < records.length; i++) {
                var record = records[i];
                if (record.get("id") == this.get("id")) {
                    selModel.select(record, true, true);    //选中record，并且保持现有的选择，不触发选中事件
                }
            }
        });
    },
    /************单个移除与添加************/
    //移除已分配司机
    removeDriver: function(grid, rowIndex, colIndex) {
     	var rec = grid.getStore().getAt(rowIndex);
     	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
    	if(selection.length == 0){
    		return;
    	}
    	var select = selection[0].getData();
        var driverIds = rec.data.id;
        var driverRealname = rec.data.realname;
        var deptName = select.text;
        this.removeDriverOneAndList(driverIds, driverRealname,deptName);
     },
    //分配司机到部门
     addDriver: function(grid, rowIndex, colIndex) {
      	var rec = grid.getStore().getAt(rowIndex);
      	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
     	if(selection.length == 0){
     		return;
     	}
     	var select = selection[0].getData();
         var driverIds = rec.data.id;
         var allocateDepId = select.id;
         var driverRealname = rec.data.realname;
         var deptName = select.text;
         this.addDriverOneAndList(driverIds,allocateDepId,driverRealname,deptName);

      },
	/************批量移除与添加************/
	removeDriverList : function(){
		var record = Ext.getCmp('gridAssignedDriver').AllSelectedAssignedRecords;
     	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
     	if(selection && selection.length == 0){
    		Ext.Msg.alert('提示', '请先选择企业部门！');
    		return;
    	}else if(record && record.length == 0){
    		Ext.Msg.alert('提示', '请先选择要移除的司机！');
    		return;
    	}
    	var select = selection[0].getData();
    	var driverRealname,driverIds;
    	for(var i = 0 ; i < record.length; i++){
    		if(i==0){
    			driverRealname = record[i].data.realname;
    			driverIds = record[i].data.id;
    		}else{
    			driverRealname += ',' + record[i].data.realname;
    			driverIds += ',' + record[i].data.id ;
    		}    		
    	}
    	this.removeDriverOneAndList(driverIds, driverRealname,select.text);
	},
	addDriverList : function(){
		var record = Ext.getCmp('gridAvailableAssignDriver').AllSelectedUnAssignedRecords;
     	var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
    	if(selection && selection.length == 0){
    		Ext.Msg.alert('提示', '请先选择企业部门！');
    		return;
    	}else if(record && record.length == 0){
    		Ext.Msg.alert('提示', '请先选择要添加的司机！');
    		return;
    	}
    	var select = selection[0].getData();
    	var driverRealname,driverIds;
    	for(var i = 0 ; i < record.length; i++){
    		if(i==0){
    			driverRealname = record[i].data.realname;
    			driverIds = record[i].data.id;
    		}else{
    			driverRealname +=  ',' + record[i].data.realname;
    			driverIds += ',' + record[i].data.id;
    		}    		
    	}
    	this.addDriverOneAndList(driverIds, select.id,driverRealname,select.text);
	}, 
	/**********批量与单个移除或分配公用处理事件***********/
	removeDriverOneAndList:function(driverIds,driverRealname,deptName){
		var msg = '是否确认将司机 ' + driverRealname + ' 从 ' +  deptName + ' 移除?';
     	console.log('+++unassignDriver+++driverIds: ' + driverIds);
     	Ext.Msg.confirm('消息提示', msg, function(btn){
     		if (btn == 'yes') {
     			//调用部门移除接口 start
     			var input = {'ids' : driverIds,};
		        var json = Ext.encode(input);
             	Ext.Ajax.request({
         			url : 'driver/deleteDriverToDep',
         			method : 'POST',
				    params:{ json:json},
         			success : function(response, options) {
         				var respText = Ext.util.JSON.decode(response.responseText);
         				var status = respText.status;         		    	
         		    	if(status == 'success') {
         		    		Ext.Msg.alert('消息提示', '移除司机成功！');
         		    		Ext.getCmp("searchFormAssignedDriver").getForm().reset();
					      	var assignDriverStore = Ext.getCmp('assignedDriverPage').store;
							assignDriverStore.currentPage = 1;
                            /*清除选中*/
                            Ext.getCmp('gridAssignedDriver').getSelectionModel().clearSelections();
                            Ext.getCmp('gridAssignedDriver').AllSelectedAssignedRecords.length = 0;
							/*重新加载*/
							Ext.getCmp("gridAssignedDriver").getStore("assignedDriverStore").load();
							
         		    	}else{
                            var win = Ext.widget("showDriverRemoveErrorWindow");
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
                                    Ext.getCmp("showDriverRemoveErrorWindow").items.items[i].items.items[0].setHtml(html);
                                    if(respText.data[i].driverInfo != null){
                                        Ext.getCmp("showDriverRemoveErrorWindow").items.items[i].items.items[1].getViewModel().getStore("driverInfoStore").insert(0,respText.data[i].driverInfo);
                                    }else{
                                        Ext.getCmp("showDriverRemoveErrorWindow").items.items[i].items.items[1].hide();
                                    }

                                    if(respText.data[i].orderList != null){
                                        for(var n = 0; n < respText.data[i].orderList.length; n++){
                                            Ext.getCmp("showDriverRemoveErrorWindow").items.items[i].items.items[2].getViewModel().getStore("tasksInfoStore").insert(n,respText.data[i].orderList[n]);
                                        }
                                    }else{
                                        Ext.getCmp("showDriverRemoveErrorWindow").items.items[i].items.items[2].hide();
                                    }
                                }else{
                                    var item = {
                                        xtype:'confirmDriverRemoveError',
                                    };
                                    Ext.getCmp('showDriverRemoveErrorWindow').add(item);
                                    var errorMsg;
                                    if(respText.data[i].driverInfo != null && respText.data[i].orderList != null){
                                        errorMsg = '请先解绑司机并处理未完成订单！'; 
                                    }else if(respText.data[i].driverInfo == null && respText.data[i].orderList != null){
                                        errorMsg = '请先处理未完成订单！';
                                    }else if(respText.data[i].driverInfo != null && respText.data[i].orderList == null){
                                        errorMsg = '请先解绑司机！';
                                    }
                                    var html = '<div class="errorVehcileNum">'+ respText.data[i].titleName +'</div><div class="errorMsg">' + errorMsg +'</div>';
                                    Ext.getCmp("showDriverRemoveErrorWindow").items.items[i].items.items[0].setHtml(html);
                                    if(respText.data[i].driverInfo != null){
                                        Ext.getCmp("showDriverRemoveErrorWindow").items.items[i].items.items[1].getViewModel().getStore("driverInfoStore").insert(0,respText.data[i].driverInfo);
                                    }else{
                                        Ext.getCmp("showDriverRemoveErrorWindow").items.items[i].items.items[1].hide();
                                    }

                                    if(respText.data[i].orderList != null){
                                        for(var n = 0; n < respText.data[i].orderList.length; n++){
                                            Ext.getCmp("showDriverRemoveErrorWindow").items.items[i].items.items[2].getViewModel().getStore("tasksInfoStore").insert(n,respText.data[i].orderList[n]);
                                        }
                                    }else{
                                        Ext.getCmp("showDriverRemoveErrorWindow").items.items[i].items.items[2].hide();
                                    }
                                }
                            }  
                            win.show();
                            win = null;
         		    		Ext.getCmp("searchFormAssignedDriver").getForm().reset();
					      	var assignDriverStore = Ext.getCmp('assignedDriverPage').store;
							assignDriverStore.currentPage = 1;
                            /*清除选中*/
                            Ext.getCmp('gridAssignedDriver').getSelectionModel().clearSelections();
                            Ext.getCmp('gridAssignedDriver').AllSelectedAssignedRecords.length = 0;
							/*重新加载*/
							Ext.getCmp("gridAssignedDriver").getStore("assignedDriverStore").load();
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
	addDriverOneAndList:function(driverIds,allocateDepId,driverRealname,deptName){
		var msg = '是否确认将司机 ' + driverRealname + ' 加入到部门 ' +  deptName ;
     	console.log('+++unassignDriver+++driverIds: ' + driverIds);
     	Ext.Msg.confirm('消息提示', msg, function(btn){
     		if (btn == 'yes') {
     			//调用部门移除接口 start
     			var input = {'ids' : driverIds,'allocateDepId': allocateDepId};
		        var json = Ext.encode(input);
             	Ext.Ajax.request({
             		url:'driver/setDriverToDep',
         			method : 'POST',
				    params:{ json:json},
         			success : function(response, options) {
         				var respText = Ext.util.JSON.decode(response.responseText);
         				var status = respText.status;
         		    	if(status == 'success') {
         		    		Ext.Msg.alert('消息提示', '添加司机成功！');
         		    		var avialiableDriverStore = Ext.getCmp('availableAssignDriverPage').store;
         			      	avialiableDriverStore.currentPage = 1;
							//重新加载数据
         			      	Ext.getCmp("gridAvailableAssignDriver").getStore("assignedDriverStore").load();
         			      	/*清除选中*/
							Ext.getCmp('gridAvailableAssignDriver').getSelectionModel().clearSelections();
							Ext.getCmp('gridAvailableAssignDriver').AllSelectedUnAssignedRecords.length = 0;
         		    	}else{
         		    		Ext.Msg.alert('消息提示', '添加司机失败！');
							//重新加载数据
         		    		Ext.getCmp("gridAvailableAssignDriver").getStore("assignedDriverStore").load();
         		    		/*清除选中*/
							Ext.getCmp('gridAvailableAssignDriver').getSelectionModel().clearSelections();
							Ext.getCmp('gridAvailableAssignDriver').AllSelectedUnAssignedRecords.length = 0;    		    		
         		    	}
         			},
         			failure : function() {
         				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
         			},
         			scope : this
         		});
     			//调用可分配司机接口 end
     		}
     	});
	},
})


/**********点击跳转事件***********/
function clickVehicleNumberAtDriver(me) {
    var vehicleNumber = me.innerHTML;
    Ext.getCmp('showDriverRemoveErrorWindow').close();

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

function clickOrderListAtDriver(me) {
    var orderNumber = me.innerHTML;
    Ext.getCmp('showDriverRemoveErrorWindow').close();

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