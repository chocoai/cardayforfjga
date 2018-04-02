/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderrecreate.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'orderrecreate',
    requires: [
               'Ext.layout.container.VBox',
               'Ext.layout.container.Fit',
               'Ext.window.MessageBox',
               'Ext.grid.plugin.RowEditing',
               'Admin.view.ordermgmt.orderrecreate.SearchForm',
           ],
           
       controller: 'orderrecreatecontroller',
       viewModel: {
           xclass: 'Admin.view.ordermgmt.orderrecreate.OrderViewModel'
       },
       
       listeners:{
           afterrender: 'onSearchClick',
       },
       
       autoScroll: true,
       layout: {
           type: 'vbox',
           pack: 'start',
           align: 'stretch'
       },
       bodyPadding: 20,
//           defaults: {
//               frame: true,
//               collapsible: true,
//               margin: '0 0 3 0'
//           },
       items: [{
           xclass: 'Admin.view.ordermgmt.orderrecreate.SearchForm',
       }, {
           xclass: 'Admin.view.ordermgmt.orderrecreate.GridOrderList',
           frame: true
       }],
       initComponent: function() {
           this.callParent();
       }
});
