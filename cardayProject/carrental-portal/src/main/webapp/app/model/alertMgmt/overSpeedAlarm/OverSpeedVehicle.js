Ext.define('Admin.model.alertMgmt.overSpeedAlarm.OverSpeedVehicle', {
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
            name: 'alertSpeed' //实际速度
        },{
        	type: 'string',
            name: 'overspeedPercent'  //超速比例
        },{
        	type: 'string',
            name: 'alertPosition' //超速位置
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
        },{
            type: 'string',
            name: 'alertLongitude'
        },{
            type: 'string',
            name: 'alertLatitude'
        }]
});