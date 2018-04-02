Ext.define('Admin.view.vehiclemgmt.stationmgmt.StationModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.stationmodel',

    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	stationsResults: {
            //model: 'Admin.store.vehiclemgmt.stationmgmt.Station',
    		type:'stationmgmt',
            autoLoad: false,
            pageSize: 10,
            //remoteSort: false,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeLoad'
            }
        },
    }
});
