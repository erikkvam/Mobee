package edu.upc.fib.erik.Mobee;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;


/**
 * A simple {@link ListFragment} subclass.
 */
public class ActorSearchFragment extends ListFragment {
    private SearchView searchView;

    public ActorSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchView = new SearchView(this.getContext());
        searchView.setOnSearchClickListener(new View.OnClickListener() {

            private FilmData filmData;
            @Override
            public void onClick(View v) {
                SearchView searchTextView = (SearchView) v;
                String query = searchTextView.getQuery().toString();

                filmData = new FilmData(getContext());
                filmData.open();

                List<Film> values = filmData.searchByActor(query);

                ArrayAdapter<Film> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_list_item_1, values);
                setListAdapter(adapter);
            }
        });
        searchView.setIconified(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_actor_search, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        getActivity().setTitle(getContext().getString(R.string.search_by_actor_fragment_name));
        super.onResume();
    }
}
