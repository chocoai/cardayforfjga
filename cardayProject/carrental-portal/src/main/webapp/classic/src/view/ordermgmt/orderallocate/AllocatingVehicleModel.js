Ext.define('Admin.view.ordermgmt.orderallocate.AllocatingVehicleModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.allocatingVehicleModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json',
        'Admin.view.ordermgmt.orderallocate.AllocatingVehicleStore'
    ],

    stores: {
    	allocatingVehicleReport: {
    		 model: 'Admin.view.ordermgmt.orderallocate.AllocatingVehicleStore',
             remoteFilter: true,
             pageSize: 12,
             autoLoad: false,
             sorters: [{
                 property: 'passengerNum',
                 direction: 'DESC'
             }],
             listeners:{
            	 load: function(store){
//            		 var data = {"id":3226,"vehicleNumber":"闽AA0188","vehicleIdentification":"LDNM43G2290237388","vehicleType":"0","vehicleBrand":"大众","vehicleModel":"帕萨特","seatNumber":5,"vehicleColor":"黑色","vehicleOutput":"3.0T","vehicleFuel":"90(京89)","vehicleBuyTime":"2016-02-25","licenseType":"","rentId":null,"rentName":null,"entId":73,"entName":"福建省公安厅","currentuseOrgId":3538,"currentuseOrgName":"警务保障室","city":"350100","theoreticalFuelCon":8.0,"insuranceExpiredate":"2017-10-04","inspectionExpiredate":"2017-09-25","parkingSpaceInfo":"park1234","vehiclePurpose":"","deviceNumber":"898602B6101680818012","simNumber":"1064826718528","snNumber":"A49AB013C8","iccidNumber":"898602B6101680818012","limitSpeed":20,"latestLimitSpeed":null,"commandStatus":null,"startTime":"07:00","endTime":"17:00","vehicleFromId":73,"vehicleFromName":"福建省公安厅","arrangedOrgId":3538,"arrangedOrgName":"警务保障室","arrangedEntId":null,"arrangedEntName":null,"stationName":"福建移动通信大楼","isInternalUse":null,"province":"350000","cityName":"福州市","provinceName":"福建省","driverId":null,"realname":"王司机","phone":"13545469779","deviceVendorNumber":"DBJ","accDeviceNumber":null,"fitNum":null,"isFit":true,"vehStatus":6,"enableSecret":0,"enableTrafficPkg":null,"registrationNumber":"111111","authorizedNumber":"11111","reasonOfChanging":"维修"};
//            		 store.insert(0, data);
            	 }
             }
        }
    }
});
