Ext.define('Admin.model.vehiclemgmt.vehicleInfomgmt.Vehicle', {
    extend: 'Admin.model.Base',

    fields: [
        {
            type: 'string',
            name: 'id'  //车Id
        },{
            type: 'string',
            name: 'vehicleNumber'  //车牌号
        },{
            type: 'string',
            name: 'vehicleType'  //公车性质
        },{
            type: 'string',
            name: 'vehicleBrand' //车辆品牌
        },{
            type: 'string',
            name: 'vehicleModel' //车辆型号
        },{
        	type: 'int',
            name: 'seatNumber'  //车辆座位数
        },{
        	type: 'string',
            name: 'vehicleColor' //车辆颜色
        },{
        	type: 'string',
            name: 'vehicleOutput' //排量
        },{
        	type: 'string',
            name: 'vehicleFuel'    //燃油号
        },{
        	type: 'string',
            name: 'city'    //车辆所属城市
    	},{
        	type: 'string',
            name: 'vehicleFromId'  //车辆来源Id
        },{
        	type: 'string',
            name: 'vehicleFromName'  //车辆来源名称
        },{
        	type: 'string',
        	name: 'licenseType'   //准驾类型
        },{
        	type: 'date',
        	name: 'vehicleBuyTime',  //购买时间
        /*	convert: function (value) {
         			return new Date(value);
        	}*/
        },{
        	type: 'date',
        	name: 'insuranceExpiredate', //保险到期日
        /*	convert: function (value) {
         			return new Date(value);
        	}*/
        },{
        	type: 'date',
        	name: 'inspectionExpiredate', //年检到期日
        	/*convert: function (value) {
         			return new Date(value);
        	}*/
        },{
        	type: 'string',
        	name: 'vehicleIdentification'  //车架号
        },{
        	type:'string',
        	name:'arrangedEntName'       //分配企业名称
        },{
        	type:'string',
        	name:'arrangedEntId'		//分配企业Id
        },{
        	type:'string',
        	name:'arrangedOrgName'       //当前部门名称
        },{
        	type:'string',
        	name:'arrangedOrgId'		//当前部门Id
        },{
        	type:'string',
        	name:'vehiclePurpose'		//车辆用途
        },{
        	type:'string',
        	name:'simNumber'  //sim卡
        },{
        	type:'string',
        	name:'theoreticalFuelCon' //理论油耗
        },{
        	type:'string',
        	name:'parkingSpaceInfo'  //车位信息
        },{
        	type:'string',
        	name:'deviceNumber'   //设备号
        },{
        	type:'string',
        	name:'limitSpeed'   //限速
        },{
        	type:'string',
        	name:'snNumber'  //sn号
        },{
        	type:'string',
        	name:'iccidNumber'  //iccid
        },
    ]
});