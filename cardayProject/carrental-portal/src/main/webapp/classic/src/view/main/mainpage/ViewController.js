var taskOnMessage;
var taskOnActiveVehicle;
//车辆统计在线离线状态
function loadVehicleStatus() {
    var navigationMainPageTree = Ext.getCmp('navigationMainPageTree');
    var idselect = navigationMainPageTree.selectItemId;
    var deptId = '';
    if (idselect != null) {
        var itemSection = navigationMainPageTree.getStore().getNodeById(idselect); //点击的节点
        //如果是企业节点
        if (itemSection.data.level == '1') {
            deptId = '';
        } else if (itemSection.data.level == '2') { //如果是部门节点
            deptId = itemSection.data.deptId;
        } else if (itemSection.data.level == '3') {
            deptId = itemSection.parentNode.data.deptId
        }
    }
    if (deptId == undefined) {
        deptId = '';
    }
    var input = {
        'deptId': deptId
    };
    var json = Ext.encode(input);

    Ext.Ajax.request({
        url: './usage/report/findVehicleStatus',
        method: 'POST',
        params: { 'json': json },
        success: function(response) {
//            response = '{"data":{"totalNumber":28,"onLineNumber":7,"drivingNumber":7,"stopNumber":0,"offLineNumber":21},"status":"success"}';
//            var respText = Ext.util.JSON.decode(response);
            var respText = Ext.util.JSON.decode(response.responseText);
            console.log(JSON.stringify(respText));
            if (respText.data != null) {
                var totalNumber = respText.data.totalNumber;
                var onLineNumber = respText.data.onLineNumber;
                var drivingNumber = respText.data.drivingNumber;
                var stopNumber = respText.data.stopNumber;
                var offLineNumber = respText.data.offLineNumber;
                $("#mainPageTable th")[0].innerHTML = '车辆总数 ' + totalNumber;
                $("#mainPageTable th")[1].innerHTML = '在线 ' + onLineNumber;
                $("#mainPageTable th")[2].innerHTML = '行驶 ' + drivingNumber;
                $("#mainPageTable th")[3].innerHTML = '停止 ' + stopNumber;
                $("#mainPageTable th")[4].innerHTML = '离线 ' + offLineNumber;
            }
        },
        failure: function(response) {
            console.log('AllVehs');
        }
    });
}
//每辆车的实时位置
function loadVehiclePosition(panel, map, flag) {
    //拿到存入panel中车牌数组
    var name = panel.vehNum;
    var name2 = decodeURIComponent(name);
    Ext.Ajax.request({
        url: './vehicle/queryObdLocationByName',
        method: 'POST',
        params: { name: name2 },
        success: function(response) {
  	        var respText = Ext.util.JSON.decode(response.responseText);
            var data = respText.data.obdLocationList;
            //全屏显示地图，保留数据
            var mainpageView = Ext.getCmp('mainpageView');
            mainpageView.mapdata = data;
            var navigationMainPageTree = Ext.getCmp('navigationMainPageTree');
            var idselect = navigationMainPageTree.selectItemId;
            if (idselect != null && idselect.indexOf('dashboard') != 0) {
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
                onAfterRenderPanelTrackingMap(data, map, flag);
            } else {
                function myFun(result) {
                    var cityName = result.name;
                    console.log('初始化地图--城市名称:' + cityName);
                    map.setCenter(cityName);
                    map.centerAndZoom(cityName, 12);
                }
                var myCity = new BMap.LocalCity();
                myCity.get(myFun);
            }
        },
        failure: function(response) {
            console.log('error');
        }
    });
}

function onAfterRenderPanelTrackingMap(data, map, flag) {
//    if (data.length == 1) {
//        var mainpageView = Ext.getCmp('mainpageView');
//        mainpageView.pointArray.push(new BMap.Point(data[0].longitude, data[0].latitude));
//    }
    map.clearOverlays();


    var points = new Array();
    var point;
    var opts = {
        width: 250, // 信息窗口宽度
        height: 170, // 信息窗口高度
        //              title : "信息窗口" , // 信息窗口标题
        enableMessage: true //设置允许信息窗发送短息
    };

    // 编写自定义函数,创建标注
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
            var content = '车牌：' + vehiclenumber + '<br/>时间：' + tracetime + '<br/>状态：' + status + '<br/>经度：' + point.lng + '<br/>纬度：' + point.lat + '<br/>方向：' + getDirection(bearing, point.lng, point.lat) + '<br/>速度：约' + speed + 'km/h' + '<br/>地址：' + address + '<br/>司机姓名：' + realname + '<br/>司机手机号：' + phone;
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
        var infoWindow = new BMap.InfoWindow(content, opts); // 创建信息窗口对象 
        map.openInfoWindow(infoWindow, point); //开启信息窗口
    }
   //var mappanel = Ext.getCmp("mainpageView").down("mappanel");
    var mappanel = Ext.getCmp("mainpageView");
    var fullscreen = Ext.getCmp("viewFullScreenId");
    var fullmap = null;
    if(fullscreen != null){
    	var fullmappanel = fullscreen.down("mappanel");
    	fullmap = fullmappanel.map;
    }
    if (points.length == 1) {
        if (flag == 'init') {
            var polyline = new BMap.Polyline(points, {strokeColor:"green", strokeWeight:4, strokeOpacity:0.5}); 
            polyline.disableMassClear();	//禁止轨迹线在map.clearOverlays方法中被清除
            map.addOverlay(polyline); 
        	mappanel.trackLine = polyline;
            map.centerAndZoom(points[0], 13);
        }else{
    		var polyline = mappanel.trackLine;
//    		map.addOverlay(polyline);
//    		var overlays = map.getOverlays();
    		var path = polyline.getPath();
    		path.push(points[0]);
    		polyline.setPath(path);
    		map.removeOverlay(polyline);
    		var newline = new BMap.Polyline(path, {strokeColor:"green", strokeWeight:4, strokeOpacity:0.5}); 
    		newline.disableMassClear();	//禁止轨迹线在map.clearOverlays方法中被清除
            map.addOverlay(newline); 
        	mappanel.trackLine = newline;
    	}
    }else if(points.length > 1 && flag == 'init'){
        map.setViewport(points);
	}
}
/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.main.mainpage.ViewController', {
    extend: 'Ext.app.ViewController',
    requires: [
        'Ext.window.MessageBox'
    ],

    undealPanelRender: function(backlogPanel) {
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
                    backlogPanel.getViewModel().set('outbound', outbound);
                    backlogPanel.getViewModel().set('overspeed', overspeed);
                    backlogPanel.getViewModel().set('vehicleback', vehicleback);
                }
            }
        });
    },

    messagePanelRender: function() {
        var messagePanel = Ext.getCmp('messagePanel');
        console.log('Systems Message in' + new Date());
        Ext.Ajax.request({
            url: 'sysMessage/getLatestSystemMessage',
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'charset': 'UTF-8' },
            success: function(res) {
                var appendData = Ext.JSON.decode(res.responseText);
                if (appendData.status == 'success' && appendData.data != null) {
                    var html = '<ul class="messageContent">';
                    var titleOne, titleTwo, titleThree;
                    if (appendData.data[0] != undefined) {
                        if (appendData.data[0].title.length > 20) {
                            titleOne = appendData.data[0].title.substring(0, 20) + '...';
                        } else {
                            titleOne = appendData.data[0].title;
                        }
                        messagePanel.getViewModel().set('systemMessageOne', titleOne);
                        messagePanel.getViewModel().set('systemMessageOneTime', appendData.data[0].time.split(" ")[0]);
                        messagePanel.getViewModel().set('systemMessageOneContent', appendData.data[0].time + '&' + appendData.data[0].title + '&' + appendData.data[0].msg);
                    }
                    var systemMessageOne = titleOne;
                    var systemMessageOneTime = appendData.data[0].time.split(" ")[0];
                    var systemMessageOneContent = appendData.data[0].time + '&' + appendData.data[0].title + '&' + appendData.data[0].msg;
                    html += '<li><a onclick="getFirstMessage(this)" >' + systemMessageOne + '</a><span>' + systemMessageOneTime + '</span><p hidden="hidden">' + systemMessageOneContent + '</p></li>';

                    if (appendData.data[1] != undefined) {
                        if (appendData.data[1].title.length > 20) {
                            titleTwo = appendData.data[1].title.substring(0, 20) + '...';
                        } else {
                            titleTwo = appendData.data[1].title;
                        }
                        messagePanel.getViewModel().set('systemMessageTwo', titleTwo);
                        messagePanel.getViewModel().set('systemMessageTwoTime', appendData.data[1].time.split(" ")[0]);
                        messagePanel.getViewModel().set('systemMessageTwoContent', appendData.data[1].time + '&' + appendData.data[1].title + '&' + appendData.data[1].msg);

                        var systemMessageTwo = titleTwo;
                        var systemMessageTwoTime = appendData.data[1].time.split(" ")[0];
                        var systemMessageTwoContent = appendData.data[1].time + '&' + appendData.data[1].title + '&' + appendData.data[1].msg;
                        html += '<li><a onclick="getFirstMessage(this)" >' + systemMessageTwo + '</a><span>' + systemMessageTwoTime + '</span><p hidden="hidden">' + systemMessageTwoContent + '</p></li>';

                    }


                    if (appendData.data[2] != undefined) {
                        if (appendData.data[2].title.length > 20) {
                            titleThree = appendData.data[2].title.substring(0, 20) + '...';
                        } else {
                            titleThree = appendData.data[2].title;
                        }
                        messagePanel.getViewModel().set('systemMessageThree', titleThree);
                        messagePanel.getViewModel().set('systemMessageThreeTime', appendData.data[2].time.split(" ")[0]);
                        messagePanel.getViewModel().set('systemMessageThreeContent', appendData.data[2].time + '&' + appendData.data[2].title + '&' + appendData.data[2].msg);

                        var systemMessageThree = titleThree;
                        var systemMessageThreeTime = appendData.data[2].time.split(" ")[0];
                        var systemMessageThreeContent = appendData.data[2].time + '&' + appendData.data[2].title + '&' + appendData.data[2].msg;
                        html += '<li><a onclick="getFirstMessage(this)" >' + systemMessageThree + '</a><span>' + systemMessageThreeTime + '</span><p hidden="hidden">' + systemMessageThreeContent + '</p></li>';

                    }
                    html += "</ul>";
                    Ext.getCmp("messagePanel").setHtml(html);
                } else {
                    var html = '<div style="color:gray; font-size:13px; padding:10px; ">暂无公告！</div>';
                    Ext.getCmp("messagePanel").setHtml(html);
                }
            }
        });
    },

    afterrenderMap: function(panel, eOpts) {
        //代办事项
        var backlogPanel = this.lookupReference('backlogPanel');
        this.undealPanelRender(backlogPanel);

        //报警信息
        var alarmGrid = this.lookupReference('gridAlarmInfo');
        alarmGrid.getViewModel().getStore("AlertInfo").load();

        //拿到地图
        var map = this.lookupReference('bmappanelMainPage').bmap;
        this.initMap(map);
        loadVehicleStatus();
        loadVehiclePosition(panel, map, 'init');
        
        //定时器
        var clockStart = function(panel, map) {
            loadVehicleStatus();
            loadVehiclePosition(panel, map, 'noInit');
            
//          var fullscreen = Ext.getCmp("viewFullScreenId");
//	        if(fullscreen != null){
//	        	var fullmap = fullscreen.down("mappanel").bmap;
//	        	loadVehiclePosition(panel, fullmap, 'noInit');
//	        }
        }

        var i = 10;

        var runByTime = function() {
            console.log("runByTime: " + new Date() + ' : ' + i);
            if (i > 1) {
                i--;
            } else {
                clockStart(panel, map);
                i = 10;
            }
            $("#mainPageTable th")[5].innerHTML = '地图将在' + i + '秒后刷新';
        };
        /*循环刷新首页地图和公告消息*/
        if (taskOnMessage || taskOnActiveVehicle) {
            Ext.TaskManager.stopAll();
        }

        /*taskOnMessage = Ext.TaskManager.start({
            run: this.messagePanelRender,
            interval: 30000,
            repeat: 120,
            scope: this
        });

        taskOnActiveVehicle = Ext.TaskManager.start({
            run: function() { runByTime() },
            interval: 1000
        });*/
    },

    afterrenderMapWindow: function() {
        console.log('全屏地图');
        var mainpageView = Ext.getCmp('mainpageView');
        var map = this.lookupReference('bmappanelMainPage');
        map.width = document.documentElement.clientWidth;
        map.height = document.documentElement.clientHeight;

        //        map.bmap.width = document.documentElement.clientWidth;
        //        map.bmap.height = document.documentElement.clientHeight;
        map.bmap.enableAutoResize();
        var viewFullScreen = this.lookupReference('bmappanelMainPage').up().up();
        viewFullScreen.width = document.documentElement.clientWidth;
        viewFullScreen.height = document.documentElement.clientHeight;

        this.initFullScreenMap(map.bmap);
        var bmap = Ext.getCmp('viewFullScreenId').down().down().bmap;
        var data = mainpageView.mapdata;
        console.log("data:" + JSON.stringify(data));
        if (data != null && data.length > 0) {
        	if(data.length == 1){
            	bmap.centerAndZoom(new BMap.Point(data[0].longitude, data[0].latitude), 13);
//            	var polyline = Ext.getCmp("mainpageView").down("mappanel").trackLine;
//            	bmap.addOverlay(polyline);
        	}
            onAfterRenderPanelTrackingMap(data, bmap,'init');
        } else {
            function myFun(result) {
                var cityName = result.name;
                console.log('initView:' + cityName);
                map.bmap.setCenter(cityName);
                map.bmap.centerAndZoom(cityName, 12);
            }
            var myCity = new BMap.LocalCity();

            myCity.get(myFun);
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
                    Ext.getCmp('viewFullScreenId').close();
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
            // 设置样式
            zoomMax.style.background = "url(resources/images/icons/manicons/icon_home_map_unfold.png) no-repeat center";
            zoomMax.style.width = "60px",
                zoomMax.style.height = "60px",
                // 绑定事件，点击全屏地图
                zoomMax.onclick = function(e) {
                    var win = Ext.widget("viewFullScreen");
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
            var marginRight = 0;
            //由于没摸清楚中间地图部分的宽度为什么在1600和1280下是一样的，在1920下又不一样，所以先固定控件marginRight
            if (dWidth < 1900) {
                marginRight = 790;
            } else {
                marginRight = 1020;
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

    onBeforeLoad: function() {
        var frmValues = {};
        var page = Ext.getCmp('alertPage').store.currentPage;
        var limit = Ext.getCmp('alertPage').pageSize;
        frmValues.currentPage = page;
        frmValues.numPerPage = limit;
        this.getViewModel().getStore("AlertInfo").proxy.extraParams = {
            "json": Ext.encode(frmValues)
        }
    },
    onSearchClick: function() {
        var VehicleStore = Ext.getCmp('gridAlarmInfo').getStore();
        VehicleStore.currentPage = 1;
        this.getViewModel().getStore("AlertInfo").load();
    },

    itemGridAlarmInfoClick: function(recode, item, index) {
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

                    Ext.getCmp('mainpagecontainer').hide();
                    Ext.getCmp('maincontainerwrap').show();
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

                    Ext.getCmp('mainpagecontainer').hide();
                    Ext.getCmp('maincontainerwrap').show();
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

                    Ext.getCmp('mainpagecontainer').hide();
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
    },

    treeItemClassReset: function() {
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

});

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
    var alertType = me.innerHTML.split(':')[0];
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
                var main = Ext.getCmp('main').getController();
                var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();

                treeItemClassResetforAlert();
                $("#alertMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-alertMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_abnormalwarningmgmt_pre.png) no-repeat center");
                $("#alertMgmtButton-btnInnerEl").css("color", "#fff");

                Ext.getCmp('mainpagecontainer').hide();
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
                var main = Ext.getCmp('main').getController();
                var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();

                treeItemClassResetforAlert();
                $("#alertMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-alertMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_abnormalwarningmgmt_pre.png) no-repeat center");
                $("#alertMgmtButton-btnInnerEl").css("color", "#fff");

                Ext.getCmp('mainpagecontainer').hide();
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
                var main = Ext.getCmp('main').getController();
                var navigationTreeListStore = Ext.getCmp('navigationTreeList').getStore();

                treeItemClassResetforAlert();
                $("#alertMgmtButton").css({ "border-color": "#86C2E4", "background-color": "#86C2E4" });
                $(".app-header-toolbar-button-alertMgmt").css("background", "url(resources/images/icons/manicons/icon_nav_abnormalwarningmgmt_pre.png) no-repeat center");
                $("#alertMgmtButton-btnInnerEl").css("color", "#fff");

                Ext.getCmp('mainpagecontainer').hide();
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

function getDirection(bearing, longitude, latitude){
	if(bearing == null || bearing == undefined){
		return "--";
	}
	if(longitude == 0 && latitude == 0){
		return "--";
	}
	var bearing = parseInt(bearing);
	var direction = "--";
	switch(bearing){
	case 0:
		direction = "正北";
		break;
	case 90:
		direction = "正东";
		break;
	case 180:
		direction = "正南";
		break;
	case 270:
		direction = "正西";
		break;
	case 360:
		direction = "正北";
		break;
	default:
		if(bearing > 0 && bearing < 90){
			direction = "东北";
		}else if(bearing > 90 && bearing < 180){
			direction = "东南";
		}else if(bearing > 180 && bearing < 270){
			direction = "西南";
		}else if(bearing > 270 && bearing < 360){
			direction = "西北";
		}
	}
	return direction;
}

