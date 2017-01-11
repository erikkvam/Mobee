package edu.upc.fib.erik.Mobee;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddMovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMovieFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FilmData filmData;
    private Button saveButton;
    private SeekBar rateBar;
    private TextView rate;
    private OnFragmentInteractionListener mListener;

    public AddMovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMovieFragment newInstance(String param1, String param2) {
        AddMovieFragment fragment = new AddMovieFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = (View) inflater.inflate(R.layout.fragment_add_movie, container, false);
        MainView act = (MainView) getActivity();
        FloatingActionButton fab = (FloatingActionButton) act.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        filmData = act.getFilmData();
        saveButton = (Button) mView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onButtonPressed();
            }
        });
        rateBar = (SeekBar) mView.findViewById(R.id.rateBar);
        rate = (TextView) mView.findViewById(R.id.rate);
        rateBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                rate.setText(String.valueOf(progress*10 / rateBar.getMax()));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                rate.setText(String.valueOf(progress*10 / rateBar.getMax()));
            }
        });
        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        MainView act = (MainView) getActivity();
        TextView title = (TextView) act.findViewById(R.id.title);
        if (title.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar
                    .make(getView(), "Please insert a title.", Snackbar.LENGTH_LONG);
            TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return;
        }
        TextView year = (TextView) act.findViewById(R.id.year);
        if (year.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar
                    .make(getView(), "Please insert a year.", Snackbar.LENGTH_LONG);
            TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return;
        }
        TextView director = (TextView) act.findViewById(R.id.director);
        if (director.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar
                    .make(getView(), "Please insert a director.", Snackbar.LENGTH_LONG);
            TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return;
        }
        TextView country = (TextView) act.findViewById(R.id.country);
        TextView protagonist = (TextView) act.findViewById(R.id.protagonist);
        rate = (TextView) act.findViewById(R.id.rate);
        filmData.createFilm(title.getText().toString(), director.getText().toString(), country.getText().toString(),
                Integer.valueOf(year.getText().toString()), protagonist.getText().toString(), Integer.valueOf(rate.getText().toString()));
        getActivity().getSupportFragmentManager().popBackStack();
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
        getActivity().setTitle("Add movie");

        super.onResume();
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
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
