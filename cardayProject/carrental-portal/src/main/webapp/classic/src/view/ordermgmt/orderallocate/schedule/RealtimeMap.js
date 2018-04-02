Ext.define('Admin.view.ordermgmt.orderallocate.schedule.RealtimeMap', {
    extend: 'Ext.panel.Panel',
    // xtype: 'tracking-map-panel',
    requires: [
        'Admin.ux.MapPanel',
    ],
//    id: 'vehiclemapdis_vehicleTrackingMap',
    /*controller: {
        xclass: 'Admin.view.ordermgmt.orderallocate.schedule.ScheduleController'
    },*/
    reference: 'realtimeMap',
    itemId: 'panelInventoryTrackingMap',
    maxWidth: 670,
    maxHeight: 512,
//    maxHeight: 680,
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
        },
        afterrender: 'loadTimerElement',
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
            reference: 'realtimeMapPanel',
            itemId: 'bmappanelInventoryTrackingMap',
            minWidth: 670,
            minHeight: 512,
//            minHeight: 680,
            width: 670,
            height:512,
//            height:680,
            plain: true,
            border: false,
            zoomLevel: 12,
            gmapType: 'map',
            overlays: [],
            markers: [],
            ContextMenus: ['在此添加标注', '放大', '缩小', '放置到最大级', '查看全国'],
            mapControls: ['NavigationControl', 'ScaleControl', 'OverviewMapControl', 'MapTypeControl'],
            listeners: {
                afterrender: 'onAfterRenderRealtimeMap',
            },
        }];
        me.callParent();

    }
});
