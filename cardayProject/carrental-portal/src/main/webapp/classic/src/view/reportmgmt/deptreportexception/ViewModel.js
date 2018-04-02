/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.reportmgmt.deptreportexception.ViewModel', {
    extend: 'Ext.app.ViewModel',
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
        overSpeedStatistics : {
        	model: 'Admin.model.reportmgmt.deptreportexception.LineModel',
            autoLoad: false,
            remoteFilter: true,
            listeners: {
                beforeload: 'onBeforeLoadOverSpeedUse'
            }
        },

        outMarkerStatistics : {
            model: 'Admin.model.reportmgmt.deptreportexception.LineModel',
            autoLoad: false,
            remoteFilter: true,
            listeners: {
                beforeload: 'onBeforeLoadOutMarkerUse'
            }
        },

        backStationStatistics : {
            model: 'Admin.model.reportmgmt.deptreportexception.LineModel',
            autoLoad: false,
            remoteFilter: true,
            listeners: {
                beforeload: 'onBeforeLoadBackStationUse'
            }
        },
        
        overSpeedList: {
            model: 'Admin.model.reportmgmt.deptreportexception.List',
            pageSize: 10,
            autoLoad: false,
            remoteSort: false,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeOverSpeedList'
            }
        },

        outMarkerList: {
            model: 'Admin.model.reportmgmt.deptreportexception.List',
            pageSize: 10,
            autoLoad: false,
            remoteSort: false,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeOutMarkerList'
            }
        },

        backStationList: {
            model: 'Admin.model.reportmgmt.deptreportexception.List',
            pageSize: 10,
            autoLoad: false,
            remoteSort: false,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeBackStationList'
            }
        },
        
        myCompanyFlowReportMonth: {
    		autoLoad: true,
    	    fields: ['month', 'data1'],
    	    
		    data: [
			        { month: 'Jan',     data1: 20},
			        { month: 'Feb',     data1: 20},
			        { month: 'Mar',     data1: 19},
			        { month: 'Apr',     data1: 18},
			        { month: 'May',     data1: 18},
			        { month: 'Jun',     data1: 17},
			        { month: 'Jul',     data1: 16},
			        { month: 'Aug',     data1: 16},
			        { month: 'Sep',     data1: 16},
			        { month: 'Oct',     data1: 16},
			        { month: 'Nov',     data1: 15},
			        { month: 'Dec',     data1: 15},
			    ]
        },
        
        myCompanyFlowReportDay: {
    		autoLoad: true,
    	    fields: ['month', 'data1'],
    	    
		    data: [
			        { month: '1',     data1: 20},
			        { month: '3',     data1: 20},
			        { month: '5',     data1: 19},
			        { month: '7',     data1: 18},
			        { month: '9',     data1: 18},
			        { month: '11',     data1: 17},
			        { month: '13',     data1: 16},
			        { month: '15',     data1: 16},
			        { month: '17',     data1: 16},
			        { month: '19',     data1: 16},
			        { month: '21',     data1: 15},
			        { month: '23',     data1: 15},
			        { month: '25',     data1: 16},
			        { month: '27',     data1: 16},
			        { month: '29',     data1: 15},
			        { month: '31',     data1: 15},
			    ]
        },
        
    }
});