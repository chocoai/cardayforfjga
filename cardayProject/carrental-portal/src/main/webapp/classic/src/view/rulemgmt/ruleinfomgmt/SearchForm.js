/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.rulemgmt.ruleinfomgmt.SearchForm', {
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
            xtype:'button',
            text:'新增用车规则',
            style:'float:right',
            handler:'onAddRuleInfoClick'
        }
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
