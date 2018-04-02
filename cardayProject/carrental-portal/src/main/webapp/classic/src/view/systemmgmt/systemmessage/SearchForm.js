/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.systemmessage.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    reference: 'searchForm',
    header: false,

    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    margin: '0 0 20 0', //对panel生效
    
    items: [
	{
		xtype:'container',
		flex:1,
		items:[{
			xtype:'button',
            id:'onAddClick',
			text: '新增公告',
			margin: '0 0 0 0',
			handler:'onAddClick',
            hidden:true
		},
		]
	}],
    
    initComponent: function() {
        this.callParent();
    }
});
