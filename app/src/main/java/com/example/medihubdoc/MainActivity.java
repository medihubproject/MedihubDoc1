package com.example.medihubdoc;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Subscriber;
import com.opentok.android.OpentokError;
import androidx.annotation.NonNull;
import android.Manifest;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.opengl.GLSurfaceView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener{

    private static String API_KEY = "46824644";
    private static String SESSION_ID = "1_MX40NjgyNDY0NH5-MTU5NjExNzk4NDY3MX5Sb1pKTVVybkNueWRaK0NPcE14OEFEQlJ-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjgyNDY0NCZzaWc9NGNkNGExNzlhNTNjZmQxZmRiYTA0MDMyZTZmMzM4YmQ1ZjkxMWJhNjpzZXNzaW9uX2lkPTFfTVg0ME5qZ3lORFkwTkg1LU1UVTVOakV4TnprNE5EWTNNWDVTYjFwS1RWVnlia051ZVdSYUswTlBjRTE0T0VGRVFsSi1mZyZjcmVhdGVfdGltZT0xNTk2MTE3OTk5Jm5vbmNlPTAuMzg2MzQ2NzU3NzcxMDk3OTUmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTU5ODcxMDAwMCZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private Session mSession;
    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    FloatingActionButton disc;
    ProgressDialog progressBar,progressBar2;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Please wait, Setting up streamer");
        progressBar.show();
        disc=findViewById(R.id.disconnect);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,loginActivity.class);
                startActivity(intent);
                finish();
            }
        },80000);

        disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relieve();
            }
        });



//        mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
//        mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout
            mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
            mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);

            // initialize and connect to the session
            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
            mSession.setSessionListener(this);
            mSession.connect(TOKEN);


        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }


    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

        mPublisherViewContainer.addView(mPublisher.getView());

        if (mPublisher.getView() instanceof GLSurfaceView){
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);
        if (progressBar != null) {
            progressBar.dismiss();
        }
        progressBar2 = new ProgressDialog(MainActivity.this);
        progressBar2.setCancelable(false);
        progressBar2.setMessage("Please wait, Connecting Online Doctor....");
        progressBar2.show();
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());
            if (progressBar2 != null) {
                progressBar2.dismiss();
            }
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");
        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }


    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated");
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed");
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "Publisher error: " + opentokError.getMessage());
    }
    public void relieve()
    {
        mSession.disconnect();
        startActivity(new Intent(MainActivity.this,loginActivity.class));
    }
}