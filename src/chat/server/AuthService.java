package chat.server;

public interface AuthService {
    void start();
    void stop();
    String getNickByLoginPass(String login, String password);
}
