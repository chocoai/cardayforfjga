package com.cmdt.carrental.common.model;

import java.util.List;
import com.cmdt.carrental.common.entity.Marker;;



public class VehicleOutBoundModel {
	private  List<Marker> markers;
	private List<VehicleHistoryTrack> traceModels;
	public VehicleOutBoundModel() {
	}
	public List<VehicleHistoryTrack> getTraceModels() {
		return traceModels;
	}
	public void setTraceModels(List<VehicleHistoryTrack> traceModels) {
		this.traceModels = traceModels;
	}
	public List<Marker> getMarkers() {
		return markers;
	}
	public void setMarkers(List<Marker> markers) {
		this.markers = markers;
	}

}
