package com.rns.newsapplication.utils;

import com.rns.newsapplication.R;

public class Constants {

    /**
     * Read and Connect timeout for setting up the HTTP request
     */
    public static final int READ_TIME_OUT = R.dimen.READ_TIME_OUT;
    public static final int CONNECT_TIME_OUT = R.dimen.CONNECT_TIME_OUT;

    /**
     * Request method type "GET"
     */
    public static final String REQUEST_METHOD_GET = "GET"; //NON-NLS

    /**
     * https://content.guardianapis.com/search?q=slave&format=json&tag=film/film,tone/reviews&from-date=2010-01-01&show-tags=contributor&show-fields=starRating,headline,thumbnail,short-url&order-by=relevance&api-key=test
     */
    public static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search"; //NON-NLS

    /**
     * Parameters
     */
    public static final String QUERY_PARAM = "q"; //NON-NLS
    public static final String FORMAT_PARAM = "format"; //NON-NLS
    public static final String TAGS_PARAM = "tag"; //NON-NLS
    public static final String FROM_DATE_PARAM = "from-date"; //NON-NLS
    public static final String SHOW_TAGS_PARAM = "show-tags"; //NON-NLS
    public static final String ORDER_BY_PARAM = "order-by"; //NON-NLS
    public static final String API_KEY_PARAM = "api-key"; //NON-NLS //NON-NLS
    public static final String SHOW_FIELDS_PARAM = "show-fields"; //NON-NLS

    /**
     * The show fields we want our API to return //NON-NLS
     */
    public static final String FORMAT = "json"; //NON-NLS
    public static final String QUERY = "slave"; //NON-NLS
    public static final String ORDER = "relevance"; //NON-NLS //NON-NLS
    public static final String TAGS = "film/film,tone/reviews"; //NON-NLS
    public static final String SHOW_TAGS = "contributor"; //NON-NLS
    public static final String FROM_DATE = "2010-01-01"; //NON-NLS
    public static final String SHOW_FIELDS = "starRating,headline,thumbnail,short-url"; //NON-NLS
    public static final String API_KEY = "test"; //NON-NLS

    /**
     * Extract the key associated with the JSONObject
     */
    static final String JSON_KEY_RESPONSE = "response"; //NON-NLS
    static final String JSON_KEY_RESULTS = "results"; //NON-NLS
    static final String JSON_KEY_WEB_TITLE = "webTitle"; //NON-NLS //NON-NLS
    static final String JSON_KEY_SECTION_NAME = "sectionName"; //NON-NLS
    static final String JSON_KEY_WEB_PUBLICATION_DATE = "webPublicationDate"; //NON-NLS
    static final String JSON_KEY_WEB_URL = "webUrl"; //NON-NLS //NON-NLS //NON-NLS
    static final String JSON_KEY_FIELDS = "fields"; //NON-NLS
    static final String JSON_KEY_THUMBNAIL = "thumbnail"; //NON-NLS //NON-NLS
    static final String JSON_KEY_TAGS = "tags"; //NON-NLS

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    public static final int NEWS_LOADER_ID = 1;
}
