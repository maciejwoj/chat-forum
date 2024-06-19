import org.apache.kafka.clients.producer.ProducerRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;

public class Chat extends JFrame {
    private JTextArea chatView;
    private JPanel mainPanel;
    private JTextField message;
    private JButton sendButton;
    private JButton logoutButton;
    private JList<String> userList;

    private final MessageConsumer messageConsumer;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final String username;
    private final String topic;
    private boolean running = true;

    public Chat(String topic, String username) throws HeadlessException {
        this.messageConsumer = new MessageConsumer(topic, username);
        this.username = username;
        this.topic = topic;

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle(topic + " - " + username);
        this.setSize(800, 600);

        initializeComponents();

        Executors.newSingleThreadExecutor().execute(() -> {
            while (running) {
                messageConsumer.kafkaConsumer.poll(java.time.Duration.ofSeconds(1)).forEach(
                        m -> {
                            chatView.append(m.value() + System.lineSeparator());
                            updateUsersList(m.value());
                        }
                );
            }
            messageConsumer.kafkaConsumer.close();
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = message.getText();
                String formattedMessage = String.format("%s [%s]: %s",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                        username, text);
                MessageProducer.send(new ProducerRecord<>(topic, formattedMessage));
                message.setText("");
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                running = false;
                dispose();
                new Login();
            }
        });
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(60, 63, 65));

        chatView = new JTextArea();
        chatView.setEditable(false);
        chatView.setFont(new Font("Arial", Font.PLAIN, 14));
        chatView.setBackground(new Color(43, 43, 43));
        chatView.setForeground(Color.WHITE);
        JScrollPane chatScrollPane = new JScrollPane(chatView);
        mainPanel.add(chatScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(new Color(43, 43, 43));
        message = new JTextField();
        message.setFont(new Font("Arial", Font.PLAIN, 14));
        message.setBackground(new Color(69, 73, 74));
        message.setForeground(Color.WHITE);
        inputPanel.add(message, BorderLayout.CENTER);

        sendButton = createButton("Send");
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(75, 110, 175));
        logoutButton = createButton("Logout");
        topPanel.add(logoutButton, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        userList = new JList<>(listModel);
        userList.setBackground(new Color(60, 63, 65));
        userList.setForeground(Color.WHITE);
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(150, 0));
        mainPanel.add(userScrollPane, BorderLayout.WEST);

        this.add(mainPanel);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(75, 110, 175));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void updateUsersList(String message) {
        String newUser = message.split(" ")[1].replace(":", "").replace("[", "").replace("]", "");
        if (!listModel.contains(newUser)) {
            listModel.addElement(newUser);
        }
    }
}
