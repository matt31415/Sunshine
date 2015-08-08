package com.example.android.sunshine.app;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * {@link ForecastAdapter} exposes a list of weather forecasts 
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}. 
 */
public class ForecastAdapter extends CursorAdapter {
    private final String LOG_TAG = ForecastAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_COUNT = 2;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /**
     * Copy/paste note: Replace existing newView() method in ForecastAdapter with this one.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        if(viewType == VIEW_TYPE_TODAY) {
            layoutId = R.layout.list_item_forecast_today;
        }
        else {
            layoutId = R.layout.list_item_forecast;
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        view.setTag(new ViewHolder(view));

        return view;
    }

    /* 
        This is where we fill-in the views with the contents of the cursor. 
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

         // Set the date
        long date = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        String formattedDate = Utility.getFriendlyDayString(view.getContext(), date);
        viewHolder.dateView.setText(formattedDate);

        // Set the forecast
        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        viewHolder.descriptionView.setText(description);

        // Set the temperatures
        boolean isMetric = Utility.isMetric(view.getContext());

        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        String formattedHigh = Utility.formatTemperature(view.getContext(), high, isMetric);
        viewHolder.maxTempView.setText(formattedHigh);

        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        String formattedLow = Utility.formatTemperature(view.getContext(), low, isMetric);
        viewHolder.minTempView.setText(formattedLow);

        //Set the icons
        int conditionId = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);
        int viewType = getItemViewType(cursor.getPosition());
        if(viewType == VIEW_TYPE_TODAY) {
            viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(conditionId));
        }
        else {
            viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(conditionId));

        }
    }
} 