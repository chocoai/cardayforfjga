Ext.define('Admin.model.ordermgmt.allocatecar.allocatedOrderTrace', {
    extend: 'Admin.model.Base',

    fields: [
        {
            type: 'string',
            name: 'startTime'
        },
        {
            type: 'string',
            name: 'endTime'
        },
        {
            type: 'string',
            name: 'mileage'
        },
        {
            type: 'string',
            name: 'speed'
        }
    ]
});
