package com.application;

import com.application.broadcast.BroadCastReceiver;
import com.application.broadcast.intent.Intent;
import com.application.broadcast.intent.IntentFilter;
import com.application.broadcast.intent.PendingIntent;
import com.application.handler.Handler;
import com.application.handler.MessageQueue;
import com.application.handler.WorkLooper;
import com.application.selfdesignpattern.abstractfactory.Client;
import com.application.selfdesignpattern.abstractfactory.DefaultValue;
import com.application.selfdesignpattern.builder.Director;
import com.application.selfdesignpattern.observer.ObserverClient;
import com.application.selfdesignpattern.observer.SubServer;
import com.application.selfdesignpattern.proxymode.WorkClassPoxy;
import com.application.selfdesignpattern.simplefactory.SimpleFactoryA;
import com.application.selfdesignpattern.simplefactory.SimpleFactoryB;
import com.application.selfdesignpattern.simplefactory.SimpleFactoryBase;
import com.sun.crypto.provider.BlowfishKeyGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Objects;

import static com.application.broadcast.Receiver.registerReceiver;
import static com.application.broadcast.Receiver.sendBroadCastReceiver;

public class Main {

    private static final String MSG_A = "msg-a";
    private static final String MSG_B = "msg-b";
    private static final String MSG_C = "msg-c";

    private static void myHandlerTest() {//模拟handler消息执行
        Handler handler = new Handler() {
            @Override
            public void handleMessage(MessageQueue.Message msg) {
                switch (msg.msgString) {
                    case MSG_A:
                        Log.i(TAG, MSG_A);
                        break;
                    case MSG_B:
                        Log.i(TAG, MSG_B);
                        break;
                    case MSG_C:
                        Log.i(TAG, MSG_C);
                        break;
                    default:
                        break;
                }
            }
        };

        handler.sendMessage(MSG_A);
        handler.sendMessage(MSG_B, 6000);
        handler.sendMessage(MSG_C, 1050);
    }

    private static final String TAG = "Main";
    public static void main(String[] args) {
        Log.i(TAG, "my app\n");
        factoryMode();

        /*观察者模式*/
        ObserverClient observerClient = ObserverClient.getObserverClient();//注册观察者

        SubServer subServer = SubServer.getSubServer();
        subServer.serverNotifyObserver();//通知所有观察者，进行更新

        observerClient.StopObserverClient();//注销观察者

        Director.conCreate();

        WorkUtils.linkStack();
        WorkUtils.linkDLStack();
        WorkUtils.fifoQueue();

        myHandlerTest();

        WorkClassPoxy workClassPoxy = new WorkClassPoxy();
        workClassPoxy.doSomeThing();//不可见具体的执行代码
        /*广播接收器*/
        IntentFilter iFilter= new IntentFilter();
        iFilter.addAction("add");
        iFilter.addAction("abc");
        iFilter.addAction("bbb");

        BroadCastReceiver receiver = new BroadCastReceiver() {
            @Override
            public void onReceiver(Intent i) {
                if (i.getAction().equals("abc")) {
                    Log.i(TAG, "receiver got the broad cast ： " + i.getAction());
                }
            }
        };
        registerReceiver(receiver, iFilter);

        BroadCastReceiver receiver2 = new BroadCastReceiver() {
            @Override
            public void onReceiver(Intent i) {
                if (i.getAction().equals("add")) {
                    Log.i(TAG, "receiver2 got the broad cast ： " + i.getAction());
                }
            }
        };
        registerReceiver(receiver2, iFilter);

        BroadCastReceiver receiver3 = new BroadCastReceiver() {
            @Override
            public void onReceiver(Intent i) {
                if (i.getAction().equals("bbb")) {
                    Log.i(TAG, "receiver3 got the broad cast ： " + i.getAction());
                }
            }
        };
        registerReceiver(receiver3, iFilter);

        sendBroadCastReceiver(new Intent("abc"));
        sendBroadCastReceiver(new Intent("add"));
        sendBroadCastReceiver(new PendingIntent("bbb", 5000));


        HTTPConnectionUtils utils = HTTPConnectionUtils.getUtils();
        utils.setUrl("https://www.baidu.com/s?wd=sniffing%20Matroska%20Extractor");
        utils.httpConnect();
    }

    private static void factoryMode() {
        /*简单工厂模式*/
        SimpleFactoryA simpleFactoryA = (SimpleFactoryA) SimpleFactoryBase
                .createFactory(SimpleFactoryBase.TYPE_SIMPLE_FACTORY_A);
        simpleFactoryA.function();

        SimpleFactoryB simpleFactoryB = (SimpleFactoryB) SimpleFactoryBase
                .createFactory(SimpleFactoryBase.TYPE_SIMPLE_FACTORY_B);
        simpleFactoryB.function();

        /*抽象工厂模式
         * 定义各种抽象产品类，定义多个继承各种抽象产品类的实际产品（相同子类属同类型，但个体有差异的产品）
         * 定义一个抽象工厂类，该类定义抽象函数（生产所有抽象产品的函数）
         * 构造多个可实例化工厂类继承该抽象工厂类，同时需要实现生产产品函数，这里每一个实例工厂类只对应产出一种类型的产品
         * 根据例子，则是：ConcreateFactory1只生产ProductA1和ProductA2两种同类型产品；
         *                ConcreateFactory2只生产ProductB1和ProductB2两种同类型产品.
         * 缺点增加产品困难，扩展行为容易
         * */
        Client.getClient().doProductFunction(DefaultValue.FACTORY_1, DefaultValue.PRODUCT_A_1);
        Client.getClient().doProductFunction(DefaultValue.FACTORY_1, DefaultValue.PRODUCT_A_2);
        Client.getClient().doProductFunction(DefaultValue.FACTORY_2, DefaultValue.PRODUCT_B_1);
        Client.getClient().doProductFunction(DefaultValue.FACTORY_2, DefaultValue.PRODUCT_B_2);
    }
}
