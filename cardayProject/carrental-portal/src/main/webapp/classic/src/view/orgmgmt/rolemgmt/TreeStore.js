Ext.define('Admin.view.orgmgmt.rolemgmt.TreeStore', {
	extend : 'Ext.data.TreeStore',
	alias : 'store.roleStore',
	root : {
		expanded : true,
		children : [ {
			id : '1',
			text : '中移德电',
			leaf : false,
			children : [ {
				id : '11',
				text : '代表处',
				leaf : true
			}, {
				id : '12',
				text : '市场部',
				leaf : true
			}, {
				id : '13',
				text : '研发部',
				leaf : true
			} ]
		}
//		, {
//			id : '2',
//			text : '部门2',
//			leaf : false,
//			children : [ {
//				id : '21',
//				text : '子部门1',
//				leaf : true
//			}, {
//				id : '22',
//				text : '子部门2',
//				leaf : true
//			} ]
//		}, {
//			id : '3',
//			text : '部门3',
//			leaf : true
//		} 
		]
	}
//    proxy: {
//         type: 'ajax',
//    //     url: 'Tree/getTreeList.do'
//         url: 'test.json'
//    },
//    reader : {
//          type : 'json'
//    },
//    root: {
//         text: '树根',
//         id: '00',
//         expanded: true
//    },
//    folderSort: true,
////         sorters: [{
////            property: 'id',
////            direction: 'ASC'
////     }]

});