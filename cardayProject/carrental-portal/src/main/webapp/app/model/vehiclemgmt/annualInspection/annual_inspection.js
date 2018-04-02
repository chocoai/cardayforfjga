Ext.define('Admin.model.vehiclemgmt.annualInspection.annual_inspection', {
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
            name: 'arrangedOrgName'
        },
        {
            type: 'string',
            name: 'vehicleFromName'
        },
        {
            type: 'string',
            name: 'insuranceDueTime'
        },
        {
            type: 'string',
            name: 'insuranceDueTimeF'
        },
        {
            type: 'string',
            name: 'inspectionLastTime'
        },
        {
            type: 'string',
            name: 'inspectionLastTimeF'
        },
        {
            type: 'string',
            name: 'inspectionNextTime'
        },
        {
            type: 'string',
            name: 'inspectionNextTimeF'
        }
    ]
});
