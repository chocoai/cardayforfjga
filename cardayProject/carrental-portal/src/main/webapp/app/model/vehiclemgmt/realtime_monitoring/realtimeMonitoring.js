Ext.define('Admin.model.vehiclemgmt.realtime_monitoring.realtimeMonitoring', {
    extend: 'Admin.model.Base',

    fields: [
        {
            type: 'string',
            name: 'vehiclenumber'
        },
        {
            type: 'string',
            name: 'vehicle_type'
        },
        {
            type: 'string',
            name: 'longitude'
        },
        {
            type: 'string',
            name: 'latitude'
        }
    ]
});
