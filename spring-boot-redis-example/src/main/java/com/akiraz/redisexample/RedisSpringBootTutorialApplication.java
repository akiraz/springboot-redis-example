package com.akiraz.redisexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.akiraz.redisexample.entities.Coordinate;

@SpringBootApplication
public class RedisSpringBootTutorialApplication {

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();
	}

	@Bean
	RedisTemplate<String,Coordinate> redisTemplate() {
		RedisTemplate<String,Coordinate> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		jedisConnectionFactory().setTimeout(20000);
		return redisTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(RedisSpringBootTutorialApplication.class, args);
	}

}
