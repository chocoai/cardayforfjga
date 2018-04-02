/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderallocate.VehicleScheduleView', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    reference: 'VehicleScheduleView',
//    bodyPadding: 8,
    header: false,
    id:'VehicleScheduleView',
    listeners:{
    	//afterrender: 'loadScheduleInfo',
    },
    fieldDefaults: {
        // labelAlign: 'right',
        labelWidth: 65,
        margin: '10 17 0 0', //label和上一个输入框的样式，间隔5
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
            type: 'column',
        },
        title:'车辆排期',
        flex:4,
        items: [{
    		xtype : "checkboxgroup",
    		id: 'lblOperationBehavior',
    		 name: 'lblOperationBehavior',
             itemCls: 'x-check-group-alt',
    		columns: 6,
    		items : [ 
    		      { boxLabel : '00:00-00:30', name: "vehicleSchedule", inputValue : '00:30'}, 
    		      { boxLabel : '00:30-01:00', name: "vehicleSchedule", inputValue : '01:00'},
    		      { boxLabel : '01:00-01:30', name: "vehicleSchedule", inputValue : '01:30', checked:true, style:'background-color:#ff3366;'},
    		      { boxLabel : '01:30-02:00', name: "vehicleSchedule", inputValue : '02:00'},
    		      { boxLabel : '02:00-02:30', name: "vehicleSchedule", inputValue : '02:30'},
    		      { boxLabel : '02:30-03:00', name: "vehicleSchedule", inputValue : '03:00'},
    		      { boxLabel : '03:00-03:30', name: "vehicleSchedule", inputValue : '03:30'},
    		      { boxLabel : '03:30-04:00', name: "vehicleSchedule", inputValue : '04:00'},
    		      { boxLabel : '04:00-04:30', name: "vehicleSchedule", inputValue : '04:30'},
    		      { boxLabel : '04:30-05:00', name: "vehicleSchedule", inputValue : '05:00'},
    		      { boxLabel : '05:00-05:30', name: "vehicleSchedule", inputValue : '05:30'},
    		      { boxLabel : '05:30-06:00', name: "vehicleSchedule", inputValue : '06:00'},
    		      { boxLabel : '06:00-06:30', name: "vehicleSchedule", inputValue : '06:30'},
    		      { boxLabel : '06:30-07:00', name: "vehicleSchedule", inputValue : '07:00'},
    		      { boxLabel : '07:00-07:30', name: "vehicleSchedule", inputValue : '07:30'},
    		      { boxLabel : '07:30-08:00', name: "vehicleSchedule", inputValue : '08:00'},
    		      { boxLabel : '08:00-08:30', name: "vehicleSchedule", inputValue : '08:30'},
    		      { boxLabel : '08:30-09:00', name: "vehicleSchedule", inputValue : '09:00'},
    		      { boxLabel : '09:00-09:30', name: "vehicleSchedule", inputValue : '09:30'},
    		      { boxLabel : '09:30-10:00', name: "vehicleSchedule", inputValue : '10:00'},
    		      { boxLabel : '10:00-10:30', name: "vehicleSchedule", inputValue : '10:30'},
    		      { boxLabel : '10:30-11:00', name: "vehicleSchedule", inputValue : '11:00'},
    		      { boxLabel : '11:00-11:30', name: "vehicleSchedule", inputValue : '11:30'},
    		      { boxLabel : '11:30-12:00', name: "vehicleSchedule", inputValue : '12:00'},
    		      { boxLabel : '12:00-12:30', name: "vehicleSchedule", inputValue : '12:30'},
    		      { boxLabel : '12:30-13:00', name: "vehicleSchedule", inputValue : '13:00'},
    		      { boxLabel : '13:00-13:30', name: "vehicleSchedule", inputValue : '13:30'},
    		      { boxLabel : '13:30-14:00', name: "vehicleSchedule", inputValue : '14:00'},
    		      { boxLabel : '14:00-14:30', name: "vehicleSchedule", inputValue : '14:30'},
    		      { boxLabel : '14:30-15:00', name: "vehicleSchedule", inputValue : '15:00'},
    		      { boxLabel : '15:00-15:30', name: "vehicleSchedule", inputValue : '15:30'},
    		      { boxLabel : '15:30-16:00', name: "vehicleSchedule", inputValue : '16:00'},
    		      { boxLabel : '16:00-16:30', name: "vehicleSchedule", inputValue : '16:30'},
    		      { boxLabel : '16:30-17:00', name: "vehicleSchedule", inputValue : '17:00'},
    		      { boxLabel : '17:00-17:30', name: "vehicleSchedule", inputValue : '17:30'},
    		      { boxLabel : '17:30-18:00', name: "vehicleSchedule", inputValue : '18:00'},
    		      { boxLabel : '18:00-18:30', name: "vehicleSchedule", inputValue : '18:30'},
    		      { boxLabel : '18:30-19:00', name: "vehicleSchedule", inputValue : '19:00'},
    		      { boxLabel : '19:00-19:30', name: "vehicleSchedule", inputValue : '19:30'},
    		      { boxLabel : '19:30-20:00', name: "vehicleSchedule", inputValue : '20:00'},
    		      { boxLabel : '20:00-20:30', name: "vehicleSchedule", inputValue : '20:30'},
    		      { boxLabel : '20:30-21:00', name: "vehicleSchedule", inputValue : '21:00'},
    		      { boxLabel : '21:00-21:30', name: "vehicleSchedule", inputValue : '21:30'},
    		      { boxLabel : '21:30-22:00', name: "vehicleSchedule", inputValue : '22:00'},
    		      { boxLabel : '22:00-22:30', name: "vehicleSchedule", inputValue : '22:30'},
    		      { boxLabel : '22:30-23:00', name: "vehicleSchedule", inputValue : '23:00'},
    		      { boxLabel : '23:00-23:30', name: "vehicleSchedule", inputValue : '23:30'},
    		      { boxLabel : '23:30-24:00', name: "vehicleSchedule", inputValue : '24:00'}
    		]
    	}
        ]
	}/*,
	{
    	xtype:'container',
    	flex:1,
    	items:[{
    		xtype:'button',
    		text: '<i class="fa fa-search"></i>&nbsp;查询',
    		style:'float:right',
    		handler:'onAddClick'
    	}]
	}*/
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
