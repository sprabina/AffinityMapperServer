package com.msse.teamflyte.affinitymapper.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.msse.teamflyte.affinitymapper.models.Location;
import com.msse.teamflyte.affinitymapper.models.Person;
import com.msse.teamflyte.affinitymapper.models.MatchingPerson;

;

public class PersonService {

	private EntityManager entityManager;

	public PersonService(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<MatchingPerson> getUsersWithSimilarInterst(List<Location> nearByLocationsOfUsers) {
		//List<String> nearByUserIds = getUserIdFromLocations(nearByLocationsOfUsers);

		List<MatchingPerson> matchingPersons = new ArrayList<MatchingPerson>();
		MatchingPerson mPerson1 = new MatchingPerson();
		mPerson1.setEmail("UdeebTheGreat");
		mPerson1.setName("Udeeb");
		List<String> listOfInterest = new ArrayList<String>();
		listOfInterest.add("Books");
		listOfInterest.add("Games");
		mPerson1.setInterestGroups(listOfInterest);
		mPerson1.setLatitude(21.75);
		mPerson1.setLongitude(21.75);
		
		matchingPersons.add(mPerson1);
		return matchingPersons;

	}

	public List<String> getUserIdFromLocations(List<Location> nearByLocationsOfUsers) {
		List<String> userIds = new ArrayList<String>();

		for (Location eachLocation : nearByLocationsOfUsers) {
			userIds.add(eachLocation.getEmail());
		}

		return userIds;
	}
}
