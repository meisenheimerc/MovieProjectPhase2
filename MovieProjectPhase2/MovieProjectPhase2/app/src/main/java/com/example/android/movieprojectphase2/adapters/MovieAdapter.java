package com.example.android.movieprojectphase2.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieprojectphase2.R;
import com.example.android.movieprojectphase2.activity.DetailActivity;
import com.example.android.movieprojectphase2.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;



public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private List<Movie> mMovies;
    private Context mContext;

    public MovieAdapter(List<Movie> movies, Context context) {
        mMovies = movies;
        mContext = context;
    }

    /**
     * This method reloads the adapter with new data
     **/
    public void replaceData(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mPosterPath;
        public View mMovieView;

        public ViewHolder(View itemView) {
            super(itemView);
            mPosterPath = (ImageView) itemView.findViewById(R.id.movie_poster);
            mMovieView = itemView;
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
        final Movie movie = mMovies.get(position);

        /** Allows click handling of each Movie and passes data to DetailActivity **/
        holder.mMovieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Movie movieParcel = movie;
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("PARCEL_KEY_MAIN", movieParcel);

                mContext.startActivity(intent);
            }
        });

        Picasso.with(mContext)
                .load(BASE_IMAGE_URL + movie.getPosterPath())
                .into(holder.mPosterPath);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.size();
    }
}
