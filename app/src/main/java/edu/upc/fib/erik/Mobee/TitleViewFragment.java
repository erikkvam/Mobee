package edu.upc.fib.erik.Mobee;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

import java.util.List;


/**
 * A simple {@link ListFragment} subclass.
 */
public class TitleViewFragment extends ListFragment {
    private FilmData filmData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filmData = new FilmData(this.getContext());
        filmData.open();

        List<Film> values = filmData.getFilmTitles();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Film> adapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }
}
