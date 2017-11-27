package com.example.android.movieprojectphase2.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieprojectphase2.R;
import com.example.android.movieprojectphase2.activity.DetailActivity;
import com.example.android.movieprojectphase2.data.MovieContract;
import com.squareup.picasso.Picasso;



public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{
    private Cursor mCursor;
    private Context mContext;

    public FavoriteAdapter(Context context) {
        mContext = context;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mPosterPath;
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mPosterPath = (ImageView) itemView.findViewById(R.id.movie_poster);
            mView = itemView;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_movies, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry._ID);
        int photoIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE);
        int movieIdIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        String photoUrl = mCursor.getString(photoIndex); // Gets the movie poster for the Movie
        final int favoriteMovieId = Integer.parseInt(mCursor.getString(movieIdIndex)); // Gets the movie ID for the Movie

        holder.itemView.setTag(id);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putExtra("parcel_key_favorites", favoriteMovieId); // Passing the movieID to the detail activity when clicked
                mContext.startActivity(detailIntent);
            }
        });

        Picasso.with(mContext)
                .load(photoUrl)
                .into(holder.mPosterPath);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


}
