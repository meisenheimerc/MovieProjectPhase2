package com.example.android.movieprojectphase2.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.movieprojectphase2.R;
import com.example.android.movieprojectphase2.adapters.Trailer;
import com.example.android.movieprojectphase2.adapters.TrailerAdapter;
import com.example.android.movieprojectphase2.data.Movie;
import com.example.android.movieprojectphase2.data.MovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.movieprojectphase2.data.MovieContract.BASE_CONTENT_URI;
import static com.example.android.movieprojectphase2.data.MovieContract.PATH_MOVIES;


public class DetailActivity extends AppCompatActivity {
    private static final String PARCEL_KEY_MAIN = "PARCEL_KEY_MAIN";
    private static final String PARCEL_KEY_FAVORITE = "parcel_key_favorites";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w342/";
    private ImageView mPosterPath;
    private TextView mMovieTitle;
    private TextView mReleaseYear;
    private TextView mPlot;
    private TextView mVoteAverage;
    private Movie mMovieData;
    private int mMovieId;
    private static List<Trailer> mTrailers = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private static Trailer mTrailer;
    private FloatingActionButton mFavoriteButton;
    private boolean mFavoriteStatus; // Sets the movie favorite to false initially

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mPosterPath = (ImageView) findViewById(R.id.poster_path);
        mMovieTitle = (TextView) findViewById(R.id.tv_title);
        mReleaseYear = (TextView) findViewById(R.id.tv_release);
        mPlot = (TextView) findViewById(R.id.tv_overview);
        mVoteAverage = (TextView) findViewById(R.id.tv_vote);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_trailer);
        mFavoriteButton = (FloatingActionButton) findViewById(R.id.favoriteButton);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mTrailerAdapter = new TrailerAdapter(mTrailers, getApplicationContext()); // New trailer adapter with list of movies

        mMovieData = getIntent().getParcelableExtra(PARCEL_KEY_MAIN);

        if (mMovieData == null) { // If the movie data is null, then we came from the favorite movies and we need to request the details
            mMovieId = getIntent().getIntExtra(PARCEL_KEY_FAVORITE, 0); // Store a new movieId from FavoritesActivity
            mMovieData = new Movie(mMovieId); // Make a new movie object with the passed in movieId
            loadData(buildFavoriteMovieDetailsUrl(mMovieData.getMovieId()));
            loadData(buildRequestTrailerUrl(mMovieData.getMovieId()));
        } else {
            loadData(buildRequestTrailerUrl(mMovieData.getMovieId())); // We already have the movie details from Main, just need to get trailers
            setDetails();
        }

        mFavoriteStatus = isMovieFavorite(); // Checks to see if the current movie is a favorite movie

        if (mFavoriteStatus) {
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }
    /** Sets the details of the movie on start up **/
    private void setDetails() {
        mMovieTitle.setText(mMovieData.getTitle());
        mReleaseYear.setText(mMovieData.getReleaseDate());
        mPlot.setText(mMovieData.getOverview());
        mVoteAverage.setText(Double.toString(mMovieData.getVoteAverage()));
        mMovieId = mMovieData.getMovieId(); // Gets the MovieID to pass to the String Request to get trailers

        Picasso.with(this)
                .load(BASE_IMAGE_URL + mMovieData.getPosterPath())
                .into(mPosterPath);
    }

    /** Queries if the movie is a favorite or not on start up **/
    private boolean isMovieFavorite() {
        Cursor movieToQuery = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, // Checks to see if movie already exists in db
                null,
                "movieId=?",
                new String[] {Integer.toString(mMovieId)},
                null);

        return movieToQuery != null && movieToQuery.getCount() > 0;
    }

    /** This method toggles saving and deleting a movie from favorites **/
    public void saveMovie(View view) {
        ContentValues contentValues = new ContentValues();

        if (!mFavoriteStatus) { // If the movie isn't favorite status, insert this movie
            mFavoriteStatus = true;
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_black_24dp);
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, mMovieData.getTitle());
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE, BASE_IMAGE_URL + mMovieData.getPosterPath());
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovieId);
            getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
            Toast.makeText(this, mMovieData.getTitle() + " was starred", Toast.LENGTH_LONG).show();
        } else { // Delete this movie
            mFavoriteStatus = false;
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            Uri uri = BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_MOVIES)
                    .appendPath(Integer.toString(mMovieId))
                    .build();

            getContentResolver().delete(uri,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + "movieId=?",
                    new String[] {Integer.toString(mMovieId)});

            Toast.makeText(this, mMovieData.getTitle() + " was deleted", Toast.LENGTH_LONG).show();
        }
    }

    public void goToReviews(View view) {
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra("movieId", mMovieId);
        startActivity(intent);
    }
    /** This method loads the details and trailers for the specific movie via the passed in movieId from the MainActivity **/
    public void loadData(String url) {
        StringRequest trailerRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    buildTrailerList(response);
                    setDetails();
                    mRecyclerView.setAdapter(mTrailerAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        StringRequest favoriteRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    getFavoriteMovieDetails(response);
                    setDetails();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(trailerRequest);
        requestQueue.add(favoriteRequest);
    }
    /** This method builds the trailer URL for the Volley Request**/
    private String buildRequestTrailerUrl(int movieId) {
        String BASE_URL = "https://api.themoviedb.org/3/movie";
        Uri movieTrailerUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(Integer.toString(movieId))
                .appendPath("videos")
                .appendQueryParameter("api_key", MainActivity.API_KEY)
                .build();

        String finalUrl = movieTrailerUri.toString();
        return finalUrl;
    }

    private String buildFavoriteMovieDetailsUrl(int movieId) {
        String BASE_URL = "https://api.themoviedb.org/3/movie";
        Uri favoriteMovieUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(Integer.toString(movieId))
                .appendQueryParameter("api_key", MainActivity.API_KEY)
                .build();
        String finalUrl = favoriteMovieUri.toString();
        return finalUrl;
    }

    /** This method parses the JSON response and gets the videoId and the name of the trailer **/
    private static void buildTrailerList(String response) throws JSONException {
        mTrailers.clear(); // Clears the list of trailers
        JSONObject jsonObject = new JSONObject(response);
        JSONArray results = jsonObject.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject objects = results.getJSONObject(i);
            mTrailer = new Trailer(
                    objects.getString("key"), // Get the videoId
                    objects.getString("name")
            );
            mTrailers.add(mTrailer); // Store the trailer videoId in the list of trailers
        }
    }
    /** This method parses the JSON response of the movie using the movieID and makes a new movie with all the details **/
    private Movie getFavoriteMovieDetails(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        for (int i = 0; i < jsonObject.length(); i++) {
            mMovieData = new Movie(
                    jsonObject.getString("original_title"),
                    jsonObject.getString("poster_path"),
                    jsonObject.getString("overview"),
                    jsonObject.getDouble("vote_average"),
                    jsonObject.getString("release_date"),
                    jsonObject.getInt("id")
            );
        }
        return mMovieData;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the VisualizerActivity
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("bundleKey", mMovieData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMovieData = savedInstanceState.getParcelable("bundleKey");
    }
}
