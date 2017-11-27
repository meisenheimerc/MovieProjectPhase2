package com.example.android.movieprojectphase2.adapters;

import android.os.Parcel;
import android.os.Parcelable;



public class Trailer implements Parcelable {
    private String mVideoId;
    private String mName;

    public Trailer(String videoId, String name) {
        mVideoId = videoId;
        mName = name;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mVideoId);
        dest.writeString(mName);
    }

    protected Trailer(Parcel in) {
        mVideoId = in.readString();
        mName = in.readString();
    }


    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
