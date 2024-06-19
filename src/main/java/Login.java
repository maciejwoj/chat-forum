import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Login extends JFrame {
    private JTextField usernameField;
    private JTextField topicField;
    private JPanel mainPanel;

    public Login() {
        setTitle("Login");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon.png"))).getImage());

        add(createLoginPanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(60, 63, 65));
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Chat Application");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(createLabel("Username:"), gbc);

        usernameField = new JTextField();
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(createLabel("Topic:"), gbc);

        topicField = new JTextField();
        gbc.gridx = 1;
        mainPanel.add(topicField, gbc);

        JButton loginButton = createButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String topic = topicField.getText();
                if (!username.isEmpty() && !topic.isEmpty()) {
                    new Chat(topic, username).setVisible(true);
//                    dispose();
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Username and topic cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return mainPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(75, 110, 175));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }


}
