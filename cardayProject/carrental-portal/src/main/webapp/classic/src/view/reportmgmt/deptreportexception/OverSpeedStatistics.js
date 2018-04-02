Ext.define('Admin.view.reportmgmt.deptreportexception.OverSpeedStatistics', {
	extend: 'Ext.chart.Chart',
    alias: "widget.columnchart",
    requires: [
			'Ext.chart.CartesianChart',
			'Ext.chart.axis.Numeric',
			'Ext.chart.axis.Time',
			'Ext.draw.modifier.Highlight',
			'Ext.chart.interactions.ItemHighlight',
    ],
    
    controller: 'deptreportexceptioncontroller',

    tbar: [
           {
        	   xtype:'tbtext',
               text: '条',
//               margin:'0 0 0 0',
           },
           {
        	   xtype:'component',
        	   flex:1,
        	   html:''
           },
           {
        	   xtype:'tbtext',
               text:'超速报警走势图',
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
        	   html:''
           },
       ],
    
    items:[{
       xtype: 'cartesian',
       animate: true,
	   bind: {
		   store: '{overSpeedStatistics}'
	   },

		width: '100%',
		height: 450,
		//  insetPadding: 50,
		innerPadding: 20,
		//theme: 'Base:gradients',
		//提示信息显示
		legend : {
			docked : 'bottom',
			itemSpacing:10,
			padding:5,
			toggleable : false,
		},
/*		sprites: [
			 {
	            type: 'text',
	            text: '超速报警走势图',
	            fontSize: 22,
	            fontWeight: 'bold',
	            width: 100,
	            height: 10,
	            x: 700, // the sprite x position
	            y: 20  // the sprite y position
	        },
	        {
	            type: 'text',
	            text: '条',
	            width: 100,
	            height: 10,
	            x: 50, // the sprite x position
	            y: 20  // the sprite y position
	        },
        ],*/
        
        //x轴与y轴的声明
	    axes: [{
	        type: 'numeric',
	        position: 'left',
	        fields: 'countValue',
	        grid: true,
	        minimum: 0,
            title: '报警条数',  
            position: 'left' 
	    },
	    {
	    	type: 'category',
	        position: 'bottom',
	        fields: 'countDate',
	        grid: true,
	    }],
	    
	   series: [
	     {
	       type: 'line',
	       xField: 'countDate',
	       yField: 'countValue',
	       title: '超速报警条数',
           tooltip: {
               trackMouse: true,
               showDelay: 0,
               dismissDelay: 0,
               hideDelay: 0,
               renderer: function (tooltip, record, item) {
            	   tooltip.setHtml(record.get('countValue')+'条');
               },
           },
           marker: {
                type: 'circle',
                size: 4,
                radius: 4,
                'stroke-width': 0
           },
	   },
	     ]
    }],
   
});
