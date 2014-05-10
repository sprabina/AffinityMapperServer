package com.msse.teamflyte.affinitymapper.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.msse.teamflyte.affinitymapper.models.Location;

public class LocationService {

	private EntityManager entityManager;

	private static final Double coordinateOffsetPerKm = 0.01;
	private static final Double bufferForCoordinate = 0.01;
	private static final Double mileToKmConversionFactor = 1.6;

	public LocationService(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Location getLocationOfAUser(String userId) {
		String locationQueryStr = "select from Location as Location where userId = :value ";
		Query locationQuery = entityManager.createQuery(locationQueryStr);
		locationQuery.setParameter("value", userId);

		Location location = null;
		if (locationQuery.getResultList().size() > 0) {
			location = (Location) locationQuery.getResultList().get(0);
			System.out.println("Location of a user is => " + location.getUserId() + "<>" + location.getLatitude() + "<>"
					+ location.getLongitude());

		}
		return location;
	}

	public List<Location> getNearByLocation(Location originLocation, int range) {
		HashMap<String, Double> coordinateOffset = getRoughApproximateCoordinate(originLocation, range);

		String locationQueryStrLatitude = "select from Location as Location where " + " latitude > :lowerLatitude "
				+ " and latitude < :upperLatitude";
		Query locationQueryLatitude = this.entityManager.createQuery(locationQueryStrLatitude);

		System.out.println("LocationService Lower latitude is => " + coordinateOffset.get("lowerLatitude"));
		locationQueryLatitude.setParameter("lowerLatitude", coordinateOffset.get("lowerLatitude"));
		System.out.println("LocationService Upper latitude is => " + coordinateOffset.get("upperLatitude"));
		locationQueryLatitude.setParameter("upperLatitude", coordinateOffset.get("upperLatitude"));

		String locationQueryStrLongitude = "select from Location as Location where " + " longitude > :lowerLongitude "
				+ " and longitude < :upperLongitude ";
		Query locationQueryLongitude = this.entityManager.createQuery(locationQueryStrLongitude);

		System.out.println("LocationService Lower longitude is => " + coordinateOffset.get("lowerLongitude"));
		locationQueryLongitude.setParameter("lowerLongitude", coordinateOffset.get("lowerLongitude"));
		System.out.println("LocationService Upper longitude is => " + coordinateOffset.get("upperLongitude"));
		locationQueryLongitude.setParameter("upperLongitude", coordinateOffset.get("upperLongitude"));

		System.out.println("LocationService Latitude Query is => " + locationQueryLatitude.toString());
		System.out.println("LocationService Longitude Query is => " + locationQueryLongitude.toString());

		List<Location> locationByLatitude = locationQueryLatitude.getResultList();
		List<Location> locationByLongitude = locationQueryLongitude.getResultList();

		System.out.println("LocationService Total location per latitude criteria is => " + locationByLatitude.size());
		System.out.println("LocationService Total location per longitude criteria is => " + locationByLongitude.size());

		List<Location> locationIntersection = new ArrayList<Location>();

		for (Location locationLatitude : locationByLatitude) {
			for (Location locationLongitude : locationByLongitude) {
				if (locationLatitude.getUserId().compareTo(locationLongitude.getUserId()) == 0) {
					locationIntersection.add(locationLatitude);
				}
			}
		}

		System.out.println("Total location per criteria is => " + locationIntersection.size());
		return locationIntersection;
	}

	public static HashMap<String, Double> getRoughApproximateCoordinate(Location originLocation, int range) {
		Double rangeInKm = range == 0 ? 10 * mileToKmConversionFactor : range * mileToKmConversionFactor;
		Double approxCoordinateOffsetPerUnitOfRange = rangeInKm * coordinateOffsetPerKm + bufferForCoordinate;

		System.out.println("LocationService approxCoordinateOffsetPerUnitOfRange => " + approxCoordinateOffsetPerUnitOfRange);

		HashMap<String, Double> coordinateOffset = new HashMap<String, Double>();
		coordinateOffset.put("upperLatitude", originLocation.getLatitude() + approxCoordinateOffsetPerUnitOfRange);
		coordinateOffset.put("lowerLatitude", originLocation.getLatitude() - approxCoordinateOffsetPerUnitOfRange);
		coordinateOffset.put("upperLongitude", originLocation.getLongitude() + approxCoordinateOffsetPerUnitOfRange);
		coordinateOffset.put("lowerLongitude", originLocation.getLongitude() - approxCoordinateOffsetPerUnitOfRange);

		return coordinateOffset;
	}

}
