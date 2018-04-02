/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.TraceSearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    reference: 'traceSearchForm',
    bodyPadding: 1,
    header: false,
    fieldDefaults: {
        // labelAlign: 'right',
        labelWidth: 65,
        margin: '0 0 0 0', //label和上一个输入框的样式，间隔5
        msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
    },
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    margin: '0 0 5 0', //对panel生效
    
    items: [
	{
        xtype: 'form',
        layout: {
            type: 'column',
        },
        flex:4,
        items: [{
            name:'userID',
            fieldLabel: '选择日期',
            xtype: 'datefield',
			format: 'Y-m-d',
        	}/*,{
        	xtype:'button',
        	text: '<i class="fa fa-search"></i>&nbsp;查询',
        	style:'float:right;width:100px;left:-69px;top:10px;',
        	handler: 'onSearchClick',
        }*/
        ]
	},
	{
    	xtype:'container',
    	flex:1,
    	items:[{
    		xtype:'button',
    		text: '<i class="fa fa-search"></i>&nbsp;轨迹回放',
    		style:'float:right',
    		handler:'tracePlayback'
    	}]
	}
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
