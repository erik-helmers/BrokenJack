package com.ehfactory.brokenjack.Widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ehfactory.brokenjack.Activitys.NowPlaying;
import com.ehfactory.brokenjack.Music.MusicControl;
import com.ehfactory.brokenjack.Music.MusicGetter;
import com.ehfactory.brokenjack.Music.Player;
import com.ehfactory.brokenjack.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Crée par Erik H. (moi) le 18/02/2017 à 20:06.
 * Ce fichier doit probablement etre extremement cool.
 */

public class MusicControllerWidget extends RelativeLayout {

    final static String TAG = "MusicController";

    private EventBus eventBus = EventBus.getDefault();

    private ImageButton mPreviousButton;
    private ImageButton mNextButton;
    private ImageButton mPlayPauseButton;

    private ProgressBar progressBar;

    private TextView mSongText;

    private View dependency;
    private int dependency_id;
    private View parent;

    private int widgetY = 0;

    public MusicControllerWidget(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public MusicControllerWidget(Context context, AttributeSet attrs){
        super(context, attrs);
        initializeViews(context, attrs);
    }


    public MusicControllerWidget(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.music_controller, this);

        setAttrsParameters(attrs);
    }

    private void setAttrsParameters(AttributeSet attrs){
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MusicControllerWidget);

        if (!a.hasValue(R.styleable.MusicControllerWidget_extends_on))
            throw (new RuntimeException("Must have an extends_on attribute"));
        else dependency_id = a.getResourceId(R.styleable.MusicControllerWidget_extends_on, -1);


        a.recycle();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // Handle Button click

        getRootView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), NowPlaying.class));
            }
        });
        mPlayPauseButton = (ImageButton) findViewById(R.id.play_pause_button);
        mPlayPauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {MusicControl.toogle_playing();
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicControl.next_track();
            }
        });

        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {MusicControl.previous_track();}
        });

        mSongText = (TextView) findViewById(R.id.song_title);
        mSongText.setSelected(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);



        MusicControl.add_listener(new MusicControl.OnStatChangedListener() {
            @Override
            public void onStatChanged(MusicControl.PlaybackStatus status) {
                try {
                    mSongText.setText(MusicControl.getActualSong().getName());
                } catch (NullPointerException e) { int i =0;}
                finally {
                    mPlayPauseButton.setImageResource(status.isPlaying() ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
                    setVisibility(!status.isFirstSong());
                }

            }
        });


    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void setVisibility(boolean visible){

        Log.i(TAG, "setVisibility: "+visible+" "+getHeight());

        if (visible)
        {
            dependency.setPadding(dependency.getPaddingLeft(),
                    dependency.getPaddingTop(),
                    dependency.getPaddingRight(),
                    100);
            this.setVisibility(VISIBLE);
        } else {
            this.setVisibility(GONE);
            dependency.setPadding(dependency.getPaddingLeft(),
                    dependency.getPaddingTop(),
                    dependency.getPaddingRight(),
                    0);
        }


    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow(){
        super.onAttachedToWindow();

        dependency = getRootView().findViewById(dependency_id);
        parent = getRootView();

        eventBus.register(this);
        MusicControl.call_listeners();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrackProgressUpdate(Player.TrackProgressUpdate t){
        if (progressBar.getMax() != t.max){
            progressBar.setMax(t.max);
        }
        progressBar.setProgress(t.pos);
    }

}

