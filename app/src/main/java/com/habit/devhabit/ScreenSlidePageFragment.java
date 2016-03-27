package com.habit.devhabit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by michael on 2016/3/22.
 */
public class ScreenSlidePageFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
    static private Context mContext;
    static private TargetController mController;
    static private Bitmap mBitmap;
    static private ImageView mCameraImage;

    static private ViewGroup mViewGroup;

    AlertDialog.Builder mFirstDlg;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(Context ctx, TargetController controller, int pageNumber) {
        mContext = ctx;
        mController = controller;

        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);

        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        switch (mPageNumber) {
            case 0:
                return createPageOne(inflater, container, savedInstanceState);
            //break;
            case 1:
                return createPageTwo(inflater, container, savedInstanceState);
            //break;
//            case 2:
//                return createPageTwo(inflater, container, savedInstanceState);
            //break;
            default:
                break;
        }
        return null;
    }

    private View createPageZero(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.activity_main_page_1, container, false);
        //Button cameraBtn = (Button) rootView.findViewById(R.id.camera_button);
        FloatingActionButton cameraBtn = (FloatingActionButton) rootView.findViewById(R.id.fab1);
        mCameraImage = (ImageView) rootView.findViewById(R.id.camera_result);
        if (mBitmap != null && mCameraImage != null) {
            mCameraImage.setImageBitmap(mBitmap);
        }
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            (Activity) mContext,
                            new String(Manifest.permission.CAMERA))) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {
                        ActivityCompat.requestPermissions((Activity) mContext,
                                new String[]{Manifest.permission.CAMERA},
                                MainActivity.MY_PERMISSIONS_REQUEST_CAMERA);

                    }

                } else {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    ((Activity) mContext).startActivityForResult(i, MainActivity.REQUEST_IMAGE_CAPTURE);
                }


            }
        });
        return rootView;
    }

    private View createPageOne(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.activity_main_page_2, container, false);

        rootView.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Hello Snackbar", Snackbar.LENGTH_LONG).show();
                Intent i = new Intent();
                i.setClass(mContext, InputTargetActivity.class);

                ((Activity) mContext).startActivityForResult(i, MainActivity.SET_NEW_HABIT);
            }
        });

        // need to be careful, initUi cannot find container if it's been put in the wrong position
        if (mController != null) {
            mController.initHabitViewUi(rootView);
        }

        return rootView;
    }

    private View createPageTwo(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.activity_main_page_3, container, false);

        // need to be careful, initUi cannot find container if it's been put in the wrong position
        if (mController != null) {
            mController.initStatisticUi(rootView);
        }
        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

    static public void setImageBitmap(Bitmap bmp) {
        mBitmap = bmp;

        if (mBitmap != null && mCameraImage != null) {
            mCameraImage.setImageBitmap(mBitmap);
        }
    }

    static public void addNewHabit() {
        ViewGroup habitContainer = (ViewGroup) ((Activity) mContext).findViewById(R.id.habit_container);
        ViewGroup habitStatisticContainer = (ViewGroup) ((Activity) mContext).findViewById(R.id.habit_chart_container);
        if (mController != null) {
            mController.addHabit(habitContainer, habitStatisticContainer);
        }
    }
}
