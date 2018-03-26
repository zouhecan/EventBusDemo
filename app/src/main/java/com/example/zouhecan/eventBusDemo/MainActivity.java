package com.example.zouhecan.eventBusDemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * desc: 通过EventBus（Android的发布/订阅事件总线），实现Activity与Activity、Activity与Fragment之间互相传值
 *
 * 代替：直接通过intent包裹Extra值来进行Activity传值
 *
 * 代替：直接通过构建Bundle后setArguments(bundle)进行Fragment传值
 *
 * 功能好处：EventBus可以直接传递一个没有序列化的对象数据，而intent与Bundle传的对象必须要序列化（实现Parcelable接口）
 *
 * 性能好处：代码的可读性更好，耦合度更低，eventBus开销更小
 *
 * EventBus的github地址: https://github.com/greenrobot/EventBus
 *
 * 注意事项：接收方完成register和receive，发送方完成post，每一个post前一定要保证对应的register已经完成，否则接收不到数据
 *
 * 注意事项：接收函数可以自定义命名，但是一定要用注解@Subscribe(threadMode = ThreadMode.MAIN，否则会crash
 *
 * @author zouhecan （zouhecan@58ganji.com）
 *         date: 2018/3/26
 */
public class MainActivity extends FragmentActivity {

    private MyFragment fragment;
    private MyShop shop;
    private EditText editText;
    private String returnData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        editText = findViewById(R.id.data_from_sub_activity);

        //跳转到OtherActivity，并通过EventBus把Myshop信息带过去
        findViewById(R.id.start_other_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherActivity otherActivity = new OtherActivity();//在OtherActivity的构造器中，注册EventBus，保证在post前得到register
                Intent intent = new Intent(MainActivity.this, otherActivity.getClass());
                startActivity(intent);
                EventBus.getDefault().post(initData());//向OtherActivity发送数据, 注意post操作应该放在OtherActivity实例之后，即先register
            }
        });

        //加载本页面的Fragment，并通过EventBus把Myshop信息带过去
        findViewById(R.id.load_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(initData());
            }
        });

        //接收从SubActivity通过EventBus返回data
        findViewById(R.id.get_return_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
            }
        });
    }

    private MyShop initData(){
        shop = new MyShop();
        shop.setName("邹禾灿的门店");
        shop.setPrice("5000块一个月");
        shop.setScore("满分");
        return shop;
    }

    private void addFragment(MyShop shop){
        if(isFinishing()){
            return;
        }
        if(fragment != null){
           return;
        }
        fragment = MyFragment.newInstance();//在实例MyFragment时，便注册EventBus，保证在post前得到register
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();
        EventBus.getDefault().post(shop);//向Fragment发送数据, 注意post操作应该放在MyFragment实例之后，即先register
    }

    //获取由SubActivity通过EventBus返回的数据
    //不可省略注解，否则会crash
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent (String data) {
        if (data != null) {
            returnData = data;
            editText.setText(returnData);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}
