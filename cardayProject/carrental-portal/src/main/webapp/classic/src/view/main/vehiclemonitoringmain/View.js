Ext.define('Admin.view.main.vehiclemonitoringmain.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'vehicleMonitoringMain',
    id:'vehicleMonitoringMain',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Border',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.main.MainModel'
    ],

    controller: {
        xclass: 'Admin.view.main.vehiclemonitoringmain.ViewController'
    },

    listeners:{
        afterrender: 'afterrenderMap',
        beforerender: 'onBeforeLoad'
    },
	/*viewModel: {
        xclass: 'Admin.view.main.MainModel'
    },*/
    mapdata: null,
    trackLine: null,	//实时数据轨迹线
    pointArray:new Array(),
    autoScroll: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    margin: '0 0 3 0',
    defaults: {
//        flex: 1,
        frame: true,
    },
    bodyPadding: 5,

    items : [
             {
			 	xclass:'Admin.view.main.vehiclemonitoringmain.SearchForm',
			 	height:50,
			 },
             {
//                margin: '0 0 0 0',
                xtype: 'panel',
                flex: 1,
                 layout: {
                    type: 'vbox',
                    pack: 'start',
                    align: 'stretch'
                },
                frame: true,
                items : [
                          {
                                xtype:'panel',
                                id:'VehicleMonitoringStaticInfo',
                                reference: 'mainMapInfo',
                                html:'<div class="mainPageMapBanner"><table id="mainMonitoringTable"><tr><th>车辆总数 0</th><th>在线 0</th><th>行驶 0</th><th>停止 0</th><th class="rightline">离线 0</th><th onclick="backlogOnClick(this)" style="cursor:pointer;">超速报警 0</th><th onclick="backlogOnClick(this)" style="cursor:pointer;">回车报警 0</th><th class="rightline" onclick="backlogOnClick(this)" style="cursor:pointer;">越界报警 0</th><th colspan="2" class="long">地图将在10秒后刷新</th></tr></table></div>',
                                xclass: 'Admin.view.main.vehiclemonitoringmain.MainPageMap',
                          },
                          
                        ],
                },
                {
//                  flex: 5,
//              	 flex:1,
                  xclass: 'Admin.view.main.vehiclemonitoringmain.GridAlarmInfo',
//                  maxHeight:200,
               },
             ],
    initComponent: function() {
        this.callParent();
    }
});
