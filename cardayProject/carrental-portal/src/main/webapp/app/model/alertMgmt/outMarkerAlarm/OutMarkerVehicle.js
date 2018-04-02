Ext.define('Admin.model.alertMgmt.outMarkerAlarm.OutMarkerVehicle', {
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
        },
        {
        	type: 'string',
        	name: 'firstOutboundtime',  //首次发生时间
        },{
        	type: 'string',
        	name: 'outboundReleasetime',  //解除时间时间
        	/*convert: function (value) {
         			return new Date(value);
        	}*/
        },{
            type: 'string',
            name: 'outboundMinutes' //越界时长
        },{
        	type: 'string',
            name: 'outboundKilos'  //越界里程
        },{
        	type: 'string',
            name: 'vehicleSource'  //车辆来源名称
        },{
        	type: 'string',
        	name: 'currentuseOrgName'       //所属部门名称
        },{
        	type:'string',
        	name:'driverName'		//司机姓名
        },{
        	type: 'string',
        	name: 'driverPhone'   //司机电话
        }]
});