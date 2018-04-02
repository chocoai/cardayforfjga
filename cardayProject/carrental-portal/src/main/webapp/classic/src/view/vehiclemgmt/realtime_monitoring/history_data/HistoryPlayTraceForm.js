/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.history_data.HistoryPlayTraceForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    controller: 'historyViewController',
    reference: 'historyPlayTraceForm',
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
    margin: '0 0 0 0', //对panel生效
    
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
        	   xtype: 'field',
               value : '轨迹回放',
               width: 75,
           }, {
        	   xtype: 'button',
        	   text: '<i class="fa fa-play"></i>&nbsp;开始/继续',
        	   width: 100,
        	   id: 'historyTracePlayButton',
        	   margin: '10 10 10 10',
        	   handler: 'playAction'
           }, {
        		xtype: 'button',
                text: '<i class="fa fa-pause"></i>&nbsp;暂停',
                width: 100,
                id: 'historyTracePauseButton',
                id: 'historyTracePauseButton',
                disabled: true,
                margin: '10 10 10 10',
                handler: 'pauseAction'
           }, {
        	   xtype: 'button',
               text: '<i class="fa fa-stop"></i>&nbsp;停止',
               width: 100,
               id: 'historyTraceStopButton',
               disabled: true,
               margin: '10 10 10 10',
               handler: 'stopAction'
           }, {
        	   xtype: 'button',
               text: '<i class="fa fa-fast-backward"></i>&nbsp;快退',
               width: 100,
               id: 'historyTraceBackwardButton',
               disabled: true,
               margin: '10 10 10 10',
               handler: 'backwardAction'
           }, {
        	   xtype: 'button',
               text: '<i class="fa fa-fast-forward"></i>&nbsp;快进',
               width: 100,
               id: 'historyTraceForwardButton',
               margin: '10 10 10 10',
               disabled: true,
               handler: 'forwardAction',
//               handler: 'executeRunner',
           }, { 
        	   xtype: 'button',
        	   id: 'historyTraceResetButton',
               text: '<i class="fa fa-refresh"></i>&nbsp;重新开始',
               width: 100,
               disabled: true,
               margin: '10 10 10 10',
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
