package com.connellboyce.scraper;

import java.util.StringJoiner;

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

