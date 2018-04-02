/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.empmgmt.DeptChooseWin', {
	extend: 'Ext.window.Window',
	requires : [ 'Ext.form.Panel'
	],
	id:'empmgmtChooseDeptWin',
	controller:{
		xclass:'Admin.view.systemmgmt.empmgmt.ViewController'
	},
	reference : 'searchForm',
    constrain: true,
    closable: true,
    resizable: false,
    modal: true,
    width:500,
    height:400,
    scrollable:'y',
	title: '选择机关',
	bodyPadding:20,
    items: [{
    	xtype:'treepanel',
    	frame:false,
    	rootVisible:false,
    	id:'empmgmtChooseDeptWinTree',
    	store: new Ext.data.TreeStore({
            proxy: {
                type: 'ajax',
                url: 'department/tree',
//                url: 'app/data/arcmgmtinfo/arctreeData.json',
                	reader: {
       	             type: 'json',
       	             rootProperty: 'children',
//       	             successProperty: 'success'
       	         },
            },
            listeners : {
            	load:function(store, records, successful, eOpts){
            		var parentId = Ext.getCmp("empmgmtChooseDeptWin").parentId;
            		if(parentId == null){
            			return;
            		}
            		var record = store.getNodeById(parentId);
            		if(record != null){
        				Ext.getCmp("empmgmtChooseDeptWinTree").getSelectionModel().select(record);
        				record.expand();
        			}
            		
            		//展开选中节点的父节点
            		var tree = Ext.getCmp("empmgmtChooseDeptWinTree");
            		if(tree == null){
            			return;
            		}
            		var selection = tree.getSelectionModel().getSelection();
                	if(selection.length == 0){
                		return;
                	}
                	var select = selection[0].getData();
                	if(select.parentId == "root"){
            			return;
            		}
                	if(select.leaf == true && select.parentId != null){
                		var record = tree.getStore().getNodeById(select.parentId);
            			if(record != null){
            				record.expand();
            			}
                	}
            	}
            },
        }),
    }],
    buttons:[{
    	text:'确定',
    	listeners:{
    		click:'chooseDept',
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
