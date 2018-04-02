/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.rulemgmt.locationmgmt.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    reference: 'searchForm',
    //bodyPadding: 8,
    header: false,

    id:'locationsearchForm',
    
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
            name:'locationName',
            xtype: 'textfield',
            emptyText:'用车位置名称',
            allowBlank: true, 
            width:220
        	},{
        	xtype:'button',
        	text: '<i class="fa fa-search"></i>&nbsp;查询',
        	handler: 'searchByLocationName'
        }]
	},
	{
    	xtype:'container',
    	flex:1,
    	items:[{
    		xtype:'button',
    		text:'新增用车位置',
    		style:'float:right',
    		handler:'onAddLocationClick'
    	}]
	}
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
