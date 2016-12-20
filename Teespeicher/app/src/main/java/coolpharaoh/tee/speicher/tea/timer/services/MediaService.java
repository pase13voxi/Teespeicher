package coolpharaoh.tee.speicher.tea.timer.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import java.io.IOException;

import coolpharaoh.tee.speicher.tea.timer.views.MainActivity;

/**
 * Created by CoolPharaoh on 10.02.2016.
 */
public class MediaService extends Service {
    MediaPlayer mediaPlayer = null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Musikstück initialisieren
        if(MainActivity.settings.getMusicChoice()!=null) {
            mediaPlayer = new MediaPlayer();
            Uri uri = Uri.parse(MainActivity.settings.getMusicChoice());
            try {
                //synchronisiere Musikstreams
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mediaPlayer.setDataSource(getApplicationContext(), uri);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //mediaPlayer = MediaPlayer.create(this, uri);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mediaPlayer != null)
            mediaPlayer.start();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null)
            mediaPlayer.release();
    }
}
