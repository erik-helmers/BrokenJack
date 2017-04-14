package com.ehfactory.brokenjack;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.ehfactory.brokenjack.Activitys.Album.AlbumActivity;
import com.ehfactory.brokenjack.Activitys.Artist.ArtistActivity;
import com.ehfactory.brokenjack.Activitys.Intro.Tutorial;
import com.ehfactory.brokenjack.Fragment.Album.AlbumFragment;
import com.ehfactory.brokenjack.Fragment.Artists.ArtistFragment;
import com.ehfactory.brokenjack.Fragment.GeneralFragment;
import com.ehfactory.brokenjack.Fragment.Songs.SongFragment;
import com.ehfactory.brokenjack.Music.MusicControl;
import com.ehfactory.brokenjack.Music.MusicGetter;
import com.ehfactory.brokenjack.Music.Player;
import com.ehfactory.brokenjack.Widget.MusicControllerWidget;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GeneralFragment.OnListFragmentInteractionListener{

    public static final String ARTIST_EXTRA = "artist_extra";
    public static final String ALBUM_EXTRA = "album_extra";
    private MusicControllerWidget msc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        checkReadPermissionOrFirstLaunch();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        msc = (MusicControllerWidget) findViewById(R.id.controller);
        Player.init(this);
        setupPager();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Player.start(this);
    }

    public void setupPager(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.add(ArtistFragment.newInstance(null, true), "Artists");
        adapter.add(new AlbumFragment(), "Alubms");
        adapter.add(new SongFragment(), "Songs");

        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

    }


    @Override
    public void onListFragmentInteraction(Class t, final Long item) {
        if (t== MusicGetter.ARTIST_TYPE){
            Intent intent = new Intent(this, ArtistActivity.class);
            intent.putExtra(ARTIST_EXTRA, item);
            startActivity(intent);
        }
        else if (t==MusicGetter.SONG_TYPE){
            MusicControl.setSongList(new ArrayList<Long>(){{add(item);}});
        }
        else if (t==MusicGetter.ALBUM_TYPE){
            Intent intent = new Intent(this, AlbumActivity.class);
            intent.putExtra(ALBUM_EXTRA, item);
            Log.i("AFI", "onListFragmentInteraction: "+item);
            startActivity(intent);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        Player.onScreenVisible();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Player.onScreenInvisible();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Player.destroy(this);
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void checkReadPermissionOrFirstLaunch(){
        //TODO: On First Launch Show

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            startActivity(new Intent(this, Tutorial.class));

        }
    }


    @Override
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.parameters) {
            // Handle the camera action
        } else if (id == R.id.feedback) {

        } else if (id == R.id.credits) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
