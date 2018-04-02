Ext.define('Admin.view.main.MainModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.main',
requires: [
      'Admin.store.alertMgmt.overSpeedAlarm.AlarmInfo'
    ],
    data: {
        currentView: null
    },
      stores: {
        AlertInfo: {
            type: 'alarmInfo',   
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
	         listeners: {
	            beforeload: 'onBeforeLoad'
	        }
        },

        AlertInfoPreMonitoring: {
            type: 'alertInfoPreMonitoring',   
            remoteFilter: true,
            autoLoad: false,
             listeners: {
                beforeload: 'onBeforeLoadforPre'
            }
        },
        AlertInfoNextMonitoring: {
            type: 'alertInfoNextMonitoring',   
            remoteFilter: true,
            autoLoad: false,
             listeners: {
                beforeload: 'onBeforeLoadforNext'
            }
        }         
    }
});
