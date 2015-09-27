package com.diemen.olaoff;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.diemen.olaoff.utilities.HttpUtils;

/**
 * Created by vikoo on 27/09/15.
 */
public class SplashActivity extends AppCompatActivity {

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler = new Handler();
        sendRequest();
//        mAppSettings.setBool(AppSettings.PREF_LOGGED_IN, false);
    }

    private void sendRequest() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return HttpUtils.isConnectedToInternet();
            }

            @Override
            protected void onPostExecute(Boolean status) {
                Intent i = new Intent(getApplicationContext(), BookingActivity.class);
                i.putExtra(BookingActivity.EXTRA_INTERNET, status);
                startActivity(i);
                finish();

            }
        }.execute();
    }
}
