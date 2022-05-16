import java.util.ArrayList;

public class Client {
    String name;
    ArrayList<String> messages;
    ArrayList<String> friends;
    boolean loggedIn;

    public Client(String name){
        this.name = name;
        messages = new ArrayList<>();
        friends = new ArrayList<>();
        loggedIn = false;
    }
}
