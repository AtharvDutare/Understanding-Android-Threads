package com.example.androidthread;

import android.annotation.SuppressLint;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private Button startThread, stopThread;
    private TextView threadCount;
    int count=0;
    private boolean mThreadState=false;
    private MySyncTask mySyncTask;
    Handler mHandler = new Handler(Looper.getMainLooper());
    LooperThread looperThread;
    CustomHandlerThread customHandlerThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startThread=findViewById(R.id.startthread);
        stopThread=findViewById(R.id.stopThread);
        threadCount=findViewById(R.id.threadCount);
//        looperThread=new LooperThread();
//        looperThread.start();
        customHandlerThread =new CustomHandlerThread("customHandlerThread");
        customHandlerThread.start();
        Log.v(TAG,"MainActivity Thread is:"+Thread.currentThread().getId());
        startThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mThreadState=true;
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        while(mThreadState){
//                            try{
//                                Thread.sleep(1000);
//                                count++;
//
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            Log.v(TAG,"Thread id while loop -> "+Thread.currentThread().getId()+", count:"+count);
////                            mHandler.post(new Runnable() {
////                                @Override
////                                public void run() {
////                                    threadCount.setText(""+count);
////                                }
////                            });
//                            threadCount.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    threadCount.setText("count:"+count);
//                                }
//                            });
//                        }

                    //}
                //}).start();
//                mThreadState=true;
//                mySyncTask=new MySyncTask();
//                mySyncTask.execute(count);
                mThreadState=true;
                //Log.v(TAG,"Click huva");
                executeOnCustomLooper();
                //executeOnCustomLooperWithCustomHandler();
           }
        });
        stopThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThreadState=false;
                //mySyncTask.cancel(true);
            }
        });


    }

    public void executeOnCustomLooper() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Log.v("TAG",mThreadState+"");
                while(mThreadState) {
                    try {
                        Log.v(TAG,"Thread id of thread thats send message:"+Thread.currentThread().getId());
                        Thread.sleep(1000);
                        count++;
                        Message msg=new Message();
                        msg.obj=""+count;
                        //looperThread.mHandler.handleMessage(msg);
                        customHandlerThread.mHandler.handleMessage(msg);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
    public void executeOnCustomLooperWithCustomHandler() {
        looperThread.mHandler.post(new Runnable() {
            public void run() {
                while(mThreadState) {
                    try{
                        Thread.sleep(1000);
                        count++;
                        Log.v(TAG,"Thread id of runnable post:"+Thread.currentThread().getId());
                        runOnUiThread(new Runnable() {
                            public void run() {
                                threadCount.setText(""+count);
                                Log.v(TAG,"Thread id of thread thats change UI:"+Thread.currentThread().getId());
                            }
                        });
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }


    private class MySyncTask extends AsyncTask<Integer, Integer, Integer> {
        private Integer customCount;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customCount=0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            int count=integer;
            threadCount.setText(String.valueOf(count));
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            customCount=integers[0];
            while(mThreadState){
                try{
                    Thread.sleep(1000);
                    customCount=customCount+1;
                    publishProgress(customCount);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                Log.v(TAG,"BackGround Thread:"+Thread.currentThread().getId()+" "+customCount);
            }
            return customCount;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            threadCount.setText(String.valueOf(values[0]));
            count=values[0];

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(customHandlerThread!=null){
            customHandlerThread.quitSafely();
        }
    }
}