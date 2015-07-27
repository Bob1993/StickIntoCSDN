package com.bob.stickintocsdn.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bob.stickintocsdn.MainActivity;
import com.bob.stickintocsdn.R;

import java.io.UnsupportedEncodingException;

/**
 * Created by Bob on 15/7/25.
 */
public class MainService extends Service {

    private RequestQueue queue;
    private StartWork mStartWork= new StartWork();
    private int currentTimes = 0;

    public class StartWork extends Binder{
        /**
         * 为每一组的任务都创建一个单独的执行队列
         * queue里边总共有5个线程在轮询等待网络访问请求，也就是说，queue里边共有5个线程并发执行,一次最多好像只能处理2000左右的请求
         *
         * @param url
         * @param times
         */
        public void doTask(String url, final int times) {

            Log.w("time＋url：", url);
            for (int i = 0; i < times; i++) {

                CustomStringRequest request = new CustomStringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {//同步代码块保护
                        // Log.i("result", "success");
                        currentTimes++;//每成功一次，次数＋1
                        /*if (currentTimes== times){
                            Toast.makeText(MainActivity.this, "攻击完毕", Toast.LENGTH_SHORT).show();
                        }*/
                        //Log.i("currentTimes", currentTimes+"");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("result", "failed");
                    }
                });

                queue.add(request);

            }
        }

        public int getProgress(){
            return currentTimes;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {//与活动进行通信绑定的时候才会调用
        return mStartWork;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        queue= Volley.newRequestQueue(getApplication());
        Notification notification = new Notification(R.mipmap.ic_launcher, "Start sticking", System.currentTimeMillis());//通知栏图标和启动图标一致
        Intent notificationIntent= new Intent(this, MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, "Stick into csdn", "sticking...", pendingIntent);
        startForeground(1, notification);
    }

    private class CustomStringRequest extends StringRequest {
        public CustomStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(url, listener, errorListener);
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            return null;
        }
    }

}
