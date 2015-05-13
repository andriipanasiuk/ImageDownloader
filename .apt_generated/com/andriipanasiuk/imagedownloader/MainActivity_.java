//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.andriipanasiuk.imagedownloader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.andriipanasiuk.imagedownloader.R.id;
import com.andriipanasiuk.imagedownloader.R.layout;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class MainActivity_
    extends MainActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private final IntentFilter intentFilter1_ = new IntentFilter();
    private final BroadcastReceiver onDownloadCancelledReceiver_ = new BroadcastReceiver() {


        public void onReceive(Context context, Intent intent) {
            MainActivity_.this.onDownloadCancelled();
        }

    }
    ;
    private final IntentFilter intentFilter2_ = new IntentFilter();
    private final BroadcastReceiver onDownloadProgressReceiver_ = new BroadcastReceiver() {

        public final static String DOWNLOADED_ID_KEY_EXTRA = "downloaded_id_key";

        public void onReceive(Context context, Intent intent) {
            Bundle extras_ = ((intent.getExtras()!= null)?intent.getExtras():new Bundle());
            int position = extras_.getInt(DOWNLOADED_ID_KEY_EXTRA);
            MainActivity_.this.onDownloadProgress(position);
        }

    }
    ;
    private final IntentFilter intentFilter3_ = new IntentFilter();
    private final BroadcastReceiver onDownloadErrorReceiver_ = new BroadcastReceiver() {

        public final static String DOWNLOADED_ID_KEY_EXTRA = "downloaded_id_key";

        public void onReceive(Context context, Intent intent) {
            Bundle extras_ = ((intent.getExtras()!= null)?intent.getExtras():new Bundle());
            int position = extras_.getInt(DOWNLOADED_ID_KEY_EXTRA);
            MainActivity_.this.onDownloadError(position);
        }

    }
    ;
    private final IntentFilter intentFilter4_ = new IntentFilter();
    private final BroadcastReceiver onDownloadCompleteReceiver_ = new BroadcastReceiver() {

        public final static String DOWNLOADED_ID_KEY_EXTRA = "downloaded_id_key";

        public void onReceive(Context context, Intent intent) {
            Bundle extras_ = ((intent.getExtras()!= null)?intent.getExtras():new Bundle());
            int position = extras_.getInt(DOWNLOADED_ID_KEY_EXTRA);
            MainActivity_.this.onDownloadComplete(position);
        }

    }
    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_main);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        restoreSavedInstanceState_(savedInstanceState);
        intentFilter1_.addAction("download_cancelled");
        this.registerReceiver(onDownloadCancelledReceiver_, intentFilter1_);
        intentFilter2_.addAction("download_progress");
        this.registerReceiver(onDownloadProgressReceiver_, intentFilter2_);
        intentFilter3_.addAction("download_error");
        this.registerReceiver(onDownloadErrorReceiver_, intentFilter3_);
        intentFilter4_.addAction("download_complete");
        this.registerReceiver(onDownloadCompleteReceiver_, intentFilter4_);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static MainActivity_.IntentBuilder_ intent(Context context) {
        return new MainActivity_.IntentBuilder_(context);
    }

    public static MainActivity_.IntentBuilder_ intent(Fragment supportFragment) {
        return new MainActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        imageListView = ((ListView) hasViews.findViewById(id.image_list));
        downloadButton = ((Button) hasViews.findViewById(id.download_button));
        urlEditText = ((EditText) hasViews.findViewById(id.download_url));
        if (downloadButton!= null) {
            downloadButton.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    MainActivity_.this.onDownloadClick(view);
                }

            }
            );
        }
        init();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle_) {
        super.onSaveInstanceState(bundle_);
        bundle_.putBoolean("serviceStopped", serviceStopped);
    }

    private void restoreSavedInstanceState_(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return ;
        }
        serviceStopped = savedInstanceState.getBoolean("serviceStopped");
    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver(onDownloadCancelledReceiver_);
        this.unregisterReceiver(onDownloadProgressReceiver_);
        this.unregisterReceiver(onDownloadErrorReceiver_);
        this.unregisterReceiver(onDownloadCompleteReceiver_);
        super.onDestroy();
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<MainActivity_.IntentBuilder_>
    {

        private Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, MainActivity_.class);
        }

        public IntentBuilder_(Fragment fragment) {
            super(fragment.getActivity(), MainActivity_.class);
            fragmentSupport_ = fragment;
        }

        @Override
        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent, requestCode);
            } else {
                if (context instanceof Activity) {
                    Activity activity = ((Activity) context);
                    ActivityCompat.startActivityForResult(activity, intent, requestCode, lastOptions);
                } else {
                    context.startActivity(intent);
                }
            }
        }

    }

}
