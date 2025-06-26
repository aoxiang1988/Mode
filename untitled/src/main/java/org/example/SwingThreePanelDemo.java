package org.example;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class SwingThreePanelDemo extends JFrame {

    private String folderPath = "";
    private String filePath1 = "";
    private String fileName1 = "";
    private String filePath2 = "";
    private String fileName2 = "";

    private JLabel folderLabel;
    private JLabel fileLabel1;
    private JLabel fileLabel2;
    private JButton execButton;
    private JLabel statusLabel;

    private String inputParam1 = "";
    private String inputParam2 = "";
    private JTextField textField1;
    private JTextField textField2;
    private static DiffFileCheckerUtils mInputUtils;

    private static JTextArea infoTextArea;
    private JScrollPane scrollPane;
    private static final ResourceBundle messages = ResourceBundle.getBundle("lang.Message");

    public SwingThreePanelDemo() {
        setTitle(messages.getString("title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800); // 固定大小为500*300
        setLocationRelativeTo(null);
        setResizable(false); // 禁止缩放

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 区域1：文件夹选择
        JPanel panel1 = new JPanel();
        panel1.setBorder(new TitledBorder(messages.getString("select.folder")));
        panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton folderBtn = new JButton(messages.getString("button.select.folder"));
        folderLabel = new JLabel(messages.getString("label.not.selected"));
        folderBtn.addActionListener(e -> chooseFolder());
        panel1.add(folderBtn);
        panel1.add(folderLabel);

        // 区域2：文件选择
        JPanel panel2 = new JPanel();
        panel2.setBorder(new TitledBorder(messages.getString("select.config.files")));
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

        JPanel filePanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton fileBtn1 = new JButton(messages.getString("button.select.old.file"));
        fileLabel1 = new JLabel(messages.getString("label.not.selected"));
        fileBtn1.addActionListener(e -> chooseFile(1));
        filePanel1.add(fileBtn1);
        filePanel1.add(fileLabel1);

        JPanel filePanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton fileBtn2 = new JButton(messages.getString("button.select.new.file"));
        fileLabel2 = new JLabel(messages.getString("label.not.selected"));
        fileBtn2.addActionListener(e -> chooseFile(2));
        filePanel2.add(fileBtn2);
        filePanel2.add(fileLabel2);

        panel2.add(filePanel1);
        panel2.add(filePanel2);

        // 区域3：执行按钮
        JPanel panel3 = new JPanel();
        panel3.setBorder(new TitledBorder(messages.getString("execute.panel.title")));
        panel3.setLayout(new FlowLayout(FlowLayout.LEFT));
        execButton = new JButton(messages.getString("button.execute"));
        statusLabel = new JLabel(messages.getString("status.waiting"));
        execButton.addActionListener(this::onExecute);

        JPanel inputPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label1 = new JLabel(messages.getString("label.server.path"));
        textField1 = new JTextField(20); // 文本框宽度为20
        inputPanel1.add(label1);
        inputPanel1.add(textField1);
        // 输入框2
        JPanel inputPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label2 = new JLabel(messages.getString("label.diff.name"));
        textField2 = new JTextField(20); // 文本框宽度为20
        inputPanel2.add(label2);
        inputPanel2.add(textField2);

        // 将输入面板添加到 panel3
        panel3.add(inputPanel1);
        panel3.add(inputPanel2);

        panel3.add(execButton);
        panel3.add(statusLabel);

        // 区域4：文本显示区域
        JPanel panel4 = new JPanel();
        panel4.setBorder(new TitledBorder(messages.getString("usage.instructions")));
        panel4.setLayout(new BorderLayout());

        infoTextArea = new JTextArea(5, 40); // 高度为5行，宽度为40字符
        infoTextArea.setEditable(false); // 禁止编辑
        scrollPane = new JScrollPane(infoTextArea);

        infoTextArea.append(messages.getString("info.text"));

        panel4.add(scrollPane, BorderLayout.CENTER);


        // 添加到主面板
        mainPanel.add(panel1);
        mainPanel.add(panel2);
        mainPanel.add(panel3);
        mainPanel.add(panel4);


        add(mainPanel, BorderLayout.CENTER);
    }

    private void chooseFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ret = chooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File folder = chooser.getSelectedFile();
            folderPath = folder.getAbsolutePath();
            folderLabel.setText(folderPath);
        }
    }

    private void chooseFile(int idx) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int ret = chooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (idx == 1) {
                filePath1 = file.getAbsolutePath();
                fileLabel1.setText(filePath1);
                fileName1 = file.getName();
            } else {
                filePath2 = file.getAbsolutePath();
                fileLabel2.setText(filePath2);
                fileName2 = file.getName();
            }
        }
    }

    private void onExecute(ActionEvent e) {
        boolean isAnyEmpty = folderPath.isEmpty() || filePath1.isEmpty() || filePath2.isEmpty();
        if (isAnyEmpty) {
            showInfoDialog(true);
        } else {
            execButton.setEnabled(false);
            statusLabel.setText(messages.getString("status.working"));


            inputParam1 = textField1.getText(); // 获取参数1的值
            inputParam2 = textField2.getText(); // 获取参数2的值

            // 执行操作，例如打印参数值
            System.out.println("服务器路径: " + inputParam1);
            System.out.println("版本差分名: " + inputParam2);

            System.out.println("old file: " + fileName1);
            System.out.println("new file: " + fileName2);

            mInputUtils.buildDiffFile(folderPath, fileName1, fileName2);
            mInputUtils.diffInitFun(folderPath, inputParam1, inputParam2);

            // 使用SwingWorker避免阻塞UI线程
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    Thread.sleep(2500); // 5秒
                    return null;
                }

                @Override
                protected void done() {
                    statusLabel.setText(messages.getString("status.finished"));
                    execButton.setEnabled(true);
                }
            }.execute();
            showInfoDialog(false);
        }
    }

    // 全局设置字体
    private static void setUIFont(Font font) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }

    // 信息提示框实现
    private void showInfoDialog(boolean isAnyEmpty) {
        JDialog dialog = new JDialog(this, messages.getString("dialog.title"), true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JLabel msgLabel = new JLabel(isAnyEmpty ?
                messages.getString("prompt.empty.params") :
                messages.getString("prompt.execution.done"),
                SwingConstants.CENTER);
        msgLabel.setFont(msgLabel.getFont().deriveFont(Font.PLAIN, 18f));
        dialog.add(msgLabel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton okBtn = new JButton(messages.getString("dialog.button.title"));
        btnPanel.add(okBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        okBtn.addActionListener(e -> {
            dialog.dispose();
            if (!isAnyEmpty) {
                try {
                    Desktop.getDesktop().open(new File(folderPath));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "无法打开文件夹:\\n" + ex.getMessage());
                }
            }
        });

        dialog.setVisible(true);
    }


    public static void main(String[] args) {
        mInputUtils = new DiffFileCheckerUtils();
        setUIFont(new Font("Dialog", Font.PLAIN, 25)); // 设置全局字体为20号
        SwingUtilities.invokeLater(() -> {
            SwingThreePanelDemo frame = new SwingThreePanelDemo();
            frame.setVisible(true);
        });
    }
}