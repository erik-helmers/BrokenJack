package com.ehfactory.brokenjack.Activitys.Artist;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.ehfactory.brokenjack.Activitys.Album.AlbumActivity;
import com.ehfactory.brokenjack.Fragment.Album.AlbumFragment;
import com.ehfactory.brokenjack.Fragment.GeneralFragment;
import com.ehfactory.brokenjack.Fragment.Songs.SongFragment;
import com.ehfactory.brokenjack.MainActivity;
import com.ehfactory.brokenjack.Music.MusicControl;
import com.ehfactory.brokenjack.Music.MusicElements.Artist;
import com.ehfactory.brokenjack.Music.MusicElements.Song;
import com.ehfactory.brokenjack.Music.MusicGetter;
import com.ehfactory.brokenjack.Music.MusicLoader;
import com.ehfactory.brokenjack.Music.Player;
import com.ehfactory.brokenjack.R;

import java.util.ArrayList;
import java.util.List;

public class ArtistActivity extends AppCompatActivity implements GeneralFragment.OnListFragmentInteractionListener {

    private Artist artist;
    private ArrayList<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initContent();
        setTitle(artist.getName());

        setupPager();

    }


    public void initContent(){
        Intent intent = getIntent();
        Long artist_id= intent.getLongExtra(MainActivity.ARTIST_EXTRA, 0L);
        MusicLoader.initialize_by_artist(getContentResolver(), artist_id);

        artist = MusicGetter.get(MusicGetter.ARTIST_TYPE, artist_id);
        songs = MusicGetter.sort_by_names(MusicGetter.get_all(MusicGetter.SONG_TYPE, artist.getSongs_id()));

    }
    public void setupPager(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.add(AlbumFragment.newInstance(artist.getAlbums_id(), true), "Alubms");
        adapter.add(SongFragment.newInstance(MusicGetter.extract_ids(songs), true), "Songs");


        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Class t, Long item) {
        if (t==MusicGetter.SONG_TYPE){
            MusicControl.setSongList(MusicGetter.extract_ids(songs), MusicGetter.extract_ids(songs).indexOf(item)); //TODO: efficient upgrade
        } else {
            Intent intent = new Intent(this, AlbumActivity.class);
            intent.putExtra(MainActivity.ALBUM_EXTRA, item);
            startActivity(intent);
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public void add(Fragment f, String t){mFragmentList.add(f); mTitleList.add(t);}

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

    }
}
