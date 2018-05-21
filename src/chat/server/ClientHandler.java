package chat.server;

import chat.ServerData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements ServerData{
    private ServerSide server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nick;
    private long t;

    ClientHandler(ServerSide server, Socket socket){
        this.server = server;
        this.socket = socket;
        nick = "undefined";
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }catch (IOException exc){
            exc.printStackTrace();
        }
        new Thread(() -> {
            try{
                long createTime = System.currentTimeMillis();
                while (true){
                    t = System.currentTimeMillis()-createTime;
                    if (t>=10000){
                        throw new OutOfTimeException();
                    }
                    if (in.available() != 0){
                        String msg = in.readUTF();
                        if (msg.startsWith(AUTH)){
                            String[] elements = msg.split(" ");
                            String nick = server.getAuthService().getNickByLoginPass(elements[1],elements[2]);
                            if (nick != null){
                                if (!server.isNickBusy(nick)){

                                    sendMsg(AUTH_SUCCESSFUL + " " + nick);
                                    this.nick = nick;
                                    server.broadcast(this.nick + " has been entered the ShoutBox");
                                    server.setOnLiners(this.nick);
                                    server.listPainter();
                                    break;
                                }else{
                                    sendMsg("This nick is already in ShoutBox");
                                }
                            }else{
                                sendMsg("Wrong login/password");
                            }
                        }
                    }
                }
                while (true){
                    String msg = in.readUTF();
                    if (msg.startsWith(PERSONAL_MSG)){
                        String[] elements = msg.split(" ");
                        server.personalMSG(this,elements[1],server.decodeWhisper(elements));
                    }else if (msg.equalsIgnoreCase(CLOSE_COMMAND)){
                        server.correctOnLiners(this.nick);
                        server.listPainter();
                        break;
                    }else {
                        server.broadcast(this.nick + ": " + msg);
                    }
                }
            }catch (EOFException exc){
                exc.printStackTrace();
            }catch (IOException exc){
                exc.printStackTrace();
            }catch (OutOfTimeException exc){
                sendMsg(EXCEED_LOGIN_TIME + "You exceeded login time. Socket has been closed!");
            }
            finally {
                try{
                    System.out.println(t);
                    server.unSubscribeMe(this);
                    if (t<10000){ sendMsg(CLOSE_OK + " " + this.nick);}
                    socket.close();
                }catch (IOException exc){
                    exc.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMsg (String msg){
        try{
            out.writeUTF(msg);
            out.flush();
        }catch (IOException exc){
            exc.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }

}
