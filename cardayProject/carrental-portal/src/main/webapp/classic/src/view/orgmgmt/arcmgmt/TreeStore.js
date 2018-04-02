Ext.define('Admin.view.orgmgmt.arcmgmt.TreeStore',{
    extend:'Ext.data.TreeStore',
    alias: "store.TreeStore",
    
    model: 'Admin.model.arcmgmt.arctree',

    proxy:{
    	type: 'ajax',
         url: 'app/data/arcmgmtinfo/arctreeData.json',
         reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'success'
         }
    },

    autoLoad: 'true',

});