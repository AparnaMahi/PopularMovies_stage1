package com.example.aparn.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;


// derived code from site - http://www.vogella.com/tutorials/AndroidParcelable/article.html
// basic knowledge of using Parcelable got from udacity's example app
public class movieDetails  implements Parcelable
{
    public final String originalTitle;
    public final String overview;
    public final String posterPath;
    public final String releaseDate;
    public final double voteAverage;

    public movieDetails(String originalTitle, String overview,
                        String posterPath, String releaseDate, double voteAverage) {
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    private movieDetails(Parcel in) {
        originalTitle = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
    }

    public static final Creator<movieDetails> CREATOR = new Creator<movieDetails>() {
        @Override
        public movieDetails createFromParcel(Parcel in) {
            return new movieDetails(in);
        }

        @Override
        public movieDetails[] newArray(int size) {
            return new movieDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getOverview() {
        return overview;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
    }
}
