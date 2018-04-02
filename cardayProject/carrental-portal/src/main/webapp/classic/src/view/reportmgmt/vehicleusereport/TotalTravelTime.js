Ext.define('Admin.view.reportmgmt.vehicleusereport.TotalTravelTime', {
	extend: 'Ext.chart.Chart',
    alias: "widget.columnchart",
    //alias: "widget.chart",
    requires: [
			'Ext.chart.CartesianChart',
			'Ext.chart.axis.Numeric',
			'Ext.chart.axis.Time',
			'Ext.draw.modifier.Highlight',
			'Ext.chart.interactions.ItemHighlight',
    ],
    
    controller: 'viewcontroller',
    id: 'vehicleusereport_totaltraveltime_id',
    tbar: [
           {
        	   xtype:'tbtext',
               text: '单位：小时',
           },
           {
        	   xtype:'component',
        	   flex:1,
        	   html:''
           },
           {
        	   xtype:'tbtext',
               text:'总行驶时长',
               textAlign:'cener',
               height:35,
               style:{
            	   fontSize: '22px',
   	           	   fontWeight: 'bold',
   	           	   lineHeight:'35px',
               }
           },
           {
        	   xtype:'component',
        	   flex:1,
        	   html:'',
           },
           {
        	   xtype:'component',
        	   html:'',
        	   width:73,
           }
       ],
    items:[{
       xtype: 'polar',
       animate: true,
	   bind: {
		   store: '{totalTravelTime}'
	   },

	   width: '100%',
       height: 450,
//       insetPadding: 50,
       innerPadding: 20,
	   //theme: 'Base:gradients',
	   //提示信息显示
		legend : {
			docked : 'bottom',
			itemSpacing:10,
			padding:5,
			toggleable : false,
		},
		/*sprites: [
			 {
	            type: 'text',
	            text: '总行驶时长',
	            fontSize: 22,
	            fontWeight: 'bold',
	            width: 100,
	            height: 10,
	            x: 360, // the sprite x position
	            y: 30  // the sprite y position
	        },
	        {
	            type: 'text',
	            text: '单位：小时',
	            width: 100,
	            height: 10,
	            x: 50, // the sprite x position
	            y: 30  // the sprite y position
	        },
        ],*/
	   series: [{
	       type: 'pie',
	       highlight: false,
	       angleField: 'data',
	       donut: 0,
	       showInLegend : true,
	       animate : true,
	       highlight: true,
	       //自定义饼图的颜色
	       //colors:["#B0E2FF", "#DDA0DD"],
   
	       tooltip: {
               trackMouse: true,
               //点击饼图，显示used,unused具体数据
               renderer: function(tooltip, record, item) {
            	   tooltip.setHtml(record.get('name') + ",  " + record.get('data')+',  ' + record.get('precent'));
               }
             },
           
           label: {
        	   field: 'name',
//               display: 'rotate',
               display: 'none',
               contrast: true,
               font: '18px Arial',
           }
	   }]
    }],
   
});
