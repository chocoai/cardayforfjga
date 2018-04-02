Ext.define('Admin.model.vehiclemgmt.geofencemgmt.Geofence', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'geofence/findByName',
        actionMethods: {
                    create : 'POST',
                    read   : 'POST', // by default GET
                    update : 'POST',
                    destroy: 'POST'
        },
        reader: {
             type: 'json',
             rootProperty: 'data.resultList',
             successProperty: 'status',
             totalProperty:'data.totalRows'
        }
    },
    autoLoad: 'false',

    sorters: {
        direction: 'ASC',
  //  	direction:'DESC',
        property: 'id'
    }
});
