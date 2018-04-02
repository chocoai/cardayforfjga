Ext.define('Admin.model.arcmgmt.arc', {
    extend: 'Admin.model.Base',

    fields: [
        {
            type: 'string',
            name: 'name'
        },
        {
            type: 'string',
            name: 'address'
        },
        {
            type: 'string',
            name: 'telephone'
        },
        {
        	type: 'string',
            name: 'introduction'
        }
    ]
});
