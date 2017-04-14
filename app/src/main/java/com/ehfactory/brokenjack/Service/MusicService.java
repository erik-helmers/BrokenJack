package com.ehfactory.brokenjack.Service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ehfactory.brokenjack.MainActivity;
import com.ehfactory.brokenjack.Music.MusicControl;
import com.ehfactory.brokenjack.R;


public class MusicService extends Service{

    private final IBinder musicBind = new MusicBinder();
    final public static int NOTIF_ID = 3949;

    final public static String PAUSE_ACTION = "com.ehfactory.brokenjack.Service.PAUSE_ACTION";



    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy(){
        stopForeground(true);
    }

    public void removeNotif(){stopForeground(true);}

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }



    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void updateNotification(){
        //TODO: update notif
    }

    public void createNotification(){

        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent onClickIntent = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent pauseIntent = new Intent(this, NotifReceiver.class);
        pauseIntent.setAction(PAUSE_ACTION);
        PendingIntent pendingPauseIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);


        builder.setContentIntent(onClickIntent)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true)
                .setContentTitle(MusicControl.getActualSong().getName())
                .setContentText(String.valueOf(MusicControl.getActualSong().getArtist_id()))
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(0L);


        if (Build.VERSION.SDK_INT==19){
            addAction19(builder, pendingPauseIntent);
        } else {
            addAction20(builder, pendingPauseIntent);
            Log.i("AddAction", "createNotification: ");
        }




        Notification not = builder.build();

        startForeground(NOTIF_ID, not);
    }

    @TargetApi(19)
    @SuppressWarnings("deprecated")
    public void addAction19(Notification.Builder builder, PendingIntent pause){
        builder.addAction(android.R.drawable.ic_media_pause, "Pause", pause);
    }
    @TargetApi(23)
    public void addAction20(Notification.Builder builder, PendingIntent pause){
        Notification.Action pauseAction = new Notification.Action.Builder(Icon.createWithResource(this, android.R.drawable.ic_media_pause), "Pause", pause).build();
        builder.addAction(pauseAction);
        builder.setStyle(new Notification.MediaStyle().setShowActionsInCompactView(0));
        builder.setColor(getResources().getColor(R.color.primary_material_dark_1));
    }

}
