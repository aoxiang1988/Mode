import selfdesignpattern.abstractfactory.Client;
import selfdesignpattern.abstractfactory.DefaultValue;
import selfdesignpattern.builder.Director;
import selfdesignpattern.observer.ObserverClient;
import selfdesignpattern.observer.SubServer;
import selfdesignpattern.simplefactory.SimpleFactoryA;
import selfdesignpattern.simplefactory.SimpleFactoryB;
import selfdesignpattern.simplefactory.SimpleFactoryBase;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        System.out.print("my app\n");
        JFrame frame = new JFrame();
        frame.setBounds(500, 500, 500, 500);
        JPanel panel = new JPanel(new FlowLayout());
        JButton button = new JButton("select");
        panel.add(button);
        frame.setContentPane(panel);
        frame.setVisible(true);

        factoryMode();

        /*观察者模式*/
        ObserverClient observerClient = ObserverClient.getObserverClient();//注册观察者

        SubServer subServer = SubServer.getSubServer();
        subServer.serverNotifyObserver();//通知所有观察者，进行更新

        observerClient.StopObserverClient();//注销观察者

        Director.conCreate();

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
