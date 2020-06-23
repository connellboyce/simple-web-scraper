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
    private String minSHU;
    private String maxSHU;
    private String imageURL;
    private String description;

    /**
     * Object Constructor
     *
     * @param name the pepper's most common name
     * @param altNames the pepper's alternative names
     * @param species the pepper's species within Capsicum
     * @param origin the pepper's place of origin
     * @param minSHU the pepper's minimum Scoville units
     * @param shuMax the pepper's maximum Scoville units
     * @param imageURL the pepper's image URL
     * @param description the pepper's description in english
     */
    public PepperDetail(String name, String altNames, String species, String origin, String minSHU, String shuMax, String imageURL, String description) {
        this.name = name;
        this.altNames = altNames;
        this.species = species;
        this.origin = origin;
        this.minSHU = minSHU;
        this.maxSHU = shuMax;
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

    public String getMinSHU() {
        return minSHU;
    }

    public void setMinSHU(String minSHU) {
        this.minSHU = minSHU;
    }

    public String getMaxSHU() {
        return maxSHU;
    }

    public void setMaxSHU(String maxSHU) {
        this.maxSHU = maxSHU;
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
        if (minSHU != null ? !minSHU.equals(that.minSHU) : that.minSHU != null) return false;
        if (maxSHU != null ? !maxSHU.equals(that.maxSHU) : that.maxSHU != null) return false;
        if (imageURL != null ? !imageURL.equals(that.imageURL) : that.imageURL != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (altNames != null ? altNames.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        result = 31 * result + (origin != null ? origin.hashCode() : 0);
        result = 31 * result + (minSHU != null ? minSHU.hashCode() : 0);
        result = 31 * result + (maxSHU != null ? maxSHU.hashCode() : 0);
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
                .add("shuMin='" + minSHU + "'")
                .add("shuMax='" + maxSHU + "'")
                .add("imageURL='" + imageURL + "'")
                .add("description='" + description + "'")
                .toString();
    }

}