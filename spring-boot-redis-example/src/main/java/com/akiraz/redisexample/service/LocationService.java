package com.akiraz.redisexample.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akiraz.redisexample.entities.RectangleDto;
import com.akiraz.redisexample.util.RectangleUtil;

@RestController
@RequestMapping("/rest/")
public class LocationService {

	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	RedisOperations<String, String> operations;
	GeoOperations<String, String> geoOperations;

	@Autowired
	public LocationService(RedisOperations<String, String> operations) {
		this.operations = operations;
		geoOperations = operations.opsForGeo();
	}

	public LocationService() {

	}

	@GetMapping("/getNearestPoints/{latitude}/{longitude}/{radius}")
	public void getNearestPoints(@PathVariable("latitude") double latitude, @PathVariable("longitude") double longitude,
			@PathVariable("radius") double radius) {

		Circle circle = new Circle(new Point(latitude, longitude), new Distance(radius,Metrics.KILOMETERS));
	
		long startTime = System.currentTimeMillis();
		GeoResults<GeoLocation<String>> result = geoOperations.geoRadius("Location", circle,GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates());
		int resultCount = 0;
		if (result != null && result.getContent() != null) {
			 resultCount = result.getContent().size();
			for (int i = 0; i < resultCount; i++) {
				GeoLocation<String> geoLocation = result.getContent().get(i).getContent();
				//System.out.println(geoLocation.getName()+" "+result.getContent().get(i).getContent().getPoint().getX() +  " " + result.getContent().get(i).getContent().getPoint().getY()+ " "+result.getContent().get(i).getDistance());
			}
		}

		long endTime = System.currentTimeMillis();
		double elapsedTime = (endTime - startTime);
		System.out.println("Result Count = " + resultCount);
		System.out.println(elapsedTime + " ms");
	}

	@GetMapping("/populateData")
	public void populate() {

		RectangleDto rec = RectangleUtil.GetBoundingCoords(40.990701, 29.044168, 30);

		for (int i = 0; i < 10000000; i++) {
			double startLat = rec.getTop().getLatitude();
			double endLat = rec.getBottom().getLatitude();

			double randomLat = startLat + new Random().nextDouble() * (endLat - startLat);

			double startLong = rec.getRight().getLongitude();
			double endLong = rec.getLeft().getLongitude();

			double randomLong = startLong + (new Random().nextDouble() * (endLong - startLong));

			System.out.println(randomAlphaNumeric(5) + " " + randomLat + "," + randomLong);

			geoOperations.geoAdd("Location", new Point(randomLat, randomLong), randomAlphaNumeric(5));

		}

	}

	private String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.append(" City").toString();
	}

}
