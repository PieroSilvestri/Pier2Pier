package com.example.piero.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TEST-APP";
    int mCounter;
    TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = (TextView)findViewById(R.id.etichetta);

        MyRunnable myRunnable = new MyRunnable();

        Thread vThread = new Thread(myRunnable);
        vThread.start();

    }

    private void updateText(final int aValue){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mText.setText("" + aValue);
            }
        });
    }

    private class MyRunnable implements Runnable{
        @Override
        public void run() {
            for(int vIndex = 0; vIndex < 10000000; vIndex++){
                mCounter += vIndex;
            }
            updateText(mCounter);

            Log.d(TAG, "COUNTER:"+mCounter);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy:" + this);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.d(TAG, "onFinalize" + this);
    }
}
