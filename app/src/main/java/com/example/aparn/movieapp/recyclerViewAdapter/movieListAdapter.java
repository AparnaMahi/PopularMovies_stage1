package com.example.aparn.movieapp.recyclerViewAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.aparn.movieapp.model.movieDetails;
import com.example.aparn.movieapp.networkConn.networkUtils;
import com.example.aparn.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


// derived from https://developer.android.com/guide/topics/ui/layout/recyclerview

public class movieListAdapter extends RecyclerView.Adapter<movieListAdapter.MovieViewHolder> {
    final private ListItemClickListener movieOnClickListener;
    private static final String TAG = movieListAdapter.class.getSimpleName();
    private List<movieDetails> movieDetailsList;
    private final Context movieContext;

    public interface ListItemClickListener {
        void OnListItemClick(movieDetails movieItem);
    }

    public movieListAdapter(List<movieDetails> movieItemList, ListItemClickListener listener, Context context) {

        if (movieItemList == null ) {
            movieDetailsList = new ArrayList<>();
        }
        movieDetailsList = movieItemList;
        movieOnClickListener = listener;
        movieContext = context;
    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            Context context = parent.getContext();
            int id = R.layout.moviedetails;
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(id, parent, false);
            return new MovieViewHolder(view);
    }

    public void onBindViewHolder(@SuppressWarnings("NullableProblems") MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movieDetailsList == null ? 0 : movieDetailsList.size();
    }

    public void displayList(List<movieDetails> movieItemList) {
        movieDetailsList = movieItemList;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView listMovieItemView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            listMovieItemView = itemView.findViewById(R.id.iv_posteritems);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            movieDetails movieItem = movieDetailsList.get(listIndex);
            listMovieItemView = itemView.findViewById(R.id.iv_posteritems);
            String posterPathURL = networkUtils.makePosterUrl(movieItem.getPosterPath());
            try {
                Picasso.with(movieContext)
                        .load(posterPathURL)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(listMovieItemView);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            movieOnClickListener.OnListItemClick(movieDetailsList.get(clickedPosition));
        }
    }
}
