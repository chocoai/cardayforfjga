Ext.define('Admin.view.vehiclespurchase.vehipurchaseapply.SearchFormApply', {
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
            text: "新增申请单",
            //style:'float:right',
            handler:'addVehiPurchase'
        }]
    }],
    initComponent: function() {
        this.callParent();
    }
});
