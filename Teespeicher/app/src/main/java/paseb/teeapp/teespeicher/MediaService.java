package paseb.teeapp.teespeicher;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

/**
 * Created by paseb on 10.02.2016.
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
            Uri uri = Uri.parse(MainActivity.settings.getMusicChoice());
            mediaPlayer = MediaPlayer.create(this, uri);
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
