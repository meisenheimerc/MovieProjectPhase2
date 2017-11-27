package com.example.android.movieprojectphase2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieprojectphase2.R;

import java.util.List;



public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    private List<Review> mReviews;
    private Context mContext;

    public ReviewAdapter(List<Review> reviews, Context context) {
        mReviews = reviews;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextAuthor;
        public TextView mTextContent;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextAuthor = (TextView) itemView.findViewById(R.id.text_author);
            mTextContent = (TextView) itemView.findViewById(R.id.text_content);
        }

    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_reviews, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Review review = mReviews.get(position);

        holder.mTextAuthor.setText(review.getAuthor());
        holder.mTextContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) return 0;
        return mReviews.size();
    }
}
