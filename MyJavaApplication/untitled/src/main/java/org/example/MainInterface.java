package org.example;
import java.awt.*;
public class MainInterface {
    public static void main(String[] args) {
        //1.创建窗口对象
        Frame frame = new Frame("这是第一个窗口容器");
        //设置窗口的位置和大小
        frame.setBounds(100,100,500,300);

        //2.创建Panel容器对象
        Panel panel = new Panel();

        //3.往Panel容器中添加组件
        panel.add(new TextField("这是一个测试文本"));
        panel.add(new Button("这是一个测试按钮"));

        //4.把Panel添加到Frame中
        frame.add(panel);

        //5.设置Frame的位置和大小
        frame.setBounds(30,30,500,300);

        //6.设置Frame可见
        frame.setVisible(true);


        //设置窗口可见
        frame.setVisible(true);
    }
}
