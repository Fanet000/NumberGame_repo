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
        // Use SwingUtilities.invokeLater to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                // Set the system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Create and show the game UI
                EnhancedGameUI gameUI = new EnhancedGameUI();
                gameUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameUI.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error starting the game: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
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
    private Color accentColor = new Color(46, 204, 113);

    public EnhancedGameUI() {
        // Set up the main window
        setTitle("Guess the Number - Challenge Mode");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        
        // Initialize game session and player stats
        gameSession = new GameSession(1, 100, 10);
        playerStats = new PlayerStats();
        
        // Create main game panel
        JPanel mainGamePanel = new JPanel();
        mainGamePanel.setLayout(new BorderLayout(10, 10));
        mainGamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create top panel for stats
        JPanel topPanel = new JPanel(new BorderLayout());
        statsPanel = createStatsPanel();
        topPanel.add(statsPanel, BorderLayout.WEST);
        progressBar = new JProgressBar(0, 100);
        styleProgressBar(progressBar);
        topPanel.add(progressBar, BorderLayout.CENTER);
        mainGamePanel.add(topPanel, BorderLayout.NORTH);

        // Create center panel for game controls
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add game title
        JLabel titleLabel = new JLabel("Guess The Number");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Add message label
        messageLabel = new JLabel("Guess a number between 1 and 100");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(messageLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Add timer label
        timerLabel = new JLabel("Time: 60s");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(timerLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Create input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        // Add guess field
        guessField = new JTextField(5);
        guessField.setFont(new Font("Arial", Font.PLAIN, 20));
        inputPanel.add(guessField);

        // Add buttons
        guessButton = new JButton("Guess");
        powerUpButton = new JButton("Use Power-Up");
        restartButton = new JButton("New Game");

        styleButton(guessButton);
        styleButton(powerUpButton);
        styleButton(restartButton);

        inputPanel.add(guessButton);
        inputPanel.add(powerUpButton);
        inputPanel.add(restartButton);

        centerPanel.add(inputPanel);
        mainGamePanel.add(centerPanel, BorderLayout.CENTER);

        // Add achievements panel at the bottom
        achievementsPanel = createAchievementsPanel();
        mainGamePanel.add(achievementsPanel, BorderLayout.SOUTH);

        // Add action listeners
        guessButton.addActionListener(_ -> handleGuess());
        powerUpButton.addActionListener(_ -> handlePowerUp());
        restartButton.addActionListener(_ -> startNewGame());

        // Add main panel to frame
        add(mainGamePanel);

        // Set up timer for UI updates
        updateTimer = new Timer(1000, _ -> {
            updateTimerLabel();
            updateProgressBar();
        });
        updateTimer.start();

        // Pack and show
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(41, 128, 185));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setOpaque(true);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
        });
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        
        statsLabel = new JLabel("Games: 0 | Wins: 0 | Streak: 0");
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
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

    private void styleProgressBar(JProgressBar bar) {
        bar.setPreferredSize(new Dimension(300, 20));
        bar.setForeground(new Color(46, 204, 113));
        bar.setBackground(new Color(236, 240, 241));
        bar.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        bar.setStringPainted(true);
        bar.setFont(new Font("Arial", Font.BOLD, 12));
    }
}
