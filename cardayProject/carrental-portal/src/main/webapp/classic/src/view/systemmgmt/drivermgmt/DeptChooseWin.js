/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.drivermgmt.DeptChooseWin', {
	extend: 'Ext.window.Window',
	requires : [ 'Ext.form.Panel'
	],
	id:'drivermgmtChooseDeptWin',
	controller:{
		xclass:'Admin.view.systemmgmt.drivermgmt.ViewController'
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
    	frame:false,
    	rootVisible:false,
    	id:'drivermgmtChooseDeptWinTree',
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
            		var win = Ext.getCmp("drivermgmtChooseDeptWin");
            		if(win == null){
            			return;
            		}
            		var tree = Ext.getCmp("drivermgmtChooseDeptWinTree");
            		if(tree == null){
            			return;
            		}
            		var parentId = win.parentId;
            		if(parentId == null || parentId == "-1" || parentId == ""){
            			var parentName = win.parentName;
            			if(parentName == null){
            				return;
            			}
            			//如果有部门名称而没有ID，说明是企业，则选中默认节点
            			var userType = window.sessionStorage.getItem('userType');
            	      	if(userType == '2' || userType == '6'){
            	      		tree.getSelectionModel().select(0);
            	      	}
            			return;
            		}else{
            			var record = store.getNodeById(parentId);
                		if(record != null){
                			tree.getSelectionModel().select(record);
            				record.expand();
            			}
            		}
            		
            		
            		//展开选中节点的父节点
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
