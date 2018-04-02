Ext.define('Admin.view.rulemgmt.locationmgmt.LocationMap', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Admin.ux.MapPanel',
    ],
    reference: 'locationMap',
    itemId: 'panelLocationMap',
    maxWidth: 1920,
    maxHeight: 1200,
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
        me.items = [{
            xtype: 'mappanel',
            id: 'locationmapdis_bmappanel',
            reference: 'bmappanelLocation',
            itemId: 'bmappanelLocationeMap',
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
            },
        }];
        me.callParent();

    }
});
