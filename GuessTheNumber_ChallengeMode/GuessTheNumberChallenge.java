// Enhanced Guess the Number Game with Challenge Mode, Power-Ups, Analytics, and Customization
// Features: 
// 1. Challenge Mode - Progressive difficulty and timed rounds
// 2. Power-Ups - Reveal digit, reduce range, extra attempts
// 3. Advanced Analytics - Win/loss ratio, streaks, performance history
// 4. Achievements & Rewards - Unlockable badges, in-game currency
// 5. Visual & Audio Upgrades - Animated feedback, sound effects, themes

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.Timer;

public class GuessTheNumberChallenge {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new EnhancedGameUI();
        });
    }
}

class GameSession {
    private int targetNumber, attempts, minRange, maxRange, maxAttempts;
    private long startTime;
    private boolean gameWon;
    private List<Integer> guessHistory;
    private int powerUps;
    private int level;
    private int timeLimit;
    private Timer gameTimer;
    private boolean isTimeUp;

    public GameSession(int minRange, int maxRange, int maxAttempts) {
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.maxAttempts = maxAttempts;
        this.level = 1;
        this.timeLimit = 60; // 60 seconds for first level
        resetGame();
    }

    public void resetGame() {
        targetNumber = new Random().nextInt(maxRange - minRange + 1) + minRange;
        attempts = 0;
        gameWon = false;
        guessHistory = new ArrayList<>();
        powerUps = 3;
        startTime = System.currentTimeMillis();
        isTimeUp = false;
        
        if (gameTimer != null) {
            gameTimer.stop();
        }
        startTimer();
    }

    private void startTimer() {
        gameTimer = new Timer(1000, _ -> {
            if (getElapsedTime() >= timeLimit) {
                isTimeUp = true;
                gameTimer.stop();
            }
        });
        gameTimer.start();
    }

    public void increaseDifficulty() {
        level++;
        maxRange *= 1.5; // Increase range by 50%
        timeLimit = Math.max(30, 60 - (level * 5)); // Decrease time limit by 5 seconds per level
        maxAttempts = Math.min(15, 10 + (level / 2)); // Increase max attempts every 2 levels
    }

    public String checkGuess(int guess) {
        if (isTimeUp) {
            return "⏰ Time's up! The number was: " + targetNumber;
        }
        
        attempts++;
        guessHistory.add(guess);
        
        if (guess == targetNumber) {
            gameWon = true;
            gameTimer.stop();
            return "🎉 Correct! The number was " + targetNumber + ". Attempts: " + attempts;
        }
        
        if (attempts >= maxAttempts) {
            gameTimer.stop();
            return "❌ Game Over! The number was: " + targetNumber;
        }
        
        return guess < targetNumber ? "📉 Too low!" : "📈 Too high!";
    }

    public String usePowerUp() {
        if (powerUps <= 0) return "❌ No Power-Ups Left!";
        powerUps--;
        
        // Random power-up effect
        int powerUpType = new Random().nextInt(3);
        switch (powerUpType) {
            case 0:
                int hintDigit = targetNumber % 10;
                return "💡 Power-Up: Last digit is " + hintDigit;
            case 1:
                int rangeReduction = (maxRange - minRange) / 4;
                maxRange -= rangeReduction;
                minRange += rangeReduction;
                return "🎯 Power-Up: Range reduced! New range: " + minRange + " - " + maxRange;
            case 2:
                maxAttempts++;
                return "⏳ Power-Up: Extra attempt granted!";
            default:
                return "❌ Power-Up failed!";
        }
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getPowerUps() {
        return powerUps;
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    public long getElapsedTime() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public int getLevel() {
        return level;
    }

    public boolean isTimeUp() {
        return isTimeUp;
    }

    public int getMinRange() {
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }
}

class PlayerStats {
    private int totalGames;
    private int gamesWon;
    private int currentStreak;
    private int bestStreak;
    private int totalGuesses;
    private int coins;
    private Set<String> unlockedAchievements;

    public PlayerStats() {
        totalGames = 0;
        gamesWon = 0;
        currentStreak = 0;
        bestStreak = 0;
        totalGuesses = 0;
        coins = 0;
        unlockedAchievements = new HashSet<>();
    }

    public void updateStats(boolean won, int attempts) {
        totalGames++;
        totalGuesses += attempts;
        
        if (won) {
            gamesWon++;
            currentStreak++;
            bestStreak = Math.max(bestStreak, currentStreak);
            coins += 10; // Base reward for winning
        } else {
            currentStreak = 0;
        }

        checkAchievements();
    }

    private void checkAchievements() {
        // Check for various achievements
        if (gamesWon >= 10 && !unlockedAchievements.contains("Guess Master")) {
            unlockedAchievements.add("Guess Master");
            coins += 50;
        }
        if (bestStreak >= 5 && !unlockedAchievements.contains("Hot Streak")) {
            unlockedAchievements.add("Hot Streak");
            coins += 30;
        }
        if (totalGames >= 50 && !unlockedAchievements.contains("Veteran")) {
            unlockedAchievements.add("Veteran");
            coins += 100;
        }
    }

    public double getWinRate() {
        return totalGames == 0 ? 0 : (double) gamesWon / totalGames * 100;
    }

    public double getAverageGuesses() {
        return totalGames == 0 ? 0 : (double) totalGuesses / totalGames;
    }

    public int getCoins() {
        return coins;
    }

    public Set<String> getUnlockedAchievements() {
        return unlockedAchievements;
    }

    public int getBestStreak() {
        return bestStreak;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getGamesWon() {
        return gamesWon;
    }
}

class EnhancedGameUI extends JFrame {
    private GameSession gameSession;
    private PlayerStats playerStats;
    private JTextField guessField;
    private JLabel messageLabel, statsLabel, timerLabel;
    private JButton guessButton, powerUpButton, restartButton;
    private JProgressBar progressBar;
    private Timer updateTimer;
    private JPanel statsPanel;
    private JPanel achievementsPanel;
    private JPanel mainPanel;
    private JPanel gamePanel;
    private Color primaryColor = new Color(41, 128, 185);
    private Color secondaryColor = new Color(52, 152, 219);
    private Color accentColor = new Color(46, 204, 113);
    private Color textColor = new Color(44, 62, 80);

    public EnhancedGameUI() {
        setTitle("Number Guessing Challenge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Initialize game session and player stats
        gameSession = new GameSession(1, 100, 10);
        playerStats = new PlayerStats();
        
        // Create main panel with gradient background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, primaryColor, getWidth(), getHeight(), secondaryColor);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create game panel
        gamePanel = new JPanel();
        gamePanel.setOpaque(false);
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
        gamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create and style components
        JLabel titleLabel = new JLabel("Guess The Number");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        messageLabel = new JLabel("Enter your guess!");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        guessField = new JTextField(10);
        styleTextField(guessField);
        guessField.setMaximumSize(new Dimension(200, 40));
        guessField.setAlignmentX(Component.CENTER_ALIGNMENT);

        guessButton = createStyledButton("Guess");
        powerUpButton = createStyledButton("Use Power-Up");
        restartButton = createStyledButton("New Game");

        // Timer and progress bar
        timerLabel = new JLabel("Time: 60s");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressBar = new JProgressBar();
        styleProgressBar(progressBar);
        progressBar.setMaximumSize(new Dimension(300, 20));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to game panel
        gamePanel.add(Box.createVerticalStrut(20));
        gamePanel.add(titleLabel);
        gamePanel.add(Box.createVerticalStrut(20));
        gamePanel.add(messageLabel);
        gamePanel.add(Box.createVerticalStrut(20));
        gamePanel.add(guessField);
        gamePanel.add(Box.createVerticalStrut(20));
        gamePanel.add(guessButton);
        gamePanel.add(Box.createVerticalStrut(10));
        gamePanel.add(powerUpButton);
        gamePanel.add(Box.createVerticalStrut(10));
        gamePanel.add(restartButton);
        gamePanel.add(Box.createVerticalStrut(20));
        gamePanel.add(timerLabel);
        gamePanel.add(Box.createVerticalStrut(10));
        gamePanel.add(progressBar);

        // Create stats and achievements panels
        statsPanel = createStatsPanel();
        achievementsPanel = createAchievementsPanel();

        // Add panels to main panel
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(statsPanel, BorderLayout.EAST);
        mainPanel.add(achievementsPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Initialize game
        startNewGame();

        // Set up timer for UI updates
        updateTimer = new Timer(1000, _ -> {
            updateTimerLabel();
            updateProgressBar();
        });
        updateTimer.start();

        // Make the window visible
        setVisible(true);
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setForeground(textColor);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque()) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(getBackground());
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                }
                super.paintComponent(g);
            }
        };
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(accentColor);
        button.setPreferredSize(new Dimension(150, 40));
        button.setMaximumSize(new Dimension(150, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(_ -> {
            if (button == guessButton) handleGuess();
            else if (button == powerUpButton) handlePowerUp();
            else if (button == restartButton) startNewGame();
        });
        return button;
    }

    private void styleProgressBar(JProgressBar bar) {
        bar.setForeground(accentColor);
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createEmptyBorder());
        bar.setStringPainted(true);
        bar.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        statsLabel = new JLabel();
        statsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statsLabel.setForeground(Color.WHITE);
        panel.add(statsLabel);
        
        return panel;
    }

    private JPanel createAchievementsPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private void updateAchievementsPanel() {
        achievementsPanel.removeAll();
        for (String achievement : playerStats.getUnlockedAchievements()) {
            achievementsPanel.add(new JLabel("✨ " + achievement));
        }
        achievementsPanel.revalidate();
        achievementsPanel.repaint();
    }

    private void updateStatsPanel() {
        if (playerStats != null) {
            statsLabel.setText(String.format(
                "Games: %d | Wins: %d | Streak: %d\nWin Rate: %.1f%% | Coins: %d",
                playerStats.getTotalGames(),
                playerStats.getGamesWon(),
                playerStats.getBestStreak(),
                playerStats.getWinRate(),
                playerStats.getCoins()
            ));
        }
    }

    private void updateTimerLabel() {
        if (gameSession != null) {
            long remainingTime = gameSession.getTimeLimit() - gameSession.getElapsedTime();
            timerLabel.setText(String.format("⏱️ Time: %ds", Math.max(0, remainingTime)));
            
            if (gameSession.isTimeUp()) {
                messageLabel.setText("⏰ Time's up! Game Over!");
                updateUI();
            }
        }
    }

    private void updateProgressBar() {
        if (gameSession != null) {
            int progress = (int) ((gameSession.getElapsedTime() * 100) / gameSession.getTimeLimit());
            progressBar.setValue(progress);
            progressBar.setString(String.format("Level %d - %d%%", gameSession.getLevel(), progress));
            
            // Change progress bar color based on time remaining
            if (progress > 75) {
                progressBar.setForeground(new Color(231, 76, 60)); // Red
            } else if (progress > 50) {
                progressBar.setForeground(new Color(241, 196, 15)); // Yellow
            } else {
                progressBar.setForeground(accentColor); // Green
            }
        }
    }

    private void animateButton(JButton button) {
        final Timer[] timerRef = new Timer[1];
        timerRef[0] = new Timer(50, _ -> {
            float alpha = 1.0f - (float) 50 / 1000f;
            if (alpha <= 0) {
                timerRef[0].stop();
                button.setBackground(accentColor);
                return;
            }
            button.setBackground(new Color(
                (int) (accentColor.getRed() * alpha),
                (int) (accentColor.getGreen() * alpha),
                (int) (accentColor.getBlue() * alpha)
            ));
        });
        timerRef[0].start();
    }

    private void handleGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText());
            String feedback = gameSession.checkGuess(guess);
            
            // Animate the guess button
            animateButton(guessButton);
            
            // Add visual feedback based on the guess
            if (feedback.contains("Correct")) {
                messageLabel.setForeground(new Color(46, 204, 113)); // Green
                guessField.setBackground(new Color(46, 204, 113, 50));
            } else if (feedback.contains("Game Over")) {
                messageLabel.setForeground(new Color(231, 76, 60)); // Red
                guessField.setBackground(new Color(231, 76, 60, 50));
            } else {
                messageLabel.setForeground(Color.WHITE);
                guessField.setBackground(Color.WHITE);
            }
            
            messageLabel.setText(feedback);
            updateUI();
        } catch (NumberFormatException ex) {
            messageLabel.setForeground(new Color(231, 76, 60)); // Red
            messageLabel.setText("❌ Please enter a valid number!");
            guessField.setBackground(new Color(231, 76, 60, 50));
        }
    }

    private void handlePowerUp() {
        String powerUpResult = gameSession.usePowerUp();
        animateButton(powerUpButton);
        
        // Add visual feedback for power-up
        if (powerUpResult.contains("No Power-Ups")) {
            messageLabel.setForeground(new Color(231, 76, 60)); // Red
        } else {
            messageLabel.setForeground(new Color(52, 152, 219)); // Blue
        }
        
        messageLabel.setText(powerUpResult);
        updateUI();
    }

    private void startNewGame() {
        animateButton(restartButton);
        gameSession = new GameSession(1, 100, 10);
        playerStats = new PlayerStats();
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setText("🎮 Welcome! Guess a number between 1 and 100");
        guessField.setBackground(Color.WHITE);
        updateUI();
    }

    private void updateUI() {
        updateStatsPanel();
        updateAchievementsPanel();
        repaint();
    }
}
