package org.example;
import java.awt.*;
public class MainInterface {
    public static void main(String[] args) {
        //1.�������ڶ���
        Frame frame = new Frame("���ǵ�һ����������");
        //���ô��ڵ�λ�úʹ�С
        frame.setBounds(100,100,500,300);

        //2.����Panel��������
        Panel panel = new Panel();

        //3.��Panel������������
        panel.add(new TextField("����һ�������ı�"));
        panel.add(new Button("����һ�����԰�ť"));

        //4.��Panel��ӵ�Frame��
        frame.add(panel);

        //5.����Frame��λ�úʹ�С
        frame.setBounds(30,30,500,300);

        //6.����Frame�ɼ�
        frame.setVisible(true);


        //���ô��ڿɼ�
        frame.setVisible(true);
    }
}
