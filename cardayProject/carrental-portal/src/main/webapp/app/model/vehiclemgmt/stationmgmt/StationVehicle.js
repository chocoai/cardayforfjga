Ext.define('Admin.model.vehiclemgmt.stationmgmt.StationVehicle', {
    extend: 'Admin.model.Base',

    fields: [
        {
            type: 'string',
            name: 'vehicleNumber'
        },{
            type: 'string',
            name: 'vehicleType'
        },{
            type: 'string',
            name: 'vehicleBrand'
        },{
            type: 'string',
            name: 'vehicleModel'
        },{
        	type: 'string',
            name: 'vehicleColor'
        },{
        	type: 'string',
            name: 'vehicleOriginal'
        },{
        	type: 'string',
            name: 'vehicleOrgnization'
        }
    ]
});