Ext.define('Admin.view.ordermgmt.orderallocate.AllocatedTraceMap', {
    extend: 'Ext.panel.Panel',
    // xtype: 'tracking-map-panel',
    requires: [
        'Admin.ux.MapPanel',
    ],
//    id: 'vehiclemapdis_vehicleTrackingMap',
    reference: 'allocatedTraceMap',
    itemId: 'panelInventoryTrackingMap',
    maxWidth: 830,
    maxHeight: 350,
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
            id: 'allocatedTraceMapPanel',
            reference: 'allocatedTraceMapPanel',
            itemId: 'bmappanelInventoryTrackingMap',
            minWidth: 400,
            minHeight: 350,
            width: 900,
            height:350,
            plain: true,
            border: false,
            zoomLevel: 12,
            gmapType: 'map',
            overlays: [],
            markers: [],
            ContextMenus: ['在此添加标注', '放大', '缩小', '放置到最大级', '查看全国'],
            mapControls: ['NavigationControl', 'ScaleControl', 'OverviewMapControl', 'MapTypeControl'],
            listeners: {
                afterrender: 'loadTripTraceMapView',
//                afterrender: 'loadTripTraceMoveMapView',
                // active: 'onActivePanelTrackingMap',
                // afterlayout: 'onAfterlayoutPanelTrackingMap'
            },
        }];
        me.callParent();

    }
});
