/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.personmgmt.SearchForm', {
	extend : 'Ext.form.Panel',
	xtype : 'personSearch',
	requires : [ 'Ext.layout.container.HBox',
	//        'Ext.form.field.Date',
	//        'Ext.form.FieldContainer',
	//        'Ext.form.field.ComboBox',
	//        'Ext.form.FieldSet'
	'Ext.button.Button' ],
	reference : 'searchForm',
	bodyPadding : 8,
	header : false,
	layout: {
        type: 'hbox',
        align: 'stretch'
    },
  //  border:true,
    margin: '20 0 0 0',
    items: [{
        xtype: 'form',
        layout: 'hbox',
        items: [{
            //id: 'imei',
            name:'userName',
            xtype: 'textfield',
            emptyText:'姓名',
    //        labelWidth: 60,
            width:220
     //       fieldLabel: 'Obd NO',
        },{
        	xtype:'button',
        text: '<i class="fa fa-search"></i>&nbsp;查询',
        //用iconCls会出现图标偏移
     //   width: 60,
    //    height: 30,
        handler: 'searchClick'
        }]
    },{
    	xtype:'button',
    	text:'添加',
    	margin: '0 0 0 680',
    	width: 80,
    	handler:'addPersonClick'
    }],
	initComponent : function() {
		this.callParent();
	}
});
