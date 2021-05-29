package com.rns.newsapplication.models;

import java.util.List;

public class Result {
    private final String sectionName;
    private final String webPublicationDate;
    private final String webTitle;
    private final String webUrl;
    private final Fields fields;
    private final List<Tag> tags;

    public Result(String sectionName, String webPublicationDate, String webTitle, String webUrl, Fields fields, List<Tag> tags) {
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.fields = fields;
        this.tags = tags;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public Fields getFields() {
        return fields;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public List<Tag> getTags() {
        return tags;
    }
}
