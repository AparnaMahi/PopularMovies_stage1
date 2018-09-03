package com.example.aparn.movieapp.utils;

import android.util.Log;

import com.example.aparn.movieapp.model.movieDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class fetchJSONData {

    private static final String TAG = fetchJSONData.class.getSimpleName();

    public static ArrayList<movieDetails> parseMovieJson(String json) {
        try {

            movieDetails movie;
            JSONObject object = new JSONObject(json);

            JSONArray resultsArray = new JSONArray(object.optString("results",
                    "[\"\"]"));

            ArrayList<movieDetails> items = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                String current = resultsArray.optString(i, "");

                JSONObject movieJson = new JSONObject(current);

                String overview = movieJson.optString("overview", "Not Available");
                String original_title = movieJson.optString("original_title",
                        "Not Available");
                String poster_path = movieJson
                        .optString("poster_path", "Not Available");
                String release_date = movieJson.optString("release_date",
                        "Not Available");
                String vote_average = movieJson.optString("vote_average", "Not Available");
                movie = new movieDetails(original_title, overview, poster_path, release_date, Double.parseDouble(vote_average));
                items.add(movie);
            }
            return items;

        } catch (Exception ex) {
            return null;
        }
    }
}
