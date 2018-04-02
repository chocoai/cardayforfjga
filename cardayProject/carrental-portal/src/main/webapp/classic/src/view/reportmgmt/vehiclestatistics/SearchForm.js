/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.vehiclestatistics.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    reference: 'searchForm',
    id: 'vehiclestatistics_SearchForm_id',
    bodyPadding: 1,
    header: false,
    fieldDefaults: {
        // labelAlign: 'right',
        labelWidth: 65,
        msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
    },
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    
    items: [
	{
		xtype: 'form',
        layout: 'hbox',
        flex:1.4,
        items: [
            {
                name: 'deptName',
                xtype: 'textfield',
                labelWidth: 60,
                width:200,
                emptyText: '单位名称',
                fieldLabel: '单位名称'
            },
            {
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
                margin:'0 0 0 10',
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
            },{
            margin: '0 0 0 10',
            xtype:'button',
            id:'vehiclestatisticsSearch',
            text:'<i class="fa fa-search"></i>&nbsp;查询',
            width:100,
            handler:'onSearchClick'
        }
        ]
	},
	{
    	xtype:'container',
    	items:[{
            xtype:'button',
            text:'<i class="fa fa-download"></i>&nbsp;导出Excel',
            width:100,
            //id: 'export2excel',
            style:'float:right; margin-right:10px',
            handler:'onExcelClick'
        }]
	}
    ],

    initComponent: function() {
        this.callParent();
    }
});
