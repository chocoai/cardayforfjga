Ext.define('Admin.store.systemmgmt.rolemgmt.Roles', {
    extend: 'Ext.data.Store',

    alias: 'store.rolemgmtrole',

    //model: 'Admin.model.systemmgmt.rolemgmt.Role',

    proxy:{
    	type: 'ajax',
         url: 'app/data/systemmgmt/rolemgmt/roles.json',
         reader: {
             type: 'json',
             rootProperty: 'roles',
             successProperty: 'success'
         }
    },

    autoLoad: 'true',

    sorters: {
        direction: 'ASC',
  //  	direction:'DESC',
        property: 'identifier'
    }
});
