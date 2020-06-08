package com.connellboyce.scraper.scrapers;

import com.connellboyce.scraper.PepperDetail;
import com.connellboyce.scraper.PepperLink;
import com.connellboyce.scraper.converters.PeppersToJSONConverter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class PepperScraper implements Runnable {

    public String chilePlanetURL = "http://www.chileplanet.eu";
    public String chilePlanetPage = "database.html";
    public List<PepperLink> pepperLinks = new ArrayList<>();
    public List<PepperDetailScraper> pepperDetails = new ArrayList<>();
    public Set<PepperDetail> foundDetails = new HashSet<>();

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        extractPepperURLs();
        try {
            findDetails();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Output to console
        //pepperDetails.stream().forEach(e -> System.out.println(e.toString()));

        PeppersToJSONConverter converter = new PeppersToJSONConverter();
        converter.convertToFile(foundDetails);
    }

    /**
     * This method examines the entire list of pepper links in the main table of the website,
     * pulling the name and href from the HTML of the webpage.
     * This information is added to a pepperLink object, and subsequently added to a list of
     * pepperLinks.
     */
    private void extractPepperURLs() {
        Document doc = null;

        //Join the base URL and the specific page we are using
        String dbPage = StringUtils.join(new String[] {chilePlanetURL, chilePlanetPage},"/");

        try {
            //Connect to initial page
            doc = Jsoup.connect(dbPage).get();

            //Navigate to the elements in the document with a class "td_events1"
            Elements pepperTypes = doc.select(".td_events1");

            //Iterate through the pepperTypes found in the above query
            for (Element pepperType : pepperTypes) {

                //Extract the pepper name
                String tempName = pepperType.text();

                //Check for blank name
                if (StringUtils.isNotBlank(tempName)) {
                    //Access the aref
                    Element aref = pepperType.select("a").first();
                    //If aref is not null, access the href
                    if (aref != null) {
                        String tempHref = aref.attr("href");
                        //if href is not null, add it to the pepperLink object
                        if(StringUtils.isNotBlank(tempHref)) {
                            String fullURL = StringUtils.join(new String[] {chilePlanetURL, tempHref},"/");
                            PepperLink tempPepper = new PepperLink(StringUtils.stripAccents(tempName).replace("?","n"), fullURL);

                            //add the pepperLink to the list of pepperLinks
                            pepperLinks.add(tempPepper);
                        }
                    }

                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        for (PepperLink pepperLink : pepperLinks) {
            System.out.println("Found link to: " + pepperLink);
        }
    }

    /**
     * Based on the peppers stored in pepperLinks, this program
     * scrapes their links to find more specific information
     * and populate a PepperDetail object for each pepper.
     */
    public void findDetails() throws InterruptedException {

        //Base URL to precede the URL extensions from the images
        String baseURL = "http://www.chileplanet.eu/schede";

        ExecutorService executor = Executors.newFixedThreadPool(25);


        //Iterate through the list of pepperLinks
        for (PepperLink pepperLink : pepperLinks) {
            pepperDetails.add(new PepperDetailScraper(pepperLink, baseURL));
        }

        List<Future<PepperDetail>> futures = executor.invokeAll(pepperDetails);
        futures.stream().forEach(e -> {
            try {
                PepperDetail pepperDetail = e.get();
                foundDetails.add(pepperDetail);
            } catch (InterruptedException | ExecutionException ee) {
                ee.printStackTrace();
            }
        });


        executor.shutdown();
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }




}
