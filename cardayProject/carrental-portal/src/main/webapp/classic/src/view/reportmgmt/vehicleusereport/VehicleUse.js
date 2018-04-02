Ext.define('Admin.view.reportmgmt.vehicleusereport.VehicleUse', {
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
    tbar: [
    		{
    			xtype:"container",  
    			border:false,
    			width: '100%',
    			items:[{  
    				//tbar第一行
    				xtype:"toolbar",
    				padding: '2 0 2 0',
    				items : [
    					{
    						xtype:'tbtext',
    						text: '里程（千米）',
//	                 margin:'0 0 0 0',
    					},
    					{
    						xtype:'component',
    						flex:1,
    						html:''
    					},
    					{
    						xtype:'tbtext',
    						text:'车辆使用情况',
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
    						xtype:'tbtext',
    						text: '油耗量（升）',
//	                 margin:'0 0 0 0',
    					}
    					
    					]  
    			},{  
    				//tbar第二行
    				xtype:"toolbar",
    				padding: '2 0 2 0',
    				items : [
    					{
    						xtype:'component',
    						flex:1,
    						html:''
    					},
    					{
    						id: 'avgMileageText',
    						xtype:'tbtext',
    						html: '平均值：',
    					},
    					{
    						xtype: 'component',
    						flex: 13,
    						html: ''
    					},
    					{
    						id: 'avgFuelText',
    						xtype:'tbtext',
    						html: '平均值：',
    					},
    					{
    						xtype:'component',
    						flex:1,
    						html:''
    					},
    					]  
    			}]  
    		}
       ],
    items:[{
       xtype: 'cartesian',
       id: 'chart',
       animate: true,
	   bind: {
		   store: '{vehicleUse}'
	   },

	   width: '100%',
       height: 450,
//       insetPadding: 40,
       innerPadding: {
    	   top: 40,
           //right: 40,
           bottom: 40,
           //left: 10
       },
       legend: {
           type: 'sprite',
           docked: 'bottom',
           toggleable : false
       },
        
        //x轴与y轴的声明
	    axes: [{
	    	id: 'avgMileage',
	        type: 'numeric',
	        position: 'left',
	        fields: 'data1',
	        grid: true,
	        //minimum: 0,
	    },
	    {
	    	id: 'avgFuel',
	        type: 'numeric',
	        position: 'right',
	        fields: 'data2',
	        grid: true,
	        //minimum: 0,	        
	    },
	    {
	    	type: 'category',
	        position: 'bottom',
	        //fields: 'name',
	        grid: true,
	        label: {
                rotate: {
                    degrees: -45
                }
            }
	    }],
	    
	   series: [
	     {
	       type: 'line',
	       smooth: 1,
	       title: '里程',
	       xField: 'day',
	       yField: 'data1',	                 
           label: {
        	   display: 'insideEnd',
               //'text-anchor': 'middle',
               //field: 'data1',
               orientation: 'horizontal',
           },
           tooltip: {
               trackMouse: true,
               showDelay: 0,
               dismissDelay: 0,
               hideDelay: 0,
               renderer: function (tooltip, record, item) {
            	   tooltip.setHtml('里程，'+record.get('data1')+'KM');
               },
           },
           marker: {
               type: 'arrow',
               fx: {
                   duration: 200,
                   easing: 'backOut'
               }
           },
	   },
	   {
	       type: 'line',
	       smooth: 1000,
	       title: '油耗量',
	       xField: 'day',
	       yField: 'data2',          
           label: {
        	   display: 'insideEnd',
               //'text-anchor': 'middle',
               //field: 'data2',
               orientation: 'horizontal',
           },
           tooltip: {
               trackMouse: true,
               showDelay: 0,
               dismissDelay: 0,
               hideDelay: 0,
               renderer: function (tooltip, record, item) {
            	   tooltip.setHtml('油耗量，'+record.get('data2')+'L');
               },
           },
           marker: {
               type: 'square',
               fx: {
                   duration: 200,
                   easing: 'backOut'
               }
          }, 
          
	   },
	     ]
    }],
   
});
