Ext.define('Admin.view.vehiclemgmt.geofencemgmt.GeofenceMap', {
    extend: 'Ext.panel.Panel',
    // xtype: 'tracking-map-panel',
    requires: [
        'Admin.ux.MapPanel',
    ],
    reference: 'geofenceMap',
    itemId: 'panelGeofenceMap',
    maxWidth: 1920,
    maxHeight: 1200,
//    title: 'Vehicle Map Display',
    //title: '地图展示',
//    autoScroll: true,
    plain: true,
    resizable: false,
    // autoScroll: true,
    // bodyPadding: 5,
    // constrain: true,
    // closable: true,
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
        // record = me.getViewModel().get('selectedRow'),
        // form = {
        //     autoShow: true,
        //     xclass: 'Web.view.module.buslinemgnt.linemgnt.FormInMapWindow',
        // };
        // if (record === null) {
        //  form.autoShow = false;
        // }
        me.items = [{
            xtype: 'mappanel',
            // xtype: 'panel',
            id: 'geofencemapdis_bmappanel',
            reference: 'bmappanelGeofence',
            itemId: 'bmappanelGeofenceMap',
            maxWidth: 1920,
            maxHeight: 1200,
            width: 820,
            height: 460,
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
