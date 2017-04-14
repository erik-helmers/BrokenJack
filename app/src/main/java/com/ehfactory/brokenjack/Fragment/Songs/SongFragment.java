package com.ehfactory.brokenjack.Fragment.Songs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ehfactory.brokenjack.Fragment.GeneralFragment;
import com.ehfactory.brokenjack.R;
import com.ehfactory.brokenjack.Utils.Converter;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SongFragment extends GeneralFragment {



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SongFragment() {
    }


    public static SongFragment newInstance(ArrayList<Long> ids, boolean preload) {
        SongFragment fragment = new SongFragment();
        Bundle args = getBundle(ids, preload);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void loadContent() {
        recyclerView.setAdapter(new MySongRecyclerViewAdapter(mValues, mListener, contentResolver));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return createView(inflater, container, savedInstanceState,
                R.layout.fragment_song_list, R.id.song_recycler_view, true);
    }


}
