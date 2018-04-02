Ext.define('Admin.view.systemmgmt.smsconfig.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.smsconfigModel',

    stores: {
    	smsconfigResults: {
            model: 'Admin.model.systemmgmt.smsconfig.SmsConfigModel',
            remoteFilter: true,
            autoLoad: false,
            listeners: {
            	load: function(store){
//            		var data = {eventType: "ALLOCATESMS", enable: 0}
//            		store.insert(0, data);
            	}
            }
        }
    }
});
