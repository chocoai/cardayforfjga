Ext.define('Admin.view.rulemgmt.ruleinfomgmt.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.ruleinfomodel',

    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	ruleInfoResults: {
    		model: 'Admin.model.rulemgmt.ruleinfomgmt.RuleInfo',
            autoLoad: false,
            pageSize: 10,
            remoteFilter: true,
            sorters: [{
                property: 'ruleId',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeLoad'
            }
        },
    }
});
