import javax.swing.*;

public class ChatRoomView extends JPanel {



    private JTextArea incoming;
    private JTextField outgoing;
    
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

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel textAndButtonPanel = new JPanel();
        textAndButtonPanel.add(outgoing);
        textAndButtonPanel.add(sendButton);
        this.add(scroller);
        this.add(textAndButtonPanel);
    }

    public JTextArea getIncoming() {
        return incoming;
    }

}
