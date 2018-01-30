import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.*;

public class ConnectionWorker implements Runnable{

    //Сокет через который обменваемся днными с клиентом
    private Socket clientSocket = null;

    private InputStream inputStream = null;

    public static final String URL = "jdbc:mysql://localhost:3306/sensorsappdb?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String USER = "root";
    public static final String PASSWORD = "root";

    public ConnectionWorker (Socket socket) {
        clientSocket = socket;
    }



    @Override
    public void run() {
        try {
            inputStream = clientSocket.getInputStream();
        }catch (IOException e) {
            System.out.println("Can't get input stream");
        }

        //Creating connection with DB
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
        }
        catch (SQLException e) {
            System.out.println("Driver not registered");
        }

        byte[] buffer = new byte[1024*24];
        String[] list = new String[5];

        while (true) {
            try {
                int count = inputStream.read(buffer, 0, buffer.length);
                StringBuilder stringBuilder = new StringBuilder(new String(buffer, 0, count));
                int n = 0;
                int k = 0;

                for (int i = 0; i < 4; i++) {
                    k = stringBuilder.indexOf(" ", n);
                    list[i] = stringBuilder.substring(n, k);
                    n = k++;
                }


                for (String e: list) {
                    System.out.print(e);
                }

                if (count > 0) {
                    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); Statement statement = connection.createStatement();) {

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println(new String(buffer, 0, count));
                } else
                    if (count == -1) {
                        System.out.println("close socket");
                        clientSocket.close();
                        break;
                    }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
