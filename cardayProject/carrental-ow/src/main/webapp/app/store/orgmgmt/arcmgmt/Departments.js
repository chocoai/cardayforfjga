Ext.define('Admin.store.orgmgmt.arcmgmt.Departments', {
    extend: 'Ext.data.Store',

    alias: 'store.departments',

    model: 'Admin.model.arcmgmt.arc',

    proxy:{
    	type: 'ajax',
        url: 'organization/0/listDirectChildrenById',
//      pageSize:5,
        reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'status'
//           totalProperty:14
        }
    },
    autoLoad: 'false',

    sorters: [{
        property: 'id',
        direction: 'ASC'
    }, {
        property: 'text',
        direction: 'ASC'
    }],
});
