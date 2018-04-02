/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.reportmgmt.vehicleusereport.ViewModel', {
    extend: 'Ext.app.ViewModel',
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	totalMileage: {
    		autoLoad: false,
            fields: ['name', 'data', 'precent'],
            listeners: {
                //beforeload: 'onBeforeLoad'
            }
        },
        
        averageMileage: {
        	autoLoad: false,
            fields: ['name', 'data', 'precent'],
            listeners: {
                //beforeload: 'onBeforeLoad'
            }
        },
        
        totalFuel: {
    		autoLoad: false,
            fields: ['name', 'data', 'precent'],
            listeners: {
                //beforeload: 'onBeforeLoad'
            }
        },
        
        averageFuel: {
        	autoLoad: false,
            fields: ['name', 'data', 'precent'],
            listeners: {
                //beforeload: 'onBeforeLoad'
            }
        },
        
        totalTravelTime: {
    		autoLoad: false,
            fields: ['name', 'data', 'precent'],
            listeners: {
                //beforeload: 'onBeforeLoad'
            }
        },
        
        averageTravelTime: {
        	autoLoad: false,
            fields: ['name', 'data', 'precent'],
            listeners: {
                //beforeload: 'onBeforeLoad'
            }
        },
        
        vehicleUse : {
        	model: 'Admin.view.reportmgmt.vehicleusereport.VehicleUseDataModel',
            autoLoad: false,
            remoteFilter: true,
            listeners: {
                beforeload: 'onBeforeLoadVehicleUse',
                load: function(me,records,successful,operation,eOpts){
                    var rawData = me.getModel().getProxy().getReader().rawData;
                    if (rawData != undefined) {
                        Ext.getCmp('avgMileageText').setHtml("平均值："+rawData.data.data1avgval);
                        Ext.getCmp('avgFuelText').setHtml("平均值："+rawData.data.data2avgval);
                    }
                }
            }
        },
        
        
        vehicleList: {
            model: 'Admin.view.reportmgmt.vehicleusereport.VehicleDataModel',
            pageSize: 5,
            autoLoad: false,
            //remoteSort: false,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'ASC'
            }],
            listeners: {
                beforeload: 'onBeforeLoadVehicle'
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

/*    	    constructor: function (config) {
    	        config = config || {};

    	        config.data = [
    	            { month: 'Jan', data1: 20, data2: 37, data3: 35, data4: 4, other: 4 },
    	            { month: 'Feb', data1: 20, data2: 37, data3: 36, data4: 5, other: 2 },
    	            { month: 'Mar', data1: 19, data2: 36, data3: 37, data4: 4, other: 4 },
    	            { month: 'Apr', data1: 18, data2: 36, data3: 38, data4: 5, other: 3 },
    	            { month: 'May', data1: 18, data2: 35, data3: 39, data4: 4, other: 4 },
    	            { month: 'Jun', data1: 17, data2: 34, data3: 42, data4: 4, other: 3 },
    	            { month: 'Jul', data1: 16, data2: 34, data3: 43, data4: 4, other: 3 },
    	            { month: 'Aug', data1: 16, data2: 33, data3: 44, data4: 4, other: 3 },
    	            { month: 'Sep', data1: 16, data2: 32, data3: 44, data4: 4, other: 4 },
    	            { month: 'Oct', data1: 16, data2: 32, data3: 45, data4: 4, other: 3 },
    	            { month: 'Nov', data1: 15, data2: 31, data3: 46, data4: 4, other: 4 },
    	            { month: 'Dec', data1: 15, data2: 31, data3: 47, data4: 4, other: 3 }
    	        ];

    	        this.callParent([config]);
    	    },*/
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