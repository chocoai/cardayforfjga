Ext.define('Admin.view.reportmgmt.vehicleusereport.AverageMileage', {
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
    id: 'vehicleusereport_averagemileage_id',
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
               text:'平均里程',
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
            {
              id: 'averageMileageId',
              xtype:'tbtext',
              html: '平均值：',
            },
       ],
    items:[
      {
       xtype: 'cartesian',
       animate: true,
	   bind: {
		   store: '{averageMileage}'
	   },

	   width: '100%',
       height: 450,
//       insetPadding: {
//           top: 40,
//           bottom: 40,
//           left: 20,
//           right: 40
//       },
       //innerPadding: 20,
		
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
	     //position: 'bottom',
	    }],
	    
		/*sprites: [
			 {
	            type: 'text',
	            text: '平均里程',
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
//	        {
//	            type: 'text',
//	            text: '平均值：'+window.avgMileageVal,
//	            width: 100,
//	            height: 10,
//	            x: 600, // the sprite x position
//	            y: 30  // the sprite y position
//	        },
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
   
	       tooltip: {
              trackMouse: true,
              renderer: function(tooltip, record, item) {
           	   tooltip.setHtml(record.get('name') + ": " + record.get('data'));
              }
            },
           
           label: {
        	   display: 'insideEnd',
               field: 'data',
               orientation: 'horizontal',
           }
	   }]
    }],
   
});
