package edu.upc.fib.erik.Mobee;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FilmData filmData;
    private ArrayAdapter<Film> adapter;
    private ListView listView;
    Map<Long, Integer> mItemIdTopMap = new HashMap<>();
    List<Film> moviesList;;
    private static final int MOVE_DURATION = 150;
    private int SW_MIN_DISTANCE;
    private int SW_MAX_OFF_PATH;
    private int SW_THRESHOLD_VELOCITY;


    private OnFragmentInteractionListener mListener;

    public DeleteMovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeleteMovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteMovieFragment newInstance(String param1, String param2) {
        DeleteMovieFragment fragment = new DeleteMovieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        SW_MIN_DISTANCE = (int) (120.0f * dm.densityDpi / 160.0f + 0.5);
        SW_MAX_OFF_PATH = (int) (250.0f * dm.densityDpi / 160.0f + 0.5);
        SW_THRESHOLD_VELOCITY = (int) (200.0f * dm.densityDpi / 160.0f + 0.5);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View out = inflater.inflate(R.layout.fragment_delete_movie, container, false);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        filmData = MainView.getFilmData();
        moviesList = filmData.getAllFilms();
        Collections.sort(moviesList, new Comparator<Film>() {
            @Override
            public int compare(Film f1, Film f2) {
                return (f1.getTitle()).compareTo(f2.getTitle());
            }
        });
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, moviesList);
        setListAdapter(adapter);

        return out;

    }

    public void animateRemoval(int position) {
        listView = super.getListView();
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        for (int i = 0; i < listView.getChildCount(); ++i) {
            View child = listView.getChildAt(i);
            if (i != position) {
                int pos = firstVisiblePosition + i;
                long itemId = adapter.getItemId(pos);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }
        // Delete the item from the adapter

        filmData.deleteFilm(moviesList.get(position));
        moviesList.remove(position);
        adapter.notifyDataSetChanged();

        final ViewTreeObserver observer = listView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listView.getFirstVisiblePosition();
                for (int i = 0; i < listView.getChildCount(); ++i) {
                    final View child = listView.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = adapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop == null) {
                        int childHeight = child.getHeight() + listView.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                    }
                    int delta = startTop - top;
                    if (delta != 0) {
                        Runnable endA;
                        if (firstAnimation) endA = new Runnable() {
                            public void run() {
                                listView.setEnabled(true);
                            }
                        };
                        else endA = null;
                        final Runnable endAction = endA;
                        firstAnimation = false;
                        View view = getView();
                        view.animate().setDuration(MOVE_DURATION);
                        ObjectAnimator anim = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0, 0);
                        anim.setDuration(MOVE_DURATION);
                        anim.start();
                        if (endAction != null) {
                            anim.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    endAction.run();
                                }
                            });
                        }
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }

    public void getSwipeItem(int position) { animateRemoval(position); }

    public void onItemClickListener(int position) {
        if (position < 0) return;
        Toast.makeText(getActivity(),"Swipe to delete", Toast.LENGTH_SHORT).show();
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
                    getSwipeItem(pos);
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
        getActivity().setTitle("Delete movie");
        ListView listView = super.getListView();

        final GestureDetector gestureDetector = new GestureDetector(getContext(), new MyGestureDetector());

        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        try {listView.setOnTouchListener(gestureListener);}
        catch (Exception e) {System.out.println("ListView not set exception");}
        super.onResume();
    }

    @Override
    public void onPause() {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
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
