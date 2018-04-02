Ext.define('Admin.view.main.MainPageContainer', {
    extend: 'Ext.container.Container',
    xtype: 'mainpagecontainer',
    id: 'mainpagecontainer',
//登录后首次显示的treelist，车辆tree
    requires : [
        'Ext.layout.container.HBox',
        'Admin.view.main.OverridesRootTreeItem'
    ],

    scrollable: 'y',

    layout: {
        type: 'hbox',
        align: 'stretchmax',

        // Tell the layout to animate the x/width of the child items.
        animate: true,
        animatePolicy: {
            x: true,
            width: true
        }
    },

                items: [
                {
                    xtype: 'treelist',
                    requires:['Admin.view.main.OverridesRootTreeItem'],
                    reference: 'navigationMainPageTree',
                    itemId: 'navigationMainPageTree',       //车辆树
                    id: 'navigationMainPageTree',
                    ui: 'navigation',
                    rootVisible: false,
                    store: 'NavigationMainTree',
                    width: 250,
                    maxHeight:888,
                    scrollable:'y',
                    style:{
                    	height:'888px',
                		overflow:'auto',
                    },
                	expanderFirst: false,
					expanderOnly: false,
					selectItemId:null,
                    listeners: {
                    selectionchange: 'onNavigationTreeSelectionChange',//点击菜单，进入相应的页面
                    itemclick: 'onTreeItemClick'
                    }
                },
              {
                    xtype: 'container',
                    flex: 1,
                    reference: 'mainPageCardPanel',
                    cls: 'sencha-dash-right-main-container',
                    itemId: 'contentPagePanel',
                    layout: {
                        type: 'card',
                        anchor: '100%'
                    }
                }
            ],

    beforeLayout : function() {
        // We setup some minHeights dynamically to ensure we stretch to fill the height
        // of the viewport minus the top toolbar

        var me = this,
            height = Ext.Element.getViewportHeight() - 140,  // offset by topmost toolbar height
            // We use itemId/getComponent instead of "reference" because the initial
            // layout occurs too early for the reference to be resolved
            navTree = me.getComponent('navigationMainPageTree');

        me.minHeight = height;

        navTree.setStyle({
            'min-height': height + 'px'
        });

        me.callParent(arguments);
    }
});
