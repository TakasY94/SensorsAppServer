import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ConnectionWorker implements Runnable{

    //Сокет через который обменваемся днными с клиентом
    private Socket clientSocket = null;

    private InputStream inputStream = null;

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

        byte[] buffer = new byte[1024*24];

        while (true) {
            try {
                int count = inputStream.read(buffer, 0, buffer.length);

                if (count > 0) {
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
