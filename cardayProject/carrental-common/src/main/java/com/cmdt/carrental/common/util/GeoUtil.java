package com.cmdt.carrental.common.util;

public class GeoUtil {
	
	private static double EARTH_RADIUS = 6378.137; 
	
	//POLYGON((114.34491621572 30.530405160995,114.35430357266 30.532314128513,114.35394162485 30.524667363823,114.34598610157 30.523998829544,114.34341262764 30.52655105792,114.34491621572 30.530405160995))
	//[[{"lng":114.356286,"lat":30.534166},{"lng":114.361532,"lat":30.536592},{"lng":114.370587,"lat":30.529624},{"lng":114.360454,"lat":30.528504},{"lng":114.355998,"lat":30.529997},{"lng":114.356286,"lat":30.534166}]]!
	public static boolean pointInPolygon(int polyCorners,float[] polyX,float[]  polyY,float  x,float  y) {

		  int i, j=polyCorners-1 ;
		  boolean  oddNodes = false;

		  for (i=0; i<polyCorners; i++) {
		    if ((polyY[i]< y && polyY[j]>=y
		    ||   polyY[j]< y && polyY[i]>=y)
		    &&  (polyX[i]<=x || polyX[j]<=x)) {
		      if (polyX[i]+(y-polyY[i])/(polyY[j]-polyY[i])*(polyX[j]-polyX[i])<x) {
		        oddNodes=!oddNodes; }}
		    j=i; }
		  
		  return oddNodes; }
	
	
	/**
	 * should remove
	 * @param args
	 */
	public static void main(String args[]){
		
		int    polyCorners  =  6;
		float  polyX[]      =  {30.530405160995f,30.532314128513f,30.524667363823f,30.523998829544f,30.52655105792f,30.530405160995f};
		float  polyY[]      =  {114.34491621572f,114.35430357266f,114.35394162485f,114.34598610157f,114.34341262764f,114.34491621572f};
		float  x            =  30.5287291834f;
		float  y            =  114.3500933803f;
		System.out.println(pointInPolygon(polyCorners,polyX,polyY,x,y));
	}

}
