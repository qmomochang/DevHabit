package com.habit.devhabit;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Service class that manages notifications of the timer.
 */
public class NotificationService extends IntentService {

    public static final String TAG = "TimerNotificationSvc";
    static public String ACTION_SHOW_ALARM = "ACTION_SHOW_ALARM";
    static public String ACTION_DELETE_ALARM = "ACTION_DELETE_ALARM";
    static public String ACTION_RESTART_ALARM = "ACTION_RESTART_ALARM";

    public int NOTIFICATION_TIMER_COUNTDOWN = 100;
    public int NOTIFICATION_TIMER_EXPIRED = 101;

    public NotificationService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onHandleIntent called with intent: " + intent);
        }
        String action = intent.getAction();
        if (ACTION_SHOW_ALARM.equals(action)) {
            showTimerDoneNotification();
        } else if (ACTION_DELETE_ALARM.equals(action)) {
            deleteTimer();
        } else if (ACTION_RESTART_ALARM.equals(action)) {
            restartAlarm();
        } else {
            throw new IllegalStateException("Undefined constant used: " + action);
        }
    }

    private void restartAlarm() {
        Intent dialogIntent = new Intent(this, MainActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Timer restarted.");
        }
    }

    private void deleteTimer() {
        cancelCountdownNotification();

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ACTION_SHOW_ALARM, null, this,
                NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.cancel(pendingIntent);

        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Timer deleted.");
        }
    }

    private void cancelCountdownNotification() {
        NotificationManager notifyMgr =
                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        notifyMgr.cancel(NOTIFICATION_TIMER_COUNTDOWN);
    }

    private void showTimerDoneNotification() {
        // Cancel the countdown notification to show the "timer done" notification.
        cancelCountdownNotification();

        // Create an intent to restart a timer.
        Intent restartIntent = new Intent(ACTION_RESTART_ALARM, null, this,
                NotificationService.class);
        PendingIntent pendingIntentRestart = PendingIntent
                .getService(this, 0, restartIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create notification that timer has expired.
        NotificationManager notifyMgr =
                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        Notification notif = new Notification.Builder(this)
                .setSmallIcon(R.drawable.event_calendar)
                .setContentTitle(getResources().getString(R.string.notification_title))
                .setContentText(getResources().getString(R.string.notification_content))
                //.setUsesChronometer(true)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .build();

        notifyMgr.notify(NOTIFICATION_TIMER_EXPIRED, notif);
    }
}