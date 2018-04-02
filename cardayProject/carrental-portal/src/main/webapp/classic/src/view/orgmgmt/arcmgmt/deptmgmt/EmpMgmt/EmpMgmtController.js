Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.EmpMgmtController', {
    extend: 'Ext.app.ViewController',
    requires: [
    ],
    loadData : function(){

    },
    /*******加载前执行函数*******/
    onBeforeLoadforAssignedEmp: function () {
        var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
        if(selection.length == 0){
            return;
        }
        var select = selection[0].getData();
        var deptId = select.id;
        console.log('onBeforeLoadforAssignedEmp');
        var frmValues = this.lookupReference('searchFormAssignedEmp').getValues();
        var page=Ext.getCmp('assignedEmpPage').store.currentPage;
        var limit=Ext.getCmp('assignedEmpPage').pageSize;
        frmValues.addemp_role==-1?frmValues.addemp_role="":"";
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
            "organizationId":deptId,
            "realname":frmValues.realname,
            "username":frmValues.username,
            "phone" : frmValues.phone,
            "roleId" : frmValues.addemp_role
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("assignedEmpStore").proxy.extraParams = {
            "json" : pram
        }
    },
    onBeforeLoadforAvailableAssignEmp: function () {
        console.log('onBeforeLoadforAvailableAssignEmp');
        var frmValues = Ext.getCmp('searchFormAvailableAssignEmp').getValues();
        var page=Ext.getCmp('availableAssignEmpPage').store.currentPage;
        var limit=Ext.getCmp('availableAssignEmpPage').pageSize;
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
            "realname":frmValues.realname,
            "username":frmValues.username,
            "phone" : frmValues.phone
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("availableAssignEmpStore").proxy.extraParams = {
            "json" : pram
        }
    },
    //选择员工角色
    selectRoles : function() {
        Ext.Ajax.request({
            url: 'role',
            method : 'POST',
            defaultHeaders : {'Content-type' : 'application/json;utf-8'},
            success : function(response,options) {
                var respText = Ext.util.JSON.decode(response.responseText);
                // var all = '{"id":"","role":"全部"}';
                // respText.data.push(JSON.parse(all));
                var all = {
                    "id":-1,
                    "role":"所有"
                };
                respText.data.splice(0,0,all);
//				var data = respText.data;
//				data=[{"id": 1, "role": "超级管理员"},
//				          {"id": 2, "role": "机构管理员"}
//				];
                console.log("显示员工role list");
                console.log(respText);
                Ext.getCmp("addemp_role").setStore(respText);
                //下面这种loadData的方法不行
                //Ext.getCmp("gridroleid").getStore('rolesResults').loadData(data);
            }
			/*,
			 failure : function() {
			 Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
			 },*/
        });

    },
    /**********点击查询执行***********/
    onSearchClickforAssignedEmp :　function() {
        console.log('onSearchClickforAssignedEmp');
        var EmpStore = this.lookupReference('gridAssignedEmp').getStore();
        EmpStore.currentPage = 1;
        Ext.getCmp('gridAssignedEmp').getViewModel().getStore("assignedEmpStore").load();
    },
    onSearchClickforAvailableAssignEmp :　function() {
        console.log('onSearchClickforAvailableAssignEmp');
        var EmpStore = this.lookupReference('gridAvailableAssignEmp').getStore();
        EmpStore.currentPage = 1;
        Ext.getCmp('gridAvailableAssignEmp').getViewModel().getStore("availableAssignEmpStore").load();
    },
    /************点击重置************/
    onResetClickforAssignedEmp : function() {
        this.lookupReference('searchFormAssignedEmp').getForm().reset();
    },
    onResetClickforAvailableAssignEmp : function() {
        this.lookupReference('searchFormAvailableAssignEmp').getForm().reset();
    },
    /************多选框选择与放弃************/
	/*现有司机的选中识别*/
    checkDriSelectAssigned: function (me, record, index, opts) {
        Ext.getCmp('gridAssignedEmp').AllSelectedAssignedRecords.push(record);
    },
    checkDrideSelectAssigned: function (me, record, index, opts) {
        Ext.getCmp('gridAssignedEmp').AllSelectedAssignedRecords = Ext.Array.filter(Ext.getCmp('gridAssignedEmp').AllSelectedAssignedRecords, function (item) {
            return item.get("id") != record.get("id");
        });
    },
	/*可分配司机的选中识别*/
    checkDriSelectUnAssigned: function (me, record, index, opts) {
        Ext.getCmp('gridAvailableAssignEmp').AllSelectedUnAssignedRecords.push(record);
    },
    checkDrideSelectUnAssigned: function (me, record, index, opts) {
        Ext.getCmp('gridAvailableAssignEmp').AllSelectedUnAssignedRecords = Ext.Array.filter(Ext.getCmp('gridAvailableAssignEmp').AllSelectedUnAssignedRecords, function (item) {
            return item.get("id") != record.get("id");
        });
    },

    /*********切换车辆管理下的tab页*********/
    onActivateforAssignedEmp: function(me,eOpts){
        console.log('onActivateforAssignedEmp');
        Ext.getCmp('gridAssignedEmp').AllSelectedAssignedRecords.length = 0;
        Ext.getCmp('gridAssignedEmp').getSelectionModel().clearSelections();
        this.onResetClickforAssignedEmp();
        this.onSearchClickforAssignedEmp();
    },

    onActivateforUnAssignedEmp: function(me,eOpts){
        console.log('onActivateforUnAssignedEmp');
        Ext.getCmp('gridAvailableAssignEmp').AllSelectedUnAssignedRecords.length = 0;
        Ext.getCmp('gridAvailableAssignEmp').getSelectionModel().clearSelections();
        this.onResetClickforAvailableAssignEmp();
        this.onSearchClickforAvailableAssignEmp();
    },
    /*********打开查看车辆信息窗体*********/
    viewEmpInfo : function(grid, rowIndex, colIndex) {
        console.log('viewEmp+++');
        var rec = grid.getStore().getAt(rowIndex);
        var EmpId = rec.data.id;
        url = 'employee/'+EmpId+'/update';

        var EmpRet = new Ext.data.Model();
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
                var reg=new RegExp("\\.0$");
                if (retStatus == 'success') {
                    var win = Ext.widget("viewEmp");
                    //设置查看附件链接地址
                    EmpRet.data = data;
                    win.down("form").loadRecord(EmpRet);
                    console.log(data);
                    var monthLimitvalue;
                    if(EmpRet.data.monthLimitvalue == -1){
                        monthLimitvalue = '不限';
                    }else{
                        monthLimitvalue =  EmpRet.data.monthLimitvalue + '元/月';
                    }

                    win.down("form").getForm().findField("policeNumber").setValue('000001');
                    win.down("form").getForm().findField("monthLimitvalue").setValue(monthLimitvalue);
                    win.down("form").getForm().findField("orderAu").setValue(data.orderCustomer+data.orderSelf);
                    win.down("form").getForm().findField("orderChannel").setValue(data.orderWeb+data.orderApp);
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
    loadforAssignedEmp: function (me, records, success, opts) {
        if (!success || !records || records.length == 0)
            return;

        //根据全局的选择，初始化选中的列
        var selModel = Ext.getCmp('gridAssignedEmp').getSelectionModel();
        Ext.Array.each(Ext.getCmp('gridAssignedEmp').AllSelectedAssignedRecords, function () {
            for (var i = 0; i < records.length; i++) {
                var record = records[i];
                if (record.get("id") == this.get("id")) {
                    selModel.select(record, true, true);    //选中record，并且保持现有的选择，不触发选中事件
                }
            }
        });
    },
    loadforAvailableAssignEmp: function (me, records, success, opts) {
        if (!success || !records || records.length == 0)
            return;

        //根据全局的选择，初始化选中的列
        var selModel = Ext.getCmp('gridAvailableAssignEmp').getSelectionModel();
        Ext.Array.each(Ext.getCmp('gridAvailableAssignEmp').AllSelectedUnAssignedRecords, function () {
            for (var i = 0; i < records.length; i++) {
                var record = records[i];
                if (record.get("id") == this.get("id")) {
                    selModel.select(record, true, true);    //选中record，并且保持现有的选择，不触发选中事件
                }
            }
        });
    },
    /************单个移除与添加************/
    //移除已分配员工
    removeEmp: function(grid, rowIndex, colIndex) {
        var rec = grid.getStore().getAt(rowIndex);
        var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
        if(selection.length == 0){
            return;
        }
        var select = selection[0].getData();
        var EmpIds = rec.data.id;
        var EmpRealname = rec.data.realname;
        var deptName = select.text;
        this.removeEmpOneAndList(EmpIds, EmpRealname,deptName);
    },
    //分配员工到部门
    addEmp: function(grid, rowIndex, colIndex) {
        var rec = grid.getStore().getAt(rowIndex);
        var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
        if(selection.length == 0){
            return;
        }
        var select = selection[0].getData();
        var EmpIds = rec.data.id;
        var allocateDepId = select.id;
        var EmpRealname = rec.data.realname;
        var deptName = select.text;
        this.addEmpOneAndList(EmpIds,allocateDepId,EmpRealname,deptName);

    },
    /************批量移除与添加************/
    removeEmpList : function(){
        var record = Ext.getCmp('gridAssignedEmp').AllSelectedAssignedRecords;
        var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
        if(selection && selection.length == 0){
            Ext.Msg.alert('提示', '请先选择企业部门！');
            return;
        }else if(record && record.length == 0){
            Ext.Msg.alert('提示', '请先选择要移除的员工！');
            return;
        }
        var select = selection[0].getData();
        var EmpRealname,EmpIds;
        for(var i = 0 ; i < record.length; i++){
            if(i==0){
                EmpRealname = record[i].data.realname;
                EmpIds = record[i].data.id;
            }else{
                EmpRealname += ',' + record[i].data.realname;
                EmpIds += ',' + record[i].data.id ;
            }
        }
        this.removeEmpOneAndList(EmpIds, EmpRealname,select.text);
    },
    addEmpList : function(){
        var record = Ext.getCmp('gridAvailableAssignEmp').AllSelectedUnAssignedRecords;
        var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
        if(selection && selection.length == 0){
            Ext.Msg.alert('提示', '请先选择企业部门！');
            return;
        }else if(record && record.length == 0){
            Ext.Msg.alert('提示', '请先选择要添加的员工！');
            return;
        }
        var select = selection[0].getData();
        var EmpRealname,EmpIds;
        for(var i = 0 ; i < record.length; i++){
            if(i==0){
                EmpRealname = record[i].data.realname;
                EmpIds = record[i].data.id;
            }else{
                EmpRealname +=  ',' + record[i].data.realname;
                EmpIds += ',' + record[i].data.id;
            }
        }
        this.addEmpOneAndList(EmpIds, select.id,EmpRealname,select.text);
    },
    /**********批量与单个移除或分配公用处理事件***********/
    removeEmpOneAndList:function(EmpIds,EmpRealname,deptName){
        var msg = '是否确认将员工 ' + EmpRealname + ' 从 ' +  deptName + ' 移除?';
        console.log('+++unassignEmp+++EmpIds: ' + EmpIds);
        Ext.Msg.confirm('消息提示', msg, function(btn){
            if (btn == 'yes') {
                //调用部门移除接口 start
                var input = {'ids' : EmpIds,};
                var json = Ext.encode(input);
                Ext.Ajax.request({
                    url : 'employee/deleteEmployeeToDep',
                    method : 'POST',
                    params:{ json:json},
                    success : function(response, options) {
                        var respText = Ext.util.JSON.decode(response.responseText);
                        var status = respText.status;
                        var orderList = respText.status.data?respText.status.data.orderList : null;
                        var EmpList = respText.status.data?respText.status.data.EmpList : null;

                        if(status == 'success') {
                            Ext.Msg.alert('消息提示', '移除员工成功！');
                            Ext.getCmp("searchFormAssignedEmp").getForm().reset();
                            var assignEmpStore = Ext.getCmp('assignedEmpPage').store;
                            assignEmpStore.currentPage = 1;
							/*重新加载*/
                            Ext.getCmp("gridAssignedEmp").getStore("assignedEmpStore").load();
							/*清除选中*/
                            Ext.getCmp('gridAssignedEmp').getSelectionModel().clearSelections();
                            Ext.getCmp('gridAssignedEmp').AllSelectedAssignedRecords.length = 0;

                        }else{
                            var win = Ext.widget("showEmpRemoveErrorWindow");

                            for (var i = 0; i < respText.data.length; i++) {
                                if(i==0){
                                    var errorMsg = '请先处理未完成订单！';
                                    var html = '<div class="errorVehcileNum">'+ respText.data[i].titleName +'</div><div class="errorMsg">' + errorMsg +'</div>';
                                    Ext.getCmp("showEmpRemoveErrorWindow").items.items[i].items.items[0].setHtml(html);
                                    if(respText.data[i].orderList != null){
                                        for(var n = 0; n < respText.data[i].orderList.length; n++){
                                            Ext.getCmp("showEmpRemoveErrorWindow").items.items[i].items.items[1].getViewModel().getStore("tasksInfoStore").insert(n,respText.data[i].orderList[n]);
                                        }
                                    }
                                }else{
                                    var item = {
                                        xtype:'confirmEmpRemoveError',
                                    };
                                    Ext.getCmp('showEmpRemoveErrorWindow').add(item);
                                    var errorMsg = '请先处理未完成订单！';
                                    var html = '<div class="errorVehcileNum">'+ respText.data[i].titleName +'</div><div class="errorMsg">' + errorMsg +'</div>';
                                    Ext.getCmp("showEmpRemoveErrorWindow").items.items[i].items.items[0].setHtml(html);
                                    if(respText.data[i].orderList != null){
                                        for(var n = 0; n < respText.data[i].orderList.length; n++){
                                            Ext.getCmp("showEmpRemoveErrorWindow").items.items[i].items.items[1].getViewModel().getStore("tasksInfoStore").insert(n,respText.data[i].orderList[n]);
                                        }
                                    }
                                }
                            } 
                            win.show();
                            win = null;
                            Ext.getCmp("searchFormAssignedEmp").getForm().reset();
                            var assignEmpStore = Ext.getCmp('assignedEmpPage').store;
                            assignEmpStore.currentPage = 1;
                            /*清除选中*/
                            Ext.getCmp('gridAssignedEmp').getSelectionModel().clearSelections();
                            Ext.getCmp('gridAssignedEmp').AllSelectedAssignedRecords.length = 0;
							/*重新加载*/
                            Ext.getCmp("gridAssignedEmp").getStore("assignedEmpStore").load();
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
    addEmpOneAndList:function(EmpIds,allocateDepId,EmpRealname,deptName){
        var msg = '是否确认将员工 ' + EmpRealname + ' 加入到部门 ' +  deptName ;
        console.log('+++unassignEmp+++EmpIds: ' + EmpIds);
        Ext.Msg.confirm('消息提示', msg, function(btn){
            if (btn == 'yes') {
                //调用部门移除接口 start
                var input = {'ids' : EmpIds,'allocateDepId': allocateDepId};
                var json = Ext.encode(input);
                Ext.Ajax.request({
                    url:'employee/setEmployeeToDep',
                    method : 'POST',
                    params:{ json:json},
                    success : function(response, options) {
                        var respText = Ext.util.JSON.decode(response.responseText);
                        var status = respText.status;
                        if(status == 'success') {
                            Ext.Msg.alert('消息提示', '添加员工成功！');
                            var avialiableEmpStore = Ext.getCmp('availableAssignEmpPage').store;
                            avialiableEmpStore.currentPage = 1;
                            //重新加载数据
                            Ext.getCmp("gridAvailableAssignEmp").getStore("assignedEmpStore").load();
							/*清除选中*/
                            Ext.getCmp('gridAvailableAssignEmp').getSelectionModel().clearSelections();
                            Ext.getCmp('gridAvailableAssignEmp').AllSelectedUnAssignedRecords.length = 0;
                        }else{
                            Ext.Msg.alert('消息提示', '添加员工失败！');
                            //重新加载数据
                            Ext.getCmp("gridAvailableAssignEmp").getStore("assignedEmpStore").load();
							/*清除选中*/
                            Ext.getCmp('gridAvailableAssignEmp').getSelectionModel().clearSelections();
                            Ext.getCmp('gridAvailableAssignEmp').AllSelectedUnAssignedRecords.length = 0;
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
function clickVehicleNumberAtEmp(me) {
    var vehicleNumber = me.innerHTML;
    Ext.getCmp('showEmpRemoveErrorWindow').close();

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

function clickOrderListAtEmp(me) {
    var orderNumber = me.innerHTML;
    Ext.getCmp('showEmpRemoveErrorWindow').close();

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