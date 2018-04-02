Ext.define('Admin.model.arcmgmt.vehiclemgmt.AvailableAssignVehicle', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'vehicle/organization/listUnAssignedVehicle',
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
      direction:'DESC',
      property: 'id'
    }
});
