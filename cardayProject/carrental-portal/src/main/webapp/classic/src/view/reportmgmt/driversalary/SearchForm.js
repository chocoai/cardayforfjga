/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.driversalary.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    id: 'driversalarySearchForm',
    reference: 'searchForm',
    bodyPadding: 1,
    header: false,
    fieldDefaults: {
        // labelAlign: 'right',
        labelWidth: 65,
        msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
    },
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    
    items: [
	{
		xtype: 'form',
        layout: 'hbox',
        flex:1.2,
        items: [
        {
            name:'startDate',
            fieldLabel: '开始日期',
            xtype: 'datefield',
            emptyText: '请选择开始日期',
            format: 'Y-m-d',
            vtype: 'daterange',
            itemId: 'startdt',
            endDateField: 'enddt',
            editable: false,
            maxValue : new Date(),
            width: 215,
        },
        {
            margin: '0 0 0 7',
            name:'endDate',
            fieldLabel: '结束日期',
            emptyText: '请选择结束日期',
            xtype: 'datefield',
            format: 'Y-m-d',
            vtype: 'daterange',
            itemId: 'enddt',
            startDateField: 'startdt',
            editable: false,
            maxValue : new Date(),
            width: 215,
        },{
                xtype:'textfield',
                name:'deptId',
                hidden:true,
                value: window.sessionStorage.getItem("organizationId"),               
                listeners:{
                    afterrender: function(text){
                        text.setValue(window.sessionStorage.getItem("organizationId"));
                    }
                },
            },{
                margin: '0 0 0 7',
                xtype : 'combo',
                name:'deptName',
                fieldLabel : '所属部门',
                itemId : 'deptId',
                editable : false,
                emptyText : '请选择...',
                value: window.sessionStorage.getItem("organizationName"),
                listeners:{
                    expand: 'openDeptChooseWin',
                    afterrender: function(combo){
                        console.log("name: " + window.sessionStorage.getItem("organizationName"));
                        console.log("Id: " + window.sessionStorage.getItem("organizationId"));
                        combo.setValue(window.sessionStorage.getItem("organizationName"));
                    }
                },
                width: 220,
            }, 
        {
            //margin: '0 0 0 20',
            xtype:'button',
            text:'<i class="fa fa-search"></i>&nbsp;查询',
            width:75,
            handler:'onSearchClick'
        }
        /*,
        {
            margin: '0 0 0 5',
            xtype:'button',
            text:'<i class="fa fa-download"></i>&nbsp;导出Excel',
            //style:'float:right',
            width:95,
            handler:'onExcelClick'
        }*/
        ]
	}
    ],

    initComponent: function() {
        this.callParent();
    }
});
