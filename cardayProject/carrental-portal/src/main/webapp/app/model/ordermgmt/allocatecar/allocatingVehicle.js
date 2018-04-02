Ext.define('Admin.model.ordermgmt.allocatecar.allocatingVehicle', {
    extend: 'Admin.model.Base',

    fields: [
        {
            type: 'string',
            name: 'carNum'
        },
        {
            type: 'string',
            name: 'carBrand'
        },
        {
            type: 'string',
            name: 'carModel'
        },
        {
            type: 'string',
            name: 'carType'
        },
        {
            type: 'string',
            name: 'seatNum'
        },
        {
            type: 'string',
            name: 'displacement'
        },
        {
            type: 'string',
            name: 'carColor'
        },
        {
            type: 'string',
            name: 'station'
        }
    ]
});
