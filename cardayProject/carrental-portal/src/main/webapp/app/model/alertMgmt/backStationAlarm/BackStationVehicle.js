Ext.define('Admin.model.alertMgmt.backStationAlarm.BackStationVehicle', {
    extend: 'Admin.model.Base',

    fields: [{
            type: 'int',
            name: 'id'  //车Id
        },{
            type: 'string',
            name: 'vehicleNumber'  //车牌号
        },{
            type: 'string',
            name: 'vehicleType'  //公车性质
        },{
            type: 'string',
            name: 'alertType' //异常类型
        },{
        
        	type: 'date',
        	name: 'alertTime',  //报警时间
        	convert: function (value) {
         			return new Date(value);
        	}
        },{
        	type: 'string',
        	name: 'outboundReleasetime'  //解除时间时间
        },{
            type: 'string',
            name: 'alertCity' //城市
        },{
        	type: 'string',
            name: 'alertPosition'  //实际停车位置
        },{
        	type: 'string',
            name: 'stationName'  //站点名称
        },{
        	type: 'string',
        	name: 'distance'       //相差距离
        },{
        	type: 'string',
            name: 'vehicleSource'  //车辆来源名称
        },{
        	type:'string',
        	name:'currentuseOrgName'       //所属部门名称
        },{
        	type:'string',
        	name:'driverName'		//司机姓名
        },{
        	type: 'string',
        	name: 'driverPhone'   //司机电话
        }]
});