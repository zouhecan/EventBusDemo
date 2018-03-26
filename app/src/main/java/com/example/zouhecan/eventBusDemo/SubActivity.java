package com.example.zouhecan.eventBusDemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * desc: 本页面由MainActivity中的Intent开启，开启前，MainActivity中已完成EventBus注册，启动后，执行post操作返回数据，实现和替代startActivityForResult()
 * @author zouhecan （zouhecan@58ganji.com）
 *         date: 2018/3/26
 */
public class SubActivity extends AppCompatActivity {

    private static String data = "这是来自SubActivity的返回数据";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sub_activity);
        EventBus.getDefault().post(data);//post操作在SubActivity中完成，在post之前，MainActivity中已经完成了register
        /*findViewById(R.id.return_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(data);//post操作在SubActivity中完成，在post之前，MainActivity中已经完成了register
            }
        });*/
    }
}
