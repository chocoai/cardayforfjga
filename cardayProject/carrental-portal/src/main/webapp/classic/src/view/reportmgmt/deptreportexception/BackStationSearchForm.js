/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.deptreportexception.BackStationSearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    reference: 'backStationSearchForm',
    id: 'backStationSearchFormId',
    bodyPadding: 1,
    header: false,
    fieldDefaults: {
        labelWidth: 65,
        margin: '0 20 0 0', //label和上一个输入框的样式，间隔5
        msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
    },
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
            }, {
                xtype:'checkboxgroup',
                hideLabel:true,
                columns: 2,
                width:180,
                flex:null,
                vertical: true,
                items: [
                    { boxLabel: '本部门', name: 'includeSelf', inputValue: true,checked: true, 
                        width:70,
                        listeners:{
                            change:'checkIsGroupNull',
                        },
                        style:{
                            marginRight:'10px',
                            marginLeft:'10px',
                        }},
                    { boxLabel: '子部门', name: 'includeChild', inputValue: true,checked: true,
                        listeners:{
                            change:'checkIsGroupNull',
                        },
                        style:{
                            marginRight:'10px',
                        }},
                ],
                allowBlank : false,
                blankText : '不能为空'
            },
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
                //maxValue: new Date()
            },
            {
                name:'endDate',
                fieldLabel: '结束日期',
                emptyText: '请选择结束日期',
                xtype: 'datefield',
                format: 'Y-m-d',
                vtype: 'daterange',
                itemId: 'enddt',
                startDateField: 'startdt',
                editable: false,
                value: new Date(),
                maxValue: new Date(),
            },
            {
                margin: '0 0 0 20',
                xtype:'button',
                text:'<i class="fa fa-search"></i>&nbsp;查询',
                width:100,
                //style:'float:right',
                handler:'onBeforeLoadBackStation'
            },
        ]
	},
	{
    	xtype:'container',
    	//flex:1,
    	items:[
        {
            xtype : 'button',
            text:'<i class="fa fa-download"></i>&nbsp;导出Excel',
            width:100,
            style:'float:right',
            handler:'onExcelBackStationClick'
        }, ]
	}
    ],

    initComponent: function() {
        this.callParent();
    }
});
