package com.example.zouhecan.eventBusDemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * desc:本Activity的数据没有通过intent传递，而是通过EventBus在MainActivity中发起post，本activity中接收，好处在于传递的对象数据不用序列化
 * @author zouhecan （zouhecan@58ganji.com）
 *         date: 2018/3/26
 */
public class OtherActivity extends AppCompatActivity {
    private static MyShop myShop;
    private TextView nameView;
    private TextView scoreView;
    private TextView priceView;

    public OtherActivity(){
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_layout);
        priceView = findViewById(R.id.price);
        nameView = findViewById(R.id.name);
        scoreView = findViewById(R.id.score);
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent (MyShop shop) {
        if (shop != null) {
            myShop = shop;
        }
    }

    private void initView(){
        if(myShop != null){
            priceView.setText(myShop.getPrice());
            nameView.setText(myShop.getName());
            scoreView.setText(myShop.getScore());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}
