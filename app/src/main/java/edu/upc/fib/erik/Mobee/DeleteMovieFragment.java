package edu.upc.fib.erik.Mobee;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

import static java.lang.Math.abs;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeleteMovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeleteMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteMovieFragment extends ListFragment {

    private FilmData filmData;
    private ArrayAdapter<Film> adapter;
    private List<Film> moviesList;
    public static Deque<Film> toDelete;
    private Deque<Integer> toDeleteInt;
    private FloatingActionButton fabUndo;

    private int SW_MIN_DISTANCE;
    private int SW_MAX_OFF_PATH;
    private int SW_THRESHOLD_VELOCITY;


    private OnFragmentInteractionListener mListener;

    public DeleteMovieFragment() {
        // Required empty public constructor
    }

    public static DeleteMovieFragment newInstance() {
        return new DeleteMovieFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        SW_MIN_DISTANCE = (int) (120.0f * dm.densityDpi / 160.0f + 0.5);
        SW_MAX_OFF_PATH = (int) (250.0f * dm.densityDpi / 160.0f + 0.5);
        SW_THRESHOLD_VELOCITY = (int) (200.0f * dm.densityDpi / 160.0f + 0.5);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View out = inflater.inflate(R.layout.fragment_delete_movie, container, false);
        filmData = ((MainView)getActivity()).getFilmData();
        moviesList = filmData.getAllFilms();
        Collections.sort(moviesList, new Comparator<Film>() {
            @Override
            public int compare(Film f1, Film f2) {
                return (f1.getTitle()).compareTo(f2.getTitle());
            }
        });
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, moviesList);
        setListAdapter(adapter);
        toDeleteInt = new ArrayDeque<>();
        toDelete = new ArrayDeque<>();
        return out;

    }

    public void deleteItem(int position) {
        toDelete.add(moviesList.get(position));
        toDeleteInt.add(position);
        moviesList.remove(position);
        adapter.notifyDataSetChanged();
        Snackbar snackBar = Snackbar.make(getView(), "Movie successfully deleted.", Snackbar.LENGTH_LONG);
        fabUndo.setVisibility(View.VISIBLE);
        MainView.dbMod = true;
        TextView textView = (TextView) snackBar.getView().findViewById(android.support.design.R.id.snackbar_text);
        int cText = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        textView.setTextColor(cText);
        snackBar.show();
    }

    public void onItemClickListener(int position) {
        if (position >= 0) Toast.makeText(getActivity(),"Swipe to delete", Toast.LENGTH_SHORT).show();
    }

    public ListView getListView() {
        return super.getListView();
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private int tmp_pos = -1;
        ListView listView = getListView();

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            int pos = listView.pointToPosition((int) e.getX(), (int) e.getY());
            onItemClickListener(pos);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            tmp_pos = listView.pointToPosition((int) e.getX(), (int) e.getY());
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (abs(e1.getY() - e2.getY()) > SW_MAX_OFF_PATH) return false;
            if (abs(e1.getX() - e2.getX()) > SW_MIN_DISTANCE && abs(velocityX) > SW_THRESHOLD_VELOCITY) {
                int pos = listView.pointToPosition((int) e1.getX(), (int) e2.getY());
                if (pos >= 0 && tmp_pos == pos)
                    deleteItem(pos);
            }
            return false;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        getActivity().setTitle(getContext().getString(R.string.deleteMovie));
        toDelete.clear();
        toDeleteInt.clear();
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fabUndo = (FloatingActionButton) getActivity().findViewById(R.id.fabUndo);
        fabUndo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Snackbar snackBarU = Snackbar.make(getView(), "Movie is restored!", Snackbar.LENGTH_SHORT);
                TextView textView = (TextView) snackBarU.getView().findViewById(android.support.design.R.id.snackbar_text);
                int cText = ContextCompat.getColor(getContext(), R.color.colorPrimary);
                textView.setTextColor(cText);
                snackBarU.show();
                moviesList.add(toDeleteInt.pollLast(),toDelete.pollLast());
                adapter.notifyDataSetChanged();
                if (toDelete.size() == 0) {MainView.dbMod = false; fabUndo.setVisibility(View.GONE);}
            }
        });
        final GestureDetector gestureDetector = new GestureDetector(getContext(), new MyGestureDetector());
        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        ListView listView = super.getListView();
        try {listView.setOnTouchListener(gestureListener);}
        catch (Exception e) {System.out.println("ListView not set exception");}
        super.onResume();
    }

    @Override
    public void onPause() {
        for (int i = 0; i < toDelete.size(); i++) filmData.deleteFilm(toDelete.poll());
        super.onPause();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
