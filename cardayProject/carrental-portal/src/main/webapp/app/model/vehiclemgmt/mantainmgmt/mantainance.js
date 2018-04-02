Ext.define('Admin.model.vehiclemgmt.mantainmgmt.mantainance', {
    extend: 'Admin.model.Base',

    fields: [
        {
        	type: 'string',
        	name: 'id'
        },
        {
            type: 'string',
            name: 'vehicleNumber'
        },
        {
            type: 'string',
            name: 'vehicleBrand'
        },
        {
            type: 'string',
            name: 'vehicleModel'
        },
        {
            type: 'string',
            name: 'arrangedOrgName'
        },
        {
            type: 'string',
            name: 'totalMileage'
        },
        {
            type: 'string',
            name: 'mantainMileage'
        },
        {
            type: 'string',
            name: 'runMileage'
        },
        {
            type: 'string',
            name: 'lastMantainTime'
        }
    ]
});
