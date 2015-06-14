package com.example.android.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<String> weekForecast = new ArrayList<String>();
        weekForecast.add("Today - Sunny - 88/75");
        weekForecast.add("Tomorrow - Partly Cloudy - 76/68");
        weekForecast.add("Tuesday - Partly Cloudy - 81/70");
        weekForecast.add("Wednesday - Partly Cloudy - 80/72");
        weekForecast.add("Thursday - Cloudy - 75/64");
        weekForecast.add("Friday - Rainy - 75/73");
        weekForecast.add("Today - Sunny - 88/75");
        weekForecast.add("Tomorrow - Partly Cloudy - 76/68");
        weekForecast.add("Tuesday - Partly Cloudy - 81/70");
        weekForecast.add("Wednesday - Partly Cloudy - 80/72");
        weekForecast.add("Thursday - Cloudy - 75/64");
        weekForecast.add("Friday - Rainy - 75/73");

        ArrayAdapter<String> forecastAdapter =
                new ArrayAdapter<String>(
                        //Context
                        getActivity(),
                        // List item view id
                        R.layout.list_item_forecast,
                        //Textview to fill
                        R.id.list_item_forecast_textview,
                        //List of strings to put into textview
                        weekForecast);

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ListView forecastListView = (ListView) view.findViewById(R.id.listview_forecast);
        forecastListView.setAdapter(forecastAdapter);

        return view;
    }
}
