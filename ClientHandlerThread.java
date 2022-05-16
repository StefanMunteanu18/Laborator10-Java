import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ClientHandlerThread extends Thread{
    private Socket socket = null;
    HashMap<String, Client> clients;
    private String name;
    ServerSocket serverSocket;

    public ClientHandlerThread (Socket socket, HashMap<String, Client> clients, ServerSocket serverSocket) {
        this.socket = socket;
        this.clients = clients;
        name = new String();
        this.serverSocket = serverSocket;
    }

    public void run () {
        try {
            Commands commands = new Commands(clients);
            // Get the request from the input stream: client → server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            boolean exit = false;
            while(exit == false) {
                String request = in.readLine();
                String[] splited = request.split("\\s+", 2);
                String response = new String();
                switch (splited[0]) {
                    case "register": {
                        response = commands.register(splited[1]);
                        break;
                    }
                    case "login": {
                        response = commands.login(splited[1]);
                        if (response.equals("Logged in!") == true) {
                            name = splited[1];
                            commands.setOwnerName(splited[1]);
                        }
                        break;
                    }
                    case "friend": {
                        response = "";
                        int i = 0;
                        String[] names = splited[1].split("\\s+");
                        boolean x = true;
                        while (x == true){
                            try{
                                response = response + commands.friend(names[i]) + " ";
                                i++;
                            }catch (ArrayIndexOutOfBoundsException e){
                                x = false;
                            }
                        }
                        break;
                    }
                    case "send": {
                        response = commands.send(splited[1]);
                        break;
                    }
                    case "read": {
                        response = commands.read();
                        break;
                    }
                    case "exit": {
                        exit = true;
                        response = commands.exit();
                        break;
                    }
                    case "stop": {
                        response = "Server closed";
                        try {
                            serverSocket.close();
                        } catch (IOException e) { System.err.println (e); }
                        break;
                    }
                    default: {
                        response = "Invalid command!";
                    }
                }


                // Send the response to the oputput stream: server → client
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println(response);
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("Communication error... " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) { System.err.println (e); }
        }
    }

}
