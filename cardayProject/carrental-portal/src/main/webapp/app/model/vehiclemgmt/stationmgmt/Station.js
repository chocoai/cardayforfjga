Ext.define('Admin.model.vehiclemgmt.stationmgmt.Station', {
    extend: 'Admin.model.Base',

    fields: [
        {
            type: 'string',
            name: 'stationName'
        },
        {
            type: 'string',
            name: 'city'
        },
        {
            type: 'string',
            name: 'position'
        },
        {
            type: 'string',
            name: 'radius'
        },
        {
            type: 'string',
            name: 'carNumber'
        }
    ]
});
