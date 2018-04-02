/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemconfiguration.vehPurchaseConfiguration.AdminChooseWin', {
	extend: 'Ext.window.Window',
	requires : [ 'Ext.form.Panel'
	],
	id:'vehPurchaseAdminChooseWin',
	controller:{
		xclass:'Admin.view.systemconfiguration.vehPurchaseConfiguration.ViewController'
	},
	reference : 'vehPurchaseAdminChooseWin',
	bodyPadding : 10,
	bodyPadding: 10,
    constrain: true,
    closable: true,
    resizable: false,
    modal: true,
    width:500,
    height:400,
    scrollable:'y',
	title: '选择员工',
	bodyPadding:20,
    items: [{
    	xtype:'treepanel',
        id:'vehPurchaseChooseAdminWinTree',
    	frame:false,
    	rootVisible:false,
    	store: new Ext.data.TreeStore({
            proxy: {
                type: 'ajax',
                url: 'app/data/systemconfiguration/vehPurchaseAdminChooseTree.json',
                	reader: {
       	             type: 'json',
       	             rootProperty: 'children',
       	         },
            },
        }),
    }],
    buttons:[{
    	text:'确定',
    	listeners:{
    		click:'chooseAdmin',
    	}
    },{
    	text:'取消',
    	handler:function(){
    		this.up("window").close();
    	}
    }],
	initComponent : function() {
		this.callParent();
	}
});
