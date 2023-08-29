import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleChatClient {


    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    private JFrame frame;
    private LoginView loginPanel;
    private ChatRoomView mainPanel = new ChatRoomView(this);


    public void go() {
        setUpNetworking();

        frame = new JFrame("Simple Chat Client");
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginPanel = new LoginView(this);

        frame.getContentPane().add(BorderLayout.CENTER,loginPanel);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println("UImanager error");
            e.printStackTrace();
        }

        frame.setVisible(true);


        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new IncomingReader());


    }

    public void launchMainApp() {

            frame.getContentPane().remove(loginPanel);
            frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
            frame.revalidate();
            frame.repaint();

    }


    private void setUpNetworking() {

        try {
            InetSocketAddress serverAdress = new InetSocketAddress("127.0.0.1",8020);
            SocketChannel socketChannel = SocketChannel.open(serverAdress);
            reader = new ObjectInputStream(socketChannel.socket().getInputStream());
            writer = new ObjectOutputStream(socketChannel.socket().getOutputStream());

            System.out.println("Networking established");
        } catch (IOException e) {
            e.printStackTrace();
        }

        }

        public void loginMessage(String username) {

        try {
            writer.writeObject(new Message("USERS_UPDATE", username));
            writer.flush();
            if (!username.equals("")) {
                launchMainApp();
            } else {
                JOptionPane.showMessageDialog(null, "Nickname cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException e) {
            System.out.println("login message failed");
            e.printStackTrace();
        }

        }

        public void sendMessage(JTextField outgoing) {
        try {
            writer.writeObject(new Message("MESSAGE",outgoing.getText()));
            writer.flush();
        } catch (IOException e) {
            System.out.println("Sending message failed.");
            e.printStackTrace();
        }

        outgoing.setText("");
        outgoing.requestFocus();

    }

    public class IncomingReader implements Runnable {
        public void run() {
            Message message;
            try {
                while ((message = (Message) reader.readObject()) != null) {
                    System.out.println("received message");
                    if (message.getType().equals("USERS_UPDATE")) {
                        System.out.println(message.getConnectedClients());
                        mainPanel.getActiveUsers().setListData(message.getConnectedClients().toArray(new String[0]));
                    } else if (message.getType().equals("MESSAGE")){
                        mainPanel.getIncoming().append(message.getMessage() + "\n");
                    }

                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new SimpleChatClient().go();
    }

}
