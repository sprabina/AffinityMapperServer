package com.msse.teamflyte.affinitymapper.models;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class Location {

	@Id
	private Key id;
	private String userId;
	private boolean active;
	private Double latitude;
	private Double longitude;
	
	public Key getId() {
		return id;
	}
	public void setId(Key id) {
		this.id = id;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
