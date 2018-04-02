Ext.define('Admin.view.alertMgmt.alarmConfig.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.alarmConfigModel',

    stores: {
    	alarmConfigResults: {
            model: 'Admin.model.alertMgmt.alarmConfig.AlarmConfigModel',
            remoteFilter: true,
            autoLoad: false,
        }
    }
});
