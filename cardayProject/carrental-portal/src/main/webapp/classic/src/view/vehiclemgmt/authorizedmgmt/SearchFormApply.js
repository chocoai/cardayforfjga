Ext.define('Admin.view.vehiclemgmt.authorizedmgmt.SearchFormApply', {
    extend: 'Ext.form.Panel',
    requires: ['Ext.layout.container.HBox', 
                'Ext.form.field.ComboBox'],
    
    reference: 'searchFormApply',
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    header: false,
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    items: [
    {
        xtype:'container',
        margin:'0 0 20 0',
        //flex:1,
        items:[{
            xtype:'button',
            text: "新增编制调整申请单",
            //style:'float:right',
            handler:'addAuthorizedVehApply'
        }]
    }],
    initComponent: function() {
        this.callParent();
    }
});
