package com.msse.teamflyte.affinitymapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.msse.teamflyte.affinitymapper.models.InterestEnum;
import com.msse.teamflyte.affinitymapper.models.Location;
import com.msse.teamflyte.affinitymapper.models.MatchingPerson;
import com.msse.teamflyte.affinitymapper.models.MatchingPersonList;
import com.msse.teamflyte.affinitymapper.models.Person;
import com.msse.teamflyte.affinitymapper.services.LocationService;
import com.msse.teamflyte.affinitymapper.services.PersonService;

@Api(name = "affinitymapper", version = "v1")
public class AffinityMapperController {

	PersonService personService;
	LocationService locationService;

	@SuppressWarnings("unchecked")
	@ApiMethod(name = "getUser", path = "user/{userId}", httpMethod = HttpMethod.GET)
	public Person getUser(@Named("userId") String userId) {
		EntityManager mgr = getEntityManager();
		try {
			String queryStr = "select from Person as Person where userId = :value ";
			Query query = mgr.createQuery(queryStr);
			query.setParameter("value", userId);
			if (query.getResultList().size() > 0) {
				return ((List<Person>) query.getResultList()).get(0);
			}

		} finally {
			mgr.close();
		}

		return null;
	}

	@ApiMethod(name = "addUser", path = "user/add", httpMethod = HttpMethod.POST)
	public void addUser(Person requestBody) {
		EntityManager mgr = getEntityManager();
		try {
			Person person = new Person();
			if (requestBody.getUserId() != null) {
				person.setUserId(requestBody.getUserId());
			}
			if (requestBody.getEmail() != null) {
				person.setEmail(requestBody.getEmail());
			}
			if (requestBody.getImageUrl() != null) {
				person.setImageUrl(requestBody.getImageUrl());
			}
			if (requestBody.getName() != null) {
				person.setName(requestBody.getName());
			}
			person.setChatRequestToggle(requestBody.isChatRequestToggle());
			person.setProximityAlertLimit(requestBody.getProximityAlertLimit());
			person.setProximityAlertToggle(requestBody.isProximityAlertToggle());
			person.setInterestGroups(requestBody.getInterestGroups());
			mgr.persist(person);
		} finally {
			mgr.close();
		}
	}

	@ApiMethod(name = "getNearByUsers", path = "getNearByUsers/{userId}/{interest}", httpMethod = HttpMethod.GET)
	public MatchingPersonList getNearByUsers(@Named("userId") String userId,
			@Named("interest") String interest) {
		EntityManager mgr = getEntityManager();
		personService = new PersonService(mgr);
		locationService = new LocationService(mgr);
		try {
			// TODO query the location service and then pass the list to the
			// PersonService.
			Person currentUser = personService.getUser(userId);
			Location currentUserLocation = locationService
					.getLocationOfAUser(userId);
			List<Location> nearByLocationsOfUsers = locationService
					.getNearByLocation(currentUserLocation,
							currentUser.getProximityAlertLimit());
			System.out.println("Controller nearByLocationsOfUsers => "
					+ nearByLocationsOfUsers.size());

			MatchingPersonList mpList = personService
					.getUsersWithSimilarInterst(currentUser,
							nearByLocationsOfUsers, interest);

			personService.addDummyUserData(mpList.getMatchingPersons(),
					currentUserLocation);

			return mpList;
			// List<MatchingPerson> matchingPersonList = new
			// ArrayList<MatchingPerson>();
			// personService.addDummyUserData(matchingPersonList,
			// currentUserLocation);
			// MatchingPersonList mpList = new
			// MatchingPersonList(matchingPersonList);
			//
			// return mpList;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			mgr.close();
		}
		return null;
	}

	@ApiMethod(name = "UpdateUser", path = "user/update/{userId}", httpMethod = HttpMethod.POST)
	public void updateUser(@Named("userId") String userId, Person requestBody) {

		EntityManager mgr = getEntityManager();
		try {
			String queryStr = "select from Person as Person where userId = :value ";
			Query query = mgr.createQuery(queryStr);
			query.setParameter("value", userId);

			if (query.getResultList().size() > 0) {
				Person person = (Person) query.getResultList().get(0);
				if (requestBody.getEmail() != null) {
					person.setEmail(requestBody.getEmail());
				}
				if (requestBody.getImageUrl() != null) {
					person.setImageUrl(requestBody.getImageUrl());
				}
				if (requestBody.getName() != null) {
					person.setName(requestBody.getName());
				}
				person.setChatRequestToggle(requestBody.isChatRequestToggle());
				person.setProximityAlertLimit(requestBody
						.getProximityAlertLimit());
				person.setProximityAlertToggle(requestBody
						.isProximityAlertToggle());
				person.setInterestGroups(requestBody.getInterestGroups());
				mgr.persist(person);
			}

		} finally {
			mgr.close();
		}
	}

	@ApiMethod(name = "updateLocation", path = "location/user/{uniqueId}", httpMethod = HttpMethod.POST)
	public Location updateLocation(@Named("uniqueId") String uniqueId,
			Location requestBody) {
		EntityManager mgr = getEntityManager();
		try {
			String queryStr = "select from Person as Person where userId = :value ";
			Query query = mgr.createQuery(queryStr);
			query.setParameter("value", uniqueId);

			if (query.getResultList().size() > 0) {
				Person person = (Person) query.getResultList().get(0);

				String locationQueryStr = "select from Location as Location where userId = :value ";
				Query locationQuery = mgr.createQuery(locationQueryStr);
				locationQuery.setParameter("value", uniqueId);

				Location location = null;
				if (locationQuery.getResultList().size() > 0) {
					location = (Location) locationQuery.getResultList().get(0);
				} else {
					String uuid = UUID.randomUUID().toString();
					Key key = KeyFactory.createKey(person.getId(),
							Location.class.getSimpleName(), uuid);
					location = new Location();
					location.setId(key);
					location.setUserId(uniqueId);
				}
				location.setActive(true);
				location.setLatitude(requestBody.getLatitude());
				location.setLongitude(requestBody.getLongitude());
				mgr.persist(location);
				mgr.refresh(location);

				return location;
			}

		} finally {
			mgr.close();
		}
		return null;
	}

	@ApiMethod(name = "getLocation", path = "location/user/{userId}", httpMethod = HttpMethod.GET)
	public Location getLocation(@Named("userId") String userId) {
		EntityManager mgr = getEntityManager();
		try {
			String queryStr = "select from Person as Person where userId = :value ";
			Query query = mgr.createQuery(queryStr);
			query.setParameter("value", userId);

			if (query.getResultList().size() > 0) {
				String locationQueryStr = "select from Location as Location where userId = :value ";
				Query locationQuery = mgr.createQuery(locationQueryStr);
				locationQuery.setParameter("value", userId);

				if (locationQuery.getResultList().size() > 0) {
					return ((Location) locationQuery.getResultList().get(0));
				}
			}

		} finally {
			mgr.close();
		}

		return null;
	}

	@ApiMethod(name = "listInterestGroups", path = "listInterestGroups", httpMethod = HttpMethod.GET)
	public List<String> listInterestGroups() {
		List<String> response = new ArrayList<String>();
		for (InterestEnum interest : InterestEnum.values()) {
			response.add(interest.getDisplayName());
		}
		return response;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}
}
