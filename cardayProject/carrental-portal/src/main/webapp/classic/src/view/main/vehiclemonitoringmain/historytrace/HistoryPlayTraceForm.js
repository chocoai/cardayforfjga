/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.main.vehiclemonitoringmain.historytrace.HistoryPlayTraceForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    bodyPadding: 1,
    header: false,
    fieldDefaults: {
        // labelAlign: 'right',
        labelWidth: 100,
        margin: '0 0 0 0', //label和上一个输入框的样式，间隔5
        msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
    },
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    margin: '-2 0 0 0', //对panel生效
    
    items: [
	{
        xtype: 'form',
        layout: {
	        type: 'table',
	        columns: 8,
    	},
//        flex:4,
        items: [
           {
        	   xtype: 'displayfield',
               value : '轨迹回放：',
               labelHide:true,
               margin: '3 0 0 0',
               width: 74,
           }, {
        	   xtype: 'button',
        	   text: '<i class="fa fa-play"></i>',
        	   width: 40,
        	   disabled:true,
        	   id: 'vehMoniTracePlayButton',
        	   margin: '3 4 3 0',
        	   handler: 'playAction'
           }, {
        		xtype: 'button',
                text: '<i class="fa fa-pause"></i>',
                width: 40,
                id: 'vehMoniTracePauseButton',
                disabled: true,
                margin: '3 4 3 0',
                handler: 'pauseAction'
           }, {
        	   xtype: 'button',
               text: '<i class="fa fa-stop"></i>',
               width: 40,
               id: 'vehMoniTraceStopButton',
               disabled: true,
               margin: '3 4 3 0',
               handler: 'stopAction'
           }, {
        	   xtype: 'button',
               text: '<i class="fa fa-fast-backward"></i>',
               width: 40,
               id: 'vehMoniTraceBackwardButton',
               disabled: true,
               margin: '3 4 3 0',
               handler: 'backwardAction'
           }, {
        	   xtype: 'button',
               text: '<i class="fa fa-fast-forward"></i>',
               width: 40,
               id: 'vehMoniTraceForwardButton',
               margin: '3 4 3 0',
               disabled: true,
               handler: 'forwardAction',
//               handler: 'executeRunner',
           }, { 
        	   xtype: 'button',
        	   id: 'vehMoniTraceResetButton',
               text: '<i class="fa fa-refresh"></i>',
               width: 40,
               disabled: true,
               margin: '3 4 3 0',
               handler: 'refreshAction'
           }
		   
        ]
	},
//	{
//    	xtype:'container',
//    	flex:1,
//    	items:[{
//    		xtype:'button',
//    		text: '<i class="fa fa-search"></i>&nbsp;查询',
//    		style:'float:right',
//    		handler:'tracePlayback',
//    	}]
//	}
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
