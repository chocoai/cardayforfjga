/**
 * This class is the template view for the application.
 */
var localSearchdiOfStrict;
Ext.define('Admin.view.vehiclemgmt.stationmgmt.ViewController', {
    extend : 'Ext.app.ViewController',
    requires : [
        'Ext.window.MessageBox',
        'Admin.view.vehiclemgmt.stationmgmt.View',
        'Admin.view.vehiclemgmt.stationmgmt.SearchForm',
        'Admin.view.vehiclemgmt.stationmgmt.AddStation',
        'Admin.view.vehiclemgmt.stationmgmt.EditStation',
        'Admin.view.vehiclemgmt.stationmgmt.ViewStation'
    ],
    alias : 'controller.stationmgmtcontroller',


    onSearchClick : function(it, e) {
        Ext.Msg.alert("Message Box", "查询按钮");
    },

    onBeforeLoad : function() {
        var frmValues = this.lookupReference('searchForm').getValues();
        var page=Ext.getCmp('stationPage').store.currentPage;
        var limit=Ext.getCmp('stationPage').pageSize;
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
            "stationName":frmValues.stationName,
        };
        var pram = Ext.encode(input);
        this.getViewModel().getStore("stationsResults").proxy.extraParams = {
            "json" : pram
        }
        //this.getViewModel().getStore("stationsResults").load();
    },

    //根据用户的输入的站点名称查询
    searchByStationName :　function() {
        var VehicleStore = this.lookupReference('gridStation').getStore();
        VehicleStore.currentPage = 1;
        this.getViewModel().getStore("stationsResults").load();
    },

    //打开新增站点窗口
    onAddClick : function() {
        //Ext.Msg.alert("Message Box", "添加按钮");
        win = Ext.widget('addStation');
        win.show();
        this.renderPositionInfoOnMap();

    },

    //完成新增站点
    addStationDone : function(btn) {
        var myMask = new Ext.LoadMask({
            msg    : '请稍后，正在添加站点........',
            target : this.getView()
        });

        //organizationId,resourceIds,station,description
        var stationInfo = this.getView().down('form').getForm().getValues();
        var carNumber = stationInfo.carNumber;
        var r = /^\+?[1-9][0-9]*$/;　　//正整数
        if(carNumber != '' && !r.test(carNumber)) {
            Ext.Msg.alert('消息提示','停车位数量只能输入正整数！');
            return;
        }
		/*if(isNaN(carNumber)){
		 Ext.Msg.alert('消息提示','停车位数量只能输入数字！');
		 return;
		 }*/

        myMask.show();
/*        if(carNumber == ''){
            carNumber = '0';
        }*/
        //var organizationId = userInfo.organizationId.toString();
        var areaId = Ext.getCmp('addStationAreaCmb').getValue();
        var city = Ext.getCmp('addStationCityCmb').getRawValue();
        var input = {
            'organizationId': stationInfo.organizationId,
            'stationName': stationInfo.stationName,
            'city': city,
            'areaId': areaId,
            'position': stationInfo.position,
            'longitude': stationInfo.longitude,
            'latitude': stationInfo.latitude,
            'radius': stationInfo.radius,
            'carNumber': carNumber,
            'startTime': stationInfo.startTime,
            'endTime': stationInfo.endTime,
        };
        var json = Ext.encode(input);

        if (this.getView().down('form').getForm().isValid()) {

            var map = Ext.getCmp("stationmapdis_bmappanel").bmap;

            var local = new BMap.LocalSearch(map, { //智能搜索
                onSearchComplete: function(){
                    var flag = false;
                    if(local.getResults().getPoi(0) == undefined){
                        Ext.Msg.alert('消息提示','无法此解析地址，请重新输入');
                        Ext.getCmp('addStation').down('form').getForm().findField('positionStationId').setValue('');
                        Ext.getCmp('addStation').down('form').getForm().findField('radius').setValue('');
                        myMask.hide();
                        return ;
                    }else{
                        var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
                        var point = new BMap.Point(pp.lng,pp.lat);
                        var bdary = new BMap.Boundary();
                        bdary.get(Ext.getCmp('addStationCityCmb').getRawValue() + Ext.getCmp('addStationAreaCmb').getRawValue(), function(rs){       //获取行政区域
                            var count = rs.boundaries.length; //行政区域的点有多少个
                            if (count === 0) {
                                Ext.Msg.alert('消息提示','未能获取当前输入行政区域');
                                myMask.hide();
                                return ;
                            }
                            for (var i = 0; i < count; i++) {
                                var ply = new BMap.Polygon(rs.boundaries[i]); //建立多边形覆盖物
                                if(BMapLib.GeoUtils.isPointInPolygon(point,ply)){
                                    flag = true;
                                    break;
                                }
                            }
                            if(!flag){
                                Ext.Msg.alert('消息提示', '此位置不在 ' + Ext.getCmp('addStationAreaCmb').getRawValue() + ' 内！请重新输入！');
                                Ext.getCmp('addStation').down('form').getForm().findField('positionStationId').setValue('');
                                Ext.getCmp('addStation').down('form').getForm().findField('radius').setValue('');
                                myMask.hide();
                                return ;
                            }else{
                                Ext.Ajax.request({
                                    url : 'station/create',
                                    method : 'POST',
                                    params:{json:json},
                                    success : function(response,options) {
                                        var respText = Ext.util.JSON.decode(response.responseText);
                                        var retStatus = respText.status;
                                        var data = respText.data;
                                        if (retStatus == 'success' && data != "") {
                                            btn.up('addStation').close();
                                            Ext.Msg.alert('提示信息','添加站点成功');
                                            Ext.getCmp("gridStation").getStore('stationsResults').load();
                                        }else{
                                            btn.up('addStation').close();
                                            Ext.Msg.alert('消息提示','新增站点失败！');
                                            Ext.getCmp("gridStation").getStore('stationsResults').load();
                                        }
                                    }
//								    ,
//							        failure : function() {
//										btn.up('addStation').close();
//							            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//							        }
                                });
                            }
                        });
                    }
                }
            });
            local.search(Ext.getCmp('positionStationId').getValue());
        }else{
            Ext.Msg.alert('消息提示','站点信息有误，请重新输入！');
            myMask.hide();
        }
    },

    //打开站点信息修改,根据id查询站点信息
    editStation : function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex);
        var win = Ext.widget("editStation");
        win.down("form").loadRecord(rec);
        win.show();
        this.renderPositionInfoOnMap(rec.data.position);
        this.viewStationMapinfo(rec);
    },

    //完成站点信息修改
    editStationDone : function(btn) {
        //Ext.Msg.alert('修改站点','修改站点信息');
        var myMask = new Ext.LoadMask({
            msg    : '请稍后，正在修改站点........',
            target : this.getView()
        });
//        myMask.show(); 
        var stationInfo = this.getView().down('form').getForm().getValues();
        var assignedVehicleNumber = stationInfo.assignedVehicleNumber;
        var carNumber = stationInfo.carNumber;
        var r = /^\+?[1-9][0-9]*$/;　　//正整数
        if(carNumber != '' && !r.test(carNumber)) {
            Ext.Msg.alert('消息提示','停车位数量只能输入正整数！');
            return;
        }
		/*if(isNaN(carNumber)){
		 Ext.Msg.alert('消息提示','停车位数量只能输入数字！');
		 return;
		 }*/
        myMask.show();
/*        if(carNumber == ''){
            carNumber = '0';
        }*/

        if(carNumber == '' && parseInt(assignedVehicleNumber) > 0){
            Ext.Msg.alert('消息提示','已被分配车辆，停车位数量不能小于 '+ assignedVehicleNumber +' ！');
            myMask.hide();
        }else if(parseInt(carNumber) <  parseInt(assignedVehicleNumber)){
            Ext.Msg.alert('消息提示','停车位数量不少于已分配车辆数'+ assignedVehicleNumber +'辆！');
            myMask.hide();
        }else{
            var city =  Ext.getCmp('editStationCityCmb').getRawValue();
            var areaId =  Ext.getCmp('editStationAreaCmb').getValue();
            console.log('areaId:' + areaId);
            var input = {
                'id': stationInfo.id,
                'organizationId': stationInfo.organizationId,
                'stationName': stationInfo.stationName,
                'city': city,
                'areaId': areaId,
                'position': stationInfo.position,
                'longitude': stationInfo.longitude,
                'latitude': stationInfo.latitude,
                'radius': stationInfo.radius,
                'carNumber': carNumber,
                'startTime': stationInfo.startTime,
                'endTime': stationInfo.endTime,
            };

            var json = Ext.encode(input);
            if (this.getView().down('form').getForm().isValid()) {
                var map = Ext.getCmp("stationmapdis_bmappanel").bmap;

                var local = new BMap.LocalSearch(map, { //智能搜索
                    onSearchComplete: function(){
                        var flag = false;
                        if(local.getResults().getPoi(0) == undefined){
                            Ext.Msg.alert('消息提示','无法此解析地址，请重新输入');
                            Ext.getCmp('edit_station_id').down('form').getForm().findField('positionStationId').setValue('');
                            Ext.getCmp('edit_station_id').down('form').getForm().findField('radius').setValue('');
                            myMask.hide();
                            return ;
                        }else{
                            var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
                            var point = new BMap.Point(pp.lng,pp.lat);
                            var bdary = new BMap.Boundary();
                            bdary.get(Ext.getCmp('editStationCityCmb').getRawValue() + Ext.getCmp('editStationAreaCmb').getRawValue(), function(rs){       //获取行政区域
                                var count = rs.boundaries.length; //行政区域的点有多少个
                                if (count === 0) {
                                    Ext.Msg.alert('消息提示','未能获取当前输入行政区域');
                                    return ;
                                }
                                for (var i = 0; i < count; i++) {
                                    var ply = new BMap.Polygon(rs.boundaries[i]); //建立多边形覆盖物
                                    if(BMapLib.GeoUtils.isPointInPolygon(point,ply)){
                                        flag = true;
                                        break;
                                    }
                                }
                                if(!flag){
                                    Ext.Msg.alert('消息提示', '此位置不在 ' + Ext.getCmp('editStationAreaCmb').getRawValue() + ' 内！请重新输入！');
                                    Ext.getCmp('edit_station_id').down('form').getForm().findField('positionStationId').setValue('');
                                    Ext.getCmp('edit_station_id').down('form').getForm().findField('radius').setValue('');
                                    myMask.hide();
                                    return ;
                                }else{
                                    Ext.Ajax.request({
                                        url: 'station/update',
                                        method : 'POST',
                                        params:{json:json},
                                        success : function(response,options) {
                                            var respText = Ext.util.JSON.decode(response.responseText);
                                            var retStatus = respText.status;var data = respText.data;
                                            if (retStatus == 'success' && data != "") {
                                                btn.up('editStation').close();
                                                Ext.Msg.alert('提示信息','修改站点成功');
                                                Ext.getCmp("gridStation").getStore('stationsResults').load();
                                            }else{
                                                btn.up('editStation').close();
                                                Ext.Msg.alert('消息提示','修改站点失败！');
                                                Ext.getCmp("gridStation").getStore('stationsResults').load();
                                            }
                                        },
//								        failure : function() {
//											btn.up('editStation').close();
//								            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//								        },
                                        scope:this
                                    });
                                }
                            });
                        }
                    }
                });
                local.search(Ext.getCmp('positionStationId').getValue());


            }else{
                Ext.Msg.alert('消息提示','站点信息有误，请重新输入！');
                myMask.hide();
            }
        }
    },

    //查看站点信息
    viewStation : function(grid, rowIndex, colIndex) {
        var rec = grid.getStore().getAt(rowIndex);
        var win = Ext.widget("viewStation");
        //console.dir(win.down("form"));
        win.down("form").loadRecord(rec);
        Ext.getCmp('radiusLocation').setValue(rec.data.radius + '公里');
        win.show();

        this.viewStationMapinfo(rec);

    },

    viewStationMapinfo: function(rec){
        var map = Ext.getCmp('stationmapdis_bmappanel').bmap;
        this.initMap(map);
        var point = new BMap.Point(rec.data.longitude, rec.data.latitude);
        map.centerAndZoom(point, 15);
        var marker = new BMap.Marker(point);
        var circle = new BMap.Circle(point,rec.data.radius * 1000,{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
        map.addOverlay(marker);
        map.addOverlay(circle);

        var content = "<table>";
        content = content + "<tr class=\"window-table\"><td>城市：" + rec.data.city + "</td></tr>";
        content = content + "<tr class=\"window-table\"><td>地址：" + rec.data.position + "</td></tr>";
        content = content + "<tr class=\"window-table\"><td>纬度：" + rec.data.longitude + "</td></tr>";
        content = content + "<tr class=\"window-table\"><td>经度：" + rec.data.latitude + "</td></tr>";
        content += "</table>";

        (function () {
            var infoWindow = new BMap.InfoWindow(content);
            marker.addEventListener("click", function () {
                this.openInfoWindow(infoWindow);
            });
        })();
    },

    //删除
    deleteStation : function(grid, rowIndex, colIndex) {
        Ext.Msg.confirm('消息提示','确定要删除吗！！！',function(btn){
            if (btn == 'yes') {
                var stationID = grid.getStore().getAt(rowIndex).id;
                var url = 'station/'+stationID+'/delete';

                Ext.Ajax.request({
                    url: url,
                    method : 'POST',
                    defaultHeaders : {'Content-type' : 'application/json;utf-8'},
                    success : function(response,options) {
                        var respText = Ext.util.JSON.decode(response.responseText);
                        var	retStatus = respText.status;
                        if (retStatus == 'success') {
                            //删除成功后，刷新页面
                            Ext.getCmp("gridStation").getStore('stationsResults').load();
                        }
                    },
//			        failure : function() {
//			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			        },
                    scope:this
                });
            }
        });
    },
    //打开给站点分配车辆窗口，显示当前站点可分配车辆
    assignVehicle:function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex);
        if(rec.data.carNumber == ''){
            Ext.Msg.alert('消息提示','此站点无停车位，不可分配车辆！');
        }else{
            var win = Ext.widget("assignVehicle", {
                title: '车辆管理',
                closable: false,
                buttonAlign : 'center',
                stationId: rec.data.id,
                stationName: rec.data.stationName,
                carNumber: rec.data.carNumber,
                buttons : [{
                    text : '返回',
                    handler: 'toBackStationView'
                }]
            });
            win.show();
        }
    },
    //返回站点管理页面
    toBackStationView: function() {
        console.log('to back station view');
        var VehicleStore = Ext.getCmp('stationmgmt').getViewModel().getStore('stationsResults');
        VehicleStore.currentPage = 1;
        Ext.getCmp('stationmgmt').getViewModel().getStore("stationsResults").load();
        this.view.close();
    },
    loadStationVehicle: function() {
        var stationId = Ext.getCmp("assignVehicle").stationId;
        var page=Ext.getCmp('assignVehiclePage').store.currentPage;
        var limit=Ext.getCmp('assignVehiclePage').pageSize;
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
            "stationId":stationId,
        };
        var pram = Ext.encode(input);
        Ext.getCmp("assignedVehicleGrid").getViewModel().getStore("stationVehicleAssignedStore").proxy.extraParams = {
            "json" : pram
        };

    },

    onAfterLoadStationVehicle: function(){
        var avialiableVehicleStore = Ext.getCmp('assignVehiclePage').store;
        avialiableVehicleStore.currentPage = 1;
        Ext.getCmp("assignedVehicleGrid").getViewModel().getStore("stationVehicleAssignedStore").load();
    },

    loadStationAvialiableVehicle: function() {

        var stationId = Ext.getCmp("addVehicleToStation").stationId;
        var page=Ext.getCmp('addVehicleToStationPage').store.currentPage;
        var limit=Ext.getCmp('addVehicleToStationPage').pageSize;
        var input = {
            "currentPage" : page,
            "numPerPage" : limit,
            "stationId":stationId,
        };
        var pram = Ext.encode(input);
        Ext.getCmp("addVehicleToStationView").getViewModel().getStore("stationVehicleStore").proxy.extraParams = {
            "json" : pram
        };

    },

    onAfterStationAvialiableVehicle: function(){
        var avialiableVehicleStore = Ext.getCmp('addVehicleToStationPage').store;
        avialiableVehicleStore.currentPage = 1;
        Ext.getCmp("addVehicleToStationView").getViewModel().getStore("stationVehicleStore").load();
    },
    //打开可分配车辆窗口
    showAvailiableVehicleView: function(){
        var assignedVehicleCount = Ext.getCmp('assignVehiclePage').store.totalCount;
        var stationId = Ext.getCmp("assignVehicle").stationId;
        var stationName = Ext.getCmp("assignVehicle").stationName;
        var carNumber = Ext.getCmp("assignVehicle").carNumber;

        if(assignedVehicleCount >= parseInt(carNumber)){
            Ext.Msg.alert('消息提示', '站点 ' + stationName + ' 有 '+ carNumber +' 个停车位，最多分配这么多辆车！');
        }else{

            var input = {
                "currentPage" : 1,
                "numPerPage" : 10,
                'stationId' : stationId};
            var json = Ext.encode(input);
            Ext.Ajax.request({
                url : "station/findStationAvialiableVehicles",
                method : 'POST',
                params:{ json:json},
                success : function(response, options) {
                    var respText = Ext.util.JSON.decode(response.responseText);
                    var totalRows = respText.data.totalRows;
                    if(totalRows == '0'){
                        var win = Ext.widget("emptyAvialiableVehicles", {
                            title: '车辆管理',
                            buttonAlign : 'center',
                            closable: false,
                            stationId : stationId,
                            stationName: stationName,
                            carNumber: carNumber,
                            assignedVehicleCount: assignedVehicleCount,
                            buttons : [{
                                text : '返回',
                                handler: 'toAssignVehicleView'
                            }]
                        });
                        //this.view.close();
                        Ext.getCmp("assignVehicle").close();
                        //Ext.getXClass("Admin.view.vehiclemgmt.stationmgmt.AssignVehicle").close();
                        win.show();
                    }else{
                        console.log('+++showAvailiableVehicleView+++stationId: ' + stationId);
                        var win = Ext.widget("addVehicleToStation", {
                            title: '车辆管理',
                            buttonAlign : 'center',
                            closable: false,
                            stationId : stationId,
                            stationName: stationName,
                            carNumber: carNumber,
                            assignedVehicleCount: assignedVehicleCount,
                            buttons : [{
                                text : '返回',
                                handler: 'toAssignVehicleView'
                            }]
                        });
                        //this.view.close();
                        Ext.getCmp("assignVehicle").close();
                        //Ext.getXClass("Admin.view.vehiclemgmt.stationmgmt.AssignVehicle").close();
                        win.show();
                    }

                },
//	 			failure : function() {
//	 				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//	 			},
                scope : this
            });
        }
    },
    toAssignVehicleView: function() {
        if(Ext.getCmp("addVehicleToStation")){
            var stationId = Ext.getCmp("addVehicleToStation").stationId;
            var stationName = Ext.getCmp("addVehicleToStation").stationName;
            var carNumber = Ext.getCmp("addVehicleToStation").carNumber;
            var assignedVehicleCount = Ext.getCmp("addVehicleToStation").assignedVehicleCount;
            /*清空存储的选中车辆list*/
            Ext.getCmp('addVehicleToStation').AllSelectedRecords.length = 0;
            Ext.getCmp('addVehicleToStationView').getSelectionModel().clearSelections();
        }
        if(Ext.getCmp("emptyAvialiableVehicles")){
            var stationId = Ext.getCmp("emptyAvialiableVehicles").stationId;
            var stationName = Ext.getCmp("emptyAvialiableVehicles").stationName;
            var carNumber = Ext.getCmp("emptyAvialiableVehicles").carNumber;
            var assignedVehicleCount = Ext.getCmp("emptyAvialiableVehicles").assignedVehicleCount;
        }

        console.log('stationId id : ' + stationId);
        var win = Ext.widget("assignVehicle", {
            title: '车辆管理',
            closable: false,
            buttonAlign : 'center',
            stationId: stationId,
            stationName: stationName,
            carNumber: carNumber,
            assignedVehicleCount: assignedVehicleCount,
            buttons : [{
                text : '返回',
                handler: 'toBackStationView'
            }]
        });
        this.view.close();
        win.show();
    },
    //给站点分配车辆
    assignVehicleDone:function(grid, rowIndex, colIndex){
        win = Ext.widget('assignVehicle');
        win.show();

    },
    //移除已分配车辆
    unassignVehicle: function(grid, rowIndex, colIndex) {
        var rec = grid.getStore().getAt(rowIndex);
        var stationName = Ext.getCmp("assignVehicle").stationName;
        var msg = '是否确认将车辆 ' + rec.data.vehicleNumber + ' 从站点 ' +  stationName + ' 移除?';
        var stationId = Ext.getCmp("assignVehicle").stationId;
        console.log('+++unassignVehicle+++stationId: ' + stationId);
        Ext.Msg.confirm('消息提示', msg, function(btn){
            if (btn == 'yes') {
                console.log('stationId:' + stationId + '; id' + rec.data.id);
                //调用部门移除接口 start
                var input = {'stationId' : stationId,
                    'vehicleId': rec.data.id};
                var json = Ext.encode(input);
                Ext.Ajax.request({
                    url : "station/unassignVehicles",
                    method : 'POST',
                    params:{ json:json},
                    success : function(response, options) {
                        var respText = Ext.util.JSON.decode(response.responseText);
                        var status = respText.status;
                        console.log('length:' + status);
                        if(status == 'success') {
                            Ext.Msg.alert('消息提示', '移除成功');
                            var avialiableVehicleStore = Ext.getCmp('assignVehiclePage').store;
                            avialiableVehicleStore.currentPage = 1;
                            Ext.getCmp("assignedVehicleGrid").getViewModel().getStore("stationVehicleAssignedStore").load();
                        }
                    },
//         			failure : function() {
//         				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//         			},
                    scope : this
                });
                //调用已分配车辆移除接口 end
            }
        });
    },
    confirmAddVehicle: function() {
        var stationId = Ext.getCmp("addVehicleToStation").stationId;
        var stationName = Ext.getCmp("addVehicleToStation").stationName;
        var carNumber = Ext.getCmp("addVehicleToStation").carNumber;
        var assignedVehicleCount = Ext.getCmp("addVehicleToStation").assignedVehicleCount;
        console.log('to confirm add vehicle to station');
        var gridPanel = Ext.getCmp('addVehicleToStationView');
        var record = Ext.getCmp('addVehicleToStation').AllSelectedRecords;
        console.log('length:' + record.length);
        if(gridPanel.getViewModel().getStore('stationVehicleStore').totalCount == 0) {
            Ext.Msg.alert('消息提示', '无车辆可选择');
            return;
        }
        if(record.length == 0) {
            Ext.Msg.alert('消息提示', '请选择车辆');
            return;
        }

        var remainAssignedRule = parseInt(carNumber) - assignedVehicleCount;

        if(record.length > remainAssignedRule){
            Ext.Msg.alert('消息提示', '站点 ' + stationName + ' 有 ' +carNumber+ ' 个停车位,已分配 ' + assignedVehicleCount + ' 辆车，最多只能分配 ' + remainAssignedRule + ' 辆车！' );
        }else{
            var vehicleIds = '';
            for (var i=0; i<record.length; i++) {
                vehicleIds += record[i].data.id + ',';
            }
            console.log('stationId:' + stationId);
            vehicleIds = vehicleIds.substr(0,vehicleIds.length-1);
            console.log('vehicleIds:' + vehicleIds);

            var input = {
                'stationId' : stationId,
                'vehicleIds' : vehicleIds
            };
            var json = Ext.encode(input);
            Ext.Ajax.request({
                url : "station/assignVehicles",
                method : 'POST',
                params:{ json:json},
                success : function(response, options) {
                    var respText = Ext.util.JSON.decode(response.responseText);
                    var status = respText.status;
                    var input = {
                        'stationId' : Ext.getCmp("addVehicleToStation").stationId
                    };
                    var win = Ext.widget("assignVehicle", {
                        title: '车辆管理',
                        closable: false,
                        buttonAlign : 'center',
                        stationId: Ext.getCmp("addVehicleToStation").stationId,
                        stationName: Ext.getCmp("addVehicleToStation").stationName,
                        carNumber: Ext.getCmp("addVehicleToStation").carNumber,
                        assignedVehicleCount: Ext.getCmp("addVehicleToStation").assignedVehicleCount,
                        buttons : [{
                            text : '返回',
                            handler: 'toBackStationView'
                        }]
                    });
                    if(status == 'success') {
                        Ext.Msg.alert('消息提示', '添加成功', function(text) {
							/*清空存储的选中车辆list*/
                            Ext.getCmp('addVehicleToStation').AllSelectedRecords.length = 0;
                            Ext.getCmp('addVehicleToStationView').getSelectionModel().clearSelections();
                            Ext.getCmp("addVehicleToStation").close();
                            win.show();
                        });
                    }
                },
//	 			failure : function() {
//	 				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//	 			},
                scope : this
            });
        }
    },


    onResetClick : function() {
        Ext.Msg.alert("Message Box", "重置按钮");
    },

    loadAddStationInformation: function(){
        var map = this.lookupReference('bmappanelStation').bmap;

        this.initMap(map);

        function myFun(result){
            var cityName = result.name;
            console.log('initView:' + cityName);
            map.setCenter(cityName);
            map.centerAndZoom(cityName,12);
        }
        var myCity = new BMap.LocalCity();

        myCity.get(myFun);

		/*    	var point = new BMap.Point(114.29821255382512, 30.589583523896508);
		 map.centerAndZoom(point, 13);*/
    },

    initMap: function(map){
        //添加比例尺控件
        var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
        var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
        map.addControl(top_left_control);
        map.addControl(top_left_navigation);
    },

    renderPositionInfoOnMap: function(position){
        var map = Ext.getCmp("stationmapdis_bmappanel").bmap;
        //建立一个自动完成的对象
        var ac = new BMap.Autocomplete({
            "input" : "positionStationId-inputEl",
            "location" : map
        });
        if(position != ""  && position != null){
            ac.setInputValue(position);
        }

        ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
            var str = "";
            var _value = e.fromitem.value;
            e.v4.index > -1 && (localSearchdiOfStrict = e.v4.value.district);
            var value = "";
            if (e.fromitem.index > -1) {
                value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
                //localSearchdiOfStrict = _value.district;
            }
            str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

            value = "";
            if (e.toitem.index > -1) {
                _value = e.toitem.value;
                value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
            }
            str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
        });
        console.log('localSearchdiOfStrict:' + localSearchdiOfStrict);
        var myValue;
        ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
            $("#positionStationId-inputEl").attr("disabled","disabled");
            var map = Ext.getCmp('stationmapdis_bmappanel').bmap;
            var _value = e.item.value;
            myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;

            map.clearOverlays();    //清除地图上所有覆盖物
            var local = new BMap.LocalSearch(map, { //智能搜索
                onSearchComplete: function(){
                    $("#positionStationId-inputEl").removeAttr("disabled");
                    var city;
                    if(Ext.getCmp('addStation')){
                        area = Ext.getCmp('addStation').down('form').getForm().findField('area').getRawValue();
                    }

                    if(Ext.getCmp('edit_station_id')){
                        area = Ext.getCmp('edit_station_id').down('form').getForm().findField('areaId').getRawValue();
                    }
                    console.log('local.getResults().district:' + localSearchdiOfStrict);
                    console.log('city:' + area);
                    if(area != null){
                        if(localSearchdiOfStrict == area){
                            var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
                            map.centerAndZoom(pp, 15);
                            var marker = new BMap.Marker(pp);
                            map.addOverlay(marker);    //添加标注

                            var content = "<table>";
                            content = content + "<tr class=\"window-table\"><td>城市：" + local.getResults().getPoi(0).city + "</td></tr>";
                            content = content + "<tr class=\"window-table\"><td>地址：" + local.getResults().getPoi(0).address + "</td></tr>";
                            content = content + "<tr class=\"window-table\"><td>纬度：" + local.getResults().getPoi(0).point.lat + "</td></tr>";
                            content = content + "<tr class=\"window-table\"><td>经度：" + local.getResults().getPoi(0).point.lng + "</td></tr>";
                            content += "</table>";

                            (function () {
                                var infoWindow = new BMap.InfoWindow(content);
                                marker.addEventListener("click", function () {
                                    this.openInfoWindow(infoWindow);
                                });
                            })();


                            if(Ext.getCmp('addStation')){
                                Ext.getCmp('addStation').down('form').getForm().findField('radius').setValue('');
                            }

                            if(Ext.getCmp('edit_station_id')){
                                Ext.getCmp('edit_station_id').down('form').getForm().findField('radius').setValue('');
                            }

                        }else{
                            Ext.Msg.alert('消息提示', '此位置不在 ' + area + ' 内！请重新输入！');
                            if(Ext.getCmp('addStation')){
                                Ext.getCmp('addStation').down('form').getForm().findField('positionStationId').setValue('');
                                Ext.getCmp('addStation').down('form').getForm().findField('radius').setValue('');
                            }

                            if(Ext.getCmp('edit_station_id')){
                                Ext.getCmp('edit_station_id').down('form').getForm().findField('positionStationId').setValue('');
                                Ext.getCmp('edit_station_id').down('form').getForm().findField('radius').setValue('');
                            }
                        }
                    }else{
                        Ext.Msg.alert('消息提示', '请选择城市！');
                    }
                }
            });
            local.search(myValue);
        });
    },
    provinceSelectLoadOnMap : function(combo , record , eOpts){
        var map = Ext.getCmp('stationmapdis_bmappanel').bmap;
        map.centerAndZoom(record.data.regionName,11);
        if(Ext.getCmp('addStation')){
            Ext.getCmp('addStation').down('form').getForm().findField('addStationCityCmb').setValue('');
        }
        if(Ext.getCmp('edit_station_id')){
            Ext.getCmp('edit_station_id').down('form').getForm().findField('editStationCityCmb').setValue('');
            Ext.getCmp('edit_station_id').down('form').getForm().findField('editStationAreaCmb').setValue('');
            Ext.getCmp('edit_station_id').down('form').getForm().findField('position').setValue('');
        }
    },
    citySelectLoadOnMap: function(combo , record , eOpts){
        console.log('Load Posistion On Map');
        var map = Ext.getCmp('stationmapdis_bmappanel').bmap;
        map.centerAndZoom(record.data.regionName,11);
        if(Ext.getCmp('addStation')){
            Ext.getCmp('addStation').down('form').getForm().findField('addStationAreaCmb').setValue('');
        }

        if(Ext.getCmp('edit_station_id')){
            Ext.getCmp('edit_station_id').down('form').getForm().findField('editStationAreaCmb').setValue('');
            Ext.getCmp('edit_station_id').down('form').getForm().findField('position').setValue('');
        }

    },
    areaSelectLoadOnMap: function(combo , record , eOpts){
        var map = Ext.getCmp('stationmapdis_bmappanel').bmap;
        map.centerAndZoom(record.data.regionName,11);
        if(Ext.getCmp('addStation')){
            Ext.getCmp('addStation').down('form').getForm().findField('position').setValue('');
        }
        if(Ext.getCmp('edit_station_id')){
            Ext.getCmp('edit_station_id').down('form').getForm().findField('position').setValue('');
//		     Ext.getCmp('edit_station_id').down('form').getForm().findField('position').setValue('');
        }
    },
    radiusSelectLoadOnMap: function(combo , record , eOpts){
        console.log('Load Circle On Map');
        var map = Ext.getCmp('stationmapdis_bmappanel').bmap;
        map.clearOverlays();

        var position = Ext.getCmp('positionStationId').getValue();

        if(position == '' || position == null){
            Ext.Msg.alert('消息提示', '位置不能为空！');
        }else{
            var local = new BMap.LocalSearch(map, { //智能搜索
                onSearchComplete: function(){
                    if(local.getResults().getPoi(0) == undefined){
                        Ext.Msg.alert('消息提示','无法此解析地址，请重新输入');
                        if(Ext.getCmp('addStation')){
                            Ext.getCmp('addStation').down('form').getForm().findField('positionStationId').setValue('');
                            Ext.getCmp('addStation').down('form').getForm().findField('radius').setValue('');
                        }

                        if(Ext.getCmp('edit_station_id')){
                            Ext.getCmp('edit_station_id').down('form').getForm().findField('positionStationId').setValue('');
                            Ext.getCmp('edit_station_id').down('form').getForm().findField('radius').setValue('');
                        }
                    }else{
                        var pp = local.getResults().getPoi(0).point;
                        map.centerAndZoom(pp, 15);
                        map.addOverlay(new BMap.Marker(pp));
                        if(Ext.getCmp('addStation')){
                            Ext.getCmp('addStation').down('form').getForm().findField('longitude').setValue(pp.lng);
                            Ext.getCmp('addStation').down('form').getForm().findField('latitude').setValue(pp.lat);
                        }

                        if(Ext.getCmp('edit_station_id')){
                            Ext.getCmp('edit_station_id').down('form').getForm().findField('longitude').setValue(pp.lng);
                            Ext.getCmp('edit_station_id').down('form').getForm().findField('latitude').setValue(pp.lat);
                        }
                        var circle = new BMap.Circle(pp,record.data.value * 1000,{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
                        map.addOverlay(circle);
                    }
                }
            });
            local.search(position);
        }
    },

    checkVehSelect: function (me, record, index, opts) {
        Ext.getCmp('addVehicleToStation').AllSelectedRecords.push(record);
    },

    checkVehdeSelect: function (me, record, index, opts) {
        Ext.getCmp('addVehicleToStation').AllSelectedRecords = Ext.Array.filter(Ext.getCmp('addVehicleToStation').AllSelectedRecords, function (item) {
            return item.get("id") != record.get("id");
        });
    },
    loadCheckAvialiableVehicle: function (me, records, success, opts) {
        if (!success || !records || records.length == 0)
            return;

        //根据全局的选择，初始化选中的列
        var selModel = Ext.getCmp('addVehicleToStationView').getSelectionModel();
        Ext.Array.each(Ext.getCmp('addVehicleToStation').AllSelectedRecords, function () {
            for (var i = 0; i < records.length; i++) {
                var record = records[i];
                if (record.get("id") == this.get("id")) {
                    selModel.select(record, true, true);    //选中record，并且保持现有的选择，不触发选中事件
                }
            }
        });
    },
    loadProvinceComStore : function()  {
        var url= 'area/queryAreaInfo';
        Ext.Ajax.request({
            method:'GET',
            url:url,
            defaultHeaders : {'Content-type' : 'application/json;utf-8'},
            success: function(res){
                var appendData=Ext.util.JSON.decode(res.responseText);
                if(appendData.status=='success'){
                    Ext.getCmp("editStationProvinceCmb").setStore(appendData);
                }else{
                    Ext.Msg.alert("提示信息", appendData.error);
                }
            }
			/*,
			 failure : function() {
			 Ext.Msg.alert('Failure','Call interface error!');
			 }*/
        });
    },
    getCityByprovince: function(obj,newValue,oldValue){
        console.log('getCityByprovince++++newValue:' + newValue);
        var input={
            parentId:newValue
        };
        var json=Ext.encode(input);
        Ext.Ajax.request({
            method:'GET',
            url:'area/queryAreaInfo',
            params:{json:json},
            defaultHeaders : {'Content-type' : 'application/json;utf-8'},
            success: function(res){
                var appendData=Ext.util.JSON.decode(res.responseText);
                if(appendData.status=='success'){
                    Ext.getCmp("editStationCityCmb").setStore(appendData);
                    if(oldValue != null){
                        Ext.getCmp('editStationCityCmb').setValue(appendData.data[0].regionId);
                    }

                }else{
                    Ext.Msg.alert("提示信息", appendData.error);
                }
            }
			/*,
			 failure : function() {
			 Ext.Msg.alert('Failure','Call interface error!');
			 }*/
        });
    },
    getAreaBycity : function(obj,newValue,oldValue) {
        var cityId = Ext.getCmp('editStationCityCmb').getValue();
        if(newValue!=null) {
            var input={
                parentId:newValue
            };
            var json=Ext.encode(input);
            Ext.Ajax.request({
                method:'GET',
                url:'area/queryAreaInfo/',
                params:{json:json},
                defaultHeaders : {'Content-type' : 'application/json;utf-8'},
                success: function(res){
                    var appendData=Ext.util.JSON.decode(res.responseText);
                    if(appendData.status=='success'){
                        Ext.getCmp("editStationAreaCmb").setStore(appendData);
                        if(oldValue != null){
//						    if(oldValue!=null) {
                            Ext.getCmp('editStationAreaCmb').setValue(appendData.data[0].regionId);
                        }

                    }else{
                        Ext.Msg.alert("提示信息", appendData.error);
                    }
                }
				/*,
				 failure : function() {
				 Ext.Msg.alert('Failure','Call interface error!');
				 }*/
            });
        }

    },
});

function onEmptyforVehMag(){
    console.log('click');
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

    main.redirectTo('vehicleInfoMgmt');
    Ext.getCmp("emptyAvialiableVehicles").close();
}

