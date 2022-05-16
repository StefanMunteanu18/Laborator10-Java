import java.util.HashMap;

public class Commands {
    String OwnerName;
    private HashMap<String, Client> clients;
    public Commands(HashMap<String, Client> clients){
        this.clients = clients;
        this.OwnerName = null;
    }

    public void setOwnerName(String s){
        this.OwnerName = s;
    }

    public String register(String name) {
        synchronized (clients){
            if(clients.get(name) == null){
                clients.put(name, new Client(name));
                return name + " registered!";
            }
            else{
                return "User already registered!";
            }
        }
    }

    public String login(String name) {
        synchronized (clients){
            if(clients.get(name) != null){
                if(clients.get(name).loggedIn == false){
                    clients.get(name).loggedIn = true;
                    return "Logged in!";
                }
                else {
                    return "Already logged in!";
                }
            }
            else{
                return "No user found!";
            }
        }
    }

    public String friend(String name){
        synchronized (clients){
            if(OwnerName != null){
                if(clients.get(name)!=null){
                    if(clients.get(OwnerName).friends.contains(name)){
                        return name + " is already a friend";
                    }
                    else {
                        clients.get(OwnerName).friends.add(name);
                        return name + " added";
                    }
                }
                else{
                    return "User " + name + " does not exist!";
                }
            }
            else{
                return "You must log in!";
            }
        }
    }

    public String send(String message){
        synchronized (clients){
            if(OwnerName != null){
                for(String name : clients.get(OwnerName).friends){
                    clients.get(name).messages.add(OwnerName + ": " + message);
                }
                return "Message sent!";
            }
            else{
                return "You must log in!";
            }
        }
    }

    public String read(){
        synchronized (clients){
            if(OwnerName != null){
                return "Messages of user " + OwnerName + ": " + clients.get(OwnerName).messages.toString();
            }
            else{
                return "You must log in!";
            }
        }
    }

    public String exit(){
        synchronized (clients){
            if(OwnerName != null) {
                clients.get(OwnerName).loggedIn = false;
            }
            return "exit";
        }
    }
}
