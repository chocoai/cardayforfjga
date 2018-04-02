/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.syncUsermgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.orgmgmt.syncUsermgmt.Grid',
			],
			init: function(view) {
		        this.roleType = window.sessionStorage.getItem('userType');
		        this.getViewModel().set('status',this.roleType);
		    },
			onSearchClick:function(){
				var syncUserId = this.lookupReference('syncUserId').getStore();
				syncUserId.currentPage = 1;
				this.getViewModel().getStore('syncUserResult').load();
			},
			onBeforeload: function(){
				var page=Ext.getCmp('syncUserPage').store.currentPage;
				var limit=Ext.getCmp('syncUserPage').pageSize;
				this.getRecordList(page, limit);
			},			
			getRecordList: function(page_number, page_size) {
				this.getViewModel().getStore("syncUserResult").proxy.url='https://www.car-day.cn/boss-order/ws/boss/internal/customer/'+page_number+ '/'+ page_size+'/get';
//				this.getViewModel().getStore("syncUserResult").proxy.url='https://uat.car-day.cn/boss-order/ws/boss/internal/customer/'+page_number+ '/'+ page_size+'/get';				
				// this.getViewModel().getStore("syncUserResult").proxy.url='http://172.16.1.114:8888/boss-order/ws/boss/internal/customer/'+page_number+ '/'+ page_size+'/get';

            }
});
