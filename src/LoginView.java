import javax.swing.*;

public class LoginView extends JPanel {


    private JTextField nicknameField;
    private SimpleChatClient simpleChatClient;
    public LoginView(SimpleChatClient simpleChatClient)  {
        this.simpleChatClient = simpleChatClient;
        JButton loginButton = new JButton("Enter chat");
        nicknameField = new JTextField(20);
        JLabel nickLabel = new JLabel("Choose your nickname");
        loginButton.addActionListener(e -> simpleChatClient.loginMessage(nicknameField.getText()));

        this.add(nickLabel);
        this.add(nicknameField);
        this.add(loginButton);
    }

}
