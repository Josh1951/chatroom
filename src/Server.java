import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ArrayList<String> userNames = new ArrayList<>();
    private ArrayList<ServerThread> serverThreads = new ArrayList<>();

    public static void main(String[] args) {
        //Instantiate the server
        new Server().start();
    }

    public void start() {
        try
        {
            //Open server socket
            ServerSocket serverSocket = new ServerSocket(4444);
            System.out.println("Server waiting for requests...");

            while (true) {
                //Create socket upon new client
                Socket socket = serverSocket.accept();
                System.out.println("Request received from client...");

                //Create new thread to handle client
                ServerThread newClient = new ServerThread(socket, this);
                serverThreads.add(newClient);

                //Run client thread
                System.out.println(newClient + "connected");
                newClient.start();
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //Method to broadcast message to all connected clients
    void broadcast(String message)
    {
        for (ServerThread client : serverThreads) {
            client.sendMessage(message);
        }
    }

    //Method to add clients to list - called from ServerThread
    void addClient(String userName)
    {
        userNames.add(userName);
    }

    //Method clients closing connection
    void removeClient(String userName, ServerThread client) {

            serverThreads.remove(client);
            System.out.println(client + "has left the server");

    }

    //Getter for ServerThread class to get list of active clients
    ArrayList<String> getUserNames() {
        return this.userNames;
    }
}