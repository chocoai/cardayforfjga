Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.VehicleTrackingMap', {
    extend: 'Ext.panel.Panel',
    // xtype: 'tracking-map-panel',
    requires: [
        'Admin.ux.MapPanel',
    ],
//    id: 'vehiclemapdis_vehicleTrackingMap',
    reference: 'vehicleTrackingMap',
    itemId: 'panelInventoryTrackingMap',
/*    maxWidth: 1425,
    minWidth: 1100,*/
//    title: 'Vehicle Map Display',
    title: '地图展示',
//    autoScroll: true,
    plain: true,
    resizable: false,
    // autoScroll: true,
    // bodyPadding: 5,
    // constrain: true,
    // closable: true,
    viewConfig: {
        loadMask: true,
       	loadingText: '加载中...',
       	emptyText: '无记录！',
        deferEmptyText: false
    },
    bodyStyle : 'overflow-x:hidden; overflow-y:hidden',
//    listeners: {
//        restore: {
//            fn: function(win) {
//                Ext.each(me.dashWindows, function(w) {
//                    w.doConstrain();
//                });
//            }
//        }
//    },
    initComponent: function() {
//        console.log(this);
        var me = this;
        // record = me.getViewModel().get('selectedRow'),
        // form = {
        //     autoShow: true,
        //     xclass: 'Web.view.module.buslinemgnt.linemgnt.FormInMapWindow',
        // };
        // if (record === null) {
        //  form.autoShow = false;
        // }

        //me.layout = 'fit';
        me.items = [{
            xtype: 'mappanel',
            // xtype: 'panel',
            id: 'vehiclemapdis_bmappanel',
            reference: 'bmappanel',
            itemId: 'bmappanelInventoryTrackingMap',
/*            maxWidth: 1425,
            width:1200,
            minWidth: 1100,
            height: 690,*/            
            width: (screen.width <= 1600)?1100:1440, 
            height: 736,
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
