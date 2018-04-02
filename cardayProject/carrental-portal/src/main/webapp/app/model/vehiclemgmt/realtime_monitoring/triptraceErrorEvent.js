Ext.define('Admin.model.vehiclemgmt.realtime_monitoring.triptraceErrorEvent', {
    extend: 'Admin.model.Base',

    fields: [
        {
            type: 'string',
            name: 'createdTime'
        },
        {
            type: 'string',
            name: 'event'
        },
        {
        	type: 'string',
        	name: 'eventDescribe'
        }
    ]
});
