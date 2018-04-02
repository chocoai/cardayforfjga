Ext.define('Admin.view.main.MainController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.main',
    listen: {
        controller: {
            '#': {
                unmatchedroute: 'onRouteChange'
            }
        }
    },

    routes: {
        ':node': 'onRouteChange' //浏览器路径更改,触发onNavigationTreeSelectionChange函数,同时会执行方法onRouteChange,
    },
    /**
     * 当初次登陆系统，因为Applicatioin.js入口文件中有 defaultToken : 'dashboard',并且NavigationTree.js中有 routeId: 'dashboard',
     * 首先会进入dashboard页面，先触发onMainViewRender函数，然后执行onRouteChange函数，最后会执行onNavigationTreeSelectionChange,
     * 当点击tree中的节点时，首先onNavigationTreeSelectionChange会触发，然后浏览器路径变更，然后执行onRouteChange函数。
     *
     * @type 
     */
    lastView: null,

    //获取新的跳转页面信息，干掉原来的页面，显示新的页面
    setCurrentView: function(hashTag) {
        hashTag = (hashTag || '').toLowerCase();

        var me = this,
            refs = me.getReferences();


        var navigationList = refs.navigationTreeList,
            store = navigationList.getStore(),
            mainCard = refs.mainCardPanel,
            mainLayout = mainCard.getLayout();

        var node = store.findNode('routeId', hashTag) ||
            store.findNode('viewType', hashTag),
            view = (node && node.get('viewType')) || 'page404',
            lastView = me.lastView,
            existingItem = mainCard.child('component[routeId=' + hashTag + ']'),
            newView;

        // Kill any previously routed window
        if (lastView && lastView.isWindow) {
            lastView.destroy();
        }

        lastView = mainLayout.getActiveItem();

        if (!existingItem) {
            newView = Ext.create({
                xtype: view,
                routeId: hashTag, // for existingItem search later
                hideMode: 'offsets'
            });
        }

        if (!newView || !newView.isWindow) {
            // !newView means we have an existing view, but if the newView isWindow
            // we don't add it to the card layout.
            if (existingItem) {
                // We don't have a newView, so activate the existing view.
                if (existingItem !== lastView) {
                    mainLayout.setActiveItem(existingItem);
                }
                newView = existingItem;
            } else {
                // newView is set (did not exist already), so add it and make it the
                // activeItem.
                Ext.suspendLayouts();
                mainLayout.setActiveItem(mainCard.add(newView));
                Ext.resumeLayouts(true);
            }
        }

        navigationList.setSelection(node);

        if (newView.isFocusable(true)) {
            newView.focus();
        }
        me.lastView = newView;

        /*首页报警统计跳转时，不一定进入报警的onSearchClick 但是一定会进入此方法*/
        this.clickAlarmInit(hashTag);        
        this.clickVehicleMgmt();
        this.clickOrderMgmt();
    },

    clickAlarmInit: function(hashTag) {
        switch (hashTag) {
            case "overspeedalarm":
                if (window.sessionStorage.getItem("overSpeedAlarm") == '1') {
                    var grid = Ext.getCmp("overSpeedVehicleGrid");
                    if (grid) {
                        Ext.getCmp('overSpeedAlarmSearchForm').getForm().findField('startTime').setValue(new Date());
                        Ext.getCmp('overSpeedAlarmSearchForm').getForm().findField('endTime').setValue(new Date());
                        Ext.getCmp('overSpeedAlarmPage').store.currentPage = 1;
                        Ext.getCmp('overSpeedAlarmPage').pageSize = 10;
                        grid.getStore("OverSpeedResults").load();
                        window.sessionStorage.setItem("overSpeedAlarm", '0');
                    };
                }
                break;
            case "outmarkermgmt":
                if (window.sessionStorage.getItem("outMarkerMgmt") == '1') {
                    var grid = Ext.getCmp("outMarkerVehicleGrid");
                    if (grid) {
                        Ext.getCmp('outMarkerAlarmSearchForm').getForm().findField('startTime').setValue(new Date());
                        Ext.getCmp('outMarkerAlarmSearchForm').getForm().findField('endTime').setValue(new Date());
                        Ext.getCmp('outMarkerPage').store.currentPage = 1;
                        Ext.getCmp('outMarkerPage').pageSize = 10;
                        grid.getStore("outMarkerResults").load();
                        window.sessionStorage.setItem("outMarkerMgmt", '0');
                    };
                }
                break;
        }
    },

    clickVehicleMgmt: function() {
        if (window.sessionStorage.getItem("vehicleMgmt") == '1') {
            var grid = Ext.getCmp("vehicleInfomgmtId");
            if (grid) {
                Ext.getCmp('vehicleinfo_searchform_id').getForm().findField('vehicleNumber').setValue(window.sessionStorage.getItem("vehicleNumber"));
                Ext.getCmp('vehiclePage').store.currentPage = 1;
                Ext.getCmp('vehiclePage').pageSize = 10;
                grid.getStore("VehicleResults").load();
                window.sessionStorage.setItem("vehicleMgmt", '0');
            };
        }else{   
            var grid = Ext.getCmp("vehicleInfomgmtId");
            if (grid) {
                Ext.getCmp('vehicleinfo_searchform_id').getForm().findField('vehicleNumber').setValue('');
                Ext.getCmp('vehiclePage').store.currentPage = 1;
                Ext.getCmp('vehiclePage').pageSize = 10;
                grid.getStore("VehicleResults").load();
            };
        }
    },

    clickOrderMgmt: function() {
        if (window.sessionStorage.getItem("orderMgmt") == '1') {
            var grid = Ext.getCmp("gridorderlist_id");
            if (grid) {
                Ext.getCmp('orderlist_searchForm_id').getForm().findField('orderNo').setValue(window.sessionStorage.getItem("orderNumber"));
                Ext.getCmp('orderlistpage').store.currentPage = 1;
                Ext.getCmp('orderlistpage').pageSize = 20;
                grid.getStore("ordersResults").load();
                window.sessionStorage.setItem("orderMgmt", '0');
            };
        }else{  
            var grid = Ext.getCmp("gridorderlist_id");
            if (grid) {
                Ext.getCmp('orderlist_searchForm_id').getForm().findField('orderNo').setValue('');
                Ext.getCmp('orderlistpage').store.currentPage = 1;
                Ext.getCmp('orderlistpage').pageSize = 20;
                grid.getStore("ordersResults").load();
            };
        }
    },

    splitViewType: function(viewTypeVin) {
        var index = viewTypeVin.lastIndexOf('_');
        var obj = {};

        if (index < 0) {
            obj.viewType = viewTypeVin;
            obj.vehNum = 'AllVehs';
        } else {
            obj.viewType = viewTypeVin.substring(0, index);
            obj.vehNum = viewTypeVin.substring(index + 1);
        }
        return obj;
    },

    setMainView: function(viewTypeVin) {
    	
        var obj = this.splitViewType(viewTypeVin);

        var hashTag = obj.viewType;
        var vehNum = obj.vehNum;

        hashTag = (hashTag || '').toLowerCase();

        var me = this,
            refs = me.getReferences();

        var navigationList = refs.navigationMainPageTree,
            store = navigationList.getStore(),
            mainCard = refs.mainPageCardPanel,
            mainLayout = mainCard.getLayout();

        var node = store.findNode('routeId', hashTag) ||
            store.findNode('viewType', hashTag),
            view = (node && node.get('viewType')) || 'page404',
            lastView = me.lastView,
            existingItem = mainCard.child('component[routeId=' + hashTag + ']'),
            newView;

        // Kill any previously routed window
        if (lastView && lastView.isWindow) {
            lastView.destroy();
        }

        lastView = mainLayout.getActiveItem();
        if (lastView != null) {
            lastView.destroy();
        }

        existingItem = null;
        if (!existingItem) {
            newView = Ext.create({
                xtype: view,
                routeId: hashTag, // for existingItem search later
                hideMode: 'offsets',
                vehNum: vehNum
            });
        }

        if (!newView || !newView.isWindow) {
            // !newView means we have an existing view, but if the newView isWindow
            // we don't add it to the card layout.
            if (existingItem) {
                // We don't have a newView, so activate the existing view.
                if (existingItem !== lastView) {
                    mainLayout.setActiveItem(existingItem);
                }
                newView = existingItem;
            } else {
                // newView is set (did not exist already), so add it and make it the
                // activeItem.
                Ext.suspendLayouts();
                mainLayout.setActiveItem(mainCard.add(newView));
                Ext.resumeLayouts(true);
            }
        }

        if (newView.isFocusable(true)) {
            newView.focus();
        }
        me.lastView = newView;
    },

    splitVehMoniViewType: function(viewTypeVin) {
    	
    	
    	var index = viewTypeVin.lastIndexOf('_');
    	var obj = {};
    	
    	if (index < 0) {
    		obj.viewType = viewTypeVin;
    		obj.vehNum = 'AllVehs';
    	} else {
    		var strs = viewTypeVin.split("_");
    		obj.viewType = strs[0];
    		obj.isVeh = strs[1];
    		if(obj.isVeh == 'AllVehs'){
                //根节点
                obj.orgId  = '';
                obj.vehicleNumber = '';
            }else if(obj.isVeh == 'dept'){
    			//部门id
                obj.orgId  = strs[strs.length - 1];
                obj.vehicleNumber = '';
    			//obj.vehNum = strs.slice(2,strs.length).join('_');
    		}else if(obj.isVeh == 'veh'){
    			//车辆id
                obj.orgId  = '';
                obj.vehicleNumber = strs[2];
/*    			obj.vehNum = strs[2];
    			obj.vehicleId = strs[3];*/
    		}
    	}
    	return obj;
    },
    
    setVehicleMonitoringView: function(viewTypeVin) {
        var obj = this.splitVehMoniViewType(viewTypeVin);

        var hashTag = obj.viewType;
        var orgId = obj.orgId;
        var vehicleNumber = obj.vehicleNumber;

        hashTag = (hashTag || '').toLowerCase();

        var me = this,
            refs = me.getReferences();

        var navigationList = refs.navigationVehicleMonitoringTree,
            store = navigationList.getStore(),
            mainCard = refs.mainPageCardPanel,
            mainLayout = mainCard.getLayout();

        var node = store.findNode('routeId', hashTag) ||
            store.findNode('viewType', hashTag),
            view = (node && node.get('viewType')) || 'page404',
            lastView = me.lastView,
            existingItem = mainCard.child('component[routeId=' + hashTag + ']'),
            newView;

        // Kill any previously routed window
        if (lastView && lastView.isWindow) {
            lastView.destroy();
        }

        lastView = mainLayout.getActiveItem();
        if (lastView != null) {
            lastView.destroy();
        }

        existingItem = null;
        if (!existingItem) {
            newView = Ext.create({
                xtype: view,
                routeId: hashTag, // for existingItem search later
                hideMode: 'offsets',
                orgId: orgId,
                vehicleNumber: vehicleNumber
            });
        }

        if (!newView || !newView.isWindow) {
            // !newView means we have an existing view, but if the newView isWindow
            // we don't add it to the card layout.
            if (existingItem) {
                // We don't have a newView, so activate the existing view.
                if (existingItem !== lastView) {
                    mainLayout.setActiveItem(existingItem);
                }
                newView = existingItem;
            } else {
                // newView is set (did not exist already), so add it and make it the
                // activeItem.
                Ext.suspendLayouts();
                mainLayout.setActiveItem(mainCard.add(newView));
                Ext.resumeLayouts(true);
            }
        }

        if (newView.isFocusable(true)) {
            newView.focus();
        }
        me.lastView = newView;
    },

    onNavigationTreeSelectionChange: function(tree, node) {
    	if (tree.itemId == "navigationMainPageTree") {
            tree.selectItemId = node.data.id;
        }
    	
    	if (tree.itemId == "navigationVehicleMonitoringTree") {
            tree.selectItemId = node.data.id;
        }
        // 
        //      alert('onNavigationTreeSelectionChange');
        //清空地图中的轨迹点
    	/*
        if (tree.itemId == "navigationMainPageTree") {
            if (Ext.getCmp('mainpageView') != undefined) {
                var length = Ext.getCmp('mainpageView').pointArray.length;
                Ext.getCmp('mainpageView').pointArray.splice(0, length);
            }
        }*/
        var to = node && (node.get('routeId') || node.get('viewType'));

        if (to) {
            this.redirectTo(to);
        };
        //Ext.Msg.alert('onTreeItemClick', info.node.data);
    },
    //调整左边栏的大小  无用的方法
    onToggleNavigationSize: function() {
        var me = this,
            refs = me.getReferences(),
            navigationList = refs.navigationTreeList,
            wrapContainer = refs.mainContainerWrap,
            collapsing = !navigationList.getMicro(),
            new_width = collapsing ? 64 : 250;

        if (Ext.isIE9m || !Ext.os.is.Desktop) {
            Ext.suspendLayouts();

            refs.senchaLogo.setWidth(new_width);

            navigationList.setWidth(new_width);
            navigationList.setMicro(collapsing);

            Ext.resumeLayouts(); // do not flush the layout here...

            // No animation for IE9 or lower...
            wrapContainer.layout.animatePolicy = wrapContainer.layout.animate = null;
            wrapContainer.updateLayout(); // ... since this will flush them
        } else {
            if (!collapsing) {
                // If we are leaving micro mode (expanding), we do that first so that the
                // text of the items in the navlist will be revealed by the animation.
                navigationList.setMicro(false);
            }

            // Start this layout first since it does not require a layout
            refs.senchaLogo.animate({ dynamic: true, to: { width: new_width } });

            // Directly adjust the width config and then run the main wrap container layout
            // as the root layout (it and its chidren). This will cause the adjusted size to
            // be flushed to the element and animate to that new size.
            navigationList.width = new_width;
            wrapContainer.updateLayout({ isRoot: true });
            navigationList.el.addCls('nav-tree-animating');

            // We need to switch to micro mode on the navlist *after* the animation (this
            // allows the "sweep" to leave the item text in place until it is no longer
            // visible.
            if (collapsing) {
                navigationList.on({
                    afterlayoutanimation: function() {
                        navigationList.setMicro(true);
                        navigationList.el.removeCls('nav-tree-animating');
                    },
                    single: true
                });
            }
        }
    },

    onMainViewRender: function() {
        //      alert('onMainViewRender------------'+window.location.hash);//首先执行
        Admin.ux.Util.onPrepareMenu();
        //当刷新页面时强制跳转到车辆监控
        this.onRouteChange("vehicleMonitoringMain");
        /*员工进入展示*/
        if(window.sessionStorage.getItem("userType") == '4'){  
            console.log('Display Emp!');            
            Ext.getCmp('maincontainerwrap').show();
            Ext.getCmp('vehiclemonitoringcontainer').hide();

            $("#orderMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
            $(".app-header-toolbar-button-orderMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_ordermgmt_pre.png) no-repeat center");
            $("#orderMgmtButton-btnInnerEl").css("color", "#fff");
        }

        Ext.Ajax.request({
            url: 'driver/getImageUrl',
            method: 'GET',
            timeout: 60000,
            async: false,
            success: function(response) {
                var respText = Ext.util.JSON.decode(response.responseText);
                var retStatus = respText.status;
                if (retStatus == 'success') {
                    window.sessionStorage.setItem("imageUrl", respText.data);
                }
            }
        /*,
            failure: function() {
                Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
            }*/
        });
        var shortname = window.sessionStorage.getItem('shortname');
        if (shortname != null) {
            Ext.getCmp('logoutUrl').setHtml('<a href="./' + shortname + 'logout"><img src="resources/images/icons/icon_quit.png" alt="logout"></a>');
        }

        if(window.sessionStorage.getItem('userType') == 2 || window.sessionStorage.getItem('userType') == 6 || window.sessionStorage.getItem('userType') == 3){
             /*首页监控保养 车险 年检数据*/
            Ext.Ajax.request({
            url: './maintenance/queryVehicleDataCount',
                method: 'POST',
                success: function(response) {
                    var respText = Ext.util.JSON.decode(response.responseText);
                    console.log(JSON.stringify(respText));
                    if (respText.data != null) {
                        var sumCount = respText.data.maintenanceCount + respText.data.insuranceCount + respText.data.inspectionCount;
                        $("#vehMaintainInfo div")[0].innerHTML = sumCount;
                        $("#square th")[0].innerHTML = '保养 ' + respText.data.maintenanceCount;
                        $("#square th")[1].innerHTML = '车险 ' + respText.data.insuranceCount;
                        $("#square th")[2].innerHTML = '年检 ' + respText.data.inspectionCount;
                    }else{
                        Ext.Msg.alert('消息提示', '取得保险数据异常！');
                    }
                }
            /*,
                failure: function() {
                    Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
                }*/
            });   
        }else{
            Ext.getCmp('vehMaintainInfo').hide();
        }

    },

    onRouteChange: function(id) {
        console.log(id);
        //        alert('onRouteChange------'+id);    // onNavigationTreeSelectionChange 执行后再执行
        if (id.indexOf('admindashboard') == 0 || id == 'dashboard') {
            console.log(' admindashboard 123');
            this.setMainView(id);
        }else if (id.indexOf('vehicleMonitoringMain') == 0 || id == 'vehicleMonitoringMain') {
            if(window.sessionStorage.getItem("userType") == '4'){
                console.log('Emp first');
                var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();
                navigationTreeListStore.getNodeById('orderMgmt').expand();
                var firstChildItemId = navigationTreeListStore.getNodeById('orderMgmt').firstChild.data.viewType;  
                this.setCurrentView(firstChildItemId);                
            }else{
                console.log('vehicleMonitoringMain first');
                this.setVehicleMonitoringView(id);
            }
        }  else {
            console.log('CurrentView first');
            this.setCurrentView(id);
        }
    },

    /*  onSearchRouteChange: function () {
          this.setCurrentView('searchresults');
      },

      onSwitchToModern: function () {
          Ext.Msg.confirm('Switch to Modern', 'Are you sure you want to switch toolkits?',
                          this.onSwitchToModernConfirmed, this);
      },*/

    /*   onSwitchToModernConfirmed: function (choice) {
           if (choice === 'yes') {
               var s = location.search;

               // Strip "?classic" or "&classic" with optionally more "&foo" tokens
               // following and ensure we don't start with "?".
               s = s.replace(/(^\?|&)classic($|&)/, '').replace(/^\?/, '');

               // Add "?modern&" before the remaining tokens and strip & if there are
               // none.
               location.search = ('?modern&' + s).replace(/&$/, '');
           }
       },

       onEmailRouteChange: function () {
           this.setCurrentView('email');
       },*/

    treeItemExpand: function(treeStore) {
        for (var i in treeStore.data.items) {
            treeStore.data.items[i].collapse();
        }
    },

    treeItemClassReset: function() {
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

        /*购置管理*/
        $("#vehiclesPurchaseButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
        $(".app-header-toolbar-button-vehiclesPurchase").css("background", "url(resources/images/icons/manicons/icon_vehicles_purchase.png) no-repeat center");
        $("#vehiclesPurchaseButton-btnInnerEl").css("color", "#DDDDDD");

        /*号牌管理*/
        $("#carNumberButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
        $(".app-header-toolbar-button-carNumber").css("background", "url(resources/images/icons/manicons/icon_car_number.png) no-repeat center");
        $("#carNumberButton-btnInnerEl").css("color", "#DDDDDD");

        /*三定一统*/
        $("#vehicleMixButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
        $(".app-header-toolbar-button-vehicleMix").css("background", "url(resources/images/icons/manicons/icon_vehicle_mgmt.png) no-repeat center");
        $("#vehicleMixButton-btnInnerEl").css("color", "#DDDDDD");

        /*流程管理*/
        $("#systemConfigurationButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
        $(".app-header-toolbar-button-systemConfiguration").css("background", "url(resources/images/icons/manicons/icon_setting.png) no-repeat center");
        $("#systemConfigurationButton-btnInnerEl").css("color", "#DDDDDD");
    },
    
    //关闭所有首页弹框
    closeMainVehMoniWins: function(){
    	if(Ext.getCmp('viewVehicleInfo') != null){
    		Ext.getCmp('viewVehicleInfo').close();
        }
    	
    	if(Ext.getCmp('viewVehicleCountInfo') != null){
    		Ext.getCmp('viewVehicleCountInfo').close();
        }
    	
    	if(Ext.getCmp('gridAlarmStatisticsWindow') != null){
    		Ext.getCmp('gridAlarmStatisticsWindow').close();
        }
    	
    	//如果有历史轨迹或今日轨迹，关掉
    	var win = Ext.getCmp("vehMoniHistoryTrace");
    	if(win != null){
    		win.closeNotRefreshMap();
    	}
    },
    
    /*顶部导航栏点击影响左侧导航栏收缩和伸展*/
    navigationMainClick: function(button, e) {
    	this.closeMainVehMoniWins();
        var buttonId = button.id;
        var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore(); //拿到目录导航列表
        //var navigationMainPageTreeStore = Ext.getCmp('navigationMainPageTree').getStore(); //        
        var navigationVehicleMonitoringTreeStore = Ext.getCmp('navigationVehicleMonitoringTree').getStore(); //拿到首页监控车辆tree
        var main = Ext.getCmp('main').getController();
        switch (buttonId) {
            case "mainPageButton":
                //    Ext.TaskManager.stopAll();

                this.treeItemClassReset();
                $("#mainPageButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-navigationMain").css("background", "url(resources/images/icons/manicons/icon_nav_home_pre.png) no-repeat center");
                $("#mainPageButton-btnInnerEl").css("color", "#fff");

                Ext.getCmp('mainpagecontainer').show();
                Ext.getCmp('maincontainerwrap').hide();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                var mainNode = navigationMainPageTreeStore.getNodeById('dashboard'); //store 中的配置routeId
                for (var i in mainNode.childNodes) {
                    mainNode.childNodes[i].collapse();
                }
                mainNode.expand();
            	Ext.getCmp('navigationMainPageTree').setSelection(mainNode);
                main.redirectTo("admindashboard_AllVehs"); //viewtype配置
                break;
            case "vehicleMonitoringMainButton":
            	//保持左侧搜索栏车辆更新
            	$("#searchVehicleText-inputEl").val("");//清空搜索框
            	//跳转至首页
            	Ext.TaskManager.stopAll();

                this.treeItemClassReset();
                $("#vehicleMonitoringMainButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-vehicleMonitoringMain").css("background", "url(resources/images/icons/manicons/icon_nav_report_pre.png) no-repeat center");
                $("#vehicleMonitoringMainButton-btnInnerEl").css("color", "#fff");

                //Ext.getCmp('mainpagecontainer').hide();
                Ext.getCmp('maincontainerwrap').hide();
                Ext.getCmp('vehiclemonitoringcontainer').show();
                //更新树回调加载右边view
            	this.onSearchClick(null,null,null,function(){
            		var mainNode = navigationVehicleMonitoringTreeStore.getNodeById('vehicleMonitoringMain'); //store 中的配置routeId
                    for (var i in mainNode.childNodes) {
                        mainNode.childNodes[i].collapse();
                    }
                    mainNode.expand();
                    Ext.getCmp('navigationVehicleMonitoringTree').setSelection(mainNode);
                    main.redirectTo("vehicleMonitoringMain_AllVehs"); //viewtype配置
                    
            	});
                
                break;
            case "vehicleMonitoringButton":
                Ext.TaskManager.stopAll();

                this.treeItemClassReset();
                $("#vehicleMonitoringButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-vehicleMonitoring").css("background", "url(resources/images/icons/manicons/icon_nav_report_pre.png) no-repeat center");
                $("#vehicleMonitoringButton-btnInnerEl").css("color", "#fff");

                //Ext.getCmp('mainpagecontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                this.treeItemExpand(navigationTreeListStore);
                navigationTreeListStore.getNodeById('vehicleMonitoring').expand();
                var firstChildItemId = navigationTreeListStore.getNodeById('vehicleMonitoring').firstChild.data.viewType;
                main.redirectTo(firstChildItemId);
                break;
            case "orderMgmtButton":
                Ext.TaskManager.stopAll();

                this.treeItemClassReset();
                $("#orderMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-orderMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_ordermgmt_pre.png) no-repeat center");
                $("#orderMgmtButton-btnInnerEl").css("color", "#fff");

                //Ext.getCmp('mainpagecontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                this.treeItemExpand(navigationTreeListStore);
                navigationTreeListStore.getNodeById('orderMgmt').expand();
                var firstChildItemId = navigationTreeListStore.getNodeById('orderMgmt').firstChild.data.viewType;
                main.redirectTo(firstChildItemId);
                break;
            case "alertMgmtButton":
                Ext.TaskManager.stopAll();
                /*导航class样式*/
                this.treeItemClassReset();
                $("#alertMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-alertMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_abnormalwarningmgmt_pre.png) no-repeat center");
                $("#alertMgmtButton-btnInnerEl").css("color", "#fff");

                //Ext.getCmp('mainpagecontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                this.treeItemExpand(navigationTreeListStore);
                navigationTreeListStore.getNodeById('alertMgmt').expand();
                var firstChildItemId = navigationTreeListStore.getNodeById('alertMgmt').firstChild.data.viewType;
                main.redirectTo(firstChildItemId);
                switch (firstChildItemId) {
                    case "overSpeedAlarm":
                        var grid = Ext.getCmp("overSpeedVehicleGrid");
                        if (grid) {
                            Ext.getCmp('overSpeedAlarmSearchForm').getForm().findField('startTime').setValue('');
                            Ext.getCmp('overSpeedAlarmSearchForm').getForm().findField('endTime').setValue(new Date());
                            Ext.getCmp('overSpeedAlarmPage').store.currentPage = 1;
                            Ext.getCmp('overSpeedAlarmPage').pageSize = 10;
                            grid.getStore("OverSpeedResults").load();
                        };
                        break;
                    case "outMarkerMgmt":
                        var grid = Ext.getCmp("outMarkerVehicleGrid");
                        if (grid) {
                            Ext.getCmp('outMarkerAlarmSearchForm').getForm().findField('startTime').setValue('');
                            Ext.getCmp('outMarkerAlarmSearchForm').getForm().findField('endTime').setValue(new Date());
                            Ext.getCmp('outMarkerPage').store.currentPage = 1;
                            Ext.getCmp('outMarkerPage').pageSize = 10;
                            grid.getStore("outMarkerResults").load();
                        };
                        break;
                    case "backStationMgmt":
                        var grid = Ext.getCmp("backStationVehicleGrid");
                        if (grid) {
                            Ext.getCmp('backStationAlarmSearchForm').getForm().findField('startTime').setValue('');
                            Ext.getCmp('backStationAlarmSearchForm').getForm().findField('endTime').setValue('');
                            Ext.getCmp('backStationPage').store.currentPage = 1;
                            Ext.getCmp('backStationPage').pageSize = 10;
                            grid.getStore("backStationResults").load();
                        };
                        break;
                }
                break;
            case "vehicleMgmtButton":
                Ext.TaskManager.stopAll();

                this.treeItemClassReset();
                $("#vehicleMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-vehicleMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_veclemgmt_pre.png) no-repeat center");
                $("#vehicleMgmtButton-btnInnerEl").css("color", "#fff");

                //Ext.getCmp('mainpagecontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                this.treeItemExpand(navigationTreeListStore);
                navigationTreeListStore.getNodeById('vehicleMgmt').expand();
                var firstChildItemId = navigationTreeListStore.getNodeById('vehicleMgmt').firstChild.data.viewType;
                main.redirectTo(firstChildItemId);
                break;
            case "stationMaintainMgmtButton":
                Ext.TaskManager.stopAll();

                this.treeItemClassReset();
                $("#stationMaintainMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-stationMaintainMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_station_pre.png) no-repeat center");
                $("#stationMaintainMgmtButton-btnInnerEl").css("color", "#fff");

                //Ext.getCmp('mainpagecontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                this.treeItemExpand(navigationTreeListStore);
                navigationTreeListStore.getNodeById('stationMaintainMgmt').expand();
                var firstChildItemId = navigationTreeListStore.getNodeById('stationMaintainMgmt').firstChild.data.viewType;
                main.redirectTo(firstChildItemId);
                break;
            case "reportMgmtButton":
                Ext.TaskManager.stopAll();

                this.treeItemClassReset();
                $("#reportMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-reportMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_veclemonitoring_pre.png) no-repeat center");
                $("#reportMgmtButton-btnInnerEl").css("color", "#fff");

                //Ext.getCmp('mainpagecontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                this.treeItemExpand(navigationTreeListStore);
                navigationTreeListStore.getNodeById('reportMgmt').expand();
                var firstChildItemId = navigationTreeListStore.getNodeById('reportMgmt').firstChild.data.viewType;
                main.redirectTo(firstChildItemId);
                break;
            case "permissionMgmtButton":
                Ext.TaskManager.stopAll();

                this.treeItemClassReset();
                $("#permissionMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-permissionMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_orgmgmt_pre.png) no-repeat center");
                $("#permissionMgmtButton-btnInnerEl").css("color", "#fff");

                //Ext.getCmp('mainpagecontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                this.treeItemExpand(navigationTreeListStore);
                navigationTreeListStore.getNodeById('permissionMgmt').expand();
                var firstChildItemId = navigationTreeListStore.getNodeById('permissionMgmt').firstChild.data.viewType;
                main.redirectTo(firstChildItemId);
                break;
            case "organizationMgmtButton":
                Ext.TaskManager.stopAll();

                this.treeItemClassReset();
                $("#organizationMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-organizationMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_orgmgmt_pre.png) no-repeat center");
                $("#organizationMgmtButton-btnInnerEl").css("color", "#fff");

                //Ext.getCmp('mainpagecontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                this.treeItemExpand(navigationTreeListStore);
                navigationTreeListStore.getNodeById('organizationMgmt').expand();
                var firstChildItemId = navigationTreeListStore.getNodeById('organizationMgmt').firstChild.data.viewType;
                main.redirectTo(firstChildItemId);
                break;
            case "ruleMgmtButton":
                Ext.TaskManager.stopAll();

                this.treeItemClassReset();
                $("#ruleMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-ruleMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_rule_mgmt_pre.png) no-repeat center");
                $("#ruleMgmtButton-btnInnerEl").css("color", "#fff");

                //Ext.getCmp('mainpagecontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                this.treeItemExpand(navigationTreeListStore);
                navigationTreeListStore.getNodeById('ruleMgmt').expand();
                var firstChildItemId = navigationTreeListStore.getNodeById('ruleMgmt').firstChild.data.viewType;
                main.redirectTo(firstChildItemId);
                break;
            case "vehiclesPurchaseButton":
                Ext.TaskManager.stopAll();

                this.treeItemClassReset();
                $("#vehiclesPurchaseButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-vehiclesPurchase").css("background", "url(resources/images/icons/manicons/icon_vehicles_purchase_pre.png) no-repeat center");
                $("#vehiclesPurchaseButton-btnInnerEl").css("color", "#fff");

                //Ext.getCmp('mainpagecontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                this.treeItemExpand(navigationTreeListStore);
                navigationTreeListStore.getNodeById('vehiclesPurchaseMgmt').expand();
                var firstChildItemId = navigationTreeListStore.getNodeById('vehiclesPurchaseMgmt').firstChild.data.viewType;
                main.redirectTo(firstChildItemId);
                break;
            case "carNumberButton":
                Ext.TaskManager.stopAll();

                this.treeItemClassReset();
                $("#carNumberButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-carNumber").css("background", "url(resources/images/icons/manicons/icon_car_number_pre.png) no-repeat center");
                $("#carNumberButton-btnInnerEl").css("color", "#fff");

                //Ext.getCmp('mainpagecontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                this.treeItemExpand(navigationTreeListStore);
                navigationTreeListStore.getNodeById('carNumberMgmt').expand();
                var firstChildItemId = navigationTreeListStore.getNodeById('carNumberMgmt').firstChild.data.viewType;
                main.redirectTo(firstChildItemId);
                break;
            case "vehicleMixButton":
                Ext.TaskManager.stopAll();

                this.treeItemClassReset();
                $("#vehicleMixButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-vehicleMix").css("background", "url(resources/images/icons/manicons/icon_vehicle_pre.png) no-repeat center");
                $("#vehicleMixButton-btnInnerEl").css("color", "#fff");

                //Ext.getCmp('mainpagecontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                this.treeItemExpand(navigationTreeListStore);
                navigationTreeListStore.getNodeById('vehicleMixFun').expand();
                var firstChildItemId = navigationTreeListStore.getNodeById('vehicleMixFun').firstChild.data.viewType;
                main.redirectTo(firstChildItemId);
                break;
            case "systemConfigurationButton":
                Ext.TaskManager.stopAll();

                this.treeItemClassReset();
                $("#systemConfigurationButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-systemConfiguration").css("background", "url(resources/images/icons/manicons/icon_setting_pre.png) no-repeat center");
                $("#systemConfigurationButton-btnInnerEl").css("color", "#fff");

                //Ext.getCmp('mainpagecontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                this.treeItemExpand(navigationTreeListStore);
                navigationTreeListStore.getNodeById('systemConfiguration').expand();
                var firstChildItemId = navigationTreeListStore.getNodeById('systemConfiguration').firstChild.data.viewType;
                main.redirectTo(firstChildItemId);
                break;
        }
    },
  //根据车牌号模糊查询
    onSearchClick : function(sender, info, eOpts,cb){ 
    	var searchText = $("#searchVehicleText-inputEl").val().trim().toUpperCase();
    	Ext.getCmp("navigationVehicleMonitoringTree").setStore(
    		new Admin.store.NavigationVehicleMonitoringTree({
    			proxy : {  
    				type: 'ajax', 
    				actionMethods: {
    		            create : 'POST',
    		            read   : 'POST', // by default GET
    		            update : 'POST',
    		            destroy: 'POST'
    		        },
			  		url: 'vehicle/listVehMoniStatusTreeData',
					rootProperty: 'children',
//					params:{vehicleNumber:searchText}
					extraParams:{"vehicleNumber":searchText},
					cb:cb
    	        }  
    		})
    	)
    },
    //前端筛选方法
    treeFilter:function(){
    	this.filterByText=function(text) {
            this.filterBy(text, 'text');
        }
        this.filterBy=function(text, by) {
        	
            this.clearFilter();
            
            //var view = this.getView(),
                me = this,
                nodesAndParents = [];
     
            // 找到匹配的节点并展开.
            // 添加匹配的节点和他们的父节点到nodesAndParents数组.
                this.getStore().getRoot().cascadeBy(function(tree) {
                var currNode = this;
                if(currNode.get("text").toString().indexOf("span")>-1){
                	if ($(currNode.get("text"))[0].innerHTML.toLowerCase().indexOf(text.toLowerCase()) > -1) {
	                    //me.expandPath(currNode.getPath());
	                	this.expand();
	                    while (currNode.parentNode) {
	                        nodesAndParents.push(currNode.id);
	                        currNode = currNode.parentNode;
	                    }
	                }
                }
                
            }, null, [me]);
     
            // 将不在nodesAndParents数组中的节点隐藏
            this.getStore().getRoot().cascadeBy(function(tree) {
                //var uiNode = view.getNodeByRecord(this);
            	var uiNode = $("#"+tree.getItem(this,this.parentNode).id)[0];
            	//排除根节点
            	if(this.get("text").toString().indexOf("span")>-1){
            		if (uiNode && !Ext.Array.contains(nodesAndParents, this.id)) {
                        Ext.get(uiNode).setDisplayed('none');
                    }
            	}
            }, null, [me]);
        };
        this.clearFilter=function() {
            this.getStore().getRoot().cascadeBy(function(tree) {
            	//通过ext.tabel继承的方法getRootNode()方法去拿uinode(this=>tree)
            	//x-treelist-item-expanded || x-treelist-row
                //var uiNode = view.getNodeByRecord(this);
            	var uiNode = $("#"+tree.getItem(this,this.parentNode).id)[0];
            	
                if (uiNode) {
                    //Ext.get(uiNode).setDisplayed('table-row');
                    Ext.get(uiNode).setDisplayed('block');
                    this.expand();
                }
            }, null, [this]);
        }
    },
    //回车关键字搜索树
    specialkey:function(field, e) {  
        if (e.getKey() == Ext.EventObject.ENTER) {
        	this.onSearchClick();
        }  
    },
    //input变化时搜索树
    searchByTextChange:function(){
    	/*var searchText = $("#searchVehicleText-inputEl").val().trim();
    	Object.assign(Ext.getCmp("navigationVehicleMonitoringTree"),new this.treeFilter());
    	Ext.getCmp("navigationVehicleMonitoringTree").filterByText(searchText);
    */},
    onTreeItemClick: function(sender, info, eOpts) {
    	//关闭首页监控所有之前打开的信息窗口
    	//轨迹
    	var his = Ext.getCmp("vehMoniHistoryTrace");
    	if(his != null){
    		his.closeNotRefreshMap();
    	}
    	//车辆信息
    	var veh = Ext.getCmp("viewVehicleInfo");
    	if(veh != null){
    		veh.close();
    	}
    	
    	//车辆统计
    	var vehCnt = Ext.getCmp("viewVehicleCountInfo");
    	if(vehCnt != null){
    		vehCnt.close();
    	}
    	
    	//报警统计
    	var alarmCnt = Ext.getCmp("gridAlarmStatisticsWindow");
    	if(alarmCnt != null){
    		alarmCnt.close();
    	}
    	
    	
        var itemNode = info.node;
        var treeStore = info.tree.getStore();
        /*左侧导航栏排他性收缩和伸展*/
        if (itemNode.hasChildNodes()) {

            var node = treeStore.getNodeById(itemNode.id);
            if (node.id == "dashboard") {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.childNodes[i].collapse();
                }
            }else if (node.id == "vehicleMonitoring") {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.childNodes[i].collapse();
                }
            } else {
                var childNodes = node.parentNode.childNodes;
                for (var i = 0; i < childNodes.length; i++) {
                    if (childNodes[i].data.id != node.data.id) {
                        childNodes[i].collapse();
                    }
                }
            }
        }

        if (itemNode.parentNode != null) {
            var itemId = itemNode.parentNode.data.itemId;
            switch (itemId) {
                case 'admindashboard':
                    this.treeItemClassReset();
                    $("#mainPageButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-navigationMain").css("background", "url(resources/images/icons/manicons/icon_nav_home_pre.png) no-repeat center");
                    $("#mainPageButton-btnInnerEl").css("color", "#fff");
                    break;
                case 'vehicleMonitoring':
                    this.treeItemClassReset();
                    $("#vehicleMonitoringButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-vehicleMonitoring").css("background", "url(resources/images/icons/manicons/icon_nav_report_pre.png) no-repeat center");
                    $("#vehicleMonitoringButton-btnInnerEl").css("color", "#fff");
                    break;
                case "orderMgmt":

                    this.treeItemClassReset();
                    $("#orderMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-orderMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_ordermgmt_pre.png) no-repeat center");
                    $("#orderMgmtButton-btnInnerEl").css("color", "#fff");
                    break;
                case "alertMgmt":
                    /*导航class样式*/
                    this.treeItemClassReset();
                    $("#alertMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-alertMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_abnormalwarningmgmt_pre.png) no-repeat center");
                    $("#alertMgmtButton-btnInnerEl").css("color", "#fff");
                    break;
                case "vehicleMgmt":
                    this.treeItemClassReset();
                    $("#vehicleMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-vehicleMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_veclemgmt_pre.png) no-repeat center");
                    $("#vehicleMgmtButton-btnInnerEl").css("color", "#fff");
                    break;
                case "stationMaintainMgmt":
                    this.treeItemClassReset();
                    $("#stationMaintainMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-stationMaintainMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_station_pre.png) no-repeat center");
                    $("#stationMaintainMgmtButton-btnInnerEl").css("color", "#fff");
                    break;
                case "reportMgmt":
                    this.treeItemClassReset();
                    $("#reportMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-reportMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_veclemonitoring_pre.png) no-repeat center");
                    $("#reportMgmtButton-btnInnerEl").css("color", "#fff");
                    break;
                case "permissionMgmt":
                    this.treeItemClassReset();
                    $("#permissionMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-permissionMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_orgmgmt_pre.png) no-repeat center");
                    $("#permissionMgmtButton-btnInnerEl").css("color", "#fff");
                    break;
                case "organizationMgmt":
                    this.treeItemClassReset();
                    $("#organizationMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-organizationMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_orgmgmt_pre.png) no-repeat center");
                    $("#organizationMgmtButton-btnInnerEl").css("color", "#fff");
                    break;
                case "ruleMgmt":
                    this.treeItemClassReset();
                    $("#ruleMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-ruleMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_rule_mgmt_pre.png) no-repeat center");
                    $("#ruleMgmtButton-btnInnerEl").css("color", "#fff");
                    break;
                case "vehiclesPurchaseMgmt":
                    this.treeItemClassReset();
                    $("#vehiclesPurchaseButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-vehiclesPurchase").css("background", "url(resources/images/icons/manicons/icon_vehicles_purchase_pre.png) no-repeat center");
                    $("#vehiclesPurchaseButton-btnInnerEl").css("color", "#fff");
                    break;
                case "carNumberMgmt":
                    this.treeItemClassReset();
                    $("#carNumberButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-carNumber").css("background", "url(resources/images/icons/manicons/icon_car_number_pre.png) no-repeat center");
                    $("#carNumberButton-btnInnerEl").css("color", "#fff");
                    break;
                case "vehicleMixFun":
                    this.treeItemClassReset();
                    $("#vehicleMixButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-vehicleMix").css("background", "url(resources/images/icons/manicons/icon_vehicle_pre.png) no-repeat center");
                    $("#vehicleMixButton-btnInnerEl").css("color", "#fff");
                    break;
                case "systemConfiguration":
                    this.treeItemClassReset();
                    $("#systemConfigurationButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-systemConfiguration").css("background", "url(resources/images/icons/manicons/icon_setting_pre.png) no-repeat center");
                    $("#systemConfigurationButton-btnInnerEl").css("color", "#fff");
                    break;
                default:
                    break;
            }

            if (itemId == null) {
                var viewType = itemNode.data.viewType;
                if (viewType == "admindashboard") {
                    this.treeItemClassReset();
                    $("#mainPageButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-navigationMain").css("background", "url(resources/images/icons/manicons/icon_nav_home_pre.png) no-repeat center");
                    $("#mainPageButton-btnInnerEl").css("color", "#fff");
                }
            }
        }
        var viewType = info.node.data.viewType;
        //判断viewType,刷新相应的页面
        var main = Ext.getCmp('main').getController();
        switch (viewType) {
            case "admindashboard":
                Ext.getCmp('mainpagecontainer').show();
                Ext.getCmp('maincontainerwrap').hide();
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                main.redirectTo("admindashboard");
                break;
            case "vehicleMonitoringMain":
            	Ext.getCmp('mainpagecontainer').hide();
            	Ext.getCmp('maincontainerwrap').hide();
            	Ext.getCmp('vehiclemonitoringcontainer').show();
            	main.redirectTo("vehicleMonitoringMain");
            	break;
            case "RentInfoMgmt":
                var grid = Ext.getCmp("rentId");
                if (grid) {
                    Ext.Ajax.request({
                        url: 'rent/list',
                        method: 'GET',
                        headers: { 'Content-type': 'application/json;utf-8' },
                        success: function(response, options) {
                            var respText = Ext.util.JSON.decode(response.responseText);
                            var data = respText.data;
                            grid.getStore('rentsResults').loadData(data);
                        }
                        /*,
                        failure: function() {
                            Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
                        }*/
                    });
                };
                break;
            case "Enterinfoxx":
                var grid = Ext.getCmp("enterId");
                if (grid) {
                    Ext.Ajax.request({
                        url: 'organization/list',
                        method: 'GET',
                        headers: { 'Content-type': 'application/json;utf-8' },
                        success: function(response, options) {
                            var respText = Ext.util.JSON.decode(response.responseText);
                            var data = respText.data;
                            grid.getStore('usersResults').loadData(data);
                        }
                        /*,
                        failure: function() {
                            Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
                        }*/
                    });
                };
                break;
            case "EnterinfoAudit":

                /*var input={
                    "status":'0'
                };*/
                var grid = Ext.getCmp("auditId");
                if (grid) {
                    Ext.Ajax.request({
                        url: 'organization/audit/list',
                        method: 'GET',
                        headers: { 'Content-type': 'application/json;utf-8' },
                        success: function(response, options) {
                            var respText = Ext.util.JSON.decode(response.responseText);
                            var data = respText.data;
                            grid.getStore('auditResults').loadData(data);
                        }
                        /*,
                        failure: function() {
                            Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
                        }*/
                    });
                };
                break;
            case "VehicleInfoMgmt":
                var grid = Ext.getCmp("vehicleInfomgmtId");
                if (grid) {
                	Ext.getCmp('vehicleinfo_searchform_id').reset();
                	grid.getStore("VehicleResults").load();
                	Ext.getCmp('vehicleinfo_searchform_id').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('vehicleinfo_searchform_id').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
                }
                break;
            case 'usermgmt':
                var grid = Ext.getCmp("griduserid");
                if (grid) {
                	Ext.getCmp('user_searchfomr_id').reset();
                	Ext.getCmp('user_searchfomr_id').getForm().findField('organizationId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('user_searchfomr_id').getForm().findField('organizationName').setValue(window.sessionStorage.getItem("organizationName"));
                	grid.getStore("usersResults").load();
                }
                break;
            case 'rolemgmt':
                var grid = Ext.getCmp("gridroleid");
                if (grid) {
                	Ext.getCmp('role_searchform_id').reset();
                    grid.getStore("rolesResults").load();
                }
                break;
            case 'empmgmt':
                var grid = Ext.getCmp("gridempid");
                if (grid) {
                	Ext.getCmp('emp_searchform_id').reset();
                	//重置后把部门设为当前部门
                	Ext.getCmp('emp_searchform_id').getForm().findField('organizationId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('emp_searchform_id').getForm().findField('organizationName').setValue(window.sessionStorage.getItem("organizationName"));
                    grid.getStore("empsResults").load();
                }
                break;
            case 'drivermgmt':
                var grid = Ext.getCmp("griddriverid");
                if (grid) {
                	Ext.getCmp('driver_searchform_id').reset();
                	//重置后把部门设为当前部门
                	Ext.getCmp('driver_searchform_id').getForm().findField('organizationId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('driver_searchform_id').getForm().findField('organizationName').setValue(window.sessionStorage.getItem("organizationName"));
                    grid.getStore("driversResults").load();
                }
                break;
            case 'orderlist':
            	//点击左边菜单栏，保留查询条件
            	 var grid = Ext.getCmp("gridorderlist_id");
                 if (grid) {
                	 Ext.getCmp('orderlist_searchForm_id').reset();
                     grid.getStore("ordersResults").load();
                     Ext.getCmp('orderlist_searchForm_id').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
                     Ext.getCmp('orderlist_searchForm_id').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
                 };
                break;      
            case 'orderrecreate':
            	//点击左边菜单栏，保留查询条件
            	 var grid = Ext.getCmp("orderrecreate_grid_id");
                 if (grid) {
                	 Ext.getCmp('orderrecreate_searchform_id').reset();
                     grid.getStore("recreateorderlistpage").load();
                     Ext.getCmp('orderrecreate_searchform_id').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
                     Ext.getCmp('orderrecreate_searchform_id').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
                 };
                break; 
            case 'orderaudit':
            	//待审核
            	var gridOrderAudit = Ext.getCmp("gridorderaudit_id");
                if (gridOrderAudit) {
                	Ext.getCmp('orderaudit_searchformaudit_id').reset();
                	gridOrderAudit.getStore("ordersResults").load();
                	Ext.getCmp('orderaudit_searchformaudit_id').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('orderaudit_searchformaudit_id').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
                };
                
                //已审核
                var gridOrderAudited = Ext.getCmp("gridorderaudited_id");
                if (gridOrderAudited) {
                	Ext.getCmp('orderaudit_searchformaudited_id').reset();
                	gridOrderAudited.getStore("ordersResults").load();
                	Ext.getCmp('orderaudit_searchformaudited_id').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('orderaudit_searchformaudited_id').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
                };
                break;
            case 'orderAllocate':
            	//待排车
            	var gridOrderAudit = Ext.getCmp("allocatingGrid");
                if (gridOrderAudit) {
                	Ext.getCmp('allocatingsearchform_id').reset();
                	gridOrderAudit.getStore("allocatedCarReport").load();
                	Ext.getCmp('allocatingsearchform_id').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('allocatingsearchform_id').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
                };
                
                //已排车
                var gridOrderAudited = Ext.getCmp("allocatedGrid");
                if (gridOrderAudited) {
                	Ext.getCmp('allocatedsearchform_id').reset();
                	gridOrderAudited.getStore("allocatedCarReport").load();
                	Ext.getCmp('allocatedsearchform_id').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('allocatedsearchform_id').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
                };
                break;
            case 'vehicle_realtime_monitoring':
                var grid = Ext.getCmp("vehicleMonitoringListGrid");
                if (grid) {
                    console.log('chick vehicle_realtime_monitoring');
                    loadVehicleRealtimeReport();
                }

                function loadVehicleRealtimeReport() {
                    var input = {
                        "vehicleType": "-1",
                        "fromOrgId": "-1",
                        "deptId": "-1"
                    };
                    var json = Ext.encode(input);
                    Ext.Ajax.request({
                        //                      url : 'vehicle/queryObdLocationList?json={"vehicleType":"-1","fromOrgId":"-1","deptId":"-1"}',
                        url: 'vehicle/queryObdLocationList',
                        method: 'POST',
                        //                      defaultHeaders : {
                        //                          'Content-type' : 'application/json;utf-8'
                        //                      },
                        params: { json: json },
                        success: function(response, options) {
                            var respText = Ext.util.JSON.decode(response.responseText);
                            var data = respText.data;
                            //                          Ext.getCmp('vehicleMonitoringListGrid').getViewModel().getStore("vehicleRealtimeReport").loadData(data);
                            Ext.getCmp('vehicleMonitoringListGrid').getStore().loadData(data);
                            //加载地图
                            onAfterRenderPanelTrackingMap(data);
                        },
//                        failure: function() {
//                            Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//                        },
                        scope: this
                    });
                }

                function onAfterRenderPanelTrackingMap(data) {
                    var map = Ext.getCmp('vehiclemapdis_bmappanel').bmap;
                    map.clearOverlays();
                    //                  map.reset();
                    var points = new Array();
                    //添加比例尺控件
                    var top_left_control = new BMap.ScaleControl({ anchor: BMAP_ANCHOR_TOP_LEFT }); // 左上角，添加比例尺
                    var top_left_navigation = new BMap.NavigationControl(); //左上角，添加默认缩放平移控件
                    map.addControl(top_left_control);
                    map.addControl(top_left_navigation);
                    var point;
                    //自定义函数,创建标注
                    function addMarker(point, status, speed, tracetime, vehiclenumber, imei) {
                        var myIcon;
                        if (typeof(speed) == undefined || speed == '0') {
                            myIcon = new BMap.Icon("resources/images/icons/icon_home_parkinglocated.png", new BMap.Size(60, 50), { //小车图片
                                imageOffset: new BMap.Size(0, 0) //图片的偏移量。为了是图片底部中心对准坐标点。
                            });
                        } else {
                            myIcon = new BMap.Icon("resources/images/icons/icon_home_drivinglocated.png", new BMap.Size(60, 50), { //小车图片
                                imageOffset: new BMap.Size(0, 0) //图片的偏移量。为了是图片底部中心对准坐标点。
                            });
                        }
                        var marker = new BMap.Marker(point, { icon: myIcon });
                        //                    var marker = new BMap.Marker(point);
                        map.addOverlay(marker);
                        //绑定事件
                        addClickHandler(marker, imei, point.lng, point.lat, vehiclenumber, speed,
                            status, tracetime);
                    }
                    //轮询数据
                    for (var i = 0; i < data.length; i++) {
                        var point = new BMap.Point(data[i].longitude, data[i].latitude);
                        addMarker(point, data[i].status, data[i].speed, data[i].tracetime,
                            data[i].vehiclenumber, data[i].imei);
                        points.push(point);
                    }

                    function addClickHandler(marker, imei, longitude, latitude,
                        vehiclenumber, speed, status, tracetime) {
                        //绑定点击事件
                        marker.addEventListener("click", function(e) {
                            //加载消息窗口
                            openRealtimeView(imei, longitude, latitude, vehiclenumber, speed, status, tracetime);
                        });
                    }

                    function openRealtimeView(imei, longitude, latitude, vehiclenumber, speed, status, tracetime) {
                        var win = Ext.widget("realtimeMapWindow", {
                            //                          title: '车辆实时定位——' + rec.data.vehicle_num + '——窗口将在10秒后刷新',
                            closable: true,
                            buttonAlign: 'center',
                            imei: imei,
                            longitude: longitude,
                            latitude: latitude,
                            vehiclenumber: vehiclenumber,
                            speed: speed,
                            status: status,
                            tracetime: tracetime,
                            listeners: {
                                close: function() {
                                    runner.stop(task);
                                }
                            }
                        });
                        win.show();
                    }
                    if (points.length != 0) {
                        if (points.length == 1) {
                            map.centerAndZoom(points[0], 15);
                        } else {
                            map.setViewport(points);
                        }
                    } else {
                         map.centerAndZoom(new BMap.Point(119.314088, 26.08788), 11);
/*                        function myFun(result) {
                            var cityName = result.name;
                            console.log('initView:' + cityName);
                            map.setCenter(cityName);
                            map.centerAndZoom(cityName, 12);
                        }
                        var myCity = new BMap.LocalCity();
                        myCity.get(myFun);*/
                    }
                    map.panBy(-50, 0); //地图偏移
                    map.addControl(new BMap.MapTypeControl({ mapTypes: [BMAP_NORMAL_MAP, BMAP_SATELLITE_MAP] }));
                }
                break;
            case "locationmgmt":
                var grid = Ext.getCmp("gridLocation");
                if (grid) {
                    Ext.getCmp('locationsearchForm').reset();
                    Ext.getCmp('locationPage').store.currentPage = 1;
                    Ext.getCmp('locationPage').pageSize = 10;
                    grid.getStore("locationResults").load();
                };
                break;
            case "ruleinfomgmt":
                var grid = Ext.getCmp("gridRuleInfo");
                if (grid) {
                    grid.getStore("ruleInfoResults").load();
                };
                break;
            case "systemmessage":
                var grid = Ext.getCmp("gridMessage");
                if (grid) {
                    Ext.getCmp('messageId').store.currentPage = 1;
                    Ext.getCmp('messageId').pageSize = 20;
                    grid.getStore("messageResults").load();
                };
                break;
            case "stationmgmt":
                var grid = Ext.getCmp("gridStation");
                if (grid) {
                    Ext.getCmp('stationsearchForm').reset();
                    Ext.getCmp('stationPage').store.currentPage = 1;
                    Ext.getCmp('stationPage').pageSize = 10;
                    grid.getStore("stationsResults").load();
                };
                break;
            case "geofencemgmt":
                var grid = Ext.getCmp("gridGeofence");
                if (grid) {
                    Ext.getCmp('geofencesearchForm').reset();
                    Ext.getCmp('geofencePage').store.currentPage = 1;
                    Ext.getCmp('geofencePage').pageSize = 10;
                    grid.getStore("geofenceResults").load();
                };
                break;
            case "overSpeedAlarm":
                var grid = Ext.getCmp("overSpeedVehicleGrid");
                if (grid) {
                    Ext.getCmp('overSpeedAlarmSearchForm').getForm().findField('startTime').setValue('');
                    Ext.getCmp('overSpeedAlarmSearchForm').getForm().findField('endTime').setValue(new Date());
                    Ext.getCmp('overSpeedAlarmPage').store.currentPage = 1;
                    Ext.getCmp('overSpeedAlarmPage').pageSize = 10;
                    grid.getStore("OverSpeedResults").load();
                };
                break;
            case "outMarkerMgmt":
                var grid = Ext.getCmp("outMarkerVehicleGrid");
                if (grid) {
                    Ext.getCmp('outMarkerAlarmSearchForm').getForm().findField('startTime').setValue('');
                    Ext.getCmp('outMarkerAlarmSearchForm').getForm().findField('endTime').setValue(new Date());
                    Ext.getCmp('outMarkerPage').store.currentPage = 1;
                    Ext.getCmp('outMarkerPage').pageSize = 10;
                    grid.getStore("outMarkerResults").load();
                };
                break;
            case "backStationMgmt":
                var grid = Ext.getCmp("backStationVehicleGrid");
                if (grid) {
                    Ext.getCmp('backStationAlarmSearchForm').getForm().findField('startTime').setValue('');
                    Ext.getCmp('backStationAlarmSearchForm').getForm().findField('endTime').setValue('');
                    Ext.getCmp('backStationPage').store.currentPage = 1;
                    Ext.getCmp('backStationPage').pageSize = 10;
                    grid.getStore("backStationResults").load();
                };
                break;
            case "mantainance":
                var grid = Ext.getCmp("mantaingrid");
                if (grid) {
                    Ext.getCmp('mantainanceSearchForm').getForm().findField('vehicleNumber').setValue('');
                    Ext.getCmp('mantainanceSearchForm').getForm().findField('deptId').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('mantainanceSearchForm').getForm().findField('deptName').setValue(window.sessionStorage.getItem("organizationName"));
                    Ext.getCmp('mantainanceSearchForm').getForm().findField('includeSelf').setValue(true);
                    Ext.getCmp('mantainanceSearchForm').getForm().findField('includeChild').setValue(true);
                    Ext.getCmp('mantainanceSearchForm').getForm().findField('searchScope').setValue('');
                    Ext.getCmp('mantainGridPage').store.currentPage = 1;
                    Ext.getCmp('mantainGridPage').pageSize = 10;
                    grid.getStore("mantainanceStore").load();
                };
                break;                
            case "annualInspection":
                var grid = Ext.getCmp("annualInspectionGrid");
                if (grid) {
                    Ext.getCmp('annualInspectionSearchForm').getForm().findField('vehicleNumber').setValue('');
                    Ext.getCmp('annualInspectionSearchForm').getForm().findField('arrangedOrgName').setValue(window.sessionStorage.getItem("organizationId"));
                    Ext.getCmp('annualInspectionSearchForm').getForm().findField('arrangedOrgNameName').setValue(window.sessionStorage.getItem("organizationName"));
                    Ext.getCmp('annualInspectionSearchForm').getForm().findField('selfDept').setValue(true);
                    Ext.getCmp('annualInspectionSearchForm').getForm().findField('childDept').setValue(true);
                    Ext.getCmp('annualInspectionSearchForm').getForm().findField('insuranceStatus').setValue('');
                    Ext.getCmp('annualInspectionSearchForm').getForm().findField('inspectionStatus').setValue('');
                    Ext.getCmp('annualInspectionPage').store.currentPage = 1;
                    Ext.getCmp('annualInspectionPage').pageSize = 10;
                    grid.getStore("annualInspectionStore").load();
                };
                break;
        }
    },

    viewInfo: function() {
        //alert('个人信息查看');
        var rec = new Ext.data.Model();

        Ext.Ajax.request({
            url: 'user/loadCurrentUser',
            method: 'POST',
            //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
            success: function(response, options) {
                var respText = Ext.util.JSON.decode(response.responseText);
                var data = respText.data;

                if (respText.success == true) {
                    var win = Ext.widget("ViewUserBaseInfo");
                    if (data.organizationName == null || data.organizationName == '') {
                        data.organizationName = '--';
                    }
                    rec.data = data;
                    win.down("form").loadRecord(rec);
                    win.show();
                }
            }
        /*,
            failure: function() {
                Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
            },*/
        });
    },

    //个人信息修改
    changeUserInfo: function(btn) {
        var userInfo = this.getView().down('form').getForm().getValues();
        //手机号码验证
/*        var regx1 = /[1][3578]\d{9}$/;
        var res1 = regx1.test(userInfo.phone);
        if (!res1) {
            Ext.Msg.alert('提示信息', '你输入的手机号码有误，请重新输入！');
            return;
        }

        //邮箱验证
        var regx2 = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        var res2 = regx2.test(userInfo.email);
        if (!res2) {
            Ext.Msg.alert('提示信息', '你输入的邮箱地址有误，请重新输入！');
            return;
        }*/

        var input = {
            'realname': userInfo.realname,
            'phone': userInfo.phone,
            'email': userInfo.email,
        }
        var json = Ext.encode(input);
        Ext.Ajax.request({
            url: 'user/changeUserInfo', //?json='+ Ext.encode(input),
            method: 'POST',
            params: { json: json },
            //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
            success: function(response, options) {
                var respText = Ext.util.JSON.decode(response.responseText);
                var data = respText.data;

                if (respText.status == 'success') {
                    btn.up('ViewUserBaseInfo').close();
                    Ext.Msg.alert('提示信息', '修改成功');
                    var usernameText = Ext.ComponentQuery.query('#mainView')[0].down('toolbar').query('#usernameText')[0];
                    usernameText.setText(userInfo.realname);
                    //this.viewInfo();
                } else if (respText.status == 'failure') {
                    Ext.Msg.alert('提示信息', respText.msg);
                }
            },
//            failure: function() {
//                Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//            },
            scope: this
        });
    },

    modifyPassword: function() {
        //alert('修改密码');
        win = Ext.widget('modifypassword');
        win.show();
    },

    modifypasswordDone: function(btn) {
        //alert('完成密码修改');
        var userInfo = this.getView().down('form').getForm().getValues();
        var oldpassword = userInfo.oldpassword;
        var newpassword = userInfo.newpassword;
        var confirmpassword = userInfo.confirmpassword;

        if (newpassword != confirmpassword) {
            Ext.Msg.alert('提示信息', '你两次输入的新密码不一致，请重新输入！');
            return;
        }

        //密码格式验证
        var regx = /^([a-zA-Z0-9]){6,20}$/;
        if (!regx.test(newpassword)) {
            Ext.Msg.alert('提示信息', '你输入的密码格式不符合要求，请重新输入6-20位字符,含数字、字母！');
            return;
        }
        if (!regx.test(confirmpassword)) {
            Ext.Msg.alert('提示信息', '你输入的密码格式不符合要求，请重新输入6-20位字符,含数字、字母！');
            return;
        }

        var input = {
            'oldPassword': userInfo.oldpassword,
            'newPassword': userInfo.newpassword,
        }
        var json = Ext.encode(input);
        Ext.Ajax.request({
            url: 'user/changePassword', //?json=' + Ext.encode(input),
            method: 'POST',
            params: { json: json },
            //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
            success: function(response, options) {
                var respText = Ext.util.JSON.decode(response.responseText);
                var data = respText.data;
                if (respText.status == 'success') {
                    Ext.Msg.alert('提示信息', '密码修改成功！');
                    btn.up('modifypassword').close();
                } else if (respText.status == 'failure') {
                    Ext.Msg.alert('提示信息', respText.msg);
                }
            }
            /*,
            failure: function() {
                Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
            },*/
        });

    }
});

//获取鼠标在document内的绝对位置
function mousePosition(evt){
	var ev = evt || window.event; 
	window.mousePosition = {};
	if(ev.pageX || ev.pageY){
		window.mousePosition.x = ev.pageX;
		window.mousePosition.y = ev.pageY;
	}else{
		window.mousePosition.x = ev.clientX + document.body.scrollLeft - document.body.clientLeft;
		window.mousePosition.y = ev.clientY + document.body.scrollTop - document.body.clientTop;
	}
}
function showVehMaintainInfoOver(me){
    $('#pop').show();
    var x = $('#triangle').offset().left;
    var position = $("#square").offset();
    $("#square").offset({left:x - 190, y:position.top});
    //给消息图标添加鼠标离开事件
    $("#mainPopImg").off('mouseleave').on('mouseleave',function(e){
    	if( $(me).data('ischeck') == 0 ){//是否正在检查
    		$(me).data('ischeck',1);
        	document.addEventListener('mousemove', mousePosition);//添加获取鼠标事件
        	var timer = setTimeout(function(){
        		//获取鼠标位置
        		var mp = window.mousePosition;
        		//获取pop内区域位置
            	var popOffset = $('#square').offset();
            	var popLoc = {
            			divx1:popOffset.left,
            			divy1:popOffset.top,
            			divx2:popOffset.left + parseFloat( $('#square').css("width") ),
            			divy2:popOffset.top + parseFloat( $('#square').css("height") ),
            			
            	}
            	if(( mp.x < popLoc.divx1 || mp.x > popLoc.divx2 || mp.y < popLoc.divy1 || mp.y > popLoc.divy2)){
            		//鼠标不在pop内
            		$('#pop').hide();
            	}
        		document.removeEventListener('mousemove',mousePosition);//移除获取鼠标事件
            	clearTimeout(timer)
            	$(me).data('ischeck',0);//清除检查状态
            },1000)
    	}
    })
}

function showVehMaintainInfoOut(){
    $('#pop').hide();
}


//顶部导航年检提醒什么的点击方法
function countOnClick(me) {
        var alertType = me.innerHTML.substring(0,2);
        var outboundflag = true;
        var overspeedflag = true;
        var vehiclebackflag = true;
        this.closeMainVehMoniWins();
        switch (alertType) {
            case '保养':
                if (outboundflag) {
                    this.treeItemClassReset();
                    var main = Ext.getCmp('main').getController();
                    var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();

                    treeItemClassResetforAlert();
                    $("#vehicleMixButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-vehicleMix").css("background", "url(resources/images/icons/manicons/icon_vehicle_pre.png) no-repeat center");
                    $("#vehicleMixButton-btnInnerEl").css("color", "#fff");

                    Ext.getCmp('vehiclemonitoringcontainer').hide();
                    Ext.getCmp('maincontainerwrap').show();
                    for (var i in navigationTreeListStore.data.items) {
                        navigationTreeListStore.data.items[i].collapse();
                    }
                    navigationTreeListStore.getNodeById('vehicleMixFun').expand();
                    Ext.TaskManager.stopAll();                
                    window.sessionStorage.setItem("mantainance", '1');
                    main.redirectTo('mantainance');
                    if(Ext.getCmp('mantainance') != undefined){       
                        Ext.getCmp('mantaingrid').getViewModel().getStore('mantainanceStore').load(); 
                    }
                } else {
                    Ext.Msg.alert('消息提示', '您没有越界报警权限！');
                }
                break;
            case '车险':
                if (overspeedflag) {
                    this.treeItemClassReset();
                    var main = Ext.getCmp('main').getController();
                    var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();
                    treeItemClassResetforAlert();
                    $("#vehicleMixButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-vehicleMix").css("background", "url(resources/images/icons/manicons/icon_vehicle_pre.png) no-repeat center");
                    $("#vehicleMixButton-btnInnerEl").css("color", "#fff");
                    Ext.getCmp('vehiclemonitoringcontainer').hide();
                    Ext.getCmp('maincontainerwrap').show();
                    for (var i in navigationTreeListStore.data.items) {
                        navigationTreeListStore.data.items[i].collapse();
                    }
                    navigationTreeListStore.getNodeById('vehicleMixFun').expand();
                    Ext.TaskManager.stopAll();
                    window.sessionStorage.setItem("annualInspection", '1');
                    main.redirectTo('annualInspection'); 
                    if(Ext.getCmp('annualInspection') != undefined){  
                        Ext.getCmp('annualInspectionGrid').getViewModel().getStore('annualInspectionStore').load();                  
                    }
                } else {
                    Ext.Msg.alert('消息提示', '您没有超速报警权限！');
                }
                break;
            case '年检':
                if (vehiclebackflag) {
                    this.treeItemClassReset();
                    var main = Ext.getCmp('main').getController();
                    var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();
                    treeItemClassResetforAlert();
                    $("#vehicleMixButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-vehicleMix").css("background", "url(resources/images/icons/manicons/icon_vehicle_pre.png) no-repeat center");
                    $("#vehicleMixButton-btnInnerEl").css("color", "#fff");
                    Ext.getCmp('vehiclemonitoringcontainer').hide();
                    Ext.getCmp('maincontainerwrap').show();
                    for (var i in navigationTreeListStore.data.items) {
                        navigationTreeListStore.data.items[i].collapse();
                    }
                    navigationTreeListStore.getNodeById('vehicleMixFun').expand();
                    Ext.TaskManager.stopAll();
                    main.redirectTo('annualInspection');
                    window.sessionStorage.setItem("annualInspection", '2');                  
                    main.redirectTo('annualInspection');
                    if(Ext.getCmp('annualInspection') != undefined){  
                        Ext.getCmp('annualInspectionGrid').getViewModel().getStore('annualInspectionStore').load();
                    }
                } else {
                    Ext.Msg.alert('消息提示', '您没有回车报警权限！');
                }
                break;
        }
}
