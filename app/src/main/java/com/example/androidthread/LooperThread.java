package com.example.androidthread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;

public class LooperThread extends Thread {
    private static final String TAG = "LooperThread";
    Handler mHandler;
    @Override
    public void run() {
        Looper.prepare();

        mHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.v(TAG,"Thread id when message is posted: "+Thread.currentThread().getId()+", Count: "+msg.obj);
            }
        };
        Looper.loop();
    }
}
