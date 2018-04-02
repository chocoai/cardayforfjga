/**
 * 搜索公司名称
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.rentalcompanymgmt.SearchFormVehsResources', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],

    id:'searchFormVehsResourcesRental',

    reference: 'searchFormVehsResources',

    header : false,
    fieldDefaults : {
        labelWidth : 60,
    },
    layout: {
        type: 'hbox',
        align: 'stretch',
    },
    items : [{
                name : 'vehicleNumber',
                margin : '0 20 20 20',
                xtype : 'textfield',
                width: 200,
                emptyText : '车牌号',
                fieldLabel : '车牌号'
            }, {
                xtype : 'combo',
                margin : '0 20 20 20',
                width: 250,
                valueField : 'value',
                name : 'vehicleType',
                fieldLabel : '车辆类型',
                queryMode : 'local',
                editable : false,
                value:'-1',
                displayField : 'name',
                emptyText : '请选择车辆类型',
                store : {
                    fields : ['name', 'value'],
                    data : [{
                                "name" : "全部",
                                "value" : "-1"
                            }, {
                                "name" : "经济型",
                                "value" : "0"
                            }, {
                                "name" : "舒适型",
                                "value" : "1"
                            }, {
                                "name" : "商务型",
                                "value" : "2"
                            }, {
                                "name" : "豪华型",
                                "value" : "3"
                            }]
                }
            }, {
                xtype : 'combo',
                margin : '0 20 20 20',
                width: 250,
                valueField : 'id',
                name : 'arrangeEnt',
                fieldLabel : '当前分配',
                queryMode : 'remote',
                typeAhead:false,
                editable : false,
                displayField : 'name',
                emptyText : '请选择...',
                store : Ext.create('Ext.data.Store', {
                    proxy : {
                        type : 'ajax',
                        url : 'vehicle/listVehicleFrom',
                         actionMethods: {
			                    create : 'POST',
			                    read   : 'POST', // by default GET
			                    update : 'POST',
			                    destroy: 'POST'
        				},
                        reader : {
                            type : 'json',
                            rootProperty : 'data',
                            successProperty : 'status'
                        }
                    },
                    autoLoad : false,
                    listeners : {
                        load : function(store, records, options) {
                        	var organizationId = Ext.getCmp("showResourcesInfoRental").companyId;
			                for(var i in records){
			               		if(records[i].getData().id==organizationId){
			               			store.remove(records[i]);
			               		}
			                };
                            store.insert(0, {
                                        "name" : "全部",
                                        "id" : "-1"
                            });
                        }
                    }
                })
            },{
                xtype:'button',
                margin : '0 20 20 20',
                text:'<i class="fa fa-search"></i>&nbsp;查询',
                handler:'onSearchClickforVehsResources',
              }
            ],
    initComponent : function() {
        this.callParent();
    }
});
