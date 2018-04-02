Ext.define('Admin.view.main.vehiclemonitoringmain.MainPageMap', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Admin.ux.MapPanel',
    ],
    reference: 'mainPageMap',
    itemId: 'panelMainPageMap',
//    maxWidth: 1920,
//    maxHeight: 1200,
    plain: true,
    resizable: false,
    listeners: {
        restore: {
            fn: function(win) {
                Ext.each(me.dashWindows, function(w) {
                    w.doConstrain();
                });
            }
        }
    },
    initComponent: function() {
        console.log(this);
        var me = this;
		console.log("screen.width: " + screen.width);
		console.log("window.width: " + window.width);
		console.log("window.screen.availWidth: " + window.screen.availWidth);
		console.log("window.screen.width: " + window.screen.width);
		console.log("$(window).width().width: " + $(window).width());
        me.items = [{
            xtype: 'mappanel',
            // xtype: 'panel',
//            id: 'bmappanelMainPage',
            reference: 'bmappanelMainPage',
//            itemId: 'bmappanelMainPageMap',
             //trackLine: null,	//实时数据轨迹线
//            maxWidth: 1920,
//            maxHeight: 1200,
            width: ($(window).width() <= 1600)?1320:1670, 
//            width: 1320, 
//            height: 665,
            height: 797,
//            width: 1100,
//            height: 860,
            plain: true,
            border: false,
            zoomLevel: 12,
            gmapType: 'map',
            overlays: [],
            markers: [],
            ContextMenus: ['在此添加标注', '放大', '缩小', '放置到最大级', '查看全国'],
            mapControls: ['NavigationControl', 'ScaleControl', 'OverviewMapControl', 'MapTypeControl'],
            listeners: {
//                afterrender: 'onAfterRenderPanelTrackingMap',
                // active: 'onActivePanelTrackingMap',
                // afterlayout: 'onAfterlayoutPanelTrackingMap'
            },
        }];
        me.callParent();

    }
});
