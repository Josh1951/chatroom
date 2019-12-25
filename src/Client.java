import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {

        String name;
        Scanner scn;
        BufferedReader input; // Declare BufferedReader for incoming data
        PrintWriter output; // Declare Printwiter for outgoing data



        Socket socket = new Socket("localhost", 4444);

        //Input stream from server
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //Output stream from client
        output = new PrintWriter(socket.getOutputStream(), true);
        //User Input
        scn = new Scanner(System.in);

        //Take username and send to server
        System.out.println("Enter your name:");
        name = scn.nextLine();
        output.println(name);

        //Thread to handle outgoing data
        Thread send = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    String message = scn.nextLine();
                    output.println(message);
                    System.out.println(message);
                }
            }
        });

        //Thread to handle incoming data
        Thread recieve = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        String message = input.readLine();
                        System.out.println(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        send.start();
        recieve.start();
    }
}
