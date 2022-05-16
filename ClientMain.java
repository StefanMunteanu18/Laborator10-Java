import com.sun.source.tree.CompoundAssignmentTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main (String[] args) throws IOException {
        String serverAddress = "127.0.0.1"; // The server's IP address
        int PORT = 8100; // The server's port
        try (
                Socket socket = new Socket(serverAddress, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader (new InputStreamReader(  socket.getInputStream())) ) {
            Scanner scan = new Scanner(System.in);
            // Send a request to the server
            boolean close = false;
            while(close == false) {
                String command = scan.nextLine();
                String request = command;
                out.println(request);
                // Wait the response from the server ("Hello World!")
                try{String response = in.readLine();
                    if(response.equals("exit") == true){
                        close = true;
                    }
                    System.out.println(response);
                }catch (NullPointerException e){
                    close = true;
                    System.out.println("Server is closed");
                }catch (SocketException e){
                    System.out.println("Inactive for too long, connection time out!");
                    close = true;
                }
            }
        } catch (ConnectException e) {
            System.out.println("No server listening... " + e);
        }
    }
}
