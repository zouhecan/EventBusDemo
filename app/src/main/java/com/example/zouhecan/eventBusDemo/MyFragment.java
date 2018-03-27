package com.example.zouhecan.eventBusDemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * desc: 本Fragment的数据没有通过Bundle传递，而是通过EventBus在Activity中发起post，fragment中接收，好处在于传递的对象数据不用序列化
 * 生命周期: EventBus注册——>接收到EventBus数据——>onCreate——>onCreateView——>onCreateView——>onDestroy——>EventBus取消注册
 * @author zouhecan （zouhecan@58ganji.com）
 *         date: 2018/3/26
 */
public class MyFragment extends Fragment {
    private MyShop myShop;
    private TextView nameView;
    private TextView scoreView;
    private TextView priceView;

    public static MyFragment newInstance(){
        MyFragment fragment = new MyFragment();
        EventBus.getDefault().register(fragment);
        Log.d(fragment.getClass().getName(), "EventBus注册");
        return fragment;
    }

    public MyFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getClass().getName(), "fragment： onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(getClass().getName(), "fragment： onCreateView");
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        priceView = view.findViewById(R.id.price);
        nameView = view.findViewById(R.id.name);
        scoreView = view.findViewById(R.id.score);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(getClass().getName(), "fragment： onActivityCreate");
        initView();
    }

    private void initView(){
        if(myShop != null){
            priceView.setText(myShop.getPrice());
            nameView.setText(myShop.getName());
            scoreView.setText(myShop.getScore());
        }
    }

    /**
     * 根据生命周器，EventBus接收数据发生在View的实例化和绘制之前，那么如何完成刷新UI操作呢？
     * 错误的做法：在接收数据的同事去刷新UI（因为此时view还没有绘制）
     * 正确的做法：先接收数据，然后在onActivityCreated中，利用接收的数据去刷新UI
     * @param shop
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent (MyShop shop) {
        if (shop != null) {
            Log.d(getClass().getName(), "接收到EventBus数据");
            myShop = shop;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(getClass().getName(), "fragment： onDestroy");
        EventBus.getDefault().unregister(this);//取消注册
        Log.d(getClass().getName(), "EventBus取消注册");
    }
}
