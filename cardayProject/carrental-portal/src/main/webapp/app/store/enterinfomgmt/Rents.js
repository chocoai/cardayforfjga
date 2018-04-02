Ext.define('Admin.store.enterinfomgmt.Rents', {
    extend: 'Ext.data.Store',

    alias: 'store.rents',

    model: 'Admin.model.enterinfo.Rent',

    proxy:{
    	type: 'ajax',
        url: 'rent/list',
        reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'status'
        }
    },
    autoLoad: 'false',
    sorters: {
    	direction:'DESC',
        property: 'id'
    }
});