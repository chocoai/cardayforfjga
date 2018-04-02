package com.cmdt.carrental.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmdt.carrental.common.model.Circle;
import com.cmdt.carrental.common.model.Point;
import com.cmdt.carrental.common.model.Triangle;

public class MiniCircleAlgorithm {
	
	private static final Logger LOG = LoggerFactory.getLogger(MiniCircleAlgorithm.class);
	
	private static final double EPS = 1e-8; 		//数值偏差
	private static final double PLU = 111319.55; 	//坐标距离转换系数
	
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private static double Len(Point a,Point b){ 
	    return Math.sqrt(Math.pow((a.getLat()-b.getLat()), 2) + Math.pow((a.getLon()-b.getLon()), 2));
	}
	
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	private static double TriangleArea(Triangle a){  
	    double px1=a.getV()[1].getLat()-a.getV()[0].getLat();  
	    double py1=a.getV()[1].getLon()-a.getV()[0].getLon();  
	    double px2=a.getV()[2].getLat()-a.getV()[0].getLat();  
	    double py2=a.getV()[2].getLon()-a.getV()[0].getLon();  
	    return Math.abs(px1*py2-px2*py1)/2;  
	}  

	/**
	 * 
	 * @param t
	 * @return
	 */
	private static Circle CircleOfTriangle(Triangle t){  
	    Circle tmp = new Circle();  
	    double a=Len(t.getV()[0],t.getV()[1]);  
	    double b=Len(t.getV()[0],t.getV()[2]);  
	    double c=Len(t.getV()[1],t.getV()[2]);  
	    tmp.setRadius(a*b*c/4/TriangleArea(t));  
	    double a1=t.getV()[1].getLat()-t.getV()[0].getLat();  
	    double b1=t.getV()[1].getLon()-t.getV()[0].getLon();  
	    double c1=(a1*a1+b1*b1)/2;  
	    double a2=t.getV()[2].getLat()-t.getV()[0].getLat();  
	    double b2=t.getV()[2].getLon()-t.getV()[0].getLon();  
	    double c2=(a2*a2+b2*b2)/2;  
	    double d=a1*b2-a2*b1;  
	    tmp.getCenter().setLat(t.getV()[0].getLat()+(c1*b2-c2*b1)/d);  
	    tmp.getCenter().setLon(t.getV()[0].getLon()+(a1*c2-a2*c1)/d);  
	    return tmp;  
	} 
	
	
	/**
	 * 
	 * @param p
	 */
	public static Circle calculate(Point[] p){  
		int n = p.length;  
	    Circle tep = new Circle();  
	    tep.setCenter(new Point(p[0].getLat(),p[0].getLon()));  
	    tep.setRadius(0);  
	    for(int i=1;i<n;i++)  
	    {  
	        if(Len(p[i],tep.getCenter())>tep.getRadius()+EPS)  
	        {  
	            tep.setCenter(new Point(p[i].getLat(),p[i].getLon()));  
	            tep.setRadius(0);  
	            for(int j=0;j<i;j++)  
	            {  
	                if(Len(p[j],tep.getCenter())>tep.getRadius()+EPS)  
	                {  
	                    tep.getCenter().setLat((p[i].getLat()+p[j].getLat())/2);  
	                    tep.getCenter().setLon((p[i].getLon()+p[j].getLon())/2);  
	                    tep.setRadius(Len(p[i],p[j])/2);
	                    for(int k=0;k<j;k++)  
	                    {  
	                        if(Len(p[k],tep.getCenter())>tep.getRadius()+EPS)  
	                        {  
	                            Triangle t = new Triangle();  
	                            t.getV()[0]=p[i];
	                            t.getV()[1]=p[j];  
	                            t.getV()[2]=p[k];  
	                            tep=CircleOfTriangle(t);  
	                        }  
	                    }  
	                }  
	            }  
	        }  
	    }
	    
	    //trans GPS gap to mileage
	    tep.setRadius(tep.getRadius() * PLU);
	    
	    return tep;
	    
	}  
	
	public static double disLen(Point a,Point b){
		return Len(a,b) * PLU;
	}
	
	public static void main(String[] arg)  
	{  
	    //points list
//		Point p0 = new Point(116.280949,39.953441);
//		Point p1 = new Point(116.280662,39.904087);
//		Point p2 = new Point(116.364456,39.904862);
//		Point p3 = new Point(116.39277,39.974345);
//		Point[] p = new Point[4];
//		p[0] = p0;
//		p[1] = p1;
//		p[2] = p2;
//		p[3] = p3;
//		Circle c = calculate(p);
//		LOG.info("resp: center - lng-"+ c.getCenter().getLat() +", lat-"+ c.getCenter().getLon() + ", radius-" + c.getRadius());
		
		Point p0 = new Point(116.423352,39.907664);
		Point p1 = new Point(116.420362,39.930936);
		double length = Len(p0,p1);
		LOG.info("resp: length - "+ length * PLU);
	} 
}
