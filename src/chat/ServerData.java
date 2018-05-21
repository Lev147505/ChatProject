package chat;

public interface ServerData {
    int PORT = 8189;
    String SERVER_URL = "localhost";
    String CLOSE_COMMAND = "/end";
    String AUTH = "/auth";
    String AUTH_SUCCESSFUL = "SuccessAuthorization";
    String CLOSE_OK = "You are successfully disconnected,";
    String PERSONAL_MSG = "/w";
    String UPDATE_ONLINE_LIST = "/UpdateOnLiners";
    String EXCEED_LOGIN_TIME = "/exceed";
}
