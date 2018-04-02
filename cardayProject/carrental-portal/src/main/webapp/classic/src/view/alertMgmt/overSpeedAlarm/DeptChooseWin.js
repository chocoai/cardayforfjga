/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.alertMgmt.overSpeedAlarm.DeptChooseWin', {
	extend: 'Ext.window.Window',
	requires : [ 'Ext.form.Panel'
	],
	id:'overSpeedAlarmChooseDeptWin',
	controller:{
		xclass:'Admin.view.alertMgmt.overSpeedAlarm.ViewController'
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
            		var win = Ext.getCmp("overSpeedAlarmChooseDeptWin");
            		if(win == null){
            			return;
            		}
            		var deptId = win.deptId;
            		if(deptId == null){
            			return;
            		}
            		var record = store.getNodeById(deptId);
            		if(record != null){
            			win.down("treepanel").getSelectionModel().select(record);
        				record.expand();
        			}
            		
            		//展开选中节点的父节点
            		var tree = win.down("treepanel");
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
