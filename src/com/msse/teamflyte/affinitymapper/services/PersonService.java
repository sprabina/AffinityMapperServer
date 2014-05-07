package com.msse.teamflyte.affinitymapper.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.msse.teamflyte.affinitymapper.models.Location;
import com.msse.teamflyte.affinitymapper.models.MatchingPersonList;
import com.msse.teamflyte.affinitymapper.models.Person;
import com.msse.teamflyte.affinitymapper.models.MatchingPerson;

;

public class PersonService {

	private EntityManager entityManager;

	public PersonService(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public MatchingPersonList getUsersWithSimilarInterst(List<Location> nearByLocationsOfUsers) {
		//List<String> nearByUserIds = getUserIdFromLocations(nearByLocationsOfUsers);

		 
		List<MatchingPerson> matchingPersons = new ArrayList<MatchingPerson>();
		
		List<String> listOfInterest = new ArrayList<String>();
		listOfInterest.add("Books");
		listOfInterest.add("Games");
		
		MatchingPerson mPerson1 = new MatchingPerson();
		mPerson1.setEmail("UdeebTheGreat");
		mPerson1.setName("Udeeb");	
		mPerson1.setUserId("Udeeb");	
		mPerson1.setImageUrl("Udeeb");		
		mPerson1.setInterestGroups(listOfInterest);
		mPerson1.setLatitude(44.97);
		mPerson1.setLongitude(-93.23);
		
		MatchingPerson mPerson2 = new MatchingPerson();
		mPerson2.setEmail("Udeeeb II");
		mPerson2.setName("Udeeb II");
		mPerson2.setUserId("Udeeb II");	
		mPerson2.setImageUrl("Udeeb II");		
		mPerson2.setInterestGroups(listOfInterest);
		mPerson2.setLatitude(44.9);
		mPerson2.setLongitude(-93.3);
		
		MatchingPerson mPerson3 = new MatchingPerson();
		mPerson3.setEmail("Udeeeb III");
		mPerson3.setName("Udeeb III");	
		mPerson3.setUserId("Udeeb III");	
		mPerson3.setImageUrl("Udeeb III");	
		mPerson3.setInterestGroups(listOfInterest);
		mPerson3.setLatitude(44.8);
		mPerson3.setLongitude(-93.2);
		
		matchingPersons.add(mPerson1);
		matchingPersons.add(mPerson2);
		matchingPersons.add(mPerson3);
		return new MatchingPersonList(matchingPersons);

	}

	public List<String> getUserIdFromLocations(List<Location> nearByLocationsOfUsers) {
		List<String> userIds = new ArrayList<String>();

		for (Location eachLocation : nearByLocationsOfUsers) {
			userIds.add(eachLocation.getUserId());
		}

		return userIds;
	}
}
