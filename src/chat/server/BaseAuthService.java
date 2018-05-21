package chat.server;

import java.util.ArrayList;

public class BaseAuthService implements AuthService {
    private class Entry{
        private String login;
        private String password;
        private String nick;

        private Entry(String login, String password, String nick) {
            this.login = login;
            this.password = password;
            this.nick = nick;
        }
    }
    private ArrayList<Entry> entries;
    public BaseAuthService(){
        this.entries = new ArrayList<>();
        entries.add(new Entry("1","1", "Rick" ));
        entries.add(new Entry("2", "2", "Morty"));
        entries.add(new Entry("3", "3", "Jerry"));
        entries.add(new Entry("4","4", "Beth"));

    }
    @Override
    public void start(){

    }
    @Override
    public void stop(){

    }

    @Override
    public String getNickByLoginPass(String login, String password) {
        for (Entry entry : entries) {
            if (entry.login.equals(login)&&entry.password.equals(password))return entry.nick;
        }
        return null;
    }
}
