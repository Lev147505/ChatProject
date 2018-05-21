package chat.server;
import chat.ServerData;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class ServerSide implements ServerData {
    private ServerSocket server;
    private Socket socket;
    private Vector<ClientHandler> clientList;
    private ArrayList<String> onLiners;

    private AuthService authService;
    public AuthService getAuthService() {
        return authService;
    }

    public ServerSide(){
        try{
            server = new ServerSocket(PORT);
            authService = new BaseAuthService();
            authService.start();
            clientList = new Vector<>();
            onLiners = new ArrayList<>();
            System.out.println("Server running and awaiting connections");
            while (true){
                socket = server.accept();
                clientList.add(new ClientHandler(this,socket));
                System.out.println("Client connected");
            }
        }catch (IOException exc){
            exc.printStackTrace();
        }finally {

        }

    }
    public void broadcast(String msg){
        for (ClientHandler client : clientList) {
            client.sendMsg(msg);
        }
    }
    public void personalMSG(ClientHandler writer, String nick, String msg){
        for (ClientHandler client : clientList) {
            if (client.getNick().equals(nick)){
                client.sendMsg(writer.getNick() + " send you private massage: " + msg);
                writer.sendMsg(writer.getNick() + " to " + nick + ": " + msg);
                return;
            }
        }
        writer.sendMsg("There is no user " + nick + " in ShoutBox!" + "\n Maybe you make a typo...");
    }
    public void unSubscribeMe(ClientHandler client){
        clientList.remove(client);
    }
    public boolean isNickBusy(String nick){
        for (ClientHandler client : clientList) {
            if (client.getNick().equals(nick))return true;
        }
        return false;
    }
    public String decodeWhisper(String[] elements){
        StringBuffer str = new StringBuffer(elements[2]);
        for (int i=3; i<elements.length; i++){
            str.append(" " + elements[i]);
        }
        str.trimToSize();
        return str.toString();
    }
    public void setOnLiners(String nick){
        onLiners.add(nick);
    }
    public void correctOnLiners(String nick){
        onLiners.remove(nick);
    }
    public void listPainter(){
        StringBuffer str = new StringBuffer(" " + onLiners.get(0) + "\n");
        for (int i = 1; i < onLiners.size(); i++) {
            str.append(" " + onLiners.get(i) + "\n");
        }
        str.trimToSize();
        broadcast(UPDATE_ONLINE_LIST + " " + str.toString());
    }
}
