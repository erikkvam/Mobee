package edu.upc.fib.erik.Mobee;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<Film> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, countryLabel, country, year, directorLabel, director, protagonistLabel, protagonist, rateLabel, rate;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            country = (TextView) view.findViewById(R.id.country);
            director = (TextView) view.findViewById(R.id.director);
            directorLabel = (TextView) view.findViewById(R.id.directorLabel);
            year = (TextView) view.findViewById(R.id.year);
            protagonist = (TextView) view.findViewById(R.id.protagonist);
            protagonistLabel = (TextView) view.findViewById(R.id.protagonistLabel);
            rate = (TextView) view.findViewById(R.id.rate);
            rateLabel = (TextView) view.findViewById(R.id.rateLabel);
        }
    }

    public MoviesAdapter(List<Film> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Film movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.country.setText(movie.getCountry());
        holder.year.setText(String.valueOf(movie.getYear()));
        holder.director.setText(movie.getDirector());
        holder.protagonist.setText(movie.getProtagonist());
        holder.rate.setText(String.valueOf(movie.getCritics_rate()));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}