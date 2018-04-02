/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.reportmgmt.reportexception.ViewModel', {
    extend: 'Ext.app.ViewModel',
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	overspeedAlarm: {
    		autoLoad: false,
            fields: ['name', 'data', 'percent'],
            listeners: {
                //beforeload: 'onBeforeLoad'
            }
        },

        averageOverspeedAlarm: {
            autoLoad: false,
            fields: ['name', 'data'],
            listeners: {
                //beforeload: 'onBeforeLoad'
            }
        },

        enterAlarm: {
            autoLoad: false,
            fields: ['name', 'data', 'percent'],
            listeners: {
                //beforeload: 'onBeforeLoad'
            }
        },

        averagEnterAlarm: {
            autoLoad: false,
            fields: ['name', 'data'],
            listeners: {
                //beforeload: 'onBeforeLoad'
            }
        },

        overborderAlarm: {
            autoLoad: false,
            fields: ['name', 'data', 'percent'],
            listeners: {
                //beforeload: 'onBeforeLoad'
            }
        },

        averageOverborderAlarm: {
            autoLoad: false,
            fields: ['name', 'data'],
            listeners: {
                //beforeload: 'onBeforeLoad'
            }
        },


        overborderMileage: {
            autoLoad: false,
            fields: ['name', 'data', 'percent'],
            listeners: {
                //beforeload: 'onBeforeLoad'
            }
        },

        averageOverborderMileage: {
            autoLoad: false,
            fields: ['name', 'data'],
            listeners: {
                //beforeload: 'onBeforeLoad'
            }
        },
        
    }
});