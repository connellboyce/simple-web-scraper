package com.connellboyce.scraper.scrapers;

import com.connellboyce.scraper.converters.PeppersToJSONConverter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class PepperScraper implements Runnable {

    public String chilePlanetURL = "http://www.chileplanet.eu";
    public String chilePlanetPage = "database.html";
    public List<PepperLink> pepperLinks = new ArrayList<>();
    public Set<PepperDetail> pepperDetails = new HashSet<>();

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
        findDetails();

        //Output to console
        //pepperDetails.stream().forEach(e -> System.out.println(e.toString()));

        PeppersToJSONConverter converter = new PeppersToJSONConverter();
        converter.convertToSystemOut(pepperDetails);
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
    public void findDetails() {

        //Base URL to precede the URL extensions from the images
        String baseURL = "http://www.chileplanet.eu/schede";

        //Iterate through the list of pepperLinks
        for (PepperLink pepperLink : pepperLinks) {
            try {
                //Connect to the URL stored in each pepperLink
                Document doc = Jsoup.connect(pepperLink.getHref()).get();

                System.out.println("Processing " + doc.title());

                //Navigate to the first element with a CSS id of "content"
                Element content = doc.select("#content").first();
                if (content == null) {
                    System.out.println("-----------------------------------Bailing on content");
                    continue;
                }

                //Navigate to the first element with a CSS class of "t_scheda" inside content
                Element table = content.select(".t_scheda").first();
                if (table == null) {
                    System.out.println("++++++++++++++++++++++++++++++++++++Bailing on table");
                    continue;
                }

                //Initialize a new pepperDetail object with the name of the pepperLink
                PepperDetail pepperDetail = new PepperDetail();
                pepperDetail.setName(pepperLink.getName());

                //Navigate to the first tbody element inside "t_scheda"
                Element tbody = table.selectFirst("tbody");

                //Navigate to the tr elements inside the first tbody
                Elements rows = tbody.select("tr");

                //Iterate through each row (tr)
                for (Element row : rows) {
                    //Access each cell
                    Elements cells = row.select("td");

                    //Most of the rows have 2 cells, but the first has 3 so it will be handled later
                    if (cells.size() == 2) {
                        String label = cells.get(0).text();
                        String value = cells.get(1).text();

                        if (StringUtils.equalsIgnoreCase(label, "origin:")) pepperDetail.setOrigin(value);
                        if (StringUtils.equalsIgnoreCase(label, "species:")) pepperDetail.setSpecies(StringUtils.replace(value,"C. ", "Capsicum "));
                        if (StringUtils.equalsIgnoreCase(label, "shu min.")) pepperDetail.setShuMin(value);
                        if (StringUtils.equalsIgnoreCase(label, "shu max.")) pepperDetail.setShuMax(value);
                    }

                    //The first row has an image and then 2 other cells, so it is handled as follows
                    if (cells.size() == 3) {
                        String image = cells.get(0).select("a").attr("href");
                        String label = cells.get(1).text();
                        String value = cells.get(2).text();

                        String imageUrl = StringUtils.join(new String[]{baseURL, image}, "/");
                        pepperDetail.setImageURL(StringUtils.replace(imageUrl," ", "%20"));
                        if (StringUtils.equalsIgnoreCase(label, "alternative names:")) pepperDetail.setAltNames(StringUtils.stripAccents(value));

                    }
                }

                //Going back to content, navigate to the first element with an id of "corpo2"
                Element corpo2 = content.select("#corpo2").first();

                //Navigate to the p elements within "corpo2"
                Elements pTags = corpo2.select("p");

                //Base case for no description
                pepperDetail.setDescription("No description");

                //Iterate through the p tags (different languages) and if one has an image with an alt of "en"
                //for english, set the description to the text in that p tag
                for (Element pTag : pTags) {
                    if (pTag.select("img").attr("alt").equalsIgnoreCase("en")) {
                        pepperDetail.setDescription(StringUtils.stripAccents(pTag.text()));
                    }
                }

                //Add each pepperDetail to the set
                pepperDetails.add(pepperDetail);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Pepper Link Object
     *
     * Stores the pepper name and the link to its more detailed webpage
     */
    public class PepperLink {

        private String name;
        private String href;

        /**
         * Constructor
         *
         * @param name name of pepper
         * @param href link to detailed page
         */
        public PepperLink(String name, String href) {
            this.name = name;
            this.href = href;
        }

        public String getName() {
            return name;
        }

        public String getHref() {
            return href;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setHref(String href) {
            this.href = href;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", PepperLink.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .add("href='" + href + "'")
                    .toString();
        }
    }

    /**
     * Pepper Details Object
     *
     * Contains all of the scraped information on the peppers found in the website's HTML
     */
    public class PepperDetail {

        private String name;
        private String altNames;
        private String species;
        private String origin;
        private String shuMin;
        private String shuMax;
        private String imageURL;
        private String description;

        /**
         * Object Constructor
         *
         * @param name the pepper's most common name
         * @param altNames the pepper's alternative names
         * @param species the pepper's species within Capsicum
         * @param origin the pepper's place of origin
         * @param shuMin the pepper's minimum Scoville units
         * @param shuMax the pepper's maximum Scoville units
         * @param imageURL the pepper's image URL
         * @param description the pepper's description in english
         */
        public PepperDetail(String name, String altNames, String species, String origin, String shuMin, String shuMax, String imageURL, String description) {
            this.name = name;
            this.altNames = altNames;
            this.species = species;
            this.origin = origin;
            this.shuMin = shuMin;
            this.shuMax = shuMax;
            this.imageURL = imageURL;
            this.description = description;
        }

        /**
         * Empty Constructor
         */
        public PepperDetail() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAltNames() {
            return altNames;
        }

        public void setAltNames(String altNames) {
            this.altNames = altNames;
        }

        public String getSpecies() {
            return species;
        }

        public void setSpecies(String species) {
            this.species = species;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getShuMin() {
            return shuMin;
        }

        public void setShuMin(String shuMin) {
            this.shuMin = shuMin;
        }

        public String getShuMax() {
            return shuMax;
        }

        public void setShuMax(String shuMax) {
            this.shuMax = shuMax;
        }

        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PepperDetail that = (PepperDetail) o;

            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (altNames != null ? !altNames.equals(that.altNames) : that.altNames != null) return false;
            if (species != null ? !species.equals(that.species) : that.species != null) return false;
            if (origin != null ? !origin.equals(that.origin) : that.origin != null) return false;
            if (shuMin != null ? !shuMin.equals(that.shuMin) : that.shuMin != null) return false;
            if (shuMax != null ? !shuMax.equals(that.shuMax) : that.shuMax != null) return false;
            if (imageURL != null ? !imageURL.equals(that.imageURL) : that.imageURL != null) return false;
            return description != null ? description.equals(that.description) : that.description == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (altNames != null ? altNames.hashCode() : 0);
            result = 31 * result + (species != null ? species.hashCode() : 0);
            result = 31 * result + (origin != null ? origin.hashCode() : 0);
            result = 31 * result + (shuMin != null ? shuMin.hashCode() : 0);
            result = 31 * result + (shuMax != null ? shuMax.hashCode() : 0);
            result = 31 * result + (imageURL != null ? imageURL.hashCode() : 0);
            result = 31 * result + (description != null ? description.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", PepperDetail.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .add("altNames='" + altNames + "'")
                    .add("species='" + species + "'")
                    .add("origin='" + origin + "'")
                    .add("shuMin='" + shuMin + "'")
                    .add("shuMax='" + shuMax + "'")
                    .add("imageURL='" + imageURL + "'")
                    .add("description='" + description + "'")
                    .toString();
        }

    }

}
