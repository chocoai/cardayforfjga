/**
 * This example shows simple checkbox selection in a tree. It is enabled on leaf nodes by
 * simply setting `checked: true/false` at the node level.
 *
 * This example also shows loading an entire tree structure statically in one load call,
 * rather than loading each node asynchronously.
 */
Ext.define('Admin.view.orgmgmt.arcmgmt.OrgTreeList', {
    extend: 'Ext.tree.Panel',
    id: 'orgTreeList',
    
    xtype: 'check-tree',
    listeners:{
        itemclick: 'onItemClick',
    },
    rootVisible: false,
    useArrows: true,
    frame: true,
    title: '部门',
    width: 280,
    height: 300,
//    checkModel: 'single',
    multiSelect: true,
//    bufferedRenderer: false,
    animate: false,
    
    initComponent: function(){

        Ext.apply(this, {
            store: new Ext.data.TreeStore({
            	alias: 'store.orgTreeStore',
            	
//            	root : {
//            		expanded : true,
//            		children : [ {
//            			id : '1',
//            			text : '企业1',
//            			leaf : false,
//            			checked : false,
//            			children : [ {
//            				id : '2',
//            				text : '研发部',
//            				leaf : false,
//            				checked : false,
//            				children: [{
//            					id:'4',
//            					text:'Java',
//            					children:null,
//            					leaf:true,
//            					checked:false
//            				},{
//            					id:'29',
//            					text:'Ext',
//            					children:null,
//            					leaf:true,
//            					checked:false
//            				}]
//            			}, {
//            				id : '23',
//            				text : '代表处',
//            				leaf : true,
//            				checked : false
//            			}, {
//            				id : '30',
//            				text : '市场部',
//            				leaf : true,
//            				checked : false
//            			} ]
//            		},
//            		{
//            			id : '200',
//            			text : '中移德电2',
//            			leaf : false,
//            			children : [ {
//            				id : '201',
//            				text : '代表处2',
//            				leaf : true
//            			}, {
//            				id : '202',
//            				text : '市场部2',
//            				leaf : true
//            			}, {
//            				id : '203',
//            				text : '研发部2',
//            				leaf : true
//            			} ]
//            		}
//            		]
//            	},
            	
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
                		var organizationId = '';
                		if(successful) {
                			for(var i=0;i<records.length;i++) {
                				console.log(records[i].id);
                				if(records[i].checked = 'true') {
                					organizationId = records[i].id;
                					break;
                				}
                			}
                			//加载右侧部门详细信息
                			var input = {"orgId" : organizationId};
                			console.log('object::' + Ext.getCmp("orgDepartmentGridId").getStore('arcMgmtResults'));
                			Ext.getCmp("orgDepartmentGridId").getViewModel().getStore('arcMgmtResults').proxy.url = 'department/listDirectChildrenWithCountByOrgId?json=' + Ext.encode(input),
                			Ext.getCmp("orgDepartmentGridId").getViewModel().getStore('arcMgmtResults').load();
                		}
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
        });
        this.callParent();
    }, 
//    onCheckedNodesClick: function(){
//        var records = this.getView().getChecked(),
//            names = [];
//                   
//        Ext.Array.each(records, function(rec){
//            names.push(rec.get('text'));
//        });
//                    
//        Ext.MessageBox.show({
//            title: 'Selected Nodes',
//            msg: names.join('<br />'),
//            icon: Ext.MessageBox.INFO
//        });
//    }
});