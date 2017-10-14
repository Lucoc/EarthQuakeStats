package edu.itu.csc.earthquakestats.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.itu.csc.earthquakestats.R;
import edu.itu.csc.earthquakestats.adapters.EarthQuakeInfoAdapter;
import edu.itu.csc.earthquakestats.pojos.EarthQuakeInfo;
import edu.itu.csc.earthquakestats.settings.SettingsActivity;
import edu.itu.csc.earthquakestats.trie.AutoCompleteTrie;
import edu.itu.csc.earthquakestats.utils.Utils;

/**
 * Search activity to display city name recommendations where there was an earthquake given filters using Trie data structure.
 *
 * @author "Jigar Gosalia"
 *
 */
public class SearchActivity extends AppCompatActivity {

    private static ArrayAdapter<EarthQuakeInfo> earthquakeAdapter = null;

    private static List<EarthQuakeInfo> earthQuakeDetails = new ArrayList<EarthQuakeInfo>();

    private static AutoCompleteTrie autoCompleteTrie = new AutoCompleteTrie();

    private TextView updateTime;

    private TextView updateFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#228B22")));

        updateTime = (TextView) findViewById(R.id.update_time);
        updateFilter = (TextView) findViewById(R.id.update_filter);

        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.pref_general, false);

        earthquakeAdapter = new EarthQuakeInfoAdapter(this, R.layout.list_item_earthquake, new ArrayList<EarthQuakeInfo>());

        // Get a reference to the ListView and attach this adapter to it.
        ListView listView = (ListView) findViewById(R.id.listview_earthquake);
        listView.setAdapter(earthquakeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (earthquakeAdapter != null
                        && earthquakeAdapter.getItem(i) != null) {
                    EarthQuakeInfo earthQuakeInfo = earthquakeAdapter.getItem(i);
                    Log.d(MainActivity.APP_TAG, "Item selected to view details : " + earthQuakeInfo);
                    Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
                    intent.putExtra("title", earthQuakeInfo.getTitle());
                    intent.putExtra("location", earthQuakeInfo.getFormattedPlace());
                    intent.putExtra("coordinates", earthQuakeInfo.getFormattedCoordinates());
                    intent.putExtra("time", earthQuakeInfo.getFormattedTime());
                    intent.putExtra("depth", earthQuakeInfo.getDepth());
                    intent.putExtra("eventid", earthQuakeInfo.getEventId());
                    intent.putExtra("significance", earthQuakeInfo.getSignificance());
                    intent.putExtra("status", earthQuakeInfo.getStatus());
                    intent.putExtra("url", earthQuakeInfo.getUrl());
                    startActivity(intent);
                }
            }
        });

        final EditText cityName = (EditText) findViewById(R.id.search_box);
        cityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                EditText cName = (EditText) findViewById(R.id.search_box);
                String prefix = cName.getText().toString();
                if (prefix != null && prefix.length() > 0) {
                    earthquakeAdapter.clear();
                    List<String> cityNames = autoCompleteTrie.predict(prefix.toLowerCase(), 10);
                    for (EarthQuakeInfo earthQuakeInfo : earthQuakeDetails) {
                        if (cityNames.contains(earthQuakeInfo.getFormattedPlace().toLowerCase())) {
                            earthquakeAdapter.add(earthQuakeInfo);
                        }
                    }
                } else {
                    earthquakeAdapter.clear();
                    earthquakeAdapter.addAll(earthQuakeDetails);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });
    }

    /**
     * AsyncTask to fetch latest quakes data from USGS.
     */
    public static class LatestDataFetchTask extends AsyncTask<String, Void, List<EarthQuakeInfo>> {

        private ProgressDialog dialog;
        private Context context;

        public LatestDataFetchTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Latest Quakes Data", "Collecting latest quakes data from USGS ... ", false);
            autoCompleteTrie = new AutoCompleteTrie();
        }

        @Override
        protected List<EarthQuakeInfo> doInBackground(String... data) {
            String url = Utils.urlType.get("today");
            String magnitude = "3.0";
            String duration = "last24hr";
            String distance = "miles";
            if (data != null
                    && data.length > 2) {
                return Utils.getEarthQuakeData("SearchActivity", Utils.urlType.get(data[1]), data[0], data[1], data[2]);
            }
            return Utils.getEarthQuakeData("SearchActivity", url, magnitude, duration, distance);
        }

        @Override
        protected void onPostExecute(List<EarthQuakeInfo> result) {
            super.onPostExecute(result);
            if (dialog != null
                    && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (result != null
                    && earthquakeAdapter != null) {
                earthquakeAdapter.clear();
                earthQuakeDetails.clear();
                if (result.size() > 0) {
                    for (EarthQuakeInfo earthquakeInfo : result) {
                        earthquakeAdapter.add(earthquakeInfo);
                        autoCompleteTrie.addWord(earthquakeInfo.getFormattedPlace());
                        earthQuakeDetails.add(earthquakeInfo);
                    }
                } else {
                    Toast.makeText(context, "No data found with given filters!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, "Couldn't fetch quakes data from USGS, check your internet connection and try again!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLatestQuakes(this);
        Utils.updateLastViewed(SearchActivity.class.getSimpleName(), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mini, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                loadLatestQuakes(this);
                break;
            case R.id.action_settings:
                startActivity(new Intent(SearchActivity.this, SettingsActivity.class));
                break;
            default:
                Toast.makeText(this, "No Such Action Supported!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * load quakes data.
     *
     * @param context
     */
    private void loadLatestQuakes(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String magnitude = prefs.getString(context.getString(R.string.pref_magnitude_key), null);
        String duration = prefs.getString(context.getString(R.string.pref_duration_key), null);
        String distance = prefs.getString(context.getString(R.string.pref_distance_key), null);
        new SearchActivity.LatestDataFetchTask(context).execute(magnitude, duration, distance);
        updateFooter();
    }

    /**
     * refresh footer with last updated time and user filters.
     *
     */
    private void updateFooter() {
        Date date = new Date();
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String magnitude = prefs.getString(this.getString(R.string.pref_magnitude_key), null);
        String duration = prefs.getString(this.getString(R.string.pref_duration_key), null);
        updateTime.setText(("Updated: " + timeFormatter.format(date)).toString());
        updateFilter.setText(("Filters: " + magnitude + "; " + Utils.durationMap.get(duration)).toString());
    }
}
