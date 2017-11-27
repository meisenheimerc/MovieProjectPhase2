package com.example.android.movieprojectphase2.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.movieprojectphase2.R;
import com.example.android.movieprojectphase2.adapters.FavoriteAdapter;
import com.example.android.movieprojectphase2.data.MovieContract;



public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = FavoritesActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private FavoriteAdapter mFavoriteAdapter;
    private static final int MOVIE_LOADER_ID = 93;
    private static String LAST_POSITION_KEY = "lastPositionKey";
    private static int mLastFirstVisiblePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            mLastFirstVisiblePosition = savedInstanceState.getInt(LAST_POSITION_KEY);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mFavoriteAdapter = new FavoriteAdapter(this);
        mRecyclerView.setAdapter(mFavoriteAdapter);

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.MovieEntry._ID);
                } catch (Exception e) {
                    Log.e(TAG, "Failed to load movie data");
                    e.printStackTrace();
                    return null;
                }
            }
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFavoriteAdapter.swapCursor(data);
        ((GridLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(mLastFirstVisiblePosition, 0);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavoriteAdapter.swapCursor(null);
        ((GridLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(mLastFirstVisiblePosition, 0);
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
