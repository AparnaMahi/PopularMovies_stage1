package com.example.aparn.movieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.example.aparn.movieapp.networkConn.networkUtils;
import com.example.aparn.movieapp.model.movieDetails;
import com.example.aparn.movieapp.utils.fetchJSONData;
import com.example.aparn.movieapp.recyclerViewAdapter.movieListAdapter;

//how to use gridlayout,appbarlayout,toolbar derived from tutorials
public class MainActivity extends AppCompatActivity implements movieListAdapter.ListItemClickListener{

    private movieListAdapter movieDetAdapter;
    private RecyclerView movieListView;
    private static final String SORT_POPULAR = "popular";
    private static final String SORT_TOP_RATED = "top_rated";
    private static String currentSort = SORT_POPULAR;
    private static final String MOVIE_LIST_KEY = "MOVIE_LIST_KEY";
    private static final String CURRENT_SORT_KEY = "CURRENT_SORT_KEY";
    private ArrayList<movieDetails> movieItems;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        movieListView = (RecyclerView)findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        movieListView.setLayoutManager(layoutManager);
        movieListView.setHasFixedSize(true);
        movieDetAdapter = new movieListAdapter(movieItems, this, this);
        movieListView.setAdapter(movieDetAdapter);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_LIST_KEY)) {
            movieItems = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
            currentSort = savedInstanceState.getString(CURRENT_SORT_KEY, SORT_POPULAR);
        }
        displayMovieDetails();
    }
    private void displayMovieDetails() {
        if (movieItems == null || movieItems.isEmpty()) {
            new networkConnTask().execute(networkUtils.buildDataUrl(getText(R.string.api_key).toString(), currentSort));
        }else
            movieDetAdapter.displayList(movieItems);
    }

    public class networkConnTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String responseFromHttpUrl = null;
            try {
                responseFromHttpUrl = networkUtils.getResponseFromHttpUrl(searchUrl, getApplicationContext());

            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
            return responseFromHttpUrl;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            if (searchResults != null && !searchResults.equals("")) {
                movieItems = fetchJSONData.parseMovieJson(searchResults);
                movieDetAdapter.displayList(movieItems);
            } else {
                movieItems =new ArrayList<>();
                movieDetAdapter.displayList(movieItems);
                Toast.makeText(getApplicationContext(), " Network connection problem.Can't load movies..", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitems, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_most_popular && !currentSort.equals(SORT_POPULAR)) {
            resetMovieItems();
            currentSort = SORT_POPULAR;
            displayMovieDetails();
            return true;
        }
        if (id == R.id.action_sort_top_rated && !currentSort.equals(SORT_TOP_RATED)) {
            resetMovieItems();
            currentSort = SORT_TOP_RATED;
            displayMovieDetails();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentSort.equals(SORT_TOP_RATED)) {
            menu.findItem(R.id.action_sort_top_rated).setChecked(true);

        } else {
            menu.findItem(R.id.action_sort_most_popular).setChecked(true);
        }
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movieItems = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
        currentSort = savedInstanceState.getString(CURRENT_SORT_KEY);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_LIST_KEY, movieItems);
        outState.putString(CURRENT_SORT_KEY, currentSort);
    }

    @Override
    public void OnListItemClick(movieDetails movieItem) {
        Intent myIntent = new Intent(this, DetailActivity.class);
        myIntent.putExtra(DetailActivity.EXTRA_INDEX, 1);
        myIntent.putExtra("movieItem", movieItem);
        startActivity(myIntent);
    }
    private void resetMovieItems() {
        if (movieItems != null) {
            movieItems.clear();
        }
    }
}


