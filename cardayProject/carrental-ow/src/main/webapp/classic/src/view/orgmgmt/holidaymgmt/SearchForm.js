/**
 * 搜索公司名称
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.holidaymgmt.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],

    id:'holidaysearchForm',

    reference: 'searchForm',

    header: false,
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
   margin: '0 0 20 0', //对panel生效
    items: [{
        xtype: 'form',
        layout: 'hbox',
        flex:1,
        items: [{
            xtype : 'combo',
            queryMode : 'local',
            editable : false,
            store : new Ext.data.ArrayStore({
                fields : ['id','name'],
                data : []
            }),
            name: 'holidayYear',
            valueField : 'name',
            displayField : 'id',
            triggerAction : 'all',
            autoSelect : true,
            value: Ext.Date.format(new Date(),'Y'),
            listeners : {
                beforerender :  function(){
                    var newyear = Ext.Date.format(new Date(),'Y');//这是为了取现在的年份数
                    var yearlist = [];
                     for(var i = Number(newyear) + 1 ; i >= Number(newyear) - 50; i--){
                      yearlist.push([i,i]);
                    }
                    this.store.loadData(yearlist);
                }
            }
        },{
        	xtype:'button',
        	text: '<i class="fa fa-search"></i>&nbsp;查询',
        	handler: 'onSearchClick'
        }]
    },{
    	xtype:'container',
		style: {
			float:'right'
		},
    	items:[{
    		xtype:'button',
    		text:'新增',
    		handler:'onAddClick',
    	}]
    	
    }],
    initComponent: function() {
        this.callParent();
    }
});
