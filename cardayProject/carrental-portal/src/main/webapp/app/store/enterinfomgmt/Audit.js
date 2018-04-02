Ext.define('Admin.store.enterinfomgmt.Audit', {
    extend: 'Ext.data.Store',

    alias: 'store.audit',

    model: 'Admin.model.enterinfo.User',

    proxy:{
    	type: 'ajax',
         url: 'organization/audit/list',
         reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'status'
         }
    },
    autoLoad: 'false',
    sorters: {
        direction: 'DESC',
        property: 'id'
    }
});
