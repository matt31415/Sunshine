package com.example.android.sunshine.app;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mgay on 7/23/2015.
 */
public class ViewHolder {

    public final ImageView iconView;
    public final TextView dateView;
    public final TextView descriptionView;
    public final TextView maxTempView;
    public final TextView minTempView;

    public ViewHolder (View view) {
        iconView = (ImageView) view.findViewById(R.id.list_item_icon);
        dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
        descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
        maxTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
        minTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
    }
}
