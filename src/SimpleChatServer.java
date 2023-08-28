import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleChatServer {
    private final List<PrintWriter> clientWriters = new ArrayList<>();

    public static void main(String[] args) {
        new SimpleChatServer().go();
    }

    public void go() {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()){
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8020));

            while(serverSocketChannel.isOpen()) {
                SocketChannel clientSocket = serverSocketChannel.accept();
                PrintWriter writer = new PrintWriter(Channels.newWriter(clientSocket, StandardCharsets.UTF_8));
                clientWriters.add(writer);
                threadPool.submit(new ClientHandler(clientSocket));
                System.out.println("got a connection");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tellEveryone(ClientHandler user, String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(user.getUsername() + ": " + message);
            writer.flush();
        }
    }

    private void tellEveryone(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
            writer.flush();
        }
    }

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        SocketChannel socket;

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        private String username;

        public static List<ClientHandler> clients = new CopyOnWriteArrayList<>();

        public ClientHandler(SocketChannel clientSocket) {
            socket = clientSocket;
            reader = new BufferedReader(Channels.newReader(socket, StandardCharsets.UTF_8));
            this.username = username;
        }

        public void run() {
            clients.add(this);
            String message;
            try {
                while( (message = reader.readLine()) != null) {
                    if (message.startsWith("New login: ")) {
                        this.username = message.substring(10);
                        tellEveryone(username + " has entered the chat!");
                    } else {
                        System.out.println("read " + message);
                        tellEveryone(this,message);
                    }

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
