import java.net.*;
import java.io.*;

public class WebServer {
    private static final int SOCKET_PORT = 8888; // port in which server is listening
    private BufferedReader in; // input that listens to client
    private PrintWriter textOut; // for outputting header
    private BufferedOutputStream dataOut; // for outputting body
    public Socket socket;

    public WebServer(int port) {
        try {
            // Starts web server
            ServerSocket server = new ServerSocket(SOCKET_PORT);
            System.out.println("Web Server started");
            System.out.println("listening for connections on port " + SOCKET_PORT);

            while (true) { // listens for connection until program closes

                socket = server.accept(); // waits until someone connects
                System.out.println("Connection opened from: " + socket);

                // takes input and output from the client socket
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                textOut = new PrintWriter(socket.getOutputStream());
                dataOut = new BufferedOutputStream(socket.getOutputStream());

                // gives connection to connection handler and then comes back to line 20 to
                // listen further connection
                ConnectionManager connectionManager = new ConnectionManager(socket, in, textOut, dataOut);
                connectionManager.start(); // starts new thread
            }
        } catch (IOException e) {
            System.err.println("Connection error");
            // close connection
            try {
                //server.close();
                socket.close();
                in.close();
                textOut.close();
                dataOut.close();
            } catch (IOException ex) {
                System.err.println("Closing connection error");
            }
        }
    }

    public static void main(String[] args) throws IOException {

        new WebServer(SOCKET_PORT);

    }
}
