var taskOnMessage;
var taskOnActiveVehicle;

Ext.define('Admin.view.main.vehiclemonitoringmain.ViewController', {
    extend: 'Ext.app.ViewController',
    requires: [
        'Ext.window.MessageBox'
    ],

    onBeforeLoad : function(){
        console.log('onBeforeLoad');
        this.messagePanelRender();
    },

    onBeforeLoadforPre : function() {
        console.log('onBeforeLoadforPre');
        var input = {
            "isPre" : true,
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("AlertInfoPreMonitoring").proxy.extraParams = {
            "json" : pram
        }
    },

    onBeforeLoadforNext : function() {
        console.log('onBeforeLoadforNext');
        var input = {
            "isPre" : false,
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("AlertInfoNextMonitoring").proxy.extraParams = {
            "json" : pram
        }
    },

    onSearchClick: function() {
        console.log('onSearchClick');
        this.getViewModel().getStore("AlertInfoPreMonitoring").load();
        this.getViewModel().getStore("AlertInfoNextMonitoring").load();
    },

    itemGridAlarmInfoClick: function(recode, item, index) {
    	closeMainVehMoniWins();
        var outboundflag = true,
            overspeedflag = true,
            vehiclebackflag = true;
        Ext.Ajax.request({
            url: './resource/loadUserNoPermissionMenus',
            method: 'POST',
            async: false,
            success: function(response) {
                var resp = Ext.decode(response.responseText);
                if (typeof(resp) != 'undefined') {
                    var itemsNodes = Ext.getCmp('navigationTreeList').getStore().data.items;
                    var navItems = Ext.ComponentQuery.query('#mainView')[0].items.items[0].items.items;
                    for (var i in resp) {
                        var menu = resp[i].name;

                        if (menu == "越界报警") {
                            outboundflag = false;
                        }
                        if (menu == "超速报警") {
                            overspeedflag = false;
                        }
                        if (menu == "回车报警") {
                            vehiclebackflag = false;
                        }
                    }
                }
            },
            failure: function(response) {
                Ext.Msg.alert('消息提示', '获取权限失败！');
            }
        });

        switch (item.data.alertType) {
            case 'OUTBOUND':
                if (outboundflag) {
                    var main = Ext.getCmp('main').getController();
                    var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();

                    this.treeItemClassReset();
                    $("#alertMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-alertMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_abnormalwarningmgmt_pre.png) no-repeat center");
                    $("#alertMgmtButton-btnInnerEl").css("color", "#fff");

                    //Ext.getCmp('mainpagecontainer').hide();
                    Ext.getCmp('maincontainerwrap').show();
                    Ext.getCmp('vehiclemonitoringcontainer').hide();
                    for (var i in navigationTreeListStore.data.items) {
                        navigationTreeListStore.data.items[i].collapse();
                    }
                    navigationTreeListStore.getNodeById('alertMgmt').expand();
                    Ext.TaskManager.stopAll();
                    main.redirectTo('outMarkerMgmt');
                } else {
                    Ext.Msg.alert('消息提示', '您没有越界报警权限！');
                }
                break;
            case 'OVERSPEED':
                if (overspeedflag) {
                    var main = Ext.getCmp('main').getController();
                    var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();

                    this.treeItemClassReset();
                    $("#alertMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-alertMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_abnormalwarningmgmt_pre.png) no-repeat center");
                    $("#alertMgmtButton-btnInnerEl").css("color", "#fff");

                    //Ext.getCmp('mainpagecontainer').hide();
                    Ext.getCmp('maincontainerwrap').show();
                    Ext.getCmp('vehiclemonitoringcontainer').hide();
                    for (var i in navigationTreeListStore.data.items) {
                        navigationTreeListStore.data.items[i].collapse();
                    }
                    navigationTreeListStore.getNodeById('alertMgmt').expand();
                    Ext.TaskManager.stopAll();
                    main.redirectTo('overSpeedAlarm');
                } else {
                    Ext.Msg.alert('消息提示', '您没有超速报警权限！');
                }
                break;
            case 'VEHICLEBACK':
                if (vehiclebackflag) {
                    var main = Ext.getCmp('main').getController();
                    var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();

                    this.treeItemClassReset();
                    $("#alertMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                    $(".app-header-toolbar-button-alertMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_abnormalwarningmgmt_pre.png) no-repeat center");
                    $("#alertMgmtButton-btnInnerEl").css("color", "#fff");

                    //Ext.getCmp('mainpagecontainer').hide();
                    Ext.getCmp('maincontainerwrap').show();
                    Ext.getCmp('vehiclemonitoringcontainer').hide();
                    for (var i in navigationTreeListStore.data.items) {
                        navigationTreeListStore.data.items[i].collapse();
                    }
                    navigationTreeListStore.getNodeById('alertMgmt').expand();
                    Ext.TaskManager.stopAll();
                    main.redirectTo('backStationMgmt');
                } else {
                    Ext.Msg.alert('消息提示', '您没有回车报警权限！');
                }
                break;
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
    },
    //公告
    messagePanelRender: function() {
        var messagePanel = Ext.getCmp('messagePanel');
        // console.log('Systems Message in' + new Date());
        Ext.Ajax.request({
            url: 'sysMessage/getLatestSystemMessage',
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'charset': 'UTF-8' },
            success: function(res) {
                var appendData = Ext.JSON.decode(res.responseText);
                if (appendData.status == 'success' && appendData.data != null) {
                    // var html = '<ul class="messageContent">';
                    var titleOne, titleTwo, titleThree;
                    console.log('公告信息数据');
                    console.log(appendData);
                    if (appendData.data[0] != undefined) {
                        // if (appendData.data[0].title.length > 20) {
                        //     titleOne = appendData.data[0].title.substring(0, 20) + '...';
                        // } else {
                        titleOne = appendData.data[0].title;
                        // }
                        messagePanel.getViewModel().set('systemMessageOne', titleOne);
                        messagePanel.getViewModel().set('systemMessageOneTime', "【"+appendData.data[0].time.substring(0,10)+"】");
                        messagePanel.getViewModel().set('systemMessageOneContent',"1."+appendData.data[0].msg);
                    }
                    // var systemMessageOne = titleOne;
                    // var systemMessageOneTime = appendData.data[0].time.split(" ")[0];
                    // var systemMessageOneContent = appendData.data[0].time + '&' + appendData.data[0].title + '&' + appendData.data[0].msg;
                    // html += '<li><a onclick="getFirstMessage(this)" >' + systemMessageOne + '</a><span>' + systemMessageOneTime + '</span><p hidden="hidden">' + systemMessageOneContent + '</p></li>';

                    if (appendData.data[1] != undefined) {
                        // if (appendData.data[1].title.length > 20) {
                        //     titleTwo = appendData.data[1].title.substring(0, 20) + '...';
                        // } else {
                        titleTwo = appendData.data[1].title;
                        // }
                        messagePanel.getViewModel().set('systemMessageTwo', titleTwo);
                        messagePanel.getViewModel().set('systemMessageTwoTime', "【"+appendData.data[1].time.substring(0,10)+"】");
                        messagePanel.getViewModel().set('systemMessageTwoContent', "2."+appendData.data[1].msg);

                        // var systemMessageTwo = titleTwo;
                        // var systemMessageTwoTime = appendData.data[1].time.split(" ")[0];
                        // var systemMessageTwoContent = appendData.data[1].time + '&' + appendData.data[1].title + '&' + appendData.data[1].msg;
                        // html += '<li><a onclick="getFirstMessage(this)" >' + systemMessageTwo + '</a><span>' + systemMessageTwoTime + '</span><p hidden="hidden">' + systemMessageTwoContent + '</p></li>';

                    }


                    if (appendData.data[2] != undefined) {
                        // if (appendData.data[2].title.length > 20) {
                        //     titleThree = appendData.data[2].title.substring(0, 20) + '...';
                        // } else {
                        titleThree = appendData.data[2].title;
                        // }
                        messagePanel.getViewModel().set('systemMessageThree', titleThree);
                        messagePanel.getViewModel().set('systemMessageThreeTime', "【"+appendData.data[2].time.substring(0,10)+"】");
                        messagePanel.getViewModel().set('systemMessageThreeContent', "3."+appendData.data[2].msg);

                        // var systemMessageThree = titleThree;
                        // var systemMessageThreeTime = appendData.data[2].time.split(" ")[0];
                        // var systemMessageThreeContent = appendData.data[2].time + '&' + appendData.data[2].title + '&' + appendData.data[2].msg;
                        // html += '<li><a onclick="getFirstMessage(this)" >' + systemMessageThree + '</a><span>' + systemMessageThreeTime + '</span><p hidden="hidden">' + systemMessageThreeContent + '</p></li>';

                    }
                    //var flag = 0;
                    setTimeout(function(){
                        if(flag===0){
                            flag = 1;
                            //alert($("#mainMessageContent").css("marginLeft"));
                            if(parseInt($("#mainMessageContent").css("marginLeft"))===0){
                                $("#mainMessageContent").append($("#message1").clone());//复制公告1
                                $("#mainMessageContent").append($("#message2").clone());//复制公告2
                                $("#mainMessageContent").append($("#message3").clone());//复制公告3
                                $("#mainMessageContent").append($("#message1").clone());//复制公告1
                                $("#mainMessageContent").append($("#message2").clone());//复制公告2
                                $("#mainMessageContent").append($("#message3").clone());//复制公告3
                                var saveFlag = 1;//当前应该判断第几条
                                var messageFlag = setInterval(function(){
                                    $("#mainMessageContent").animate({marginLeft:parseInt($("#mainMessageContent").css("marginLeft"))-25+"px"},490,"linear",function(){//每秒移动50像素
                                        $(this).stop(true);
                                        //$("#mainMessageContent").css("marginLeft",10000-parseInt($("#mainMessageContent").width())-50);//动画有点问题，复位
                                        $("#mainMessageContent").width($("#mainMessageContent").width()+25);//增加宽度
                                        $("#mainMessageContent").append($("#message"+saveFlag).clone());//复制公告
                                        saveFlag>=3?saveFlag=1:saveFlag+=1;//重置当前
                                    });
                                },500)//每秒判定一次
                            }
                        }
                    },500);
                    // html += "</ul>";
                    // Ext.getCmp("messagePanel").setHtml(html);

                } else {
                    // var html = '<div style="color:gray; font-size:13px; padding:10px; ">暂无公告！</div>';
                    // Ext.getCmp("messagePanel").setHtml(html);
                }

                // $("#mainMessage").hide();
            }
        });
    },

    //地图：
    afterrenderMap: function(panel, eOpts) {
        /*//代办事项
         var backlogPanel = this.lookupReference('backlogPanel');
         this.undealPanelRender(backlogPanel);

         //报警信息
         var alarmGrid = this.lookupReference('gridAlarmInfo');
         alarmGrid.getViewModel().getStore("AlertInfo").load();*/

        //重新加载时停掉所有定时器
        Ext.TaskManager.stopAll();

        //控制按钮，只有选中车辆时有效
        var vehicleNumber = panel.vehicleNumber;
        if(vehicleNumber == null || vehicleNumber == ''){
//    		panel.down("form").disable();
            panel.down("form").query('button').forEach(function(c){c.setDisabled(true);});
        }else{
//    		panel.down("form").enable();
            panel.down("form").query('button').forEach(function(c){c.setDisabled(false);});
        }

        //拿到地图
        var map = this.lookupReference('bmappanelMainPage').bmap;
        map.clearOverlays();
        this.initMap(map);
        loadVehicleStatus();
        var isFullMap = false;
        loadVehiclePosition(isFullMap, panel, map, 'init');

        //定时器
        var clockStart = function(panel, map) {
            loadVehicleStatus();
            loadVehiclePosition(isFullMap, panel, map, 'noInit');

            var fullscreen = Ext.getCmp("viewMonitoringFullScreenMap");
	        if(fullscreen != null){
	        	var fullmap = fullscreen.down("mappanel").bmap;
	        	var fullMapFlag = true;
	        	loadVehiclePosition(fullMapFlag, panel, fullmap, 'noInit');
	        }
        }

        var i = 10;

        var runByTime = function() {
            console.log("定时刷新地图: " + new Date() + ' : ' + i);
            if (i > 1) {
                i--;
            } else {
                clockStart(panel, map);
                i = 10;
            }
            $("#mainMonitoringTable th")[8].innerHTML = '地图将在' + i + '秒后刷新';
        };

        taskOnActiveVehicle = Ext.TaskManager.start({
            run: function() { runByTime() },
            interval: 1000
        });
    },

    afterrenderMapWindow: function() {
        console.log('全屏地图');
        var mainpageView = Ext.getCmp('vehicleMonitoringMain');
        var map = this.lookupReference('bmappanelMainPage');
        
        var main = Ext.getCmp("main");
        var mainWidth = document.getElementById("main-innerCt").offsetWidth;
        console.log("全屏地图宽度：" + mainWidth);
//        map.width = document.documentElement.clientWidth;
        map.width = mainWidth;
        map.height = document.documentElement.clientHeight;

        //        map.bmap.width = document.documentElement.clientWidth;
        //        map.bmap.height = document.documentElement.clientHeight;
        map.bmap.enableAutoResize();
        var viewFullScreen = this.lookupReference('bmappanelMainPage').up().up();
//        viewFullScreen.width = document.documentElement.clientWidth;
        viewFullScreen.width = mainWidth;
        viewFullScreen.height = document.documentElement.clientHeight;

        this.initFullScreenMap(map.bmap);
        var bmap = Ext.getCmp('viewMonitoringFullScreenMap').down().down().bmap;
        var data = mainpageView.mapdata;
        //console.log("data:" + JSON.stringify(data));
        if (data != null && data.length > 0) {
            if(data.length == 1){
                bmap.centerAndZoom(new BMap.Point(data[0].longitude, data[0].latitude), 13);
//            	var polyline = Ext.getCmp("mainpageView").down("mappanel").trackLine;
//            	bmap.addOverlay(polyline);
            }
            var isFullMap = true;
            onAfterRenderPanelTrackingMap(isFullMap, data, bmap,'init');
        } else {

            map.bmap.centerAndZoom(new BMap.Point(119.314088, 26.08788), 11);
/*            function myFun(result) {
                var cityName = result.name;
                //console.log('initView:' + cityName);
                map.bmap.setCenter(cityName);
                map.bmap.centerAndZoom(cityName, 12);
            }
            var myCity = new BMap.LocalCity();

            myCity.get(myFun);*/
        }
    },

    initFullScreenMap: function(map) {

        var top_left_control = new BMap.ScaleControl({ anchor: BMAP_ANCHOR_BOTTOM_LEFT }); // 左下角，添加比例尺
        //var top_left_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_LEFT, offset: new BMap.Size(16, 74)});  //左上角，添加默认缩放平移控件
        map.addControl(top_left_control);
        //map.addControl(top_left_navigation);

        function ZoomControl() {
            this.defaultAnchor = BMAP_ANCHOR_BOTTOM_RIGHT;
            this.defaultOffset = new BMap.Size(8, 10);
        }
        ZoomControl.prototype = new BMap.Control();
        ZoomControl.prototype.initialize = function(map) {
            var div = document.createElement("div");

            var zoomMin = document.createElement("div");

            zoomMin.style.background = "url(resources/images/icons/manicons/icon_home_map_fold.png) no-repeat center";
            zoomMin.style.width = "60px",
                zoomMin.style.height = "60px",
                zoomMin.onclick = function(e) {
                    Ext.getCmp('viewMonitoringFullScreenMap').close();
                }
            zoomMin.onmouseover = function() {
                zoomMin.style.background = "url(resources/images/icons/manicons/icon_home_map_fold_pre.png) no-repeat center";
            }
            zoomMin.onmouseout = function() {
                zoomMin.style.background = "url(resources/images/icons/manicons/icon_home_map_fold.png) no-repeat center";
            }
            div.appendChild(zoomMin);

            //放大控件
            var zoomAdd = document.createElement("div");
            zoomAdd.style.background = "url(resources/images/icons/manicons/icon_home_zoomout.png) no-repeat center";
            zoomAdd.style.width = "60px",
                zoomAdd.style.height = "60px",
                zoomAdd.onclick = function(e) {
                    map.setZoom(map.getZoom() + 1);
                }
            zoomAdd.onmouseover = function() {
                zoomAdd.style.background = "url(resources/images/icons/manicons/icon_home_zoomout_pre.png) no-repeat center";
            }
            zoomAdd.onmouseout = function() {
                zoomAdd.style.background = "url(resources/images/icons/manicons/icon_home_zoomout.png) no-repeat center";
            }
            div.appendChild(zoomAdd);

            //缩小控件
            var zoomReduce = document.createElement("div");
            zoomReduce.style.background = "url(resources/images/icons/manicons/icon_home_zoomin.png) no-repeat center";
            zoomReduce.style.width = "60px",
                zoomReduce.style.height = "60px",
                zoomReduce.onclick = function(e) {
                    map.setZoom(map.getZoom() - 1);
                }
            zoomReduce.onmouseover = function() {
                zoomReduce.style.background = "url(resources/images/icons/manicons/icon_home_zoomin_pre.png) no-repeat center";
            }
            zoomReduce.onmouseout = function() {
                zoomReduce.style.background = "url(resources/images/icons/manicons/icon_home_zoomin.png) no-repeat center";
            }
            div.appendChild(zoomReduce);


            map.getContainer().appendChild(div);
            return div;
        }

        var myZoomCtrl = new ZoomControl();
        map.addControl(myZoomCtrl);
    },

    initMap: function(map) {
        map.enableAutoResize();
        //添加比例尺控件
        var top_left_control = new BMap.ScaleControl({ anchor: BMAP_ANCHOR_BOTTOM_LEFT }); // 左下角，添加比例尺
        //var top_left_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_LEFT, offset: new BMap.Size(16, 74)});  //左上角，添加默认缩放平移控件
        map.addControl(top_left_control);
        //map.addControl(top_left_navigation);

        // 定义一个控件类，即function
        function ZoomControl() {
            // 设置默认停靠位置和偏移量
            this.defaultAnchor = BMAP_ANCHOR_BOTTOM_LEFT;
            this.defaultOffset = new BMap.Size(8, 10);
        }
        // 通过JavaScript的prototype属性继承于BMap.Control
        ZoomControl.prototype = new BMap.Control();

        // 自定义控件必须实现initialize方法，并且将控件的DOM元素返回
        // 在本方法中创建个div元素作为控件的容器，并将其添加到地图容器中
        ZoomControl.prototype.initialize = function(map) {
            // 创建一个DOM元素
            var div = document.createElement("div");

            //最大化控件
            var zoomMax = document.createElement("div");
            zoomMax.setAttribute('class','fullScreenBtn');
            // 设置样式
            zoomMax.style.background = "url(resources/images/icons/manicons/icon_home_map_unfold.png) no-repeat center";
            zoomMax.style.width = "60px",
                zoomMax.style.height = "60px",
                // 绑定事件，点击全屏地图
                zoomMax.onclick = function(e) {
//                    var win = Ext.widget("viewFullScreen");
                    var win = new Admin.view.main.vehiclemonitoringmain.ViewFullScreen();
                    win.show();
                }
            zoomMax.onmouseover = function() {
                zoomMax.style.background = "url(resources/images/icons/manicons/icon_home_map_unfold_pre.png) no-repeat center";
            }
            zoomMax.onmouseout = function() {
                zoomMax.style.background = "url(resources/images/icons/manicons/icon_home_map_unfold.png) no-repeat center";
            }
            div.appendChild(zoomMax);

            //放大控件
            var zoomAdd = document.createElement("div");
            zoomAdd.style.background = "url(resources/images/icons/manicons/icon_home_zoomout.png) no-repeat center";
            zoomAdd.style.width = "60px",
                zoomAdd.style.height = "60px",
                zoomAdd.onclick = function(e) {
                    map.setZoom(map.getZoom() + 1);
                }
            zoomAdd.onmouseover = function() {
                zoomAdd.style.background = "url(resources/images/icons/manicons/icon_home_zoomout_pre.png) no-repeat center";
            }
            zoomAdd.onmouseout = function() {
                zoomAdd.style.background = "url(resources/images/icons/manicons/icon_home_zoomout.png) no-repeat center";
            }
            div.appendChild(zoomAdd);

            //缩小控件
            var zoomReduce = document.createElement("div");
            zoomReduce.style.background = "url(resources/images/icons/manicons/icon_home_zoomin.png) no-repeat center";
            zoomReduce.style.width = "60px",
                zoomReduce.style.height = "60px",
                zoomReduce.onclick = function(e) {
                    map.setZoom(map.getZoom() - 1);
                }
            zoomReduce.onmouseover = function() {
                zoomReduce.style.background = "url(resources/images/icons/manicons/icon_home_zoomin_pre.png) no-repeat center";
            }
            zoomReduce.onmouseout = function() {
                zoomReduce.style.background = "url(resources/images/icons/manicons/icon_home_zoomin.png) no-repeat center";
            }
            div.appendChild(zoomReduce);

            //        var dWidth = window.screen.availWidth;
            //           var dWidth = window.width;
            var dWidth = $(window).width();
            //alert(dWidth)
//            var marginRight = 0;
            var marginRight = 1240;
            //由于没摸清楚中间地图部分的宽度为什么在1600，280下是一样的，在1920下又不一样，所以先固定控件marginRight
//            if (dWidth < 1900) {
//                marginRight = 790;
//            } else {
//                marginRight = 1020;
//            }
            if (dWidth < 1900) {
                marginRight = 1240;
            } else {
                marginRight = 1580;
            }
            var margin = "0px 0px 0px " + marginRight + "px";
            div.style.margin = margin;

            // 添加DOM元素到地图中
            map.getContainer().appendChild(div);
            // 将DOM元素返回
            return div;
        }
        var myZoomCtrl = new ZoomControl();
        map.addControl(myZoomCtrl);

        //添加放大控件

        //添加缩小控件
    },

    getXfromBody: function (obj) {
        var l = obj.offsetLeft;
        while(obj.offsetParent != null){
            obj = obj.offsetParent;
            l += obj.offsetLeft;
        }
        return l;
    },

    getYfromBody: function (obj) {
        var l = obj.offsetTop;
        while(obj.offsetParent != null){
            obj = obj.offsetParent;
            l += obj.offsetTop;
        }
        return l;
    },

    closeOtherWins: function(winId){
        if(Ext.getCmp('viewVehicleInfo') != null){
            if(winId != 'viewVehicleInfo'){
                Ext.getCmp('viewVehicleInfo').close();
            }
        }

        if(Ext.getCmp('viewVehicleCountInfo') != null){
            if(winId != 'viewVehicleCountInfo'){
                Ext.getCmp('viewVehicleCountInfo').close();
            }
        }

        if(Ext.getCmp('gridAlarmStatisticsWindow') != null){
            if(winId != 'gridAlarmStatisticsWindow'){
                Ext.getCmp('gridAlarmStatisticsWindow').close();
            }
        }
    },

    //获得车辆信息
    getVehInfo: function(btn, pressed, eOpts) {
    	if(!pressed){
    		if(Ext.getCmp('viewVehicleInfo') != undefined){
    			Ext.getCmp('viewVehicleInfo').close();
            }
    		return;
    	}
    	//$("#fullScreenBtn").hide();
        this.closeOtherWins('viewVehicleInfo');
        //判断窗口是否已经开启
        if(Ext.getCmp('viewVehicleInfo') != undefined){
            return;
        }
        var mainpanel = Ext.getCmp("vehicleMonitoringMain");
        var vehName = mainpanel.vehicleNumber;
        vehName = decodeURIComponent(vehName);
        var input = {
            //"vehicleNumber":'京AX77G6'
            "vehicleNumber":vehName
        };
        var objectModel = new Ext.data.Model();
        var win = Ext.widget("viewVehicleInfo");
        win.x = this.getXfromBody(document.getElementById("VehicleMonitoringStaticInfo"));
        win.y = this.getYfromBody(document.getElementById("VehicleMonitoringStaticInfo"));;
        var json = Ext.encode(input);
        Ext.Ajax.request({
            url: 'vehicle/findVehicleInfoByVehicleNumber',
            method : 'POST',
            params:{json:json},
            success : function(response,options) {
                var respText = Ext.util.JSON.decode(response.responseText);
                var data = respText.data;
                if (respText.status == 'success') {
                    if(data != null){
                        objectModel.data = data;
                        win.down("form").loadRecord(objectModel);
                        win.show();
                        $("#viewVehicleInfo").find(".x-form-item-label-text").css("color","#333");
                        $("#viewVehicleInfo").find('.x-form-display-field-default').css("color","#666");
                    }else{
                        Ext.Msg.alert('消息提示','获取不到车辆信息！');
                        win.close();
                    }
                }else{
                    Ext.Msg.alert('消息提示','获取不到车辆信息！');
                    win.close();
                }
            }
            /*,
            failure : function() {
                Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
                win.close();
            },*/
        });
    },

    //获得车辆数据统计
    getVehicleCountInfo: function(btn, pressed, eOpts) {
    	if(!pressed){
    		if(Ext.getCmp('viewVehicleCountInfo') != undefined){
    			Ext.getCmp('viewVehicleCountInfo').close();
            }
    		return;
    	}
    	//$("#fullScreenBtn").hide();
        this.closeOtherWins('viewVehicleCountInfo');
        //判断窗口是否已经开启
        if(Ext.getCmp('viewVehicleCountInfo') != undefined){
            return;
        }
        var mainpanel = Ext.getCmp("vehicleMonitoringMain");
        var vehName = mainpanel.vehicleNumber;
        vehName = decodeURIComponent(vehName);
        var input = {
            //'vehicleNumber' : '京AX77G6',
            "vehicleNumber":vehName
        };
        var objectModel = new Ext.data.Model();
        var win = Ext.widget("viewVehicleCountInfo");
        win.x = this.getXfromBody(document.getElementById("VehicleMonitoringStaticInfo"));
        win.y = this.getYfromBody(document.getElementById("VehicleMonitoringStaticInfo"));;
        var json = Ext.encode(input);

        Ext.Ajax.request({
            url: 'usage/report/getVehicleAllMileageAndFuleconsListByVehNum',
            method : 'POST',
            params:{json:json},
            success : function(response,options) {
                var respText = Ext.util.JSON.decode(response.responseText);
                var data = respText.data;
                if (respText.status == 'success') {
                    if(data != null){
                        objectModel.data = data;
                        win.down("form").loadRecord(objectModel);
                        win.show();
                        $("#viewVehicleCountInfo").find(".x-form-item-label-text").css("color","#333");
                        $("#viewVehicleCountInfo").find('.x-form-display-field-default').css("color","#666");
                    
                    }else{
                        Ext.Msg.alert('消息提示','获取不到车辆数据统计！');
                        win.close();
                    }
                }else{
                    Ext.Msg.alert('消息提示','获取不到车辆数据统计！');
                    win.close();
                }
            }
            /*,
            failure : function() {
                Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
                win.close();
            },*/
        });
    },

    GetDateStr: function(AddDayCount) {
        var dd = new Date();
        dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
        var y = dd.getFullYear();
        var m = dd.getMonth()+1;//获取当前月份的日期
        var d = dd.getDate();
        return y+"-"+m+"-"+d;
    },

    //获得车辆报警事件统计
    getAlarmStatistics: function(btn, pressed, eOpts) {
    	if(!pressed){
    		if(Ext.getCmp('gridAlarmStatisticsWindow') != undefined){
    			Ext.getCmp('gridAlarmStatisticsWindow').close();
            }
    		return;
    	}
    	//$("#fullScreenBtn").hide();
        this.closeOtherWins('gridAlarmStatisticsWindow');
        //判断窗口是否已经开启
        if(Ext.getCmp('gridAlarmStatisticsWindow') != undefined){
            return;
        }
        var mainpanel = Ext.getCmp("vehicleMonitoringMain");
        var vehName = mainpanel.vehicleNumber;
        vehName = decodeURIComponent(vehName);
//        var vehName = '沪AVF850';

        var parentWin = parent.Ext.getCmp('AlarmStatisticsWin');
        if (parentWin) {
            parentWin.close();
        }

        var win = Ext.widget("gridAlarmStatistics",{
            //id: 'AlarmStatisticsWinId'
        });
        win.x = this.getXfromBody(document.getElementById("VehicleMonitoringStaticInfo"));
        win.y = this.getYfromBody(document.getElementById("VehicleMonitoringStaticInfo"));;

        var startTime = this.GetDateStr(-7) + ' 00:00:00';
        var endTime = this.GetDateStr(0) + ' 23:59:59';
        var input = {
            'vehicleNumber' : vehName,
            'startTime' : startTime,
            'endTime' : endTime
        };

        var json = Ext.encode(input);
        Ext.getCmp('gridAlarmStatistics').getViewModel().getStore("alarmStatisticsStore").proxy.extraParams = {
            "json" : json};
        Ext.getCmp("gridAlarmStatistics").getViewModel().getStore("alarmStatisticsStore").load();
        win.show();
/*        Ext.Ajax.request({
            url: 'vehicleAlert/findVehicleAlertCountByDate',
            method : 'POST',
            params:{json:json},
            success : function(response,options) {
                var respText = Ext.util.JSON.decode(response.responseText);
                if (respText.status == 'success') {
                    if(respText.data != null){
                        Ext.getCmp('gridAlarmStatistics').getViewModel().getStore("alarmStatisticsStore").proxy.extraParams = {
                            "json" : json};
                        Ext.getCmp("gridAlarmStatistics").getViewModel().getStore("alarmStatisticsStore").load();
                        win.show();
                    }else{
                        Ext.Msg.alert('消息提示','获取不到报警事件统计！');
                        win.close();
                    }
                }else{
                    Ext.Msg.alert('消息提示','获取不到报警事件统计！');
                    win.close();
                }
            },
            failure : function() {
                Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
                win.close();
            },
        });*/
    },

    //历史轨迹按钮
    openHistoryWindow: function(){
    	$(".fullScreenBtn").hide();
        this.closeOtherWins('');
        //如果有历史轨迹或今日轨迹，关掉
        var win = Ext.getCmp("vehMoniHistoryTrace");
        if(win != null){
            win.closeNotRefreshMap();
        }

        var win = Ext.create('Admin.view.main.vehiclemonitoringmain.historytrace.HistoryTrace',{
            title:'历史轨迹'
        });
        win.x = this.getXfromBody(document.getElementById("VehicleMonitoringStaticInfo"));
        win.y = this.getYfromBody(document.getElementById("VehicleMonitoringStaticInfo"));
        win.show();
        $("#vehMoniHistoryTrace").find(".x-form-item-label-text").css("color","#333");
        $("#vehMoniHistoryTrace").find('.x-form-display-field-default').css("color","#666");
        
        //停止定时器
        Ext.TaskManager.stopAll();
        //清除实时轨迹
        var panel = Ext.getCmp("vehicleMonitoringMain");
        var line = panel.trackLine;
        if(line != null){
            line.enableMassClear();
        }
        var mappanel = Ext.getCmp("VehicleMonitoringStaticInfo").down("mappanel");
        mappanel.bmap.clearOverlays();
        panel.trackLine = null;

        //修改地图刷新提示
        $("#mainMonitoringTable th")[8].innerHTML = '地图停止刷新';
    },

    //今日轨迹按钮
    openTodayWindow: function(){
    	$(".fullScreenBtn").hide();
        this.closeOtherWins('');
        //如果有历史轨迹或今日轨迹，关掉
        var win = Ext.getCmp("vehMoniHistoryTrace");
        if(win != null){
            win.closeNotRefreshMap();
        }

        var win = Ext.create('Admin.view.main.vehiclemonitoringmain.historytrace.HistoryTrace',{
//            title:'今日轨迹'
        	header:{
        		title : '今日轨迹',
        		height:24,
        		style:{
        			lineHeight:24,
        			padding: '0px 10px 4px 10px',
        		}
        	}	
        });
        win.x = this.getXfromBody(document.getElementById("VehicleMonitoringStaticInfo"));
        win.y = this.getYfromBody(document.getElementById("VehicleMonitoringStaticInfo"));
        win.show();
        $("#vehMoniHistoryTrace").find(".x-form-item-label-text").css("color","#333");
        $("#vehMoniHistoryTrace").find('.x-form-display-field-default').css("color","#666");
        
        //停止定时器
        Ext.TaskManager.stopAll();
        //清除实时轨迹
        var panel = Ext.getCmp("vehicleMonitoringMain");
        var line = panel.trackLine;
        if(line != null){
            line.enableMassClear();
        }
        var mappanel = Ext.getCmp("VehicleMonitoringStaticInfo").down("mappanel");
        mappanel.bmap.clearOverlays();
        panel.trackLine = null;
        win.title='今日轨迹';
        //设置日期为今天
        win.down("#mainVehMoniTraceStartDate").setValue(getTodayDate());
        win.down("#mainVehMoniTraceEndDate").setValue(getTodayDate());
        win.down("#mainVehMoniTraceStartDate").disable();
        win.down("#mainVehMoniTraceEndDate").disable();
        //设置结束时间为当前时间
        var endDate = win.down("#mainVehMoniTraceEndDate");
        var date = new Date();
        endDate.nextSibling("fieldcontainer").down("combobox").setValue(date.getHours());
        endDate.nextSibling("fieldcontainer").down("combobox").nextSibling("combobox").setValue(date.getMinutes());
        endDate.nextSibling("fieldcontainer").down("combobox").nextSibling("combobox").nextSibling("combobox").setValue(date.getSeconds());
        //修改地图刷新提示
        $("#mainMonitoringTable th")[8].innerHTML = '地图停止刷新';
        //自动搜索当天轨迹
        var searchBtn = win.down("#mainVehMoniHistorySearch");
        searchBtn.fireEvent('click', searchBtn);
    },

    //实时定位按钮
    locateVeh: function(){
    	$(".fullScreenBtn").show();
        this.closeOtherWins('');
        //如果有历史轨迹或今日轨迹，关掉
        var win = Ext.getCmp("vehMoniHistoryTrace");
        if(win != null){
            win.close();
        }

        var panel = Ext.getCmp("vehicleMonitoringMain");
        //拿到地图
        var mappanel = Ext.getCmp("VehicleMonitoringStaticInfo").down("mappanel");
        var map = mappanel.bmap;
        loadVehicleStatus();

        var line = panel.trackLine;
        if(line == null){
            loadVehiclePosition(false, panel, map, 'init');
        }else{
            loadVehiclePosition(false, panel, map, 'noInit');
        }
    },
    
});

//关闭所有首页弹框
function closeMainVehMoniWins(){
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
}

function getTodayDate() {
    var dd = new Date();
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;
    if(m < 10){
        m = '0' + m;
    }
    var d = dd.getDate();
    if(d < 10){
        d = '0' + d;
    }
    return y+"-"+m+"-"+d;
}

//车辆统计在线离线状态
function loadVehicleStatus() {
    var navigationMainPageTree = Ext.getCmp('navigationVehicleMonitoringTree');
    var idselect = navigationMainPageTree.selectItemId;
    var deptId = '';
    if (idselect != null) {
        var itemSection = navigationMainPageTree.getStore().getNodeById(idselect); //点击的节点
        //如果是企业节点
        if (itemSection.data.level == '1') {
            deptId = '';
        } else if (!itemSection.data.vehicleNode) { //如果是部门节点
            deptId = itemSection.data.deptId;
        } else if (itemSection.data.vehicleNode) {	//如果是车辆节点
            deptId = itemSection.parentNode.data.deptId
        }
    }
    if (deptId == undefined) {
        deptId = '';
    }
    var input = {
        'orgId': deptId
    };
    var json = Ext.encode(input);

    //车辆信息统计
    Ext.Ajax.request({
        url: './usage/report/findVehicleStatusData',
//    	url: '/carrental-portal/app/data/arcmgmtinfo/test1.json',
        method: 'POST',
        params: { 'json': json },
        success: function(response) {
//            response = '{"data":{"totalNumber":28,"onLineNumber":7,"drivingNumber":7,"stopNumber":0,"offLineNumber":21},"status":"success"}';
//            var respText = Ext.util.JSON.decode(response);
            var respText = Ext.util.JSON.decode(response.responseText);
            //console.log(JSON.stringify(respText));
            if (respText.data != null) {
                var totalNumber = respText.data.totalNumber;
                var onLineNumber = respText.data.onLineNumber;
                var drivingNumber = respText.data.drivingNumber;
                var stopNumber = respText.data.stopNumber;
                var offLineNumber = respText.data.offLineNumber;
                $("#mainMonitoringTable th")[0].innerHTML = '车辆总数 ' + (totalNumber==null?'0':totalNumber);
                $("#mainMonitoringTable th")[1].innerHTML = '在线 ' + (onLineNumber==null?'0':onLineNumber);
                $("#mainMonitoringTable th")[2].innerHTML = '行驶 ' + (drivingNumber==null?'0':drivingNumber);
                $("#mainMonitoringTable th")[3].innerHTML = '停止 ' + (stopNumber==null?'0':stopNumber);
                $("#mainMonitoringTable th")[4].innerHTML = '离线 ' + (offLineNumber==null?'0':offLineNumber);
            }
        },
        failure: function(response) {
            console.log('AllVehs');
        }
    });

    //报警信息统计
    Ext.Ajax.request({
        //url:'vehicleAlert/getUndealCount',
        url: 'vehicleAlert/findVehicleAlertCount',
        method: 'GET',
        headers: { 'Content-Type': 'application/json', 'charset': 'UTF-8' },
        success: function(res) {
            var appendData = Ext.JSON.decode(res.responseText);
            if (appendData.status == 'success') {
                var outbound = appendData.data[0].value;
                var overspeed = appendData.data[1].value;
                var vehicleback = appendData.data[2].value;
                $("#mainMonitoringTable th")[5].innerHTML = '超速报警 ' + (overspeed==null?'0':overspeed);
                $("#mainMonitoringTable th")[6].innerHTML = '回车报警 ' + (vehicleback==null?'0':vehicleback);
                $("#mainMonitoringTable th")[7].innerHTML = '越界报警 ' + (outbound==null?'0':outbound);
            }
        }
    });
}
//每辆车的实时位置
/**
 * isFullMap: 是否全屏地图
 */
function loadVehiclePosition(isFullMap, panel, map, flag) {
    //拿到存入panel中车牌数组
    var vehicleNumber;
    if(panel.vehicleNumber != undefined){
        vehicleNumber = decodeURIComponent(panel.vehicleNumber);
    }else{
        vehicleNumber = panel.vehicleNumber;
    }
     
    var input = {
        'orgId':panel.orgId,
        'vehicleNumber':vehicleNumber
    };
    var json = Ext.encode(input);
    Ext.Ajax.request({
//        url: '/carrental-portal/vehicle/queryObdLocationByName',
        //url: '/carrental-portal/vehicle/queryObdLocationByName',
        url: './vehicle/queryObdLocation',
       // url: '/carrental-portal/app/data/arcmgmtinfo/test.json',
        method: 'POST',
        params: {json:json},
        success: function(response) {
            var respText = Ext.util.JSON.decode(response.responseText);
            var data = respText.data.obdLocationList;
            //全屏显示地图，保留数组
            var mainpageView = Ext.getCmp('vehicleMonitoringMain');
            mainpageView.mapdata = data;
            var navigationMainPageTree = Ext.getCmp('navigationVehicleMonitoringTree');
            var idselect = navigationMainPageTree.selectItemId;
            if (idselect != null && idselect.indexOf('vehicleMonitoringMain') != 0) {
                var itemSection = navigationMainPageTree.getStore().getNodeById(idselect);
                if (itemSection.hasChildNodes()) {
                    for (var i in itemSection.childNodes) {
                        for (var t in data) {
                            if (data[t].id == itemSection.childNodes[i].data.vehicleId) {
                                if (data[t].status.indexOf('行驶') == 0) {
                                    itemSection.childNodes[i].set('text', "<span class='vehicle_status'>" + data[t].vehiclenumber + '</span><span class="vehicle_status_online">行驶</span>');
                                }
                                if (data[t].status.indexOf('停止') == 0) {
                                    itemSection.childNodes[i].set('text', "<span class='vehicle_status'>" + data[t].vehiclenumber + '</span><span class="vehicle_status_stop">停止</span>');
                                }
                                if (data[t].status.indexOf('离线') == 0) {
                                    itemSection.childNodes[i].set('text', "<span class='vehicle_status'>" + data[t].vehiclenumber + '</span><span class="vehicle_status_offline">离线</span>');
                                }
                            }
                        }
                    }
                } else if (itemSection.isLeaf()) {
                    for (var t in data) {
                        if (data[t].id == itemSection.data.vehicleId) {
                            if (data[t].status.indexOf('行驶') == 0) {
                                itemSection.set('text', "<span class='vehicle_status'>" + data[t].vehiclenumber + '</span><span class="vehicle_status_online">行驶</span>');
                            }
                            if (data[t].status.indexOf('停止') == 0) {
                                itemSection.set('text', "<span class='vehicle_status'>" + data[t].vehiclenumber + '</span><span class="vehicle_status_stop">停止</span>');
                            }
                            if (data[t].status.indexOf('离线') == 0) {
                                itemSection.set('text', "<span class='vehicle_status'>" + data[t].vehiclenumber + '</span><span class="vehicle_status_offline">离线</span>');
                            }
                        }
                    }
                }
            }
            if (data != null && data.length > 0) {
                onAfterRenderPanelTrackingMap(isFullMap, data, map, flag);
            } else {

                map.centerAndZoom(new BMap.Point(119.314088, 26.08788), 11);
/*                function myFun(result) {
                    var cityName = result.name;
                    //console.log('初始化地图城市名称:' + cityName);
                    map.setCenter(cityName);
                    map.centerAndZoom(cityName, 12);
                }
                var myCity = new BMap.LocalCity();
                myCity.get(myFun);*/
            }
        },
        failure: function(response) {
            console.log('error');
        }
    });
}

function onAfterRenderPanelTrackingMap(isFullMap, data, map, flag) {
//    if (data.length == 1) {
//        var mainpageView = Ext.getCmp('mainpageView');
//        mainpageView.pointArray.push(new BMap.Point(data[0].longitude, data[0].latitude));
//    }
    map.clearOverlays();


    var points = new Array();
    var point;

    // 编写自定义函数创建标注
    function addMarker(point, status, speed, tracetime, vehiclenumber, imei, realname, phone, bearing) {
        var myIcon;
        if (status.indexOf('行驶') == 0) {
            myIcon = new BMap.Icon("resources/images/icons/icon_bussiness_driving.png", new BMap.Size(80, 80), { //小车图片
                imageOffset: new BMap.Size(25, 0) //图片的偏移量。为了是图片底部中心对准坐标点。
            });

        } else if (status.indexOf('停止') == 0) {
            myIcon = new BMap.Icon("resources/images/icons/icon_bussiness_parking.png", new BMap.Size(80, 80), { //小车图片
                imageOffset: new BMap.Size(25, 0) //图片的偏移量。为了是图片底部中心对准坐标点。
            });
        } else if (status.indexOf('离线') == 0) {
            myIcon = new BMap.Icon("resources/images/icons/icon_bussiness_offline.png", new BMap.Size(80, 80), { //小车图片
                imageOffset: new BMap.Size(25, 0) //图片的偏移量。为了是图片底部中心对准坐标点。
            });
        }

        var marker = new BMap.Marker(point, { icon: myIcon });
        map.addOverlay(marker);



        var address = '';
        if (realname == null || realname == '') {
            realname = '--';
        }
        if (phone == null || phone == '') {
            phone = '--';
        }
        var geoc = new BMap.Geocoder();
        geoc.getLocation(point, function(rs) {
            var addComp = rs.addressComponents;
            address = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
            var content = '车牌：' + vehiclenumber + '<br/>时间：' + tracetime + '<br/>状态：' + status + '<br/>经度：'  + point.lng + '<br/>纬度：' + point.lat + '<br/>方向：' + getDirection(bearing, point.lng, point.lat) + '<br/>速度：约' + speed + 'km/h' + '<br/>地址：'  + address + '<br/>司机姓名：'  + realname + '<br/>司机手机号：' + phone;
            addClickHandler(content, marker, imei, point.lng, point.lat, vehiclenumber, speed,
                status, tracetime);
        });
    }
    // 随机向地图添加标注
    for (var i = 0; i < data.length; i++) {
        var point = new BMap.Point(data[i].longitude, data[i].latitude);
        addMarker(point, data[i].status, data[i].speed, data[i].tracetime,
            data[i].vehiclenumber, data[i].imei, data[i].realname, data[i].phone, data[i].bearing);
        points.push(point);
    }

    function addClickHandler(content, marker,
                             imei, longitude, latitude, vehiclenumber, speed, status, tracetime) {
        //绑定点击事件
        marker.addEventListener("click", function(e) {
            //加载消息窗口
            openInfo(content, e)
        });
    }

    function openInfo(content, e) {
        var p = e.target;
        var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);        
        var opts = {
            enableMessage: true //设置允许信息窗发送短息
        };
        var infoWindow = new BMap.InfoWindow(content, opts); // 创建信息窗口对象 
        map.openInfoWindow(infoWindow, point); //开启信息窗口
    }
    //var mappanel = Ext.getCmp("mainpageView").down("mappanel");
    var mappanel = Ext.getCmp("vehicleMonitoringMain");
    var fullscreen = Ext.getCmp("viewMonitoringFullScreenMap");
    var fullmap = null;
    if(fullscreen != null){
        var fullmappanel = fullscreen.down("mappanel");
        fullmap = fullmappanel.map;
    }
    if (points.length == 1) {
        if (flag == 'init') {
//            var polyline = new BMap.Polyline(points, {strokeColor:"green", strokeWeight:4, strokeOpacity:0.5});
//            polyline.disableMassClear();	//禁止轨迹线在map.clearOverlays方法中被清除
//            map.addOverlay(polyline);
            if(isFullMap){
            	//如果主页地图已经有轨迹了，直接把轨迹画在全屏地图上
            	var trackLine = mappanel.trackLine;
            	if(trackLine != null){

            		var path = trackLine.getPath();
            		var newline = new BMap.Polyline(path, {strokeColor:"green", strokeWeight:4, strokeOpacity:0.5});
            		map.addOverlay(newline);
            		mappanel.fullTrackLine = newline;
//            		map.setViewport(path, {margins:[30, 30, 30, 30]});
            	}else{

            		var polyline = new BMap.Polyline(points, {strokeColor:"green", strokeWeight:4, strokeOpacity:0.5});
                    polyline.disableMassClear();	//禁止轨迹线在map.clearOverlays方法中被清除
                    map.addOverlay(polyline);
                    mappanel.fullTrackLine = polyline;
            	}
            }else{

            	var polyline = new BMap.Polyline(points, {strokeColor:"green", strokeWeight:4, strokeOpacity:0.5});
                polyline.disableMassClear();	//禁止轨迹线在map.clearOverlays方法中被清除
                map.addOverlay(polyline);
            	mappanel.trackLine = polyline;
            }
            map.centerAndZoom(points[0], 13);
        }else{
            var polyline;
            if(isFullMap){
            	polyline = mappanel.fullTrackLine;
            }else{
            	polyline = mappanel.trackLine;
            }
//    		map.addOverlay(polyline);
//    		var overlays = map.getOverlays();
            var path = polyline.getPath();
            path.push(points[0]);
            polyline.setPath(path);
            map.removeOverlay(polyline);
//    		
            var newline = new BMap.Polyline(path, {
                strokeColor: "green", strokeWeight: 5, strokeOpacity: 10,
                // icons:[new BMap.IconSequence(
                // new BMap.Symbol(BMap_Symbol_SHAPE_BACKWARD_CLOSED_ARROW, {
                //     scale: 0.5,
                //     strokeWeight: 2,
                //     rotation: 0,
                //     fillColor: 'white',
                //     fillOpacity: 1,
                //     strokeColor:'white'
                // }),'100%','5%',false)]});
            });

            newline.disableMassClear();	//禁止轨迹线在map.clearOverlays方法中被清除
            map.addOverlay(newline);
            if(isFullMap){
            	mappanel.fullTrackLine = newline;
            }else{
            	mappanel.trackLine = newline;
            }
//            mappanel.trackLine = newline;
        }
        //      map.addOverlay(new BMap.Polyline(Ext.getCmp('mainpageView').pointArray, { strokeColor: "green", strokeWeight: 8, strokeOpacity: 8 }));
    }else if(points.length > 1 && flag == 'init'){
        map.setViewport(points, {margins:[30, 30, 30, 30]});	//视野调整的预留边距，例如： margins: [30, 20, 0, 20] 表示坐标点会限制在上述区域内
    }
}

function getFirstMessage(me) {
    var win = Ext.widget("viewMainMessage");
    Ext.getCmp('mainMessageTime').setValue(me.nextElementSibling.nextElementSibling.textContent.split('&')[0]);
    Ext.getCmp('mainMessageTitle').setValue(me.nextElementSibling.nextElementSibling.textContent.split('&')[1]);
    Ext.getCmp('mainMessageMsg').setValue(me.nextElementSibling.nextElementSibling.textContent.split('&')[2]);
    win.show();
}

function getSecondMessage(me) {
    var win = Ext.widget("viewMainMessage");
    Ext.getCmp('mainMessageTime').setValue(me.nextElementSibling.nextElementSibling.textContent.split('&')[0]);
    Ext.getCmp('mainMessageTitle').setValue(me.nextElementSibling.nextElementSibling.textContent.split('&')[1]);
    Ext.getCmp('mainMessageMsg').setValue(me.nextElementSibling.nextElementSibling.textContent.split('&')[2]);
    win.show();
}

function getThirdMessage(me) {
    var win = Ext.widget("viewMainMessage");
    Ext.getCmp('mainMessageTime').setValue(me.nextElementSibling.nextElementSibling.textContent.split('&')[0]);
    Ext.getCmp('mainMessageTitle').setValue(me.nextElementSibling.nextElementSibling.textContent.split('&')[1]);
    Ext.getCmp('mainMessageMsg').setValue(me.nextElementSibling.nextElementSibling.textContent.split('&')[2]);
    win.show();
}

function onmouseoverbacklog(me) {
    //    me.style.color = "#4e4e4e";
    me.style.color = "#0D85CA";
}

function onmouseoutbacklog(me) {
    //    me.style.color = "#0D85CA";
    me.style.color = "#4e4e4e";
}


function treeItemClassResetforAlert() {
    /*首页*/
    $("#mainPageButton").css({ "border-color": "#0d85ca", "background-color": "#0d85ca" });
    $(".app-header-toolbar-button-navigationMain").css("background", "url(resources/images/icons/manicons/icon_home_home.png) no-repeat center");
    $("#mainPageButton-btnInnerEl").css("color", "#DDDDDD");

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

function backlogOnClick(me) {
	closeMainVehMoniWins();
    var alertType = me.innerHTML.substring(0,4);
    console.log(alertType);
    var outboundflag = true,
        overspeedflag = true,
        vehiclebackflag = true;
    Ext.Ajax.request({
        url: './resource/loadUserNoPermissionMenus',
        method: 'POST',
        async: false,
        success: function(response) {
            var resp = Ext.decode(response.responseText);
            if (typeof(resp) != 'undefined') {
                var itemsNodes = Ext.getCmp('navigationTreeList').getStore().data.items;
                var navItems = Ext.ComponentQuery.query('#mainView')[0].items.items[0].items.items;
                for (var i in resp) {
                    var menu = resp[i].name;

                    if (menu == "越界报警") {
                        outboundflag = false;
                    }
                    if (menu == "超速报警") {
                        overspeedflag = false;
                    }
                    if (menu == "回车报警") {
                        vehiclebackflag = false;
                    }
                }
            }
        },
        failure: function(response) {
            Ext.Msg.alert('消息提示', '获取权限失败！');
        }
    });

    switch (alertType) {
        case '越界报警':
            if (outboundflag) {
                this.treeItemClassReset();
                var main = Ext.getCmp('main').getController();
                var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();

                treeItemClassResetforAlert();
                $("#alertMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-alertMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_abnormalwarningmgmt_pre.png) no-repeat center");
                $("#alertMgmtButton-btnInnerEl").css("color", "#fff");

                Ext.getCmp('vehiclemonitoringcontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                for (var i in navigationTreeListStore.data.items) {
                    navigationTreeListStore.data.items[i].collapse();
                }
                navigationTreeListStore.getNodeById('alertMgmt').expand();
                Ext.TaskManager.stopAll();
                main.redirectTo('outMarkerMgmt');
                window.sessionStorage.setItem("outMarkerMgmt", '1');
            } else {
                Ext.Msg.alert('消息提示', '您没有越界报警权限！');
            }
            break;
        case '超速报警':
            if (overspeedflag) {
                this.treeItemClassReset();
                var main = Ext.getCmp('main').getController();
                var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();
                treeItemClassResetforAlert();
                $("#alertMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-alertMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_abnormalwarningmgmt_pre.png) no-repeat center");
                $("#alertMgmtButton-btnInnerEl").css("color", "#fff");
                Ext.getCmp('vehiclemonitoringcontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                for (var i in navigationTreeListStore.data.items) {
                    navigationTreeListStore.data.items[i].collapse();
                }
                navigationTreeListStore.getNodeById('alertMgmt').expand();
                Ext.TaskManager.stopAll();
                main.redirectTo('overSpeedAlarm');
                window.sessionStorage.setItem("overSpeedAlarm", '1');
            } else {
                Ext.Msg.alert('消息提示', '您没有超速报警权限！');
            }
            break;
        case '回车报警':
            if (vehiclebackflag) {
                this.treeItemClassReset();
                var main = Ext.getCmp('main').getController();
                var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();

                treeItemClassResetforAlert();
                $("#alertMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-alertMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_abnormalwarningmgmt_pre.png) no-repeat center");
                $("#alertMgmtButton-btnInnerEl").css("color", "#fff");

                Ext.getCmp('vehiclemonitoringcontainer').hide();
                Ext.getCmp('maincontainerwrap').show();
                for (var i in navigationTreeListStore.data.items) {
                    navigationTreeListStore.data.items[i].collapse();
                }
                navigationTreeListStore.getNodeById('alertMgmt').expand();
                Ext.TaskManager.stopAll();
                main.redirectTo('backStationMgmt');
            } else {
                Ext.Msg.alert('消息提示', '您没有回车报警权限！');
            }
            break;
    }
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

function draw_line_direction() {
    var weight = 8;
    var icons=new BMap.IconSequence(
        new BMap.Symbol('M0 -5 L-5 -2 L0 -4 L5 -2 Z', {
            scale: weight/10,
            strokeWeight: 1,
            rotation: 0,
            fillColor: 'white',
            fillOpacity: 1,
            strokeColor:'white'
        }),'100%','5%',false);
    return icons;
}