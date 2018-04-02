Ext.define('Admin.view.reportmgmt.reportexception.AverageOverborderMileage', {
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
    //controller: 'viewcontroller',
    tbar: [
           {
        	   xtype:'tbtext',
               text: '单位：千米/天',
           },
           {
        	   xtype:'component',
        	   flex:1,
        	   html:''
           },
           {
        	   xtype:'tbtext',
               text:'平均越界里程',
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
              id: 'averageOverborderMileageId',
              xtype:'tbtext',
              html: '平均值：',
            },
       ],
    
    items:[
      {
       xtype: 'cartesian',
	   id: 'averageOverborderMileageText',
       animate: true,
	   bind: {
		   store: '{averageOverborderMileage}'
	   },

	   width: '100%',
       height: 450,
       insetPadding: {
           top: 40,
           bottom: 40,
           left: 20,
           right: 40
       },
       innerPadding: {
           top: 20,
       },
		
       //x轴与y轴的声明
	    axes: [
	    {
	        type: 'numeric',
	        position: 'left',
	        //fields: 'data',
	        title: false,
	        grid: true,
	        minimum: 0,
	    }, 
	    {
	    	type: 'category',
	        position: 'bottom',
	        //fields: 'name',
	        //title: false,
	    }],
	    
/*		sprites: [	        
		     {
	            type: 'text',
	            text: '平均值：60',
	            width: 100,
	            height: 10,
	            x: 670, // the sprite x position
	            y: 30  // the sprite y position
	        },
			 {
	            type: 'text',
	            text: '平均越界里程(千米)',
	            fontSize: 22,
	            fontWeight: 'bold',
	            width: 100,
	            height: 10,
	            x: 360, // the sprite x position
	            y: 30  // the sprite y position
	        },
	        {
	            type: 'text',
	            text: '单位：千米/天',
	            width: 100,
	            height: 10,
	            x: 50, // the sprite x position
	            y: 30  // the sprite y position
	        },
        ],*/
	   series: [{
	       type: 'bar',
	       colors: ['#389BD2'],//柱状颜色
	       style: {
               minGapWidth: 75
           },
	       axis: 'left',
	       xField: 'name',
	       yField: 'data',
            highlight: {
                fillStyle: 'red'
            },   
              tooltip: {
               trackMouse: true,
/*               width: 160,
               height: 28,*/
               renderer: function(tooltip, record, item) {
            	   tooltip.setHtml(record.get('name') + " , " + record.get('data'));
               }
             },
           label: {
        	   display: 'insideEnd',
               //'text-anchor': 'middle',
               field: 'data',
               orientation: 'horizontal',
           }
	   }]
    }],
   
});
