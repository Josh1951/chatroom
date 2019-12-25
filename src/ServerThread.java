import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket socket;
    private Server server;
    private PrintWriter writer;

    public ServerThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {

        String serverMessage;
        String clientMessage;

        try {
            //Get input
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            //Get Output
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            //Send list of connected users, if any
            if (server.getUserNames() != null) {
                writer.println("connected clients: " + server.getUserNames());
            }

            //Take username add to list
            String userName = reader.readLine();
            server.addClient(userName);

            //Broadcast to connected clients new client has joined the server
            serverMessage = userName + " Joined the server";
            server.broadcast(serverMessage);
            server.broadcast("Weclome to the chatroom " + userName + ". Enter your messages into the console. send 'logout' to logout. ");

            //Take message and broadcast to all connected clients
            while(true) {
                clientMessage = reader.readLine();
                serverMessage = userName + " : " + clientMessage;
                if (clientMessage == "logout") {
                    server.removeClient(userName, this);
                } else {
                    server.broadcast(serverMessage);
                }



            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String message) {
        writer.println(message);
    }

}
