Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.creditlimitmgmt.ShowCreditLimitMgmt', {
	extend: 'Ext.panel.Panel',
	
	reference: 'showCreditLimitMgmt',

    controller: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.creditlimitmgmt.CreditLimitMgmtController'
    },

	id: 'showCreditLimitMgmt',

	listeners:{
    	activate: 'onActivateCreditLimit',
    },
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [
   			{
        	xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.creditlimitmgmt.GridCreditLimitMgmt',
        	frame: true
    		}],

    dockedItems : [{
        xtype : 'toolbar',
        dock : 'bottom',
        ui : 'footer',
        style : "background-color:#FFFFFF",
        items : [
                '->', {
                    text : '保存',
                    id:'saveCreditlimit',
                    width:100,
                    margin:20,
                    handler : 'saveCreditlimit'
                }, {
                    text : '取消',
                    width:100,
                    margin:20,
                    handler : 'cancelCreditlimit'
                },'->']
    }],
    initComponent: function() {
        this.callParent();
    }
});