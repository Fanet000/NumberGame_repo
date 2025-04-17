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
import java.util.Random;

public class GuessTheNumberChallenge {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new SimpleGameUI();
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

class SimpleGameUI extends JFrame {
    private int targetNumber;
    private int attempts;
    private JTextField guessField;
    private JLabel messageLabel, timerLabel;
    private JButton guessButton, powerUpButton, restartButton;
    private Timer gameTimer;
    private final int MAX_ATTEMPTS = 10;
    private int timeLeft = 20;
    private int totalGames = 0;
    private int gamesWon = 0;
    private int currentStreak = 0;
    private int totalGuesses = 0;
    private int coins = 0;
    private int powerUps = 3;
    private JLabel levelLabel;
    private JProgressBar levelBar;
    private JLabel gamesStatsLabel, avgGuessesLabel, winRateLabel, coinsLabel;

    public SimpleGameUI() {
        setTitle("Guess the Number - Challenge Mode");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setSize(800, 600);
        getContentPane().setBackground(Color.WHITE);

        // Create main panel with white background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create level panel at the top
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS));
        levelPanel.setBackground(Color.WHITE);
        levelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add level label
        levelLabel = new JLabel("Level 1");
        levelLabel.setForeground(new Color(51, 153, 255));
        levelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelPanel.add(levelLabel);

        // Add level progress bar
        levelBar = new JProgressBar(0, MAX_ATTEMPTS);
        levelBar.setPreferredSize(new Dimension(200, 5));
        levelBar.setMaximumSize(new Dimension(200, 5));
        levelBar.setForeground(new Color(51, 153, 255));
        levelBar.setBackground(Color.WHITE);
        levelBar.setBorderPainted(false);
        levelBar.setValue(0);
        levelPanel.add(Box.createVerticalStrut(5));
        levelPanel.add(levelBar);

        // Create stats panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240)),
            "📊 Statistics"
        ));
        statsPanel.setBackground(Color.WHITE);

        // Stats content panel
        JPanel statsContent = new JPanel(new GridLayout(2, 2, 10, 5));
        statsContent.setBackground(Color.WHITE);
        statsContent.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Stats labels
        gamesStatsLabel = new JLabel("Games: 0 | Wins: 0 | Streak: 0");
        winRateLabel = new JLabel("Win Rate: 0%");
        avgGuessesLabel = new JLabel("Avg Guesses: 0");
        coinsLabel = new JLabel("💰 Coins: 0");

        // Set font for all stats labels
        Font statsFont = new Font("Segoe UI", Font.PLAIN, 12);
        gamesStatsLabel.setFont(statsFont);
        winRateLabel.setFont(statsFont);
        avgGuessesLabel.setFont(statsFont);
        coinsLabel.setFont(statsFont);

        // Add stats labels to content panel
        statsContent.add(gamesStatsLabel);
        statsContent.add(winRateLabel);
        statsContent.add(avgGuessesLabel);
        statsContent.add(coinsLabel);

        statsPanel.add(statsContent);

        // Create message label (centered)
        messageLabel = new JLabel("🎯 Level 1: Guess a number between 1 and 100");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create timer label (centered)
        timerLabel = new JLabel("⏱️ Time: 20s");
        timerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create input panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputPanel.setBackground(Color.WHITE);

        JLabel guessLabel = new JLabel("Your Guess:");
        guessLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        guessField = new JTextField(8);
        guessField.setPreferredSize(new Dimension(100, 25));
        guessField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        guessField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        guessButton = new JButton("✓ Guess");
        powerUpButton = new JButton("⚡ Use Power-Up");
        restartButton = new JButton("↺ Restart");

        // Style buttons
        for (JButton button : new JButton[]{guessButton, powerUpButton, restartButton}) {
            button.setBackground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            button.setBorderPainted(true);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            button.setPreferredSize(new Dimension(button.getPreferredSize().width, 25));
        }

        inputPanel.add(guessLabel);
        inputPanel.add(guessField);
        inputPanel.add(guessButton);
        inputPanel.add(powerUpButton);
        inputPanel.add(restartButton);

        // Add components to main panel with exact spacing
        mainPanel.add(levelPanel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(statsPanel);
        mainPanel.add(Box.createVerticalStrut(80));
        mainPanel.add(messageLabel);
        mainPanel.add(Box.createVerticalStrut(80));
        mainPanel.add(timerLabel);
        mainPanel.add(Box.createVerticalStrut(40));
        mainPanel.add(inputPanel);

        // Add main panel to frame
        add(mainPanel);

        // Add action listeners
        guessButton.addActionListener(_ -> handleGuess());
        powerUpButton.addActionListener(_ -> usePowerUp());
        restartButton.addActionListener(_ -> startNewGame());
        guessField.addActionListener(_ -> handleGuess());

        // Create timer
        gameTimer = new Timer(1000, _ -> {
            timeLeft--;
            timerLabel.setText("⏱️ Time: " + timeLeft + "s");
            if (timeLeft <= 0) {
                gameTimer.stop();
                handleGameOver(false);
            }
        });

        // Start new game
        startNewGame();

        // Center the frame on screen
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startNewGame() {
        targetNumber = new Random().nextInt(100) + 1;
        attempts = 0;
        timeLeft = 20;
        powerUps = 3;
        messageLabel.setText("🎯 Level 1: Guess a number between 1 and 100");
        guessField.setText("");
        guessField.setEnabled(true);
        levelLabel.setText("Level 1");
        levelBar.setValue(0);
        powerUpButton.setText("⚡ Use Power-Up");
        gameTimer.start();
        guessField.requestFocus();
    }

    private void handleGuess() {
        if (!guessField.isEnabled()) return;
        
        try {
            int guess = Integer.parseInt(guessField.getText());
            attempts++;
            totalGuesses++;
            
            // Update level progress bar
            levelBar.setValue(attempts);
            
            if (guess == targetNumber) {
                gameTimer.stop();
                handleGameOver(true);
            } else if (attempts >= MAX_ATTEMPTS) {
                gameTimer.stop();
                handleGameOver(false);
            } else if (guess < targetNumber) {
                messageLabel.setText("📉 Too low! Tries left: " + (MAX_ATTEMPTS - attempts));
            } else {
                messageLabel.setText("📈 Too high! Tries left: " + (MAX_ATTEMPTS - attempts));
            }
            
            guessField.setText("");
            guessField.requestFocus();
        } catch (NumberFormatException ex) {
            messageLabel.setText("❌ Enter a valid number!");
        }
    }

    private void handleGameOver(boolean won) {
        totalGames++;
        guessField.setEnabled(false);
        
        if (won) {
            gamesWon++;
            currentStreak++;
            coins += 10;
            messageLabel.setText("🎉 Correct! You won in " + attempts + " attempts!");
        } else {
            currentStreak = 0;
            messageLabel.setText("❌ Game Over! The number was " + targetNumber);
        }
        
        updateStats();
    }

    private void usePowerUp() {
        if (coins >= 5) {
            coins -= 5;
            
            // Simple power-up: Reveal if number is in first or second half of range
            if (targetNumber <= 50) {
                messageLabel.setText("💡 Power-Up: The number is between 1 and 50");
            } else {
                messageLabel.setText("💡 Power-Up: The number is between 51 and 100");
            }
            
            updateStats();
        } else {
            messageLabel.setText("❌ Not enough coins! You need 5 coins to use a power-up.");
        }
    }

    private void updateStats() {
        double avgGuesses = totalGames == 0 ? 0 : (double) totalGuesses / totalGames;
        double winRate = totalGames == 0 ? 0 : (double) gamesWon / totalGames * 100;
        
        gamesStatsLabel.setText(String.format("Games: %d | Wins: %d | Streak: %d", totalGames, gamesWon, currentStreak));
        avgGuessesLabel.setText(String.format("Avg Guesses: %.1f", avgGuesses));
        winRateLabel.setText(String.format("Win Rate: %.1f%%", winRate));
        coinsLabel.setText("💰 Coins: " + coins);
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

    public EnhancedGameUI() {
        // Set up the main window
        setTitle("🎯 Guess the Number - Challenge Mode");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));

        // Initialize game session and player stats
        gameSession = new GameSession(1, 100, 10);
        playerStats = new PlayerStats();

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create stats panel
        statsPanel = createStatsPanel();
        mainPanel.add(statsPanel, BorderLayout.NORTH);

        // Create center panel
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
        messageLabel = new JLabel("Enter your guess!");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(messageLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Add timer label
        timerLabel = new JLabel("⏱️ Time: 60s");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(timerLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Create input panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        guessField = new JTextField(5);
        guessField.setFont(new Font("Arial", Font.PLAIN, 20));
        guessButton = new JButton("✅ Guess");
        powerUpButton = new JButton("🔋 Use Power-Up");
        restartButton = new JButton("🔄 Restart");

        inputPanel.add(new JLabel("Your Guess:"));
        inputPanel.add(guessField);
        inputPanel.add(guessButton);
        inputPanel.add(powerUpButton);
        inputPanel.add(restartButton);

        centerPanel.add(inputPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Create progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Level 1");
        mainPanel.add(progressBar, BorderLayout.SOUTH);

        // Create achievements panel
        achievementsPanel = createAchievementsPanel();
        add(achievementsPanel, BorderLayout.EAST);

        // Add main panel
        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners
        guessButton.addActionListener(_ -> handleGuess());
        powerUpButton.addActionListener(_ -> handlePowerUp());
        restartButton.addActionListener(_ -> startNewGame());

        // Set up timer for UI updates
        updateTimer = new Timer(1000, _ -> {
            updateTimerLabel();
            updateProgressBar();
        });
        updateTimer.start();

        // Initialize game
        startNewGame();

        // Pack and show
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240)),
            "📊 Statistics"
        ));
        
        statsLabel = new JLabel("Games: 0 | Wins: 0 | Streak: 0");
        JLabel winRateLabel = new JLabel("Win Rate: 0%");
        JLabel avgGuessesLabel = new JLabel("Avg Guesses: 0");
        JLabel coinsLabel = new JLabel("💰 Coins: 0");

        panel.add(statsLabel);
        panel.add(winRateLabel);
        panel.add(avgGuessesLabel);
        panel.add(coinsLabel);

        return panel;
    }

    private JPanel createAchievementsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("🏆 Achievements"));
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
            statsLabel.setText(String.format("Games: %d | Wins: %d | Streak: %d",
                playerStats.getTotalGames(), playerStats.getGamesWon(), playerStats.getBestStreak()));
        }
    }

    private void handleGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText());
            String feedback = gameSession.checkGuess(guess);
            messageLabel.setText(feedback);
            progressBar.setValue((gameSession.getAttempts() * 100) / gameSession.getMaxAttempts());

            if (gameSession.isGameWon()) {
                playerStats.updateStats(true, gameSession.getAttempts());
                messageLabel.setText(String.format("🏆 You won in %d attempts!", gameSession.getAttempts()));
                updateUI();
            } else if (gameSession.isTimeUp() || gameSession.getAttempts() >= gameSession.getMaxAttempts()) {
                playerStats.updateStats(false, gameSession.getAttempts());
                updateUI();
            }
        } catch (NumberFormatException ex) {
            messageLabel.setText("❌ Enter a valid number!");
        }
    }

    private void handlePowerUp() {
        String powerUpResult = gameSession.usePowerUp();
        messageLabel.setText(powerUpResult);
        updateUI();
    }

    private void startNewGame() {
        if (gameSession != null && gameSession.isGameWon()) {
            gameSession.increaseDifficulty();
        }
        gameSession = new GameSession(1, 100, 10);
        messageLabel.setText(String.format("🔢 Level %d: Guess a number between %d and %d",
            gameSession.getLevel(), gameSession.getMinRange(), gameSession.getMaxRange()));
        progressBar.setValue(0);
        progressBar.setString("Level " + gameSession.getLevel());
        guessField.setText("");
        updateUI();
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
        }
    }

    private void updateUI() {
        updateStatsPanel();
        updateAchievementsPanel();
        repaint();
    }
}
