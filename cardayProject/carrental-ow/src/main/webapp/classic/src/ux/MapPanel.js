Ext.define('Admin.ux.MapPanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.mappanel',
    overlays: [],
    markers: {},
    initComponent: function() {
        //测试配置
        var defConfig = {
            width: 1920,
            height: 1080,
            border: false,
            zoomLevel: 12,
            gmapType: 'map',
            ContextMenus: ['在此添加标注', '放大', '缩小', '放置到最大级', '查看全国'],
            markers: [{
                lat: 114.414308,
                lng: 30.483367
            }],
            mapControls: ['NavigationControl', 'ScaleControl', 'OverviewMapControl', 'MapTypeControl'],
            center: {
                lat: 114.414308,
                lng: 30.483367
            }
        };
        Ext.applyIf(this, defConfig);
        Admin.ux.MapPanel.superclass.initComponent.call(this);
    },

    afterRender: function() {
        console.log('inside afterRender function of MapPanel in ux');
        var me = this;
        uxMapPanel = this;
//         p = new BMap.Point(116.404, 39.915);
        //实例地图
        me.bmap = new BMap.Map(me.id);
        // me.bmap.centerAndZoom('上海');
        // me.bmap.centerAndZoom(p, 11);
        // me.bmap.setCurrentCity("北京");
//        me.bmap.centerAndZoom('武汉', 13);
        //设置中心位置
        me.panorama = new BMap.Panorama(me.id); 
        me.getLocation();
        //地图加载完成后加载控件和菜单
        me.bmap.addEventListener("tilesloaded", me.onMapReady());
        Admin.ux.MapPanel.superclass.afterRender.call(me);
    },
    getMap: function() {
        return this.bmap;
    },
    getMarkers: function() {
        return this.markers;
    },
    setMarkers: function(markers) {
        this.markers = markers;
    },
    onMapReady: function() {
        console.log("on map ready");
        var me = this;
        // me.getMap().setZoom(4);
        //添加地图控件
        //me.addMapControls();
        //添加标注
        me.addMarkers(me.markers);
        console.log(me.markers);
        //添加右击菜单
        me.addContextMenus();
        // 百度api demo--鼠标实例--鼠标绘制点线面
        me.getMap().enableScrollWheelZoom();
    },
    onMarkerDragend: function(type, target, pixel, point) {
        console.log("onMarkerDragend");
        console.log(type, target, pixel, point);
    },
    onMarkerClick: function(type, target) {
        console.log("onMarkerClick");
        console.log(type, target);
    },
    addMapControls: function() {
        var me = this;
        //根据配置设置地图默认控件
        if (me.gmapType === 'map') {
            if (Ext.isArray(me.mapControls)) {
                for (var i = 0; i < me.mapControls.length; i++) {
                    me.addMapControl(me.mapControls[i]);
                }
            } else if (typeof me.mapControls === 'string') {
                me.addMapControl(me.mapControls);
            } else if (typeof me.mapControls === 'object') {
                me.getMap().addControl(me.mapControls);
            }
        }
    },
    addMapControl: function(mc) {
        //Check是否是百度地图的事件
        var mcf = BMap[mc];
        if (typeof mcf === 'function') {
            this.getMap().addControl(new mcf());
        }
    },
    addContextMenus: function() {
        var me = this,
            contextMenu = new BMap.ContextMenu();
        //根据配置设置地图右击菜单
        if (Ext.isArray(me.ContextMenus)) {
            for (i = 0; i < me.ContextMenus.length; i++) {
                contextMenu.addItem(me.addContextMenu(me.ContextMenus[i]));
            }
        }
        me.bmap.addContextMenu(contextMenu);
    },
    addContextMenu: function(Menu) {
        var me = this,
            map = me.getMap();
        if (typeof Menu === 'string') {
            switch (Menu) {
                case '放大':
                    return new BMap.MenuItem(Menu, function() {
                        map.zoomIn();
                    }, 100);
                case '缩小':
                    return new BMap.MenuItem(Menu, function() {
                        map.zoomOut();
                    }, 100);
                case '放置到最大级':
                    return new BMap.MenuItem(Menu, function() {
                        map.setZoom(18);
                    }, 100);
                case '查看全国':
                    return new BMap.MenuItem(Menu, function() {
                        map.setZoom(4);
                    }, 100);
                case '在此添加标注':
                    return new BMap.MenuItem(Menu, function(p) {
                        var marker = new BMap.Marker(p),
                            px = map.pointToPixel(p);
                        map.addOverlay(marker);
                        //启动标注可移动
                        marker.enableDragging(true);
                        //绑定click事件
                        marker.addEventListener("click", me.onMarkerClick);
                        marker.addEventListener("dragend", me.onMarkerDragend);
                    }, 100);
            }
        }
    },
    addMarkers: function(markers) {
        if (Ext.isArray(markers)) {
            for (var i = 0; i < markers.length; i++) {
                var mkr_point = new BMap.Point(markers[i].lat, markers[i].lng);
                var markerS = new BMap.Marker(mkr_point);
                markerS.enableDragging(true);
                this.addMarker(mkr_point, markerS, false, true, markers[i].listeners);
            }
        }
    },
    addMarker: function(point, markerS, clear, center, listeners) {
        var me = this;
        if (clear === true) {
            me.getMap().clearOverlays();
        }
        if (center === true) {
            me.getMap().setCenter(point, me.zoomLevel);
        }
        if (typeof listeners === 'object') {
            for (evt in listeners) {
                markerS.addEventListener(evt, listeners[evt]);
            }
        }
        me.getMap().addOverlay(markerS);
    },
    /**
     * 清除覆盖物
     */
    clearAll: function() {
        var me = this,
            map = me.map;
        var overlays = me.overlays;
        for (var i = 0; i < overlays.length; i++) {
            map.removeOverlay(overlays[i]);
        }
        me.overlays.length = 0;
        map.removeOverlay(me.myPolygon);
        me.myPolygon = '';
    },

    /**
     * HTML5采集用户地理位置方法
     * @return {[type]} [description]
     */
    getLocation: function() {
        var options = {
            enableHighAccuracy: true,
            maximumAge: 1000
        }
        if (navigator.geolocation) {
            //浏览器支持geolocation
            navigator.geolocation.getCurrentPosition(this.onSuccess, this.onError, options);

        } else {
            //浏览器不支持geolocation
            // alert('浏览器不支持自动定位geolocation');
        }
    },

    //成功时
    onSuccess: function(position) {
        //返回用户位置
        //经度
        var longitude = position.coords.longitude;
        //纬度
        var latitude = position.coords.latitude;

        //使用百度地图API
        //创建一个坐标
        var point = new BMap.Point(longitude, latitude);
        //地图初始化，设置中心点坐标和地图级别 
        uxMapPanel.getMap().centerAndZoom(point, 15);
    },


    //失败时
    onError: function(error) {
        switch (error.code) {
            case 1:
                console.log("位置服务被拒绝");
                break;

            case 2:
                console.log("暂时获取不到位置信息");
                break;

            case 3:
                console.log("获取信息超时");
                break;

            case 4:
                console.log("未知错误");
                break;
        }
    },
});
