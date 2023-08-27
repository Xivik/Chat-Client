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

    private JTextArea incoming;
    private JTextField outgoing;
    private BufferedReader reader;
    private PrintWriter writer;

    public void go() {
        setUpNetworking();

        JScrollPane scroller = createScrollableTextArea();
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());

        JPanel mainPanel = new JPanel();
        mainPanel.add(scroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new IncomingReader());

        JFrame frame = new JFrame("Simple Chat Client");
        frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
        frame.setSize(400,350);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JScrollPane createScrollableTextArea() {
        incoming = new JTextArea(15,30);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane scroller = new JScrollPane(incoming);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        return scroller;
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

        private void sendMessage() {
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
                    incoming.append(message + "\n");
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
