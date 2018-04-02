package com.cmdt.carrental.common.model;

public class Circle {
	private Point center;
	private double radius;
	
	public Circle(){
		this.center = new Point();
	}
	
	public Point getCenter() {
		return center;
	}
	public void setCenter(Point center) {
		this.center = center;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	
}
