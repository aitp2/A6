package com.hankcs.example.aitp.data;


public class KeywordsEmployeedata {

	private EmployeeData employeeData;
	
	private int publishCount;
	
	private int attendCount;
	
	private int commentCount;

	private int checkinCount;
	
	private double publishActive;
	
	private double attendActive;
	
	private double commentActive;
	
	private double commentAttendScale;
	
	private double checkinAttendScale;
	
	private double totalActive;
	
	private double feelingActive;

	public EmployeeData getEmployeeData() {
		return employeeData;
	}

	public void setEmployeeData(EmployeeData employeeData) {
		this.employeeData = employeeData;
	}

	public int getPublishCount() {
		return publishCount;
	}

	public void setPublishCount(int publishCount) {
		this.publishCount = publishCount;
	}

	public int getAttendCount() {
		return attendCount;
	}

	public void setAttendCount(int attendCount) {
		this.attendCount = attendCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getCheckinCount() {
		return checkinCount;
	}

	public void setCheckinCount(int checkinCount) {
		this.checkinCount = checkinCount;
	}

	public double getPublishActive() {
		return publishActive;
	}

	public void setPublishActive(double publishActive) {
		this.publishActive = publishActive;
	}

	public double getAttendActive() {
		return attendActive;
	}

	public void setAttendActive(double attendActive) {
		this.attendActive = attendActive;
	}

	public double getCommentActive() {
		return commentActive;
	}

	public void setCommentActive(double commentActive) {
		this.commentActive = commentActive;
	}

	public double getCommentAttendScale() {
		return commentAttendScale;
	}

	public void setCommentAttendScale(double commentAttendScale) {
		this.commentAttendScale = commentAttendScale;
	}

	public double getCheckinAttendScale() {
		return checkinAttendScale;
	}

	public void setCheckinAttendScale(double checkinAttendScale) {
		this.checkinAttendScale = checkinAttendScale;
	}

	public double getTotalActive() {
		return totalActive;
	}

	public void setTotalActive(double totalActive) {
		this.totalActive = totalActive;
	}

	public double getFeelingActive() {
		return feelingActive;
	}

	public void setFeelingActive(double feelingActive) {
		this.feelingActive = feelingActive;
	}
}
