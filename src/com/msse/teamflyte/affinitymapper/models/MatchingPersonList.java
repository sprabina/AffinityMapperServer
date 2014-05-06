package com.msse.teamflyte.affinitymapper.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

public class MatchingPersonList {
	private List<MatchingPerson> matchingPersons;
	
	public MatchingPersonList(List<MatchingPerson> matchingPersons){
		this.setMatchingPersons(matchingPersons);
	}
	
	public List<MatchingPerson> getMatchingPersons() {
		return matchingPersons;
	}

	public void setMatchingPersons(List<MatchingPerson> matchingPersons) {
		this.matchingPersons = matchingPersons;
	}
}
