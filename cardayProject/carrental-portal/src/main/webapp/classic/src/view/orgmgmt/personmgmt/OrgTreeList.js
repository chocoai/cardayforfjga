Ext.define('Admin.view.orgmgmt.personmgmt.OrgTreeList', {
	extend:'Ext.tree.Panel', 
	requires: [
	           'Admin.view.orgmgmt.personmgmt.ViewController'
	],
//    renderTo: Ext.getBody(),
    title: '部门',
    listeners:{
        itemclick: 'onItemClick'
},
//    bind: {
//        store: {type: "personTreeStore"}
//    },
    rootVisible: false,
    id:'personmgmtTreeId',
    useArrows: true,
    frame: true,
    title: '部门',
    width: 280,
    height: 300,
    bufferedRenderer: false,
    animate: true,
    checkModelType:'single' ,
    onlyLeafCheckable: true, 
//    checkPropagation: 'none',
    
    initComponent: function(){

        Ext.apply(this, {
            store: new Ext.data.TreeStore({
            	alias: 'store.personmgmtTreeStore',
            	
            	
                proxy: {
                    type: 'ajax',
//                    url: 'app/data/arcmgmtinfo/arctreeData.json'
                    url: 'organization/tree'
                },
                sorters: [{
                    property: 'leaf',
                    direction: 'ASC'
                }, {
                    property: 'text',
                    direction: 'ASC'
                }]
//            	root : {
//            		expanded : true,
//            		children : [ {
//            			id : '1',
//            			text : '中移德电',
//            			leaf : false,
//            			children : [ {
//            				id : '11',
//            				text : '代表处',
//            				leaf : true
//            			}, {
//            				id : '12',
//            				text : '市场部',
//            				leaf : true
//            			}, {
//            				id : '13',
//            				text : '研发部',
//            				leaf : true
//            			} ]
//            		}
//            		]
//            	}
            }),
        });
        this.callParent();
    },
    
});