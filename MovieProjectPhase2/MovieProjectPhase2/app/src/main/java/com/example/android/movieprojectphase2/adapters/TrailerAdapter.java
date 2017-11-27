package com.example.android.movieprojectphase2.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieprojectphase2.R;
import com.squareup.picasso.Picasso;

import java.util.List;



public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private static final String BASE_URL = "https://www.youtube.com/watch?v=";
    private List<Trailer> mTrailers;
    private Context mContext;


    public TrailerAdapter(List<Trailer> trailers, Context context) {
        super();
        mTrailers = trailers;
        mContext = context;
    }

    /**
     * This method reloads the adapter with new data
     **/
    public void replaceData(List<Trailer> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mPlayImage;
        public TextView mTrailerName;
        public View mTrailerView;

        public ViewHolder(View itemView) {
            super(itemView);
            mPlayImage = (ImageView) itemView.findViewById(R.id.play_icon);
            mTrailerName = (TextView) itemView.findViewById(R.id.trailer_name);
            mTrailerView = itemView;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_trailers, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Trailer trailer = mTrailers.get(position);
        final String videoPath = BASE_URL + trailer.getVideoId();

        holder.mTrailerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoPath));
                mContext.startActivity(intent);
            }
        });

        Picasso.with(mContext)
                .load(R.drawable.ic_play_arrow_black_24dp)
                .into(holder.mPlayImage);

        holder.mTrailerName.setText(trailer.getName());

    }

    @Override
    public int getItemCount() {
        if (mTrailers == null) return 0;
        return mTrailers.size();
    }


}
