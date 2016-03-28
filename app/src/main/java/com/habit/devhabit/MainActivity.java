package com.habit.devhabit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    public static final int SET_NEW_HABIT = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;


    private static final int NUM_PAGES = 2;
    private TargetController mController;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_screen_slide);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });

        //setContentView(R.layout.activity_main);



        mController = new TargetController(this);

        setupTimer();
        //startService(new Intent(this, NotificationService.class).setAction(NotificationService.ACTION_SHOW_ALARM));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            TextView tv = (TextView) ((Activity)mContext).getLayoutInflater().inflate(R.layout.about_dlg, null);
            tv.setText(R.string.about_content);
            tv.setMovementMethod(LinkMovementMethod.getInstance());

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.about_title)
                    //.setMessage(R.string.about_content)
                    .setView(tv, 100, 100, 100, 100)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestedCode, int resultCode, Intent data) {
        super.onActivityResult(requestedCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestedCode) {
            case SET_NEW_HABIT:
                Bundle b = data.getExtras();
                Item item = (Item) b.getSerializable("item");
                ItemDAO itemDAO = new ItemDAO(this);
                itemDAO.insert(item);
                ScreenSlidePageFragment.addNewHabit();
                break;
            case REQUEST_IMAGE_CAPTURE:
                Bundle b2 = data.getExtras();
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ScreenSlidePageFragment.setImageBitmap(photo);
                break;
            default:
                break;
        }
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(i);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(mContext, mController, position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private void setupTimer() {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.setAction(ACTION_SHOW_ALARM);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
        long systemTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
// 这里时区需要设置一下，不然会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MINUTE, 32);
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
// 选择的定时时间
        long selectTime = calendar.getTimeInMillis();
// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if(systemTime > selectTime) {
            Toast.makeText(this,"设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
// 计算现在时间到设定时间的时间差
        long time = selectTime - systemTime;
        firstTime += time;
        long milliSecondsInADay = 24 * 60 * 60 * 1000;
// 进行闹铃注册
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                firstTime, milliSecondsInADay, sender);
//        Log.i(TAG,"time ==== " + time +", selectTime ===== "
//                + selectTime + ", systemTime ==== " + systemTime +", firstTime === " + firstTime);
//        Toast.makeText(this,"设置重复闹铃成功! ", Toast.LENGTH_LONG).show();
    }
}
