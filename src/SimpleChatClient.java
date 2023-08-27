import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleChatClient {


    private BufferedReader reader;
    private PrintWriter writer;

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
        frame.setVisible(true);


        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new IncomingReader());


    }

    public void launchMainApp(String nicknameField) {
        if (!nicknameField.equals("")) {
            frame.getContentPane().remove(loginPanel);
            frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
            frame.revalidate();
            frame.repaint();
        }
    }


    private void setUpNetworking() {

        try {
            InetSocketAddress serverAdress = new InetSocketAddress("127.0.0.1",8020);
            SocketChannel socketChannel = SocketChannel.open(serverAdress);
            reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
            writer = new PrintWriter(Channels.newWriter(socketChannel,StandardCharsets.UTF_8));

            System.out.println("Networking established");
        } catch (IOException e) {
            e.printStackTrace();
        }

        }

        public void sendMessage(JTextField outgoing) {
        writer.println(outgoing.getText());
        writer.flush();
        outgoing.setText("");
        outgoing.requestFocus();

    }

    public class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
                    mainPanel.getIncoming().append(message + "\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new SimpleChatClient().go();
    }

}
