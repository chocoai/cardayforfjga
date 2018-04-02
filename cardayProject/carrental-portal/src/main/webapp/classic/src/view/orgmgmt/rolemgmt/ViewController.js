/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.rolemgmt.ViewController', {
    extend: 'Ext.app.ViewController',
    requires: [
    ],
    
    addRoleClick: function() {
    	win = Ext.widget('roleView');
		win.show();
    }
});

