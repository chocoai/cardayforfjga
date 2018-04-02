Ext.define('Admin.model.enterinfo.Rent', {
    extend: 'Admin.model.Base',

    fields: [{
	            type: 'int',
	            name: 'id'
        	},{
	        	type: 'string',
	            name: 'linkman'
        	},{
	        	type: 'string',
	            name: 'linkmanPhone'
        	},{
	        	type: 'string',
	            name: 'linkmanEmail'
        	}]
});
