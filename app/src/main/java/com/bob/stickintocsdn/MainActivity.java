package com.bob.stickintocsdn;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bob.stickintocsdn.service.MainService;
import com.bob.stickintocsdn.utils.Constants;

/**
 * 当前测试volley队列里最多只能里最多只能放入1900～2000次的网络访问，因此我们采用了多线程并发
 * 的策略，然并卵
 */
public class MainActivity extends AppCompatActivity {
    private EditText blogUrlEt;
    private EditText timesEt;
    private FloatingActionButton sendBt;
    private String url;//博客链接
    private MainService.StartWork startWorkBinder;
    private boolean isBind= false;

    private int totalTimes;//刷博客次数

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            startWorkBinder = (MainService.StartWork) service;
            startWorkBinder.doTask(url, totalTimes);
            sendBt.setClickable(false);
          /*  while(true){
               if (startWorkBinder.getProgress()== totalTimes){
                   sendBt.setClickable(true);
                   break;
               }
            }*/
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       final Intent bindIntent = new Intent(MainActivity.this, MainService.class);//如果已经绑定了，再次点击是不会响应的
        init();
        sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                totalTimes = 0;
                url = Constants.ARTICLE_URL + blogUrlEt.getText().toString();
                totalTimes = Integer.parseInt(timesEt.getText().toString());

                if (!isBind) {
                    bindService(bindIntent, connection, BIND_AUTO_CREATE);
                    isBind= true;
                }else {
                    unbindService(connection);
                    isBind= false;
                }
                //doTask(url, totalTimes);
                /*for (int i = 0; i < TotalTimes/100-1; i++) {
                    doTask(url, 100);
                    while (currentTimes<= TotalTimes- TotalTimes%100) {
                        Log.i("currentTimes", currentTimes+"");
                        if (currentTimes == 100) {
                            doTask(url, 100);//文章id和刷新次数
                            //break;//跳出本次轮询，进行下一次轮询
                        }
                    }
                }*/
                //doTask(url, currentTimes % 1800);
                //  doTask(url, currentTimes%1800);
               /* while (true) {
                    if (currentTimes % 100 == 0) {
                        doTask(url, currentTimes % 100);//文章id和刷新次数
                        break;
                    }
                }*/
            }
        });

    }


    private void init() {
        blogUrlEt = (EditText) findViewById(R.id.et_url);
        timesEt = (EditText) findViewById(R.id.et_times);
        sendBt = (FloatingActionButton) findViewById(R.id.bt_send);

    }
}
