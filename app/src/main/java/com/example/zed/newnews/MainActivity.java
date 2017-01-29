package com.example.zed.newnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Headline>>{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search?q=technology&api-key=cc5e2bf2-2e8d-4a04-88a1-9e86079d4c49";

    private static final int NEWS_LOADER_ID = 1;

    private TextView emptyView;

    private ListAdapter mAdapter;

    private String searchParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsList = (ListView) findViewById(R.id.list);

        mAdapter = new ListAdapter(this, new ArrayList<Headline>());

        newsList.setAdapter(mAdapter);

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        emptyView = (TextView) findViewById(R.id.empty_view);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Headline currentHeadline = mAdapter.getItem(i);

                Uri articleUri = Uri.parse(currentHeadline.getUrl());

                Intent bookSiteIntent = new Intent(Intent.ACTION_VIEW,articleUri);

                startActivity(bookSiteIntent);

            }
        });

        // Get a reference to the ConnectivityManager to check the state of the network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            emptyView.setVisibility(GONE);
            //Get a reference to the LoaderManager, in order to interact with leaders.
            Log.i(LOG_TAG, "connection OK!");

            // Initialize the loader.
            loaderManager.initLoader(NEWS_LOADER_ID, null, MainActivity.this);
        } else {
            // Update empty state with no connection error message
            emptyView.setText("No internet connection");
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<List<Headline>> onCreateLoader(int i, Bundle bundle) {

        return new NewsLoader(this, searchParams);
    }

    @Override
    public void onLoadFinished(Loader<List<Headline>> loader, List<Headline> headlines) {

        mAdapter.clear();

        // Update the ListView of {@link Book} objects
        Log.i(LOG_TAG, "onLoadFinished");
        if (headlines != null && !headlines.isEmpty()) {
            mAdapter.clear();
            mAdapter.addAll(headlines);
            emptyView.setText("Books Results");
            Log.i(LOG_TAG, "LOAD RESULTS");
        } else {
            emptyView.setText("No books in loader");
            emptyView.setVisibility(View.VISIBLE);
            Log.i(LOG_TAG, "NOTHING TO LOAD");
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Headline>> loader) {

    }
}
