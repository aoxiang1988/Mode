package com.application;

import javax.swing.*;
import java.awt.*;

public class MainDisplaySystem {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBounds(500, 500, 500, 500);
        JPanel panel = new JPanel(new FlowLayout());
        JButton button = new JButton("select");
        panel.add(button);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
