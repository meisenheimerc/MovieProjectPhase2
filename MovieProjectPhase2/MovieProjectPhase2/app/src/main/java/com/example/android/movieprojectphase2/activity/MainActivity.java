package com.example.android.movieprojectphase2.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.movieprojectphase2.R;
import com.example.android.movieprojectphase2.adapters.MovieAdapter;
import com.example.android.movieprojectphase2.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String API_KEY = "";
    private static final String POPULAR_URL = "http://api.themoviedb.org/3/movie/popular/?api_key=" + API_KEY;
    private static final String HIGHEST_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated/?api_key=" + API_KEY;
    private List<Movie> mMovies = new ArrayList<>();
    private Movie mMovie;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;
    private SharedPreferences mSharedPreferences;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private static String LAST_POSITION_KEY = "lastPositionKey";
    private static int mLastFirstVisiblePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mLastFirstVisiblePosition = savedInstanceState.getInt(LAST_POSITION_KEY);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MovieAdapter(mMovies, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        mSharedPreferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);

        if (isNetworkAvailable()) {
            if (mSharedPreferences.getBoolean(getString(R.string.show_popular_key), true)) {
                loadData(POPULAR_URL);
            } else if (mSharedPreferences.getBoolean(getString(R.string.saved_highest_rated), true)) {
                loadData(HIGHEST_RATED_URL);
            } else loadData(POPULAR_URL);
        }
    }

    /**
     * This method loads the Movie data in the main UI thread using Volley
     **/
    public void loadData(String url) {
        StringRequest popularRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonParse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        StringRequest topRatedRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonParse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(popularRequest);
        requestQueue.add(topRatedRequest);
    }

    public void jsonParse(String response) throws JSONException {
        mMovies.clear(); // Clears the list of movies
        JSONObject jsonObject = new JSONObject(response);
        JSONArray results = jsonObject.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject objects = results.getJSONObject(i);
            mMovie = new Movie(
                    objects.getString("title"),
                    objects.getString("poster_path"),
                    objects.getString("overview"),
                    objects.getDouble("vote_average"),
                    objects.getString("release_date"),
                    objects.getInt("id")
            );
            mMovies.add(mMovie);
        }
        mAdapter.replaceData(mMovies); // Reloads movie data into adapter
        ((GridLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(mLastFirstVisiblePosition, 0); // Restore scrolls position
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.favorites:
                Intent favoriteIntent = new Intent(this, FavoritesActivity.class);
                startActivity(favoriteIntent);
                return true;
            case R.id.most_popular:
                Intent popularIntent = new Intent(this, SettingsFragment.class);
                startActivity(popularIntent);
                return true;
            case R.id.highest_rating:
                Intent ratingIntent = new Intent(this, SettingsFragment.class);
                startActivity(ratingIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method checks for internet connectivty before requesting Movies from API
     **/
    private boolean isNetworkAvailable() {
        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return mNetworkInfo != null && mNetworkInfo.isConnected();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        mLastFirstVisiblePosition = ((GridLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(LAST_POSITION_KEY, mLastFirstVisiblePosition);
        super.onSaveInstanceState(outState);
    }
}
