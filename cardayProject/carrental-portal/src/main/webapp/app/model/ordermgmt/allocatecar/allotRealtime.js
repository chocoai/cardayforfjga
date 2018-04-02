Ext.define('Admin.model.ordermgmt.allocatecar.allotRealtime', {
    extend: 'Admin.model.Base',

    fields: [
        {
            type: 'string',
            name: 'currentTime'
        },
        {
            type: 'string',
            name: 'distance'
        },
        {
            type: 'string',
            name: 'lat'
        },
        {
            type: 'string',
            name: 'lng'
        },
        {
            type: 'string',
            name: 'city'
        },
        {
            type: 'string',
            name: 'road'
        },
        {
            type: 'string',
            name: 'speed'
        }
    ]
});
