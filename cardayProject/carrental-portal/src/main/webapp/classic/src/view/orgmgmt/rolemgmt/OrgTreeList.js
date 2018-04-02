Ext.define('Admin.view.orgmgmt.rolemgmt.OrgTreeList', {
	extend:'Ext.tree.Panel', 
	requires: [
	           'Admin.view.orgmgmt.rolemgmt.TreeStore',
	           'Admin.view.orgmgmt.rolemgmt.ViewController'
	],
//    renderTo: Ext.getBody(),
    title: '部门',
    listeners:{
//    	itemclick : function(node,e){
//                alert(e.id); 
//        },
        itemclick: 'onItemClick'
},
    bind: {
        store: {type: "roleTreeStore"}
    },
    width: 175,
    height: 150,
    rootVisible:false
//    root: {
//        text: 'Root',
//        id : 'OrgTreeList',
//        expanded: true,
//        children: [
//            {
//                text: '节点1',
//                leaf: true
//            },
//            {
//                text: '节点2',
//                leaf: true
//            },
//            {
//                text: '节点3',
//                expanded: true,
//                children: [
//                    {
//                        text: '孙子节点',
//                        leaf: true
//                    },
//                ]
//            },
//            {
//                text: '节点4',
//                expanded: true,
//                children: [
//                    {
//                        text: '孙子节点',
//                        expanded: true,
//                        children: [
//                                   {
//                                       text: '孙子节点',
//                                       leaf: true
//                                   },
//                               ]
//                    },
//                ]
//            }
//        ]
//    }
});