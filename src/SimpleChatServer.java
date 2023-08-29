import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleChatServer {
    private final List<ObjectOutputStream> clientWriters = new ArrayList<>();

    public static void main(String[] args) {
        new SimpleChatServer().go();
    }

    public void go() {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()){
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8020));

            while(serverSocketChannel.isOpen()) {
                SocketChannel clientSocket = serverSocketChannel.accept();
                ObjectOutputStream writer = new ObjectOutputStream(clientSocket.socket().getOutputStream());
                clientWriters.add(writer);
                threadPool.submit(new ClientHandler(clientSocket));
                System.out.println("got a connection");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void tellEveryone(Message message) {
        for (ObjectOutputStream writer : clientWriters) {
            try {
                writer.writeObject(message);
                writer.flush();
            } catch (IOException e) {
                System.out.println("Writing to clients failed.");
                e.printStackTrace();
            }

        }
    }

    public class ClientHandler implements Runnable {

        final SocketChannel socket;
        ObjectInputStream inputStream;

        private String username;

        public static List<String> clients = new CopyOnWriteArrayList<>();

        public ClientHandler(SocketChannel clientSocket) {
            socket = clientSocket;

            try {
                inputStream = new ObjectInputStream(socket.socket().getInputStream());
            } catch (IOException e) {
                System.out.println("Error creating inputstream");
                e.printStackTrace();
            }

            //this.username = username;
        }

        public void run() {

            Message message;

            try {
                while(true) {

                    message = (Message) inputStream.readObject();
                    if (message.getType().equals("USERS_UPDATE")) {
                        this.username = message.getMessage();
                        clients.add(username);
                        tellEveryone(new Message("USERS_UPDATE", clients));
                        tellEveryone(new Message("MESSAGE",username + " has entered the chat!"));
                    } else if (message.getType().equals("MESSAGE")) {
                        System.out.println("read " + message);
                        tellEveryone(new Message("MESSAGE", username +": " +message.getMessage()));
                    }

                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
                clients.remove(username);
                tellEveryone(new Message("USERS_UPDATE", clients));
            }
        }
    }
}
