package com.bob.stickintocsdn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private EditText blogUrlEt;
    private EditText timesEt;
    private ImageView sendBt;
    private String url;//博客链接
    private int times;//刷博客次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url= "http://blog.csdn.net/";//博客链接
                url= url+blogUrlEt.getText().toString();
                Log.i("url", url);
                times= Integer.parseInt(timesEt.getText().toString());
                for (int i= 0;i< times;i++){

                    StringRequest request= new StringRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.i("result", "success");
                        }
                    }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.i("result", "failed");
                        }
                    });

                    queue.add(request);
                }
            }
        });

    }

    private void init(){
        queue= Volley.newRequestQueue(this);
        blogUrlEt = (EditText) findViewById(R.id.et_url);
        timesEt = (EditText) findViewById(R.id.et_times);
        sendBt= (ImageView)findViewById(R.id.bt_send);
    }
}
