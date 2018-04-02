Ext.define('Admin.model.vehiclemgmt.realtime_monitoring.realtimeData', {
    extend: 'Admin.model.Base',

    fields: [
        {
            type: 'string',
            name: 'createdTime'
        },
        {
            type: 'string',
            name: 'distance'
        },
        {
            type: 'string',
            name: 'currentSpeed'
        }
    ]
});
