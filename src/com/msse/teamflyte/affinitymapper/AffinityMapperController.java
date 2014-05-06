package com.msse.teamflyte.affinitymapper;

import java.util.ArrayList;
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
import com.msse.teamflyte.affinitymapper.services.PersonService;

@Api(name = "affinitymapper", version = "v1")
public class AffinityMapperController {

	PersonService personService;
	@SuppressWarnings("unchecked")
	@ApiMethod(name = "getUser", path = "user/{userId}", httpMethod = HttpMethod.GET)
	public Person getUser(@Named("userId") String userId) {
		EntityManager mgr = getEntityManager();
		try {
			String queryStr = "select from Person as Person where email = :email ";
			Query query = mgr.createQuery(queryStr);
			query.setParameter("email", userId);
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
			person.setEmail(requestBody.getEmail());
			person.setName(requestBody.getName());
			person.setChatRequestToggle(requestBody.isChatRequestToggle());
			person.setProximityAlertLimit(requestBody.getProximityAlertLimit());
			person.setProximityAlertToggle(requestBody.isProximityAlertToggle());
			person.setInterestGroups(requestBody.getInterestGroups());			
			mgr.persist(person);
		} finally {
			mgr.close();
		}
	}
	
	@ApiMethod(name = "getNearByUsers", path = "getNearByUsers/{userId}", httpMethod = HttpMethod.GET)
	public MatchingPersonList getNearByUsers(@Named("userId") String userId) {
		EntityManager mgr = getEntityManager();
		personService = new PersonService(mgr);
		try {
			//TODO query the location service and then pass the list to the PersonService.
			return personService.getUsersWithSimilarInterst(null);
		} finally {
			mgr.close();
		}
	}
	
	@ApiMethod(name = "UpdateUser", path = "user/{userId}", httpMethod = HttpMethod.PUT)
	public void updateUser(@Named("userId") String userId, Person requestBody) {
		
		EntityManager mgr = getEntityManager();
		try {
			String queryStr = "select from Person as Person where email = :email ";
			Query query = mgr.createQuery(queryStr);
			query.setParameter("email", userId);

			if (query.getResultList().size() > 0) {
				Person person = (Person) query.getResultList().get(0);
				person.setName(requestBody.getName());
				person.setChatRequestToggle(requestBody.isChatRequestToggle());
				person.setProximityAlertLimit(requestBody.getProximityAlertLimit());
				person.setProximityAlertToggle(requestBody.isProximityAlertToggle());
				person.setInterestGroups(requestBody.getInterestGroups());
				mgr.persist(person);
			}

		} finally {
			mgr.close();
		}
	}
	
	@ApiMethod(name = "updateLocation", path = "location/user/{userId}", httpMethod = HttpMethod.POST)
	public void updateLocation(@Named("userId") String userId, Location requestBody) {
		EntityManager mgr = getEntityManager();
		try {
			String queryStr = "select from Person as Person where email = :email ";
			Query query = mgr.createQuery(queryStr);
			query.setParameter("email", userId);

			if (query.getResultList().size() > 0) {
				Person person = (Person) query.getResultList().get(0);
				
				String locationQueryStr = "select from Location as Location where email = :email ";
				Query locationQuery = mgr.createQuery(locationQueryStr);
				locationQuery.setParameter("email", userId);
				
				Location location = null;
				if (locationQuery.getResultList().size() > 0){
					 location = (Location) locationQuery.getResultList().get(0);
				}else
				{
					String uuid = UUID.randomUUID().toString();
					Key key = KeyFactory.createKey(person.getId(), Location.class.getSimpleName(), uuid);
					location = new Location();
					location.setId(key);
					location.setEmail(userId);
				}
				location.setActive(true);
				location.setLatitude(requestBody.getLatitude());
				location.setLongitude(requestBody.getLongitude());
				mgr.persist(location);
			}		
			
		} finally {
			mgr.close();
		}
	}
	
	@ApiMethod(name = "getLocation", path = "location/user/{userId}", httpMethod = HttpMethod.GET)
	public Location getLocation(@Named("userId") String userId) {
		EntityManager mgr = getEntityManager();
		try {
			String queryStr = "select from Person as Person where email = :email ";
			Query query = mgr.createQuery(queryStr);
			query.setParameter("email", userId);

			if (query.getResultList().size() > 0) {
				String locationQueryStr = "select from Location as Location where email = :email ";
				Query locationQuery = mgr.createQuery(locationQueryStr);
				locationQuery.setParameter("email", userId);
				
				if (locationQuery.getResultList().size() > 0){
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
