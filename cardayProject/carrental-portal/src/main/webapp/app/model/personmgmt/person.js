Ext.define('Admin.model.personmgmt.person', {
    extend: 'Admin.model.Base',

    fields: [
        {
            type: 'string',
            name: 'name'
        },
        {
            type: 'string',
            name: 'phone_number'
        },
        {
            type: 'string',
            name: 'Email'
        },
        {
            type: 'string',
            name: 'sex'
        }
    ]
});
