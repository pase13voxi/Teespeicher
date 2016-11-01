package coolpharaoh.tee.speicher.tea.timer;

import android.app.Service;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;

/**
 * Created by paseb on 11.02.2016.
 */
public class CountDownService extends Service {

    public static final String COUNTDOWN_BR = "paseb.teeapp.teespeicher.countdown_br";
    Intent BroadcaseIntent = new Intent(COUNTDOWN_BR);

    CountDownTimer countDownTimer = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String elementAt = intent.getStringExtra("elementAt");
        long millis = intent.getLongExtra("millisec",0);
        countDownTimer = new CountDownTimer(millis, 250) {
            @Override
            public void onTick(long millisUntilFinished) {
                BroadcaseIntent.putExtra("countdown", millisUntilFinished);
                sendBroadcast(BroadcaseIntent);
            }

            @Override
            public void onFinish() {
                BroadcaseIntent.putExtra("ready", true);
                sendBroadcast(BroadcaseIntent);
                //ausführen wenn die Vibration aktiviert ist
                if(MainActivity.settings.isVibration()) {
                    Vibrator vibrator = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 1000 milliseconds
                    long[] twice = { 0, 500, 400, 500 };
                    vibrator.vibrate(twice,-1);
                }
                //ausführen wenn die Notification aktiviert ist
                if(MainActivity.settings.isNotification()){
                    Intent intent = new Intent(getBaseContext(), ShowTea.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                    // Build notification
                    Notification notification = new Notification.Builder(getApplicationContext())
                                .setTicker(getResources().getString(R.string.notification_ticker))
                                .setContentTitle(getResources().getString(R.string.notification_title))
                                .setContentText(elementAt)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentIntent(pIntent)
                                .setAutoCancel(true)
                                .build();
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    // hide the notification after its selected

                    notificationManager.notify(0, notification);
                }
                startService(new Intent(getBaseContext(),MediaService.class));
            }
        };
        countDownTimer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
