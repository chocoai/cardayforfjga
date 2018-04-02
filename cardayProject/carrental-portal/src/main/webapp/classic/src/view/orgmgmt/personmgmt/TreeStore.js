Ext.define('Admin.view.orgmgmt.personmgmt.TreeStore', {
	extend : 'Ext.data.TreeStore',
	alias : 'store.personTreeStore',
//	root : {
//		expanded : true,
//		children : [ {
//			id : '1',
//			text : '中移德电',
//			leaf : false,
//			children : [ {
//				id : '11',
//				text : '代表处',
//				leaf : true
//			}, {
//				id : '12',
//				text : '市场部',
//				leaf : true
//			}, {
//				id : '13',
//				text : '研发部',
//				leaf : true
//			} ]
//		}
	
	model: 'Admin.model.arcmgmt.arctree',

	proxy: {
      type: 'ajax',
      url: 'app/data/arcmgmtinfo/arctreeData.json',
      reader: {
          type: 'json',
          rootProperty: 'data',
          successProperty: 'success'
      }
  },
  sorters: [{
      property: 'leaf',
      direction: 'ASC'
  }, {
      property: 'text',
      direction: 'ASC'
  }]

//    autoLoad: 'true',

});