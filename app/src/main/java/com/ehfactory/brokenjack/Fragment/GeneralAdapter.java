package com.ehfactory.brokenjack.Fragment;

import android.content.ContentResolver;
import android.support.v7.widget.RecyclerView;

import com.ehfactory.brokenjack.Music.MusicElements.MusicElement;
import com.ehfactory.brokenjack.Music.MusicGetter;
import com.ehfactory.brokenjack.Fragment.GeneralFragment.OnListFragmentInteractionListener;
import com.ehfactory.brokenjack.Music.MusicLoader;
import com.turingtechnologies.materialscrollbar.INameableAdapter;

import java.util.ArrayList;

/**
 * Crée par Erik H. (moi) le 04/04/2017 à 14:33.
 * Ce fichier doit probablement etre extremement cool.
 */

public abstract class GeneralAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> implements INameableAdapter {

    protected final ArrayList<Long> mValues;
    protected final OnListFragmentInteractionListener mListener;
    protected Class MUSIC_ELEMENT_TYPE;

    public <E extends MusicElement> GeneralAdapter(Class<E> element_type, ArrayList<Long> values, OnListFragmentInteractionListener lfi, ContentResolver ctRslvr) {

        MUSIC_ELEMENT_TYPE = element_type;

        if (values==null || values.size()==0){

            MusicLoader.initialize_by_type(MUSIC_ELEMENT_TYPE, ctRslvr);
            mValues = MusicGetter.extract_ids(MusicGetter.sort_by_names(MusicGetter.getAllDB(element_type)));

        } else {mValues = values;}

        mListener = lfi;
    }

    @Override
    public Character getCharacterForElement(int element) {
        return MusicGetter.get(MUSIC_ELEMENT_TYPE, mValues.get(element)).getName().charAt(0);
    }

    @Override
    public int getItemCount() {return mValues.size();}

}
