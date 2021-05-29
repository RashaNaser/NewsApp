package com.rns.newsapplication.utils;

import android.text.TextUtils;
import android.util.Log;

import com.rns.newsapplication.models.Fields;
import com.rns.newsapplication.models.Result;
import com.rns.newsapplication.models.Tag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news data from Guardian.
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Guardian data set and return a list of {@link Result} objects.
     */
    public static List<Result> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e); //NON-NLS
        }

        // Extract relevant fields from the JSON response and create a list of {@link Result}s
        // Return the list of {@link Result}s
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e); //NON-NLS
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(Constants.READ_TIME_OUT /* milliseconds */);
            urlConnection.setConnectTimeout(Constants.CONNECT_TIME_OUT /* milliseconds */);
            urlConnection.setRequestMethod(Constants.REQUEST_METHOD_GET);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode()); //NON-NLS
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e); //NON-NLS NON-NLS
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8")); //NON-NLS
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Result} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Result> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news
        List<Result> results = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Extract the JSONObject associated with the key called "response"
            JSONObject responseJsonObject = baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE);

            // Extract the JSONArray associated with the key called "results"
            JSONArray resultsArray = responseJsonObject.getJSONArray(Constants.JSON_KEY_RESULTS);

            // For each element in the resultsArray, create an {@link Result} object
            for (int i = 0; i < resultsArray.length(); i++) {

                // Get a single result at position i within the list of results
                JSONObject currentResult = resultsArray.getJSONObject(i);

                // For a given results, extract the value for the key called "sectionName"
                String sectionName = currentResult.getString(Constants.JSON_KEY_SECTION_NAME);

                // For a given results, extract the value for the key called "webPublicationDate"
                String webPublicationDate = currentResult.getString(Constants.JSON_KEY_WEB_PUBLICATION_DATE);

                // For a given results, extract the value for the key called "webTitle"
                String webTitle = currentResult.getString(Constants.JSON_KEY_WEB_TITLE);

                // For a given results, extract the value for the key called "webUrl"
                String webUrl = currentResult.getString(Constants.JSON_KEY_WEB_URL);

                // For a given results, extract JSONObject for the key called "fields"
                Fields fields;
                JSONObject fieldsObject = currentResult.getJSONObject(Constants.JSON_KEY_FIELDS);
                // Extract the value for the key called "thumbnail"
                String thumbnail = fieldsObject.getString(Constants.JSON_KEY_THUMBNAIL);
                fields = new Fields(thumbnail);

                // For a given news, extract JSONArray for the key called "tags"
                List<Tag> tags = new ArrayList<>();
                // Extract the JSONArray associated with the key called "tags"
                JSONArray tagsArray = currentResult.getJSONArray(Constants.JSON_KEY_TAGS);
                // Extract the first JSONObject in the tagsArray
                JSONObject firstTagsItem = tagsArray.getJSONObject(0);
                // Extract the value for the key called "webTitle"
                String author = firstTagsItem.getString(Constants.JSON_KEY_WEB_TITLE);
                tags.add(new Tag(author));

                // Create a new {@link Result} object with the sectionName, webPublicationDate, webTitle,
                // fields and tags from the JSON response.
                Result result = new Result(sectionName, webPublicationDate, webTitle, webUrl, fields, tags);

                // Add the new {@link Result} to the list of results.
                results.add(result);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e); //NON-NLS NON-NLS
        }

        // Return the list of results
        return results;
    }
}