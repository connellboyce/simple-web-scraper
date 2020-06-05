package com.connellboyce.scraper;

import com.connellboyce.scraper.scrapers.PepperScraper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScraperApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScraperApplication.class, args);
		startScrapers();
	}

	/**
	 * Starts threads to run the PepperScraper file
	 */
	public static void startScrapers() {
		new Thread(new PepperScraper()).start();
	}
}
