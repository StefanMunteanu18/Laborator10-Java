import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Server {
    // Define the port on which the server is listening
    public static final int PORT = 8100;
    public Server() throws IOException {
        ServerSocket serverSocket = null;
        HashMap <String, Client> clients = new HashMap<>();
        ArrayList<Thread> threads = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                System.out.println ("Waiting for a client ...");
                serverSocket.setSoTimeout(10000);
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(10000);
                System.out.println("Received client");
                Thread thr = new ClientHandlerThread(socket, clients, serverSocket);
                thr.start();
                threads.add(thr);
            }
        } catch (IOException e) {
            System.out.println("Server closed!" + e);
        }
        finally {
            for(Thread thr : threads){
                try {
                    thr.join();
                }catch (InterruptedException e){
                    System.out.println(e);
                }
            }
            serverSocket.close();
        }
    }


}
