package com.msse.teamflyte.affinitymapper.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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

	public Person getUser(String userId) {

		String queryStr = "select from Person as Person where userId = :value ";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("value", userId);
		if (query.getResultList().size() > 0) {
			return ((List<Person>) query.getResultList()).get(0);
		}

		return null;
	}

	public MatchingPersonList getUsersWithSimilarInterst(Person currentUser, List<Location> nearByLocationsOfUsers,
			String interest) {
		// List<String> nearByUserIds =
		// getUserIdFromLocations(nearByLocationsOfUsers);

		HashMap<String, Location> userLocationHashMap = getUserHashMapWithLocation(nearByLocationsOfUsers);
		System.out.println("PersonService userLocationHashMap => " + userLocationHashMap.size());

		List<Person> nearByPersonWithAnyInterest = getUsersByIds(userLocationHashMap.keySet());
		System.out.println("PersonService nearByPersonWithAnyInterest => " + nearByPersonWithAnyInterest.size());

		List<MatchingPerson> matchingPersons = new ArrayList<MatchingPerson>();

		for (Person potentialNearByUser : nearByPersonWithAnyInterest) {
			System.out.println("PersonService potentialNearByUser => "
					+ Arrays.toString(potentialNearByUser.getInterestGroups().toArray()));

			if (potentialNearByUser.getInterestGroups().contains(interest)) {
				System.out.println("PersonService Common interest is => " + interest);

				MatchingPerson candidatePerson = new MatchingPerson();
				candidatePerson.setEmail(potentialNearByUser.getEmail());
				candidatePerson.setImageUrl(potentialNearByUser.getImageUrl());
				candidatePerson.setInterestGroups(potentialNearByUser.getInterestGroups());
				candidatePerson.setName(potentialNearByUser.getName());
				candidatePerson.setUserId(potentialNearByUser.getUserId());

				Location userLocation = userLocationHashMap.get(potentialNearByUser.getUserId());
				candidatePerson.setLatitude(userLocation.getLatitude());
				candidatePerson.setLongitude(userLocation.getLongitude());

				matchingPersons.add(candidatePerson);
			}

		}

		if (matchingPersons.size() > 0 && matchingPersons.size() < 5) {
			addDummyUserData(matchingPersons);
		}

		MatchingPersonList finalPersonList = new MatchingPersonList(matchingPersons);
		return finalPersonList;
	}

	public List<Person> getUsersByIds(Set<String> set) {
		System.out.println("PersonService getUsersByIds => " + Arrays.toString(set.toArray()));

		StringBuilder strBuilder = new StringBuilder();
		for (String userId : set) {
			strBuilder.append("\'" + userId + "\',");
		}
		String finalValueForQuery = strBuilder.substring(0, strBuilder.length() - 1);
		System.out.println("PersonService getUsersByIds string buider => " + finalValueForQuery);

		String queryStr = "select from Person as Person where userId in ( :value )";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("value", finalValueForQuery);

		return ((List<Person>) query.getResultList());
	}

	public HashMap<String, Location> getUserHashMapWithLocation(List<Location> nearByLocationsOfUsers) {
		HashMap<String, Location> userLocationHashMap = new HashMap<String, Location>();
		for (Location eachLocation : nearByLocationsOfUsers) {
			userLocationHashMap.put(eachLocation.getUserId(), eachLocation);
		}
		return userLocationHashMap;
	}

	public List<String> getUserIdFromLocations(List<Location> nearByLocationsOfUsers) {
		List<String> userIds = new ArrayList<String>();

		for (Location eachLocation : nearByLocationsOfUsers) {
			userIds.add(eachLocation.getUserId());
		}

		return userIds;
	}

	public void addDummyUserData(List<MatchingPerson> matchingPersonList) {
		List<String> listOfInterest = new ArrayList<String>();
		listOfInterest.add("Books");
		listOfInterest.add("Games");

		MatchingPerson mPerson1 = new MatchingPerson();
		mPerson1.setEmail("DoNotClick-Dummy I");
		mPerson1.setName("DoNotClick-Dummy I");
		mPerson1.setUserId("DoNotClick-Dummy I");
		mPerson1.setImageUrl("DoNotClick-Dummy I");
		mPerson1.setInterestGroups(listOfInterest);
		mPerson1.setLatitude(matchingPersonList.get(0).getLatitude() + .00001);
		mPerson1.setLongitude(matchingPersonList.get(0).getLongitude() + .00001);

		MatchingPerson mPerson2 = new MatchingPerson();
		mPerson2.setEmail("DoNotClick-Dummy II");
		mPerson2.setName("DoNotClick-Dummy II");
		mPerson2.setUserId("DoNotClick-Dummy II");
		mPerson2.setImageUrl("DoNotClick-Dummy II");
		mPerson2.setInterestGroups(listOfInterest);
		mPerson2.setLatitude(matchingPersonList.get(0).getLatitude() + .00002);
		mPerson2.setLongitude(matchingPersonList.get(0).getLongitude() + .00002);

		MatchingPerson mPerson3 = new MatchingPerson();
		mPerson3.setEmail("DoNotClick-Dummy III");
		mPerson3.setName("DoNotClick-Dummy III");
		mPerson3.setUserId("DoNotClick-Dummy III");
		mPerson3.setImageUrl("DoNotClick-Dummy III");
		mPerson3.setInterestGroups(listOfInterest);
		mPerson3.setLatitude(matchingPersonList.get(0).getLatitude() + .00003);
		mPerson3.setLongitude(matchingPersonList.get(0).getLongitude() + .00003);

		matchingPersonList.add(mPerson1);
		matchingPersonList.add(mPerson2);
		matchingPersonList.add(mPerson3);
	}
}
