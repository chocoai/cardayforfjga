/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.deptinfo.DeptChooseWin', {
	extend: 'Ext.window.Window',
	requires : [ 'Ext.form.Panel'
	],
	id:'deptMgmtChooseDeptWin',
	controller:{
		xclass:'Admin.view.orgmgmt.arcmgmt.deptmgmt.deptinfo.DeptInfoController'
	},
	reference : 'searchForm',
	bodyPadding : 10,
	bodyPadding: 10,
    constrain: true,
    closable: true,
    resizable: false,
    modal: true,
    width:500,
    height:400,
    scrollable:'y',
	title: '选择部门',
	bodyPadding:20,
    items: [{
    	xtype:'treepanel',
    	id:'deptChooseWinTree',
    	listeners:{
    		beforeselect:'preventSelfDeptSelect',
    		afterrender: function(treepanel){
//    			treepanel.getStore().load();
    		}
    	},
    	frame:false,
    	rootVisible:false,
    	store: new Ext.data.TreeStore({
        	alias: 'store.orgTreeStore',
//        	autoLoad:false,
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
            		var parentId = Ext.getCmp("deptMgmtChooseDeptWin").parentId;
            		var record = store.getNodeById(parentId);
            		if(record != null){
        				Ext.getCmp("deptChooseWinTree").getSelectionModel().select(record);
        				record.expand();
        			}
            		
//            		var id = Ext.getCmp("deptMgmtChooseDeptWin").id;
//            		var self = store.getNodeById(id);
//            		if(self != null){
//            			self.text="12345";
//            			self.cls = "disable_treenode_select";
//            	        store.load({node:self});
//        			}
            	}
            },
           /* sorters: [{
                property: 'id',
                direction: 'ASC'
            }, {
                property: 'text',
                direction: 'ASC'
            }],*/
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
