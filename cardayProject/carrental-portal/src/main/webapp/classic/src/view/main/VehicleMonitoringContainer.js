Ext.define('Admin.view.main.VehicleMonitoringContainer', {
    extend: 'Ext.container.Container',
    xtype: 'vehiclemonitoringcontainer',
    id: 'vehiclemonitoringcontainer',
//登录后首次显示的treelist，车辆tree
    requires : [
        'Ext.layout.container.HBox',
        'Admin.view.main.OverridesRootTreeItem',
        'Admin.store.NavigationVehicleMonitoringTree'
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
                		xtype: 'form',
                		id:'treeLeftBar',
                        defaults:{
//                	        margin: '5 0 5 0'
                        },
                        maxHeight:888,
                        items: [{
                        	xtype: 'container',
                         	layout: 'hbox',
                    		itemId:'leftBar',
                            defaults:{
                    	        margin: '10 0 5 0'
                            },
                            width: 250,
                            items:[{
                            	name: 'vehicleNumber',
                                xtype: 'textfield',
                                emptyText: '车牌号',
                                width: 180,
                                height:40,
                                margin: '10 0 5 10',
                                id:"searchVehicleText",
                                enableKeyEvents: true,
                                //mixins:['Admin.store.TreeFilter'], 
                                listeners: {
                	                change:"searchByTextChange",
                	                specialkey : "specialkey"
                                }
                            },{
                            	 xtype: "button",
                                 text: "<i class='fa fa-search' style='color:#666;font-size:14px'></i>",
                                 handler: "onSearchClick",
                                 margin:'10 0 5 0',
                                 width: 50,
                                 height:40,
                                 id:"searchVehicleBtn",
                                 listeners: {
                                     afterrender: function() {
                                         /*var userType = window.sessionStorage.getItem('userType');
                                         if (userType == '3') {
                                             this.hidden = true;
                                         }*/
                                     }
                                 }
                            }]
                        },{
                        	xtype: 'treelist',
                            name:'navigationVehicleMonitoringTree',
                            requires:['Admin.view.main.OverridesRootTreeItem'],
                            reference: 'navigationVehicleMonitoringTree',
                            itemId: 'navigationVehicleMonitoringTree',       //车辆树
                            id: 'navigationVehicleMonitoringTree',
                            ui: 'navigation',
                            rootVisible: false,
                            //store: 'NavigationVehicleMonitoringTree',
                            store: new Admin.store.NavigationVehicleMonitoringTree(),
                            //width: 250,
                            //左侧最高888px，上面的搜索占了55px，所以这里减掉
                            scrollable:true,
                            style:{
                            	height:'833px',
                            	width: '250px',
                        		overflow:'auto',
                            },
                        	expanderFirst: false,
            				expanderOnly: false,
            				selectItemId:null,
                            listeners: {
            	                selectionchange: 'onNavigationTreeSelectionChange',//点击菜单，进入相应的页面
            	                itemclick: 'onTreeItemClick'
            	                
                            }
                            
                        }],
                        beforeLayout : function() {
//                            var me = this;
//                            height = Ext.Element.getViewportHeight();  // offset by topmost toolbar height
//                            navTree = me.getComponent('navigationVehicleMonitoringTree');
//                            me.minHeight = height;
//                            navTree.setStyle({
//                                'min-height': height + 'px'
//                            });
                        },
                        afterLayout : function(){
                        	
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

        var me = this;
            /*height = Ext.Element.getViewportHeight() - 140,  // offset by topmost toolbar height
            // We use itemId/getComponent instead of "reference" because the initial
            // layout occurs too early for the reference to be resolved
            navTree = me.getComponent('navigationVehicleMonitoringTree');

        me.minHeight = height;

        navTree.setStyle({
            'min-height': height + 'px'
        });*/

        me.callParent(arguments);
    }
});
