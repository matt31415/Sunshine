/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent openSettingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(openSettingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements
            LoaderManager.LoaderCallbacks<Cursor>{

        private final String LOG_TAG = PlaceholderFragment.class.getSimpleName();
        static final int WEATHER_DETAIL_LOADER_ID = 1;

        final static String SUNSHINE_HASHTAG = "#SunshineApp";
        private String mForecastStr;

        ShareActionProvider mShareActionProvider;

        public PlaceholderFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.detailfragment, menu);

            //Set up the share action
            MenuItem shareItem = menu.findItem(R.id
                    .action_share);
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

            if(mForecastStr != null) {
                mShareActionProvider.setShareIntent(getShareIntent());
            }

//            shareActionProvider.setShareIntent(getShareIntent());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(WEATHER_DETAIL_LOADER_ID, null,this);

            super.onActivityCreated(savedInstanceState);
        }

        Intent getShareIntent() {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + " " + SUNSHINE_HASHTAG);

            return shareIntent;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
            switch (loaderId) {
                case WEATHER_DETAIL_LOADER_ID :
                    Uri weatherForLocationAndDateUri = null;
                    Intent intent = getActivity().getIntent();

                    if(intent != null) {
                        weatherForLocationAndDateUri = intent.getData();
                    }
                    if (weatherForLocationAndDateUri != null) {
                        return new CursorLoader(
                                getActivity(),
                                weatherForLocationAndDateUri,
                                ForecastFragment.FORECAST_COLUMNS,
                                null,
                                null,
                                null
                        );
                    }

                default:
                    Log.e(LOG_TAG, "Unrecognized loaderId value");
                    return null;
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            data.moveToFirst();
            mForecastStr = Utility.convertCursorRowToUXFormat(data, getActivity());

            TextView forecastTextView = (TextView) getActivity().findViewById(R.id.forecastText);
            forecastTextView.setText(mForecastStr);

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(getShareIntent());
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}