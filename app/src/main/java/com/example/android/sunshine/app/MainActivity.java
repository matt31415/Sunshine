package com.example.android.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private String mLocation;
    private final String FORECASTFRAGMENT_TAG = "FFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocation = Utility.getPreferredLocation(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment(), FORECASTFRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String location = Utility.getPreferredLocation(this);
        if(location != null && !location.equals(mLocation)) {
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
            ff.onLocationChanged();
            mLocation = location;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
        if (id == R.id.action_view_location) {
            viewLocation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void viewLocation() {
        // First create a URI for our location (this relies on the locaiton preference being a postal code)
        String location = Utility.getPreferredLocation(this);

        //This feels ugly...  Shouldn't I be able to build a URI more elegantly than this?
        Uri mapUri = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q",location)
                .build();

        //Next set up an intent with this as the destination
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        mapIntent.setData(mapUri);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
        else {
            Toast.makeText(this,R.string.main_toast_no_maps,Toast.LENGTH_SHORT).show();
        }

    }
}
