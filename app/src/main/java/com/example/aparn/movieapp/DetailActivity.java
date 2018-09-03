package com.example.aparn.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aparn.movieapp.model.movieDetails;
import com.example.aparn.movieapp.networkConn.networkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_INDEX = "extra_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError("No Intent");
        }

        Bundle data = getIntent().getExtras();
        if(data == null)
        {
            closeOnError("Not available.");
            return;
        }
        movieDetails movieItem = data.getParcelable("movieItem");
        if (movieItem == null) {
            closeOnError("Nothing to display");
            return;
        }
        populateUI(movieItem);
    }

    private void closeOnError(String msg) {
        finish();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(movieDetails movieItem) {
        TextView mv_OriginalTitle = findViewById(R.id.tv_original_title);
        TextView mv_Overview = findViewById(R.id.tv_synopsis);
        TextView mv_ReleaseDate = findViewById(R.id.tv_release_date);
        RatingBar mv_VoteAverage = findViewById(R.id.rbv_user_rating);
        ImageView mv_Poster = findViewById(R.id.iv_movie_poster);

        mv_OriginalTitle.setText(movieItem.getOriginalTitle());
        mv_Overview.setText(movieItem.getOverview());
        mv_ReleaseDate.setText(movieItem.getReleaseDate());
        mv_VoteAverage.setRating((float) movieItem.getVoteAverage());

        String posterPathURL = networkUtils.makePosterUrl(movieItem.getPosterPath());
        try {
            Picasso.with(this)
                    .load(posterPathURL)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(mv_Poster);
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
        setTitle(movieItem.getOriginalTitle());
    }
}

