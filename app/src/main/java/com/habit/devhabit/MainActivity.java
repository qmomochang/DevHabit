package com.habit.devhabit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final int FROM_MAIN_ACTIVITY = 1;
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
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.about_title)
                    .setMessage(R.string.about_content)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getApplicationContext(), R.string.gogo, Toast.LENGTH_SHORT).show();
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
            case FROM_MAIN_ACTIVITY:
                Bundle b = data.getExtras();
                Item item = (Item) b.getSerializable("item");
                ItemDAO itemDAO = new ItemDAO(this);
                itemDAO.insert(item);
                ScreenSlidePageFragment.addNewHAbit();
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
}
