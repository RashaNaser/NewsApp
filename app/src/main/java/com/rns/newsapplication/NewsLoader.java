package com.rns.newsapplication;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.rns.newsapplication.models.Result;
import com.rns.newsapplication.utils.QueryUtils;

import java.util.List;

/**
 * Loads a list of news by using an AsyncTask to perform the network request to the given URL.
 */
public class NewsLoader extends AsyncTaskLoader<List<Result>> {

    /**
     * Query URL
     */
    private final String mUrl;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        // Trigger the loadInBackground() method to execute.
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Result> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news.
        return QueryUtils.fetchNewsData(mUrl);
    }
}
