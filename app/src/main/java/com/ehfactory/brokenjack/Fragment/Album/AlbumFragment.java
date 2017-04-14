package com.ehfactory.brokenjack.Fragment.Album;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ehfactory.brokenjack.Fragment.GeneralFragment;
import com.ehfactory.brokenjack.R;
import com.ehfactory.brokenjack.Utils.Converter;

import java.util.ArrayList;


public class AlbumFragment extends GeneralFragment {


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AlbumFragment() {
    }

    @SuppressWarnings("unused")
    public static AlbumFragment newInstance(ArrayList<Long> ids, boolean preload) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = getBundle(ids, preload);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return createView(inflater, container, savedInstanceState,
                R.layout.fragment_album_list, R.id.album_recycler_view, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void loadContent() {
        recyclerView.setAdapter(new MyAlbumRecyclerViewAdapter(mValues, mListener, contentResolver));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
