Ext.define('Admin.view.reportmgmt.deptreportexception.View', {
	extend: 'Ext.tab.Panel',
    xtype: 'deptreportexception',
	reference: 'deptreportexception',
    id:'deptReportExceptionTab',

    controller: {
        xclass: 'Admin.view.reportmgmt.deptreportexception.ViewController'
    },

    viewModel: {
        xclass: 'Admin.view.reportmgmt.deptreportexception.ViewModel'
    },

    bodyPadding: 20,
    items: [{
            title:'超速报警',
        	xclass: 'Admin.view.reportmgmt.deptreportexception.OverSpeedView',
    		},
           {
            title:'越界报警',
            xclass: 'Admin.view.reportmgmt.deptreportexception.OutMarkerView',
            },
           {
            title:'回车报警',
            xclass: 'Admin.view.reportmgmt.deptreportexception.BackStationView',
            }
],
    initComponent: function() {
        this.callParent();
    }
});