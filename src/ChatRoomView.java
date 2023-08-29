import javax.swing.*;
import java.awt.*;

public class ChatRoomView extends JPanel {

    private final JTextArea incoming;
    private final JTextField outgoing;
    private final JList<String> activeUsers;

    public ChatRoomView(SimpleChatClient simpleChatClient) {
        this.setBackground(new Color(32, 34, 38));
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> simpleChatClient.sendMessage(outgoing));

        incoming = new JTextArea(15, 30);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        incoming.setBackground(new Color(42,46,54));
        incoming.setForeground(new Color(230,230,230));
        JScrollPane scroller = new JScrollPane(incoming);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        activeUsers = new JList<>();


        JPanel chatView = new JPanel();
        chatView.setLayout(new BorderLayout());

        JPanel textAndButtonPanel = new JPanel();
        textAndButtonPanel.add(outgoing);
        textAndButtonPanel.add(sendButton);
        textAndButtonPanel.setBackground(new Color(32, 34, 38));

        chatView.add(scroller, BorderLayout.CENTER);
        chatView.add(textAndButtonPanel, BorderLayout.SOUTH);

        JPanel userView = new JPanel();
        userView.setBackground(new Color(32, 34, 38));
        userView.add(activeUsers);

        setLayout(new BorderLayout());
        add(chatView, BorderLayout.CENTER);
        add(userView, BorderLayout.EAST);
    }

    public JTextArea getIncoming() {
        return incoming;
    }

    public JList<String> getActiveUsers() {
        return activeUsers;
    }
}
