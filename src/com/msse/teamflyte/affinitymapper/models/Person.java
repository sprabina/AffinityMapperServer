package com.msse.teamflyte.affinitymapper.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;
	private String name;
	private String email;
	private boolean chatRequestToggle;
	private int proximityAlertLimit;
	private boolean proximityAlertToggle;
	private List<String> interestGroups;

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isChatRequestToggle() {
		return chatRequestToggle;
	}

	public void setChatRequestToggle(boolean chatRequestToggle) {
		this.chatRequestToggle = chatRequestToggle;
	}

	public int getProximityAlertLimit() {
		return proximityAlertLimit;
	}

	public void setProximityAlertLimit(int proximityAlertLimit) {
		this.proximityAlertLimit = proximityAlertLimit;
	}

	public boolean isProximityAlertToggle() {
		return proximityAlertToggle;
	}

	public void setProximityAlertToggle(boolean proximityAlertToggle) {
		this.proximityAlertToggle = proximityAlertToggle;
	}

	public List<String> getInterestGroups() {
		return interestGroups;
	}

	public void setInterestGroups(List<String> interestGroups) {
		this.interestGroups = interestGroups;
	}

}
