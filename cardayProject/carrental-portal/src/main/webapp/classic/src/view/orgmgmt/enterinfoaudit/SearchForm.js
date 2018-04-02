/**
 * 搜索公司名称
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.enterinfoaudit.SearchForm', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    header: false,
   /* fieldDefaults: {
        // labelAlign: 'right',
        labelWidth: 40,
        margin: '0 20 0 0', //label和上一个输入框的样式，间隔5
        msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
    },*/
   margin: '0 0 20 0', //对panel生效
    items: [{
        xtype: 'form',
        reference: 'searchEnterAudit',
        layout: 'hbox',
        flex:1,
       /* defaults:{
        	margin:'0 0 0 10'
        },*/
        items: [{
            name:'name',
            xtype: 'textfield',
            emptyText:'公司名称',
            width:220
        	},{
        	xtype:'button',
        	text: '<i class="fa fa-search"></i>&nbsp;查询',
        	handler:'onSearchClick'
        }]
    }],
    initComponent: function() {
        this.callParent();
    }
});
