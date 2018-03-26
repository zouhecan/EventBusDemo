package com.example.zouhecan.eventBusDemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * desc: 本Fragment的数据没有通过Bundle传递，而是通过EventBus在Activity中发起post，fragment中接收，好处在于传递的对象数据不用序列化
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
        return fragment;
    }

    public MyFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        priceView = view.findViewById(R.id.price);
        nameView = view.findViewById(R.id.name);
        scoreView = view.findViewById(R.id.score);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView(){
        if(myShop != null){
            priceView.setText(myShop.getPrice());
            nameView.setText(myShop.getName());
            scoreView.setText(myShop.getScore());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent (MyShop shop) {
        if (shop != null) {
            myShop = shop;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}
