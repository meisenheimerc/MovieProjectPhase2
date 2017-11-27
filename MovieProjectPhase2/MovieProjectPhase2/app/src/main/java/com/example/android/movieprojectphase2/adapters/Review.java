package com.example.android.movieprojectphase2.adapters;



public class Review {
    private String mAuthor;
    private String mContent;

    public Review (String author, String content) {
        mAuthor = author;
        mContent = content;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }
}
