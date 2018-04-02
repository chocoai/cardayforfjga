Ext.define('Admin.view.orgmgmt.rentinfomgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.rentinfomgmt.View'
			],
	onView:function(){
		this.getViewModel().getStore('rentsResults').load();
	}
});
