package com.akiraz.redisexample.util;

import com.akiraz.redisexample.entities.Coordinate;
import com.akiraz.redisexample.entities.RectangleDto;

public class RectangleUtil {

	public static RectangleDto GetBoundingCoords(double centerLat, double centerLong, double distance) {
		RectangleDto rec = new RectangleDto();

		rec.setTop(MaxLatLongOnBearing(centerLat, centerLong, 0, distance));
		rec.setBottom(MaxLatLongOnBearing(centerLat, centerLong, 180, distance));
		rec.setRight(MaxLatLongOnBearing(centerLat, centerLong, 90, distance));
		rec.setLeft(MaxLatLongOnBearing(centerLat, centerLong, 270, distance));
		
		return rec;
	}

	public static Coordinate MaxLatLongOnBearing(double centerLat, double centerLong, double bearing, double distance) {

		double lonRads = Math.toRadians(centerLong);
		double latRads = Math.toRadians(centerLat);
		double bearingRads = Math.toRadians(bearing);
		double maxLatRads = Math.asin(Math.sin(latRads) * Math.cos(distance / 6371)
				+ Math.cos(latRads) * Math.sin(distance / 6371) * Math.cos(bearingRads));
		double maxLonRads = lonRads
				+ Math.atan2((Math.sin(bearingRads) * Math.sin(distance / 6371) * Math.cos(latRads)),
						(Math.cos(distance / 6371) - Math.sin(latRads) * Math.sin(maxLatRads)));

		double maxLat = Math.toDegrees(maxLatRads);
		double maxLong = Math.toDegrees(maxLonRads);

		Coordinate coordinate = new Coordinate();
		coordinate.setLatitude(maxLat);
		coordinate.setLongitude(maxLong);

		return coordinate;
	}

}
