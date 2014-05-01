package com.msse.teamflyte.affinitymapper.services;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.msse.teamflyte.affinitymapper.models.Location;

public class LocationServices {

	private EntityManager entityManager;
	private static final Double coordinateOffsetPerKm = 0.01;
	private static final Double bufferForCoordinate = 0.01;
	private static final Double mileToKmConversionFactor = 1.6;

	LocationServices(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<Location> getNearByLocation(Location originLocation, int range) {
		HashMap<String, Double> coordinateOffset = getRoughApproximateCoordinate(originLocation, range);

		String locationQueryStr = "select from Location as Location where " + "latitude > :lowerLatitude "
				+ "and latitude < :upperLatitude " + "and longitude > :lowerLongitude " + "and longitude < :upperLongitude ";
		Query locationQuery = this.entityManager.createQuery(locationQueryStr);
		locationQuery.setParameter("lowerLatitude", coordinateOffset.get("lowerLatitude"));
		locationQuery.setParameter("upperLatitude", coordinateOffset.get("upperLatitude"));
		locationQuery.setParameter("lowerLongitude", coordinateOffset.get("lowerLongitude"));
		locationQuery.setParameter("upperLongitude", coordinateOffset.get("upperLongitude"));

		return locationQuery.getResultList();
	}

	public static HashMap<String, Double> getRoughApproximateCoordinate(Location originLocation, int range) {
		Double rangeInKm = range * mileToKmConversionFactor;
		Double approxCoordinateOffsetPerUnitOfRange = rangeInKm * coordinateOffsetPerKm * bufferForCoordinate;

		HashMap<String, Double> coordinateOffset = new HashMap<String, Double>();
		coordinateOffset.put("upperLatitude", originLocation.getLatitude() + approxCoordinateOffsetPerUnitOfRange);
		coordinateOffset.put("lowerLatitude", originLocation.getLatitude() - approxCoordinateOffsetPerUnitOfRange);
		coordinateOffset.put("upperLongitude", originLocation.getLongitude() + approxCoordinateOffsetPerUnitOfRange);
		coordinateOffset.put("lowerLongitude", originLocation.getLongitude() - approxCoordinateOffsetPerUnitOfRange);

		return coordinateOffset;
	}

}
