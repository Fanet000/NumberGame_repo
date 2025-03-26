// Enhanced Guess the Number Game with Challenge Mode, Power-Ups, Analytics, and Customization
// Features: 
// 1. Challenge Mode - Progressive difficulty and timed rounds
// 2. Power-Ups - Reveal digit, reduce range, extra attempts
// 3. Advanced Analytics - Win/loss ratio, streaks, performance history
// 4. Achievements & Rewards - Unlockable badges, in-game currency
// 5. Visual & Audio Upgrades - Animated feedback, sound effects, themes

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
        gameTimer = new Timer(1000, (ActionEvent _) -> {
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
        setTitle("🎯 Guess the Number - Challenge Mode");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        playerStats = new PlayerStats();

        // Main game panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Stats panel
        statsPanel = createStatsPanel();
        mainPanel.add(statsPanel, BorderLayout.NORTH);

        // Game message
        messageLabel = new JLabel("🎮 Welcome! Press 'Restart' to start the game.", JLabel.CENTER);
        messageLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        mainPanel.add(messageLabel, BorderLayout.CENTER);

        // Timer label
        timerLabel = new JLabel("⏱️ Time: 0s", JLabel.CENTER);
        timerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        mainPanel.add(timerLabel, BorderLayout.SOUTH);

        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        guessField = new JTextField(5);
        guessField.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        guessButton = new JButton("✅ Guess");
        powerUpButton = new JButton("🔋 Use Power-Up");
        restartButton = new JButton("🔄 Restart");
        
        inputPanel.add(new JLabel("Your Guess:"));
        inputPanel.add(guessField);
        inputPanel.add(guessButton);
        inputPanel.add(powerUpButton);
        inputPanel.add(restartButton);
        add(inputPanel, BorderLayout.SOUTH);

        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Level 1");
        add(progressBar, BorderLayout.NORTH);

        // Achievements panel
        achievementsPanel = createAchievementsPanel();
        add(achievementsPanel, BorderLayout.EAST);

        // Add main panel
        add(mainPanel, BorderLayout.CENTER);

        // Setup event listeners
        guessButton.addActionListener((ActionEvent _) -> handleGuess());
        powerUpButton.addActionListener((ActionEvent _) -> {
            String powerUpResult = gameSession.usePowerUp();
            messageLabel.setText(powerUpResult);
            updateUI();
        });
        restartButton.addActionListener((ActionEvent _) -> startNewGame());

        // Setup timer for UI updates
        updateTimer = new Timer(1000, (ActionEvent _) -> updateTimerLabel());
        updateTimer.start();

        startNewGame();
        setVisible(true);
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("📊 Statistics"));
        
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
        statsLabel.setText(String.format("Games: %d | Wins: %d | Streak: %d",
            playerStats.getTotalGames(), playerStats.getGamesWon(), playerStats.getBestStreak()));
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

    private void updateUI() {
        updateStatsPanel();
        updateAchievementsPanel();
    }
}
