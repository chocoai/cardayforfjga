Ext.define('Admin.view.main.vehiclemonitoringmain.GridAlarmInfo', {
    extend: 'Ext.panel.Panel',
    requires: [
    ],
    
    id: 'gridAlarmInfo',
    reference: 'gridAlarmInfo',
    header:{
        title : '报警信息',
        height:24,
        style:{
            lineHeight:24,
            padding: '0px 10px 4px 10px',
        }
    },

     controller: {
        xclass: 'Admin.view.main.vehiclemonitoringmain.ViewController'
    },
	viewModel: {
        xclass: 'Admin.view.main.MainModel'
    },    
    cls:'my-short-header-window',
    listeners:{
    	afterrender: 'onSearchClick',
//        itemclick: 'itemGridAlarmInfoClick',
        collapse: function(panel, eOpts){
        	Ext.getCmp("VehicleMonitoringStaticInfo").setHeight(797);
        	console.log("collapse地图高度：" + 797);
        },
        expand: function(panel, eOpts){
        	var height = panel.getHeight();
        	var mapHeight = 797 - (height - 42);
        	Ext.getCmp("VehicleMonitoringStaticInfo").setHeight(mapHeight);
        	console.log("expand地图高度：" + mapHeight);
        }

    },

    maxHeight:127,

    scrollable: 'y',
    
    collapsible: true,
    collapseToolText:'',
    expandToolText:'',
    //默认为收缩状态
    collapsed :true,

    items : [{
        xtype:'panel',
        layout: {
            type: 'hbox',
            pack: 'start',
            align: 'stretch'
        },
         items : [{
                xclass: 'Admin.view.main.vehiclemonitoringmain.GridAlarmInfoPre',
            },
            {
                xclass: 'Admin.view.main.vehiclemonitoringmain.GridAlarmInfoNext',
            }],
        }],

    
    initComponent: function() {
        this.callParent();

    },
});
