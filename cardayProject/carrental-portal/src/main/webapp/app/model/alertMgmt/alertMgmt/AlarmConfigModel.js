Ext.define('Admin.model.alertMgmt.alarmConfig.AlarmConfigModel', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'vehicleAlert/alarm/query',
        //url:'app/data/alertMgmt/alarmConfig/AlarmConfigModel.json',
        actionMethods: {
                    create : 'GET',
                    read   : 'GET', // by default GET
                    update : 'GET',
                    destroy: 'GET'
        },
        reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'status',
        }
    },
    autoLoad: 'false',
});