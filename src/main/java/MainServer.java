
import java.sql.*;

public class MainServer {




    public static void main (String[] args) {


        Server server = Server.getServer();
        Thread t = new Thread(server);
        t.start();
    }
}
