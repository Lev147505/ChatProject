package chat.client;

import com.sun.deploy.panel.JSmartTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;

public class ChatWindow extends JFrame {
    // Объявляем элементы окна чата
    private JTextArea history;
    private JPanel write_send;
    private JPanel shouters;
    private JTextField write;
    private JButton send;
    private JLabel label;
    private JTextArea listShouters;
    private JScrollPane shoutersScroll;
    private NewClient client;
    // Объявляем элементы окна авторизации
    private JTextField login;
    private JPasswordField password;
    private JButton logIn;
    private JPanel top;

    public ChatWindow(){
        setTitle("Shout Box");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);

        history = new JTextArea();
        write_send = new JPanel();
        shouters = new JPanel();

        history.setEditable(false);
        history.setLineWrap(true);
        history.setBackground(Color.RED);
        JScrollPane scroll = new JScrollPane(history);

        write_send.setPreferredSize(new Dimension(getWidth(), 25));
        write_send.setLayout(new BorderLayout());
        write = new JTextField();
        send = new JButton("SEND");
        send.setPreferredSize(new Dimension(100, write_send.getHeight()));
        write.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendAction();
            }
        });
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendAction();
            }
        });
        write_send.add(write, BorderLayout.CENTER);
        write_send.add(send, BorderLayout.EAST);

        shouters.setPreferredSize(new Dimension(100, getHeight()-write_send.getHeight()));
        shouters.setLayout(new BorderLayout());
        label = new JLabel("Shouters online:");
        label.setAlignmentX(shouters.getWidth()/4);
        listShouters = new JTextArea();
        listShouters.setEditable(false);
        shoutersScroll = new JScrollPane(listShouters);
        shouters.add(label, BorderLayout.NORTH);
        shouters.add(shoutersScroll, BorderLayout.CENTER);

        // Инициализация элементов окна авторизации
        login = new JTextField();
        password = new JPasswordField();
        logIn = new JButton("Login");
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auth();
            }
        });
        password.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auth();
            }
        });
        logIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auth();
            }
        });
        top = new JPanel(new GridLayout(1,3));
        top.add(login);
        top.add(password);
        top.add(logIn);
        // Расстановка элементов окна чата
        add(scroll, BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                client.disconnect();
            }
        });
        //Создание нового клиента и передача ссылки на класс ChatWindow в логику
        client = new NewClient();
        client.init(this);

        setVisible(true);
    }

    public void sendAction() {
        client.sendMsg(write.getText());
        write.setText("");

    }
    public void showMsg(String msg){
        Date currentDate = new Date();
        history.append(currentDate + "\n" + msg +"\n\n");
        history.setCaretPosition(history.getDocument().getLength());
    }
    public void auth(){
        String pass = new String(password.getPassword());
        if (login.getText().length() != 0 && pass.length() != 0){
            client.auth(login.getText(),pass);
        }else showMsg("Wrong login/password");
        login.setText("");
        password.setText("");
    }
    public void loginMode(){
        if (client.isAuthGet()){
            top.setVisible(false);
            history.setBackground(Color.green);
            add(write_send,BorderLayout.SOUTH);
            add(shouters, BorderLayout.EAST);
        }else{
            add(top,BorderLayout.SOUTH);
        }
    }
    public void upOnlineUsers (String list){
        listShouters.setText(list);
    }
}
