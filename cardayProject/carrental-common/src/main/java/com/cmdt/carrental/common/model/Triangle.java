package com.cmdt.carrental.common.model;

public class Triangle {
	private Point[] v;
	
	public Triangle(){
		this.v = new Point[3];
	}
	
	public Point[] getV() {
		return v;
	}

	public void setV(Point[] v) {
		this.v = v;
	}
	
}
