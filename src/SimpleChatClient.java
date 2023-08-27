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

    private JFrame frame;
    private JPanel loginPanel;
    private JTextField nicknameField;

    public void go() {
        setUpNetworking();

        frame = new JFrame("Simple Chat Client");
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginPanel = new JPanel();
        JButton loginButton = new JButton("Enter chat");
        nicknameField = new JTextField(20);
        JLabel nickLabel = new JLabel("Choose your nickname");
        loginButton.addActionListener(e -> launchMainApp());

        loginPanel.add(nickLabel);
        loginPanel.add(nicknameField);
        loginPanel.add(loginButton);


        frame.getContentPane().add(BorderLayout.CENTER,loginPanel);

        frame.setVisible(true);


        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new IncomingReader());


    }

    private void launchMainApp() {
        if (!nicknameField.getText().equals("")) {
            frame.getContentPane().remove(loginPanel);
            frame.getContentPane().add(BorderLayout.CENTER,createMainPanel());
            frame.revalidate();
            frame.repaint();
        }
    }

    private JPanel createMainPanel() {
        JScrollPane scroller = createScrollableTextArea();
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel textAndButtonPanel = new JPanel();
        textAndButtonPanel.add(outgoing);
        textAndButtonPanel.add(sendButton);
        mainPanel.add(scroller);
        mainPanel.add(textAndButtonPanel);


        return mainPanel;
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
