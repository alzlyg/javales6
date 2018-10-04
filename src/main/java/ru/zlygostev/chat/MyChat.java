package ru.zlygostev.chat;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import static com.sun.javafx.fxml.expression.Expression.add;


public class MyChat extends JFrame {
    private JTextField chatField;
    private JTextArea chatHistory;
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;
    private Socket sock;
    private Scanner in;
    private PrintWriter out;
    public MyChat() {
        try {
            sock = new Socket(SERVER_ADDR, SERVER_PORT);
            in = new Scanner(sock.getInputStream());
            out = new PrintWriter(sock.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        setBounds(600, 300, 500, 500);
        setTitle("Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chatHistory = new JTextArea();
        chatHistory.setEditable(false);
        chatHistory.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(chatHistory);
        add(jsp, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);
        JButton jbSend = new JButton("SEND");
        bottomPanel.add(jbSend, BorderLayout.EAST);
        chatField = new JTextField();
        bottomPanel.add(chatField, BorderLayout.CENTER);
        jbSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!chatField.getText().trim().isEmpty()) {
                    sendMsg();
                    chatField.grabFocus();
                }
            }
        });
        chatField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMsg();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (in.hasNext()) {
                            String w = in.nextLine();
                            if (w.equalsIgnoreCase("end session")) break;
                            chatHistory.append(w);
                            chatHistory.append("\n");
                        }
                    }
                } catch (Exception e) {
                }
            }
        }).start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    out.println("end");
                    out.flush();
                    sock.close();
                    out.close();
                    in.close();
                } catch (IOException exc) {
                }
            }
        });
        setVisible(true);
    }
    public void sendMsg() {
        out.println(chatField.getText());
        out.flush();
        chatField.setText("");
    }
}
