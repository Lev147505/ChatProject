package chat.client;
import chat.ServerData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NewClient implements ServerData {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    private boolean isAuth = false;
    public boolean isAuthGet(){
        return isAuth;
    }
    public void isAuthSet(boolean isAuth){
        this.isAuth = isAuth;
    }

    NewClient(){
        try{
            socket = new Socket(SERVER_URL, PORT);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        }catch (IOException exc){
            exc.printStackTrace();
        }

    }
    public void init (ChatWindow view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    view.loginMode();
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith(AUTH_SUCCESSFUL)){
                            isAuthSet(true);
                            view.loginMode();
                            break;
                        }else if (msg.startsWith(EXCEED_LOGIN_TIME)){
                            view.showMsg(msg.substring(EXCEED_LOGIN_TIME.length()));
                        }else if(!msg.startsWith(UPDATE_ONLINE_LIST))view.showMsg(msg);
                    }
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith(UPDATE_ONLINE_LIST)){
                            view.upOnlineUsers(msg.substring(UPDATE_ONLINE_LIST.length()));
                        }else view.showMsg(msg);
                    }
                }catch (IOException exc){
                    exc.printStackTrace();
                }
            }
        }).start();
    }
    public void sendMsg(String msg){
        try{
            out.writeUTF(msg);
            out.flush();
        }catch (IOException exc){
            exc.printStackTrace();
        }
    }
    public void auth(String login, String password){
        try {
            out.writeUTF(AUTH + " " + login + " " + password);
        }catch (IOException exc){
            exc.printStackTrace();
        }
    }
    public void disconnect(){
        try {
            out.writeUTF(CLOSE_COMMAND);
            socket.close();
        }catch (IOException exc){
            exc.printStackTrace();
        }
    }
}
