package com.ehfactory.brokenjack.Activitys.Album;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.ehfactory.brokenjack.Fragment.GeneralFragment;
import com.ehfactory.brokenjack.Fragment.Songs.SongFragment;
import com.ehfactory.brokenjack.MainActivity;
import com.ehfactory.brokenjack.Music.MusicControl;
import com.ehfactory.brokenjack.Music.MusicElements.Album;
import com.ehfactory.brokenjack.Music.MusicGetter;
import com.ehfactory.brokenjack.Music.MusicLoader;
import com.ehfactory.brokenjack.R;

public class AlbumActivity extends AppCompatActivity implements GeneralFragment.OnListFragmentInteractionListener {

    private Album album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initContent();
        setTitle(album.getName());
        setupFragment();

    }

    public void initContent(){
        Intent intent = getIntent();
        Long artist_id= intent.getLongExtra(MainActivity.ALBUM_EXTRA, 0L);
        MusicLoader.initialize_by_albums(getContentResolver(), artist_id);

        album = MusicGetter.get(MusicGetter.ALBUM_TYPE, artist_id);
    }

    public void setupFragment(){
        ViewGroup container = (ViewGroup) findViewById(R.id.fragment_container);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_container, SongFragment.newInstance(album.getSongs_ids(), true));
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Class t, Long item) {
        MusicControl.setSongList(album.getSongs_ids(), album.getSongs_ids().indexOf(item));

    }
}
