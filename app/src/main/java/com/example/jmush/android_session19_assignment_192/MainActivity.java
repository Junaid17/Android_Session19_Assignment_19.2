package com.example.jmush.android_session19_assignment_192;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "http://api.themoviedb.org/3/tv/top_rated?api_key=8496be0b2149805afa458ab8ec27560c";

    ArrayList<HashMap<String, String>> wheatherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wheatherList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            HttpHandler httpHandler = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = httpHandler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray weathers = jsonObj.getJSONArray("results");

                    // looping through All wheather
                    for (int i = 0; i < weathers.length(); i++) {
                        JSONObject c = weathers.getJSONObject(i);

                        String name = c.getString("name");
                        String vote_count = c.getString("vote_count");
                        String id = c.getString("id");


                        // tmp hash map for single wheather
                        HashMap<String, String> weather = new HashMap<>();

                        // adding each child node to HashMap key => value
                        weather.put("name", name);
                        weather.put("vote_count", vote_count);
                        weather.put("id", id);

                        // adding contact to contact list
                        wheatherList.add(weather);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, wheatherList,
                    R.layout.list_item, new String[]{"name", "vote_count",
                    "id"}, new int[]{R.id.name,
                    R.id.vote, R.id.id});

            lv.setAdapter(adapter);
        }

    }
}

