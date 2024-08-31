package com.example.androidthread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;

public class CustomHandlerThread extends HandlerThread {
    private static final String TAG = "CustomHandlerThread";
    Handler mHandler;
    public CustomHandlerThread(String name) {
        super(name);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.v(TAG,"Thread id when message is posted: "+Thread.currentThread().getId()+", Count: "+msg.obj);
            }
        };

    }
}
