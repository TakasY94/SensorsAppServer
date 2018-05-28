import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.*;

public class ConnectionWorker implements Runnable {

    //Socket for exchange data with client
    private Socket clientSocket = null;
    private Parametres parametres ;

    private InputStream inputStream = null;
    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

    public static final String URL = "jdbc:mysql://localhost:3306/sensorsappdb?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String USER = "root";
    public static final String PASSWORD = "root";

    public ConnectionWorker (Socket socket) throws IOException {
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



        while (true) {
            try {
                parametres = (Parametres) objectInputStream.readObject();

                if (parametres != null) {
                    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); Statement statement = connection.createStatement();) {
                        //Create statement here
                        int n = 0;
                        int k = 0;

                        // TODO Разобраться в ошибке вывода данных (То ли они приходят некорректные, то ли логика вычленения нарушена)
                        System.out.println("Test");

                    } catch (SQLException e) {
                        e.printStackTrace();
                      }
                    System.out.println("X - " + parametres.getAccX() + "Y - " + parametres.getAccY() + "Z - " + parametres.getAccZ());
                } else
                    if (parametres == null) {
                        System.out.println("Object null! Close socket");
                        clientSocket.close();
                        break;
                    }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
              catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
