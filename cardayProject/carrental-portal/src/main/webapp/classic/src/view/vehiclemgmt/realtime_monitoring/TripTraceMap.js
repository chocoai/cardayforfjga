Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.TripTraceMap', {
    extend: 'Ext.panel.Panel',
    // xtype: 'tracking-map-panel',
    requires: [
        'Admin.ux.MapPanel',
        'Admin.view.vehiclemgmt.realtime_monitoring.history_data.HistoryViewController'
    ],
//    id: 'vehiclemapdis_vehicleTrackingMap',
    controller: 'historyViewController',
    reference: 'tripTraceMap',
    itemId: 'panelInventoryTrackingMap',
    maxWidth: 1040,
    maxHeight: 525,
//    title: 'Vehicle Map Display',
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
//            id: 'vehiclemapdis_bmappanel',
            reference: 'tripTraceMapPanel',
            id: 'tripTraceMapPanel',
            itemId: 'bmappanelInventoryTrackingMap',
            //minWidth: 1000,
            //minHeight: 525,
            width: 1040,
            height:465,
            plain: true,
            border: false,
            zoomLevel: 12,
            gmapType: 'map',
            overlays: [],
            markers: [],
            ContextMenus: ['在此添加标注', '放大', '缩小', '放置到最大级', '查看全国'],
            mapControls: ['NavigationControl', 'ScaleControl', 'OverviewMapControl', 'MapTypeControl'],
            listeners: {
//                afterrender: 'loadTripTraceMapView',
                afterrender: 'initTripTraceMap',
            },
        }];
        me.callParent();

    }
});
