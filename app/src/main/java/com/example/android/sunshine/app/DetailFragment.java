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
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final int WEATHER_DETAIL_LOADER_ID = 1;
    static final String DATE_URI_KEY = "DateUri";

    static final String[] DETAIL_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE
    };

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_MAX_TEMP = 2;
    static final int COL_WEATHER_MIN_TEMP = 3;
    static final int COL_WEATHER_CONDITION_ID = 4;
    static final int COL_WEATHER_DESC = 5;
    static final int COL_WEATHER_HUMIDITY = 6;
    static final int COL_WEATHER_DEGREES = 7;
    static final int COL_WEATHER_WIND_SPEED = 8;
    static final int COL_WEATHER_PRESSURE = 9;


    final static String SUNSHINE_HASHTAG = "#SunshineApp";
    private String mForecastStr;
    private Uri mUri;

    ShareActionProvider mShareActionProvider;

    //Store all of the views so we don't constantly have to search for them
    private TextView mDayTextView;
    private TextView mDateTextView;
    private TextView mMaxTempTextView;
    private TextView mMinTempTextView;
    private ImageView mIconImageView;
    private TextView mDescriptionTextView;
    private TextView mHumidityTextView;
    private TextView mWindTextView;
    private TextView mPressureTextView;


    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    public static DetailFragment newInstance(Uri dateUri) {
        DetailFragment detailFrag = new DetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(DATE_URI_KEY, dateUri);
        detailFrag.setArguments(args);

        return detailFrag;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle fragArgs = getArguments();
        if (fragArgs != null) {
            mUri = fragArgs.getParcelable(DATE_URI_KEY);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mDayTextView = (TextView) rootView.findViewById(R.id.detail_day_textview);
        mDateTextView = (TextView) rootView.findViewById(R.id.detail_date_textview);
        mMaxTempTextView = (TextView) rootView.findViewById(R.id.detail_temp_max_textview);
        mMinTempTextView = (TextView) rootView.findViewById(R.id.detail_temp_min_textview);
        mIconImageView = (ImageView) rootView.findViewById(R.id.detail_icon);
        mDescriptionTextView = (TextView) rootView.findViewById(R.id.detail_description_textview);
        mHumidityTextView = (TextView) rootView.findViewById(R.id.detail_humidity_textview);
        mWindTextView = (TextView) rootView.findViewById(R.id.detail_wind_textview);
        mPressureTextView= (TextView) rootView.findViewById(R.id.detail_pressure_textview);

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

    public void onLocationChanged() {
        Uri uri = mUri;
        String newLocation = Utility.getPreferredLocation(getActivity());

        if (uri != null) {
            //Figure out the date we're currently looking at
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updateUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
            mUri = updateUri;
            getLoaderManager().restartLoader(WEATHER_DETAIL_LOADER_ID, null, this);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case WEATHER_DETAIL_LOADER_ID :
                if(mUri == null) {
                    return null;
                }
                return new CursorLoader(
                        getActivity(),
                        mUri,
                        DETAIL_COLUMNS,
                        null,
                        null,
                        null
                );

            default:
                Log.e(LOG_TAG, "Unrecognized loaderId value");
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();

        String highAndLow = Utility.formatHighLows(data.getDouble(COL_WEATHER_MAX_TEMP),
                data.getDouble(COL_WEATHER_MIN_TEMP), getActivity());

        mForecastStr =  Utility.formatDate(data.getLong(COL_WEATHER_DATE)) +
                " - " + data.getString(COL_WEATHER_DESC) +
                " - " + highAndLow;

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(getShareIntent());
        }

        // Populate date fields
        String day = Utility.getDayName(getActivity(), data.getLong(COL_WEATHER_DATE));
        mDayTextView.setText(day);

        String date = Utility.getFormattedMonthDay(getActivity(), data.getLong(COL_WEATHER_DATE));
        mDateTextView.setText(date);

        //Populate temperatute fields
        boolean isMetric = Utility.isMetric(getActivity());

        double maxTemp = data.getDouble(COL_WEATHER_MAX_TEMP);
        mMaxTempTextView.setText(Utility.formatTemperature(getActivity(), maxTemp, isMetric));

        double minTemp = data.getDouble(COL_WEATHER_MIN_TEMP);
        mMinTempTextView.setText(Utility.formatTemperature(getActivity(), minTemp, isMetric));

        //Populate description and icon
        int conditionId = data.getInt(COL_WEATHER_CONDITION_ID);
        mIconImageView.setImageResource(Utility.getArtResourceForWeatherCondition(conditionId));

        String description = data.getString(COL_WEATHER_DESC);
        mDescriptionTextView.setText(description);

        //Populate humidity, wind, and pressure fields
        double humidity = data.getDouble(COL_WEATHER_HUMIDITY);
        mHumidityTextView.setText(getActivity().getString(R.string.format_humidity, humidity));

        float windSpeed = data.getFloat(COL_WEATHER_WIND_SPEED);
        float windDegrees = data.getFloat(COL_WEATHER_DEGREES);
        mWindTextView.setText(Utility.getFormattedWind(getActivity(), windSpeed, windDegrees));

        double pressure = data.getDouble(COL_WEATHER_PRESSURE);
        mPressureTextView.setText(getActivity().getString(R.string.format_pressure, pressure));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
