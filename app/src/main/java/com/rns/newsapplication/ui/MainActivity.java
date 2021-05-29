package com.rns.newsapplication.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.rns.newsapplication.NewsLoader;
import com.rns.newsapplication.R;
import com.rns.newsapplication.adapter.NewsAdapter;
import com.rns.newsapplication.databinding.ActivityMainBinding;
import com.rns.newsapplication.models.Result;
import com.rns.newsapplication.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Result>> {

    private NewsAdapter mAdapter;
    private List<Result> results;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.listView.setEmptyView(binding.emptyTV);

        results = new ArrayList<>();
        // Create a new adapter that takes an empty list of results as input
        mAdapter = new NewsAdapter(this, results);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        binding.listView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news.
        binding.listView.setOnItemClickListener((adapterView, view, position, l) -> {
            // Convert the String URL into a URI object (to pass into the Intent constructor)
            Uri earthquakeUri = Uri.parse(mAdapter.getItem(position).getWebUrl());

            // Send the intent to launch a new activity
            startActivity(new Intent(Intent.ACTION_VIEW, earthquakeUri));
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(Constants.NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            binding.loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            binding.emptyTV.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Result>> onCreateLoader(int i, Bundle bundle) {

        Uri baseUri = Uri.parse(Constants.NEWS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value
        uriBuilder.appendQueryParameter(Constants.QUERY_PARAM, Constants.QUERY);
        uriBuilder.appendQueryParameter(Constants.FORMAT_PARAM, Constants.FORMAT);
        uriBuilder.appendQueryParameter(Constants.TAGS_PARAM, Constants.TAGS);
        uriBuilder.appendQueryParameter(Constants.FROM_DATE_PARAM, Constants.FROM_DATE);
        uriBuilder.appendQueryParameter(Constants.SHOW_TAGS_PARAM, Constants.SHOW_TAGS);
        uriBuilder.appendQueryParameter(Constants.SHOW_FIELDS_PARAM, Constants.SHOW_FIELDS);
        uriBuilder.appendQueryParameter(Constants.ORDER_BY_PARAM, Constants.ORDER);
        uriBuilder.appendQueryParameter(Constants.API_KEY_PARAM, Constants.API_KEY);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Result>> loader, List<Result> results) {
        // Hide loading indicator because the data has been loaded
        binding.loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No news found."
        binding.emptyTV.setText(R.string.no_news);

        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list of {@link Result}, then add them to the adapter's
        if (results != null && !results.isEmpty()) {
            mAdapter.addAll(results);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Result>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}