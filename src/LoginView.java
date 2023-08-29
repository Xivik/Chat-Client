import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {


    private final JTextField nicknameField;

    public LoginView(SimpleChatClient simpleChatClient)  {

        this.setBackground(new Color(32,34,38));
        JButton loginButton = new JButton("Enter chat");
        nicknameField = new JTextField(20);
        JLabel nickLabel = new JLabel("Choose your nickname");
        nickLabel.setForeground(new Color(230,230,230));
        loginButton.addActionListener(e -> simpleChatClient.loginMessage(nicknameField.getText()));

        this.add(nickLabel);
        this.add(nicknameField);
        this.add(loginButton);
    }

}
