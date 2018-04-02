package com.cmdt.carrental.common.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.cmdt.carrental.common.model.PointModel;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;


public class GeometryUtil {
	
	private final static int SRID = 4326;
	
	/**
	 * WKT String to Geometry ,init SRID is 4326,if you modifie this ,please chang private static SRID;
	 * @param wktData
	 * @return
	 */
    public static Geometry wktToGeometry(String wktData) {
        WKTReader fromText = new WKTReader();
        Geometry geom = null;
        try {
            geom = fromText.read(wktData);
            geom.setSRID(SRID);
        } catch (ParseException e) {
            throw new RuntimeException("Not a WKT string:" + wktData);
        }
        return geom;
    }
    
    /**
     * polygon to List group pointModel
     * @param polygon
     * @return
     */
    public static List<List<PointModel>> polygonToPointModel(Polygon polygon){
    	String polygonStr =  polygon.toString();
    	return polygonStrToList(polygonStr);
    }
    
    /**
     * point to pointModel
     * @param point
     * @return
     */
    public static List<List<PointModel>> pointToPointModel(Point point){
    	String pointStr = point.toString();
    	String points = pointStr.substring(pointStr.indexOf("(")+1, pointStr.indexOf(")"));
    	PointModel model = pointStrToModel(points);
    	List<List<PointModel>> list = new ArrayList<>();
    	List<PointModel> pointList = new ArrayList<>();
    	pointList.add(model);
    	list.add(pointList);
    	return list;
    }
    
    /**
     * List baidu map point group to Polygon
     * @param polygonMarker
     * @return
     */
    public static Polygon getMapPolygon(List<List<PointModel>> polygonMarker) {
		return getPolygon(polygonMarker, CoordsType.map);
	}
    
    /**
     * list point group to GPS polygon from baidu map list point group
     * @param polygonMarker
     * @return
     */
	public static Polygon getGpsPolygon(List<List<PointModel>> polygonMarker) {
		return getPolygon(polygonMarker, CoordsType.gps);
	}

	/**
	 * list point group to polygon,if CoordsType equals gps ,convert to gps polygon, 
	 * if CoordsType equals map, convert to map polygon 
	 * @param polygonMarker
	 * @param type
	 * @return Polygon
	 */
	private static Polygon getPolygon(List<List<PointModel>> polygonMarker, CoordsType type) {
		StringBuffer polygonStr = new StringBuffer();
		polygonStr.append("POLYGON(");
		for (List<PointModel> listModel : polygonMarker) {
			StringBuffer bf = new StringBuffer();
			bf.append("(");
			PointModel first = null;
			if(!listModel.get(0).equals(listModel.get(listModel.size()-1))){
				throw new RuntimeException("getPolygon error!");
			}
			for(int i= 0;i<listModel.size();i++){
				PointModel point = listModel.get(i);
				
				if (type == CoordsType.gps) {
					point = Gps2BaiDu.baidu2Gps(point);
				}
				if(i==0){
					first = point;
				}
				if(i==listModel.size()-1){
					bf.append(first.getLng() + " " + first.getLat() + ",");
				}else{
					bf.append(point.getLng() + " " + point.getLat() + ",");
				}
				
			}
			String s = bf.substring(0, bf.length() - 1) + ")";
			polygonStr.append(s);
		}
		String polygonG = polygonStr+")";
		Polygon polygon = (Polygon) GeometryUtil.wktToGeometry(polygonG);

		return polygon;
	}

	/**
	 * Map pointModel to map Point
	 * @param pointList
	 * @return Point
	 */
	public static Point getMapPoint(List<List<PointModel>> pointList){
		PointModel model = pointList.get(0).get(0);
		return getMapPoint(model);
		
	}
	
	/**
	 * Map pointModel to gsp point
	 * @param pointList
	 * @return
	 */
	public static Point getGpsPoint(List<List<PointModel>> pointList){
		PointModel model = pointList.get(0).get(0);
		return getGpsPoint(model);
	}
	
	/**
	 * Map pointModel to map Point
	 * @param pointModel
	 * @return Point
	 */
	private static Point getMapPoint(PointModel pointModel) {
		return getPoint(pointModel,CoordsType.map);
	}
	
	/**
	 * Map pointModel to gsp point
	 * @param pointModel
	 * @return
	 */
	private static Point getGpsPoint(PointModel pointModel){
		return getPoint(pointModel,CoordsType.gps);
	}
	
	/**
	 * map pointModel to point,if CoordsType equals gps ,convert to gps point, 
	 * if CoordsType equals map, convert to map point 
	 * @param pointModel
	 * @param type
	 * @return
	 */
	private static Point getPoint(PointModel pointModel,CoordsType type){
		
		if(CoordsType.gps==type){
			PointModel point = Gps2BaiDu.baidu2Gps(pointModel);
			return (Point) GeometryUtil.wktToGeometry(point.toString());
		}else{
			return (Point) GeometryUtil.wktToGeometry(pointModel.toString());
		}
	}
	
	
	private static List<List<PointModel>> polygonStrToList(String polygonStr){
    	String pointGroup = polygonStr.substring(polygonStr.indexOf("(")+1, polygonStr.lastIndexOf(")"));
    	List<List<PointModel>> polygon = new ArrayList<>();
    	String points = pointGroup.substring(pointGroup.indexOf("(")+1, pointGroup.indexOf(")"));
    	pointGroup=pointGroup.substring(pointGroup.indexOf(")")+1, pointGroup.length());
    	
    	while(!StringUtils.isEmpty(points)){
    		List<PointModel> pointList = pointsStrToModel(points);
    		polygon.add(pointList);
    		if(!StringUtils.isEmpty(pointGroup)){
    			points = pointGroup.substring(pointGroup.indexOf("(")+1, pointGroup.indexOf(")"));
    			pointGroup=pointGroup.substring(pointGroup.indexOf(")")+1, pointGroup.length());
    		}else{
    			points="";
    		}
    		
        	
    	}
    	
    	/*for(String points = pointGroup.substring(pointGroup.indexOf("(")+1, pointGroup.indexOf(")"));!StringUtils.isEmpty(points);pointGroup=pointGroup.substring(pointGroup.indexOf(")")+1, pointGroup.length())){
    		List<PointModel> pointList = pointsStrToModel(points);
    		polygon.add(pointList);
    	}*/
    	return polygon;
    	
    }
    
    private static List<PointModel> pointsStrToModel(String points){
    	String[] pointArray = points.split(",");
    	List<PointModel> listPoint = new ArrayList<>();
    	for(String point:pointArray ){
    		PointModel pointModel = pointStrToModel(point);
    		listPoint.add(pointModel);
    	}
    	
    	return listPoint;
    }
    
    private static PointModel pointStrToModel(String point){
    	String points[] = point.trim().split("\\ ");
    	PointModel pointModel = new PointModel();
    	pointModel.setLat(Double.parseDouble(points[0]));
    	pointModel.setLng(Double.parseDouble(points[1]));
    	return pointModel;
    	
    }

	enum CoordsType {
		map, gps,
	}
}
