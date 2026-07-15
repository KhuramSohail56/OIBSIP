import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class NumberGuessingGameGUI extends JFrame {
    private int randomNumber;
    private int attemptsLeft = 7;
    private int totalRounds = 1;
    private int totalWon = 0;
    
    private JTextField guessField;
    private JButton guessButton;
    private JLabel feedbackLabel;
    private JLabel attemptsLabel;
    private JProgressBar healthBar;
    private JLabel scoreLabel;
    private JButton playAgainButton;
    private JPanel cardPanel;
    
    private Timer celebrationTimer;
    private ArrayList<Particle> confettiList = new ArrayList<>();
    private boolean isCelebrating = false;

    public NumberGuessingGameGUI() {
        setTitle("Predictive - Premium Cyber Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 550);
        setMinimumSize(new Dimension(450, 550));
        setLocationRelativeTo(null);
        setResizable(true);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        startNewGame();
        initGUI();
    }

    private void startNewGame() {
        randomNumber = new Random().nextInt(100) + 1;
        attemptsLeft = 7;
        isCelebrating = false;
        confettiList.clear();
        if (celebrationTimer != null) {
            celebrationTimer.stop();
        }
    }

    private void initGUI() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gp = new GradientPaint(0, 0, new Color(15, 12, 30), 0, getHeight(), new Color(7, 12, 26));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                if (isCelebrating) {
                    for (int i = 0; i < confettiList.size(); i++) {
                        Particle p = confettiList.get(i);
                        g2d.setColor(p.color);
                        g2d.fillRoundRect((int)p.x, (int)p.y, p.size, p.size + 5, 2, 2);
                    }
                }
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel centralContainer = new JPanel();
        centralContainer.setOpaque(false);
        centralContainer.setLayout(new BorderLayout(0, 15));
        centralContainer.setPreferredSize(new Dimension(380, 490));

        JLabel titleLabel = new JLabel("Predictive", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(new Color(240, 240, 255));
        
        JLabel subTitleLabel = new JLabel("Guess the secret number between 1 & 100", SwingConstants.CENTER);
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitleLabel.setForeground(new Color(140, 145, 175));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        headerPanel.setOpaque(false);
        headerPanel.add(titleLabel);
        headerPanel.add(subTitleLabel);
        centralContainer.add(headerPanel, BorderLayout.NORTH);

        cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 10)); 
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 25);
                g2d.setColor(new Color(255, 255, 255, 22));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 25);
                g2d.dispose();
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new EmptyBorder(30, 25, 30, 25));

        guessField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isFocusOwner() ? new Color(0, 210, 255) : new Color(100, 110, 225));
                g2.fillRect(0, getHeight() - 3, getWidth(), 3); 
                g2.dispose();
            }
        };
        guessField.setFont(new Font("Segoe UI", Font.BOLD, 26));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setForeground(Color.WHITE);
        guessField.setCaretColor(Color.CYAN);
        guessField.setOpaque(false);
        guessField.setBorder(BorderFactory.createEmptyBorder(5, 5, 8, 5));
        guessField.setMaximumSize(new Dimension(280, 50));
        guessField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        guessField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || guessField.getText().length() >= 3) {
                    e.consume();
                }
            }
        });
        guessField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { repaint(); }
            public void focusLost(FocusEvent e) { repaint(); }
        });

        guessButton = new JButton("Submit Guess") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(75, 65, 175));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(125, 110, 255));
                } else {
                    g2d.setColor(new Color(100, 85, 230));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        guessButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        guessButton.setForeground(Color.WHITE);
        guessButton.setContentAreaFilled(false);
        guessButton.setBorderPainted(false);
        guessButton.setFocusPainted(false);
        guessButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        guessButton.setMaximumSize(new Dimension(280, 45));
        guessButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        healthBar = new JProgressBar(0, 7);
        healthBar.setValue(7);
        healthBar.setForeground(new Color(46, 204, 113));
        healthBar.setBackground(new Color(255, 255, 255, 15));
        healthBar.setBorderPainted(false);
        healthBar.setMaximumSize(new Dimension(280, 8));
        healthBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        feedbackLabel = new JLabel("Enter a number to begin guessing!", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        feedbackLabel.setForeground(new Color(190, 200, 220));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        attemptsLabel = new JLabel("Attempts Left: 7", SwingConstants.CENTER);
        attemptsLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        attemptsLabel.setForeground(new Color(140, 150, 180));
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(guessField);
        cardPanel.add(Box.createVerticalStrut(25));
        cardPanel.add(guessButton);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(healthBar);
        cardPanel.add(Box.createVerticalStrut(25));
        cardPanel.add(feedbackLabel);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(attemptsLabel);
        centralContainer.add(cardPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        scoreLabel = new JLabel("Round: 1  |  Rounds Won: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        scoreLabel.setForeground(new Color(165, 175, 205));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(scoreLabel);
        bottomPanel.add(Box.createVerticalStrut(10));

        playAgainButton = new JButton("Play Next Round") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(46, 204, 113));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        playAgainButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        playAgainButton.setForeground(Color.WHITE);
        playAgainButton.setContentAreaFilled(false);
        playAgainButton.setBorderPainted(false);
        playAgainButton.setFocusPainted(false);
        playAgainButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playAgainButton.setMaximumSize(new Dimension(280, 40));
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.setVisible(false);
        bottomPanel.add(playAgainButton);

        centralContainer.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(centralContainer);
        add(mainPanel);

        guessButton.addActionListener(e -> checkUserGuess());
        guessField.addActionListener(e -> checkUserGuess());
        playAgainButton.addActionListener(e -> resetGameScreen());
    }

    private void checkUserGuess() {
        String inputText = guessField.getText().trim();
        if (inputText.isEmpty()) {
            triggerVisualShake();
            feedbackLabel.setText("Please enter a number first!");
            feedbackLabel.setForeground(new Color(241, 196, 15));
            return;
        }

        int userGuess = Integer.parseInt(inputText);
        attemptsLeft--;
        healthBar.setValue(attemptsLeft);

        if (attemptsLeft > 4) {
            healthBar.setForeground(new Color(46, 204, 113));
        } else if (attemptsLeft > 2) {
            healthBar.setForeground(new Color(230, 126, 34));
        } else {
            healthBar.setForeground(new Color(231, 76, 60));
        }

        attemptsLabel.setText("Attempts Left: " + attemptsLeft);

        if (userGuess == randomNumber) {
            totalWon++;
            feedbackLabel.setText("BRAVO! You guessed the number!");
            feedbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            feedbackLabel.setForeground(new Color(46, 204, 113));
            triggerCelebration(); 
            endRoundState();
        } else if (attemptsLeft <= 0) {
            feedbackLabel.setText("Game Over! The number was: " + randomNumber);
            feedbackLabel.setForeground(new Color(231, 76, 60));
            triggerVisualShake(); 
            endRoundState();
        } else if (userGuess > randomNumber) {
            feedbackLabel.setText("Too High! Guess lower.");
            feedbackLabel.setForeground(new Color(205, 215, 235));
        } else {
            feedbackLabel.setText("Too Low! Guess higher.");
            feedbackLabel.setForeground(new Color(205, 215, 235));
        }

        guessField.setText("");
        guessField.requestFocus();
    }

    private void endRoundState() {
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
        playAgainButton.setVisible(true);
        scoreLabel.setText("Round: " + totalRounds + "  |  Rounds Won: " + totalWon);
        cardPanel.revalidate(); 
    }

    private void resetGameScreen() {
        totalRounds++;
        startNewGame();
        repaint(); 
        
        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        playAgainButton.setVisible(false);
        
        healthBar.setValue(7);
        healthBar.setForeground(new Color(46, 204, 113));
        feedbackLabel.setText("Enter a number to begin guessing!");
        feedbackLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        feedbackLabel.setForeground(new Color(200, 210, 230));
        attemptsLabel.setText("Attempts Left: 7");
        scoreLabel.setText("Round: " + totalRounds + "  |  Rounds Won: " + totalWon);
        
        guessField.setText("");
        guessField.requestFocus();
    }

    private void triggerCelebration() {
        isCelebrating = true;
        confettiList.clear();
        Color[] colors = {Color.RED, Color.GREEN, Color.CYAN, Color.YELLOW, Color.PINK, Color.ORANGE};
        Random rand = new Random();

        for (int i = 0; i < 120; i++) {
            confettiList.add(new Particle(
                rand.nextInt(getWidth() - 20),
                rand.nextInt(80) - 60, 
                colors[rand.nextInt(colors.length)],
                rand.nextInt(4) + 6,
                rand.nextFloat() * 4 + 2, 
                (rand.nextFloat() - 0.5f) * 2.5f
            ));
        }

        celebrationTimer = new Timer(16, e -> {
            boolean keepAnimating = false;
            for (Particle p : confettiList) {
                p.y += p.speedY;
                p.x += p.speedX;
                if (p.y < getHeight()) {
                    keepAnimating = true;
                }
            }
            repaint();
            if (!keepAnimating) {
                celebrationTimer.stop();
                isCelebrating = false;
            }
        });
        celebrationTimer.start();
    }

    private void triggerVisualShake() {
        final Point originalLocation = cardPanel.getLocation();
        Timer timer = new Timer(20, new ActionListener() {
            int count = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count >= 12) {
                    cardPanel.setLocation(originalLocation);
                    ((Timer)e.getSource()).stop();
                } else {
                    int offset = (count % 2 == 0) ? 7 : -7;
                    cardPanel.setLocation(originalLocation.x + offset, originalLocation.y);
                    count++;
                }
            }
        });
        timer.start();
    }

    private static class Particle {
        float x, y;
        Color color;
        int size;
        float speedY;
        float speedX;

        Particle(float x, float y, Color color, int size, float speedY, float speedX) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.size = size;
            this.speedY = speedY;
            this.speedX = speedX;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NumberGuessingGameGUI().setVisible(true);
        });
    }
}
