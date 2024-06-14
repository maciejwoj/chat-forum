import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Login extends JFrame {
    private JTextField usernameField;
    private JTextField topicField;
    private JButton loginButton;
    private JPanel mainPanel;

    public Login() {
        setTitle("Login");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon.png"))).getImage());

        add(createLoginPanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        mainPanel = new JPanel(new GridLayout(3, 2));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        mainPanel.add(usernameField);

        mainPanel.add(new JLabel("Topic:"));
        topicField = new JTextField();
        mainPanel.add(topicField);

        loginButton = new JButton("Login");
        loginButton.setBackground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        mainPanel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String topic = topicField.getText();
                if (!username.isEmpty() && !topic.isEmpty()) {
                    new Chat(topic, username).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Username and topic cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return mainPanel;
    }

}
