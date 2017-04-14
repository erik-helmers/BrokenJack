package com.ehfactory.brokenjack.Fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ehfactory.brokenjack.R;
import com.ehfactory.brokenjack.Utils.Converter;
import com.turingtechnologies.materialscrollbar.AlphabetIndicator;
import com.turingtechnologies.materialscrollbar.DragScrollBar;

import java.util.ArrayList;

/**
 * Crée par Erik H. (moi) le 04/04/2017 à 12:14.
 * Ce fichier doit probablement etre extremement cool.
 */

public abstract class GeneralFragment extends Fragment {

    protected OnListFragmentInteractionListener mListener;


    protected RecyclerView recyclerView;
    protected ProgressBar progressBar;
    protected DragScrollBar scrollBar;

    public static final String VALUES_LIST = "values_list";
    public static final String NEED_PRELOAD = "need_preload";

    protected ArrayList<Long> mValues;
    protected boolean need_preload;

    protected boolean viewCreated = false;
    protected boolean contentloaded = false;
    protected boolean needLoad = false;

    protected ContentResolver contentResolver = null;


    public static Bundle getBundle(ArrayList<Long> ids, boolean preload){
        Bundle bundle = new Bundle();
        bundle.putLongArray(VALUES_LIST, Converter.toPrimitiv(ids));
        bundle.putBoolean(NEED_PRELOAD, preload);
        return bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mValues = Converter.toArray(getArguments().getLongArray(VALUES_LIST));
            need_preload = getArguments().getBoolean(NEED_PRELOAD);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener)
            mListener = (OnListFragmentInteractionListener) context;
        else throw new RuntimeException(context.toString()
                        + " must implement the FragmentInteractionListener");
    }

    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, int view_id, int recycler_id, boolean linear) {

        View view = inflater.inflate(view_id, container, false);
        Context context = view.getContext();

        if (contentResolver==null) contentResolver = context.getContentResolver();

        recyclerView = (RecyclerView) view.findViewById(recycler_id);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);


        //TODO: upgraded scrollbar without the coordinator bug
        scrollBar = (DragScrollBar) view.findViewById(R.id.dragScrollBar);
        scrollBar.setDraggableFromAnywhere(true);
        scrollBar.setIndicator(new AlphabetIndicator(context), true);


        if (linear) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        }

        viewCreated = true;
        if (needLoad || need_preload) preLoad();
        return view;
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if ( !contentloaded && isVisibleToUser)
            if (viewCreated) preLoad();
            else needLoad=true;
    }

    public abstract void loadContent();

    @Override
    public void onDestroyView() {
        viewCreated = false;
        contentloaded = false;
        super.onDestroyView();
    }

    public void preLoad(){
        contentloaded = true;
        loadContent();
        progressBar.setVisibility(View.GONE);
    }



    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Class t, Long item);
    }
}
