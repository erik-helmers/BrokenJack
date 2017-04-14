package com.ehfactory.brokenjack.Fragment.Artists;

import android.content.ContentResolver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ehfactory.brokenjack.Fragment.GeneralAdapter;
import com.ehfactory.brokenjack.Fragment.GeneralFragment.OnListFragmentInteractionListener;
import com.ehfactory.brokenjack.Music.MusicElements.Artist;
import com.ehfactory.brokenjack.Music.MusicGetter;
import com.ehfactory.brokenjack.R;

import java.util.ArrayList;
import java.util.List;


public class MyArtistRecyclerViewAdapter extends GeneralAdapter<MyArtistRecyclerViewAdapter.ViewHolder> {



    public MyArtistRecyclerViewAdapter(ArrayList<Long> items, OnListFragmentInteractionListener lfi, ContentResolver ctntRslvr) {
        super(MusicGetter.ARTIST_TYPE, items, lfi, ctntRslvr);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Artist artist = MusicGetter.get(MusicGetter.ARTIST_TYPE, mValues.get(position));

        holder.mIdView.setText(String.valueOf(artist.getId()));
        holder.mContentView.setText(artist.getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(MUSIC_ELEMENT_TYPE, holder.mItem);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Long mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
