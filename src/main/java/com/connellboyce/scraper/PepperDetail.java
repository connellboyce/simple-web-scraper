package com.connellboyce.scraper;

import java.util.StringJoiner;

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