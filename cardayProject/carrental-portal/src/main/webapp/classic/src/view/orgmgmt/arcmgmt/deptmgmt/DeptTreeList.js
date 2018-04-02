/**
 * This example shows simple checkbox selection in a tree. It is enabled on leaf nodes by
 * simply setting `checked: true/false` at the node level.
 *
 * This example also shows loading an entire tree structure statically in one load call,
 * rather than loading each node asynchronously.
 */
Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.DeptTreeList', {
    extend: 'Ext.tree.Panel',
    
    listeners:{
    	select: 'onTreeSelect'
    },
    rootVisible: false,
    useArrows: true,
    frame: true,
    title: '组织',
    width: 280,
    height: 300,
    id:'deptMgmtTreeList',
//    checkModel: 'single',
//    multiSelect: true,
//    bufferedRenderer: false,
    animate: false,
    header:false,
    lastSelectedId:null,
    dockedItems: [{
    	xtype:'container',
    	bodyPadding: 5,
    	layout:'auto',
    	dock: 'top',
    	items:[{
    		xtype:'button',
    		text:'<i class="fa fa-plus"></i>&nbsp;新建组织',
    		margin:5,
    		handler:'addDept'
    	}]
    }],
    initComponent: function(){

        Ext.apply(this, {
            store: new Ext.data.TreeStore({
            	alias: 'store.orgTreeStore',
                proxy: {
                    type: 'ajax',
                    url: 'department/tree',
//                    url: 'app/data/arcmgmtinfo/arctreeData.json',
                    	reader: {
           	             type: 'json',
           	             rootProperty: 'children',
//           	             successProperty: 'success'
           	         },
                },
                listeners : {
                	load : function(thisstore, records, successful, eOpts) {
//                		var organizationId = '';
//                		if(successful) {
//                			for(var i=0;i<records.length;i++) {
//                				console.log(records[i].id);
//                				if(records[i].checked = 'true') {
//                					organizationId = records[i].id;
//                					break;
//                				}
//                			}
//                			//加载右侧部门详细信息
//                			var input = {"orgId" : organizationId};
//                			console.log('object::' + Ext.getCmp("orgDepartmentGridId").getStore('arcMgmtResults'));
//                			Ext.getCmp("orgDepartmentGridId").getViewModel().getStore('arcMgmtResults').proxy.url = 'department/listDirectChildrenWithCountByOrgId?json=' + Ext.encode(input),
//                			Ext.getCmp("orgDepartmentGridId").getViewModel().getStore('arcMgmtResults').load();
//                		}
//                	}
//                	load:function(){
                		var tree = Ext.getCmp("deptMgmtTreeList");
                		if(tree == null){
                			return;
                		}
                		var selectedId = tree.lastSelectedId;
                		
                		if(selectedId == null){
                			tree.getSelectionModel().select(0);
            			}else{
            				var record = tree.getStore().getNodeById(selectedId);
                			if(record != null){
                				tree.getSelectionModel().select(record);
                				record.expand();
                			}else{
                				tree.getSelectionModel().select(0);
                			}
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
//                		Ext.getCmp("deptMgmtTreeList").getSelectionModel().select(0);
                	}
                },
              /*  sorters: [{
                    property: 'id',
                    direction: 'ASC'
                }, {
                    property: 'text',
                    direction: 'ASC'
                }],*/
            }),
        });
        this.callParent();
    }, 
});