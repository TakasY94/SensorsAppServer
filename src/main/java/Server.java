import java.io.IOException;
import java.net.ServerSocket;

public class Server implements Runnable {

    private static volatile Server instance = null;

    private final int SERVER_PORT = 2666;
    private ServerSocket serverSocket = null;

    private Server() {}

    //Singletone lazy initialization
    public static Server getServer() {
        if (instance == null) {
            synchronized (Server.class) {
                if (instance == null) {
                    instance = new Server();
                }
            }
        } return instance;
    }


    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Start server on port" + SERVER_PORT);

            while (true) {
                ConnectionWorker worker = null;

                try {
                    worker = new ConnectionWorker(serverSocket.accept());
                    System.out.println("Get connection with client");

                    Thread thread = new Thread(worker);
                    thread.start();
                }catch (Exception e){
                    System.out.println("Connection error" + e.getMessage());
                }
            }
        }catch (IOException e) {
            System.out.println("Can't start server on port" + SERVER_PORT + " " + e.getMessage());
        }finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                }catch (IOException e){
                    System.out.println("Can't close connection " + e.getMessage());
                }
            }
        }
    }
}
