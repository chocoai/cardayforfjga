package com.cmdt.carrental.common.util;

import com.cmdt.carrental.common.model.Marker;
import com.cmdt.carrental.common.model.MarkerModel;
import com.cmdt.carrental.common.model.Pattern;
import com.cmdt.carrental.common.model.PointModel;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
/**
 * 
 * @author lifeng
 *
 */
public class MarkerUtil {
	public static MarkerModel MarkerToModel(Marker marker){
		MarkerModel model = new MarkerModel();
		model.setId(marker.getId());
		model.setName(marker.getName());
		model.setAddress(marker.getAddress());
		model.setTypeBusiness(marker.getTypeBusiness());
		model.setTypeArea(marker.getPattern().getTypeArea());
		model.setMapMarkerPoint(new PointModel(marker.getMapMarkerX(),marker.getMapMarkerY()));
		model.setRadius(marker.getPattern().getRadius());
		model.setTypeArea(marker.getPattern().getTypeArea());
		if(GeometryConstants.POLYGON.equals(marker.getPattern().getTypeArea())){
			model.setPattern(GeometryUtil.polygonToPointModel((Polygon)marker.getPattern().getMapGeometry()));
		}else if(GeometryConstants.CIRCLE.equals(marker.getPattern().getTypeArea())){
			model.setPattern(GeometryUtil.pointToPointModel((Point)marker.getPattern().getMapGeometry()));
		}
		
		return model;
	}
	
	public static Marker ModelToMarker(MarkerModel model){
		Marker marker = new Marker();
		marker.setId(model.getId());
		marker.setName(model.getName());
		marker.setTypeBusiness(model.getTypeBusiness());
		marker.setAddress(model.getAddress());
		marker.setMapMarkerX(model.getMapMarkerPoint().getLng());
		marker.setMapMarkerY(model.getMapMarkerPoint().getLat());
		PointModel gpsPoint = Gps2BaiDu.baidu2Gps(model.getMapMarkerPoint());
		marker.setGpsMarkerPoint((Point) GeometryUtil.wktToGeometry(gpsPoint.toString()));
		Pattern pattern = new Pattern();
		if (GeometryConstants.POLYGON.equals(model.getTypeArea())) {
			pattern.setMapGeometry(GeometryUtil.getMapPolygon(model.getPattern()));
			pattern.setGpsGeometry(GeometryUtil.getGpsPolygon(model.getPattern()));
			pattern.setTypeArea(GeometryConstants.POLYGON);
			marker.setPattern(pattern);
		} else if (GeometryConstants.CIRCLE.equals(model.getTypeArea())) {
			pattern.setMapGeometry(GeometryUtil.getMapPoint(model.getPattern()));
			pattern.setGpsGeometry(GeometryUtil.getGpsPoint(model.getPattern()));
			pattern.setTypeArea(GeometryConstants.CIRCLE);
			if(model.getRadius()<=0){
				throw new RuntimeException("circle Radius must greater than 0");
			}
			pattern.setRadius(model.getRadius());
			marker.setPattern(pattern);
		}
		pattern.setMarker(marker);
		
		return marker;
	}
}
