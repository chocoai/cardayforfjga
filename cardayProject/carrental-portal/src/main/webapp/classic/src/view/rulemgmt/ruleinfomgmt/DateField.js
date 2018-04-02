Ext.define('Admin.view.rulemgmt.ruleinfomgmt.DateField', {
    extend: 'Ext.form.FieldContainer',
    xtype: 'ruleinfodatefield',

    layout: 'hbox',
    items: [
            {
                fieldLabel: '日期ID',
                xtype: 'textfield',
                blankText: '日期ID不能为空',
                name: 'dateId',
                labelWidth: 100,
                hidden:true
            },{
                name:'startdt',
                xtype: 'datefield',
                emptyText: '请选择开始日期',
                format: 'Y-m-d',
                itemId: 'startdt',
                endDateField: 'enddt',
                editable: false,
                margin: '0 30 0 0',
                value:new Date()
            },
            {
                name:'enddt',
                emptyText: '请选择结束日期',
                xtype: 'datefield',
                format: 'Y-m-d',
                itemId: 'enddt',
                startDateField: 'startdt',
                editable: false,
                margin: '0 50 0 0',
                value:new Date()
            },
            {
                fieldLabel: '自',
                xtype: 'timefield',
                name: 'startTime',
                format: 'H:i',
                value: '09:00',
                increment: 5,
                anchor: '100%',
                labelWidth:15,
                width:120,
                editable:false,
                margin: '0 30 0 0',
            },
            {
                fieldLabel: '至',
                xtype: 'timefield',
                name: 'endTime',
                format: 'H:i',
                value: '18:00',
                increment: 5,
                anchor: '100%',
                labelWidth:15,
                width:120,
                editable:false,
                margin: '0 50 0 0',
            },
            {
                xtype:'button',
                text: '删除',
                handler: function(me) {
                    Ext.getCmp('dateField').remove(me.up());
                }
            }
    ]
});
