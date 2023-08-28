import javax.swing.*;
import java.awt.*;

public class ChatRoomView extends JPanel {



    private JTextArea incoming;
    private JTextField outgoing;

    private JList<String> activeUsers;
    
    public ChatRoomView(SimpleChatClient simpleChatClient) {
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> simpleChatClient.sendMessage(outgoing));

        incoming = new JTextArea(15,30);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane scroller = new JScrollPane(incoming);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


        activeUsers = new JList<>();

        JPanel chatView = new JPanel();
        JPanel userView = new JPanel();
        JPanel textAndButtonPanel = new JPanel();

        chatView.setLayout(new BoxLayout(chatView,BoxLayout.Y_AXIS));


        textAndButtonPanel.add(outgoing);
        textAndButtonPanel.add(sendButton);
        chatView.add(scroller);
        chatView.add(textAndButtonPanel);
        userView.add(activeUsers);
        this.add(chatView, BorderLayout.CENTER);
        this.add(userView, BorderLayout.EAST);


    }

    public JTextArea getIncoming() {
        return incoming;
    }

    public JList<String> getActiveUsers() {
        return activeUsers;
    }
}
