package com.connellboyce.scraper.scrapers;

import com.connellboyce.scraper.PepperDetail;
import com.connellboyce.scraper.PepperLink;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.concurrent.Callable;

public class PepperDetailScraper implements Callable<PepperDetail> {
    private PepperLink pepperLink;
    private String baseURL;

    public PepperDetailScraper(PepperLink pepperLink, String baseURL) {
        this.pepperLink = pepperLink;
        this.baseURL = baseURL;
    }

    public PepperDetail findDetails() {
        try {
            //Connect to the URL stored in each pepperLink
            Document doc = Jsoup.connect(pepperLink.getHref()).get();

            System.out.println("Processing " + doc.title());

            //Navigate to the first element with a CSS id of "content"
            Element content = doc.select("#content").first();
            if (content == null) {
                System.out.println("-----------------------------------Bailing on content");
                return null;
            }

            //Navigate to the first element with a CSS class of "t_scheda" inside content
            Element table = content.select(".t_scheda").first();
            if (table == null) {
                System.out.println("++++++++++++++++++++++++++++++++++++Bailing on table");
                return null;
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
            return pepperDetail;

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public PepperDetail call() throws Exception {
        return findDetails();
    }
}
