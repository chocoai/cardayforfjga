Ext.define('Admin.model.enterinfo.User', {
    extend: 'Admin.model.Base',

    fields: [
        {
            type: 'int',
            name: 'id'
        },{
            type: 'string',
            name: 'name'
        },{
            type: 'string',
            name: 'shortname'
        },{
            type: 'string',
            name: 'address'
        },{
            type: 'string',
            name: 'introduction'
        },{
        	type: 'int',
            name: 'vehileNum'
        },{
        	type: 'string',
            name: 'linkman'
        },{
        	type: 'string',
            name: 'linkmanPhone'
        },{
        	type: 'string',
            name: 'linkmanEmail'
        },{
        	type: 'string',
            name: 'city'
    	},{
        	type: 'string',
            name: 'status'
        },{
            type: 'date',
            name: 'startTime'
        },{
            type: 'date',
            name: 'endTime'
        },{
            type: 'string',
            name: 'comments'
        }
    ]
});
