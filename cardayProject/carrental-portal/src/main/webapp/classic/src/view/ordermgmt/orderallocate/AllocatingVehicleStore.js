/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.ordermgmt.orderallocate.AllocatingVehicleStore', {
	extend: 'Ext.data.Model',
	//alias: 'store.allocatingVehicleStore',

	 proxy: { //在base.js 里面有其他字段定义
	        type: 'ajax',
	        actionMethods: {
	            read: 'GET',
	            create: 'GET',
	            update: 'GET',
	            destroy: 'GET'
	        },
	        api: {
	            read: 'vehicle/listAvailableVehicleByOrder',

	        },
	        reader: {
	            type: 'json',
	            rootProperty: 'data.resultList',
	            successProperty: 'status',
	            totalProperty: 'data.totalRows'
	        },
	        writer: {
	            type: 'json',
	            writeAllFields: true,
	            encode: true,
	            rootProperty: 'data.resultList'
	        }
//	        ,
//	        listeners: {
//	            exception: function(proxy, response, operation) {
//	                Ext.MessageBox.alert("消息提示","操作失败!");
//	            }
//	        }
	    }
});
