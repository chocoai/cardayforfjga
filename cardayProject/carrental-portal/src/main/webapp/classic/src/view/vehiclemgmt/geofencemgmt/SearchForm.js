/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.geofencemgmt.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    id:'geofencesearchForm',
    reference: 'searchForm',
    //bodyPadding: 8,
    header: false,
    
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    margin: '0 0 20 0', //对panel生效
    
    items: [
	{
        xtype: 'form',
        layout: 'hbox',
        flex:1,
        items: [{
            name:'geofenceName',
            xtype: 'textfield',
            emptyText:'地理围栏名称',
            allowBlank: true, 
            width:220
        	},{
        	xtype:'button',
        	text: '<i class="fa fa-search"></i>&nbsp;查询',
        	handler: 'searchByGeofenceName'
        }]
	},
	{
    	xtype:'container',
    	flex:1,
    	items:[{
    		xtype:'button',
    		text:'新增地理围栏',
    		style:'float:right',
    	//	margin: '0 0 0 620',
    		handler:'onAddGeofenceClick'
    	}]
	}
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
