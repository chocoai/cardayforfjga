Ext.define('Admin.model.ordermgmt.allocatecar.allocatedCar', {
    extend: 'Admin.model.Base',

    fields: [
        {
            type: 'string',
            name: 'name'
        },
        {
            type: 'string',
            name: 'phone'
        },
        {
            type: 'string',
            name: 'appointmentTime'
        },
        {
            type: 'string',
            name: 'usedCity'
        },
        {
            type: 'string',
            name: 'startPlace'
        },
        {
            type: 'string',
            name: 'endPlace'
        },
        {
            type: 'string',
            name: 'carType'
        },
        {
            type: 'string',
            name: 'driverName'
        },
        {
            type: 'string',
            name: 'driverPhone'
        },
        {
            type: 'string',
            name: 'orderStatus'
        }
    ]
});
