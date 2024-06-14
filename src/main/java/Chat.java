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

    private final MessageConsumer messageConsumer;
    private final String id;
    private final String topic;

    public Chat(String topic, String id) throws HeadlessException {
        this.id = id;
        this.topic = topic;
        this.messageConsumer = new MessageConsumer(topic, id);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(createMainPanel());
        this.setVisible(true);
        this.setTitle(topic + "  - " +  id);
        this.setSize(500, 400);

        Executors.newSingleThreadExecutor().execute(() -> {
            while (true) {
                messageConsumer.kafkaConsumer.poll(java.time.Duration.ofSeconds(1)).forEach(
                        m -> {
                            chatView.append(m.value() + System.lineSeparator());
                        }
                );
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = message.getText();
                String formattedMessage = String.format("%s [%s]: %s",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                        id, text);
                MessageProducer.send(new ProducerRecord<>(topic, formattedMessage));
                message.setText("");
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private JPanel createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        chatView = new JTextArea();
        chatView.setEditable(false);
        chatView.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatView);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.LIGHT_GRAY);
        message = new JTextField();
        message.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(message, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.setBackground(Color.DARK_GRAY);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false); // usuwa efekt focusu
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.GRAY);
        logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false); // usuwa efekt focusu
        topPanel.add(logoutButton, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

}
