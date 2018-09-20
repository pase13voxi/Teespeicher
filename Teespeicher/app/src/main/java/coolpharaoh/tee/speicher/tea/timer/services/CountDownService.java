package coolpharaoh.tee.speicher.tea.timer.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;

import java.util.concurrent.TimeUnit;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.MainActivity;
import coolpharaoh.tee.speicher.tea.timer.views.ShowTea;

/**
 * Created by paseb on 11.02.2016.
 */
public class CountDownService extends Service {

    public static final String COUNTDOWN_BR = "paseb.teeapp.teespeicher.countdown_br";
    private static final String CHANNEL_ID_COUNTER = "3421";
    private static final String CHANNEL_ID_NOTIFY = "3422";
    Intent BroadcaseIntent = new Intent(COUNTDOWN_BR);

    CountDownTimer countDownTimer = null;
    private NotificationManager notificationManager_counter;
    private Notification.Builder notification_counter;

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
        //Should fix a Nullpointer-Error
        final String elementName;
        if(intent.hasExtra("elementName")) {
            elementName = intent.getStringExtra("elementName");
        }else {
            elementName = "-";
        }
        long millis = intent.getLongExtra("millisec",0);

        //Back to the Showtea Intent
        Intent intent_showtea = new Intent(getBaseContext(), ShowTea.class);
        intent_showtea.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent_showtea, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = getString(R.string.showtea_channel_name);
            String description = getString(R.string.showtea_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_COUNTER, name, importance);
            channel.setDescription(description);
            channel.setSound(null, null);
            //First time
            String ms = String.format("%02d : %02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            notification_counter = new Notification.Builder(getApplicationContext())
                    .setChannelId(CHANNEL_ID_COUNTER)
                    .setContentText(ms)
                    .setContentTitle(elementName)
                    .setSmallIcon(R.drawable.iconnotification)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pIntent);

            notificationManager_counter = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager_counter.createNotificationChannel(channel);
            notificationManager_counter.notify(1, notification_counter.build());
        }else {
            //First time
            String ms = String.format("%02d : %02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            notification_counter = new Notification.Builder(getApplicationContext())
                    .setContentText("TeeSpeicher")
                    .setContentTitle(ms)
                    .setSmallIcon(R.drawable.iconnotification)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pIntent);

            notificationManager_counter = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager_counter.notify(1, notification_counter.build());
        }

        countDownTimer = new CountDownTimer(millis, 250) {
            @Override
            public void onTick(long millisUntilFinished) {
                BroadcaseIntent.putExtra("countdown", millisUntilFinished);
                sendBroadcast(BroadcaseIntent);

                String ms = String.format("%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                //Second time
                notification_counter.setContentText(ms);
                notificationManager_counter.notify(1, notification_counter.build());
            }

            @Override
            public void onFinish() {
                notificationManager_counter.cancel(1);
                BroadcaseIntent.putExtra("ready", true);
                sendBroadcast(BroadcaseIntent);
                //ausführen wenn die Vibration aktiviert ist
                if(MainActivity.settings.isVibration()) {
                    Vibrator vibrator = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 1000 milliseconds
                    long[] twice = { 0, 500, 400, 500 };
                    if (vibrator==null){
                        throw new AssertionError("Vibrator is null.");
                    } else {
                        vibrator.vibrate(twice, -1);
                    }
                }
                //ausführen wenn die Notification aktiviert ist
                if(MainActivity.settings.isNotification()){
                    //Back to the Showtea Intent
                    Intent intent_showtea = new Intent(getBaseContext(), ShowTea.class);
                    intent_showtea.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent_showtea, 0);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String name = getString(R.string.showtea_channel_name);
                        String description = getString(R.string.showtea_channel_description);
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_NOTIFY, name, importance);
                        channel.setDescription(description);
                        channel.setSound(null, null);

                        // Build notification
                        Notification.Builder notification = new Notification.Builder(getApplicationContext())
                                .setChannelId(CHANNEL_ID_NOTIFY)
                                .setTicker(getResources().getString(R.string.notification_ticker))
                                .setContentTitle(getResources().getString(R.string.notification_title))
                                .setContentText(elementName)
                                .setSmallIcon(R.drawable.iconnotification)
                                .setContentIntent(pIntent)
                                .setAutoCancel(true);
                        // hide the notification after its selected

                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        if (notificationManager==null){
                            throw new AssertionError("NotificationManager is null.");
                        } else {
                            notificationManager.createNotificationChannel(channel);
                            notificationManager.notify(0, notification.build());
                        }
                    }else {
                        // Build notification
                        Notification.Builder notification = new Notification.Builder(getApplicationContext())
                                .setTicker(getResources().getString(R.string.notification_ticker))
                                .setContentTitle(getResources().getString(R.string.notification_title))
                                .setContentText(elementName)
                                .setSmallIcon(R.drawable.iconnotification)
                                .setContentIntent(pIntent)
                                .setAutoCancel(true);
                        // hide the notification after its selected

                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        if (notificationManager==null){
                            throw new AssertionError("NotificationManager is null.");
                        } else {
                            notificationManager.notify(0, notification.build());
                        }
                    }

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
