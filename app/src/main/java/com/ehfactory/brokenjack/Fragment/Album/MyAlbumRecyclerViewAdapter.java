package com.ehfactory.brokenjack.Fragment.Album;

import android.content.ContentResolver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ehfactory.brokenjack.Fragment.GeneralAdapter;
import com.ehfactory.brokenjack.Fragment.GeneralFragment.OnListFragmentInteractionListener;
import com.ehfactory.brokenjack.Music.MusicElements.Album;
import com.ehfactory.brokenjack.Music.MusicGetter;
import com.ehfactory.brokenjack.R;

import java.util.ArrayList;
import java.util.List;

public class MyAlbumRecyclerViewAdapter extends GeneralAdapter<MyAlbumRecyclerViewAdapter.ViewHolder> {


    public MyAlbumRecyclerViewAdapter(ArrayList<Long> values, OnListFragmentInteractionListener lfi, ContentResolver ctRslvr) {
        super(MusicGetter.ALBUM_TYPE, values, lfi, ctRslvr);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        Album album = MusicGetter.get(MusicGetter.ALBUM_TYPE, holder.mItem);
        holder.mIdView.setText(String.valueOf(album.getId()));
        holder.mContentView.setText(album.getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (null != mListener) {


                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(MusicGetter.ALBUM_TYPE, holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
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
