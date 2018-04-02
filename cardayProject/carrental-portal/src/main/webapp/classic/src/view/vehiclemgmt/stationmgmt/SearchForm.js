/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.stationmgmt.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    id:'stationsearchForm',
    reference: 'searchForm',
    //bodyPadding: 8,
    header: false,
    
//    fieldDefaults: {
//        // labelAlign: 'right',
//        labelWidth: 40,
//        margin: '0 20 0 0', //label和上一个输入框的样式，间隔5
//        msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
//    },
    
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    margin: '0 0 20 0', //对panel生效
    
    items: [
	{
        xtype: 'form',
        layout: 'hbox',
        flex:1,
        items: [{
            name:'stationName',
            xtype: 'textfield',
            emptyText:'站点名称',
            allowBlank: true, 
            width:220
        	},{
        	xtype:'button',
        	text: '<i class="fa fa-search"></i>&nbsp;查询',
        	handler: 'searchByStationName'
        }]
	},
	{
    	xtype:'container',
    	flex:1,
    	items:[{
    		xtype:'button',
    		text:'新增站点',
    		style:'float:right',
    	//	margin: '0 0 0 620',
    		handler:'onAddClick'
    	}]
	}
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
